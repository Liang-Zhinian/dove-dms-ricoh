package com.dove;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;

import com.dove.sample.app.scan.Const;
import com.dove.sample.app.scan.application.DestinationSettingDataHolder;
import com.dove.sample.app.scan.application.ScanSettingDataHolder;
import com.dove.sample.app.scan.application.StorageSettingDataHolder;
import com.dove.sample.app.scan.application.SystemStateMonitor;
import com.dove.sample.function.common.SmartSDKApplication;
import com.dove.sample.function.common.impl.AsyncConnectState;
import com.dove.sample.function.scan.ScanImage;
import com.dove.sample.function.scan.ScanJob;
import com.dove.sample.function.scan.ScanService;
import com.dove.sample.function.scan.attribute.HashScanRequestAttributeSet;
import com.dove.sample.function.scan.attribute.ScanException;
import com.dove.sample.function.scan.attribute.ScanJobAttributeSet;
import com.dove.sample.function.scan.attribute.ScanRequestAttributeSet;
import com.dove.sample.function.scan.attribute.standard.AutoCorrectJobSetting;
import com.dove.sample.function.scan.attribute.standard.FileSetting;
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
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;


/**
 * Created by Administrator on 2017/12/8.
 */

public class RCTRicohScanner extends ReactContextBaseJavaModule implements IScanApplication {
    private final static String PREFIX = "rectmodule:RicohScanner:";
    private OccuredErrorLevel mLastErrorLevel = null;
    private ScanService mScanService;
    private ScanServiceAttributeListener mScanServiceAttrListener;
    private ScanServiceInitTask mScanServiceInitTask;
    private ScanJob mScanJob;
    private ScanJobListener mScanJobListener;
    private ScanJobAttributeListener mScanJobAttrListener;
    private SmartSDKApplication mApplication;
    private DestinationSettingDataHolder mDestinationSettingDataHolder;
    private ScanSettingDataHolder mScanSettingDataHolder;
    private StorageSettingDataHolder mStorageSettingDataHolder;
    //    private SimpleScanStateMachine mStateMachine;
//    private SystemStateMonitor mSystemStateMonitor;
    protected int scannedPages;
    protected int timeOfWaitingNextOriginal = 0;

    public RCTRicohScanner(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "RCTRicohScanner";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        return constants;
    }


    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           @Nullable WritableMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
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
        params.putString("stateLabel", "READY");
        sendEvent(reactContext, "ScanJobStateUpdated", params);
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
    public ScanSettingDataHolder getScanSettingDataHolder() {
        return mScanSettingDataHolder;
    }

    @Override
    public DestinationSettingDataHolder getDestinationSettingDataHolder() {
        return mDestinationSettingDataHolder;

    }

    @Override
    public StorageSettingDataHolder getStorageSettingDataHolder() {
        return mStorageSettingDataHolder;

    }

    @Override
    public ScanJob getScanJob() {
        return mScanJob;

    }

    @ReactMethod
    public void init(Promise promise) throws JSONException {
        try {
            init();
            promise.resolve("Initialization success!!");
        } catch (android.content.ActivityNotFoundException e) {

            promise.reject("Initialization error!!");
        }
    }


