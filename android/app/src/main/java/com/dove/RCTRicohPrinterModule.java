package com.dove;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.dove.sample.app.print.Const;
import com.dove.sample.app.print.activity.DialogUtil;
import com.dove.sample.app.print.activity.PrintColorUtil;
import com.dove.sample.app.print.activity.StapleUtil;
import com.dove.sample.app.print.application.PrintSettingDataHolder;
import com.dove.sample.app.print.application.PrintSettingSupportedHolder;
import com.dove.sample.app.print.application.SystemStateMonitor;
import com.dove.sample.function.common.SmartSDKApplication;
import com.dove.sample.function.common.impl.AsyncConnectState;
import com.dove.sample.function.print.PrintFile;
import com.dove.sample.function.print.PrintService;
import com.dove.sample.function.print.attribute.PrintRequestAttribute;
import com.dove.sample.function.print.attribute.PrintServiceAttributeSet;
import com.dove.sample.function.print.attribute.standard.Copies;
import com.dove.sample.function.print.attribute.standard.PrintColor;
import com.dove.sample.function.print.attribute.standard.PrinterOccuredErrorLevel;
import com.dove.sample.function.print.attribute.standard.PrinterState;
import com.dove.sample.function.print.attribute.standard.PrinterStateReasons;
import com.dove.sample.function.print.attribute.standard.Staple;
import com.dove.sample.function.print.event.PrintServiceAttributeEvent;
import com.dove.sample.function.print.event.PrintServiceAttributeListener;
import com.dove.sample.wrapper.log.Logger;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.annotations.ReactProp;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;


/**
 * Created by Administrator on 2017/12/8.
 */

public class RCTRicohPrinterModule extends ReactContextBaseJavaModule {
    private Context mContext;

