/*
 *  Copyright (C) 2013-2017 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.app.scan.application;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dove.sample.app.scan.Const;
import com.dove.R;
//import com.dove.MainActivity;
import com.dove.sample.app.scan.activity.TopActivity;
import com.dove.sample.app.scan.activity.PreviewActivity;
import com.dove.sample.app.scan.application.StorageSettingDataHolder.StorageSendData;
import com.dove.sample.app.scan.application.StorageSettingDataHolder.StorageStoreData;
import com.dove.sample.function.common.SmartSDKApplication;
import com.dove.sample.function.scan.attribute.HashScanRequestAttributeSet;
import com.dove.sample.function.scan.attribute.ScanException;
import com.dove.sample.function.scan.attribute.ScanJobAttributeSet;
import com.dove.sample.function.scan.attribute.ScanRequestAttributeSet;
import com.dove.sample.function.scan.attribute.ScanResponseException;
import com.dove.sample.function.scan.attribute.standard.AutoCorrectJobSetting;
import com.dove.sample.function.scan.attribute.standard.DestinationSetting;
import com.dove.sample.function.scan.attribute.standard.FileSetting;
import com.dove.sample.function.scan.attribute.standard.JobMode;
import com.dove.sample.function.scan.attribute.standard.ScanJobScanningInfo;
import com.dove.sample.function.scan.attribute.standard.ScanJobState;
import com.dove.sample.function.scan.attribute.standard.ScanJobStateReason;
import com.dove.sample.function.scan.attribute.standard.ScanJobStateReasons;
import com.dove.sample.function.scan.attribute.standard.SendStoredFileSetting;
import com.dove.sample.function.scan.attribute.standard.SendStoredFileSetting.FolderInfo;
import com.dove.sample.function.scan.attribute.standard.SendStoredFileSetting.StoredFileInfo;
import com.dove.sample.function.scan.attribute.standard.StoreLocalSetting;

import java.util.Map;

/**
 * スキャンサンプルアプリのステートマシン
 * [処理内容]
 * ジョブのハンドリングを行います。
 * また、起動時及びジョブ発行後の以下のダイアログの生成・表示・破棄はすべてこのステートマシンを通して実行します。
 *   ・お待ち下さいダイアログ
 *   ・スキャン中ダイアログ
 *   ・次原稿待ちダイアログ
 *   ・プレビュー画面
 *
 * State machine of scan sample application.
 * Handles the Scan job.
 * Display/update/hide processes of the following dialog/screen are always executed by this statemachine.
 *   - Please wait dialog
 *   - Scanning dialog
 *   - Dialog asking for next original(s)
 *   - Preview screen
 */
public class ScanStateMachine {

    /**
     * システムリセット抑止状態を設定するためのアプリケーション内部インテントのアクション名です。
     * スキャンジョブの開始時に発行します。
     */
    public static final String INTENT_ACTION_LOCK_SYSTEM_RESET = "com.dove.sample.app.scan.LOCK_SYSTEM_RESET";

    /**
     * システムリセット抑止状態を解除するためのアプリケーション内部インテントのアクション名です。
     * スキャンジョブの終了時、プレビュー画面遷移時に発行します。
     */
    public static final String INTENT_ACTION_UNLOCK_SYSTEM_RESET = "com.dove.sample.app.scan.UNLOCK_SYSTEM_RESET";

    /**
     * デフォルトのダイアログの横幅
     * Default dialog width
     */
    private static final int DEFAULT_DIALOG_WIDTH = 400;

    /**
     * メインアクティビティへの参照
     * Reference to Main activity
     */
    private static Activity mActivity;

    /**
     * お待ちくださいダイアログ
     * "Please wait" dialog
     */
    private ProgressDialog mPleaseWaitDialog;

    /**
     * 初期化失敗ダイアログ
     * Boot failed dialog
     */
    private AlertDialog mBootFailedDialog;

    /**
     * スキャン中ダイアログ
     * Scanning dialog
     */
    protected ProgressDialog mScanningDialog;

    /**
     * カウントダウンあり次原稿待ちダイアログ
     * "Set next original" dialog : with count down
     */
    protected Dialog mCountDownDialog;

    /**
     * カウントダウンなし次原稿待ちダイアログ
     * "Set next original" dialog: without count down
     */
    protected Dialog mNoCountDownDialog;

    /**
     * ジョブ停止中ダイアログ
     * Job paused dialog
     */
    protected AlertDialog mJobStoppedDialog;

    /**
     * スキャンサンプルアプリケーション
     * Scan sample application object
     */
    private static ScanSampleApplication mApplication;

    /**
     * UIスレッドのハンドラー
     * UI thread handler
     */
    private Handler mHandler;

    private AfterPowerModeLock mAfterPowerModeLock;

    public ScanStateMachine(ScanSampleApplication app, Handler handler) {
        mApplication = app;
        mHandler = handler;
        mAfterPowerModeLock = null;
    }

    /**
     * メインアクティビティを登録します。
     * Registers the MainActivity.
     *
     * @param act MainActivity
     */
    public void registActivity(Activity act) {
        mActivity = act;
    }

    /**
     * 状態遷移イベント
     * State transition event
     */
    public enum ScanEvent {

        /**
         * ジョブ状態がPENDING（ジョブ実行前）に遷移したことを示すイベント
         * The event to indicate that the job state has changed to PENDING (before the job starts)
         */
        CHANGE_JOB_STATE_PENDING,

        /**
         * ジョブ状態がPROCESSING（ジョブ実行中）に遷移したことを示すイベント
         * The event to indicate that the job state has changed to PROCESSING (the job is processing)
         */
        CHANGE_JOB_STATE_PROCESSING,

        /**
         * ジョブ状態がABORTED（システム側で読取中止）に遷移したことを示すイベント
         * The event to indicate that the job state has changed to ABORTED (the job is aborted by system)
         */
        CHANGE_JOB_STATE_ABORTED,

        /**
         * ジョブ状態がCANCELED（ユーザー操作で読取中止）に遷移したことを示すイベント
         * The event to indicate that the job state has changed to CANCELED (the job is canceled by user)
         */
        CHANGE_JOB_STATE_CANCELED,

        /**
         * 送信前プレビュー状態に遷移したことを示すイベント
         * The event to indicate that the job state has changed to the preview state before the data is sent
         */
        CHANGE_JOB_STATE_STOPPED_PREVIEW,

        /**
         * 一時停止中のカウントダウン状態に遷移したことを示すイベント
         * The event to indicate that the job state has changed to the countdown state for pausing
         */
        CHANGE_JOB_STATE_STOPPED_COUNTDOWN,

        /**
         * その他の一時停止状態に遷移したことを示すイベント
         * The event to indicate that the job state has changed to the pausing state for other reason
         */
        CHANGE_JOB_STATE_STOPPED_OTHER,

        /**
         * ジョブ状態がCOMPLETED（ジョブ正常終了）に遷移したことを示すイベント
         * The event to indicate that the job state has changed to COMPLETED (the job ended successfully)
         */
        CHANGE_JOB_STATE_COMPLETED,


        /**
         * ジョブ実行のリクエストイベント
         * Job start request event
         */
        REQUEST_JOB_START,

        /**
         * ジョブキャンセルのリクエストイベント
         * Job cancel request event
         */
        REQUEST_JOB_CANCEL,

