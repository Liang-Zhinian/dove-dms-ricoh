/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.custom.logic.event;

import jp.co.ricoh.ssdk.sample.framework.auth.custom.logic.LogicEventReceiver;
import jp.co.ricoh.ssdk.sample.framework.common.SsdkEvent;
import android.content.Context;

class LogicEventReceiverWrapper extends LogicEventReceiver {
	private static final String[] TARGET_ACTIONS = new String[]{ExtAuthReqEvent.ACTION};
	private boolean isRegistered;

	private static LogicEventReceiverWrapper mInstance = new LogicEventReceiverWrapper();
	private LogicEventReceiverWrapper(){};

	static LogicEventReceiverWrapper getInstance(){
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
		if(ExtAuthReqEvent.ACTION.equals(event.getAction())) {
			ExtAuthReqEvent.notifyEventReceived(event);
		}
	}
}
