/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.event;

import jp.co.ricoh.ssdk.sample.framework.auth.AuthEventReceiver;
import jp.co.ricoh.ssdk.sample.framework.common.SsdkEvent;
import android.content.Context;

class AuthEventReceiverWrapper extends AuthEventReceiver
{
	private static final String[] TARGET_ACTIONS = new String[]{AuthStateChangedEvent.ACTION};
	private boolean isRegistered;

	private static AuthEventReceiverWrapper mInstance = new AuthEventReceiverWrapper();
	private AuthEventReceiverWrapper(){};

	static AuthEventReceiverWrapper getInstance() {
		return mInstance;
	}


	void notifyListenerAdded(Context context) {
		if (!isRegistered) {
			register(context, TARGET_ACTIONS);
		}
		isRegistered = true;
	}

	@Override
	protected void onReceiveEvent(SsdkEvent event) {
		if (AuthStateChangedEvent.ACTION.equals(event.getAction())) {
			AuthStateChangedEvent.notifyEventReceived(event);
		}
	}
}