        /**
         * ジョブ一時停止のリクエストイベント
         * Job stop request event
         */
        REQUEST_JOB_STOP,

        /**
         * ジョブ続行のリクエストイベント
         * Job continue request event
         */
        REQUEST_JOB_CONTINUE,

        /**
         * ジョブ終了のリクエストイベント
         * Job end request event
         */
        REQUEST_JOB_END,


        /**
         * スキャン実行中の情報変更イベント
         * Information change event for scan job in process
         */
        UPDATE_JOB_STATE_PROCESSING,

        /**
         * アクティビティ生成イベント
         * MainActivity created event
         */
        ACTIVITY_CREATED,

        /**
         * アクティビティ終了イベント
         * MainActivity destroyed event
         */
        ACTIVITY_DESTROYED,


        /**
         * 初期化完了イベント
         * Initialization completed event
         */
        ACTIVITY_BOOT_COMPLETED,

        /**
         * 初期化失敗イベント
         * Initialization failed event
         */
        ACTIVITY_BOOT_FAILED,

        /**
         * ジョブリセット完了イベント
         * Job reset completed event
         */
        REBOOT_COMPLETED,
        
        /**
         * Job canceled by user
         */
        CHANGE_JOB_STATE_CANCELED_OTHER,
    }

    /**
     * ステートマシンの初期状態
     * StateMachine initial state
     */
    private State mState = State.INITIAL;

    /**
     * Define the prefix for log information with abbreviation package and class name
     */
    private final static String PREFIX = "application:StateMachine:";

