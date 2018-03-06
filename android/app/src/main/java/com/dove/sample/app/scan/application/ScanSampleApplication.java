/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.app.scan.application;


import com.dove.BuildConfig;
import com.dove.RCTFileViewerReactPackage;
import com.dove.RCTRicohScannerReactPackage;
import com.dove.RCTSimpleIntentModuleReactPackage;
import com.facebook.react.ReactApplication;
import com.microsoft.codepush.react.CodePush;
import com.horcrux.svg.SvgPackage;
import com.imagepicker.ImagePickerPackage;
import com.rnfs.RNFSPackage;
import com.RNFetchBlob.RNFetchBlobPackage;
import com.reactlibrary.RNReactNativeDocViewerPackage;
import com.oblador.vectoricons.VectorIconsPackage;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;

import android.os.Handler;

import com.dove.sample.app.scan.Const;
import com.dove.R;
//import com.dove.MainActivity;
import com.dove.sample.app.scan.activity.TopActivity;
import com.dove.sample.app.scan.activity.PreviewActivity;
import com.dove.sample.app.scan.application.ScanStateMachine.ScanEvent;
import com.dove.sample.function.common.SmartSDKApplication;
import com.dove.sample.function.scan.ScanJob;
import com.dove.sample.function.scan.ScanService;
import com.dove.sample.function.scan.attribute.ScanJobAttributeSet;
import com.dove.sample.function.scan.attribute.standard.ScanJobScanningInfo;
import com.dove.sample.function.scan.attribute.standard.ScanJobSendingInfo;
import com.dove.sample.function.scan.attribute.standard.ScanJobState;
import com.dove.sample.function.scan.attribute.standard.ScanJobStateReason;
import com.dove.sample.function.scan.attribute.standard.ScanJobStateReasons;
import com.dove.sample.function.scan.event.ScanJobAttributeEvent;
import com.dove.sample.function.scan.event.ScanJobAttributeListener;
import com.dove.sample.function.scan.event.ScanJobEvent;
import com.dove.sample.function.scan.event.ScanJobListener;
import com.dove.sample.wrapper.common.Utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * スキャンサンプルアプリのアプリケーションクラスです。
 * 設定情報を保持し、スキャンサービスとジョブの管理を行います。
 * Application class of Scan sample application.
 * Saves the setting information and manages scan service and job.
 */
public class ScanSampleApplication extends SmartSDKApplication implements ReactApplication {

    private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {

        @Override
        protected String getJSBundleFile() {
            return CodePush.getJSBundleFile();
        }

        @Override
        public boolean getUseDeveloperSupport() {
            return BuildConfig.DEBUG;
        }

        @Override
        protected List<ReactPackage> getPackages() {
            return Arrays.<ReactPackage>asList(
                    new MainReactPackage(),
                    new CodePush(getResources().getString(R.string.reactNativeCodePush_androidDeploymentKey), getApplicationContext(), BuildConfig.DEBUG),
                    new SvgPackage(),
                    new ImagePickerPackage(),
                    new RNFSPackage(),
                    new RNFetchBlobPackage(),
                    new RNReactNativeDocViewerPackage(),
                    new VectorIconsPackage(),
                    new RCTFileViewerReactPackage(),
                    new RCTRicohScannerReactPackage(),
                    new RCTSimpleIntentModuleReactPackage()
            );
        }

        @Override
        protected String getJSMainModuleName() {
            return "index";
        }
    };

    @Override
    public ReactNativeHost getReactNativeHost() {
        return mReactNativeHost;
    }


    /**
     * 宛先設定
     * Destination setting
     */
	private DestinationSettingDataHolder mDestinationSettingDataHolder;

	/**
	 * スキャン設定
	 * Scan setting
	 */
	private ScanSettingDataHolder mScanSettingDataHolder;

	/**
	 * 蓄積ファイル設定
	 * Storage file setting
	 */
	private StorageSettingDataHolder mStorageSettingDataHolder;

