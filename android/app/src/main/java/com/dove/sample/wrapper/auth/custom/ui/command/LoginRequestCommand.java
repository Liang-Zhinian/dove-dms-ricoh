/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.custom.ui.command;

import jp.co.ricoh.isdk.sdkservice.auth.custom.ui.intent.Constants;
import com.dove.sample.wrapper.auth.custom.ui.data.LoginRequestInput;
import com.dove.sample.wrapper.common.SsdkCommand;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * Login command.
 */
public class LoginRequestCommand implements SsdkCommand {
	private static String TAG = "AuthWrap";
	private static final String EVENT_PERMISSION = "jp.co.ricoh.isdk.sdkservice.auth.CUSTOM_UI_EVENT_PERMISSION";
	private static final String CMD_PERMISSION = "jp.co.ricoh.isdk.sdkservice.auth.CUSTOM_UI_CMD_PERMISSION";

	private final LoginRequestInput mInput;
	private final ResultReceiver mResultReceiver;

	public interface ResultReceiver {
		void onReceiveResult(boolean result);
	}

	/**
	 * Creates command
	 * @param input Login request parameter
	 */
	public LoginRequestCommand(LoginRequestInput input){
		this.mInput = input;
		this.mResultReceiver = null;
	}

	/**
	 * Creates command
	 * @param input Login request parameter
	 * @param receiver Result listener
	 */
	public LoginRequestCommand(LoginRequestInput input, ResultReceiver receiver) {
		this.mInput = input;
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
			IntentFilter filter = new IntentFilter(Constants.ACTION_SYS_RES_LOGIN);
			context.registerReceiver(receiver, filter, EVENT_PERMISSION, null);
		}

		// Send broadcast
		sendIntent(context);
	}

	private void sendIntent(Context context) {
		Intent intent = new Intent(Constants.ACTION_REQ_LOGIN);
		intent.putExtra(Constants.EXTRA_USER_ID, mInput.getUserId());
		intent.putExtra(Constants.EXTRA_PASSWORD, mInput.getPassword());
		if (mInput.getExtInfo() != null) {
			intent.putExtra(Constants.EXTRA_EXTENSION_INFO, mInput.getExtInfo());
		}
		if (mInput.getAuthDevice() != null) {
			intent.putExtra(Constants.EXTRA_AUTH_DEVICE, mInput.getAuthDevice().name());
		}
		Log.i(TAG, "send(" + intent.getAction() + ")");
		context.sendBroadcast(intent, CMD_PERMISSION);
	}
}