    public RCTRicohPrinterModule(ReactApplicationContext reactContext) {
        super(reactContext);
//        if (reactContext != null)
//            mContext = reactContext;
//        else
//            mContext = this.getReactApplicationContext();

//        if (mContext == null)
            mContext = this.getCurrentActivity();
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

    @ReactMethod
    public void openActivity(Promise promise) throws JSONException {

        try {
            Activity currentActivity = getCurrentActivity();
            if (null != currentActivity) {
//                Intent intent = new Intent(currentActivity, SimplePrintMainActivity.class); //new Intent(currentActivity, TopActivity.class);
//                currentActivity.startActivity(intent);
            }

            promise.resolve("Start activity success!!");
        } catch (android.content.ActivityNotFoundException e) {
            promise.reject("Start activity error!!" + e.getMessage());
            throw e;
        }
    }

    @Override
    public void initialize() {
        Logger.setRecorder(mLogRecoder);

        if (mContext == null)
            mContext = this.getCurrentActivity();

        mApplication = new PrintApplicationWrapper((SmartSDKApplication) this.getCurrentActivity().getApplication());
        SimplePrintStateMachine stateMachine = mApplication.getStateMachine();
        stateMachine.setContext(mContext);
    }

    @ReactMethod
    public void onCreate(Promise promise) throws JSONException {

        try {
            registerReceiver();
            initializeListener();
            promise.resolve("Create module success!!");
        } catch (android.content.ActivityNotFoundException e) {
            promise.reject("Create module error!!" + e.getMessage());
            throw e;
        }
    }

    @ReactMethod
    public void onResume(Promise promise) throws JSONException {

        try {
            resume();
            promise.resolve("Resume module success!!");
        } catch (android.content.ActivityNotFoundException e) {
            promise.reject("Resume module error!!" + e.getMessage());
            throw e;
        }
    }

    @ReactMethod
    public void onPause(Promise promise) throws JSONException {

        try {
            pause();
            promise.resolve("Pause module success!!");
        } catch (android.content.ActivityNotFoundException e) {
            promise.reject("Pause module error!!" + e.getMessage());
            throw e;
        }
    }

    @ReactMethod
    public void onDestroy(Promise promise) throws JSONException {

        try {
            destroy();
            promise.resolve("Destroy module success!!");
        } catch (android.content.ActivityNotFoundException e) {
            promise.reject("Destroy module error!!" + e.getMessage());
            throw e;
        }
    }

    @ReactMethod
    public void onStartPrint(Promise promise) throws JSONException {

        try {
            startPrint();
            promise.resolve("Start print success!!");
        } catch (android.content.ActivityNotFoundException e) {
            promise.reject("Start print error!!" + e.getMessage());
            throw e;
        }
    }

    @ReactMethod
    public void setPrintFilePath(String path, Promise promise) throws JSONException {

        try {
            mHolder.setSelectedPrintAssetFileName(path);

            promise.resolve("Set print file path success!!");
        } catch (android.content.ActivityNotFoundException e) {
            promise.reject("Set print file path error!!" + e.getMessage());
            throw e;
        }
    }

    @ReactMethod
    public void setPrintColor(String printColor, Promise promise) throws JSONException {

        try {
            mHolder.setSelectedPrintColorValue(PrintColor.fromString(printColor));

            promise.resolve("Set print color success!!");
        } catch (android.content.ActivityNotFoundException e) {
            promise.reject("Set print color error!!" + e.getMessage());
            throw e;
        }
    }

    @ReactMethod
    public void setPrintCopies(String copies, Promise promise) throws JSONException {

        try {
            mHolder.setSelectedCopiesValue(new Copies(Integer.parseInt(copies)));

            promise.resolve("Set print copies success!!");
        } catch (android.content.ActivityNotFoundException e) {
            promise.reject("Set print copies error!!" + e.getMessage());
            throw e;
        }
    }

//    @ReactProp(name="printFilePath")
//    public void setPrintFilePath(String path){
//        mHolder.setSelectedPrintAssetFileName(path);
//    }

    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           @Nullable WritableMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    /************************************************************************************************
     *
     */
    private PrintApplicationWrapper mApplication;
    private BroadcastReceiver mReceiver;
    private boolean mRequestedSystemResetLock = false;
    private boolean mIsForeground = false;
    private PrintServiceAttributeListenerImpl mServiceAttributeListener;
    private PrintSettingDataHolder mHolder = new PrintSettingDataHolder();
    private final static String PREFIX = "javaModule:PrintModule:";
    private AlertDialogDisplayTask mAlertDialogDisplayTask = null;
    private ReturnRequestFromEnergySavingTask mReturnRequestFromEnergySavingTask = null;

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(SimplePrintStateMachine.INTENT_ACTION_LOCK_SYSTEM_RESET);     //システムリセット抑止状態を設定するための内部インテント
        filter.addAction(SimplePrintStateMachine.INTENT_ACTION_UNLOCK_SYSTEM_RESET);   //システムリセット抑止状態を解除するための内部インテント

        this.mReceiver = new BroadcastReceiver() {
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

        mContext.registerReceiver(this.mReceiver, filter);
    }

    private void processSystemResetLock() {
        if (mIsForeground && mRequestedSystemResetLock) {
            mApplication.getSSDKApplication().lockSystemReset();
        }
    }

    private void processSystemResetUnlock() {
        mApplication.getSSDKApplication().unlockSystemReset();
    }

    private void initializeListener() {
        //(1)
        mServiceAttributeListener = new PrintServiceAttributeListenerImpl(this, new Handler());

        //(2)
        new PrintServiceInitTask().execute(mServiceAttributeListener);

        //(3)
        mApplication.getStateMachine().procPrintEvent(
                SimplePrintStateMachine.PrintEvent.CHANGE_APP_ACTIVITY_INITIAL);
    }

    private void initSetting() {
//        mHolder.setSelectedPrintAssetFileName(mContext.getString(R.string.assets_file_sample_01));

//        mHolder.setSelectedCopiesValue(new Copies(1));
//        mHolder.setSelectedStaple(Staple.TOP_LEFT);
//        mHolder.setSelectedPrintColorValue(PrintColor.MONOCHROME);
        updateSettings();
//        updateSettingButtons();
    }

    private void startPrint() {
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
            Toast.makeText(mContext, R.string.error_settings_not_found, Toast.LENGTH_LONG).show();

            //Close dialog, reset flag to false
            mApplication.setmIsWinShowing(false);

            return;
        }
        mApplication.startPrint(mHolder);
    }

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
                mApplication.getSettingSupportedDataHolders();
        categories = supportedHolderMap.get(mHolder.getSelectedPDL()).getSelectableCategories();

