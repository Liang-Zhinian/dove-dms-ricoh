/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.app.print.activity;

import android.os.Handler;
import android.widget.TextView;

import com.dove.R;
import com.dove.sample.function.common.SmartSDKApplication;
import com.dove.sample.function.print.attribute.PrintServiceAttributeSet;
import com.dove.sample.function.print.attribute.standard.PrinterOccuredErrorLevel;
import com.dove.sample.function.print.attribute.standard.PrinterState;
import com.dove.sample.function.print.attribute.standard.PrinterStateReasons;
import com.dove.sample.function.print.event.PrintServiceAttributeEvent;
import com.dove.sample.function.print.event.PrintServiceAttributeListener;

/**
 * プリントサービスがポストするイベントを受け取るリスナークラスです。
 * The listener class to monitor scan service attribute changes.
 */
public class SimplePrintServiceAttributeListenerImpl implements PrintServiceAttributeListener {

    /**
     * メインアクテビティへの参照
     * Reference to PrintMainActivity
     */
    SimplePrintMainActivity mActivity;

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
    private volatile  boolean mAlertDialogDisplayed = false;

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

    public SimplePrintServiceAttributeListenerImpl(SimplePrintMainActivity activity, Handler handler){
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
        SmartSDKApplication mApplication = (SmartSDKApplication)mActivity.getApplication();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                PrintServiceAttributeSet attributes = mEvent.getAttributes();
                TextView statusView = (TextView)mActivity.findViewById(R.id.txt_state);
                StringBuilder statusString = new StringBuilder();

                PrinterState state = (PrinterState)attributes.get(PrinterState.class);
                if(state != null) {
                    switch(state){
                        case IDLE:
                            statusString.append(mActivity.getResources().getString(R.string.printer_status_idle));
                            break;
                        case MAINTENANCE:
                            statusString.append(mActivity.getResources().getString(R.string.printer_status_maintenance));
                            break;
                        case PROCESSING:
                            statusString.append(mActivity.getResources().getString(R.string.printer_status_processing));
                            break;
                        case STOPPED:
                            statusString.append(mActivity.getResources().getString(R.string.printer_status_stopped));
                            break;
                        case UNKNOWN:
                            statusString.append(mActivity.getResources().getString(R.string.printer_status_unknown));
                            break;
                        default:
                        	statusString.append(state + " (undefined)");
                        	break;
                    }
                }

                PrinterStateReasons reasons = (PrinterStateReasons)attributes.get(PrinterStateReasons.class);
                if(reasons != null) {
                    statusString.append(": ");
                    statusString.append(reasons.getReasons());
                }

                statusView.setText(statusString.toString());

            }
        });
        
        /**
         * Obtain error level and judge whether display or hide or update warning screen
         */
        PrinterState state = (PrinterState)event.getAttributes().get(PrinterState.class);
        PrinterStateReasons stateReasons = (PrinterStateReasons)event.getAttributes().get(PrinterStateReasons.class);
        PrinterOccuredErrorLevel errorLevel = (PrinterOccuredErrorLevel) event.getAttributes().get(PrinterOccuredErrorLevel.class);
      
        if (PrinterOccuredErrorLevel.ERROR.equals(errorLevel)
                || PrinterOccuredErrorLevel.FATAL_ERROR.equals(errorLevel)) {

            String stateString = mActivity.makeAlertStateString(state);
            String reasonString = mActivity.makeAlertStateReasonString(stateReasons);

            if (mLastErrorLevel == null) {
                // Normal -> Error
                if (mActivity.isForegroundApp(mActivity.getPackageName())) {
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
                    String activityName = mActivity.getTopActivityClassName(mActivity.getPackageName());
                    if (activityName == null) {
                        activityName = SimplePrintMainActivity.class.getName();
                    }
                    mApplication.hideAlertDialog(ALERT_DIALOG_APP_TYPE_PRINTER, activityName);
                    mAlertDialogDisplayed = false;
                }
            }
            mLastErrorLevel = null;
        }

    }
}