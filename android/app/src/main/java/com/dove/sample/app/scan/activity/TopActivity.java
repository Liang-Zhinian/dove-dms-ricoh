/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.app.scan.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dove.sample.app.scan.Const;
import com.dove.R;
import com.dove.sample.app.scan.activity.DialogUtil.StoreFileSettingDialog;
import com.dove.sample.app.scan.application.DestinationSettingDataHolder;
import com.dove.sample.app.scan.application.ScanSampleApplication;
import com.dove.sample.app.scan.application.ScanSettingDataHolder;
import com.dove.sample.app.scan.application.ScanStateMachine;
import com.dove.sample.app.scan.application.ScanStateMachine.ScanEvent;
import com.dove.sample.app.scan.application.StorageSettingDataHolder;
import com.dove.sample.app.scan.application.StorageSettingDataHolder.StorageSendData;
import com.dove.sample.app.scan.application.StorageSettingDataHolder.StorageStoreData;
import com.dove.sample.app.scan.application.SystemStateMonitor;
import com.dove.sample.function.common.SmartSDKApplication;
import com.dove.sample.function.common.impl.AsyncConnectState;
import com.dove.sample.function.scan.ScanService;
import com.dove.sample.function.scan.attribute.ScanServiceAttributeSet;
import com.dove.sample.function.scan.attribute.standard.AddressbookDestinationSetting;
import com.dove.sample.function.scan.attribute.standard.AddressbookDestinationSetting.DestinationKind;
import com.dove.sample.function.scan.attribute.standard.DestinationSettingItem;
import com.dove.sample.function.scan.attribute.standard.FtpAddressManualDestinationSetting;
import com.dove.sample.function.scan.attribute.standard.JobMode;
import com.dove.sample.function.scan.attribute.standard.MailAddressManualDestinationSetting;
import com.dove.sample.function.scan.attribute.standard.NcpAddressManualDestinationSetting;
import com.dove.sample.function.scan.attribute.standard.OccuredErrorLevel;
import com.dove.sample.function.scan.attribute.standard.ScannerState;
import com.dove.sample.function.scan.attribute.standard.ScannerStateReason;
import com.dove.sample.function.scan.attribute.standard.ScannerStateReasons;
import com.dove.sample.function.scan.attribute.standard.SmbAddressManualDestinationSetting;
import com.dove.sample.function.scan.event.ScanServiceAttributeEvent;
import com.dove.sample.function.scan.event.ScanServiceAttributeListener;
import com.dove.sample.wrapper.log.Logger;
import com.dove.sample.wrapper.log.Logger.LogRecorder;

import java.util.List;
import java.util.Set;

/**
 * スキャンサンプルアプリのメインアクティビティです。
 * Scan sample application: Main activity
 */
public class TopActivity extends Activity {

    public static final int REQUEST_CODE_TOP_ACTIVITY = 4000;

    /**
     * アプリケーションの種別
     * システム警告ダイアログの設定に使用します。
     * Application type
     * Used for setting system warning dialog.
     */
	private final static String ALERT_DIALOG_APP_TYPE_SCANNER = "SCANNER";

    /**
     * その他の設定ダイアログの横幅
     * Other Setting dialog width
     */
    private final int OTHER_SETTING_DIALOG_WIDTH = 600;

    /**
     * その他の設定ダイアログの高さ
     * Other Setting dialog height
     */
    private final int OTHER_SETTING_DIALOG_HEIGHT = 500;

    /**
     * スキャンサンプルアプリケーションのオブジェクト
     * Application object
     */
	private ScanSampleApplication mApplication;

	/**
	 * 設定画面からの通知を受け取るブロードキャストレシーバー
	 * Broadcast receiver to accept intents from setting dialog
	 */
    private BroadcastReceiver mReceiver;

    /**
     * 読取カラー設定ボタン
     * Scan color setting button
     */
	private Button mButtonColor;

	/**
	 * ファイル設定ボタン
	 * File setting button
	 */
	private Button mButtonFileSetting;

	/**
	 * スキャン面設定ボタン
	 * Scan side setting button
	 */
	private Button mButtonSide;

	/**
	 * 宛先設定ボタン
	 * Destination setting button
	 */
    private Button mButtonDestination;

    /**
     * ジョブモード設定ボタン
     * Job mode setting button
     */
    private Button mButtonJobMode;

    /**
     * ファイル蓄積設定ボタン
     * Store file setting button
     */
    private Button mButtonStoreFile;

    /**
     * 蓄積ファイル送信設定ボタン
     * Store file setting button
     */
    private Button mButtonSendStoredFile;


    /**
     * ジョブモード表示ラベル
     * Job mode display label
     */
    private TextView mTextJobMode;

    /**
     * その他設定ボタン
     * Other setting button
     */
	private LinearLayout mButtonOther;

	/**
	 * 読取開始ボタン
	 * Scan start button
	 */
    private RelativeLayout mButtonStart;

    /**
     * スキャンサービス状態リスナー
     * Scan service attribute listener
     */
    private ScanServiceAttributeListener mScanServiceAttrListener;

    /**
     * スキャン設定
     * Scan setting
     */
    private ScanSettingDataHolder mScanSettingDataHolder;

    /**
     * 宛先設定
     * Destination setting
     */
    private DestinationSettingDataHolder mDestSettingDataHolder;

    /**
     * 蓄積ファイル設定
     * File storage setting
     */
    private StorageSettingDataHolder mStorageSettingDataHolder;

    /**
     * ステートマシン
     * State machine
     */
    private ScanStateMachine mStateMachine;

    /**
     * スキャンサービスと接続するタスク
     * Task to connect with scan service
     */
    private ScanServiceInitTask mScanServiceInitTask;

    /**
     * スキャンサービス状態表示ラベル
     * Scan service state display label
     */
    private TextView text_state;

    /**
     * システム警告画面が表示されているかのフラグ
     * Flag to indicate if system warning screen is displayed
     */
    private volatile boolean mAlertDialogDisplayed = false;

    /**
     * 現在発生しているエラーのエラーレベル
     * Level of the currently occurring error
     */
    private OccuredErrorLevel mLastErrorLevel = null;

    /**
     * システム警告画面表示タスク
     * Asynchronous task to request to display system warning screen
     */
    private AlertDialogDisplayTask mAlertDialogDisplayTask = null;

    /**
     * 省エネ復帰要求タスク
     * Return request from energy saving task
     */
    private ReturnRequestFromEnergySavingTask mReturnRequestFromEnergySavingTask = null;

