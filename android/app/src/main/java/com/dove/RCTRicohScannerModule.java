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
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.dove.sample.app.scan.Const;
import com.dove.sample.app.scan.activity.DialogUtil;
import com.dove.sample.app.scan.activity.PreviewActivity;
import com.dove.sample.app.scan.application.DestinationSettingDataHolder;
import com.dove.sample.app.scan.application.ScanSettingDataHolder;
//import com.dove.sample.app.scan.application.ScanStateMachine;
import com.dove.sample.app.scan.application.StorageSettingDataHolder;
import com.dove.sample.app.scan.application.SystemStateMonitor;
import com.dove.sample.function.common.SmartSDKApplication;
import com.dove.sample.function.common.impl.AsyncConnectState;
import com.dove.sample.function.scan.ScanImage;
import com.dove.sample.function.scan.ScanJob;
import com.dove.sample.function.scan.ScanService;
import com.dove.sample.function.scan.attribute.ScanJobAttributeSet;
import com.dove.sample.function.scan.attribute.ScanServiceAttributeSet;
import com.dove.sample.function.scan.attribute.standard.JobMode;
import com.dove.sample.function.scan.attribute.standard.OccuredErrorLevel;
import com.dove.sample.function.scan.attribute.standard.ScanJobScanningInfo;
import com.dove.sample.function.scan.attribute.standard.ScanJobSendingInfo;
import com.dove.sample.function.scan.attribute.standard.ScanJobState;
import com.dove.sample.function.scan.attribute.standard.ScanJobStateReason;
import com.dove.sample.function.scan.attribute.standard.ScanJobStateReasons;
import com.dove.sample.function.scan.attribute.standard.ScannerState;
import com.dove.sample.function.scan.attribute.standard.ScannerStateReason;
import com.dove.sample.function.scan.attribute.standard.ScannerStateReasons;
import com.dove.sample.function.scan.event.ScanJobAttributeEvent;
import com.dove.sample.function.scan.event.ScanJobAttributeListener;
import com.dove.sample.function.scan.event.ScanJobEvent;
import com.dove.sample.function.scan.event.ScanJobListener;
import com.dove.sample.function.scan.event.ScanServiceAttributeEvent;
import com.dove.sample.function.scan.event.ScanServiceAttributeListener;
import com.dove.sample.wrapper.common.BinaryResponseBody;
import com.dove.sample.wrapper.common.Response;
import com.dove.sample.wrapper.log.Logger;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

public class RCTRicohScannerModule extends SmartApplication implements IScanApplication {

//    private static final String DURATION_SHORT_KEY = "SHORT";
//    private static final String DURATION_LONG_KEY = "LONG";

    private Context mContext;
    private Activity mActivity;
//    private SmartSDKApplication mApplication;

    public RCTRicohScannerModule(ReactApplicationContext reactContext, Application app) {
        super(reactContext);
        mContext = reactContext;
        if (mContext == null) this.getCurrentActivity();
        mActivity = this.getCurrentActivity();
        mApplication = (SmartSDKApplication) app;
    }

    @Override
    public String getName() {
        return "RCTRicohScanner";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
//        constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
//        constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
        return constants;
    }

    private void show(String message, int duration) {
        Toast.makeText(getReactApplicationContext(), message, duration).show();
    }

