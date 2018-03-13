/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.custom.ui.event;

import jp.co.ricoh.isdk.sdkservice.auth.custom.ui.intent.Constants;
import jp.co.ricoh.ssdk.sample.framework.common.SsdkEvent;
import android.content.Context;

public class RestrictDispAuthScreenEvent extends SsdkEvent {
	public static final String ACTION = Constants.ACTION_SYS_NOTIFY_RESTRICT_DISP_AUTH_SCREEN;
	private static UIEventListenerSet<EventListener> mListeners = new UIEventListenerSet<EventListener>();

	private final boolean restrict;
	private final boolean oneshotAuthScreenRestrict;

	/**
	 * Event Listener
	 */
	public interface EventListener {
		void onReceiveEvent(RestrictDispAuthScreenEvent event);
	}

	private RestrictDispAuthScreenEvent(SsdkEvent event) {
		super(event.getIntent());
		if (!ACTION.equals(getAction())) {
			throw new IllegalArgumentException("unsupported action");
		} else {
			this.restrict = getExtras().getBoolean(Constants.EXTRA_RESTRICT, false);
			this.oneshotAuthScreenRestrict = getExtras().getBoolean(Constants.EXTRA_ONESHOT_AUTH_SCREEN_RESTRICT, false);
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
	 * Obtains whether the authentication screen display needs to be restricted
	 * @return true if authentication screen display is restricted, false otherwise
	 */
	public boolean isRestrict() {
		return restrict;
	}

	/**
	 * Obtains whether the oneshot authentication screen display needs to be restricted
	 * @return true if oneshot authentication screen display is restricted, false otherwise
	 */
	public boolean isOneshotAuthScreenRestrict() {
		return oneshotAuthScreenRestrict;
	}

	static void notifyEventReceived(SsdkEvent event) {
		final RestrictDispAuthScreenEvent ev = new RestrictDispAuthScreenEvent(event);
		for(EventListener listener: mListeners.getListeners()) {
			listener.onReceiveEvent(ev);
		}
	}
}
