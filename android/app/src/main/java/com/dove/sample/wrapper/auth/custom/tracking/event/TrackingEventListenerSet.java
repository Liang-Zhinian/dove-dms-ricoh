/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.custom.tracking.event;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;

class TrackingEventListenerSet<T> {
	private final Set<T> mListeners = new HashSet<T>();

	/**
	 * Adds the specified listener to the listener list if it is not already present
	 * @param listener listener to add
	 * @return true if the listener is not already contained and added to the list
	 */
	synchronized boolean addListener(Context context, T listener) {
		if (listener==null) {
			throw new NullPointerException("listener must not be null");
		}
		boolean result = mListeners.add(listener);
		TrackingEventReceiverWrapper.getInstance().notifyListenerAdded(context);

		return result;
	}

	/**
	 * Removes the specified listener from the listener list
	 * @param listener listener to remove
	 * @return true if the list contained the specified list
	 */
	synchronized boolean removeListener (T listener) {
		return mListeners.remove(listener);
	}

	synchronized Set<T> getListeners() {
		return new HashSet<T>(mListeners);
	}
}
