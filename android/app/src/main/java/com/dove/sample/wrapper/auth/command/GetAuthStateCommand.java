/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.command;

import jp.co.ricoh.isdk.sdkservice.auth.intent.Constants;
import com.dove.sample.wrapper.auth.data.AuthState;
import com.dove.sample.wrapper.common.SsdkCommand;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class GetAuthStateCommand implements SsdkCommand {
	private static String TAG = "AuthWrap";
	private static final String CMD_PERMISSION = "jp.co.ricoh.isdk.sdkservice.common.SdkService.APP_CMD_PERMISSION";

	private final ResultReceiver mResultReceiver;

	/**
	 * Result Receiver
	 */
	public interface ResultReceiver {
		void onReceiveResult(AuthState authState);
	}

	/**
	 * Constructor
	 * @param receiver result receiver
	 */
	public GetAuthStateCommand(ResultReceiver receiver) {
		this.mResultReceiver = receiver;
	}

	@Override
	public void execute(final Context context) {
		// Result receiver
		BroadcastReceiver receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Bundle extras = getResultExtras(true);
				AuthState authState = AuthState.buildFromExtras(extras);
				mResultReceiver.onReceiveResult(authState);
			}
		};

		// Send broadcast
		sendIntent(context, receiver);
	}

	private void sendIntent(Context context, BroadcastReceiver receiver) {
		Intent intent = new Intent(Constants.ACTION_GET_AUTH_STATE);
		Log.i(TAG, "send(" + intent.getAction() + ")");
		context.sendOrderedBroadcast(intent, CMD_PERMISSION, receiver, null, 0, null, null);
	}
}
