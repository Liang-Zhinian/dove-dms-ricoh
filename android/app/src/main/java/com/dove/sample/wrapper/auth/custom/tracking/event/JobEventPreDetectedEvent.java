/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.custom.tracking.event;

import android.content.Context;
import jp.co.ricoh.isdk.sdkservice.auth.custom.tracking.DetectionEvent;
import jp.co.ricoh.isdk.sdkservice.auth.custom.tracking.intent.Constants;
import jp.co.ricoh.ssdk.sample.framework.common.SsdkEvent;

public class JobEventPreDetectedEvent extends SsdkEvent {
	public static final String ACTION = Constants.ACTION_PRE_DETECTION_EVENT;

	public static final int COUNT_TYPE_COPY = 0;
	public static final int COUNT_TYPE_SCAN = 1;
	public static final int COUNT_TYPE_PRINT = 2;
	public static final int COUNT_TYPE_FAX_SEND = 3;
	public static final int COUNT_TYPE_FAX_RECEIVE = 4;
	public static final int COUNT_TYPE_STAPLE = 5;

	private static TrackingEventListenerSet<EventListener> mListeners = new TrackingEventListenerSet<EventListener>();

	private final DetectionEvent mPreDetectionEvent;

	public interface EventListener {
		void onReceiveEvent(JobEventPreDetectedEvent event);
	}

	public JobEventPreDetectedEvent(SsdkEvent event) {
		super(event.getIntent());
		if (!ACTION.equals(getAction())) {
			throw new IllegalArgumentException("unsupported action");
		}

		mPreDetectionEvent = (DetectionEvent)getExtras().getSerializable(Constants.EXTRA_PRE_DETECTION_EVENT);
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

	/**
	 * Obtains predetectionEvent
	 * @return PreDetectionEvent
	 */
	public DetectionEvent getPreDetectionEvent() {
		return mPreDetectionEvent;
	}

	static void notifyEventReceived(SsdkEvent event) {
		final JobEventPreDetectedEvent pre_ev = new JobEventPreDetectedEvent(event);
		for(EventListener listener: mListeners.getListeners()) {
			listener.onReceiveEvent(pre_ev);
		}
	}
}
