/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.custom.tracking.command;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import jp.co.ricoh.isdk.sdkservice.auth.custom.tracking.intent.Constants;
import jp.co.ricoh.ssdk.sample.framework.common.SsdkEvent;
import com.dove.sample.wrapper.auth.custom.tracking.event.TrackingOptionsGetResultEvent;
import com.dove.sample.wrapper.common.SsdkCommand;


public class GetTrackingOptionCommand implements SsdkCommand {
	private static String TAG = "AuthWrap";
	private static final String CMD_PERMISSION = "jp.co.ricoh.isdk.sdkservice.auth.CUSTOM_TRACKING_CMD_PERMISSION";
	private static final String EVENT_PERMISSION = "jp.co.ricoh.isdk.sdkservice.auth.CUSTOM_TRACKING_EVENT_PERMISSION";

	private final TrackingOptionReceiver mTrackingOptionReceiver;
	private final ResultReceiver mResultReceiver;
	private final String mPackageName;

	/**
	 * TrackingoptionReceiver
	 */
	public interface TrackingOptionReceiver {
		void onReceive(TrackingOptionsGetResultEvent event);
	}

	/**
	 * Result Receiver
	 */
	public interface ResultReceiver {
		void onReceiveResult(boolean result);
	}

	/**
	 * Constructor
	 * @param permissionReceiver
	 * @param resultReceiver
	 */
	public GetTrackingOptionCommand(String packageName, TrackingOptionReceiver trackingReceiver, ResultReceiver resultReceiver) {
		this.mTrackingOptionReceiver =trackingReceiver;
		this.mResultReceiver = resultReceiver;
		this.mPackageName = packageName;
	}

	/**
	 * Constructor
	 * @param permissionReceiver
	 */
	public GetTrackingOptionCommand(String packageName,TrackingOptionReceiver trackingOptionReceiver) {
		this(packageName, trackingOptionReceiver, null);
	}

	public void execute(final Context context) {
		final BroadcastReceiver trackingOptReceiver = new TrackingOptionResultReceiver(context);
		IntentFilter filter = new IntentFilter(Constants.ACTION_GET_TRACKING_OPTION_RESULT);
		context.registerReceiver(trackingOptReceiver, filter, EVENT_PERMISSION, null);

		BroadcastReceiver receiver = null;
		if (mResultReceiver!=null) {
			receiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context returnContext, Intent intent) {
					Bundle extras = this.getResultExtras(true);
					mResultReceiver.onReceiveResult(extras.getBoolean(Constants.EXTRA_RESULT, true));
				}
			};
		}
		// Send broadcast
		sendIntent(context, receiver);
	}

	private void sendIntent(Context context, BroadcastReceiver receiver) {

		Intent intent = new Intent(Constants.ACTION_GET_TRACKING_OPTION);
		intent.putExtra(Constants.EXTRA_PACKAGE_NAME, mPackageName);
		Log.i(TAG, "send(" + intent.getAction() + ")");

		if (receiver==null) {
			context.sendOrderedBroadcast(intent, CMD_PERMISSION);
		} else {
			context.sendOrderedBroadcast(intent, CMD_PERMISSION, receiver, null, 0, null, null);
		}
	}

	private class TrackingOptionResultReceiver extends BroadcastReceiver {
		private final Context mContext;

		public TrackingOptionResultReceiver(Context context) {
			this.mContext = context;
		}

		@Override
		public void onReceive(Context returnContext, Intent intent) {
			TrackingOptionsGetResultEvent event = new TrackingOptionsGetResultEvent(new SsdkEvent(intent));

			// notify to the listener
			mTrackingOptionReceiver.onReceive(event);

			//Unregister receiver (used only once)
			mContext.unregisterReceiver(this);
		}
	}

}

