/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.custom.tracking.event;

import jp.co.ricoh.isdk.sdkservice.auth.custom.tracking.intent.Constants;
import jp.co.ricoh.ssdk.sample.framework.common.SsdkEvent;
import android.content.Context;
import android.os.Bundle;

public class UserInfoChangedEvent extends SsdkEvent {
	public static final String ACTION = Constants.ACTION_CHANGE_USER_ID_EVENT;
	private static TrackingEventListenerSet<EventListener> mListeners = new TrackingEventListenerSet<EventListener>();

	public enum Kind {
		ADDED,
		CHANGED,
		DELETED
	}
	private final Kind mKind;
	private final String mTargetUserId;
	private final String mModifiedUserId;

	/**
	 * Event listener
	 */
	public interface EventListener {
		void onReceiveEvent(UserInfoChangedEvent event);
	}

	public UserInfoChangedEvent(SsdkEvent event) {
		super(event.getIntent());
		if (!ACTION.equals(getAction())) {
			throw new IllegalArgumentException("unsupported action");
		}
		Bundle extras = getExtras();
		int kind = extras.getInt(Constants.EXTRA_KIND);
		String targetUserID =  extras.getString(Constants.EXTRA_TARGET_USER_ID);
		String modifiedUserID = extras.getString(Constants.EXTRA_MODIFIED_USER_ID);

		Kind type = null;
		if (kind == 0) {
			type = Kind.ADDED;
		} else if (kind == 1) {
			type = Kind.CHANGED;
		} else if (kind == 2) {
			type = Kind.DELETED;
		}

		this.mKind = type;
		this.mTargetUserId = targetUserID;
		this.mModifiedUserId = modifiedUserID;
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

	public Kind getKind() {
		return mKind;
	}

	public String getTargetUserId() {
		return mTargetUserId;
	}

	public String getModifiedUserId() {
		return mModifiedUserId;
	}

	static void notifyEventReceived(SsdkEvent event) {
		final UserInfoChangedEvent ev = new UserInfoChangedEvent(event);
		for(EventListener listener: mListeners.getListeners()) {
			listener.onReceiveEvent(ev);
		}
	}

}
