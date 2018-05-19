/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.custom.ui.command;

import jp.co.ricoh.isdk.sdkservice.auth.custom.ui.intent.Constants;
import com.dove.sample.wrapper.common.SsdkCommand;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * Login cancel command.
 */
public class LoginCancelRequestCommand implements SsdkCommand {
	private static String TAG = "AuthWrap";
	private static final String EVENT_PERMISSION = "jp.co.ricoh.isdk.sdkservice.auth.CUSTOM_UI_EVENT_PERMISSION";
	private static final String CMD_PERMISSION = "jp.co.ricoh.isdk.sdkservice.auth.CUSTOM_UI_CMD_PERMISSION";

	private final ResultReceiver mResultReceiver;

	public interface ResultReceiver {
		void onReceiveResult(boolean result);
	}

	/**
	 * Creates command
	 */
	public LoginCancelRequestCommand() {
		this.mResultReceiver = null;
	}

	/**
	 * Creates command
	 * @param receiver Result listener
	 */
	public LoginCancelRequestCommand(ResultReceiver receiver) {
		this.mResultReceiver = receiver;
	}

	@Override
	public void execute(final Context context) {
		// Register result receiver
		if (mResultReceiver !=null) {
			BroadcastReceiver receiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context returnContext, Intent intent) {
					mResultReceiver.onReceiveResult(intent.getBooleanExtra(Constants.EXTRA_RESULT, false));
					context.unregisterReceiver(this);
				}
			};
			IntentFilter filter = new IntentFilter(Constants.ACTION_SYS_RES_CANCEL_LOGIN);
			context.registerReceiver(receiver, filter, EVENT_PERMISSION, null);
		}

		// Send broadcast
		sendIntent(context);
	}

	private void sendIntent(Context context) {
		Intent intent = new Intent(Constants.ACTION_REQ_CANCEL_LOGIN);
		Log.i(TAG, "send(" + intent.getAction() + ")");
		context.sendBroadcast(intent, CMD_PERMISSION);
	}
}