    /**
     * 状態定義
     * State definition
     */
    public enum State {
        /**
         * 初期状態
         * Initial state
         */
        INITIAL {
            @Override
            public State getNextState(final ScanStateMachine sm, final ScanEvent event, final Object prm) {
                switch (event) {
                case ACTIVITY_CREATED:
                    actShowPleaseWait(sm, prm);
                    return INITIAL;
                case ACTIVITY_BOOT_COMPLETED:
                    actClosePleaseWait(sm, prm);
                    return IDLE;
                case ACTIVITY_BOOT_FAILED:
                    actClosePleaseWait(sm, prm);
                    actShowBootFailedDialog(sm, prm);
                    return INITIAL;
                default:
                    return super.getNextState(sm, event, prm);
                }
            }
        },
        /**
         * ジョブ生成後のジョブ開始前状態
         * The state before the job is started after the job has been created
         */
        IDLE {
            @Override
            public State getNextState(final ScanStateMachine sm, final ScanEvent event, final Object prm) {
                switch (event) {
                    case CHANGE_JOB_STATE_PENDING :
                        return JOB_PENDING;
                    case REQUEST_JOB_START :
                        return WAITING_JOB_START;
                default:
                    return super.getNextState(sm, event, prm);
                }
            }
        },
        /**
         * ジョブ開始待ち状態
         * Job start waiting state
         */
        WAITING_JOB_START {
            @Override
            public State getNextState(final ScanStateMachine sm, final ScanEvent event, final Object prm) {
                switch (event) {
                case REQUEST_JOB_CANCEL:
                    return WAITING_JOB_CANCEL;

                case CHANGE_JOB_STATE_PENDING :
                    return JOB_PENDING;
                case CHANGE_JOB_STATE_PROCESSING :
                    return JOB_PROCESSING;

                case CHANGE_JOB_STATE_ABORTED :
                    return JOB_ABORTED;
                case CHANGE_JOB_STATE_CANCELED :
                    return JOB_CANCELED;

                case ACTIVITY_DESTROYED:
                    return WAITING_JOB_CANCEL;
                default:
                    return super.getNextState(sm, event, prm);
                }
            }
            @Override
            public void entry(final ScanStateMachine sm, final Object prm) {
                super.entry(sm, prm);
                actShowScanningDialog(sm, prm);
                sm.new StartScanJobTask().execute();
            }
        },
        /**
         * ジョブ開始後の実行前状態
         * The state before the job is started after the job has been started
         */
        JOB_PENDING {
            @Override
            public State getNextState(final ScanStateMachine sm, final ScanEvent event, final Object prm) {
                switch (event) {
                    case CHANGE_JOB_STATE_PROCESSING :
                        return JOB_PROCESSING;
                    case REQUEST_JOB_CANCEL:
                        return WAITING_JOB_CANCEL;

                    case CHANGE_JOB_STATE_ABORTED :
                        return JOB_ABORTED;
                    case CHANGE_JOB_STATE_CANCELED :
                        return JOB_CANCELED;

                    case ACTIVITY_DESTROYED:
                        return WAITING_JOB_CANCEL;
                default:
                    return super.getNextState(sm, event, prm);
                }
            }
        },
        /**
         * ジョブ実行中
         * Job processing
         */
        JOB_PROCESSING {
            @Override
            public State getNextState(final ScanStateMachine sm, final ScanEvent event, final Object prm) {
                switch (event) {
                    case CHANGE_JOB_STATE_ABORTED :
                        return JOB_ABORTED;
                    case CHANGE_JOB_STATE_CANCELED :
                        return JOB_CANCELED;

                    case CHANGE_JOB_STATE_STOPPED_PREVIEW :
                        return JOB_STOPPED_PREVIEW;
                    case CHANGE_JOB_STATE_STOPPED_COUNTDOWN :
                        return JOB_STOPPED_COUNTDOWN;
                    case CHANGE_JOB_STATE_STOPPED_OTHER :
                        return JOB_STOPPED_OTHER;

                    case CHANGE_JOB_STATE_COMPLETED :
                        return JOB_COMPLETED;
                    case REQUEST_JOB_STOP:
                        return WAITING_JOB_STOP;
                    case REQUEST_JOB_CANCEL:
                        return WAITING_JOB_CANCEL;
                    case UPDATE_JOB_STATE_PROCESSING :
                        return JOB_PROCESSING;

                    case ACTIVITY_DESTROYED:
                        return WAITING_JOB_CANCEL;
                    case CHANGE_JOB_STATE_CANCELED_OTHER:
                        return JOB_CANCELED_OTHER;
                    default:
                        return super.getNextState(sm, event, prm);
                }
            }
            @Override
            public void entry(final ScanStateMachine sm, final Object prm) {
                super.entry(sm, prm);
                actCloseWaitForNextOriginal(sm, prm);
                actCloseJobStoppedDialog(sm, prm);
                actClosePreview(sm, prm);
                actUpdateScanningDialog(sm, prm);
            }
        },
        /**
         * システム側によりジョブキャンセル
         * Job cancelling by system
         */
        JOB_ABORTED {
            public State getNextState(final ScanStateMachine sm, final ScanEvent event, final Object prm) {
                switch (event) {
                    case REBOOT_COMPLETED :
                        return IDLE;
                    default:
                        return super.getNextState(sm, event, prm);
                }
            }
            @Override
            public void entry(final ScanStateMachine sm, final Object prm) {
                super.entry(sm, prm);
                actCloseScanningDialog(sm,prm);
                actCloseJobStoppedDialog(sm, prm);
                actCloseWaitForNextOriginal(sm,prm);
                actClosePreview(sm,prm);

                // show toast message with aborted reason
                String message = "job aborted.";
                if (prm instanceof ScanJobAttributeSet) {
                    ScanJobAttributeSet attributes = (ScanJobAttributeSet) prm;
                    ScanJobStateReasons reasons = (ScanJobStateReasons) attributes.get(ScanJobStateReasons.class);
                    if (reasons != null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(message);
                        sb.append(System.getProperty("line.separator"));
                        sb.append(reasons.getReasons().toString());
                        message = sb.toString();
                    }
                }
                actShowToastMessage(sm, message);
                // set application state to normal
                mApplication.setAppState(TopActivity.class.getName(), SmartSDKApplication.APP_STATE_NORMAL, SmartSDKApplication.APP_STATE_NORMAL_MSG, SmartSDKApplication.APP_TYPE_SCANNER);
                actInitJobSetting(sm, prm);
            }
        },
        /**
         * ジョブキャンセル中
         * Job cancelling
         */
        JOB_CANCELED {
            public State getNextState(final ScanStateMachine sm, final ScanEvent event, final Object prm) {
                switch (event) {
                    case CHANGE_JOB_STATE_PENDING:
                        return JOB_PENDING;
                    case REBOOT_COMPLETED :
                        return IDLE;
                    default:
                        return super.getNextState(sm, event, prm);
                }
            }
            @Override
            public void entry(final ScanStateMachine sm, final Object prm) {
                super.entry(sm, prm);
                actCloseScanningDialog(sm,prm);
                actCloseJobStoppedDialog(sm, prm);
                actCloseWaitForNextOriginal(sm,prm);
                actClosePreview(sm,prm);
                actInitJobSetting(sm, prm);
            }
        },
        /**
         * ジョブ一時停止中(次原稿待ちダイアログ表示)
         * Job pausing (display "set next original" dialog)
         */
        JOB_STOPPED_COUNTDOWN {
            @Override
            public State getNextState(final ScanStateMachine sm, final ScanEvent event, final Object prm) {
                switch (event) {
                    case REQUEST_JOB_CONTINUE:
                        return WAITING_JOB_CONTINUE;
                    case REQUEST_JOB_CANCEL:
                        return WAITING_JOB_CANCEL;
                    case REQUEST_JOB_END:
                        return WAITING_JOB_END;

                    case CHANGE_JOB_STATE_PROCESSING :
                        return JOB_PROCESSING;
                    case CHANGE_JOB_STATE_COMPLETED :
                        return JOB_COMPLETED;
                    case CHANGE_JOB_STATE_ABORTED :
                        return JOB_ABORTED;
                    case CHANGE_JOB_STATE_CANCELED :
                        return JOB_CANCELED;

                    case CHANGE_JOB_STATE_STOPPED_PREVIEW :
                        return JOB_STOPPED_PREVIEW;

                    case ACTIVITY_DESTROYED:
                        return WAITING_JOB_CANCEL;
                    default:
                        return super.getNextState(sm, event, prm);
                }
            }
            @Override
            public void entry(final ScanStateMachine sm, final Object prm) {
                super.entry(sm, prm);
                actCloseJobStoppedDialog(sm, prm);
                actWaitForNextOriginal(sm, prm);
            }
        },
        /**
         * ジョブ一時停止中（プレビュー表示）
         * Job pausing (display preview)
         */
        JOB_STOPPED_PREVIEW {
            @Override
            public State getNextState(final ScanStateMachine sm, final ScanEvent event, final Object prm) {
                switch (event) {
                case REQUEST_JOB_END:
                    return WAITING_JOB_END;
                case REQUEST_JOB_CANCEL:
                    return WAITING_JOB_CANCEL;

                case CHANGE_JOB_STATE_PROCESSING :
                    return JOB_PROCESSING;
                case CHANGE_JOB_STATE_COMPLETED :
                    return JOB_COMPLETED;
                case CHANGE_JOB_STATE_ABORTED :
                    return JOB_ABORTED;
                case CHANGE_JOB_STATE_CANCELED :
                    return JOB_CANCELED;

                case ACTIVITY_DESTROYED:
                    return WAITING_JOB_CANCEL;
                default:
                    return super.getNextState(sm, event, prm);
                }
            }
            @Override
            public void entry(final ScanStateMachine sm, final Object prm) {
                super.entry(sm, prm);
                actCloseJobStoppedDialog(sm, prm);
                actCloseWaitForNextOriginal(sm, prm);
                actShowPreview(sm, prm);

            }
        },
        /**
         * その他のジョブ一時停止
         * Job pausing (other)
         */
        JOB_STOPPED_OTHER {
            @Override
            public State getNextState(final ScanStateMachine sm, final ScanEvent event, final Object prm) {
                switch (event) {
                    case REQUEST_JOB_CONTINUE:
                        return WAITING_JOB_CONTINUE;
                    case REQUEST_JOB_CANCEL:
                        return WAITING_JOB_CANCEL;
                    case REQUEST_JOB_END:
                        return WAITING_JOB_END;

                    case CHANGE_JOB_STATE_STOPPED_COUNTDOWN :
                        return JOB_STOPPED_COUNTDOWN;
                    case CHANGE_JOB_STATE_STOPPED_PREVIEW :
                        return JOB_STOPPED_PREVIEW;

                    case CHANGE_JOB_STATE_PROCESSING :
                        return JOB_PROCESSING;
                    case CHANGE_JOB_STATE_COMPLETED :
                        return JOB_COMPLETED;
                    case CHANGE_JOB_STATE_ABORTED :
                        return JOB_ABORTED;
                    case CHANGE_JOB_STATE_CANCELED :
                        return JOB_CANCELED;

                    case ACTIVITY_DESTROYED:
                        return WAITING_JOB_CANCEL;
                    default:
                        return super.getNextState(sm, event, prm);
                }
            }
            @Override
            public void entry(final ScanStateMachine sm, final Object prm) {
                super.entry(sm, prm);
                actCloseWaitForNextOriginal(sm, prm);
                actShowJobStoppedDialog(sm, prm);
                
            }
        },
        
