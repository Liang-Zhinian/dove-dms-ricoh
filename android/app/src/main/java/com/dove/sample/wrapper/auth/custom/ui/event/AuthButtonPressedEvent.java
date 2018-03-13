/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.custom.ui.event;

import jp.co.ricoh.isdk.sdkservice.auth.custom.ui.intent.Constants;
import jp.co.ricoh.ssdk.sample.framework.common.SsdkEvent;
import android.content.Context;

/**
 * Login button pressed event object
 */
public class AuthButtonPressedEvent extends SsdkEvent{
	public static final String ACTION = Constants.ACTION_SYS_NOTIFY_AUTH_BUTTON_PRESSED;
	private static UIEventListenerSet<EventListener> mListeners = new UIEventListenerSet<EventListener>();

	private BUTTON_TYPE buttonType;
	private boolean logoutConfirm;

	/**
	 * Auth button type
	 */
	public enum BUTTON_TYPE {
		LOGIN("login"),
		LOGOUT("logout");

		private final String mName;
		BUTTON_TYPE(String name) {
			this.mName = name;
		}
		private static BUTTON_TYPE getFromName(String name) {
			for(BUTTON_TYPE type: BUTTON_TYPE.values()) {
				if (type.mName.equals(name)) {
					return type;
				}
			}
			return null;
		}
	}

	/**
	 * Event Listener
	 */
	public interface EventListener {
		void onReceiveEvent(AuthButtonPressedEvent event);
	}

	/**
	 * Creates object from SYS_NOTIFY_AUTH_BUTTON_PRESSED event
	 * @param event SYS_NOTIFY_AUTH_BUTTON_PRESSED event
	 */
	public AuthButtonPressedEvent(SsdkEvent event) {
		super(event.getIntent());
		if (!ACTION.equals(getAction())) {
			throw new IllegalArgumentException("unsupported action");
		} else {
			this.buttonType = BUTTON_TYPE.getFromName(getExtras().getString(Constants.EXTRA_BUTTON_TYPE));
			this.logoutConfirm = getExtras().getBoolean(Constants.EXTRA_LOGOUT_CONFIRM);
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
	 * Obtains the button type
	 * @return button type
	 */
	public BUTTON_TYPE getButtonType() {
		return buttonType;
	}

	/**
	 * Obtains whether logout confirming screen is required in the system settings
	 * @return true if logout confirming screen is required in the system settings
	 */
	public boolean isLogoutConfirm() {
		return logoutConfirm;
	}

	static void notifyEventReceived(SsdkEvent event) {
		final AuthButtonPressedEvent ev = new AuthButtonPressedEvent(event);
		for(EventListener listener: mListeners.getListeners()) {
			listener.onReceiveEvent(ev);
		}
	}
}