        if (null == categories) {
            return false;
        }

        //(3)
        if (categories.contains(Copies.class)) {
            mHolder.setSelectedCopiesValue(
                    new Copies(Integer.parseInt(mContext.getString(R.string.default_copies))));
        } else {
            mHolder.setSelectedCopiesValue(null);
        }
        if (categories.contains(Staple.class)) {
            List<Staple> stapleList = StapleUtil.getSelectableStapleList(mContext);

            if (stapleList != null) {
                mHolder.setSelectedStaple(stapleList.get(0));
            } else {
                mHolder.setSelectedStaple(null);
            }
        } else {
            mHolder.setSelectedStaple(null);
        }
        if (categories.contains(PrintColor.class)) {
            List<PrintColor> printColorList = PrintColorUtil.getSelectablePrintColorList(mContext);

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

    protected void resume() {
        Log.d(Const.TAG, PREFIX + "onResume");

        // スキャンステートマシンからシステムリセット抑止を要求されている場合、SDKServiceへロックを設定します
        mIsForeground = true;
        processSystemResetLock();

        startAlertDialogDisplayTask();
        startReturnRequestFromEnergySavingTask();
    }

    protected void pause() {
        Log.d(Const.TAG, PREFIX + "onPause");

        // システムリセットロックを解除します
        mIsForeground = false;
        processSystemResetUnlock();

        stopAlertDialogDisplayTask();

    }

    protected void destroy() {
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

        mContext.unregisterReceiver(mReceiver);
        mReceiver = null;

    }

    private void startAlertDialogDisplayTask() {
        if (mAlertDialogDisplayTask != null) {
            mAlertDialogDisplayTask.cancel(false);
        }
        mAlertDialogDisplayTask = new AlertDialogDisplayTask();
        mAlertDialogDisplayTask.execute();
    }

    private void stopAlertDialogDisplayTask() {
        if (mAlertDialogDisplayTask != null) {
            mAlertDialogDisplayTask.cancel(false);
            mAlertDialogDisplayTask = null;
        }
    }

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

    public PrintSettingDataHolder getSettingHolder() {
        return mHolder;
    }

    public void setSettingHolder(PrintSettingDataHolder holder) {
        this.mHolder = holder;
    }

    public PrintFile.PDL currentPDL(String fileName) {

        if (fileName == null) {
            return null;
        }

        //(1)
        PrintFile.PDL currentPDL = null;
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);

        //(2)
        Set<PrintFile.PDL> pdlList = mApplication.getSettingSupportedDataHolders().keySet();

        //(3)
        if (ext.equals(mContext.getString(R.string.file_extension_PDF))
                && pdlList.contains(PrintFile.PDL.PDF)) {
            currentPDL = PrintFile.PDL.PDF;
        } else if (ext.equals(mContext.getString(R.string.file_extension_PRN))
                && pdlList.contains(PrintFile.PDL.RPCS)) {
            currentPDL = PrintFile.PDL.RPCS;
        } else if (ext.equals(mContext.getString(R.string.file_extension_XPS))
                && pdlList.contains(PrintFile.PDL.XPS)) {
            currentPDL = PrintFile.PDL.XPS;
        } else if (ext.equals(mContext.getString(R.string.file_extension_TIF))
                && pdlList.contains(PrintFile.PDL.TIFF)) {
            currentPDL = PrintFile.PDL.TIFF;
        }
        return currentPDL;
    }

