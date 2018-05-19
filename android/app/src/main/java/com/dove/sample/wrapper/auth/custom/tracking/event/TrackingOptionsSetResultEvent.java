/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.custom.tracking.event;

import android.content.Context;
import jp.co.ricoh.isdk.sdkservice.auth.custom.tracking.intent.Constants;
import jp.co.ricoh.ssdk.sample.framework.common.SsdkEvent;

public class TrackingOptionsSetResultEvent extends SsdkEvent {
	public static final String ACTION = Constants.ACTION_SET_TRACKING_OPTION_RESULT;
	private static TrackingEventListenerSet<EventListener> mListeners = new TrackingEventListenerSet<EventListener>();

	private final String mPackageName;
	private final boolean mResult;

	public interface EventListener {
		void onReceiveEvent(TrackingOptionsSetResultEvent event);
	}


	public TrackingOptionsSetResultEvent(SsdkEvent event) {
		super(event.getIntent());
		if (!Constants.ACTION_SET_TRACKING_OPTION_RESULT.equals(getAction())) {
			throw new IllegalArgumentException("unsupported action");
		}
		mPackageName = getExtras().getString(Constants.EXTRA_PACKAGE_NAME);
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

	public boolean isSuccess() {
		return mResult;
	}

	static void notifyEventReceived(SsdkEvent event) {
		final TrackingOptionsSetResultEvent ev = new TrackingOptionsSetResultEvent(event);
		for(EventListener listener: mListeners.getListeners()) {
			listener.onReceiveEvent(ev);
		}
	}

}