    @Override
    protected void sendEvent(ReactContext reactContext,
                             String eventName,
                             @Nullable WritableMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    /************************** React-Native Methods ********************************************/

    @ReactMethod
    public void init(Promise promise) throws Exception {
        try {

            Logger.setRecorder(mLogRecoder);
            Log.d(Const.TAG, PREFIX + "init");

            init();

            //(1)
//            mApplication = (ScanSampleApplication) getApplication();
            mScanServiceAttrListener = new ScanServiceAttributeListenerImpl(new Handler());
//            mScanSettingDataHolder = getScanSettingDataHolder();
//            mDestSettingDataHolder = getDestinationSettingDataHolder();
//            mStorageSettingDataHolder = getStorageSettingDataHolder();
//            mStateMachine = getStateMachine();
            mStateMachine.registActivity(this.getCurrentActivity());

            //(2)
            IntentFilter filter = new IntentFilter();
            filter.addAction(DialogUtil.INTENT_ACTION_SUB_ACTIVITY_RESUMED);
            filter.addAction(SimpleScanStateMachine.INTENT_ACTION_LOCK_SYSTEM_RESET);     //システムリセット抑止状態を設定するための内部インテント
            filter.addAction(SimpleScanStateMachine.INTENT_ACTION_UNLOCK_SYSTEM_RESET);   //システムリセット抑止状態を解除するための内部インテント

            mReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (DialogUtil.INTENT_ACTION_SUB_ACTIVITY_RESUMED.equals(action)) {
                        startAlertDialogDisplayTask();
                    } else if (SimpleScanStateMachine.INTENT_ACTION_LOCK_SYSTEM_RESET.equals(action)) {
                        mRequestedSystemResetLock = true;
                        processSystemResetLock();
                    } else if (SimpleScanStateMachine.INTENT_ACTION_UNLOCK_SYSTEM_RESET.equals(action)) {
                        mRequestedSystemResetLock = false;
                        processSystemResetUnlock();
                    }
                }
            };
            mContext.registerReceiver(mReceiver, filter);

            //(3)

            if (mScanServiceInitTask != null) {
                mScanServiceInitTask.cancel(false);
            }
            mScanServiceInitTask = new ScanServiceInitTask();
            mScanServiceInitTask.execute();

            // send event
            mStateMachine.procScanEvent(SimpleScanStateMachine.ScanEvent.ACTIVITY_CREATED);

            // set application state to Normal
            mApplication.setAppState(MainActivity.class.getName(), SmartSDKApplication.APP_STATE_NORMAL, SmartSDKApplication.APP_STATE_NORMAL_MSG, SmartSDKApplication.APP_TYPE_SCANNER);

            mScanSettingDataHolder.setSelectedColor(R.string.txid_scan_b_top_auto_color_select); // white & black
            mScanSettingDataHolder.setSelectedFileSetting(R.string.txid_scan_b_top_file_spdf); // pdf (multi-page)
            mScanSettingDataHolder.setSelectedPreview(R.string.txid_scan_b_other_preview_off); // off
            mScanSettingDataHolder.setSelectedSide(R.string.txid_scan_b_top_one_sided); // one sided
            mScanSettingDataHolder.setSelectedJobMode(R.string.txid_scan_b_jobmode_scan_and_store_temporary); //  SCAN_AND_STORE_TEMPORARY

            Log.i(Const.TAG, PREFIX + "color: " + mScanSettingDataHolder.getSelectedColorValue());
            Log.i(Const.TAG, PREFIX + "file setting: " + mScanSettingDataHolder.getSelectedFileFormatValue());
            Log.i(Const.TAG, PREFIX + "preview: " + mScanSettingDataHolder.getSelectedPreviewValue());
            Log.i(Const.TAG, PREFIX + "side: " + mScanSettingDataHolder.getSelectedSideValue());
            Log.i(Const.TAG, PREFIX + "job mode: " + mScanSettingDataHolder.getSelectedJobModeValue());

            final List<Integer> list = mScanSettingDataHolder.getJobModeLabelList();
            final String[] items = new String[list.size()];
            for (int i = 0; i < items.length; ++i) {
                Log.i(Const.TAG, PREFIX + "supported job mode #" + i + ": " + mContext.getString(list.get(i)));
            }


            promise.resolve("Initialization success!!");
        } catch (Exception e) {
            Log.e(Const.TAG, PREFIX, e);
            promise.reject("Initialization error:" + e.getMessage());
            throw e;
//            promise.reject("Initialization error!!");
        }
    }


    @ReactMethod
    public void restore(Promise promise) {
        try {
            ScanService scanService = ScanService.getService();
            try {
                scanService.removeScanServiceAttributeListener(mScanServiceAttrListener);
            } catch (IllegalStateException e) {
                /* the listener is not registered. */
            }

            if (mScanServiceInitTask != null) {
                mScanServiceInitTask.cancel(false);
                mScanServiceInitTask = null;
            }
            //(4)
            //If a state change listener is registered to the current scan job, the listener is removed.
            if (mScanJob != null) {
                if (mScanJobListener != null) {
                    mScanJob.removeScanJobListener(mScanJobListener);
                }
                if (mScanJobAttrListener != null) {
                    mScanJob.removeScanJobAttributeListener(mScanJobAttrListener);
                }
            }

            mScanServiceInitTask = new ScanServiceInitTask();
            mScanServiceInitTask.execute();

            mScanJob = new ScanJob();
            mScanJobListener = new ScanJobListenerImpl();
            mScanJobAttrListener = new ScanJobAttributeListenerImpl();

            //Registers a new listener to the new scan job.
            mScanJob.addScanJobListener(mScanJobListener);
            mScanJob.addScanJobAttributeListener(mScanJobAttrListener);
            promise.resolve("Restore success!!");
        } catch (Exception e) {
            Log.e(Const.TAG, PREFIX, e);
            promise.reject("Restore error:" + e.getMessage());
//            promise.reject("Initialization error!!");
        }
    }

    @ReactMethod
    public void start(Promise promise) {
// start scan
        boolean result = false;
        String message = null;
        try {

//            ScanRequestAttributeSet requestAttributes;
//            requestAttributes = new HashScanRequestAttributeSet();
//            requestAttributes.add(AutoCorrectJobSetting.AUTO_CORRECT_ON);
//
//
//            JobMode jobMode = JobMode.SCAN_AND_STORE_TEMPORARY;
//            requestAttributes.add(jobMode);
//
//            FileSetting fileSetting = new FileSetting();
//            fileSetting.setFileFormat(FileSetting.FileFormat.PDF);
//            fileSetting.setMultiPageFormat(true);
//            requestAttributes.add(fileSetting);
//
//
//            //Check dialog showing or not
//            if (ismIsWinShowing()) {
//                return;
//            }
//            //If no dialog displays, set flag to true
//            setmIsWinShowing(true);
//
//            mStateMachine.procScanEvent(SimpleScanStateMachine.ScanEvent.REQUEST_JOB_START);
//
//            try {
//                Log.i(Const.TAG, PREFIX + "scan requesting...");
//                mStateMachine.procScanEvent(SimpleScanStateMachine.ScanEvent.REQUEST_JOB_START);
//                Log.d(Const.TAG, PREFIX + "scan requested. result=" + result);
//            } catch (Exception e) {
//                message = "job start failed. " + e.getMessage();
//
//                Log.w(Const.TAG, PREFIX + message);
//            }

            start();

            promise.resolve("Start scan job success!!");
        } catch (Exception e) {
            Log.e(Const.TAG, PREFIX, e);
            promise.reject("Start scan job error:" + e.getMessage());
//            promise.reject("Start scan job error!!");
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
    @ReactMethod
    public void onResume(Promise promise) {
        Log.d(Const.TAG, PREFIX + "onResume");
        try {

            if (mMultipleRunning) {
                return;
            }

            // スキャンステートマシンからシステムリセット抑止を要求されている場合、SDKServiceへロックを設定します
            mIsForeground = true;
            processSystemResetLock();

            startAlertDialogDisplayTask();
            startReturnRequestFromEnergySavingTask();
            promise.resolve("Resume success!!");
        } catch (Exception e) {
            Log.e(Const.TAG, PREFIX, e);
            promise.reject("Resume error:" + e.getMessage());
//            promise.reject("Start scan job error!!");
        }
    }

    /**
     * アクティビティの停止時に呼び出されます。
     * システム警告画面表示タスクが実行中であれば、キャンセルします。
     * Called when the activity is stopped.
     * If the system warning screen display task is in process, the task is cancelled.
     */
    @ReactMethod
    public void onPause(Promise promise) {
        Log.d(Const.TAG, PREFIX + "onPause");
        try {

            // システムリセットロックを解除します
            mIsForeground = false;
            processSystemResetUnlock();

            stopAlertDialogDisplayTask();
            promise.resolve("Pause success!!");
        } catch (Exception e) {
            Log.e(Const.TAG, PREFIX, e);
            promise.reject("Pause error:" + e.getMessage());
//            promise.reject("Start scan job error!!");
        }
    }

    /**
     * アクティビティが破棄される際に呼び出されます。
     * [処理内容]
     * (1)メインアクティビティ終了イベントをステートマシンに送る
     * 読取中であれば、読取がキャンセルされます。
     * (2)サービスからイベントリスナーとブロードキャストレシーバーを除去する
     * (3)非同期タスクが実行中だった場合、キャンセルする
     * (4)アプリケーションの保持データを初期化する
     * (5)参照を破棄する
     * <p>
     * Called when the activity is destroyed.
     * [Processes]
     * (1) Send MainActivity destoyed event to the state machine.
     * If scanning is in process, scanning is cancelled.
     * (2) Removes the event listener and the broadcast receiver from the service.
     * (3) If asynchronous task is in process, the task is cancelled.
     * (4) Initializes the data saved to the application.
     * (5) Discards references
     */
    @ReactMethod
    public void onDestroy(Promise promise) {
        Log.d(Const.TAG, PREFIX + "onDestroy");

        try {
            // if MainActivity another instance is already running, then exit without doing anything
            if (mMultipleRunning) {
                return;
            }

            //アクティビティ終了時は、ロック設定状態に関わらずシステムリセットのロックを解除します
            processSystemResetUnlock();

            //(1)
            mStateMachine.procScanEvent(SimpleScanStateMachine.ScanEvent.ACTIVITY_DESTROYED);

            //(2)
            ScanService scanService = getScanService();
            try {
                scanService.removeScanServiceAttributeListener(mScanServiceAttrListener);
            } catch (IllegalStateException e) {
                /* the listener is not registered. */
            }
            unregisterReceiver(mReceiver);

            //(3)
            stopAlertDialogDisplayTask();
            stopReturnRequestFromEnergySavingTask();
            if (mScanServiceInitTask != null) {
                mScanServiceInitTask.cancel(false);
                mScanServiceInitTask = null;
            }

            // set application state to Normal
            setAppState(MainActivity.class.getName(), SmartSDKApplication.APP_STATE_NORMAL, SmartSDKApplication.APP_STATE_NORMAL_MSG, SmartSDKApplication.APP_TYPE_SCANNER);

            //(4)
            init();

            //(5)
//            mApplication = null;
            mReceiver = null;
            mScanSettingDataHolder = null;
            mDestSettingDataHolder = null;
            mStorageSettingDataHolder = null;
            mStateMachine = null;
            mScanServiceAttrListener = null;

            promise.resolve("Destroy success!!");
        } catch (Exception e) {
            Log.e(Const.TAG, PREFIX, e);
            promise.reject("Destroy error:" + e.getMessage());
//            promise.reject("Start scan job error!!");
        }
    }

    @ReactMethod
    public void setSelectedJobModeLabel(Promise promise, String label) {
        try {
            final List<Integer> list = mScanSettingDataHolder.getJobModeLabelList();
            final String[] items = new String[list.size()];
            for (int i = 0; i < items.length; ++i) {
                items[i] = mContext.getResources().getString(list.get(i));
            }
            Boolean found = false;
            for (int i = 0; i < items.length; ++i) {
                if (label == items[i]) {
                    mScanSettingDataHolder.setSelectedJobMode(list.get(i));
                    found = true;
                    break;
                }
            }
            if (!found) {
                promise.reject("Unsupported mode: " + label);
                return;
            }
            promise.resolve("setSelectedJobModeLabel success!!");
        } catch (Exception e) {
            Log.e(Const.TAG, PREFIX, e);
            promise.reject("setSelectedJobModeLabel error:" + e.getMessage());
//            promise.reject("Start scan job error!!");
        }
    }

    @ReactMethod
    public void setSelectedFileSettingLabel(Promise promise, String label) {
        try {
            final List<Integer> list = mScanSettingDataHolder.getFileSettingLabelList();
            final String[] items = new String[list.size()];
            for (int i = 0; i < items.length; ++i) {
                items[i] = mContext.getResources().getString(list.get(i));
                // Log.d(Const.TAG, PREFIX+"supported file setting: "+items[i]);
            }
            Boolean found = false;
            for (int i = 0; i < items.length; ++i) {
                if (label == items[i]) {
                    mScanSettingDataHolder.setSelectedFileSetting(list.get(i));
                    found = true;
                    break;
                }
            }
            if (!found) {
                promise.reject("Unsupported file setting: " + label);
                return;
            }
            promise.resolve("setSelectedFileSettingLabel success!!");
        } catch (Exception e) {
            Log.e(Const.TAG, PREFIX, e);
            promise.reject("setSelectedFileSettingLabel error:" + e.getMessage());
        }
    }

    @ReactMethod
    public void setSelectedSideLabel(Promise promise, String label) {
        try {
            final List<Integer> list = mScanSettingDataHolder.getSideLabelList();
            final String[] items = new String[list.size()];
            for (int i = 0; i < items.length; ++i) {
                items[i] = mContext.getResources().getString(list.get(i));
                Log.d(Const.TAG, PREFIX+"supported side: "+items[i]);
            }
            Boolean found = false;
            for (int i = 0; i < items.length; ++i) {
                if (label == items[i]) {
                    mScanSettingDataHolder.setSelectedSide(list.get(i));
                    found = true;
                    break;
                }
            }
            if (!found) {
                promise.reject("Unsupported side: " + label);
                return;
            }
            promise.resolve("setSelectedSideLabel success!!");
        } catch (Exception e) {
            Log.e(Const.TAG, PREFIX, e);
            promise.reject("setSelectedSideLabel error:" + e.getMessage());
        }
    }

    @ReactMethod
    public void setSelectedColorLabel(Promise promise, String label) {
        try {
            final List<Integer> list = mScanSettingDataHolder.getColorLabelList();
            final String[] items = new String[list.size()];
            for (int i = 0; i < items.length; ++i) {
                items[i] = mContext.getResources().getString(list.get(i));
                Log.d(Const.TAG, PREFIX+"supported color: "+items[i]);
            }
            Boolean found = false;
            for (int i = 0; i < items.length; ++i) {
                if (label == items[i]) {
                    mScanSettingDataHolder.setSelectedColor(list.get(i));
                    found = true;
                    break;
                }
            }
            if (!found) {
                promise.reject("Unsupported color: " + label);
                return;
            }
            promise.resolve("setSelectedColorLabel success!!");
        } catch (Exception e) {
            Log.e(Const.TAG, PREFIX, e);
            promise.reject("setSelectedColorLabel error:" + e.getMessage());
        }
    }

    @ReactMethod
    public void setSelectedPreviewLabel(Promise promise, String label) {
        try {
            final List<Integer> list = mScanSettingDataHolder.getPreviewLabelList();
            final String[] items = new String[list.size()];
            for (int i = 0; i < items.length; ++i) {
                items[i] = mContext.getResources().getString(list.get(i));
                Log.d(Const.TAG, PREFIX+"supported preview opt: "+items[i]);
            }
            Boolean found = false;
            for (int i = 0; i < items.length; ++i) {
                if (label == items[i]) {
                    mScanSettingDataHolder.setSelectedPreview(list.get(i));
                    found = true;
                    break;
                }
            }
            if (!found) {
                promise.reject("Unsupported preview option: " + label);
                return;
            }
            promise.resolve("setSelectedPreviewLabel success!!");
        } catch (Exception e) {
            Log.e(Const.TAG, PREFIX, e);
            promise.reject("setSelectedPreviewLabel error:" + e.getMessage());
        }
    }

    private void start() {

        //Check dialog showing or not
        if (ismIsWinShowing()) {
            return;
        }
        //If no dialog displays, set flag to true
        setmIsWinShowing(true);

        mStateMachine.procScanEvent(SimpleScanStateMachine.ScanEvent.REQUEST_JOB_START);
    }


    /************************** IScanApplication implementation *********************************/


    /********************************** scan job ******************************************/
    private DestinationSettingDataHolder mDestinationSettingDataHolder;
    private ScanSettingDataHolder mScanSettingDataHolder;
    private StorageSettingDataHolder mStorageSettingDataHolder;
    private ScanService mScanService;
    private ScanJob mScanJob;
    private ScanJobListener mScanJobListener;
    private ScanJobAttributeListener mScanJobAttrListener;
    private SimpleScanStateMachine mStateMachine;
    private SystemStateMonitor mSystemStateMonitor;
    protected int scannedPages;
    private boolean mIsWinShowing = false;
    protected int timeOfWaitingNextOriginal = 0;

    public boolean ismIsWinShowing() {
        return mIsWinShowing;
    }

    public void setmIsWinShowing(boolean mIsWinShowing) {
        this.mIsWinShowing = mIsWinShowing;
    }

    public void init() {
        mDestinationSettingDataHolder = new DestinationSettingDataHolder();
        mScanSettingDataHolder = new ScanSettingDataHolder();
        mStorageSettingDataHolder = new StorageSettingDataHolder();
        mStateMachine = new SimpleScanStateMachine(this, new Handler(Looper.getMainLooper()));
        mScanService = ScanService.getService();
        initJobSetting();
    }

    public void initJobSetting() {
        //If a state change listener is registered to the current scan job, the listener is removed.
        if (mScanJob != null) {
            if (mScanJobListener != null) {
                mScanJob.removeScanJobListener(mScanJobListener);
            }
            if (mScanJobAttrListener != null) {
                mScanJob.removeScanJobAttributeListener(mScanJobAttrListener);
            }
        }

        mScanJob = new ScanJob();
        mScanJobListener = new ScanJobListenerImpl();
        mScanJobAttrListener = new ScanJobAttributeListenerImpl();

        //Registers a new listener to the new scan job.
        mScanJob.addScanJobListener(mScanJobListener);
        mScanJob.addScanJobAttributeListener(mScanJobAttrListener);



        ReactApplicationContext reactContext = getReactApplicationContext();
        WritableMap params = Arguments.createMap();
        params.putString("label", "READY");
        sendEvent(reactContext, "onScanJobStateUpdated", params);
    }

    class ScanJobAttributeListenerImpl implements ScanJobAttributeListener {

        @Override
        public void updateAttributes(ScanJobAttributeEvent attributesEvent) {
            ScanJobAttributeSet attributes = attributesEvent.getUpdateAttributes();
            mStateMachine.procScanEvent(SimpleScanStateMachine.ScanEvent.UPDATE_JOB_STATE_PROCESSING, attributes);

            //(1)
            ScanJobScanningInfo scanningInfo = (ScanJobScanningInfo) attributes.get(ScanJobScanningInfo.class);
            if (scanningInfo != null && scanningInfo.getScanningState() == ScanJobState.PROCESSING) {
                String status = mContext.getString(R.string.txid_scan_d_scanning) + " "
                        + String.format(mContext.getString(R.string.txid_scan_d_count), scanningInfo.getScannedCount());
                scannedPages = Integer.valueOf(scanningInfo.getScannedCount());

                ReactApplicationContext reactContext = getReactApplicationContext();
                WritableMap params = Arguments.createMap();
                params.putString("label", status);
                sendEvent(reactContext, "onScanJobAttributeUpdated", params);

                mStateMachine.procScanEvent(SimpleScanStateMachine.ScanEvent.UPDATE_JOB_STATE_PROCESSING, status);
            }
            if (scanningInfo != null && scanningInfo.getScanningState() == ScanJobState.PROCESSING_STOPPED) {
                timeOfWaitingNextOriginal = Integer.valueOf(scanningInfo.getRemainingTimeOfWaitingNextOriginal());
            }

            //(2)
            ScanJobSendingInfo sendingInfo = (ScanJobSendingInfo) attributes.get(ScanJobSendingInfo.class);
            if (sendingInfo != null && sendingInfo.getSendingState() == ScanJobState.PROCESSING) {
                String status = mContext.getString(R.string.txid_scan_d_sending);

                ReactApplicationContext reactContext = getReactApplicationContext();
                WritableMap params = Arguments.createMap();
                params.putString("label", status);
                sendEvent(reactContext, "onScanJobAttributeUpdated", params);

                mStateMachine.procScanEvent(SimpleScanStateMachine.ScanEvent.UPDATE_JOB_STATE_PROCESSING, status);
            }
        }

    }

    class ScanJobListenerImpl implements ScanJobListener {

        @Override
        public void jobCanceled(ScanJobEvent event) {
            // set application state to normal
            setAppState(MainActivity.class.getName(), SmartSDKApplication.APP_STATE_NORMAL, SmartSDKApplication.APP_STATE_NORMAL_MSG, SmartSDKApplication.APP_TYPE_SCANNER);

            ReactApplicationContext reactContext = getReactApplicationContext();
            WritableMap params = Arguments.createMap();
            params.putString("label", "CANCELED");
            sendEvent(reactContext, "onScanJobStateUpdated", params);

            mStateMachine.procScanEvent(SimpleScanStateMachine.ScanEvent.CHANGE_JOB_STATE_CANCELED);
        }

        @Override
        public void jobCompleted(ScanJobEvent event) {
            // set application state to normal
            setAppState(MainActivity.class.getName(), SmartSDKApplication.APP_STATE_NORMAL, SmartSDKApplication.APP_STATE_NORMAL_MSG, SmartSDKApplication.APP_TYPE_SCANNER);


            ReactApplicationContext reactContext = getReactApplicationContext();
            WritableMap params = Arguments.createMap();
            params.putString("label", "COMPLETED");
            sendEvent(reactContext, "onScanJobStateUpdated", params);

            ScanImage scanImage = new ScanImage(mScanJob);
//            InputStream inputStream = scanImage.getImageInputStream(1);
            Response<BinaryResponseBody> binResp = scanImage.getImageBinaryResponse();

            try {

                // 1
//                byte[] b = binResp.getBytes();
//                String imgString = Base64.encodeToString(b, Base64.NO_WRAP);
                byte[] b = binResp.getBytes();
                String imgString = Base64.encodeToString(b, Base64.DEFAULT);
                Log.d(Const.TAG, PREFIX+"scanned image: "+imgString);

                // 2
                reactContext = getReactApplicationContext();
                params = Arguments.createMap();
                params.putString("label", imgString);

                sendEvent(reactContext, "onScannedImageUpdated", params);


            } catch (Exception e) {
                reactContext = getReactApplicationContext();
                params = Arguments.createMap();
                params.putString("label", "readInputStreamToString error: " + e.getMessage());
                sendEvent(reactContext, "onScanJobListenerErrorUpdated", params);
            }

            mStateMachine.procScanEvent(SimpleScanStateMachine.ScanEvent.CHANGE_JOB_STATE_COMPLETED);

//            init();
//            mScanServiceAttrListener = new ScanServiceAttributeListenerImpl(new Handler(Looper.getMainLooper()));
//            mStateMachine.procScanEvent(SimpleScanStateMachine.ScanEvent.REBOOT_COMPLETED);
        }

        @Override
        public void jobAborted(ScanJobEvent event) {

            ScanJobAttributeSet attributes = event.getAttributeSet();
            ScanJobStateReasons reasons = (ScanJobStateReasons) attributes.get(ScanJobStateReasons.class);
            ScanJobStateReason jobStateReason = null;

            boolean isError = false;
            if (reasons != null) {
                Set<ScanJobStateReason> reasonSet = reasons.getReasons();
                Iterator<ScanJobStateReason> iter = reasonSet.iterator();
                while (iter.hasNext()) {
                    jobStateReason = iter.next();
                    switch (jobStateReason) {
                        case MEMORY_OVER:
                        case EXCEEDED_MAX_EMAIL_SIZE:
                        case EXCEEDED_MAX_PAGE_COUNT:
                        case RESOURCES_ARE_NOT_READY:
                        case BROADCAST_NUMBER_OVER:
                        case BLANK_PAGES_ONLY:
                        case CERTIFICATE_INVALID:
                        case JOB_FULL:
                        case CHARGE_UNIT_LIMIT:
                            isError = true;
                            break;
                        default:
                            isError = false;
                            break;
                    }
                    if (isError) {
                        break;
                    }
                }
            }
            if (isError) {
                // set application state to error
                setAppState(MainActivity.class.getName(), SmartSDKApplication.APP_STATE_ERROR, jobStateReason.toString(), SmartSDKApplication.APP_TYPE_SCANNER);
            } else {
                // set application state to normal
                setAppState(MainActivity.class.getName(), SmartSDKApplication.APP_STATE_NORMAL, SmartSDKApplication.APP_STATE_NORMAL_MSG, SmartSDKApplication.APP_TYPE_SCANNER);
            }

            ReactApplicationContext reactContext = getReactApplicationContext();
            WritableMap params = Arguments.createMap();
            params.putString("label", "ABORTED");
            sendEvent(reactContext, "onScanJobStateUpdated", params);

            mStateMachine.procScanEvent(SimpleScanStateMachine.ScanEvent.CHANGE_JOB_STATE_ABORTED, attributes);
        }

        @Override
        public void jobProcessing(ScanJobEvent event) {
            // set application state to normal
            setAppState(MainActivity.class.getName(), SmartSDKApplication.APP_STATE_NORMAL, SmartSDKApplication.APP_STATE_NORMAL_MSG, SmartSDKApplication.APP_TYPE_SCANNER);

            ScanJobAttributeSet attributes = event.getAttributeSet();

            ReactApplicationContext reactContext = getReactApplicationContext();
            WritableMap params = Arguments.createMap();
            params.putString("label", "PROCESSING");
            sendEvent(reactContext, "onScanJobStateUpdated", params);

            mStateMachine.procScanEvent(SimpleScanStateMachine.ScanEvent.CHANGE_JOB_STATE_PROCESSING, attributes);
        }

        @Override
        public void jobPending(ScanJobEvent event) {

            ScanJobAttributeSet attributes = event.getAttributeSet();
            ScanJobStateReasons reasons = (ScanJobStateReasons) attributes.get(ScanJobStateReasons.class);
            ScanJobStateReason jobStateReason = null;

            boolean isError = false;
            if (reasons != null) {
                Set<ScanJobStateReason> reasonSet = reasons.getReasons();
                Iterator<ScanJobStateReason> iter = reasonSet.iterator();
                while (iter.hasNext()) {
                    jobStateReason = iter.next();
                    switch (jobStateReason) {
                        case RESOURCES_ARE_NOT_READY:
                            isError = true;
                            break;
                        default:
                            isError = false;
                            break;
                    }
                    if (isError) {
                        break;
                    }
                }
            }
            if (isError) {
                // set application state to error
                setAppState(MainActivity.class.getName(), SmartSDKApplication.APP_STATE_ERROR, jobStateReason.toString(), SmartSDKApplication.APP_TYPE_SCANNER);
            } else {
                // set application state to normal
                setAppState(MainActivity.class.getName(), SmartSDKApplication.APP_STATE_NORMAL, SmartSDKApplication.APP_STATE_NORMAL_MSG, SmartSDKApplication.APP_TYPE_SCANNER);
            }

            ReactApplicationContext reactContext = getReactApplicationContext();
            WritableMap params = Arguments.createMap();
            params.putString("label", "PENDING");
            sendEvent(reactContext, "onScanJobStateUpdated", params);

            mStateMachine.procScanEvent(SimpleScanStateMachine.ScanEvent.CHANGE_JOB_STATE_PENDING);
        }

        @Override
        public void jobProcessingStop(ScanJobEvent event) {
            ScanJobAttributeSet attributes = event.getAttributeSet();

            ScanJobStateReasons reasons = (ScanJobStateReasons) attributes.get(ScanJobStateReasons.class);
            if (reasons != null) {
                Set<ScanJobStateReason> reasonSet = reasons.getReasons();

                boolean isError = false;
                Iterator<ScanJobStateReason> iter = reasonSet.iterator();
                ScanJobStateReason jobStateReason = null;
                while (iter.hasNext()) {
                    jobStateReason = iter.next();
                    switch (jobStateReason) {
                        case SCANNER_JAM:
                        case MEMORY_OVER:
                        case EXCEEDED_MAX_EMAIL_SIZE:
                        case EXCEEDED_MAX_PAGE_COUNT:
                            isError = true;
                            break;
                        default:
                            isError = false;
                            break;
                    }
                    if (isError) {
                        break;
                    }
                }
                if (isError) {
                    // set application state to error
                    setAppState(MainActivity.class.getName(), SmartSDKApplication.APP_STATE_ERROR, jobStateReason.toString(), SmartSDKApplication.APP_TYPE_SCANNER);
                } else {
                    // set application state to normal
                    if (reasonSet.contains(ScanJobStateReason.WAIT_FOR_ORIGINAL_PREVIEW_OPERATION)) {
                        setAppState(PreviewActivity.class.getName(), SmartSDKApplication.APP_STATE_NORMAL, SmartSDKApplication.APP_STATE_NORMAL_MSG, SmartSDKApplication.APP_TYPE_SCANNER);
                    } else {
                        setAppState(MainActivity.class.getName(), SmartSDKApplication.APP_STATE_NORMAL, SmartSDKApplication.APP_STATE_NORMAL_MSG, SmartSDKApplication.APP_TYPE_SCANNER);
                    }
                }

                // show Preview window
                if (reasonSet.contains(ScanJobStateReason.WAIT_FOR_ORIGINAL_PREVIEW_OPERATION)) {

                    ReactApplicationContext reactContext = getReactApplicationContext();
                    WritableMap params = Arguments.createMap();
                    params.putString("label", "STOPPED_PREVIEW");
                    sendEvent(reactContext, "onScanJobStateUpdated", params);

                    mStateMachine.procScanEvent(SimpleScanStateMachine.ScanEvent.CHANGE_JOB_STATE_STOPPED_PREVIEW);
                    return;
                }

                // show CountDown window
                if (reasonSet.contains(ScanJobStateReason.WAIT_FOR_NEXT_ORIGINAL_AND_CONTINUE)) {

                    ReactApplicationContext reactContext = getReactApplicationContext();
                    WritableMap params = Arguments.createMap();
                    params.putString("label", "STOPPED_COUNTDOWN");
                    sendEvent(reactContext, "onScanJobStateUpdated", params);

                    mStateMachine.procScanEvent(SimpleScanStateMachine.ScanEvent.CHANGE_JOB_STATE_STOPPED_COUNTDOWN);
                    return;
                }

                // show Other reason job stopped window
                StringBuilder sb = new StringBuilder();
                for (ScanJobStateReason reason : reasonSet) {
                    sb.append(reason.toString()).append("\n");
                }


                ReactApplicationContext reactContext = getReactApplicationContext();
                WritableMap params = Arguments.createMap();
                params.putString("label", "STOPPED_OTHER " + sb.toString());
                sendEvent(reactContext, "onScanJobStateUpdated", params);

                mStateMachine.procScanEvent(SimpleScanStateMachine.ScanEvent.CHANGE_JOB_STATE_STOPPED_OTHER, sb.toString());
                return;
            }

            // set application state to normal
            setAppState(MainActivity.class.getName(), SmartSDKApplication.APP_STATE_NORMAL, SmartSDKApplication.APP_STATE_NORMAL_MSG, SmartSDKApplication.APP_TYPE_SCANNER);

            // Unknown job stop reason

            ReactApplicationContext reactContext = getReactApplicationContext();
            WritableMap params = Arguments.createMap();
            params.putString("label", "STOPPED_OTHER (unknown reason)");
            sendEvent(reactContext, "onScanJobStateUpdated", params);

            mStateMachine.procScanEvent(SimpleScanStateMachine.ScanEvent.CHANGE_JOB_STATE_STOPPED_OTHER, "(unknown reason)");
        }
    }

    public ScanService getScanService() {
        return mScanService;
    }

    public ScanJob getScanJob() {
        return mScanJob;
    }

    public DestinationSettingDataHolder getDestinationSettingDataHolder() {
        return mDestinationSettingDataHolder;
    }

    public ScanSettingDataHolder getScanSettingDataHolder() {
        return mScanSettingDataHolder;
    }

    public StorageSettingDataHolder getStorageSettingDataHolder() {
        return mStorageSettingDataHolder;
    }

    public SimpleScanStateMachine getStateMachine() {
        return mStateMachine;
    }

    public SystemStateMonitor getSystemStateMonitor() {
        return mSystemStateMonitor;
    }

    public int getScannedPages() {
        return scannedPages;
    }

    public int getTimeOfWaitingNextOriginal() {
        return timeOfWaitingNextOriginal;
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


    /********************************** scan service *************************************/


    private final static String ALERT_DIALOG_APP_TYPE_SCANNER = "SCANNER";
    private final int OTHER_SETTING_DIALOG_WIDTH = 600;
    private final int OTHER_SETTING_DIALOG_HEIGHT = 500;
    //    private TextView mTextJobMode;
//    private TextView text_state;
    private volatile boolean mAlertDialogDisplayed = false;
    private OccuredErrorLevel mLastErrorLevel = null;
    private AlertDialogDisplayTask mAlertDialogDisplayTask = null;
    private ReturnRequestFromEnergySavingTask mReturnRequestFromEnergySavingTask = null;
    private BroadcastReceiver mReceiver;
    private ScanServiceAttributeListener mScanServiceAttrListener;
    //    private ScanSettingDataHolder mScanSettingDataHolder;
    private DestinationSettingDataHolder mDestSettingDataHolder;
    //    private StorageSettingDataHolder mStorageSettingDataHolder;
//    private ScanStateMachine mStateMachine;
    private ScanServiceInitTask mScanServiceInitTask;
    private boolean mMultipleRunning = false;
    private boolean mIsForeground = false;
    private boolean mRequestedSystemResetLock = false;
    private final static String PREFIX = "javaModule:ScannerModule:";

    class ScanServiceInitTask extends AsyncTask<Void, Void, Integer> {

        AsyncConnectState addListenerResult = null;
        AsyncConnectState getAsyncConnectStateResult = null;

        @Override
        protected Integer doInBackground(Void... params) {

            final ScanService scanService = getScanService();

            //(1)
            while (true) {
                if (isCancelled()) {
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
                getAsyncConnectStateResult = scanService.getAsyncConnectState();

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
                mScanSettingDataHolder.init(scanService);

                ReactApplicationContext reactContext = getReactApplicationContext();
                WritableMap connectStateParams = Arguments.createMap();
                connectStateParams.putString("label", AsyncConnectState.STATE.CONNECTED.toString());
                sendEvent(reactContext, "onConnectStateUpdated", connectStateParams);
            }

            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (addListenerResult == null) {
                Log.d(Const.TAG, PREFIX + "addScanServiceAttributeListener:null");
            } else {
                Log.d(Const.TAG, PREFIX + "addScanServiceAttributeListener:" + addListenerResult.getState() + "," + addListenerResult.getErrorCode());
            }
            if (getAsyncConnectStateResult == null) {
                Log.d(Const.TAG, PREFIX + "getAsyncConnectState:null");
            } else {
                Log.d(Const.TAG, PREFIX + "getAsyncConnectState:" + getAsyncConnectStateResult.getState() + "," + getAsyncConnectStateResult.getErrorCode());
            }

            if (result != 0) {
                /* canceled. */
                return;
            }

            if (addListenerResult.getState() == AsyncConnectState.STATE.CONNECTED
                    && getAsyncConnectStateResult.getState() == AsyncConnectState.STATE.CONNECTED) {
                // connection succeeded.
                // to do:
//                mButtonColor.setText(mScanSettingDataHolder.getSelectedColorLabel());
//                mButtonFileSetting.setText(mScanSettingDataHolder.getSelectedFileSettingLabel());
//                mButtonSide.setText(mScanSettingDataHolder.getSelectedSideLabel());
//                mButtonJobMode.setEnabled(true);
//                mButtonDestination.setEnabled(true);
                UpdateScreenOnJobModeChanged();
                mStateMachine.procScanEvent(SimpleScanStateMachine.ScanEvent.ACTIVITY_BOOT_COMPLETED);
            } else {
                // the connection is invalid.
                mStateMachine.procScanEvent(SimpleScanStateMachine.ScanEvent.ACTIVITY_BOOT_FAILED);
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

    private void UpdateScreenOnJobModeChanged() {
        JobMode jobMode = mScanSettingDataHolder.getSelectedJobModeValue();
//        mTextJobMode.setText(mScanSettingDataHolder.getSelectedJobModeLabel());
        ReactApplicationContext reactContext = getReactApplicationContext();
        WritableMap params = Arguments.createMap();
        params.putString("label", Integer.toString(mScanSettingDataHolder.getSelectedJobModeLabel()));
        sendEvent(reactContext, "onJobModeChanged", params);

//        LinearLayout view_storeFile = (LinearLayout)findViewById(R.id.view_send_storedfile);
//        RelativeLayout conatiner_destination = (RelativeLayout)findViewById(R.id.container_destination);
//        RelativeLayout conatiner_store_setting = (RelativeLayout)findViewById(R.id.container_store_setting);
//        switch(jobMode) {
//            case SCAN_AND_SEND:
//                mButtonSendStoredFile.setVisibility(View.INVISIBLE);
//                view_storeFile.setVisibility(View.INVISIBLE);
//                conatiner_destination.setVisibility(View.VISIBLE);
//                conatiner_store_setting.setVisibility(View.INVISIBLE);
//                enableSettingKey();
//                break;
//            case SCAN_AND_STORE_LOCAL:
//                mButtonSendStoredFile.setVisibility(View.INVISIBLE);
//                view_storeFile.setVisibility(View.INVISIBLE);
//                conatiner_destination.setVisibility(View.INVISIBLE);
//                conatiner_store_setting.setVisibility(View.VISIBLE);
//                enableSettingKey();
//                mButtonFileSetting.setEnabled(false);
//                break;
//            case SEND_STORED_FILE:
//                mButtonSendStoredFile.setVisibility(View.VISIBLE);
//                view_storeFile.setVisibility(View.VISIBLE);
//                conatiner_destination.setVisibility(View.VISIBLE);
//                conatiner_store_setting.setVisibility(View.INVISIBLE);
//                disableSettingKey();
//                mButtonFileSetting.setEnabled(true);
//                break;
//            default:
//                /* should never reach here */
//                break;
//        }
//
//        mButtonStart.setEnabled(isSettingCompleted());
    }

    private String makeAlertStateString(ScannerState state) {
        String stateString = "";
        if (state != null) {
            stateString = state.toString();
        }
        return stateString;
    }

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

    private boolean isForegroundApp(String packageName) {
        boolean result = false;
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : list) {
            if (packageName.equals(info.processName)) {
                result = (info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND);
                break;
            }
        }
        return result;
    }

    private String getTopActivityClassName(String packageName) {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(30);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (packageName.equals(info.topActivity.getPackageName())) {
                return info.topActivity.getClassName();
            }
        }
        return null;
    }

    private int getNumActivities(String packageName) {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(30);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (packageName.equals(info.topActivity.getPackageName())) {
                return info.numActivities;
            }
        }
        return 0;
    }

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
            ScannerState state = (ScannerState) event.getAttributes().get(ScannerState.class);
            ScannerStateReasons stateReasons = (ScannerStateReasons) event.getAttributes().get(ScannerStateReasons.class);
            OccuredErrorLevel errorLevel = (OccuredErrorLevel) event.getAttributes().get(OccuredErrorLevel.class);

            String stateLabel = "";

            //(1)
            switch (state) {
                case IDLE:
                    Log.i(Const.TAG, PREFIX + "ScannerState : IDLE");
                    stateLabel = mContext.getString(R.string.txid_scan_t_state_ready);
                    break;
                case MAINTENANCE:
                    Log.i(Const.TAG, PREFIX + "ScannerState : MAINTENANCE");
                    stateLabel = mContext.getString(R.string.txid_scan_t_state_maintenance);
                    break;
                case PROCESSING:
                    Log.i(Const.TAG, PREFIX + "ScannerState : PROCESSING");
                    stateLabel = mContext.getString(R.string.txid_scan_t_state_scanning);
                    break;
                case STOPPED:
                    Log.i(Const.TAG, PREFIX + "ScannerState : STOPPED");
                    stateLabel = mContext.getString(R.string.txid_scan_t_state_stopped);
                    break;
                case UNKNOWN:
                    Log.i(Const.TAG, PREFIX + "ScannerState : UNKNOWN");
                    stateLabel = mContext.getString(R.string.txid_scan_t_state_unknown);
                    break;
                default:
                    Log.i(Const.TAG, PREFIX + "ScannerState : never reach here ...");
                    stateLabel = state + " (undefined)";
                    /* never reach here */
                    break;
            }

            if (stateReasons != null) {
                Set<ScannerStateReason> reasonSet = stateReasons.getReasons();
                for (ScannerStateReason reason : reasonSet) {
                    switch (reason) {
                        case COVER_OPEN:
                            stateLabel = mContext.getString(R.string.txid_scan_t_state_reason_cover_open);
                            break;
                        case MEDIA_JAM:
                            stateLabel = mContext.getString(R.string.txid_scan_t_state_reason_media_jam);
                            break;
                        case PAUSED:
                            stateLabel = mContext.getString(R.string.txid_scan_t_state_reason_paused);
                            break;
                        case OTHER:
                            stateLabel = mContext.getString(R.string.txid_scan_t_state_reason_other);
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
//                    text_state.setText(result);
                    ReactApplicationContext reactContext = getReactApplicationContext();
                    WritableMap params = Arguments.createMap();
                    params.putString("label", result);
                    sendEvent(reactContext, "onScanServiceAttributeUpdated", params);
                }
            });

            //(2)
            if (OccuredErrorLevel.ERROR.equals(errorLevel)
                    || OccuredErrorLevel.FATAL_ERROR.equals(errorLevel)) {

                String stateString = makeAlertStateString(state);
                String reasonString = makeAlertStateReasonString(stateReasons);


                ReactApplicationContext reactContext = getReactApplicationContext();
                WritableMap params = Arguments.createMap();
                params.putString("label", stateString+": "+reasonString);
                sendEvent(reactContext, "onScanServiceAttributeListenerErrorUpdated", params);

                if (mLastErrorLevel == null) {
                    // Normal -> Error
                    if (isForegroundApp(mContext.getPackageName())) {
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
                        String activityName = getTopActivityClassName(mContext.getPackageName());
                        if (activityName == null) {
                            activityName = MainActivity.class.getName();
                        }
                        mApplication.hideAlertDialog(ALERT_DIALOG_APP_TYPE_SCANNER, activityName);
                        mAlertDialogDisplayed = false;
                    }
                }
                mLastErrorLevel = null;
            }
        }
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

    class AlertDialogDisplayTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            ScanServiceAttributeSet attributes = getScanService().getAttributes();
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

    private void startReturnRequestFromEnergySavingTask() {
        if (mReturnRequestFromEnergySavingTask != null) {
            mReturnRequestFromEnergySavingTask.cancel(true);
        }
        mReturnRequestFromEnergySavingTask = new ReturnRequestFromEnergySavingTask();
        mReturnRequestFromEnergySavingTask.execute();
    }

    private void stopReturnRequestFromEnergySavingTask() {
        if (mReturnRequestFromEnergySavingTask != null) {
            mReturnRequestFromEnergySavingTask.cancel(true);
            mReturnRequestFromEnergySavingTask = null;
        }
    }

    class ReturnRequestFromEnergySavingTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean needsRequest;

            int powerMode = getSystemStateMonitor().getPowerMode();
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
}