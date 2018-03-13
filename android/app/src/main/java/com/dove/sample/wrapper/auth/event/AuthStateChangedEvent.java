/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.event;

import jp.co.ricoh.isdk.sdkservice.auth.intent.Constants;
import jp.co.ricoh.ssdk.sample.framework.common.SsdkEvent;
import com.dove.sample.wrapper.auth.data.AuthState;
import android.content.Context;

/**
 * Event object of jp.co.ricoh.isdk.sdkservice.auth.SYS_NOTIFY_AUTH_STATE_CHANGED intent
 */
public class AuthStateChangedEvent extends SsdkEvent {
	public static final String ACTION =Constants.ACTION_SYS_NOTIFY_AUTH_STATE_CHANGED;
	private static AuthEventListenerSet<EventListener> mListeners = new AuthEventListenerSet<EventListener>();

	private final AuthState mAuthState;

	/**
	 * Event Listener
	 */
	public interface EventListener {
		void onReceiveEvent(AuthStateChangedEvent event);
	}


	public AuthStateChangedEvent(SsdkEvent event) {
		super(event.getIntent());
		mAuthState = AuthState.buildFromExtras(getExtras());
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
	public static synchronized boolean removeListener(EventListener listener) {
		return mListeners.removeListener(listener);
	}

	public AuthState getAuthState() {
		return mAuthState;
	}

	static synchronized void notifyEventReceived(SsdkEvent event) {
		AuthStateChangedEvent ev = new AuthStateChangedEvent(event);

		for(EventListener listener: mListeners.getListeners()) {
			listener.onReceiveEvent(ev);
		}
	}
}
