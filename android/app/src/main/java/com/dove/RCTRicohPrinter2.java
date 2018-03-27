package com.dove;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.dove.sample.app.print.Const;
import com.dove.sample.app.print.activity.DialogUtil;
import com.dove.sample.app.print.activity.PrintColorUtil;
//import com.dove.sample.app.print.activity.PrintMainActivity;
//import com.dove.sample.app.print.activity.SimplePrintMainActivity;
//import com.dove.sample.app.print.activity.SimplePrintServiceAttributeListenerImpl;
//import com.dove.sample.app.print.activity.PrintMainActivity;
//import com.dove.sample.app.print.activity.PrintServiceAttributeListenerImpl;
import com.dove.sample.app.print.activity.StapleUtil;
//import com.dove.sample.app.print.application.PrintSampleApplication;
//import com.dove.sample.app.print.application.PrintSampleApplication;
import com.dove.sample.app.print.application.PrintSampleApplication;
import com.dove.sample.app.print.application.PrintSettingDataHolder;
import com.dove.sample.app.print.application.PrintSettingSupportedHolder;
//import com.dove.sample.app.print.application.PrintStateMachine;
//import com.dove.sample.app.print.application.PrintStateMachine;
import com.dove.sample.app.print.application.SystemStateMonitor;
import com.dove.sample.function.common.SmartSDKApplication;
import com.dove.sample.function.common.impl.AsyncConnectState;
import com.dove.sample.function.print.PrintFile;
import com.dove.sample.function.print.PrintJob;
import com.dove.sample.function.print.PrintService;
import com.dove.sample.function.print.attribute.PrintJobAttributeSet;
import com.dove.sample.function.print.attribute.PrintRequestAttribute;
import com.dove.sample.function.print.attribute.PrintServiceAttributeSet;
import com.dove.sample.function.print.attribute.standard.Copies;
import com.dove.sample.function.print.attribute.standard.PrintColor;
import com.dove.sample.function.print.attribute.standard.PrintJobPrintingInfo;
import com.dove.sample.function.print.attribute.standard.PrintJobState;
import com.dove.sample.function.print.attribute.standard.PrinterOccuredErrorLevel;
import com.dove.sample.function.print.attribute.standard.PrinterState;
import com.dove.sample.function.print.attribute.standard.PrinterStateReasons;
import com.dove.sample.function.print.attribute.standard.Staple;
import com.dove.sample.function.print.event.PrintJobAttributeEvent;
import com.dove.sample.function.print.event.PrintJobAttributeListener;
import com.dove.sample.function.print.event.PrintServiceAttributeEvent;
import com.dove.sample.function.print.event.PrintServiceAttributeListener;
import com.dove.sample.wrapper.common.BinaryResponseBody;
import com.dove.sample.wrapper.common.Response;
import com.dove.sample.wrapper.common.Utils;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;


/**
 * Created by Administrator on 2017/12/8.
 */

public class RCTRicohPrinter extends ReactContextBaseJavaModule implements IPrintApplication {
    private final static String PREFIX = "rectmodule:RicohPrinter:";

    private SmartSDKApplication mApplication;
    protected int timeOfWaitingNextOriginal = 0;
    private PrintService mPrintService;
    private PrintJob mPrintJob;
    private PrintJobAttributeListener mJobAttributeListener;

    private SimplePrintStateMachine mStateMachine;
    private ReturnRequestFromEnergySavingTask mReturnRequestFromEnergySavingTask = null;
    private AlertDialogDisplayTask mAlertDialogDisplayTask = null;

    /**
     * 機器に設定可能な印刷プロパティの一覧
     * Map of the supported print properties
     */
    private Map<PrintFile.PDL, PrintSettingSupportedHolder> mSettingDataHolders;

    /**
     * システム状態監視
     * System state monitor
     */
    private SystemStateMonitor mSystemStateMonitor;

    private BroadcastReceiver mReceiver;
    private boolean mRequestedSystemResetLock = false;


    /**
     * Dialogの最前面表示状態を保持します。
     * Dialog showing flag
     */
    private boolean mIsWinShowing = false;

    public boolean ismIsWinShowing() {
        return mIsWinShowing;
    }

    public void setmIsWinShowing(boolean mIsWinShowing) {
        this.mIsWinShowing = mIsWinShowing;
    }

