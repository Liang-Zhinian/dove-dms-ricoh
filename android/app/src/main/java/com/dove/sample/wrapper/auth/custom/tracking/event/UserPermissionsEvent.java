/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.custom.tracking.event;

import java.util.ArrayList;

import jp.co.ricoh.isdk.sdkservice.auth.custom.tracking.Subject;
import jp.co.ricoh.isdk.sdkservice.auth.custom.tracking.intent.Constants;
import jp.co.ricoh.ssdk.sample.framework.common.SsdkEvent;
import android.content.Context;

public class UserPermissionsEvent extends SsdkEvent {
	public static final String ACTION = Constants.ACTION_USER_PERMISSIONS;
	private static TrackingEventListenerSet<EventListener> mListeners = new TrackingEventListenerSet<EventListener>();

	private final int mSubjectCount;
	private final ArrayList<Subject> mSubjects;
	private final boolean mIsLast;

	public interface EventListener {
		void onReceiveEvent(UserPermissionsEvent event);
	}

	@SuppressWarnings("unchecked")
	public UserPermissionsEvent(SsdkEvent event) {
		super(event.getIntent());
		if (!Constants.ACTION_USER_PERMISSIONS.equals(getAction())) {
			throw new IllegalArgumentException("unsupported action");
		}
		mSubjectCount = getExtras().getInt(Constants.EXTRA_SUBJECT_COUNT, 0);
		mSubjects = (ArrayList<Subject>)getExtras().getSerializable(Constants.EXTRA_SUBJECT_LIST);
		mIsLast = getExtras().getBoolean(Constants.EXTRA_LAST_LIST, false);
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

	public int getSubjectCount() {
		return mSubjectCount;
	}

	public ArrayList<Subject> getSubjects() {
		return mSubjects;
	}

	public boolean isLast() {
		return mIsLast;
	}

	static void notifyEventReceived(SsdkEvent event) {
		final UserPermissionsEvent ev = new UserPermissionsEvent(event);
		for(EventListener listener: mListeners.getListeners()) {
			listener.onReceiveEvent(ev);
		}
	}

}
