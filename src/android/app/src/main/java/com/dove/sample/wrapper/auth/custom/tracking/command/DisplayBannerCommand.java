/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.custom.tracking.command;

import jp.co.ricoh.isdk.sdkservice.auth.custom.tracking.intent.Constants;
import com.dove.sample.wrapper.common.SsdkCommand;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * A command to display the usage of the login user on the banner of the machine panel<br>
 * Usage information is not displayed on the banner when the user is logged out.
 */
public class DisplayBannerCommand implements SsdkCommand {
	private static String TAG = "AuthWrap";
	private static final String CMD_PERMISSION = "jp.co.ricoh.isdk.sdkservice.auth.CUSTOM_TRACKING_CMD_PERMISSION";

	private final int mUsed;
	private final int mMax;
	private final ResultReceiver mResultReceiver;

	/**
	 * Result receiver
	 */
	public interface ResultReceiver {
		void onReceiveResult(boolean result);
	}

	/**
	 * Creates command
	 * @param used current usage
	 * @param max maximum available usage
	 */
	public DisplayBannerCommand(int used, int max){
		this(used, max, null);
	}

	/**
	 * Create command
	 * @param used current usage
	 * @param max maximum available usage
	 * @param receiver result receiver
	 */
	public DisplayBannerCommand(int used, int max, ResultReceiver receiver) {
		this.mUsed = used;
		this.mMax = max;
		this.mResultReceiver = receiver;
	}

	@Override
	public void execute(final Context context) {
		BroadcastReceiver receiver = null;
		if (mResultReceiver!=null) {
			receiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context returnContext, Intent intent) {
					Bundle extras = this.getResultExtras(true);
					mResultReceiver.onReceiveResult(extras.getBoolean(Constants.EXTRA_RESULT, false));
				}
			};
		}
		// Send broadcast
		sendIntent(context, receiver);
	}

	private void sendIntent(Context context, BroadcastReceiver receiver) {
		Intent intent = new Intent(Constants.ACTION_DISPLAY_BANNER);
		intent.putExtra(Constants.EXTRA_USED, mUsed);
		intent.putExtra(Constants.EXTRA_MAX, mMax);
		Log.i(TAG, "send(" + intent.getAction() + ")");
		if (receiver==null) {
			context.sendOrderedBroadcast(intent, CMD_PERMISSION);
		} else {
			context.sendOrderedBroadcast(intent, CMD_PERMISSION, receiver, null, 0, null, null);
		}
	}
}