    public RCTRicohPrinter(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "RCTRicohPrinter";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        return constants;
    }

    @Override
    public void initialize() {
        onCreate();

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
        this.getCurrentActivity().getApplicationContext().registerReceiver(mReceiver, filter);

        initializeListener();
    }

    @ReactMethod
    public void startPrint(Promise promise) {
        try {
            //Check dialog showing or not
            if (this.ismIsWinShowing()) {
                return;
            }
            //If no dialog displays, set flag to true
            this.setmIsWinShowing(true);

            if (null == mHolder ||
                    null == mHolder.getSelectedPrintAssetFileName() ||
                    null == mHolder.getSelectedPDL()) {
                Toast.makeText(this.getReactApplicationContext(), R.string.error_settings_not_found, Toast.LENGTH_LONG).show();

                //Close dialog, reset flag to false
                this.setmIsWinShowing(false);

                return;
            }
            this.startPrint(mHolder);

            promise.resolve("Start print success!!");
        } catch (android.content.ActivityNotFoundException e) {

            promise.reject("Start print error!!");
        }
    }

    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           @Nullable WritableMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    @Override
    public SmartSDKApplication getSSDKApplication() {
        return mApplication;
    }

    /**
     * 次原稿までの最大待ち時間を取得します。
     * Obtains the maximum waiting time.
     *
     * @return
     */
    public int getTimeOfWaitingNextOriginal() {
        return timeOfWaitingNextOriginal;
    }

    @Override
    public void setAppState(String activityName, int state, String errMessage, int appType) {
        mApplication.setAppState(activityName, state, errMessage, appType);
    }

    @Override
    public void initJobSetting() {//If a state change listener is registered to the current scan job, the listener is removed.
        initialJob();

        ReactApplicationContext reactContext = getReactApplicationContext();
        WritableMap params = Arguments.createMap();
        params.putString("stateLabel", "READY");
        sendEvent(reactContext, "PrintJobStateUpdated", params);
    }

    @Override
    public boolean lockPowerMode() {
        return mApplication.lockPowerMode();
    }

    @Override
    public boolean lockOffline() {
        return mApplication.lockOffline();

    }

    @Override
    public boolean unlockPowerMode() {
        return true;

    }

    @Override
    public boolean unlockOffline() {
        return mApplication.unlockOffline();

    }

    @Override
    public String getPackageName() {
        return this.getCurrentActivity().getPackageName();
    }

    @Override
    public void sendBroadcast(Intent intent) {
        this.getCurrentActivity().sendBroadcast(intent);
    }

    @Override
    public SimplePrintStateMachine getStateMachine() {
        return mStateMachine;
    }

    @Override
    public Resources getResources() {
        return this.getCurrentActivity().getResources();
    }


    public void onCreate() {
        // 1
        //Register the log tag of PrintSample module which is used in function/wrapper layer
        System.setProperty("jp.co.ricoh.ssdk.sample.log.TAG", Const.TAG);

        Utils.setTagName();


        mStateMachine = new SimplePrintStateMachine(this, new Handler());
        mStateMachine.setContext(this.getReactApplicationContext());

        mSettingDataHolders = new HashMap<PrintFile.PDL, PrintSettingSupportedHolder>();

        mSystemStateMonitor = new SystemStateMonitor(this.getReactApplicationContext());
        mSystemStateMonitor.start();

        mPrintService = PrintService.getService();

        // 2
    }

    @ReactMethod
    public void onTerminate(Promise promise) {
        try {
            mSystemStateMonitor.stop();
            mSystemStateMonitor = null;
            promise.resolve("onTerminate success!!");
        } catch (android.content.ActivityNotFoundException e) {

            promise.reject("onTerminate error!!");
        }
    }

    @ReactMethod
    public void onResume(Promise promise) {
        try {
            // スキャンステートマシンからシステムリセット抑止を要求されている場合、SDKServiceへロックを設定します
            mIsForeground = true;
            processSystemResetLock();

            startAlertDialogDisplayTask();
            startReturnRequestFromEnergySavingTask();
            promise.resolve("onResume success!!");
        } catch (android.content.ActivityNotFoundException e) {

            promise.reject("onResume error!!");
        }
    }