    @ReactMethod
    public void restore(Promise promise) throws JSONException {
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
            promise.resolve("Initialization success!!");
        } catch (android.content.ActivityNotFoundException e) {

            promise.reject("Initialization error!!");
        }
    }

    @ReactMethod
    public void start(Promise promise) throws JSONException {
// start scan
        boolean result = false;
        String message = null;
        try {
//            Activity currentActivity = getCurrentActivity();
//            if(null!=currentActivity){
//                Intent intent = new Intent(currentActivity, TopActivity.class); //new Intent(currentActivity, TopActivity.class);
//                currentActivity.startActivity(intent);
//            }

//            ReactApplicationContext context = getReactApplicationContext();
//            Intent intent = new Intent(context, TopActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);

            ScanRequestAttributeSet requestAttributes;
            requestAttributes = new HashScanRequestAttributeSet();
            requestAttributes.add(AutoCorrectJobSetting.AUTO_CORRECT_ON);


            JobMode jobMode = JobMode.SCAN_AND_STORE_TEMPORARY;
            requestAttributes.add(jobMode);

            FileSetting fileSetting = new FileSetting();
            fileSetting.setFileFormat(FileSetting.FileFormat.PDF);
            fileSetting.setMultiPageFormat(true);
            requestAttributes.add(fileSetting);

//            SmbAddressManualDestinationSetting dest = new SmbAddressManualDestinationSetting();
//            dest.setPath("test");
//            dest.setUserName("test");
//            dest.setPassword("test");
//
//            mDestinationSettingDataHolder.setDestinationSettingItem(dest);

//            DestinationSetting destSetting = new DestinationSetting();
//            mDestinationSettingDataHolder.add(dest);
//            requestAttributes.add(destSetting);

//            mScanSettingDataHolder.setSelectedColor(0); // MONOCHROME_TEXT
//            mScanSettingDataHolder.setSelectedFileSetting(1); // TIFF_JPEG
//            mScanSettingDataHolder.setSelectedJobMode(3); // SCAN_AND_STORE_TEMPORARY
//            mScanSettingDataHolder.setSelectedPreview(0); // preview off

            try {
                Log.i(Const.TAG, PREFIX + "scan requesting...");
                result = mScanJob.scan(requestAttributes);
//                mStateMachine.procScanEvent(SimpleScanStateMachine.ScanEvent.REQUEST_JOB_START);
                Log.d(Const.TAG, PREFIX + "scan requested. result=" + result);
            } catch (Exception e) {
                message = "job start failed. " + e.getMessage();

                Log.w(Const.TAG, PREFIX + message);
            }


            promise.resolve("Start activity success!!");
        } catch (android.content.ActivityNotFoundException e) {

            promise.reject("Start activity error!!");
        }
    }

    private void init() {
        mApplication = (SmartSDKApplication) getCurrentActivity().getApplication();

        mDestinationSettingDataHolder = new DestinationSettingDataHolder();
        mScanSettingDataHolder = new ScanSettingDataHolder();
        mStorageSettingDataHolder = new StorageSettingDataHolder();
        mScanService = ScanService.getService();


        mScanServiceAttrListener = new SimpleScanServiceAttributeListenerImpl(new Handler(Looper.getMainLooper()));
//
        AsyncConnectState addListenerResult = null;
        addListenerResult = mScanService.addScanServiceAttributeListener(mScanServiceAttrListener);


        if (addListenerResult != null && addListenerResult.getState() == AsyncConnectState.STATE.CONNECTED) {
            Log.d(Const.TAG, PREFIX + "getAsyncConnectState:CONNECTED");
        }


        if (mScanServiceInitTask != null) {
            mScanServiceInitTask.cancel(false);
        }
        mScanServiceInitTask = new ScanServiceInitTask();
        mScanServiceInitTask.execute();


                    /* unlock power mode*/
        mApplication.unlockPowerMode();
                    /* unlock offline */
        mApplication.unlockOffline();

        initJobSetting();


        // send event
//            mStateMachine.procScanEvent(ScanEvent.ACTIVITY_CREATED);

        // set application state to Normal
        setAppState(MainActivity.class.getName(), SmartSDKApplication.APP_STATE_NORMAL, SmartSDKApplication.APP_STATE_NORMAL_MSG, SmartSDKApplication.APP_TYPE_SCANNER);

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


    class SimpleScanServiceAttributeListenerImpl implements ScanServiceAttributeListener {

        /**
         * UI thread handler
         */
        private Handler mHandler;

//        public Runnable setTextState;

        SimpleScanServiceAttributeListenerImpl(Handler handler) {
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
                    stateLabel = "Ready";//getString(R.string.txid_scan_t_state_ready);
                    break;
                case MAINTENANCE:
                    Log.i(Const.TAG, PREFIX + "ScannerState : MAINTENANCE");
                    stateLabel = "Maintenance";//getString(R.string.txid_scan_t_state_maintenance);
                    break;
                case PROCESSING:
                    Log.i(Const.TAG, PREFIX + "ScannerState : PROCESSING");
                    stateLabel = "Scanning";//getString(R.string.txid_scan_t_state_scanning);
                    break;
                case STOPPED:
                    Log.i(Const.TAG, PREFIX + "ScannerState : STOPPED");
                    stateLabel = "Stopped";//getString(R.string.txid_scan_t_state_stopped);
                    break;
                case UNKNOWN:
                    Log.i(Const.TAG, PREFIX + "ScannerState : UNKNOWN");
                    stateLabel = "Unknown";//getString(R.string.txid_scan_t_state_unknown);
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
                            stateLabel = "Cover Open";//getString(R.string.txid_scan_t_state_reason_cover_open);
                            break;
                        case MEDIA_JAM:
                            stateLabel = "Media Jam";//getString(R.string.txid_scan_t_state_reason_media_jam);
                            break;
                        case PAUSED:
                            stateLabel = "Paused";//getString(R.string.txid_scan_t_state_reason_paused);
                            break;
                        case OTHER:
                            stateLabel = "Other";//getString(R.string.txid_scan_t_state_reason_other);
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
                    ReactApplicationContext reactContext = getReactApplicationContext();
                    WritableMap params = Arguments.createMap();
                    params.putString("stateLabel", result);
                    sendEvent(reactContext, "ScanServiceAttributeUpdated", params);
                }
            });


            //(2)
            if (OccuredErrorLevel.ERROR.equals(errorLevel)
                    || OccuredErrorLevel.FATAL_ERROR.equals(errorLevel)) {
//
                String stateString = makeAlertStateString(state);
                String reasonString = makeAlertStateReasonString(stateReasons);
//
//                if (mLastErrorLevel == null) {
//                    // Normal -> Error
//                    if (isForegroundApp(getPackageName())) {
//                        mApplication.displayAlertDialog(ALERT_DIALOG_APP_TYPE_SCANNER, stateString, reasonString);
//                        mAlertDialogDisplayed = true;
//                    }
//                } else {
//                    // Error -> Error
//                    if (mAlertDialogDisplayed) {
//                        mApplication.updateAlertDialog(ALERT_DIALOG_APP_TYPE_SCANNER, stateString, reasonString);
//                    }
//                }

                ReactApplicationContext reactContext = getReactApplicationContext();
                WritableMap params = Arguments.createMap();
                params.putString("stateLabel", stateString + " >> " + reasonString);
                sendEvent(reactContext, "ScanServiceAttributeListenerErrorUpdated", params);

                mLastErrorLevel = errorLevel;
//
            } else {
//                if (mLastErrorLevel != null) {
//                    // Error -> Normal
//                    if (mAlertDialogDisplayed) {
//                        String activityName = getTopActivityClassName(getPackageName());
//                        if (activityName == null) {
//                            activityName = TopActivity.class.getName();
//                        }
//                        mApplication.hideAlertDialog(ALERT_DIALOG_APP_TYPE_SCANNER, activityName);
//                        mAlertDialogDisplayed = false;
//                    }
//                }
                mLastErrorLevel = null;
            }
        }
    }

    class ScanServiceInitTask extends AsyncTask<Void, Void, Integer> {

        AsyncConnectState addListenerResult = null;
        AsyncConnectState getAsyncConnectStateResult = null;

        @Override
        protected Integer doInBackground(Void... params) {

            final ScanService scanService = ScanService.getService(); //mScanService;

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
                connectStateParams.putString("stateLabel", AsyncConnectState.STATE.CONNECTED.toString());
                sendEvent(reactContext, "ConnectStateUpdated", connectStateParams);
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
//                mButtonColor.setText(mScanSettingDataHolder.getSelectedColorLabel());
//                mButtonFileSetting.setText(mScanSettingDataHolder.getSelectedFileSettingLabel());
//                mButtonSide.setText(mScanSettingDataHolder.getSelectedSideLabel());
//                mButtonJobMode.setEnabled(true);
//                mButtonDestination.setEnabled(true);
//                UpdateScreenOnJobModeChanged();
//                mStateMachine.procScanEvent(SimpleScanStateMachine.ScanEvent.ACTIVITY_BOOT_COMPLETED);
            } else {
                // the connection is invalid.
//                mStateMachine.procScanEvent(SimpleScanStateMachine.ScanEvent.ACTIVITY_BOOT_FAILED);
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
     * スキャンジョブの属性変化監視リスナー実装クラスです。
     * [処理内容]
     * (1)読み取り情報があれば、ステートマシンに通知します。
     * (2)送信情報があれば、ステートマシンに通知します。
     * <p>
     * The class to implement the listener to monitor scan job state changes.
     * [Processes]
     * (1) If scan information exists, the information is notified to the state machine.
     * (2) If data transfer information exists, the information is notified to the state machine.
     */
    class ScanJobAttributeListenerImpl implements ScanJobAttributeListener {

        @Override
        public void updateAttributes(ScanJobAttributeEvent attributesEvent) {
            ScanJobAttributeSet attributes = attributesEvent.getUpdateAttributes();
//            mStateMachine.procScanEvent(ScanEvent.UPDATE_JOB_STATE_PROCESSING, attributes);

            //(1)
            ScanJobScanningInfo scanningInfo = (ScanJobScanningInfo) attributes.get(ScanJobScanningInfo.class);
            if (scanningInfo != null && scanningInfo.getScanningState() == ScanJobState.PROCESSING) {
                String status = getCurrentActivity().getString(R.string.txid_scan_d_scanning) + " "
                        + String.format(getCurrentActivity().getString(R.string.txid_scan_d_count), scanningInfo.getScannedCount());
                scannedPages = Integer.valueOf(scanningInfo.getScannedCount());
//                mStateMachine.procScanEvent(ScanEvent.UPDATE_JOB_STATE_PROCESSING, status);

                ReactApplicationContext reactContext = getReactApplicationContext();
                WritableMap params = Arguments.createMap();
                params.putString("stateLabel", status);
                sendEvent(reactContext, "ScanServiceAttributeUpdated", params);
            }
            if (scanningInfo != null && scanningInfo.getScanningState() == ScanJobState.PROCESSING_STOPPED) {
                timeOfWaitingNextOriginal = Integer.valueOf(scanningInfo.getRemainingTimeOfWaitingNextOriginal());
            }

            //(2)
            ScanJobSendingInfo sendingInfo = (ScanJobSendingInfo) attributes.get(ScanJobSendingInfo.class);
            if (sendingInfo != null && sendingInfo.getSendingState() == ScanJobState.PROCESSING) {
                String status = getCurrentActivity().getString(R.string.txid_scan_d_sending);
//                mStateMachine.procScanEvent(ScanEvent.UPDATE_JOB_STATE_PROCESSING, status);

                ReactApplicationContext reactContext = getReactApplicationContext();
                WritableMap params = Arguments.createMap();
                params.putString("stateLabel", status);
                sendEvent(reactContext, "ScanServiceAttributeUpdated", params);
            }
        }

    }

    /**
     * スキャンジョブの状態監視リスナーです。
     * The listener to monitor scan job state changes.
     */
    class ScanJobListenerImpl implements ScanJobListener {

        @Override
        public void jobCanceled(ScanJobEvent event) {
            // set application state to normal
            setAppState(MainActivity.class.getName(), SmartSDKApplication.APP_STATE_NORMAL, SmartSDKApplication.APP_STATE_NORMAL_MSG, SmartSDKApplication.APP_TYPE_SCANNER);

//            mStateMachine.procScanEvent(ScanEvent.CHANGE_JOB_STATE_CANCELED);

            ReactApplicationContext reactContext = getReactApplicationContext();
            WritableMap params = Arguments.createMap();
            params.putString("stateLabel", "CHANGE_JOB_STATE_CANCELED");
            sendEvent(reactContext, "ScanJobStateUpdated", params);
        }

        @Override
        public void jobCompleted(ScanJobEvent event) {
            // set application state to normal
            setAppState(MainActivity.class.getName(), SmartSDKApplication.APP_STATE_NORMAL, SmartSDKApplication.APP_STATE_NORMAL_MSG, SmartSDKApplication.APP_TYPE_SCANNER);

//            mStateMachine.procScanEvent(ScanEvent.CHANGE_JOB_STATE_COMPLETED);


            ReactApplicationContext reactContext = getReactApplicationContext();
            WritableMap params = Arguments.createMap();
            params.putString("stateLabel", "CHANGE_JOB_STATE_COMPLETED");
            sendEvent(reactContext, "ScanJobStateUpdated", params);

            ScanImage scanImage = new ScanImage(mScanJob);
            InputStream inputStream = scanImage.getImageInputStream(1);
            Response<BinaryResponseBody> binResp = scanImage.getImageBinaryResponse();

            try {

                // 1
//                byte[] b = binResp.getBytes();
//                String imgString = Base64.encodeToString(b, Base64.NO_WRAP);

                byte[] b = binResp.getBytes();
                String imgString = Base64.encodeToString(b, Base64.DEFAULT);

                // 2
//                ByteArrayOutputStream result = new ByteArrayOutputStream();
//                byte[] buffer = new byte[1024];
//                int length;
//                while ((length = inputStream.read(buffer)) != -1) {
//                    result.write(buffer, 0, length);
//                }
//                String imgString = result.toString("UTF-8");

                // 3
//                byte[] b = readStream(inputStream);
//                String imgString = Base64.encodeToString(b, Base64.DEFAULT);

                // 4
//                Bitmap bmp = BitmapFactory.decodeResourceStream(getCurrentActivity().getResources(), new TypedValue(), inputStream, new Rect(), null);
//                String imgString = encodeImage(bmp);

                reactContext = getReactApplicationContext();
                params = Arguments.createMap();
                params.putString("stateLabel", imgString);
                sendEvent(reactContext, "ScannedImageUpdated", params);


            } catch (Exception e) {
                reactContext = getReactApplicationContext();
                params = Arguments.createMap();
                params.putString("stateLabel", "readInputStreamToString error: " + e.getMessage());
                sendEvent(reactContext, "ScanJobListenerErrorUpdated", params);
            }

            init();
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

//            mStateMachine.procScanEvent(ScanEvent.CHANGE_JOB_STATE_ABORTED, attributes);

            ReactApplicationContext reactContext = getReactApplicationContext();
            WritableMap params = Arguments.createMap();
            params.putString("stateLabel", "CHANGE_JOB_STATE_ABORTED");
            sendEvent(reactContext, "ScanJobStateUpdated", params);
        }

        @Override
        public void jobProcessing(ScanJobEvent event) {
            // set application state to normal
            setAppState(MainActivity.class.getName(), SmartSDKApplication.APP_STATE_NORMAL, SmartSDKApplication.APP_STATE_NORMAL_MSG, SmartSDKApplication.APP_TYPE_SCANNER);

            ScanJobAttributeSet attributes = event.getAttributeSet();
//            mStateMachine.procScanEvent(ScanEvent.CHANGE_JOB_STATE_PROCESSING, attributes);


            ReactApplicationContext reactContext = getReactApplicationContext();
            WritableMap params = Arguments.createMap();
            params.putString("stateLabel", "CHANGE_JOB_STATE_PROCESSING");
            sendEvent(reactContext, "ScanJobStateUpdated", params);
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

//            mStateMachine.procScanEvent(ScanEvent.CHANGE_JOB_STATE_PENDING);

            ReactApplicationContext reactContext = getReactApplicationContext();
            WritableMap params = Arguments.createMap();
            params.putString("stateLabel", "CHANGE_JOB_STATE_PENDING");
            sendEvent(reactContext, "ScanJobStateUpdated", params);
        }

        /**
         * ジョブが一時停止状態になった際に呼び出されます。
         * 状態の理由が複数ある場合は、最初の１つのみを参照します。
         * [処理内容]
         * (1)原稿ガラススキャン時、次原稿待ちの一時停止イベントだった場合
         * -ステートマシンに次原稿待ちイベントを送信します。
         * (2)プレビュー表示のための一時停止イベントだった場合
         * -ステートマシンにプレビュー表示のイベントを送信します。
         * (3)その他の理由による一時停止イベントだった場合
         * -ここでは、ジョブをキャンセルします。
         * <p>
         * Called when the job is in paused state.
         * If multiple reasons exist, only the first reason is checked.
         * [Processes]
         * (1) For the pause event for waiting for the next document when using exposure glass
         * - Sends the document waiting event to the state machine.
         * (2) For the pause event for preview display
         * - Sends the preview display event to the state machine.
         * (3) For the pause event for other reasons
         * - The job is cancelled in this case.
         */
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
                        setAppState(MainActivity.class.getName(), SmartSDKApplication.APP_STATE_NORMAL, SmartSDKApplication.APP_STATE_NORMAL_MSG, SmartSDKApplication.APP_TYPE_SCANNER);
                    } else {
                        setAppState(MainActivity.class.getName(), SmartSDKApplication.APP_STATE_NORMAL, SmartSDKApplication.APP_STATE_NORMAL_MSG, SmartSDKApplication.APP_TYPE_SCANNER);
                    }
                }

                // show Preview window
                if (reasonSet.contains(ScanJobStateReason.WAIT_FOR_ORIGINAL_PREVIEW_OPERATION)) {
//                    mStateMachine.procScanEvent(ScanEvent.CHANGE_JOB_STATE_STOPPED_PREVIEW);

//                    mStateMachine.procScanEvent(ScanEvent.CHANGE_JOB_STATE_COMPLETED);

                    ReactApplicationContext reactContext = getReactApplicationContext();
                    WritableMap params = Arguments.createMap();
                    params.putString("stateLabel", "CHANGE_JOB_STATE_STOPPED_PREVIEW");
                    sendEvent(reactContext, "ScanJobStateUpdated", params);

                    return;
                }

                // show CountDown window
                if (reasonSet.contains(ScanJobStateReason.WAIT_FOR_NEXT_ORIGINAL_AND_CONTINUE)) {
//                    mStateMachine.procScanEvent(ScanEvent.CHANGE_JOB_STATE_STOPPED_COUNTDOWN);

                    ReactApplicationContext reactContext = getReactApplicationContext();
                    WritableMap params = Arguments.createMap();
                    params.putString("stateLabel", "CHANGE_JOB_STATE_STOPPED_COUNTDOWN");
                    sendEvent(reactContext, "ScanJobStateUpdated", params);
                    return;
                }

                // show Other reason job stopped window
                StringBuilder sb = new StringBuilder();
                for (ScanJobStateReason reason : reasonSet) {
                    sb.append(reason.toString()).append("\n");
                }

//                mStateMachine.procScanEvent(ScanEvent.CHANGE_JOB_STATE_STOPPED_OTHER, sb.toString());

                ReactApplicationContext reactContext = getReactApplicationContext();
                WritableMap params = Arguments.createMap();
                params.putString("stateLabel", "CHANGE_JOB_STATE_STOPPED_OTHER " + sb.toString());
                sendEvent(reactContext, "ScanJobStateUpdated", params);
                try {
                    mScanJob.cancelScanJob();
                    init();
                } catch (ScanException e) {
                    String message = "job cancel failed. " + e.getMessage();
                    Log.w(Const.TAG, PREFIX + message);
                    reactContext = getReactApplicationContext();
                    params = Arguments.createMap();
                    params.putString("stateLabel", message);
                    sendEvent(reactContext, "ScanJobListenerErrorUpdated", params);
                }

                return;
            }

            // set application state to normal
            setAppState(MainActivity.class.getName(), SmartSDKApplication.APP_STATE_NORMAL, SmartSDKApplication.APP_STATE_NORMAL_MSG, SmartSDKApplication.APP_TYPE_SCANNER);

            // Unknown job stop reason
//            mStateMachine.procScanEvent(ScanEvent.CHANGE_JOB_STATE_STOPPED_OTHER, "(unknown reason)");

            ReactApplicationContext reactContext = getReactApplicationContext();
            WritableMap params = Arguments.createMap();
            params.putString("stateLabel", "CHANGE_JOB_STATE_STOPPED_OTHER (unknown reason)");
            sendEvent(reactContext, "ScanJobStateUpdated", params);
        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    private byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }
}