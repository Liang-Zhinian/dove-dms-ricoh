package com.dove;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;

import com.dove.sample.app.print.Const;
import com.dove.sample.app.print.application.PrintSettingSupportedHolder;
import com.dove.sample.app.print.application.PrintStateMachine;
import com.dove.sample.app.print.application.SystemStateMonitor;
import com.dove.sample.function.common.SmartSDKApplication;
import com.dove.sample.function.common.impl.AsyncConnectState;
import com.dove.sample.function.print.PrintFile;
import com.dove.sample.function.print.PrintJob;
import com.dove.sample.function.print.PrintService;
import com.dove.sample.function.print.attribute.PrintJobAttributeSet;
import com.dove.sample.function.print.attribute.PrintServiceAttributeSet;
import com.dove.sample.function.print.attribute.standard.PrintJobPrintingInfo;
import com.dove.sample.function.print.attribute.standard.PrintJobState;
import com.dove.sample.function.print.attribute.standard.PrinterStateReasons;
import com.dove.sample.function.print.event.PrintJobAttributeEvent;
import com.dove.sample.function.print.event.PrintJobAttributeListener;
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
    public PrintJob getPrintJob() {
        return mPrintJob;
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

        //Register the log tag of PrintSample module which is used in function/wrapper layer
        System.setProperty("jp.co.ricoh.ssdk.sample.log.TAG", Const.TAG);

        Utils.setTagName();


        mStateMachine = new SimplePrintStateMachine(this, new Handler());

        mSettingDataHolders = new HashMap<PrintFile.PDL, PrintSettingSupportedHolder>();

        mSystemStateMonitor = new SystemStateMonitor(this.getReactApplicationContext());
        mSystemStateMonitor.start();

        mPrintService = PrintService.getService();
    }

    public void onTerminate() {
        mSystemStateMonitor.stop();
        mSystemStateMonitor = null;
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
}