	/**
	 * スキャンサービス
	 * Scan service
	 */
	private ScanService mScanService;

	/**
	 * スキャンジョブ
	 * Scan job
	 */
	private ScanJob mScanJob;

	/**
	 * スキャンジョブ状態変化監視リスナー
	 * Scan job listener
	 */
	private ScanJobListener mScanJobListener;

	/**
	 * スキャンジョブ属性変化監視リスナー
	 * Scan job attribute listener
	 */
	private ScanJobAttributeListener mScanJobAttrListener;

	/**
	 * ステートマシン
	 * Statemachine
	 */
	private ScanStateMachine mStateMachine;

    /**
     * システム状態監視
     * System state monitor
     */
    private SystemStateMonitor mSystemStateMonitor;

    /**
     * 読み取ったページ数
     * Number of pages scanned
     */
    protected int scannedPages;

    /**
     * Dialogの最前面表示状態を保持します。
     * Dialog showing flag
     */
    private boolean mIsWinShowing = false;

    /**
     * Buttonの状態を保持します。
     * Button click flag
     */
    private boolean mIsDialogBtnClicked = false;

    public boolean ismIsDialogBtnClicked() {
        return mIsDialogBtnClicked;
    }

    public void setmIsDialogBtnClicked(boolean mIsDialogBtnClicked) {
        this.mIsDialogBtnClicked = mIsDialogBtnClicked;
    }

    public boolean ismIsWinShowing() {
        return mIsWinShowing;
    }

    public void setmIsWinShowing(boolean mIsWinShowing) {
        this.mIsWinShowing = mIsWinShowing;
    }

    /**
     * 次原稿受付までの最大待ち時間です。
     * 0を指定した場合は、待ち続けます。
     * Maximum waiting time for accept the next page.
     * This timeout value supports "0" which means "keep waiting forever".
     */
    protected int timeOfWaitingNextOriginal = 0;

	@Override
	public void onCreate() {

	    //Register the log tag of ScanSample module which is used in function/wrapper layer
	    System.setProperty("com.dove.sample.log.TAG", Const.TAG);
	    setTagName();
	    Utils.setTagName();

		super.onCreate();
        SoLoader.init(this, /* native exopackage */ false);
		mSystemStateMonitor = new SystemStateMonitor(this);
		mSystemStateMonitor.start();
		init();
	}

	@Override
    public void onTerminate() {
        mSystemStateMonitor.stop();
	    super.onTerminate();
	    mDestinationSettingDataHolder = null;
	    mScanSettingDataHolder = null;
	    mStorageSettingDataHolder = null;
	    mScanService = null;
	    mScanJob = null;
	    mScanJobListener = null;
	    mScanJobAttrListener = null;
	    mStateMachine = null;
	    mSystemStateMonitor = null;
	}

	/**
	 * リソースを初期化します。
	 * Initializes the resources.
	 */
	public void init() {
        mDestinationSettingDataHolder = new DestinationSettingDataHolder();
        mScanSettingDataHolder = new ScanSettingDataHolder();
        mStorageSettingDataHolder = new StorageSettingDataHolder();
        mStateMachine = new ScanStateMachine(this, new Handler());
        mScanService = ScanService.getService();
        initJobSetting();
	}

	/**
	 * スキャンジョブを初期化します。
	 * Initializes the scan job.
	 */
	public void initJobSetting() {
	    //If a state change listener is registered to the current scan job, the listener is removed.
	    if(mScanJob!=null) {
    	    if(mScanJobListener!=null) {
    	        mScanJob.removeScanJobListener(mScanJobListener);
    	    }
    	    if(mScanJobAttrListener!=null) {
    	        mScanJob.removeScanJobAttributeListener(mScanJobAttrListener);
    	    }
	    }

        mScanJob = new ScanJob();
        mScanJobListener = new ScanJobListenerImpl();
        mScanJobAttrListener = new ScanJobAttributeListenerImpl();

        //Registers a new listener to the new scan job.
        mScanJob.addScanJobListener(mScanJobListener);
        mScanJob.addScanJobAttributeListener(mScanJobAttrListener);

	}

