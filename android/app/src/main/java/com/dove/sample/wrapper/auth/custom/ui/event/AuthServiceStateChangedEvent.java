/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.custom.ui.event;

import jp.co.ricoh.isdk.sdkservice.auth.custom.ui.intent.Constants;
import jp.co.ricoh.ssdk.sample.framework.common.SsdkEvent;
import com.dove.sample.wrapper.auth.custom.ui.data.AuthServiceState;
import android.content.Context;

/**
 * Auth service state changed event object
 */
public class AuthServiceStateChangedEvent extends SsdkEvent {
	public static final String ACTION = Constants.ACTION_SYS_NOTIFY_AUTH_SERVICE_STATE_CHANGED;
	private static UIEventListenerSet<EventListener> mListeners = new UIEventListenerSet<EventListener>();

    private final AuthServiceState authServiceState;

	public interface EventListener {
		void onReceiveEvent(AuthServiceStateChangedEvent event);
	}

	/**
	 * Create object from SYS_NOTIFY_AUTH_SERVICE_STATE_CHANGED event
	 * @param event SYS_NOTIFY_AUTH_SERVICE_STATE_CHANGED event
	 */
    private AuthServiceStateChangedEvent(SsdkEvent event) {
		super(event.getIntent());
		if (!ACTION.equals(getAction())) {
			throw new IllegalArgumentException("unsupported action");
		} else {
			authServiceState = AuthServiceState.buildFromExtras(getExtras());
		}
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
	 * Gets auth service state object
	 * @return AuthServiceState
	 */
    public AuthServiceState getAuthServiceState() {
        return authServiceState;
    }

	static void notifyEventReceived(SsdkEvent event) {
		final AuthServiceStateChangedEvent ev = new AuthServiceStateChangedEvent(event);
		for(EventListener listener: mListeners.getListeners()) {
			listener.onReceiveEvent(ev);
		}
	}
}
