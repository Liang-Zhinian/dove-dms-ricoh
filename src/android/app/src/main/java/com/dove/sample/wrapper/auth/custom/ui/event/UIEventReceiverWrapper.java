/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.custom.ui.event;

import jp.co.ricoh.ssdk.sample.framework.auth.custom.ui.UIEventReceiver;
import jp.co.ricoh.ssdk.sample.framework.common.SsdkEvent;
import android.content.Context;

class UIEventReceiverWrapper extends UIEventReceiver {
	private static final String[] TARGET_ACTIONS = new String[]{
			AuthButtonPressedEvent.ACTION, AuthServiceStateChangedEvent.ACTION, RestrictDispAuthScreenEvent.ACTION
	};
	private boolean isRegistered;

	private static UIEventReceiverWrapper mInstance = new UIEventReceiverWrapper();
	private UIEventReceiverWrapper(){};

	static UIEventReceiverWrapper getInstance(){
		return  mInstance;
	}

	void notifyListenerAdded(Context context) {
		if (!isRegistered) {
			register(context, TARGET_ACTIONS);
		}
		isRegistered = true;
	}

	@Override
	protected void onReceiveEvent(SsdkEvent event) {
		if (AuthButtonPressedEvent.ACTION.equals(event.getAction())) {
			AuthButtonPressedEvent.notifyEventReceived(event);

		} else if (AuthServiceStateChangedEvent.ACTION.equals(event.getAction())) {
			AuthServiceStateChangedEvent.notifyEventReceived(event);

		} else if (RestrictDispAuthScreenEvent.ACTION.equals(event.getAction())) {
			RestrictDispAuthScreenEvent.notifyEventReceived(event);

		}
	}
}
