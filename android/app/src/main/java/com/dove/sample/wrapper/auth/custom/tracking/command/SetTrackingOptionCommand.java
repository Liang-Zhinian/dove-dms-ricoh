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
import com.dove.sample.wrapper.auth.custom.tracking.event.TrackingOptionsSetResultEvent;
import com.dove.sample.wrapper.common.SsdkCommand;


public class SetTrackingOptionCommand implements SsdkCommand {
	private static String TAG = "AuthWrap";
	private static final String CMD_PERMISSION = "jp.co.ricoh.isdk.sdkservice.auth.CUSTOM_TRACKING_CMD_PERMISSION";
	private static final String EVENT_PERMISSION = "jp.co.ricoh.isdk.sdkservice.auth.CUSTOM_TRACKING_EVENT_PERMISSION";

	private final TrackingOptionReceiver mTrackingOptionReceiver;
	private final ResultReceiver mResultReceiver;
	private final String mPackageName;
	private boolean mPaperLengthMode;
	private boolean mPredetectionNotification;

	/**
	 * TrackingoptionReceiver
	 */
	public interface TrackingOptionReceiver {
		void onReceive(TrackingOptionsSetResultEvent event);
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
	public SetTrackingOptionCommand(String packageName, boolean paperLengthMode, boolean preDetectionNotification,
			TrackingOptionReceiver trackingReceiver, ResultReceiver resultReceiver) {
		this.mPackageName = packageName;
		this.mPaperLengthMode = paperLengthMode;
		this.mPredetectionNotification = preDetectionNotification;
		this.mTrackingOptionReceiver =trackingReceiver;
		this.mResultReceiver = resultReceiver;
	}

	/**
	 * Constructor
	 * @param permissionReceiver
	 */
	public SetTrackingOptionCommand(String packageName,boolean paperLengthMode, boolean preDetectionNotification,
			TrackingOptionReceiver trackingOptionReceiver) {
		this(packageName, paperLengthMode, preDetectionNotification,trackingOptionReceiver,null);
	}

	public void execute(final Context context) {
		final BroadcastReceiver trackingOptReceiver = new TrackingOptionSetReceiver(context);
		IntentFilter filter = new IntentFilter(Constants.ACTION_SET_TRACKING_OPTION_RESULT);
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
		Intent intent = new Intent(Constants.ACTION_SET_TRACKING_OPTION);
		intent.putExtra(Constants.EXTRA_PACKAGE_NAME, mPackageName);
		intent.putExtra(Constants.EXTRA_PAPER_LENGTH_MODE, mPaperLengthMode);
		intent.putExtra(Constants.EXTRA_PRE_DETECTION_EVENT, mPredetectionNotification);
		Log.i(TAG, "send(" + intent.getAction() + ")" );

		if (receiver==null) {
			context.sendOrderedBroadcast(intent, CMD_PERMISSION);
		} else {
			context.sendOrderedBroadcast(intent, CMD_PERMISSION, receiver, null, 0, null, null);
		}
	}

	private class TrackingOptionSetReceiver extends BroadcastReceiver {
		private final Context mContext;

		public TrackingOptionSetReceiver(Context context) {
			this.mContext = context;
		}

		@Override
		public void onReceive(Context returnContext, Intent intent) {
			TrackingOptionsSetResultEvent event = new TrackingOptionsSetResultEvent(new SsdkEvent(intent));

			// notify to the listener
			mTrackingOptionReceiver.onReceive(event);

			//Unregister (used only once)
			mContext.unregisterReceiver(this);
		}
	}

}