    /**
     * メインアクティビティ起動済みフラグ
     * trueであれば、すでにMainActivityが起動済みです。
     * TopActivity running flag
     * If true, another Mainactivity instance is running.
     */
    private boolean mMultipleRunning = false;
    
    /**
     * メインアクティビティの最前面表示状態を保持します。
     * onResume - onPause の間はtrueになります。
     * TopActivity foreground flag
     * During onResume and onPause, it should be true.
     */
    private boolean mIsForeground = false;

    /**
     * ステートマシンからのシステムリセット抑止(ロック)要求を保持します。
     * システムリセットのロック要求を受けるとtrueになります。
     * ロックが要求され、MainActivityが最前面表示中の場合のみ、SDKServiceへシステムリセットロックを設定します。
     * Request locking system auto reset flag from state machine. 
     * When request to lock system auto reset, it is changed to true.
     * When lock is required and MainAcitivity is in foreground, set system auto reset of SDKService.
     */
    private boolean mRequestedSystemResetLock = false;
    
    /**
     * Define the prefix for log information with abbreviation package and class name
     */
    private final static String PREFIX = "activity:MainAct:";
    /**
     * アクティビティが生成されると呼び出されます。
     * [処理内容]
     *   (1)アプリケーションの初期化
     *   (2)設定ブロードキャストレシーバーの設定
     *   (3)ジョブモード指定ボタンの設定
     *   (4)送信する蓄積ファイル指定ボタンの設定
     *   (5)蓄積ファイル設定ボタンの設定
     *   (6)宛先指定ボタンの設定
     *   (7)読み取りカラー選択ボタンの設定
     *   (8)ファイル形式選択ボタンの設定
     *   (9)原稿面選択ボタンの設定
     *   (10)その他の設定ボタンの設定
     *   (11)読取開始ボタンの設定
     *   (12)各ボタンの無効化
     *   (13)リスナー初期化
     *
     * Called when an activity is created.
     * [Processes]
     *   (1) Initialize application
     *   (2) Set setting broadcast receiver
     *   (3) Set job mode setting button
     *   (4) Set send stored file setting button
     *   (5) Set local store file setting button
     *   (6) Set destination setting button
     *   (7) Set scan color setting button
     *   (8) Set file setting button
     *   (9) Set scan side setting button
     *   (10) Set other settings button
     *   (11) Set start button
     *   (12) Disable buttons
     *   (13) Initialize listener
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Logger.setRecorder(mLogRecoder);
		Log.d(Const.TAG, PREFIX + "onCreate");

		setContentView(R.layout.activity_main);

		if (getNumActivities(getPackageName()) > 1) {
		    Log.i(Const.TAG, PREFIX + "Another TopActivity instance is already running.");
		    mMultipleRunning = true;
		    finish();
		    return;
		}

		//(1)
		mApplication = (ScanSampleApplication) getApplication();
        mScanServiceAttrListener = new ScanServiceAttributeListenerImpl(new Handler());
        mScanSettingDataHolder = mApplication.getScanSettingDataHolder();
        mDestSettingDataHolder = mApplication.getDestinationSettingDataHolder();
        mStorageSettingDataHolder = mApplication.getStorageSettingDataHolder();
        mStateMachine = mApplication.getStateMachine();
        mStateMachine.registActivity(this);
        text_state = (TextView)findViewById(R.id.text_state);

        //(2)
        IntentFilter filter = new IntentFilter();
        filter.addAction(DialogUtil.INTENT_ACTION_SUB_ACTIVITY_RESUMED);
        filter.addAction(ScanStateMachine.INTENT_ACTION_LOCK_SYSTEM_RESET);     //システムリセット抑止状態を設定するための内部インテント
        filter.addAction(ScanStateMachine.INTENT_ACTION_UNLOCK_SYSTEM_RESET);   //システムリセット抑止状態を解除するための内部インテント

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DialogUtil.INTENT_ACTION_SUB_ACTIVITY_RESUMED.equals(action)) {
                    startAlertDialogDisplayTask();
                } else if (ScanStateMachine.INTENT_ACTION_LOCK_SYSTEM_RESET.equals(action)) {
                    mRequestedSystemResetLock = true;
                    processSystemResetLock();
                } else if (ScanStateMachine.INTENT_ACTION_UNLOCK_SYSTEM_RESET.equals(action)) {
                    mRequestedSystemResetLock = false;
                    processSystemResetUnlock();
                }
            }
        };
        registerReceiver(mReceiver, filter);

        //(3)
        mButtonJobMode = (Button)findViewById(R.id.btn_jobmode);
        mTextJobMode = (TextView)findViewById(R.id.txt_jobmode);
        mButtonJobMode.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Check dialog showing or not
                if(mApplication.ismIsWinShowing()){
                    return ;
                }
                //If no dialog displays, set flag to true
                mApplication.setmIsWinShowing(true);
                
                AlertDialog dialog = DialogUtil.createJobModeDialog(TopActivity.this, mScanSettingDataHolder);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener(){
                    @Override
                    public void onDismiss(DialogInterface paramDialogInterface) {
                        //Close dialog, reset flag to false
                        mApplication.setmIsWinShowing(false);
                        UpdateScreenOnJobModeChanged();
                    }
                });
                DialogUtil.showDialog(dialog, DialogUtil.INPUT_DIALOG_WIDTH, 0);
            }
        });

        //(4)
        mButtonSendStoredFile = (Button)findViewById(R.id.btn_send_storedfile);
        mButtonSendStoredFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check dialog showing or not
                if(mApplication.ismIsWinShowing()){
                    return ;
                }
                //If no dialog displays, set flag to true
                mApplication.setmIsWinShowing(true);
                
                StorageSendData sendData = mStorageSettingDataHolder.getStorageSendData();
                Intent intent = new Intent(TopActivity.this, StorageActivity.class);
                if (sendData!=null && sendData.getFileId()!=null) {
                    intent.putExtra(StorageActivity.KEY_MODE, StorageActivity.VALUE_MODE_SEND_FILE);
                } else {
                    intent.putExtra(StorageActivity.KEY_MODE, StorageActivity.VALUE_MODE_SEND_FOLDER);
                }
                startActivityForResult(intent, StorageActivity.REQUEST_CODE_STORAGE_ACTIVITY_SEND);
            }
        });

        //(5)
        mButtonStoreFile = (Button)findViewById(R.id.btn_store_setting);
        mButtonStoreFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check dialog showing or not
                if(mApplication.ismIsWinShowing()){
                    return ;
                }
                //If no dialog displays, set flag to true
                mApplication.setmIsWinShowing(true);
                
                AlertDialog dialog = new StoreFileSettingDialog(TopActivity.this, mStorageSettingDataHolder);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener(){
                    @Override
                    public void onDismiss(DialogInterface paramDialogInterface) {
                        //Close dialog, reset flag to false
                        mApplication.setmIsWinShowing(false);
                        
                        TextView txt_store_folder = (TextView)findViewById(R.id.txt_store_folder);
                        ImageView store_icon = (ImageView)findViewById(R.id.img_store_icon);
                        StorageStoreData data = mStorageSettingDataHolder.getStorageStoreData();
                        if (data!=null && data.getFolderId()!=null) {
                            String folderName = data.getFolderName();
                            if ( (folderName == null) || ("".equals(folderName)) ) {
                                txt_store_folder.setText(data.getFolderId());
                            } else {
                                txt_store_folder.setText(folderName);
                            }
                            store_icon.setVisibility(View.VISIBLE);
                        } else {
                            txt_store_folder.setText("");
                            store_icon.setVisibility(View.INVISIBLE);
                        }
                        mButtonStart.setEnabled(isSettingCompleted());
                    }
                });
                DialogUtil.showDialog(dialog, DialogUtil.INPUT_DIALOG_WIDTH, 0);
            }
        });
        

        //(6)
		mButtonDestination = (Button)findViewById(R.id.btn_destination);
		mButtonDestination.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
                //Check dialog showing or not
			    if(mApplication.ismIsWinShowing()){
			        return ;
			    }
                //If no dialog displays, set flag to true
                mApplication.setmIsWinShowing(true);
                
                AlertDialog dialog = DialogUtil.createDestTypeDialog(TopActivity.this, mDestSettingDataHolder);
                DialogUtil.showDialog(dialog, DialogUtil.INPUT_DIALOG_WIDTH, DialogUtil.DEFAULT_DIALOG_HEIGHT);
			}
		});

		//(7)
		mButtonColor = (Button)findViewById(R.id.btn_color);
		mButtonColor.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
                //Check dialog showing or not
                if(mApplication.ismIsWinShowing()){
                    return ;
                }
                //If no dialog displays, set flag to true
                mApplication.setmIsWinShowing(true);
                
                AlertDialog dialog = DialogUtil.createColorSettingDialog(TopActivity.this, mScanSettingDataHolder);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener(){
                    @Override
                    public void onDismiss(DialogInterface paramDialogInterface) {
                        //Close dialog, reset flag to false
                        mApplication.setmIsWinShowing(false);
                        
                        String label = getResources().getString(mScanSettingDataHolder.getSelectedColorLabel());
                        mButtonColor.setText(label);
                    }
                });
                DialogUtil.showDialog(dialog, DialogUtil.INPUT_DIALOG_WIDTH, 0);
			}
		});

		//(8)
		mButtonFileSetting = (Button)findViewById(R.id.btn_file);
		mButtonFileSetting.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
                //Check dialog showing or not
                if(mApplication.ismIsWinShowing()){
                    return ;
                }
                //If no dialog displays, set flag to true
                mApplication.setmIsWinShowing(true);
                
                AlertDialog dialog = DialogUtil.createFileSettingDialog(TopActivity.this, mScanSettingDataHolder);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener(){
                    @Override
                    public void onDismiss(DialogInterface paramDialogInterface) {
                        //Close dialog, reset flag to false
                        mApplication.setmIsWinShowing(false);
                        
                        String label = getResources().getString(mScanSettingDataHolder.getSelectedFileSettingLabel());
                        mButtonFileSetting.setText(label);
                    }
                });
                DialogUtil.showDialog(dialog, DialogUtil.INPUT_DIALOG_WIDTH, 0);
			}
		});

		//(9)
		mButtonSide = (Button)findViewById(R.id.btn_side);
		mButtonSide.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
                //Check dialog showing or not
                if(mApplication.ismIsWinShowing()){
                    return ;
                }
                //If no dialog displays, set flag to true
                mApplication.setmIsWinShowing(true);
                
                AlertDialog dialog = DialogUtil.createSideSettingDialog(TopActivity.this, mScanSettingDataHolder);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener(){
                    @Override
                    public void onDismiss(DialogInterface paramDialogInterface) {
                        //Close dialog, reset flag to false
                        mApplication.setmIsWinShowing(false);
                        
                        String label = getResources().getString(mScanSettingDataHolder.getSelectedSideLabel());
                        mButtonSide.setText(label);
                    }
                });
                DialogUtil.showDialog(dialog, DialogUtil.INPUT_DIALOG_WIDTH, 0);
			}
		});

		//(10)
        mButtonOther = (LinearLayout)findViewById(R.id.btn_other);
        mButtonOther.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Check dialog showing or not
                if(mApplication.ismIsWinShowing()){
                    return ;
                }
                //If no dialog displays, set flag to true
                mApplication.setmIsWinShowing(true);
                
                AlertDialog dialog = DialogUtil.createOtherSettingDialog(TopActivity.this, mScanSettingDataHolder);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener(){
                    @Override
                    public void onDismiss(DialogInterface paramDialogInterface) {
                        //Close dialog, reset flag to false
                        mApplication.setmIsWinShowing(false);
                    }
                });
                DialogUtil.showDialog(dialog, OTHER_SETTING_DIALOG_WIDTH, OTHER_SETTING_DIALOG_HEIGHT);
            }
        });

		//(11)
        // start scanning
		mButtonStart = (RelativeLayout)findViewById(R.id.btn_start);
		mButtonStart.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
			    //Check dialog showing or not
                if(mApplication.ismIsWinShowing()){
                    return ;
                }
                //If no dialog displays, set flag to true
                mApplication.setmIsWinShowing(true);
                
                mStateMachine.procScanEvent(ScanEvent.REQUEST_JOB_START);
			}
		});

		//(12)
        disableSettingKey();
        mButtonJobMode.setEnabled(false);
        mButtonDestination.setEnabled(false);
        mButtonStart.setEnabled(false);

        //(13)
        if( mScanServiceInitTask != null ) {
            mScanServiceInitTask.cancel(false);
        }
        mScanServiceInitTask = new ScanServiceInitTask();
        mScanServiceInitTask.execute();

        // send event
        mStateMachine.procScanEvent(ScanEvent.ACTIVITY_CREATED);
        
        // set application state to Normal
        mApplication.setAppState(TopActivity.class.getName(), SmartSDKApplication.APP_STATE_NORMAL, SmartSDKApplication.APP_STATE_NORMAL_MSG, SmartSDKApplication.APP_TYPE_SCANNER);
	}

    /**
     * 遷移先のアクティビティからの結果を受け取ります。
     * [処理内容]
     *   (1)プレビューから戻ってきた場合、結果に応じてステートマシンにイベントを送信します。
     *   (2)宛先設定から戻ってきた場合は、結果に応じて宛先表示欄の表示を更新します。
     *
     * Receive the result from the activity of the changed state.
     * [Processes]
     *   (1) When returned from preview, sends event to the state machine accordingly to the result.
     *   (2) When returned from destination setting, updates the display of the destination display area accordingly to the result.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        //(1)
        if(requestCode == PreviewActivity.REQUEST_CODE_PREVIEW_ACTIVITY) {
            if(resultCode == RESULT_OK){
                mStateMachine.procScanEvent(ScanEvent.REQUEST_JOB_END);
            } else if(resultCode == RESULT_CANCELED){
                mStateMachine.procScanEvent(ScanEvent.REQUEST_JOB_CANCEL);
            } else {
                /* do nothing */
            }
        }
        //(2)
        else if (requestCode == AddressActivity.REQUEST_CODE_ADDRESS_ACTIVITY) {
            //Reset flag to false
            mApplication.setmIsDialogBtnClicked(false);
            mApplication.setmIsWinShowing(false);
            
            if(resultCode == RESULT_OK){
                String entryName = intent.getStringExtra(AddressActivity.ENTRY_NAME);
                updateDestinationLabel(entryName);
            } else if(resultCode == RESULT_CANCELED){
                /* do nothing */
            } else {
                /* do nothing */
            }
        }
        else if (requestCode == StorageActivity.REQUEST_CODE_STORAGE_ACTIVITY_SEND) {
            //Reset flag to false
            mApplication.setmIsWinShowing(false);
            
            if (resultCode == RESULT_OK) {
                String keyDisplay = mStorageSettingDataHolder.getStorageSendData().getFileName();
                TextView txt_storeFile = (TextView)findViewById(R.id.txt_send_storedfile_title);
                txt_storeFile.setText(keyDisplay);
                mButtonStart.setEnabled(isSettingCompleted());
            } else if(resultCode == RESULT_CANCELED){
                /* do nothing */
            } else {
                /* do nothing */
            }
        }
    }

    /**
     * アクティビティの再開時に呼び出されます。
     * エラーの発生有無を非同期で検査し、必要であればシステム警告画面切替えます。
     * 本体の電力モードを非同期で確認し、必要であればシステム状態要求を通知します。
     * Called when the activity is resumed.
     * Checks error occurrence asynchronously and switches to a system warning screen if necessary.
     * Checks machine's power mode asynchronously and requests system state if necessary.
     */
    @Override
    protected void onResume() {
        Log.d(Const.TAG, PREFIX + "onResume");
        super.onResume();

        if (mMultipleRunning) {
            return;
        }

        // スキャンステートマシンからシステムリセット抑止を要求されている場合、SDKServiceへロックを設定します
        mIsForeground = true;
        processSystemResetLock();
        
        startAlertDialogDisplayTask();
        startReturnRequestFromEnergySavingTask();
    }

    /**
     * アクティビティの停止時に呼び出されます。
     * システム警告画面表示タスクが実行中であれば、キャンセルします。
     * Called when the activity is stopped.
     * If the system warning screen display task is in process, the task is cancelled.
     */
    @Override
    protected void onPause() {
        Log.d(Const.TAG, PREFIX + "onPause");
        super.onPause();

        // システムリセットロックを解除します
        mIsForeground = false;
        processSystemResetUnlock(); 
       
        stopAlertDialogDisplayTask();
    }

    /**
     * アクティビティが破棄される際に呼び出されます。
     * [処理内容]
     *   (1)メインアクティビティ終了イベントをステートマシンに送る
     *      読取中であれば、読取がキャンセルされます。
     *   (2)サービスからイベントリスナーとブロードキャストレシーバーを除去する
     *   (3)非同期タスクが実行中だった場合、キャンセルする
     *   (4)アプリケーションの保持データを初期化する
     *   (5)参照を破棄する
     *
     * Called when the activity is destroyed.
     * [Processes]
     *   (1) Send TopActivity destoyed event to the state machine.
     *       If scanning is in process, scanning is cancelled.
     *   (2) Removes the event listener and the broadcast receiver from the service.
     *   (3) If asynchronous task is in process, the task is cancelled.
     *   (4) Initializes the data saved to the application.
     *   (5) Discards references
     */
    @Override
    protected void onDestroy() {
        Log.d(Const.TAG, PREFIX + "onDestroy");
        super.onDestroy();

        // if TopActivity another instance is already running, then exit without doing anything
        if (mMultipleRunning) {
            return;
        }

        //アクティビティ終了時は、ロック設定状態に関わらずシステムリセットのロックを解除します
        processSystemResetUnlock();
        
        //(1)
        mStateMachine.procScanEvent(ScanEvent.ACTIVITY_DESTROYED);

        //(2)
        ScanService scanService = mApplication.getScanService();
        try {
            scanService.removeScanServiceAttributeListener(mScanServiceAttrListener);
        } catch (IllegalStateException e){
            /* the listener is not registered. */
        }
        unregisterReceiver(mReceiver);

        //(3)
        stopAlertDialogDisplayTask();
        stopReturnRequestFromEnergySavingTask();
        if( mScanServiceInitTask != null ) {
            mScanServiceInitTask.cancel(false);
            mScanServiceInitTask = null;
        }

        // set application state to Normal
        mApplication.setAppState(TopActivity.class.getName(), SmartSDKApplication.APP_STATE_NORMAL, SmartSDKApplication.APP_STATE_NORMAL_MSG, SmartSDKApplication.APP_TYPE_SCANNER);
        
        //(4)
        mApplication.init();

        //(5)
        mApplication = null;
        mReceiver = null;
        mButtonColor = null;
        mButtonFileSetting = null;
        mButtonSide = null;
        mButtonDestination = null;
        mButtonJobMode = null;
        mButtonStoreFile = null;
        mButtonSendStoredFile = null;
        mButtonOther = null;
        mButtonStart = null;
        mTextJobMode = null;
        mScanSettingDataHolder = null;
        mDestSettingDataHolder = null;
        mStorageSettingDataHolder = null;
        mStateMachine = null;
        mScanServiceAttrListener = null;
    }

    /**
     * システムリセットのロックを設定します。
     * ジョブ実行中、かつメインアクティビティ前面表示中のみ、SDKServiceへシステムリセットのロックを設定します。
     * Locking system auto reset.
     * When job is executing and MainAcitivity is in foreground, set system auto reset of SDKService.
     */
    private void processSystemResetLock() {
        if (mIsForeground && mRequestedSystemResetLock) {
            mApplication.lockSystemReset();
        }
    }

    /**
     * システムリセットのロックを解除します。
     * 意図しないロック状態の継続を避けるため、ロックを要求していない場合もロック解除を行います。
     * Clear locking system auto reset.
     * To avoid the continuation of locked state without intention, Clear locking
     * even it is not requested.
     */
    private void processSystemResetUnlock() {
        mApplication.unlockSystemReset();
    }
    
    /**
     * 宛先表示ラベルを更新します。
     * [表示の形式]
     *   (1)フォルダ（直接入力）
     *      - フォルダアイコン ＋ フォルダパス + 任意文字列
     *   (2)メール（直接入力）
     *      - メールアイコン + メールアドレス + 任意文字列
     *   (3)フォルダ（アドレス帳選択）
     *      - フォルダアイコン + 任意文字列
     *   (4)メール（アドレス帳選択）
     *      - メールアイコン + 任意文字列
     *
     * Updates the destination display label.
     * This method must be called from UI thread.
     * [Display format]
     *   (1) For email: address book
     *     - icon (folder) + folder path + string
     *   (2) For email: manual entry
     *     - icon (mail) + email path + string
     *   (3) For folder: address book
     *     - icon (icon) + string
     *   (4) For folder: manual entry
     *     - icon (mail) + string
     *
     * @param optStr : 任意文字列 Specified string
     */
	public void updateDestinationLabel(String optStr) {

	    DestinationSettingDataHolder destHolder = mApplication.getDestinationSettingDataHolder();
	    DestinationSettingItem destItem = destHolder.getDestinationSettingItem();

	    DestinationKind destKind = null;
	    String str = null;

	    //(1)
	    if (destItem instanceof FtpAddressManualDestinationSetting ) {

            FtpAddressManualDestinationSetting item = (FtpAddressManualDestinationSetting)destItem;
            destKind = DestinationKind.FOLDER;
            str = item.getPath();

        } else if (destItem instanceof SmbAddressManualDestinationSetting) {

            SmbAddressManualDestinationSetting item = (SmbAddressManualDestinationSetting)destItem;
            destKind = DestinationKind.FOLDER;
            str = item.getPath();

        } else if (destItem instanceof NcpAddressManualDestinationSetting) {

            NcpAddressManualDestinationSetting item = (NcpAddressManualDestinationSetting)destItem;
            destKind = DestinationKind.FOLDER;
            str = item.getPath();

        }
        //(2)
        else if (destItem instanceof MailAddressManualDestinationSetting) {

            MailAddressManualDestinationSetting item = (MailAddressManualDestinationSetting)destItem;
            destKind = DestinationKind.MAIL;
            str = item.getMailAddress();

        }
        //(3)(4)
        else if (destItem instanceof AddressbookDestinationSetting) {

            AddressbookDestinationSetting item = (AddressbookDestinationSetting)destItem;
            destKind = item.getDestinationKind();
            str = "";

        } else {
            /* do nothing */
        }

        if(optStr!=null) {
            str = str + " " + optStr;
        }
        updateDestinationLabel(destKind, str);

        mButtonStart.setEnabled(isSettingCompleted());
	}

	/**
	 * ジョブ開始に必要な設定が完了しているかを返します。
	 * Returns true when the settings for job start are completed.
	 */
	private boolean isSettingCompleted() {
        JobMode jobMode = mScanSettingDataHolder.getSelectedJobModeValue();
        switch(jobMode) {
            case SCAN_AND_SEND:
                if (mDestSettingDataHolder.getDestinationSettingItem()==null) {
                    return false;
                } else {
                    return true;
                }
            case SCAN_AND_STORE_LOCAL:
                StorageStoreData storageStoreData = mStorageSettingDataHolder.getStorageStoreData();
                if (storageStoreData==null || storageStoreData.getFolderId()==null) {
                    return false;
                } else {
                    return true;
                }
            case SEND_STORED_FILE:
                StorageSendData storageSendData = mStorageSettingDataHolder.getStorageSendData();
                if (mDestSettingDataHolder.getDestinationSettingItem()==null
                        || storageSendData==null
                        || storageSendData.getFileId()==null) {
                    return false;
                } else {
                    return true;
                }
            default:
                /* should never reach here */
                return false;
        }
	}

	/**
	 * 宛先表示ラベルを更新します。
	 * 表示形式は アイコン(フォルダ/メール）＋ 文字列 です。
	 *  Updates the destination display label.
	 *  Display format is "icon (folder/email) + string".
	 *
	 * @param destKind
	 * @param str
	 */
	private void updateDestinationLabel(DestinationKind destKind, String str) {
        final TextView text_dest_title = (TextView)findViewById(R.id.text_dest_title);
        final ImageView img_dest_icon = (ImageView)findViewById(R.id.img_dest_icon);

        if (destKind == DestinationKind.FOLDER) {

            img_dest_icon.setImageResource(R.drawable.icon_folder_small);
            text_dest_title.setText(str);
            img_dest_icon.setVisibility(View.VISIBLE);
            text_dest_title.setVisibility(View.VISIBLE);

        } else if (destKind == DestinationKind.MAIL) {

            img_dest_icon.setImageResource(R.drawable.icon_mail_small);
            text_dest_title.setText(str);
            img_dest_icon.setVisibility(View.VISIBLE);
            text_dest_title.setVisibility(View.VISIBLE);

        } else {
            img_dest_icon.setVisibility(View.INVISIBLE);
            text_dest_title.setVisibility(View.INVISIBLE);
        }
	}

    /**
     * システム警告画面表示要求に渡す状態文字列を生成します。
     * Creates the state string to be passed to system warning screen display request.
     *
     * @param state スキャンサービス状態
     *              State of scan service
     * @return 状態文字列
     *         State string
     */
    private String makeAlertStateString(ScannerState state) {
        String stateString = "";
        if (state != null) {
            stateString = state.toString();
        }
        return stateString;
    }

    /**
     * システム警告画面表示要求に渡す状態理由文字列を生成します。
     * 複数の状態理由があった場合、1つ目の状態理由のみを渡します。
     * Creates the state reason string to be passed to the system warning screen display request.
     * If multiple state reasons exist, only the first state reason is passed.
     *
     * @param stateReasons スキャナサービス状態理由
     *                     Scan service state reason
     * @return 状態理由文字列
     *         State reason string
     */
   private String makeAlertStateReasonString(ScannerStateReasons stateReasons) {
        String reasonString = "";
        if (stateReasons != null) {
            Object[] reasonArray = stateReasons.getReasons().toArray();
            if (reasonArray != null && reasonArray.length > 0) {
                reasonString = reasonArray[0].toString();
            }
        }
        return reasonString;
    }

    /**
     * 指定されたアプリケーションがフォアグランド状態にあるかを取得します。
     * Obtains whether or not the specified application is in the foreground state.
     *
     * @param packageName アプリケーションのパッケージ名
     *                    Application package name
     * @return フォアグラウンド状態にある場合にtrue
     *         If the application is in the foreground state, true is returned.
     */
    private boolean isForegroundApp(String packageName) {
        boolean result = false;
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> list = am.getRunningAppProcesses();
        for (RunningAppProcessInfo info : list) {
            if (packageName.equals(info.processName)) {
                result = (info.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND);
                break;
            }
        }
        return result;
    }

    /**
     * 指定されたアプリケーションのアクティビティスタックの最上位クラスを取得します。
     * Obtains the top class in the activity stack of the specified application.
     *
     * @param packageName アプリケーションのパッケージ名
     *                    Application package name
     * @return 最上位クラスのFQCNクラス名. 取得できない場合はnull
     *         The name of the FQCN class name of the top class. If the name cannot be obtained, null is returned.
     */
    private String getTopActivityClassName(String packageName) {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(30);
        for (RunningTaskInfo info : list) {
            if (packageName.equals(info.topActivity.getPackageName())) {
                return info.topActivity.getClassName();
            }
        }
        return null;
    }

    /**
     * 指定されたアプリケーションのアクティビティスタック内のアクティビティ数を取得します。
     * Obtains the number of activities in the activity stack of the specified application.
     *
     * @param packageName アプリケーションのパッケージ名
     *                    Application package name
     * @return アクティビティ数. 取得できない場合は0
     *         The number of activitys. If the number cannot be obtained, 0 is returned.
     */
    private int getNumActivities(String packageName) {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(30);
        for (RunningTaskInfo info : list) {
            if (packageName.equals(info.topActivity.getPackageName())) {
                return info.numActivities;
            }
        }
        return 0;
    }


    /**
     * スキャナサービスの状態変更監視リスナーです。
     * [処理内容]
     *   (1)スキャンサービスの状態によって、スキャンサービス状態表示ラベルを書き換えます。
     *   (2)エラー画面の表示・更新・非表示要求を行います。
     * The listener class to monitor scan service attribute changes.
     * [Processes]
     *   (1) Rewrites the scan service state display label accordingly to the scan service state.
     *   (2) Requests to display/update/hide error screens.
     */
    class ScanServiceAttributeListenerImpl implements ScanServiceAttributeListener {

        /**
         * UI thread handler
         */
        private Handler mHandler;

        ScanServiceAttributeListenerImpl(Handler handler) {
            mHandler = handler;
        }

        @Override
        public void attributeUpdate(final ScanServiceAttributeEvent event) {
            ScannerState state = (ScannerState)event.getAttributes().get(ScannerState.class);
            ScannerStateReasons stateReasons = (ScannerStateReasons)event.getAttributes().get(ScannerStateReasons.class);
            OccuredErrorLevel errorLevel = (OccuredErrorLevel) event.getAttributes().get(OccuredErrorLevel.class);

            String stateLabel = "";

            //(1)
            switch(state) {
            case IDLE :
                Log.i(Const.TAG, PREFIX + "ScannerState : IDLE");
                stateLabel = getString(R.string.txid_scan_t_state_ready);
                break;
            case MAINTENANCE:
                Log.i(Const.TAG, PREFIX + "ScannerState : MAINTENANCE");
                stateLabel = getString(R.string.txid_scan_t_state_maintenance);
                break;
            case PROCESSING:
                Log.i(Const.TAG, PREFIX + "ScannerState : PROCESSING");
                stateLabel = getString(R.string.txid_scan_t_state_scanning);
                break;
            case STOPPED:
                Log.i(Const.TAG, PREFIX + "ScannerState : STOPPED");
                stateLabel = getString(R.string.txid_scan_t_state_stopped);
                break;
            case UNKNOWN:
                Log.i(Const.TAG, PREFIX + "ScannerState : UNKNOWN");
                stateLabel = getString(R.string.txid_scan_t_state_unknown);
                break;
            default:
                Log.i(Const.TAG, PREFIX + "ScannerState : never reach here ...");
                stateLabel = state + " (undefined)";
                /* never reach here */
                break;
            }

            if( stateReasons != null ) {
                Set<ScannerStateReason> reasonSet = stateReasons.getReasons();
                for(ScannerStateReason reason : reasonSet) {
                    switch(reason) {
                        case COVER_OPEN:
                            stateLabel = getString(R.string.txid_scan_t_state_reason_cover_open);
                            break;
                        case MEDIA_JAM:
                            stateLabel = getString(R.string.txid_scan_t_state_reason_media_jam);
                            break;
                        case PAUSED:
                            stateLabel = getString(R.string.txid_scan_t_state_reason_paused);
                            break;
                        case OTHER:
                            stateLabel = getString(R.string.txid_scan_t_state_reason_other);
                            break;
                        default:
                            /* never reach here */
                            stateLabel = reason + " (undefined)";
                            break;
                    }
                }
            }

            final String result = stateLabel;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    text_state.setText(result);
                }
            });

            //(2)
            if (OccuredErrorLevel.ERROR.equals(errorLevel)
                    || OccuredErrorLevel.FATAL_ERROR.equals(errorLevel)) {

                String stateString = makeAlertStateString(state);
                String reasonString = makeAlertStateReasonString(stateReasons);

                if (mLastErrorLevel == null) {
                    // Normal -> Error
                    if (isForegroundApp(getPackageName())) {
                        mApplication.displayAlertDialog(ALERT_DIALOG_APP_TYPE_SCANNER, stateString, reasonString);
                        mAlertDialogDisplayed = true;
                    }
                } else {
                    // Error -> Error
                    if (mAlertDialogDisplayed) {
                        mApplication.updateAlertDialog(ALERT_DIALOG_APP_TYPE_SCANNER, stateString, reasonString);
                    }
                }
                mLastErrorLevel = errorLevel;

            } else {
                if (mLastErrorLevel != null) {
                    // Error -> Normal
                    if (mAlertDialogDisplayed) {
                        String activityName = getTopActivityClassName(getPackageName());
                        if (activityName == null) {
                            activityName = TopActivity.class.getName();
                        }
                        mApplication.hideAlertDialog(ALERT_DIALOG_APP_TYPE_SCANNER, activityName);
                        mAlertDialogDisplayed = false;
                    }
                }
                mLastErrorLevel = null;
            }
        }
    }

    /**
     * 非同期でスキャンサービスとの接続を行います。
     * [処理内容]
     *   (1)スキャンサービスのイベントを受信するリスナーを設定します。
     *      機器が利用可能になるか、キャンセルが押されるまでリトライします。
     *   (2)非同期イベントの接続確認を行います。
     *      接続可能になるか、キャンセルが押されるまでリトライします。
     *   (3)接続に成功した場合は、スキャンサービスから各設定の設定可能値を取得します。
     *
     * Connects with the scan service asynchronously.
     * [Processes]
     *   (1) Sets the listener to receive scan service events.
     *       This task repeats until the machine becomes available or cancel button is touched.
     *   (2) Confirms the asynchronous connection.
     *       This task repeats until the connection is confirmed or cancel button is touched.
     *   (3) After the machine becomes available and connection is confirmed,
     *       obtains job setting values.
     */
    class ScanServiceInitTask extends AsyncTask<Void, Void, Integer> {

        AsyncConnectState addListenerResult = null;
        AsyncConnectState getAsyncConnectStateResult = null;

        @Override
        protected Integer doInBackground(Void... params) {

            final ScanService scanService = mApplication.getScanService();

            //(1)
            while (true) {
                if(isCancelled()) {
                    return -1;
                }
                addListenerResult = scanService.addScanServiceAttributeListener(mScanServiceAttrListener);

                if (addListenerResult == null) {
                    sleep(100);
                    continue;
                }

                if (addListenerResult.getState() == AsyncConnectState.STATE.CONNECTED) {
                    break;
                }

                if (addListenerResult.getErrorCode() == AsyncConnectState.ERROR_CODE.NO_ERROR){
                    // do nothing
                } else if (addListenerResult.getErrorCode() == AsyncConnectState.ERROR_CODE.BUSY) {
                    sleep(10000);
                } else if (addListenerResult.getErrorCode() == AsyncConnectState.ERROR_CODE.TIMEOUT){
                    // do nothing
                } else if (addListenerResult.getErrorCode() == AsyncConnectState.ERROR_CODE.INVALID){
                    return 0;
                } else {
                    // unknown state
                    return 0;
                }
            }

            if (addListenerResult.getState() != AsyncConnectState.STATE.CONNECTED) {
                return 0;
            }

            //(2)
            while (true) {
                if(isCancelled()) {
                    return -1;
                }
                getAsyncConnectStateResult = scanService.getAsyncConnectState();

                if (getAsyncConnectStateResult == null) {
                    sleep(100);
                    continue;
                }

                if (getAsyncConnectStateResult.getState() == AsyncConnectState.STATE.CONNECTED) {
                    break;
                }

                if (getAsyncConnectStateResult.getErrorCode() == AsyncConnectState.ERROR_CODE.NO_ERROR){
                    // do nothing
                } else if (getAsyncConnectStateResult.getErrorCode() == AsyncConnectState.ERROR_CODE.BUSY) {
                    sleep(10000);
                } else if (getAsyncConnectStateResult.getErrorCode() == AsyncConnectState.ERROR_CODE.TIMEOUT){
                    // do nothing
                } else if (getAsyncConnectStateResult.getErrorCode() == AsyncConnectState.ERROR_CODE.INVALID){
                    return 0;
                } else {
                    // unknown state
                    return 0;
                }
            }

            //(3)
            if (addListenerResult.getState() == AsyncConnectState.STATE.CONNECTED
                    && getAsyncConnectStateResult.getState() == AsyncConnectState.STATE.CONNECTED) {
                mScanSettingDataHolder.init(scanService);
            }

            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (addListenerResult==null) {
                Log.d(Const.TAG, PREFIX + "addScanServiceAttributeListener:null");
            } else {
                Log.d(Const.TAG, PREFIX + "addScanServiceAttributeListener:" + addListenerResult.getState() + "," + addListenerResult.getErrorCode());
            }
            if (getAsyncConnectStateResult==null) {
                Log.d(Const.TAG, PREFIX + "getAsyncConnectState:null");
            } else {
                Log.d(Const.TAG, PREFIX + "getAsyncConnectState:" + getAsyncConnectStateResult.getState() + "," + getAsyncConnectStateResult.getErrorCode());
            }

            if (result!=0) {
                /* canceled. */
                return;
            }

            if (addListenerResult.getState() == AsyncConnectState.STATE.CONNECTED
                    && getAsyncConnectStateResult.getState() == AsyncConnectState.STATE.CONNECTED) {
                // connection succeeded.
                mButtonColor.setText(mScanSettingDataHolder.getSelectedColorLabel());
                mButtonFileSetting.setText(mScanSettingDataHolder.getSelectedFileSettingLabel());
                mButtonSide.setText(mScanSettingDataHolder.getSelectedSideLabel());
                mButtonJobMode.setEnabled(true);
                mButtonDestination.setEnabled(true);
                UpdateScreenOnJobModeChanged();
                mStateMachine.procScanEvent(ScanEvent.ACTIVITY_BOOT_COMPLETED);
            }
            else {
                // the connection is invalid.
                mStateMachine.procScanEvent(ScanEvent.ACTIVITY_BOOT_FAILED);
            }
        }

        /**
         * 指定された時間カレントスレッドをスリープします。
         * sleep for the whole of the specified interval
         */
        private void sleep(long time) {
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                Log.d(Const.TAG, PREFIX + e.toString());            }
        }
    }

    /**
     * 選択されたジョブモードに合わせて、画面を更新します。
     * Update screen accordingly to the selected job mode.
     */
    private void UpdateScreenOnJobModeChanged() {
        JobMode jobMode = mScanSettingDataHolder.getSelectedJobModeValue();
        mTextJobMode.setText(mScanSettingDataHolder.getSelectedJobModeLabel());

        LinearLayout view_storeFile = (LinearLayout)findViewById(R.id.view_send_storedfile);
        RelativeLayout conatiner_destination = (RelativeLayout)findViewById(R.id.container_destination);
        RelativeLayout conatiner_store_setting = (RelativeLayout)findViewById(R.id.container_store_setting);
        switch(jobMode) {
            case SCAN_AND_SEND:
                mButtonSendStoredFile.setVisibility(View.INVISIBLE);
                view_storeFile.setVisibility(View.INVISIBLE);
                conatiner_destination.setVisibility(View.VISIBLE);
                conatiner_store_setting.setVisibility(View.INVISIBLE);
                enableSettingKey();
                break;
            case SCAN_AND_STORE_LOCAL:
                mButtonSendStoredFile.setVisibility(View.INVISIBLE);
                view_storeFile.setVisibility(View.INVISIBLE);
                conatiner_destination.setVisibility(View.INVISIBLE);
                conatiner_store_setting.setVisibility(View.VISIBLE);
                enableSettingKey();
                mButtonFileSetting.setEnabled(false);
                break;
            case SEND_STORED_FILE:
                mButtonSendStoredFile.setVisibility(View.VISIBLE);
                view_storeFile.setVisibility(View.VISIBLE);
                conatiner_destination.setVisibility(View.VISIBLE);
                conatiner_store_setting.setVisibility(View.INVISIBLE);
                disableSettingKey();
                mButtonFileSetting.setEnabled(true);
                break;
            default:
                /* should never reach here */
                break;
        }

        mButtonStart.setEnabled(isSettingCompleted());
    }

    /**
     * 各設定ボタンを有効化します。
     * Enables setting buttons.
     */
    private void enableSettingKey() {
        mButtonColor.setEnabled(true);
        mButtonFileSetting.setEnabled(true);
        mButtonSide.setEnabled(true);
        mButtonOther.setEnabled(true);
    }

    /**
     * 各設定ボタンを無効化します。
     * Disables setting buttons.
     */
    private void disableSettingKey() {
        mButtonColor.setEnabled(false);
        mButtonFileSetting.setEnabled(false);
        mButtonSide.setEnabled(false);
        mButtonOther.setEnabled(false);
    }

    /**
     * システム警告画面表示タスクを開始します。
     * Starts the alert dialog display task.
     */
    private void startAlertDialogDisplayTask() {
        if (mAlertDialogDisplayTask != null) {
            mAlertDialogDisplayTask.cancel(false);
        }
        mAlertDialogDisplayTask = new AlertDialogDisplayTask();
        mAlertDialogDisplayTask.execute();
    }

    /**
     * システム警告画面表示タスクをキャンセルします。
     * Stop the alert dialog display task.
     */
    private void stopAlertDialogDisplayTask() {
        if (mAlertDialogDisplayTask != null) {
            mAlertDialogDisplayTask.cancel(false);
            mAlertDialogDisplayTask = null;
        }
    }

    /**
     * システム警告画面の表示有無を判断し、必要な場合は表示要求を行う非同期タスクです。
     * The asynchronous task to judge to display system warning screen and to request to display the screen if necessary.
     */
    class AlertDialogDisplayTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            ScanServiceAttributeSet attributes = mApplication.getScanService().getAttributes();
            OccuredErrorLevel errorLevel = (OccuredErrorLevel) attributes.get(OccuredErrorLevel.class);

            if (OccuredErrorLevel.ERROR.equals(errorLevel)
                    || OccuredErrorLevel.FATAL_ERROR.equals(errorLevel)) {
                ScannerState state = (ScannerState) attributes.get(ScannerState.class);
                ScannerStateReasons stateReasons = (ScannerStateReasons) attributes.get(ScannerStateReasons.class);

                String stateString = makeAlertStateString(state);
                String reasonString = makeAlertStateReasonString(stateReasons);
                if (isCancelled()) {
                    return null;
                }

                mApplication.displayAlertDialog(ALERT_DIALOG_APP_TYPE_SCANNER, stateString, reasonString);

                mAlertDialogDisplayed = true;
                mLastErrorLevel = errorLevel;
            }
            return null;
        }

    }

    /**
     * 省エネ復帰要求タスクを開始します。
     * Starts the return request from energy saving task.
     */
    private void startReturnRequestFromEnergySavingTask() {
        if (mReturnRequestFromEnergySavingTask != null) {
            mReturnRequestFromEnergySavingTask.cancel(true);
        }
        mReturnRequestFromEnergySavingTask = new ReturnRequestFromEnergySavingTask();
        mReturnRequestFromEnergySavingTask.execute();
    }

    /**
     * 省エネ復帰要求タスクをキャンセルします。
     * Stop the return request from energy saving task.
     */
    private void stopReturnRequestFromEnergySavingTask() {
        if (mReturnRequestFromEnergySavingTask != null) {
            mReturnRequestFromEnergySavingTask.cancel(true);
            mReturnRequestFromEnergySavingTask = null;
        }
    }

    /**
     * 機器本体の電力モードを確認し、必要な場合は省エネ復帰要求行う非同期タスクです。
     * このタスクでは復帰要求のみを行います。
     * 省エネから復帰したことを確認する場合は、電力モード通知を確認するようにしてください。
     * This is an asynchronous task to check the machine's power mode and to request to recover from Energy Saver mode if necessary.
     * This task only requests to recover from Energy Saver mode.
     * To check if the machine has recovered from Energy Saver mode, enable to check power mode notification.
     */
    class ReturnRequestFromEnergySavingTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean needsRequest;

            int powerMode = mApplication.getSystemStateMonitor().getPowerMode();
            Log.d(Const.TAG, PREFIX + "getPowerMode=" + powerMode);

            switch (powerMode) {
                case SystemStateMonitor.POWER_MODE_ENGINE_OFF:
                case SystemStateMonitor.POWER_MODE_OFF_MODE:
                case SystemStateMonitor.POWER_MODE_UNKNOWN:
                    needsRequest = true;
                    break;
                default:
                    needsRequest = false;
                    break;
            }

            Boolean result = Boolean.FALSE;
            if (needsRequest) {
                result = mApplication.controllerStateRequest(SmartSDKApplication.REQUEST_CONTROLLER_STATE_FUSING_UNIT_OFF);
            }
            return result;
        }

    }
    
    /**
     * Add interface LogRecorder implementation
     */
    private final LogRecorder mLogRecoder = new LogRecorder() {
        @Override
        public void logging(String level, String tag, String msg) {
            
            if("debug".equals(level)) {
                Log.d(tag, msg);
            } else if("info".equals(level)) {
                Log.i(tag, msg);
            } else if("warn".equals(level)){
                Log.w(tag, msg);
            }
            
        }
    };

}