    /**
     * スキャンジョブの属性変化監視リスナー実装クラスです。
     * [処理内容]
     *    (1)読み取り情報があれば、ステートマシンに通知します。
     *    (2)送信情報があれば、ステートマシンに通知します。
     *
     * The class to implement the listener to monitor scan job state changes.
     * [Processes]
     *    (1) If scan information exists, the information is notified to the state machine.
     *    (2) If data transfer information exists, the information is notified to the state machine.
     */
    class ScanJobAttributeListenerImpl implements ScanJobAttributeListener {

        @Override
        public void updateAttributes(ScanJobAttributeEvent attributesEvent) {
            ScanJobAttributeSet attributes = attributesEvent.getUpdateAttributes();
            mStateMachine.procScanEvent(ScanEvent.UPDATE_JOB_STATE_PROCESSING, attributes);

            //(1)
            ScanJobScanningInfo scanningInfo = (ScanJobScanningInfo) attributes.get(ScanJobScanningInfo.class);
            if (scanningInfo != null && scanningInfo.getScanningState()==ScanJobState.PROCESSING) {
                String status = getString(R.string.txid_scan_d_scanning) + " "
                        + String.format(getString(R.string.txid_scan_d_count), scanningInfo.getScannedCount());
                scannedPages = Integer.valueOf(scanningInfo.getScannedCount());
                mStateMachine.procScanEvent(ScanEvent.UPDATE_JOB_STATE_PROCESSING, status);
            }
            if (scanningInfo != null && scanningInfo.getScanningState() == ScanJobState.PROCESSING_STOPPED) {
                timeOfWaitingNextOriginal = Integer.valueOf(scanningInfo.getRemainingTimeOfWaitingNextOriginal());
            }

            //(2)
            ScanJobSendingInfo sendingInfo = (ScanJobSendingInfo) attributes.get(ScanJobSendingInfo.class);
            if (sendingInfo != null && sendingInfo.getSendingState()==ScanJobState.PROCESSING) {
                String status = getString(R.string.txid_scan_d_sending);
                mStateMachine.procScanEvent(ScanEvent.UPDATE_JOB_STATE_PROCESSING, status);
            }
        }

    }

    /**
     * スキャンジョブの状態監視リスナーです。
     * The listener to monitor scan job state changes.
     */
    class ScanJobListenerImpl  implements ScanJobListener {

		@Override
		public void jobCanceled(ScanJobEvent event) {
		    // set application state to normal
		    setAppState(TopActivity.class.getName(), SmartSDKApplication.APP_STATE_NORMAL, SmartSDKApplication.APP_STATE_NORMAL_MSG, SmartSDKApplication.APP_TYPE_SCANNER);

		    mStateMachine.procScanEvent(ScanEvent.CHANGE_JOB_STATE_CANCELED);
		}

		@Override
		public void jobCompleted(ScanJobEvent event) {
		    // set application state to normal
            setAppState(TopActivity.class.getName(), SmartSDKApplication.APP_STATE_NORMAL, SmartSDKApplication.APP_STATE_NORMAL_MSG, SmartSDKApplication.APP_TYPE_SCANNER);

            mStateMachine.procScanEvent(ScanEvent.CHANGE_JOB_STATE_COMPLETED);
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
                while(iter.hasNext()){
                    jobStateReason = iter.next();
                    switch(jobStateReason){
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
                        default :
                            isError = false;
                            break;
                    }
                    if(isError){
                        break;
                    }
                }
            }
            if(isError){
                // set application state to error
                setAppState(TopActivity.class.getName(), SmartSDKApplication.APP_STATE_ERROR, jobStateReason.toString(), SmartSDKApplication.APP_TYPE_SCANNER);
            }
            else{
                // set application state to normal
                setAppState(TopActivity.class.getName(), SmartSDKApplication.APP_STATE_NORMAL, SmartSDKApplication.APP_STATE_NORMAL_MSG, SmartSDKApplication.APP_TYPE_SCANNER);
            }