    public Application getApplication() {
        return this.getCurrentActivity().getApplication();
    }

    public String makeAlertStateString(PrinterState state) {
        String stateString = "";
        if (state != null) {
            stateString = state.toString();
        }
        return stateString;
    }

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

    public boolean isForegroundApp(String packageName) {
        boolean result = false;
        ActivityManager am = (ActivityManager) this.getCurrentActivity().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : list) {
            if (packageName.equals(info.processName)) {
                result = (info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND);
                break;
            }
        }
        return result;
    }

    public String getTopActivityClassName(String packageName) {
        ActivityManager am = (ActivityManager) this.getCurrentActivity().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(30);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (packageName.equals(info.topActivity.getPackageName())) {
                return info.topActivity.getClassName();
            }
        }
        return null;
    }

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

                mApplication.getSSDKApplication().displayAlertDialog(PrintServiceAttributeListenerImpl.ALERT_DIALOG_APP_TYPE_PRINTER, stateString, reasonString);
                mServiceAttributeListener.setAlertDialogDisplayed(true);
                mServiceAttributeListener.setLastErrorLevel(errorLevel);
            }
            return null;
        }

    }

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

                List<PrintFile.PDL> supportedPDL = printService.getSupportedPDL();
                if (supportedPDL == null) return null;

                for (PrintFile.PDL pdl : supportedPDL) {
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

    class PrintServiceAttributeListenerImpl implements PrintServiceAttributeListener {

        /**
         * メインアクテビティへの参照
         * Reference to PrintMainActivity
         */
        RCTRicohPrinterModule mActivity;

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

        public PrintServiceAttributeListenerImpl(RCTRicohPrinterModule activity, Handler handler) {
            mActivity = activity;
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
            SmartSDKApplication mApplication = (SmartSDKApplication) mActivity.getApplication();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    PrintServiceAttributeSet attributes = mEvent.getAttributes();
//                    TextView statusView = (TextView)mActivity.findViewById(R.id.txt_state);
                    StringBuilder statusString = new StringBuilder();

                    PrinterState state = (PrinterState) attributes.get(PrinterState.class);
                    if (state != null) {
                        switch (state) {
                            case IDLE:
                                statusString.append(mActivity.getReactApplicationContext().getResources().getString(R.string.printer_status_idle));
                                break;
                            case MAINTENANCE:
                                statusString.append(mActivity.getReactApplicationContext().getResources().getString(R.string.printer_status_maintenance));
                                break;
                            case PROCESSING:
                                statusString.append(mActivity.getReactApplicationContext().getResources().getString(R.string.printer_status_processing));
                                break;
                            case STOPPED:
                                statusString.append(mActivity.getReactApplicationContext().getResources().getString(R.string.printer_status_stopped));
                                break;
                            case UNKNOWN:
                                statusString.append(mActivity.getReactApplicationContext().getResources().getString(R.string.printer_status_unknown));
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

//                    statusView.setText(statusString.toString());

                    ReactApplicationContext reactContext = getReactApplicationContext();
                    WritableMap params = Arguments.createMap();
                    params.putString("status", statusString.toString());
                    sendEvent(reactContext, "PrintServiceAttributeStatusUpdated", params);

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

                String stateString = mActivity.makeAlertStateString(state);
                String reasonString = mActivity.makeAlertStateReasonString(stateReasons);

                if (mLastErrorLevel == null) {
                    // Normal -> Error
                    if (mActivity.isForegroundApp(mActivity.getCurrentActivity().getPackageName())) {
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
                        String activityName = mActivity.getTopActivityClassName(mActivity.getCurrentActivity().getPackageName());
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

    private final Logger.LogRecorder mLogRecoder = new Logger.LogRecorder() {
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