    @ReactMethod
    public void onPause(Promise promise) {
        try {
            // システムリセットロックを解除します
            mIsForeground = false;
            processSystemResetUnlock();

            stopAlertDialogDisplayTask();
            promise.resolve("onPause success!!");
        } catch (android.content.ActivityNotFoundException e) {

            promise.reject("onPause error!!");
        }

    }

    @ReactMethod
    public void onDestroy(Promise promise) {
        try {
            DialogUtil.setSelectFItemClicked(false);

            //アクティビティ終了時は、ロック設定状態に関わらずシステムリセットのロックを解除します
            processSystemResetUnlock();

            //(1)
            mStateMachine.procPrintEvent(SimplePrintStateMachine.PrintEvent.CHANGE_APP_ACTIVITY_DESTROYED);

            //(2)
            if (mServiceAttributeListener != null) {
                PrintService service = mPrintService;
                service.removePrintServiceAttributeListener(mServiceAttributeListener);
            }

            //(3)
            stopReturnRequestFromEnergySavingTask();

            this.getCurrentActivity().getApplicationContext().unregisterReceiver(mReceiver);
            mReceiver = null;
            promise.resolve("onDestroy success!!");
        } catch (android.content.ActivityNotFoundException e) {

            promise.reject("onDestroy error!!");
        }

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
            ((SmartSDKApplication) this.getCurrentActivity().getApplication()).lockSystemReset();
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
        ((SmartSDKApplication) this.getCurrentActivity().getApplication()).unlockSystemReset();
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
        mServiceAttributeListener = new PrintServiceAttributeListenerImpl(new Handler());

        //(2)
        new PrintServiceInitTask().execute(mServiceAttributeListener);

        //(3)
        mStateMachine.procPrintEvent(
                SimplePrintStateMachine.PrintEvent.CHANGE_APP_ACTIVITY_INITIAL);
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

            int powerMode = RCTRicohPrinter.this.getSystemStateMonitor().getPowerMode();
            Log.d(Const.TAG, PREFIX + "getPowerMode=" + powerMode);

            if (powerMode != SystemStateMonitor.POWER_MODE_NORMAL_STANDBY) {
                needsRequest = true;
            }

            Boolean result = Boolean.FALSE;
            if (needsRequest) {
                result = ((SmartSDKApplication) RCTRicohPrinter.this.getCurrentActivity().getApplication()).controllerStateRequest(
                        SmartSDKApplication.REQUEST_CONTROLLER_STATE_NORMAL_STANDBY);
            }
            return result;
        }

    }

    /***********************************************************
     * 公開メソッド
     * public methods
     ***********************************************************/

    /**
     * プリントサービスを取得します。
     * obtains the print service
     */
    public PrintService getPrintService() {
        return mPrintService;
    }

    /**
     * プリント情報をセットします。
     * Sets the print setting object.
     *
     * @param pdl
     * @param holder
     */
    public void putPrintSettingSupportedHolder(PrintFile.PDL pdl, PrintSettingSupportedHolder holder) {
        mSettingDataHolders.put(pdl, holder);
    }

    /**
     * 印刷を開始します。
     * Start print.
     *
     * @param holder
     */
    public void startPrint(PrintSettingDataHolder holder) {
        if (holder == null) return;

        mStateMachine.procPrintEvent(SimplePrintStateMachine.PrintEvent.CHANGE_JOB_STATE_INITIAL, holder);
        mStateMachine.procPrintEvent(SimplePrintStateMachine.PrintEvent.CHANGE_JOB_STATE_PRE_PROCESS, holder);
    }

    /****************************************************************
     * 内部メソッド
     * private methods
     ****************************************************************/

    /**
     * プリントジョブを初期化します。
     * Initializes the print job.
     */
    private void initialJob() {
        if (mPrintJob != null) {
            if (mJobAttributeListener != null) {
                mPrintJob.removePrintJobAttributeListener(mJobAttributeListener);
            }
        }

        mPrintJob = new PrintJob();
        mJobAttributeListener = new PrintJobAttributeListenerImpl();

        mPrintJob.addPrintJobAttributeListener(mJobAttributeListener);

    }

    /**
     * プリントジョブを取得します。
     * Obtains the print job.
     */
    @Override
    public PrintJob getPrintJob() {
        initialJob();
        return mPrintJob;
    }

    /****************************************************************
     * メンバのセッター/ゲッター
     * Setter/Getter method
     ****************************************************************/

    /**
     * プリントジョブの設定データ保持クラスのインスタンスを取得します。
     * Obtains the instance for the class to save print setting data.
     */
    public Map<PrintFile.PDL, PrintSettingSupportedHolder> getSettingSupportedDataHolders() {
        return this.mSettingDataHolders;
    }

    /**
     * システム状態監視クラスのインスタンスを取得します。
     * Obtains the instance for the class to system state monitor.
     */
    public SystemStateMonitor getSystemStateMonitor() {
        return mSystemStateMonitor;
    }


    /*****************************************************************************************
     * *****************************************************************************************
     ******************************************************************************************/

    class PrintJobAttributeListenerImpl implements PrintJobAttributeListener {
        /**
         * ジョブ状態が変化すると呼び出されるメソッドです。
         * ジョブ状態を示すイベントを受信し、受信したイベントに応じてステートマシンにイベントをポストします。
         * Called when the job state changes.
         * Receives the job state event and posts the appropriate event to the statemachine.
         */
        @Override
        public void updateAttributes(PrintJobAttributeEvent attributesEvent) {
            PrintJobAttributeSet attributeSet = attributesEvent.getUpdateAttributes();

            PrintJobState jobState = (PrintJobState) attributeSet.get(PrintJobState.class);
            Log.i(Const.TAG, PREFIX + "JobState[" + jobState + "]");
            if (jobState == null) return;

            switch (jobState) {
                case PENDING:
                    mStateMachine.procPrintEvent(
                            SimplePrintStateMachine.PrintEvent.CHANGE_JOB_STATE_PENDING, null);
                    break;
                case PROCESSING:
                    PrintJobPrintingInfo printingInfo = (PrintJobPrintingInfo) attributeSet.get(
                            PrintJobPrintingInfo.class);

                    mStateMachine.procPrintEvent(
                            SimplePrintStateMachine.PrintEvent.CHANGE_JOB_STATE_PROCESSING, printingInfo);
                    break;
                case PROCESSING_STOPPED:
                    PrintServiceAttributeSet serviceAttributeSet = mPrintService.getAttributes();
                    PrinterStateReasons reasons = (PrinterStateReasons) serviceAttributeSet.get((PrinterStateReasons.class));

                    mStateMachine.procPrintEvent(
                            SimplePrintStateMachine.PrintEvent.CHANGE_JOB_STATE_PROCESSING_STOPPED, reasons);
                    break;
                case ABORTED:
                    serviceAttributeSet = mPrintService.getAttributes();
                    reasons = (PrinterStateReasons) serviceAttributeSet.get((PrinterStateReasons.class));

                    mStateMachine.procPrintEvent(
                            SimplePrintStateMachine.PrintEvent.CHANGE_JOB_STATE_ABORTED, reasons);
                    break;
                case CANCELED:
                    mStateMachine.procPrintEvent(
                            SimplePrintStateMachine.PrintEvent.CHANGE_JOB_STATE_CANCELED, null);
                    break;
                case COMPLETED:
                    mStateMachine.procPrintEvent(
                            SimplePrintStateMachine.PrintEvent.CHANGE_JOB_STATE_COMPLETED, null);
                    break;
                default:
                    break;
            }
        }
    }


    /***********************************************************************************************/

    /**
     * 印刷に必要な設定値
     * Print settings.
     */
    private PrintSettingDataHolder mHolder = new PrintSettingDataHolder();
    private boolean mIsForeground = false;

    /**
     * 印刷に実現するために必要なイベントを受け取るためのリスナー
     * Listener to receive print service attribute event.
     */
    private PrintServiceAttributeListenerImpl mServiceAttributeListener;

    private void initSetting() {
        mHolder.setSelectedPrintAssetFileName(this.getCurrentActivity().getString(R.string.assets_file_sample_01));
        updateSettings();
//        updateSettingButtons();
    }

    public void setmIsForeground(boolean isForeground) {
        mIsForeground = isForeground;
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
    public PrintFile.PDL currentPDL(String fileName) {

        if (fileName == null) {
            return null;
        }

        //(1)
        PrintFile.PDL currentPDL = null;
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);

        //(2)
        Set<PrintFile.PDL> pdlList = RCTRicohPrinter.this.getSettingSupportedDataHolders().keySet();

        //(3)
        if (ext.equals(this.getCurrentActivity().getString(R.string.file_extension_PDF))
                && pdlList.contains(PrintFile.PDL.PDF)) {
            currentPDL = PrintFile.PDL.PDF;
        } else if (ext.equals(this.getCurrentActivity().getString(R.string.file_extension_PRN))
                && pdlList.contains(PrintFile.PDL.RPCS)) {
            currentPDL = PrintFile.PDL.RPCS;
        } else if (ext.equals(this.getCurrentActivity().getString(R.string.file_extension_XPS))
                && pdlList.contains(PrintFile.PDL.XPS)) {
            currentPDL = PrintFile.PDL.XPS;
        } else if (ext.equals(this.getCurrentActivity().getString(R.string.file_extension_TIF))
                && pdlList.contains(PrintFile.PDL.TIFF)) {
            currentPDL = PrintFile.PDL.TIFF;
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
        Map<PrintFile.PDL, PrintSettingSupportedHolder> supportedHolderMap =
                RCTRicohPrinter.this.getSettingSupportedDataHolders();
        categories = supportedHolderMap.get(mHolder.getSelectedPDL()).getSelectableCategories();

        if (null == categories) {
            return false;
        }

        //(3)
        if (categories.contains(Copies.class)) {
            mHolder.setSelectedCopiesValue(
                    new Copies(Integer.parseInt(this.getCurrentActivity().getString(R.string.default_copies))));
        } else {
            mHolder.setSelectedCopiesValue(null);
        }
        if (categories.contains(Staple.class)) {
            List<Staple> stapleList = StapleUtil.getSelectableStapleList(this.getReactApplicationContext());

            if (stapleList != null) {
                mHolder.setSelectedStaple(stapleList.get(0));
            } else {
                mHolder.setSelectedStaple(null);
            }
        } else {
            mHolder.setSelectedStaple(null);
        }
        if (categories.contains(PrintColor.class)) {
            List<PrintColor> printColorList = PrintColorUtil.getSelectablePrintColorList(this.getReactApplicationContext());

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

            PrintService printService = mPrintService;

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

                List<PrintFile.PDL> supportedPDL = printService.getSupportedPDL();
                if (supportedPDL == null) return null;

                for (PrintFile.PDL pdl : supportedPDL) {
                    RCTRicohPrinter.this
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
                mStateMachine.procPrintEvent(
                        SimplePrintStateMachine.PrintEvent.CHANGE_APP_ACTIVITY_STARTED);
            } else {
                // the connection is invalid.
                mStateMachine.procPrintEvent(
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

    class PrintServiceAttributeListenerImpl implements PrintServiceAttributeListener {

        /**
         * メインアクテビティへの参照
         * Reference to PrintMainActivity
         */
//    SimplePrintMainActivity mActivity;

        /**
         * UIスレッドのハンドラ
         * UI thread handler
         */
        Handler mHandler;

        /**
         * 現在発生しているエラーのエラーレベル
         * Level of the currently occurring error
         */
        private PrinterOccuredErrorLevel mLastErrorLevel = null;


        /**
         * システム警告画面が表示されているかのフラグ
         * Flag to indicate if system warning screen is displayed
         */
        private volatile boolean mAlertDialogDisplayed = false;

        /**
         * アプリケーションの種別
         * システム警告ダイアログの設定に使用します。
         * Application type
         * Used for setting system warning dialog.
         */
        public final static String ALERT_DIALOG_APP_TYPE_PRINTER = "PRINTER";

        public PrinterOccuredErrorLevel getLastErrorLevel() {
            return mLastErrorLevel;
        }

        public void setLastErrorLevel(PrinterOccuredErrorLevel mLastErrorLevel) {
            this.mLastErrorLevel = mLastErrorLevel;
        }

        public boolean isAlertDialogDisplayed() {
            return mAlertDialogDisplayed;
        }

        public void setAlertDialogDisplayed(boolean mAlertDialogDisplayed) {
            this.mAlertDialogDisplayed = mAlertDialogDisplayed;
        }

        /**
         * プリントサービスから受け取ったイベント
         * Event received from print service
         */
        PrintServiceAttributeEvent mEvent;

        public PrintServiceAttributeListenerImpl(/* SimplePrintMainActivity activity, */Handler handler) {
//        mActivity = activity;
            mHandler = handler;
        }

        /**
         * プリントサービスからイベントを受け取ったときに呼び出されるメソッドです。
         * The method called when receive an event from the print service.
         *
         * @param event
         */
        @Override
        public void attributeUpdate(PrintServiceAttributeEvent event) {
            mEvent = event;
//        SmartSDKApplication mApplication = (SmartSDKApplication)mActivity.getApplication();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    PrintServiceAttributeSet attributes = mEvent.getAttributes();
//                TextView statusView = (TextView)mActivity.findViewById(R.id.txt_state);
                    StringBuilder statusString = new StringBuilder();

                    PrinterState state = (PrinterState) attributes.get(PrinterState.class);
                    if (state != null) {

                        switch (state) {
                            case IDLE:
                                statusString.append("Ready");
                                break;
                            case MAINTENANCE:
                                statusString.append("Maintenance");
                                break;
                            case PROCESSING:
                                statusString.append("Processing");
                                break;
                            case STOPPED:
                                statusString.append("Stopped");
                                break;
                            case UNKNOWN:
                                statusString.append("Unknown");
                                break;
                            default:
                                statusString.append(state + " (undefined)");
                                break;
                        }
                    }

                    PrinterStateReasons reasons = (PrinterStateReasons) attributes.get(PrinterStateReasons.class);
                    if (reasons != null) {
                        statusString.append(": ");
                        statusString.append(reasons.getReasons());
                    }

//                statusView.setText(statusString.toString());

                }
            });

            /**
             * Obtain error level and judge whether display or hide or update warning screen
             */
            PrinterState state = (PrinterState) event.getAttributes().get(PrinterState.class);
            PrinterStateReasons stateReasons = (PrinterStateReasons) event.getAttributes().get(PrinterStateReasons.class);
            PrinterOccuredErrorLevel errorLevel = (PrinterOccuredErrorLevel) event.getAttributes().get(PrinterOccuredErrorLevel.class);

            if (PrinterOccuredErrorLevel.ERROR.equals(errorLevel)
                    || PrinterOccuredErrorLevel.FATAL_ERROR.equals(errorLevel)) {

                String stateString = makeAlertStateString(state);
                String reasonString = makeAlertStateReasonString(stateReasons);

                if (mLastErrorLevel == null) {
                    // Normal -> Error
                    if (isForegroundApp(RCTRicohPrinter.this.getPackageName())) {
                        mApplication.displayAlertDialog(ALERT_DIALOG_APP_TYPE_PRINTER, stateString, reasonString);
                        mAlertDialogDisplayed = true;
                    }
                } else {
                    // Error -> Error
                    if (mAlertDialogDisplayed) {
                        mApplication.updateAlertDialog(ALERT_DIALOG_APP_TYPE_PRINTER, stateString, reasonString);
                    }
                }
                mLastErrorLevel = errorLevel;

            } else {
                if (mLastErrorLevel != null) {
                    // Error -> Normal
                    if (mAlertDialogDisplayed) {
                        String activityName = getTopActivityClassName(RCTRicohPrinter.this.getPackageName());
                        if (activityName == null) {
                            activityName = MainActivity.class.getName();
                        }
                        mApplication.hideAlertDialog(ALERT_DIALOG_APP_TYPE_PRINTER, activityName);
                        mAlertDialogDisplayed = false;
                    }
                }
                mLastErrorLevel = null;
            }

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
        ActivityManager am = (ActivityManager) RCTRicohPrinter.this.getCurrentActivity().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : list) {
            if (packageName.equals(info.processName)) {
                result = (info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND);
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
        ActivityManager am = (ActivityManager) RCTRicohPrinter.this.getCurrentActivity().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(30);
        for (ActivityManager.RunningTaskInfo info : list) {
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
            PrintServiceAttributeSet attributes = mPrintService.getAttributes();
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

                mApplication.displayAlertDialog(PrintServiceAttributeListenerImpl.ALERT_DIALOG_APP_TYPE_PRINTER, stateString, reasonString);
                mServiceAttributeListener.setAlertDialogDisplayed(true);
                mServiceAttributeListener.setLastErrorLevel(errorLevel);
            }
            return null;
        }

    }
}