        /**
         *ジョブキャンセル待ち
         * Job waiting to be cancelled after user request
         */
        JOB_CANCELED_OTHER {
            @Override
            public State getNextState(final ScanStateMachine sm, final ScanEvent event, final Object prm) {
                switch (event) {
                case CHANGE_JOB_STATE_CANCELED :
                    return JOB_CANCELED;
                case CHANGE_JOB_STATE_COMPLETED :
                    return JOB_COMPLETED;
                case CHANGE_JOB_STATE_ABORTED :
                    return JOB_ABORTED;
                default:
                    return super.getNextState(sm, event, prm);
                }
            }
            @Override
            public void entry(final ScanStateMachine sm, final Object prm) {
                super.entry(sm,  prm);
                sm.new CancelScanJobTask().execute();
            }
        },
        /**
         * ジョブ一時停止待ち
         * Job waiting to be stopped
         */
        WAITING_JOB_STOP {
            @Override
            public State getNextState(final ScanStateMachine sm, final ScanEvent event, final Object prm) {
                switch (event) {
                case REQUEST_JOB_CANCEL:
                    return WAITING_JOB_CANCEL;

                case UPDATE_JOB_STATE_PROCESSING :
                    return JOB_PROCESSING;

                case CHANGE_JOB_STATE_STOPPED_PREVIEW :
                    return JOB_STOPPED_PREVIEW;
                case CHANGE_JOB_STATE_STOPPED_COUNTDOWN :
                    return JOB_STOPPED_COUNTDOWN;
                case CHANGE_JOB_STATE_STOPPED_OTHER :
                    return JOB_STOPPED_OTHER;

                case CHANGE_JOB_STATE_ABORTED :
                    return JOB_ABORTED;
                case CHANGE_JOB_STATE_CANCELED :
                    return JOB_CANCELED;
                case CHANGE_JOB_STATE_COMPLETED :
                    return JOB_COMPLETED;

                case ACTIVITY_DESTROYED:
                    return WAITING_JOB_CANCEL;
                default:
                    return super.getNextState(sm, event, prm);
                }
            }
            @Override
            public void entry(final ScanStateMachine sm, final Object prm) {
                super.entry(sm, prm);
                sm.new StopScanJobTask().execute();
            }
        },
        /**
         * ジョブ再開待ち
         * Job waiting to be resumed
         */
        WAITING_JOB_CONTINUE {
            @Override
            public State getNextState(final ScanStateMachine sm, final ScanEvent event, final Object prm) {
                switch (event) {
                    case CHANGE_JOB_STATE_PROCESSING :
                        return JOB_PROCESSING;

                    case CHANGE_JOB_STATE_COMPLETED :
                        return JOB_COMPLETED;
                    case CHANGE_JOB_STATE_ABORTED :
                        return JOB_ABORTED;
                    case CHANGE_JOB_STATE_CANCELED :
                        return JOB_CANCELED;

                    case ACTIVITY_DESTROYED:
                        return WAITING_JOB_CANCEL;
                default:
                    return super.getNextState(sm, event, prm);
                }
            }
            @Override
            public void entry(final ScanStateMachine sm, final Object prm) {
                super.entry(sm, prm);
                sm.new ContinueScanJobTask().execute();
            }
        },
        /**
         * ジョブキャンセル待ち
         * Job waiting to be cancelled
         */
        WAITING_JOB_CANCEL {
            @Override
            public State getNextState(final ScanStateMachine sm, final ScanEvent event, final Object prm) {
                switch (event) {
                    case CHANGE_JOB_STATE_CANCELED:
                        return JOB_CANCELED;

                    case CHANGE_JOB_STATE_PROCESSING :
                        return JOB_PROCESSING;
                    case CHANGE_JOB_STATE_COMPLETED :
                        return JOB_COMPLETED;
                    case CHANGE_JOB_STATE_ABORTED :
                        return JOB_ABORTED;
                default:
                    return super.getNextState(sm, event, prm);
                }
            }
            @Override
            public void entry(final ScanStateMachine sm, final Object prm) {
                super.entry(sm, prm);
                sm.new CancelScanJobTask().execute();
            }
        },
        /**
         * ジョブ終了待ち
         * Job waiting to be finished
         */
        WAITING_JOB_END {
            @Override
            public State getNextState(final ScanStateMachine sm, final ScanEvent event, final Object prm) {
                switch (event) {
                    case CHANGE_JOB_STATE_COMPLETED :
                        return JOB_COMPLETED;
                    case CHANGE_JOB_STATE_STOPPED_PREVIEW :
                        return JOB_STOPPED_PREVIEW;

                    case CHANGE_JOB_STATE_ABORTED :
                        return JOB_ABORTED;
                    case CHANGE_JOB_STATE_CANCELED :
                        return JOB_CANCELED;
                    /**
                     * Change machine state "WAITING_JOB_END" to "JOB_PENDING"
                     */
                    case CHANGE_JOB_STATE_PENDING :
                        return JOB_PENDING;
                default:
                    return super.getNextState(sm, event, prm);
                }
            }
            @Override
            public void entry(final ScanStateMachine sm, final Object prm) {
                super.entry(sm, prm);
                sm.new EndScanJobTask().execute();
            }
        },

        /**
         * ジョブ正常終了
         * Job completed successfully
         */
        JOB_COMPLETED {
            @Override
            public State getNextState(final ScanStateMachine sm, final ScanEvent event, final Object prm) {
                switch (event) {
                case REBOOT_COMPLETED :
                    return IDLE;
                default:
                    return super.getNextState(sm, event, prm);
                }
            }
            @Override
            public void entry(final ScanStateMachine sm, final Object prm) {
                super.entry(sm, prm);
                actCloseScanningDialog(sm,prm);
                actCloseJobStoppedDialog(sm, prm);
                actCloseWaitForNextOriginal(sm,prm);
                actClosePreview(sm,prm);
                actInitJobSetting(sm, prm);
            }
        },
        ;


        /**
         * 次の状態を取得します。
         * 各状態がオーバーライドします。
         * Obtains the next state.
         * Each state should override this method.
         *
         * @param sm
         * @param event
         * @param prm Object for additional information
         * @return
         */
        public State getNextState(final ScanStateMachine sm, final ScanEvent event, final Object prm) {
            switch (event) {
            default:
                return null;
            }
        }

        /**
         * 状態に入るときに呼ばれるメソッドです。
         * 各状態が必要に応じてオーバーライドします。
         * This method is called when entering a state.
         * Each state should override this method if necessary.
         *
         * @param sm
         * @param prm Object for additional information
         */
        public void entry(final ScanStateMachine sm, final Object prm) {
        }

        /**
         * 状態から抜けるときに呼ばれるメソッドです。
         * 各状態が必要に応じてオーバーライドします。
         * This method is called when exiting a state.
         * Each state should override this method if necessary.
         *
         * @param sm
         * @param prm Object for additional information
         */
        public void exit(final ScanStateMachine sm, final Object prm) {
        }


        // +++++++++++++++++++++++++++++++++++++++++
        // アクション関数
        // Action method
        // +++++++++++++++++++++++++++++++++++++++++

