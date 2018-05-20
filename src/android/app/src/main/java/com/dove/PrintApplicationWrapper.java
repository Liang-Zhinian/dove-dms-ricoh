
package com.dove;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.util.Log;

import com.dove.sample.app.print.Const;
import com.dove.sample.app.print.application.PrintSettingDataHolder;
import com.dove.sample.app.print.application.PrintSettingSupportedHolder;
//import com.dove.sample.app.print.application.PrintStateMachine;
import com.dove.sample.app.print.application.SystemStateMonitor;
import com.dove.sample.function.common.SmartSDKApplication;
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
import com.dove.sample.wrapper.common.Utils;

import java.util.HashMap;
import java.util.Map;

public class PrintApplicationWrapper implements IPrintApplication {

    private SmartSDKApplication mApplication;
    private PrintService mPrintService;
    private PrintJob mPrintJob;
    private PrintJobAttributeListener mJobAttributeListener;

    private SimplePrintStateMachine mStateMachine;

    private Map<PrintFile.PDL, PrintSettingSupportedHolder> mSettingDataHolders;

    private SystemStateMonitor mSystemStateMonitor;

    private boolean mIsWinShowing = false;

    public boolean ismIsWinShowing() {
        return mIsWinShowing;
    }

    private final static String PREFIX = "application:PrintSApp:";

    public PrintApplicationWrapper(SmartSDKApplication app) {
        mApplication = app;
        onCreate();
    }

    public void onCreate() {

        //Register the log tag of PrintSample module which is used in function/wrapper layer
        System.setProperty("jp.co.ricoh.ssdk.sample.log.TAG", Const.TAG);
        mApplication.setTagName();
        Utils.setTagName();

        mStateMachine = new SimplePrintStateMachine(this, new Handler());

        mSettingDataHolders = new HashMap<PrintFile.PDL, PrintSettingSupportedHolder>();

        mSystemStateMonitor = new SystemStateMonitor(mApplication);
        mSystemStateMonitor.start();

        mPrintService = PrintService.getService();
    }

    public void onTerminate(){
        mSystemStateMonitor.stop();
        mSystemStateMonitor = null;
    }

    @Override
    public SmartSDKApplication getSSDKApplication() {
        return mApplication;
    }

    @Override
    public int getTimeOfWaitingNextOriginal() {
        return 0;
    }

    @Override
    public void setAppState(String activityName, int state, String errMessage, int appType) {
        mApplication.setAppState(activityName, state, errMessage, appType);
    }

    @Override
    public void initJobSetting() {

    }

    @Override
    public void setmIsWinShowing(boolean mIsWinShowing) {
        this.mIsWinShowing = mIsWinShowing;
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
        initialJob();
        return mPrintJob;
    }

    @Override
    public String getPackageName() {
        return mApplication.getPackageName();
    }

    @Override
    public void sendBroadcast(Intent intent) {
        mApplication.sendBroadcast(intent);
    }

    @Override
    public SimplePrintStateMachine getStateMachine() {
        return mStateMachine;
    }

    @Override
    public Resources getResources() {
        return mApplication.getResources();
    }

    /***************************************************************************************/

    public PrintService getPrintService() {
        return mPrintService;
    }

    private void initialJob() {
        if( mPrintJob != null) {
            if(mJobAttributeListener != null) {
                mPrintJob.removePrintJobAttributeListener(mJobAttributeListener);
            }
        }

        mPrintJob = new PrintJob();
        mJobAttributeListener = new PrintJobAttributeListenerImpl();

        mPrintJob.addPrintJobAttributeListener(mJobAttributeListener);

    }

    public Map<PrintFile.PDL, PrintSettingSupportedHolder> getSettingSupportedDataHolders() {
        return this.mSettingDataHolders;
    }

    public SystemStateMonitor getSystemStateMonitor() {
        return mSystemStateMonitor;
    }

    public void putPrintSettingSupportedHolder(PrintFile.PDL pdl, PrintSettingSupportedHolder holder) {
        mSettingDataHolders.put(pdl, holder);
    }

    public void startPrint(PrintSettingDataHolder holder) {
        if(holder == null) return;

        Log.d(Const.TAG, PREFIX + "startPrint");
        mStateMachine.procPrintEvent(SimplePrintStateMachine.PrintEvent.CHANGE_JOB_STATE_INITIAL, holder);
        mStateMachine.procPrintEvent(SimplePrintStateMachine.PrintEvent.CHANGE_JOB_STATE_PRE_PROCESS, holder);
    }

    /**
     * ジョブ状態を監視するリスナークラスです。
     * The listener class to monitor scan job state changes.
     */
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

            PrintJobState jobState = (PrintJobState)attributeSet.get(PrintJobState.class);
            Log.i(Const.TAG, PREFIX + "JobState[" + jobState + "]");
            if(jobState == null) return;

            switch (jobState) {
                case PENDING:
                    mStateMachine.procPrintEvent(
                            SimplePrintStateMachine.PrintEvent.CHANGE_JOB_STATE_PENDING, null);
                    break;
                case PROCESSING:
                    PrintJobPrintingInfo printingInfo = (PrintJobPrintingInfo)attributeSet.get(
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
