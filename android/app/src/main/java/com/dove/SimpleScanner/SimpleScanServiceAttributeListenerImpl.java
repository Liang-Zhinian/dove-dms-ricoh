package com.dove.SimpleScanner;

import android.os.Handler;
import android.util.Log;

//import com.dove.R;
import com.dove.sample.app.scan.Const;
//import com.dove.sample.app.scan.activity.TopActivity;
//import com.dove.sample.function.scan.attribute.standard.OccuredErrorLevel;
import com.dove.sample.function.scan.attribute.standard.ScannerState;
import com.dove.sample.function.scan.attribute.standard.ScannerStateReason;
import com.dove.sample.function.scan.attribute.standard.ScannerStateReasons;
import com.dove.sample.function.scan.event.ScanServiceAttributeEvent;
import com.dove.sample.function.scan.event.ScanServiceAttributeListener;

import java.util.Set;

public class SimpleScanServiceAttributeListenerImpl implements ScanServiceAttributeListener {
    private final static String PREFIX = "activity:MainAct:";

    /**
     * UI thread handler
     */
    private Handler mHandler;

    public Runnable setTextState;

    public SimpleScanServiceAttributeListenerImpl(Handler handler) {
        mHandler = handler;
    }

    @Override
    public void attributeUpdate(final ScanServiceAttributeEvent event) {
        ScannerState state = (ScannerState)event.getAttributes().get(ScannerState.class);
        ScannerStateReasons stateReasons = (ScannerStateReasons)event.getAttributes().get(ScannerStateReasons.class);
//        OccuredErrorLevel errorLevel = (OccuredErrorLevel) event.getAttributes().get(OccuredErrorLevel.class);

        String stateLabel = "";

        //(1)
        switch(state) {
            case IDLE :
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

        if( stateReasons != null ) {
            Set<ScannerStateReason> reasonSet = stateReasons.getReasons();
            for(ScannerStateReason reason : reasonSet) {
                switch(reason) {
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
//                text_state.setText(result);
            }
        });

        //(2)
//        if (OccuredErrorLevel.ERROR.equals(errorLevel)
//                || OccuredErrorLevel.FATAL_ERROR.equals(errorLevel)) {
//
//            String stateString = makeAlertStateString(state);
//            String reasonString = makeAlertStateReasonString(stateReasons);
//
//            if (mLastErrorLevel == null) {
//                // Normal -> Error
//                if (isForegroundApp(getPackageName())) {
//                    mApplication.displayAlertDialog(ALERT_DIALOG_APP_TYPE_SCANNER, stateString, reasonString);
//                    mAlertDialogDisplayed = true;
//                }
//            } else {
//                // Error -> Error
//                if (mAlertDialogDisplayed) {
//                    mApplication.updateAlertDialog(ALERT_DIALOG_APP_TYPE_SCANNER, stateString, reasonString);
//                }
//            }
//            mLastErrorLevel = errorLevel;
//
//        } else {
//            if (mLastErrorLevel != null) {
//                // Error -> Normal
//                if (mAlertDialogDisplayed) {
//                    String activityName = getTopActivityClassName(getPackageName());
//                    if (activityName == null) {
//                        activityName = TopActivity.class.getName();
//                    }
//                    mApplication.hideAlertDialog(ALERT_DIALOG_APP_TYPE_SCANNER, activityName);
//                    mAlertDialogDisplayed = false;
//                }
//            }
//            mLastErrorLevel = null;
//        }
    }
}