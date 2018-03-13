/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.custom.logic.event;

import jp.co.ricoh.isdk.sdkservice.auth.custom.logic.intent.Constants;
import jp.co.ricoh.ssdk.sample.framework.common.SsdkEvent;
import com.dove.sample.wrapper.auth.custom.logic.data.ExtLoginRequest;
import android.content.Context;

public class ExtAuthReqEvent extends SsdkEvent {
	public static final String ACTION = Constants.ACTION_SYS_REQ_EXTERNAL_AUTH;
	private static LogicEventListenerSet<EventListener> mListeners = new LogicEventListenerSet<EventListener>();

	private final ExtLoginRequest mExtLoginRequest;

	public interface EventListener {
		void onReceiveEvent(ExtAuthReqEvent event);
	}

	/**
	 * Creates event from SYS_REQ_EXTERNAL_AUTH intent
	 * @param event SYS_REQ_EXTERNAL_AUTH event
	 */
	public ExtAuthReqEvent(SsdkEvent event) {
		super(event.getIntent());
		if (!ACTION.equals(getAction())) {
			throw new IllegalArgumentException("unsupported action");
		}
		mExtLoginRequest = ExtLoginRequest.buildFromExtras(getExtras());
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

	public ExtLoginRequest getExtLoginRequest() {
		return mExtLoginRequest;
	}

	static void notifyEventReceived(SsdkEvent event) {
		final ExtAuthReqEvent ev = new ExtAuthReqEvent(event);
		for(EventListener listener: mListeners.getListeners()) {
			listener.onReceiveEvent(ev);
		}
	}
}