        /**
         * PleaseWait画面の表示
         * Displays please wait dialog
         */
        protected void actShowPleaseWait(final ScanStateMachine sm, final Object prm) {
            sm.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    sm.showPleaseWaitDialog();
                }
            });
        }

        /**
         * PleaseWait画面の消去
         * Hides please wait dialog
         */
        protected void actClosePleaseWait(final ScanStateMachine sm, final Object prm) {
            sm.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    sm.closePleaseWaitDialog();
                }
            });
        }

        /**
         * 初期化失敗画面の表示
         * Displays boot failed dialog
         */
        protected void actShowBootFailedDialog(final ScanStateMachine sm, final Object prm) {
            sm.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    sm.showBootFailedDialog();
                }
            });
        }

        /**
         * スキャン中画面の表示
         * Diaplays scanning dialog
         */
        protected void actShowScanningDialog(final ScanStateMachine sm, final Object prm) {
            sm.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    sm.showScanningDialog();
                }
            });
        }

        /**
         * スキャン中画面の消去
         * Hides scanning dialog
         */
        protected void actCloseScanningDialog(final ScanStateMachine sm, final Object prm) {
            sm.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    sm.closeScanningDialog();
                }
            });
        }

        /**
         * スキャン中画面の更新
         * Updates scanning dialog
         */
        protected void actUpdateScanningDialog(final ScanStateMachine sm, final Object prm) {
            if(prm instanceof ScanJobAttributeSet) {
                sm.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        sm.updateScanDialogViews((ScanJobAttributeSet)prm);
                    }
                });
            } else if(prm instanceof String) {
                sm.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        sm.updateScanDialogMessage((String)prm);
                    }
                });
            }
        }

        /**
         * 次原稿待ちダイアログの表示
         * Displays "set next original" dialog
         */
        protected void actWaitForNextOriginal(final ScanStateMachine sm, final Object prm) {
            if (mApplication.getTimeOfWaitingNextOriginal() == 0)
                sm.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        sm.showNoCountDownDialog();
                    }
                });
            else
                sm.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        sm.showCountDownDialog();
                    }
                });
        }

        /**
         * 次原稿待ちダイアログの消去
         * Hides "set next original" dialog
         */
        protected void actCloseWaitForNextOriginal(final ScanStateMachine sm, final Object prm) {
            sm.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    sm.closeCountDownDialog();
                    sm.closeNoCountDownDialog();
                }
            });
        }

        /**
         * プレビュー画面の表示
         * Displays preview screen
         */
        protected void actShowPreview(final ScanStateMachine sm, final Object prm) {
            sm.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    sm.showPreview();
                }
            });
        }

        /**
         * プレビュー画面の消去
         * Hides preview screen
         */
        protected void actClosePreview(final ScanStateMachine sm, final Object prm) {
            sm.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    sm.closePreview();
                }
            });
        }

        /**
         * その他のジョブ停止ダイアログの表示
         * Displays job paused dialog (for other reason)
         */
        protected void actShowJobStoppedDialog(final ScanStateMachine sm, final Object prm) {
            sm.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    sm.showJobStoppedDialog((String)prm);
                }
            });
        }

        /**
         * その他のジョブ停止ダイアログの消去
         * Hides job paused dialog (for other reason)
         */
        protected void actCloseJobStoppedDialog(final ScanStateMachine sm, final Object prm) {
            sm.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    sm.closeJobStoppedDialog();
                }
            });
        }

        /**
         * Toastメッセージ表示
         * Displays toast message
         */
        protected void actShowToastMessage(final ScanStateMachine sm, final Object prm) {
            sm.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    sm.showToastMessage((String)prm);
                }
            });
        }

        /**
         * スキャンジョブの初期化
         * Initializes scan job
         */
        protected void actInitJobSetting(final ScanStateMachine sm, final Object prm) {
            sm.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (sm.mAfterPowerModeLock != null) {
                        sm.mAfterPowerModeLock.finish();
                        sm.mAfterPowerModeLock = null;
                    }

                    /* unlock power mode*/
                    mApplication.unlockPowerMode();
                    /* unlock offline */
                    mApplication.unlockOffline();

                    /* システムリセット抑止状態を解除するための内部インテントを発行します */
                    Intent intent = new Intent(INTENT_ACTION_UNLOCK_SYSTEM_RESET);
                    intent.setPackage(mApplication.getPackageName());
                    mApplication.sendBroadcast(intent);

                    mApplication.initJobSetting();
                    sm.procScanEvent(ScanEvent.REBOOT_COMPLETED);

                    //Initialize job setting, reset flag to false
                    mApplication.setmIsWinShowing(false);
                }
            });
        }
    }


    /**
     * 状態遷移を行います。
     * Changes states.
     *
     * @param event
     */
    public void procScanEvent(final ScanEvent event) {
        procScanEvent(event, null);
    }

    /**
     * 状態遷移を行います。
     * Changes states.
     *
     * @param event
     * @param prm  Object for additional information
     */
    public void procScanEvent(final ScanEvent event, final Object prm) {
        Log.i(Const.TAG, PREFIX + ">evtp :" + event);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                State newState = mState.getNextState(ScanStateMachine.this, event, prm);
                if (newState != null) {
                    Log.i(Const.TAG, PREFIX + "#evtp :" + event + " state:" + mState + " > " + newState);
                    mState.exit(ScanStateMachine.this, prm);
                    mState = newState;
                    mState.entry(ScanStateMachine.this, prm);
                }
            }
        });
    }

    /*=============================================================
     * ステートマシンから呼ばれるpublicメソッド
     * public methos called by statemachine
     *=============================================================*/

    /**
     * ダイアログを指定された幅で表示します。
     * Displays the dialog in specified width.
     *
     * @param d dialog
     * @param width dialog width
     */
    private void showDialog(Dialog d, int width) {
        d.show();
        WindowManager.LayoutParams lp = d.getWindow().getAttributes();
        lp.width = width;
        d.getWindow().setAttributes(lp);
    }

    /**
     * ダイアログをデフォルトサイズで表示します。
     * Displays the dialog in default size.
     * @param d dialog
     */
    private void showDialog(Dialog d) {
        showDialog(d, DEFAULT_DIALOG_WIDTH);
    }

    /**
     * お待ち下さいダイアログを表示します。
     * Displays please wait dialog.
     */
    public void showPleaseWaitDialog() {
        if(mPleaseWaitDialog==null || mPleaseWaitDialog.isShowing()==false) {
            mPleaseWaitDialog = createPleaseWaitDialog();
            showDialog(mPleaseWaitDialog);
        }
    }

    /**
     * お待ち下さいダイアログを閉じます。
     * Hides please wait dialog.
     */
    public void closePleaseWaitDialog() {
        if(mPleaseWaitDialog!=null && mPleaseWaitDialog.isShowing()) {
            mPleaseWaitDialog.dismiss();
            mPleaseWaitDialog = null;
        }
    }

    /**
     * 初期化失敗ダイアログを表示します。
     * Displays boot failed dialog.
     */
    public void showBootFailedDialog() {
        if(mBootFailedDialog==null || mBootFailedDialog.isShowing()==false) {
            mBootFailedDialog = createBootFailedDialog();
            showDialog(mBootFailedDialog);
        }
    }

    /**
     * スキャン中ダイアログを表示します。
     * Displays scanning dialog.
     */
    public void showScanningDialog() {
        if( mScanningDialog==null || mScanningDialog.isShowing()==false) {
            mScanningDialog = createScanProgressDialog();
            showDialog(mScanningDialog);

            Button cancelButton = mScanningDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            if (cancelButton != null) {
                cancelButton.setEnabled(false);
            }

            Button stopButton = mScanningDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            if (stopButton != null) {
                stopButton.setEnabled(false);
                stopButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        procScanEvent(ScanEvent.REQUEST_JOB_STOP);
                    }
                });
            }
        }
    }

    /**
     * スキャン中ダイアログを閉じます。
     * Hides scanning dialog.
     */
    public void closeScanningDialog() {
        if(mScanningDialog!=null && mScanningDialog.isShowing()) {
            mScanningDialog.cancel();
            mScanningDialog = null;
        }
    }

    /**
     * カウントダウンあり次原稿待ちダイアログを表示します。
     * Displays "set next original" dialog (with count down)
     */
    public void showCountDownDialog() {
        if(mCountDownDialog==null || mCountDownDialog.isShowing()==false) {
            mCountDownDialog = createCountDownDialog();
            mCountDownDialog.show();
        }
    }

    /**
     * カウントダウンあり次原稿待ちダイアログを閉じます。
     * Hides "set next original" dialog (with count down)
     */
    public void closeCountDownDialog() {
        if(mCountDownDialog!=null && mCountDownDialog.isShowing()) {
            mCountDownDialog.cancel();
            mCountDownDialog = null;
        }
    }

    /**
     * カウントダウンなし次原稿待ちダイアログを表示します。
     * Displays "set next original" dialog (without count down)
     */
    public void showNoCountDownDialog() {
        if(mNoCountDownDialog==null || mNoCountDownDialog.isShowing()==false) {
            mNoCountDownDialog = createNoCountDownDialog();
            mNoCountDownDialog.show();
        }
    }

    /**
     * カウントダウンなし次原稿待ちダイアログを閉じます。
     * Hides "set next original" dialog (without count down)
     */
    public void closeNoCountDownDialog() {
        if(mNoCountDownDialog!=null && mNoCountDownDialog.isShowing()) {
            mNoCountDownDialog.cancel();
            mNoCountDownDialog = null;
        }
    }

    /**
     * ジョブ一時停止中のダイアログを表示します。
     * Displays job paused dialog.
     */
    public void showJobStoppedDialog(String reason) {
        if(mJobStoppedDialog==null || mJobStoppedDialog.isShowing()==false) {
            mJobStoppedDialog = createJobStoppedDialog(reason);
            mJobStoppedDialog.show();

            if (reason.contains(ScanJobStateReason.USER_REQUEST.toString())) {
                Button btn_end = mJobStoppedDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                btn_end.setEnabled(false);
            }
        }
    }

    /**
     * ジョブ一時停止中のダイアログを消去します。
     * Hides job paused dialog.
     */
    public void closeJobStoppedDialog() {
        if(mJobStoppedDialog!=null && mJobStoppedDialog.isShowing()) {
            mJobStoppedDialog.cancel();
            mJobStoppedDialog = null;
        }
    }

    /**
     * スキャンダイアログの表示を更新します。
     * Updates scanning dialog.
     */
    public void updateScanDialogViews(ScanJobAttributeSet attributes) {
        if(mScanningDialog!=null && mScanningDialog.isShowing()) {
            // "Cancel" button (Enable only scan job running.)
            Button cancelButton = mScanningDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            if (cancelButton != null) {
                boolean cancelButtonEnabled = false;
                ScanJobState jobState = (ScanJobState) attributes.get(ScanJobState.class);
                if (ScanJobState.PROCESSING.equals(jobState) || ScanJobState.PROCESSING_STOPPED.equals(jobState)) {
                    cancelButtonEnabled = true;
                }
                cancelButton.setEnabled(cancelButtonEnabled);
            }

            // "Stop" button (Enable only scanning.)
            Button stopButton = mScanningDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            if (stopButton != null) {
                boolean stopButtonEnabled = false;
                ScanJobScanningInfo scanningInfo = (ScanJobScanningInfo) attributes.get(ScanJobScanningInfo.class);
                if (scanningInfo != null && scanningInfo.getScanningState()==ScanJobState.PROCESSING) {
                    stopButtonEnabled = true;
                }
                stopButton.setEnabled(stopButtonEnabled);
            }
        }
    }

    /**
     * スキャンダイアログの表示を更新します。
     * Updates scanning dialog.
     */
    public void updateScanDialogMessage(String message) {
        if(mScanningDialog!=null && mScanningDialog.isShowing()) {
            mScanningDialog.setMessage(message);
        }
    }

    /**
     * プレビュー画面に遷移します。
     * Changes to the preview screen.
     */
    public void showPreview() {
        Intent intent = new Intent(mActivity, PreviewActivity.class);
        mActivity.startActivityForResult(intent, PreviewActivity.REQUEST_CODE_PREVIEW_ACTIVITY);
    }

    /**
     * プレビュー画面を消去します。
     * Hides preview screen
     */
    public void closePreview() {
        mActivity.finishActivity(PreviewActivity.REQUEST_CODE_PREVIEW_ACTIVITY);
    }

    /**
     * Toastメッセージを表示します。
     * Displays a toast message.
     */
    void showToastMessage(String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
    }

    /*=============================================================
     * ダイアログ生成
     * Creates dialog
     *=============================================================*/

    /**
     * しばらくお待ち下さいダイアログを生成します。
     * Creates the "please wait" dialog.
     */
    private ProgressDialog createPleaseWaitDialog() {
        ProgressDialog dialog = new ProgressDialog(mActivity);
        dialog.setMessage(mActivity.getString(R.string.txid_cmn_d_wait));
        dialog.setCancelable(false);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, mActivity.getString(R.string.txid_cmn_b_cancel),
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mActivity.finish();
                }
            });
        return dialog;
    }

    /**
     * 初期化失敗ダイアログを生成します。
     * Creates the "boot failed" dialog.
     */
    private AlertDialog createBootFailedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(R.string.txid_cmn_b_error);
        builder.setMessage(R.string.txid_cmn_d_cannot_connect);
        builder.setCancelable(false);
        builder.setNegativeButton(R.string.txid_cmn_b_close,
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mActivity.finish();
                }
            });
        return builder.create();
    }

    /**
     * スキャン中ダイアログを生成します。
     * Creates the scanning dialog.
     */
    private ProgressDialog createScanProgressDialog() {
        ProgressDialog dialog = new ProgressDialog(mActivity);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(mActivity.getString(R.string.txid_scan_d_scanning));
        dialog.setCancelable(false);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, mActivity.getString(R.string.txid_cmn_b_cancel),
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    procScanEvent(ScanEvent.CHANGE_JOB_STATE_CANCELED_OTHER);
                }
            });

        // Scanning stop feature is supported in SmartSDK V1.01 or later
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, mActivity.getString(R.string.txid_cmn_b_stop),
            (DialogInterface.OnClickListener)null);

        return dialog;
    }

    /**
     * 一時停止イベント中のカウントダウンダイアログを生成します。
     * Creates the "set next original" dialog (with countdown) for the pause event.
     */
   private Dialog createCountDownDialog() {
        final Dialog countDownDialog = new Dialog(mActivity);
        countDownDialog.setTitle("Scanning");
        countDownDialog.setContentView(R.layout.dlg_count_down);
        countDownDialog.show();

        final CountDownTimer timer = new CountDownTimer((mApplication.getTimeOfWaitingNextOriginal()+1)*1000, 1000) {
            @Override
            public void onTick(long millis) {
                ((TextView) countDownDialog.findViewById(R.id.txt_count_down))
                .setText(String.format(mActivity.getString(R.string.txid_scan_t_count_down_text),
                        millis / 1000));
            }

            @Override
            public void onFinish() {
                countDownDialog.dismiss();
                procScanEvent(ScanEvent.REQUEST_JOB_END);
            }
        }.start();

        countDownDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                timer.cancel();
            }
        });

        // continue button
        Button start = (Button) countDownDialog.findViewById(R.id.btn_count_down_start);
        start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                countDownDialog.dismiss();
                procScanEvent(ScanEvent.REQUEST_JOB_CONTINUE);
            }
        });

        // finish button
        Button finish = (Button) countDownDialog.findViewById(R.id.btn_count_down_finish);
        finish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                countDownDialog.dismiss();
                procScanEvent(ScanEvent.REQUEST_JOB_END);
            }
        });

        return countDownDialog;
    }

   /**
    * 一時停止イベント中のカウントダウンなしダイアログを生成します。
    * Creates "set next original" dialog (without countdown) for the pause event.
    */
  private Dialog createNoCountDownDialog() {
       final Dialog noCountDownDialog = new Dialog(mActivity);
       noCountDownDialog.setTitle("Scanning");
       noCountDownDialog.setContentView(R.layout.dlg_no_count_down);
       noCountDownDialog.show();

       // continue button
       Button start = (Button) noCountDownDialog.findViewById(R.id.btn_count_down_start);
       start.setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
               noCountDownDialog.dismiss();
               procScanEvent(ScanEvent.REQUEST_JOB_CONTINUE);
           }
       });

       // finish button
       Button finish = (Button) noCountDownDialog.findViewById(R.id.btn_count_down_finish);
       finish.setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
               noCountDownDialog.dismiss();
               procScanEvent(ScanEvent.REQUEST_JOB_END);
           }
       });

       return noCountDownDialog;
   }

  /**
   * ジョブ停止中ダイアログを生成します。
   * Creates job paused dialog.
   */
  private AlertDialog createJobStoppedDialog(String reason) {
     AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
     builder.setIcon(android.R.drawable.ic_dialog_alert);
     builder.setTitle("Job stopped");
     builder.setMessage(reason);
     builder.setCancelable(false);
     builder.setNegativeButton(R.string.txid_cmn_b_continue, new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialog, int which) {
             procScanEvent(ScanEvent.REQUEST_JOB_CONTINUE);
         }
     });
     builder.setNeutralButton(R.string.txid_cmn_b_end, new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialog, int which) {
             procScanEvent(ScanEvent.REQUEST_JOB_END);
         }
     });
     builder.setPositiveButton(R.string.txid_cmn_b_cancel, new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialog, int which) {
             procScanEvent(ScanEvent.REQUEST_JOB_CANCEL);
         }
     });

     return builder.create();
  }

   /*=============================================================
    * スキャンジョブ操作の非同期タスク
    * The asynchronous task to start the scan job.
    *=============================================================*/

   /**
    * ScanResponseExceptionのエラー情報を文字列化します。
    * フォーマットは以下の通りです。
    * Creates the string of the ScanResponseException error information.
    * The format is as below.
    *
    * base[separator]
    * [separator]
    * message_id: message[separator]
    * message_id: message[separator]
    * message_id: message
    *
    * @param e 文字列化対象のScanResponseException
    *          ScanResponseException to be converted as a string
    * @param base メッセージ先頭文字列
    *             Starting string of the message
    * @return メッセージ文字列
    *         Message string
    */
   private String makeJobErrorResponceMessage(ScanResponseException e, String base) {
       StringBuilder sb = new StringBuilder(base);
       if (e.hasErrors()) {
           Map<String, String> errors = e.getErrors();
           if (errors.size() > 0) {
               String separator = System.getProperty("line.separator");
               sb.append(separator);

               for (Map.Entry<String, String> entry : errors.entrySet()) {
                   sb.append(separator);
                   // message_id
                   sb.append(entry.getKey());
                   // message (if exists)
                   String message = entry.getValue();
                   if ((message != null) && (message.length() > 0)) {
                       sb.append(": ");
                       sb.append(message);
                   }
               }
           }
       }
       return sb.toString();
   }

   /**
    * スキャンジョブを実行開始するための非同期タスクです。
    * The asynchronous task to start the scan job.
    */
   private class StartScanJobTask extends AsyncTask<Void, Void, Boolean> {

       private String message = null;

       @Override
       protected Boolean doInBackground(Void... param) {
           // lock power mode
           if (!mApplication.lockPowerMode()) {
               Log.d(Const.TAG, PREFIX + "lockPowerMode failed. start after lock");
               mAfterPowerModeLock = new AfterPowerModeLock(mApplication);
               mAfterPowerModeLock.start();
           }
           // lock offline
           if (!mApplication.lockOffline()) {
               mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        showToastMessage("Error: cannot start scan job. lockOffline() failed.");
                    }
               });
               return false;
           }

           // ジョブ実行中はシステムリセット抑止状態とするための内部インテントを発行します
           Intent intent = new Intent(INTENT_ACTION_LOCK_SYSTEM_RESET);
           intent.setPackage(mApplication.getPackageName());
           mApplication.sendBroadcast(intent);

           // Sets scan attributes.
           ScanSettingDataHolder scanSetDataHolder = mApplication.getScanSettingDataHolder();
           ScanRequestAttributeSet requestAttributes;
           requestAttributes = new HashScanRequestAttributeSet();
           requestAttributes.add(AutoCorrectJobSetting.AUTO_CORRECT_ON);

           JobMode jobMode = scanSetDataHolder.getSelectedJobModeValue();
           requestAttributes.add(jobMode);

           switch (jobMode) {
               case SCAN_AND_SEND:
                   setScanSetting(requestAttributes);
                   setFileSetting(requestAttributes);
                   setDestinationSetting(requestAttributes);
                   break;

               case SCAN_AND_STORE_LOCAL:
                   setScanSetting(requestAttributes);
                   setStoreLocalSetting(requestAttributes);
                   break;

               case SCAN_AND_STORE_TEMPORARY:
                   Log.e(Const.TAG, PREFIX + "unsupported mode: " + jobMode);
                   return false;

               case SEND_STORED_FILE:
                   setFileSetting(requestAttributes);
                   setDestinationSetting(requestAttributes);
                   setSendStoredFileSetting(requestAttributes);
                   break;

               default:
                   Log.e(Const.TAG, PREFIX + "unknown mode: " + jobMode);
                   return false;
           }


           // start scan
           boolean result = false;
           try {
               Log.i(Const.TAG, PREFIX + "scan requesting...");
               result = mApplication.getScanJob().scan(requestAttributes);
               Log.d(Const.TAG, PREFIX + "scan requested. result=" + result);
           } catch (ScanException e) {
               message = "job start failed. " + e.getMessage();
               if (e instanceof ScanResponseException) {
                   message = makeJobErrorResponceMessage((ScanResponseException)e, message);
               }
               Log.w(Const.TAG, PREFIX + message);
           }
           return result;
       }
       @Override
       protected void onPostExecute(Boolean result) {
           if (message != null) {
               Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
           }
           if (!result) {
               ScanStateMachine.this.procScanEvent(ScanEvent.CHANGE_JOB_STATE_CANCELED);
           }
       }

       /**
        * スキャン要求の属性セットにスキャン設定を追加します。
        * Adds scan settings to the scan request attribute class.
        * @param requestAttributes
        */
       private void setScanSetting(ScanRequestAttributeSet requestAttributes) {

           ScanSettingDataHolder scanSetDataHolder = mApplication.getScanSettingDataHolder();

           requestAttributes.add(scanSetDataHolder.getSelectedColorValue());
           requestAttributes.add(scanSetDataHolder.getSelectedSideValue());
           requestAttributes.add(scanSetDataHolder.getSelectedPreviewValue());
       }

       /**
        * スキャン要求の属性セットにファイル設定を追加します。
        * Adds file settings to the scan request attribute set.
        * @param requestAttributes
        */
       private void setFileSetting(ScanRequestAttributeSet requestAttributes) {

           ScanSettingDataHolder scanSetDataHolder = mApplication.getScanSettingDataHolder();

           FileSetting fileSetting = new FileSetting();
           fileSetting.setFileFormat(scanSetDataHolder.getSelectedFileFormatValue());
           fileSetting.setMultiPageFormat(scanSetDataHolder.getSelectedMultiPageValue());
           requestAttributes.add(fileSetting);
       }

       /**
        * スキャン要求の属性セットに宛先設定を追加します。
        * Adds destination settings to the scan request attribute class.
        * @param requestAttributes
        */
       private void setDestinationSetting(ScanRequestAttributeSet requestAttributes) {

           DestinationSettingDataHolder destSetDataHolder = mApplication.getDestinationSettingDataHolder();

           DestinationSetting destSetting = new DestinationSetting();
           destSetting.add(destSetDataHolder.getDestinationSettingItem());
           requestAttributes.add(destSetting);
       }

       /**
        * スキャン要求の属性セットに蓄積設定を追加します。
        * Adds storage settings to the scan request attribute class.
        * @param requestAttributes
        */
       private void setStoreLocalSetting(ScanRequestAttributeSet requestAttributes) {
           StorageSettingDataHolder storageSetDataHolder = mApplication.getStorageSettingDataHolder();

           StoreLocalSetting storeLocalSetting = new StoreLocalSetting();
           StorageStoreData storeData = storageSetDataHolder.getStorageStoreData();
           if (storeData.getFileName()!=null && !storeData.getFileName().equals("")) {
               storeLocalSetting.setFileName(storeData.getFileName());
           }
           if (storeData.getFilePass()!=null && !storeData.getFilePass().equals("")) {
               storeLocalSetting.setFilePassword(storeData.getFilePass());
           }
           storeLocalSetting.setFolderId(storeData.getFolderId());
           if (storeData.getFolderPass()!=null && !storeData.getFolderPass().equals("")) {
               storeLocalSetting.setFolderPassword(storeData.getFolderPass());
           }
           requestAttributes.add(storeLocalSetting);
       }

       /**
        * スキャン要求の属性セットに蓄積ファイルの送信設定を追加します。
        * Adds settings for sending stored files to the scan request attribute set.
        * @param requestAttributes
        */
       private void setSendStoredFileSetting(ScanRequestAttributeSet requestAttributes) {
           StorageSettingDataHolder storageSetDataHolder = mApplication.getStorageSettingDataHolder();

           SendStoredFileSetting sendStoredFileSetting = new SendStoredFileSetting();
           StorageSendData sendData = storageSetDataHolder.getStorageSendData();

           FolderInfo folderInfo = new FolderInfo();
           folderInfo.setFolderId(sendData.getFolderId());
           if (sendData.getFolderPass()!=null && !sendData.getFolderPass().equals("")) {
               folderInfo.setFolderPassword(sendData.getFolderPass());
           }
           sendStoredFileSetting.setFolderInfo(folderInfo);

           StoredFileInfo storedFileInfo = new StoredFileInfo();
           storedFileInfo.setFileId(sendData.getFileId());
           if (sendData.getFilePass()!=null && !sendData.getFilePass().equals("")) {
               storedFileInfo.setFilePassword(sendData.getFilePass());
           }
           sendStoredFileSetting.addStoredFileInfo(storedFileInfo);

           requestAttributes.add(sendStoredFileSetting);
       }

   }

   /**
    * スキャンジョブをキャンセルするための非同期タスクです。
    * The asynchronous task to cancel the scan job.
    */
   private class CancelScanJobTask extends AsyncTask<Void, Void, Boolean> {
       @Override
       protected Boolean doInBackground(Void... param) {
           boolean result = false;
           try {
               Log.i(Const.TAG, PREFIX + "scan cancel requesting...");
               result = mApplication.getScanJob().cancelScanJob();
               Log.d(Const.TAG, PREFIX + "scan cancel requested. result=" + result);
           } catch (ScanException e) {
               String message = "job cancel failed. " + e.getMessage();
               if (e instanceof ScanResponseException) {
                   message = makeJobErrorResponceMessage((ScanResponseException)e, message);
               }
               Log.w(Const.TAG, PREFIX + message);
           }
           return result;
       }
       @Override
       protected void onPostExecute(Boolean result) {
           if (!result) {
               ScanStateMachine.this.procScanEvent(ScanEvent.CHANGE_JOB_STATE_CANCELED);
           }
       }
   }

   /**
    * スキャンジョブを一時停止するための非同期タスクです。
    * The asynchronous task to stop the scan job.
    */
   private class StopScanJobTask extends AsyncTask<Void, Void, Boolean> {

       private String message = null;

       @Override
       protected Boolean doInBackground(Void... param) {
           boolean result = false;
           try {
               Log.i(Const.TAG, PREFIX + "scan stop requesting...");
               result = mApplication.getScanJob().stopScanJob();
               Log.d(Const.TAG, PREFIX + "scan stop requested. result=" + result);
           } catch (ScanException e) {
               message = "job stop failed. " + e.getMessage();
               if (e instanceof ScanResponseException) {
                   message = makeJobErrorResponceMessage((ScanResponseException)e, message);
               }
               Log.w(Const.TAG, PREFIX + message);
           }
           return result;
       }
       @Override
       protected void onPostExecute(Boolean result) {
           if (message != null) {
               Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
           }
       }
   }

   /**
    * スキャンジョブを再開するための非同期タスクです。
    * The asynchronous task to resume the scan job.
    */
   private class ContinueScanJobTask extends AsyncTask<Void, Void, Boolean> {

       private String message = null;

       @Override
       protected Boolean doInBackground(Void... param) {
           boolean result = false;
           try {
               Log.i(Const.TAG, PREFIX + "scan continue requesting...");
               result = mApplication.getScanJob().continueScanJob(null);
               Log.d(Const.TAG, PREFIX + "scan continue requested. result=" + result);
           } catch (ScanException e) {
               message = "job continue failed. cancel job. " + e.getMessage();
               if (e instanceof ScanResponseException) {
                   message = makeJobErrorResponceMessage((ScanResponseException)e, message);
               }
               Log.w(Const.TAG, PREFIX + message);
           }
           return result;
       }
       @Override
       protected void onPostExecute(Boolean result) {
           if (message != null) {
               Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
           }
           if (!result) {
               new CancelScanJobTask().execute();
           }
       }
   }

   /**
    * スキャンジョブを終了するための非同期タスク
    * The asynchronous task to end the scan job.
    */
   private class EndScanJobTask extends AsyncTask<Void, Void, Boolean> {

       private String message = null;

       @Override
       protected Boolean doInBackground(Void... param) {
           boolean result = false;
           try {
               Log.i(Const.TAG, PREFIX + "scan end requesting...");
               result = mApplication.getScanJob().endScanJob();
               Log.d(Const.TAG, PREFIX + "scan end requested. result=" + result);
           } catch (ScanException e) {
               message = "job end failed. cancel job. " + e.getMessage();
               if (e instanceof ScanResponseException) {
                   message = makeJobErrorResponceMessage((ScanResponseException)e, message);
               }
               Log.w(Const.TAG, PREFIX + message);
           }
           return result;
       }
       @Override
       protected void onPostExecute(Boolean result) {
           if (message != null) {
               Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
           }
           if (!result) {
               new CancelScanJobTask().execute();
           }
       }
   }

}