			mStateMachine.procScanEvent(ScanEvent.CHANGE_JOB_STATE_ABORTED, attributes);
		}

		@Override
		public void jobProcessing(ScanJobEvent event) {
		    // set application state to normal
            setAppState(TopActivity.class.getName(), SmartSDKApplication.APP_STATE_NORMAL, SmartSDKApplication.APP_STATE_NORMAL_MSG, SmartSDKApplication.APP_TYPE_SCANNER);

            ScanJobAttributeSet attributes = event.getAttributeSet();
			mStateMachine.procScanEvent(ScanEvent.CHANGE_JOB_STATE_PROCESSING, attributes);
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
                while(iter.hasNext()){
                    jobStateReason = iter.next();
                    switch(jobStateReason){
                        case RESOURCES_ARE_NOT_READY:
                            isError = true;
                            break;
                        default :
                            isError = false;
                            break;
                    }
                    if(isError){
                        break;
                    }
                }
            }
            if(isError){
                // set application state to error
                setAppState(TopActivity.class.getName(), SmartSDKApplication.APP_STATE_ERROR, jobStateReason.toString(), SmartSDKApplication.APP_TYPE_SCANNER);
            }
            else{
                // set application state to normal
                setAppState(TopActivity.class.getName(), SmartSDKApplication.APP_STATE_NORMAL, SmartSDKApplication.APP_STATE_NORMAL_MSG, SmartSDKApplication.APP_TYPE_SCANNER);
            }

