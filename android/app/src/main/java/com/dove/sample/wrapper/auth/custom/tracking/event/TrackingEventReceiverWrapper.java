/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.custom.tracking.event;

import android.content.Context;
import jp.co.ricoh.ssdk.sample.framework.auth.custom.tracking.TrackingEventReceiver;
import jp.co.ricoh.ssdk.sample.framework.common.SsdkEvent;

class TrackingEventReceiverWrapper extends TrackingEventReceiver{
	private static final String[] TARGET_ACTIONS = new String[]{
			JobEventDetectedEvent.ACTION, UserInfoChangedEvent.ACTION, UserPermissionsEvent.ACTION,
			JobEventPreDetectedEvent.ACTION, TrackingOptionsSetResultEvent.ACTION, TrackingOptionsGetResultEvent.ACTION};

	private boolean isRegistered;

	private static TrackingEventReceiverWrapper mInstance = new TrackingEventReceiverWrapper();
	private TrackingEventReceiverWrapper(){};

	static TrackingEventReceiverWrapper getInstance(){
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
		if(JobEventDetectedEvent.ACTION.equals(event.getAction())) {
			JobEventDetectedEvent.notifyEventReceived(event);

		} else if (UserInfoChangedEvent.ACTION.equals(event.getAction())) {
			UserInfoChangedEvent.notifyEventReceived(event);

		} else if (UserPermissionsEvent.ACTION.equals(event.getAction())) {
			UserPermissionsEvent.notifyEventReceived(event);

		}else if (JobEventPreDetectedEvent.ACTION.equals(event.getAction())){
			JobEventPreDetectedEvent.notifyEventReceived(event);

		}else if (TrackingOptionsSetResultEvent.ACTION.equals(event.getAction())){
			TrackingOptionsSetResultEvent.notifyEventReceived(event);

		}else if (TrackingOptionsGetResultEvent.ACTION.equals(event.getAction())){
			TrackingOptionsGetResultEvent.notifyEventReceived(event);
		}
	}
}

