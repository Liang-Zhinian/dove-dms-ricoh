/**
 * Copyright (C) 2013-2016 RICOH Co.,LTD.
 * All rights reserved.
 */
package com.dove.sample.app.print.activity;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dove.MainApplication;
import com.dove.PrintApplicationWrapper;
import com.dove.R;
import com.dove.SimplePrintStateMachine;
import com.dove.sample.app.print.Const;
import com.dove.sample.app.print.application.PrintSettingDataHolder;
import com.dove.sample.app.print.application.PrintSettingSupportedHolder;
import com.dove.sample.app.print.application.SystemStateMonitor;
import com.dove.sample.function.common.SmartSDKApplication;
import com.dove.sample.function.common.impl.AsyncConnectState;
import com.dove.sample.function.print.PrintFile.PDL;
import com.dove.sample.function.print.PrintService;
import com.dove.sample.function.print.attribute.PrintRequestAttribute;
import com.dove.sample.function.print.attribute.PrintServiceAttributeSet;
import com.dove.sample.function.print.attribute.standard.Copies;
import com.dove.sample.function.print.attribute.standard.PrintColor;
import com.dove.sample.function.print.attribute.standard.PrinterOccuredErrorLevel;
import com.dove.sample.function.print.attribute.standard.PrinterState;
import com.dove.sample.function.print.attribute.standard.PrinterStateReasons;
import com.dove.sample.function.print.attribute.standard.Staple;
import com.dove.sample.function.print.event.PrintServiceAttributeListener;
import com.dove.sample.wrapper.log.Logger;
import com.dove.sample.wrapper.log.Logger.LogRecorder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * プリントサンプルアプリのメインアクティビティです。
 * Main activity of the print sample application.
 */
public class SimplePrintMainActivity extends Activity {

    /**
     * 印刷に必要な設定値
     * Print settings.
     */
    private PrintSettingDataHolder mHolder = new PrintSettingDataHolder();

    /**
     * 印刷に実現するために必要なイベントを受け取るためのリスナー
     * Listener to receive print service attribute event.
     */
    private SimplePrintServiceAttributeListenerImpl mServiceAttributeListener;

    /**
     * 印刷枚数を指定するボタン
     * Button to set the number of prints.
     */
    private Button mPrintCountBtn;

    /**
     * 印刷カラーを指定するボタン
     * Print color setting button
     */
    private Button mPrintColorBtn;

    /**
     * 印刷ファイル選択ダイアログを開くボタン
     * Button to open the print file selection dialog.
     */
    private Button mSelectFileBtn;

    /**
     * その他の設定ダイアログを開くためのボタン
     * Button to open the other setting dialog.
     */
    private LinearLayout mOtherSettingLayout;

    /**
     * 印刷開始ボタン
     * Button to start printing.
     */
    private RelativeLayout mStartLayout;

    /**
     * 省エネ復帰要求タスク
     * Return request from energy saving task
     */
    private ReturnRequestFromEnergySavingTask mReturnRequestFromEnergySavingTask = null;

    /**
     * 設定画面からの通知を受け取るブロードキャストレシーバー
     * Broadcast receiver to accept intents from setting dialog
     */
    private BroadcastReceiver mReceiver;

    /**
     * メインアクティビティの最前面表示状態を保持します。
     * onResume - onPause の間はtrueになります。
     * PrintMainActivity foreground flag
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
     * プリンタサンプルアプリケーションのオブジェクト
     * Application object
     */
    private PrintApplicationWrapper mApplication;

    /**
     * システム警告画面表示タスク
     * Asynchronous task to request to display system warning screen
     */
    private AlertDialogDisplayTask mAlertDialogDisplayTask = null;

    /**
     * Define the prefix for log information with abbreviation package and class name
     */
    private final static String PREFIX = "activity:MainAct:";

    public PrintApplicationWrapper getmApplication() {
        return mApplication;
    }