            mStateMachine.procScanEvent(ScanEvent.CHANGE_JOB_STATE_PENDING);
		}

		/**
         * ジョブが一時停止状態になった際に呼び出されます。
         * 状態の理由が複数ある場合は、最初の１つのみを参照します。
         * [処理内容]
         *   (1)原稿ガラススキャン時、次原稿待ちの一時停止イベントだった場合
         *      -ステートマシンに次原稿待ちイベントを送信します。
         *   (2)プレビュー表示のための一時停止イベントだった場合
         *      -ステートマシンにプレビュー表示のイベントを送信します。
         *   (3)その他の理由による一時停止イベントだった場合
         *      -ここでは、ジョブをキャンセルします。
         *
		 * Called when the job is in paused state.
		 * If multiple reasons exist, only the first reason is checked.
		 * [Processes]
		 *   (1) For the pause event for waiting for the next document when using exposure glass
		 *        - Sends the document waiting event to the state machine.
		 *   (2) For the pause event for preview display
		 *        - Sends the preview display event to the state machine.
		 *   (3) For the pause event for other reasons
		 *        - The job is cancelled in this case.
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
                while(iter.hasNext()){
                    jobStateReason = iter.next();
                    switch(jobStateReason){
                        case SCANNER_JAM:
                        case MEMORY_OVER:
                        case EXCEEDED_MAX_EMAIL_SIZE:
                        case EXCEEDED_MAX_PAGE_COUNT:
                            isError = true;
                            break;
                        default :
                            isError = false;
                            break;
                    }
                    if(isError){
                        break;
                    }
                }
                if(isError){
                    // set application state to error
                    setAppState(TopActivity.class.getName(), SmartSDKApplication.APP_STATE_ERROR, jobStateReason.toString(), SmartSDKApplication.APP_TYPE_SCANNER);
                }
                else{
                    // set application state to normal
                    if(reasonSet.contains(ScanJobStateReason.WAIT_FOR_ORIGINAL_PREVIEW_OPERATION)) {
                        setAppState(PreviewActivity.class.getName(), SmartSDKApplication.APP_STATE_NORMAL, SmartSDKApplication.APP_STATE_NORMAL_MSG, SmartSDKApplication.APP_TYPE_SCANNER);
                    }
                    else{
                        setAppState(TopActivity.class.getName(), SmartSDKApplication.APP_STATE_NORMAL, SmartSDKApplication.APP_STATE_NORMAL_MSG, SmartSDKApplication.APP_TYPE_SCANNER);
                    }
                }

                // show Preview window
                if (reasonSet.contains(ScanJobStateReason.WAIT_FOR_ORIGINAL_PREVIEW_OPERATION)) {
                    mStateMachine.procScanEvent(ScanEvent.CHANGE_JOB_STATE_STOPPED_PREVIEW);
                    return;
                }

                // show CountDown window
                if (reasonSet.contains(ScanJobStateReason.WAIT_FOR_NEXT_ORIGINAL_AND_CONTINUE)) {
                    mStateMachine.procScanEvent(ScanEvent.CHANGE_JOB_STATE_STOPPED_COUNTDOWN);
                    return;
                }

                // show Other reason job stopped window
                StringBuilder sb = new StringBuilder();
                for (ScanJobStateReason reason : reasonSet) {
                    sb.append(reason.toString()).append("\n");
                }

                mStateMachine.procScanEvent(ScanEvent.CHANGE_JOB_STATE_STOPPED_OTHER, sb.toString());
                return;
            }

            // set application state to normal
            setAppState(TopActivity.class.getName(), SmartSDKApplication.APP_STATE_NORMAL, SmartSDKApplication.APP_STATE_NORMAL_MSG, SmartSDKApplication.APP_TYPE_SCANNER);

            // Unknown job stop reason
            mStateMachine.procScanEvent(ScanEvent.CHANGE_JOB_STATE_STOPPED_OTHER, "(unknown reason)");
        }
    }

    /**
     * スキャンサービスを取得します。
     * obtains the scan service
     */
    public ScanService getScanService() {
        return mScanService;
    }

    /**
     * スキャンジョブを取得します。
     * Obtains the scan job.
     */
    public ScanJob getScanJob() {
        return mScanJob;
    }

	/**
	 * 宛先のデータ保持クラスのインスタンスを取得します。
	 * Obtains the instance for the class to save destination data.
	 */
	public DestinationSettingDataHolder getDestinationSettingDataHolder() {
		return mDestinationSettingDataHolder;
	}

    /**
     * スキャンジョブの設定データ保持クラスのインスタンスを取得します。
     * Obtains the instance for the class to save scan setting data.
     */
	public ScanSettingDataHolder getScanSettingDataHolder() {
	    return mScanSettingDataHolder;
	}

	/**
	 * 蓄積ファイル設定データ保持クラスのインスタンスを取得します。
	 * Obtains the instance for the class to save storage setting.
	 */
	public StorageSettingDataHolder getStorageSettingDataHolder() {
	    return mStorageSettingDataHolder;
	}

	/**
	 * このアプリのステートマシンを取得します。
	 * Obtains the statemachine.
	 */
	public ScanStateMachine getStateMachine() {
		return mStateMachine;
	}

    /**
     * システム状態監視クラスのインスタンスを取得します。
     * Obtains the instance for the class to system state monitor.
     */
    public SystemStateMonitor getSystemStateMonitor() {
        return mSystemStateMonitor;
    }

    /**
     * スキャンページ総数を取得します。
     * Obtains the number of total pages scanned.
     */
	public int getScannedPages() {
	    return scannedPages;
	}

	/**
	 * 次原稿までの最大待ち時間を取得します。
	 * Obtains the maximum waiting time.
	 * @return
	 */
	public int getTimeOfWaitingNextOriginal() {
        return timeOfWaitingNextOriginal;
    }


}
