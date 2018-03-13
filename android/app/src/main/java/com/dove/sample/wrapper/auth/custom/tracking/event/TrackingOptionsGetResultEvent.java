/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.custom.tracking.event;

import android.content.Context;
import jp.co.ricoh.isdk.sdkservice.auth.custom.tracking.intent.Constants;
import jp.co.ricoh.ssdk.sample.framework.common.SsdkEvent;

public class TrackingOptionsGetResultEvent extends SsdkEvent {
	public static final String TAG = "TrackingOptionsGetResultEvent";
	public static final String ACTION = Constants.ACTION_GET_TRACKING_OPTION_RESULT;
	private static TrackingEventListenerSet<EventListener> mListeners = new TrackingEventListenerSet<EventListener>();

	private final String mPackageName;
	private final boolean mPaperLengthModeAva;
	private final boolean mPaperLengthMode;
	private final boolean mPredetectionAvailability;
	private boolean mPredetectionNotify;
	private final boolean mResult;

	public interface EventListener {
		void onReceiveEvent(TrackingOptionsGetResultEvent ev);
	}

	public TrackingOptionsGetResultEvent(SsdkEvent event) {
		super(event.getIntent());
		if (!Constants.ACTION_GET_TRACKING_OPTION_RESULT.equals(getAction())) {
			throw new IllegalArgumentException("unsupported action");
		}
		mPackageName = getExtras().getString(Constants.EXTRA_PACKAGE_NAME);
		mPaperLengthModeAva = getExtras().getBoolean(Constants.EXTRA_PAPER_LENGTH_MODE_AVAILABLE);
		mPaperLengthMode = getExtras().getBoolean(Constants.EXTRA_PAPER_LENGTH_MODE);
		mPredetectionAvailability = getExtras().getBoolean(Constants.EXTRA_PRE_DETECTION_EVENT_AVAILABLE);
		mPredetectionNotify = getExtras().getBoolean(Constants.EXTRA_PRE_DETECTION_EVENT);
		mResult = getExtras().getBoolean(Constants.EXTRA_RESULT);
	}

	/**
	 * Adds the specified listener to the listener list if it is not already present
	 * @param listener listener to add
	 * @return true if the listener is not already contained and added to the list
	 */
	public static boolean addListener(Context context, EventListener listener) {
		return mListeners.addListener(context, listener);
	}

	/**
	 * Removes the specified listener from the listener list
	 * @param listener listener to remove
	 * @return true if the list contained the specified list
	 */
	public static boolean removeListener(EventListener listener) {
		return mListeners.removeListener(listener);
	}

	public String getPackageName() {
		return mPackageName;
	}

	public boolean isPaperLModeAva() {
		return mPaperLengthModeAva;
	}

	public boolean isPaperLengthMode(){
		return mPaperLengthMode;
	}

	public boolean isPreDetectionSupported(){
		return mPredetectionAvailability;
	}

	public boolean isPreDetectionNotify(){
		return mPredetectionNotify;
	}

	public boolean isSuccess(){
		return mResult;
	}


	static void notifyEventReceived(SsdkEvent event) {
		final TrackingOptionsGetResultEvent ev = new TrackingOptionsGetResultEvent(event);
		for(EventListener listener: mListeners.getListeners()) {
			listener.onReceiveEvent(ev);
		}
	}

}