    /**
     * アクティビティが生成されると呼び出されます。
     * [処理内容]
     * (1)アプリケーションの初期化
     * (2)印刷ファイル選択ボタンの設定
     * (3)印刷枚数ボタンの設定
     * (4)印刷カラーボタンの設定
     * (5)その他の設定ボタンの設定
     * (6)印刷開始ボタンの設定
     * (7)リスナー初期化
     * <p>
     * Called when an activity is created.
     * [Processes]
     * (1)initialize application
     * (2)Set the print file selection button.
     * (3)Set the number of copies setting button.
     * (4)Set the Print Color setting button.
     * (5)Set the other setting button.
     * (6)Set the print start button.
     * (7)Initialize Listener
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.setRecorder(mLogRecoder);
        setContentView(R.layout.main);

        //(1)
        initialize();

        IntentFilter filter = new IntentFilter();
        filter.addAction(SimplePrintStateMachine.INTENT_ACTION_LOCK_SYSTEM_RESET);     //システムリセット抑止状態を設定するための内部インテント
        filter.addAction(SimplePrintStateMachine.INTENT_ACTION_UNLOCK_SYSTEM_RESET);   //システムリセット抑止状態を解除するための内部インテント

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (SimplePrintStateMachine.INTENT_ACTION_LOCK_SYSTEM_RESET.equals(action)) {
                    mRequestedSystemResetLock = true;
                    processSystemResetLock();
                } else if (SimplePrintStateMachine.INTENT_ACTION_UNLOCK_SYSTEM_RESET.equals(action)) {
                    mRequestedSystemResetLock = false;
                    processSystemResetUnlock();
                }
            }
        };
        registerReceiver(mReceiver, filter);

        //(2)
        mSelectFileBtn = (Button) findViewById(R.id.btn_select_file);
        mSelectFileBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Check dialog showing or not
                if (mApplication.ismIsWinShowing()) {
                    return;
                }
                //If no dialog displays, set flag to true
                mApplication.setmIsWinShowing(true);

                List<String> fileList = new ArrayList<String>();
                fileList.add(v.getContext().getString(R.string.assets_file_sample_01));
                fileList.add(v.getContext().getString(R.string.assets_file_sample_02));
                fileList.add(v.getContext().getString(R.string.assets_file_sample_03));

                AlertDialog dlg = DialogUtil.selectFileDialog(v.getContext(), fileList);
                dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    /**
                     * 印刷ファイル選択ダイアログが閉じると呼び出されるメソッドです。
                     * [処理内容]
                     * (1)メインアクティビティが保持する設定値の更新
                     * (2)メインアクティビティが保持する設定値に応じてボタンの表示を更新
                     *
                     * The method when the print file selection dialog closed.
                     * [Processes]
                     *   (1)Updates the setting value saved on PrintMainActivity
                     *   (2)Updates the button displays according to the setting value saved on PrintMainActivity
                     */
                    @Override
                    public void onDismiss(DialogInterface arg0) {
                        //Close dialog, reset flag to false
                        mApplication.setmIsWinShowing(false);
                        //Judge whether Item on Select File dialog is clicked
                        if (DialogUtil.isSelectFItemClicked()) {

                            //(1)
                            updateSettings();

                            //(2)
                            updateSettingButtons();

                        }


                    }
                });
                DialogUtil.showDialog(dlg, DialogUtil.DEFAULT_DIALOG_WIDTH);
            }
        });

        //(3)
        mPrintCountBtn = (Button) findViewById(R.id.btn_print_page);
        mPrintCountBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Check dialog showing or not
                if (mApplication.ismIsWinShowing()) {
                    return;
                }
                //If no dialog displays, set flag to true
                mApplication.setmIsWinShowing(true);

                AlertDialog dlg = DialogUtil.createPrintCountDialog(v.getContext());
                dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface arg0) {
                        //Close dialog, reset flag to false
                        mApplication.setmIsWinShowing(false);

                        if (null != mHolder.getSelectedCopiesValue()) {
                            String printCount = mHolder.getSelectedCopiesValue().getValue()
                                    .toString();
                            mPrintCountBtn.setText(printCount);
                        }
                    }
                });
                DialogUtil.showDialog(dlg, DialogUtil.DEFAULT_DIALOG_WIDTH);
            }
        });

        //(4)
        mPrintColorBtn = (Button) findViewById(R.id.btn_print_color);
        mPrintColorBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * 印刷カラー設定ボタンが押下されると呼び出されるメソッドです。
             * [処理内容]
             * (1)設定可能なカラーを取得
             * (2)印刷カラー設定ダイアログを表示
             *
             * The method called when the print color setting button clicked.
             * [Processes]
             *   (1)Obtains the supported print colors
             *   (2)Displays the print color setting dialog
             */
            @Override
            public void onClick(View view) {
                //Check dialog showing or not
                if (mApplication.ismIsWinShowing()) {
                    return;
                }
                //If no dialog displays, set flag to true
                mApplication.setmIsWinShowing(true);

                //(1)
                PDL selectedPDL = getSettingHolder().getSelectedPDL();
                Map<PDL, PrintSettingSupportedHolder> map =
                        mApplication.getSettingSupportedDataHolders();
                PrintSettingSupportedHolder holder = map.get(selectedPDL);

                //(2)
                AlertDialog dlg = DialogUtil.createPrintColorDialog(view.getContext(), holder.getSelectablePrintColorList());

                dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        //Close dialog, reset flag to false
                        mApplication.setmIsWinShowing(false);

                        if (null != mHolder.getSelectedPrintColorValue()) {
                            String printColor = PrintColorUtil.getPrintColorResourceString(SimplePrintMainActivity.this, mHolder.getSelectedPrintColorValue());
                            mPrintColorBtn.setText(printColor);
                        }
                    }
                });
                DialogUtil.showDialog(dlg, DialogUtil.DEFAULT_DIALOG_WIDTH);
            }
        });

        //(5)
        mOtherSettingLayout = (LinearLayout) findViewById(R.id.btn_other);
        mOtherSettingLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Check dialog showing or not
                if (mApplication.ismIsWinShowing()) {
                    return;
                }
                //If no dialog displays, set flag to true
                mApplication.setmIsWinShowing(true);

                AlertDialog dlg = DialogUtil.createOtherSettingDialog(v.getContext());
                if (null == dlg) {
                    //No dialog is created, reset flag to false
                    mApplication.setmIsWinShowing(false);
                    return;
                }

                dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface arg0) {
                        //Close dialog, reset flag to false
                        mApplication.setmIsWinShowing(false);
                    }
                });

                DialogUtil.showDialog(dlg, DialogUtil.INPUT_DIALOG_WIDTH);
            }
        });

        //(6)
        mStartLayout = (RelativeLayout) findViewById(R.id.layout_start);
        mStartLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Check dialog showing or not
                if (mApplication.ismIsWinShowing()) {
                    return;
                }
                //If no dialog displays, set flag to true
                mApplication.setmIsWinShowing(true);

                if (null == mHolder ||
                        null == mHolder.getSelectedPrintAssetFileName() ||
                        null == mHolder.getSelectedPDL()) {

                    Log.d(Const.TAG, PREFIX + R.string.error_settings_not_found);
                    Toast.makeText(v.getContext(), R.string.error_settings_not_found, Toast.LENGTH_LONG).show();

                    //Close dialog, reset flag to false
                    mApplication.setmIsWinShowing(false);

                    return;
                }
                mApplication.startPrint(mHolder);
            }
        });

        //(7)
        initializeListener();
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
     * アプリケーションが破棄される際に呼び出されます。
     * [処理内容]
     * (1)メインアクティビティ終了イベントをステートマシンに送る
     * ジョブ実行中であれば、ジョブがキャンセルされます。
     * (2)サービスからイベントリスナーを除去する
     * (3)非同期タスクが実行中だった場合、キャンセルする
     * <p>
     * Called when the application is destroy.
     * [Processes]
     * (1) Send PrintMainActivity destoyed event to the state machine.
     * If job is in process, it is cancelled.
     * (2) Removes the event listener from PrintService.
     * (3) If asynchronous task is in process, the task is cancelled.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        DialogUtil.setSelectFItemClicked(false);

        //アクティビティ終了時は、ロック設定状態に関わらずシステムリセットのロックを解除します
        processSystemResetUnlock();

        //(1)
        mApplication.getStateMachine().procPrintEvent(SimplePrintStateMachine.PrintEvent.CHANGE_APP_ACTIVITY_DESTROYED);

        //(2)
        if (mServiceAttributeListener != null) {
            PrintService service = mApplication.getPrintService();
            service.removePrintServiceAttributeListener(mServiceAttributeListener);
        }

        //(3)
        stopReturnRequestFromEnergySavingTask();

        unregisterReceiver(mReceiver);
        mReceiver = null;

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
     * システムリセットのロックを設定します。
     * ジョブ実行中、かつメインアクティビティ前面表示中のみ、SDKServiceへシステムリセットのロックを設定します。
     * Locking system auto reset.
     * When job is executing and MainAcitivity is in foreground, set system auto reset of SDKService.
     */
    private void processSystemResetLock() {
        if (mIsForeground && mRequestedSystemResetLock) {
            mApplication.getSSDKApplication().lockSystemReset();
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
        mApplication.getSSDKApplication().unlockSystemReset();
    }

    /**
     * アプリケーションを初期化します。
     * [処理内容]
     * (1)PrintServiceからイベントを受信するリスナーを生成
     * (2)UIスレッドを使用するためのタスクを生成
     * (3)ステートマシンに初期化イベントを送信
     * <p>
     * Initialize the application.
     * [Processes]
     * (1)Create the listener that receives events from PrintService.
     * (2)Create and start the PrintService initialize task.
     * (3)Post the initial event to state machine.
     */
    private void initializeListener() {
        //(1)
        mServiceAttributeListener = new SimplePrintServiceAttributeListenerImpl(this, new Handler());

        //(2)
        new PrintServiceInitTask().execute(mServiceAttributeListener);

        //(3)
        mApplication.getStateMachine().procPrintEvent(
                SimplePrintStateMachine.PrintEvent.CHANGE_APP_ACTIVITY_INITIAL);
    }

    /**
     * 各設定値ボタンの表示を更新します。
     * Update setting button displays.
     */
    private void updateSettingButtons() {
        TextView fileNameTxt = (TextView) findViewById(R.id.text_select_file);
        fileNameTxt.setText(mHolder.getSelectedPrintAssetFileName());

        if (null != mHolder.getSelectedCopiesValue()) {
            mPrintCountBtn.setEnabled(true);
            mPrintCountBtn.setText(mHolder.getSelectedCopiesValue().getValue().toString());
        } else {
            mPrintCountBtn.setText("");
            mPrintCountBtn.setEnabled(false);
        }

        if (null != mHolder.getSelectedPrintColorValue()) {
            mPrintColorBtn.setEnabled(true);
            mPrintColorBtn.setText(PrintColorUtil.getPrintColorResourceString(SimplePrintMainActivity.this,
                    mHolder.getSelectedPrintColorValue()));
        } else {
            mPrintColorBtn.setText("");
            mPrintColorBtn.setEnabled(false);
        }

        if (null != mHolder.getSelectedStaple()) {
            mOtherSettingLayout.setEnabled(true);

        } else {
            mOtherSettingLayout.setEnabled(false);
        }

    }

    /**
     * メイン画面の初期化を行います。
     * Initialize the main screen.
     */
    private void initSetting() {
        mHolder.setSelectedPrintAssetFileName(getString(R.string.assets_file_sample_01));
        updateSettings();
        updateSettingButtons();
    }

    /**
     * 印刷ジョブのPDLを決定します。
     * [処理内容]
     * (1)選択中のファイルの拡張子を取得
     * (2)設定可能なPDLの一覧を取得
     * (3)プリンタに送信するPDLを決定
     * [注意]
     * 本サンプルではファイルの拡張子からPDLを判別していますが、
     * PDLはファイルの拡張子から確定できるものではありません。
     * <p>
     * Sets PDL of the print job.
     * [Processes]
     * (1)Obtains the extension of the selected file.
     * (2)Obtains a list of supported PDL.
     * (3)Determines the PDL to be sent to the printer.
     *
     * @param fileName
     * @return PDL
     */
    public PDL currentPDL(String fileName) {

        if (fileName == null) {
            return null;
        }

        //(1)
        PDL currentPDL = null;
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);

        //(2)
        Set<PDL> pdlList = mApplication.getSettingSupportedDataHolders().keySet();

        //(3)
        if (ext.equals(getString(R.string.file_extension_PDF))
                && pdlList.contains(PDL.PDF)) {
            currentPDL = PDL.PDF;
        } else if (ext.equals(getString(R.string.file_extension_PRN))
                && pdlList.contains(PDL.RPCS)) {
            currentPDL = PDL.RPCS;
        } else if (ext.equals(getString(R.string.file_extension_XPS))
                && pdlList.contains(PDL.XPS)) {
            currentPDL = PDL.XPS;
        } else if (ext.equals(getString(R.string.file_extension_TIF))
                && pdlList.contains(PDL.TIFF)) {
            currentPDL = PDL.TIFF;
        }
        return currentPDL;
    }

    /**
     * 印刷に必要な各種設定値を更新します。
     * [処理内容]
     * (1)ファイル名から印刷ファイルのPDLを確定する
     * (2)印刷ファイルのPDLで設定可能な値を取得する
     * (3)設定可能な項目に初期値を設定する
     * <p>
     * Updates print setting values.
     * [Processes]
     * (1)Determines the print file PDL from the file name
     * (2)Obtains the supported value of print file PDL
     * (3)Set the initial value to the available settings.
     */
    private boolean updateSettings() {

        String filename = mHolder.getSelectedPrintAssetFileName();
        if (null == filename) {
            return false;
        }

        //(1)
        mHolder.setSelectedPDL(currentPDL(filename));

        if (null == mHolder.getSelectedPDL()) {
            return false;
        }

        //(2)
        Set<Class<? extends PrintRequestAttribute>> categories;
        Map<PDL, PrintSettingSupportedHolder> supportedHolderMap =
                mApplication.getSettingSupportedDataHolders();
        categories = supportedHolderMap.get(mHolder.getSelectedPDL()).getSelectableCategories();

        if (null == categories) {
            return false;
        }

        //(3)
        if (categories.contains(Copies.class)) {
            mHolder.setSelectedCopiesValue(
                    new Copies(Integer.parseInt(getString(R.string.default_copies))));
        } else {
            mHolder.setSelectedCopiesValue(null);
        }
        if (categories.contains(Staple.class)) {
            List<Staple> stapleList = StapleUtil.getSelectableStapleList(this);

            if (stapleList != null) {
                mHolder.setSelectedStaple(stapleList.get(0));
            } else {
                mHolder.setSelectedStaple(null);
            }
        } else {
            mHolder.setSelectedStaple(null);
        }
        if (categories.contains(PrintColor.class)) {
            List<PrintColor> printColorList = PrintColorUtil.getSelectablePrintColorList(this);

            if (printColorList != null) {
                mHolder.setSelectedPrintColorValue(printColorList.get(0));
            } else {
                mHolder.setSelectedPrintColorValue(null);
            }
        } else {
            mHolder.setSelectedPrintColorValue(null);
        }
        return true;
    }

    public PrintSettingDataHolder getSettingHolder() {
        return mHolder;
    }

    public void setSettingHolder(PrintSettingDataHolder holder) {
        this.mHolder = holder;
    }

    /**
     * アプリケーションを初期化します。
     * ここでは、ステートマシンの初期化を行います。
     * Initializes the application.
     * StateMachine is initialized here.
     */
    private void initialize() {

        mApplication = new PrintApplicationWrapper((SmartSDKApplication) this.getApplication());
        SimplePrintStateMachine stateMachine = mApplication.getStateMachine();
        stateMachine.setContext(this);
    }

    /**
     * 非同期でプリントサービスとの接続を行います。
     * Connects with the print service asynchronously.
     */
    class PrintServiceInitTask extends AsyncTask<PrintServiceAttributeListener, Void, Integer> {

        AsyncConnectState addListenerResult = null;
        AsyncConnectState getAsyncConnectStateResult = null;

        /**
         * UIスレッドのバックグラウンドで実行されるメソッドです。
         * [処理内容]
         * (1)プリントサービスのイベントを受信するリスナーを設定します。
         * 機器が利用可能になるか、キャンセルが押されるまでリトライします。
         * (2)非同期イベントの接続確認を行います。
         * 接続可能になるか、キャンセルが押されるまでリトライします。
         * (3)接続に成功した場合は、プリントサービスから各設定の設定可能値を取得します。
         * <p>
         * Runs in the background on the UI thread.
         * [Processes]
         * (1) Sets the listener to receive print service events.
         * This task repeats until the machine becomes available or cancel button is touched.
         * (2) Confirms the asynchronous connection.
         * This task repeats until the connection is confirmed or cancel button is touched.
         * (3) After the machine becomes available and connection is confirmed,
         * obtains job setting values.
         */
        @Override
        protected Integer doInBackground(PrintServiceAttributeListener... listeners) {

            PrintService printService = mApplication.getPrintService();

            //(1)
            while (true) {
                if (isCancelled()) {
                    return -1;
                }
                addListenerResult = printService.addPrintServiceAttributeListener(listeners[0]);

                if (addListenerResult == null) {
                    sleep(100);
                    continue;
                }

                if (addListenerResult.getState() == AsyncConnectState.STATE.CONNECTED) {
                    break;
                }

                if (addListenerResult.getErrorCode() == AsyncConnectState.ERROR_CODE.NO_ERROR) {
                    // do nothing
                } else if (addListenerResult.getErrorCode() == AsyncConnectState.ERROR_CODE.BUSY) {
                    sleep(10000);
                } else if (addListenerResult.getErrorCode() == AsyncConnectState.ERROR_CODE.TIMEOUT) {
                    // do nothing
                } else if (addListenerResult.getErrorCode() == AsyncConnectState.ERROR_CODE.INVALID) {
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
                if (isCancelled()) {
                    return -1;
                }
                getAsyncConnectStateResult = printService.getAsyncConnectState();

                if (getAsyncConnectStateResult == null) {
                    sleep(100);
                    continue;
                }

                if (getAsyncConnectStateResult.getState() == AsyncConnectState.STATE.CONNECTED) {
                    break;
                }

                if (getAsyncConnectStateResult.getErrorCode() == AsyncConnectState.ERROR_CODE.NO_ERROR) {
                    // do nothing
                } else if (getAsyncConnectStateResult.getErrorCode() == AsyncConnectState.ERROR_CODE.BUSY) {
                    sleep(10000);
                } else if (getAsyncConnectStateResult.getErrorCode() == AsyncConnectState.ERROR_CODE.TIMEOUT) {
                    // do nothing
                } else if (getAsyncConnectStateResult.getErrorCode() == AsyncConnectState.ERROR_CODE.INVALID) {
                    return 0;
                } else {
                    // unknown state
                    return 0;
                }
            }

            //(3)
            if (addListenerResult.getState() == AsyncConnectState.STATE.CONNECTED
                    && getAsyncConnectStateResult.getState() == AsyncConnectState.STATE.CONNECTED) {

                List<PDL> supportedPDL = printService.getSupportedPDL();
                if (supportedPDL == null) return null;

                for (PDL pdl : supportedPDL) {
                    mApplication
                            .putPrintSettingSupportedHolder(pdl,
                                    new PrintSettingSupportedHolder(printService, pdl));
                }

            }

            return 0;
        }

        /**
         * doInBackground()実行後に呼び出されるメソッドです。
         * Called after doInBackground().
         */
        @Override
        protected void onPostExecute(Integer result) {

            if (result != 0) {
                /* canceled. */
                return;
            }

            if (addListenerResult.getState() == AsyncConnectState.STATE.CONNECTED
                    && getAsyncConnectStateResult.getState() == AsyncConnectState.STATE.CONNECTED) {
                // connection succeeded.
                initSetting();
                mApplication.getStateMachine().procPrintEvent(
                        SimplePrintStateMachine.PrintEvent.CHANGE_APP_ACTIVITY_STARTED);
            } else {
                // the connection is invalid.
                mApplication.getStateMachine().procPrintEvent(
                        SimplePrintStateMachine.PrintEvent.CHANGE_APP_ACTIVITY_START_FAILED);
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
                Log.d(Const.TAG, PREFIX + e.toString());
            }
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
            boolean needsRequest = false;

            int powerMode = mApplication.getSystemStateMonitor().getPowerMode();
            Log.d(Const.TAG, PREFIX + "getPowerMode=" + powerMode);

            if (powerMode != SystemStateMonitor.POWER_MODE_NORMAL_STANDBY) {
                needsRequest = true;
            }

            Boolean result = Boolean.FALSE;
            if (needsRequest) {
                result = mApplication.getSSDKApplication().controllerStateRequest(
                        SmartSDKApplication.REQUEST_CONTROLLER_STATE_NORMAL_STANDBY);
            }
            return result;
        }

    }

    /**
     * システム警告画面表示要求に渡す状態文字列を生成します。
     * Creates the state string to be passed to system warning screen display request.
     *
     * @param state プリンタサービス状態
     *              State of print service
     * @return 状態文字列
     * State string
     */
    public String makeAlertStateString(PrinterState state) {
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
     * @param stateReasons プリンタサービス状態理由
     *                     Print service state reason
     * @return 状態理由文字列
     * State reason string
     */
    public String makeAlertStateReasonString(PrinterStateReasons stateReasons) {
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
     * If the application is in the foreground state, true is returned.
     */
    public boolean isForegroundApp(String packageName) {
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
     * The name of the FQCN class name of the top class. If the name cannot be obtained, null is returned.
     */
    public String getTopActivityClassName(String packageName) {
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
     * システム警告画面の表示有無を判断し、必要な場合は表示要求を行う非同期タスクです。
     * The asynchronous task to judge to display system warning screen and to request to display the screen if necessary.
     */
    class AlertDialogDisplayTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            PrintServiceAttributeSet attributes = mApplication.getPrintService().getAttributes();
            PrinterOccuredErrorLevel errorLevel = (PrinterOccuredErrorLevel) attributes.get(PrinterOccuredErrorLevel.class);

            if (PrinterOccuredErrorLevel.ERROR.equals(errorLevel)
                    || PrinterOccuredErrorLevel.FATAL_ERROR.equals(errorLevel)) {
                PrinterState state = (PrinterState) attributes.get(PrinterState.class);
                PrinterStateReasons stateReasons = (PrinterStateReasons) attributes.get(PrinterStateReasons.class);

                String stateString = makeAlertStateString(state);
                String reasonString = makeAlertStateReasonString(stateReasons);
                if (isCancelled()) {
                    return null;
                }

                mApplication.getSSDKApplication().displayAlertDialog(SimplePrintServiceAttributeListenerImpl.ALERT_DIALOG_APP_TYPE_PRINTER, stateString, reasonString);
                mServiceAttributeListener.setAlertDialogDisplayed(true);
                mServiceAttributeListener.setLastErrorLevel(errorLevel);
            }
            return null;
        }

    }

    /**
     * Implement LogRecorder interface
     */
    private final LogRecorder mLogRecoder = new LogRecorder() {
        @Override
        public void logging(String level, String tag, String msg) {

            if ("debug".equals(level)) {
                Log.d(tag, msg);
            } else if ("info".equals(level)) {
                Log.i(tag, msg);
            } else if ("warn".equals(level)) {
                Log.w(tag, msg);
            }

        }
    };

}
