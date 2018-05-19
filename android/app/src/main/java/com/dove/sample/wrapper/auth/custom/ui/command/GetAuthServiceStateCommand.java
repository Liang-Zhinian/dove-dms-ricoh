/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.custom.ui.command;

import jp.co.ricoh.isdk.sdkservice.auth.custom.ui.intent.Constants;
import com.dove.sample.wrapper.auth.custom.ui.data.AuthServiceState;
import com.dove.sample.wrapper.common.SsdkCommand;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class GetAuthServiceStateCommand implements SsdkCommand {
	private static String TAG = "AuthWrap";
	private static final String CMD_PERMISSION = "jp.co.ricoh.isdk.sdkservice.auth.CUSTOM_UI_CMD_PERMISSION";
	private final ResultReceiver mResultReceiver;

	/**
	 * Result receiver
	 */
	public interface ResultReceiver {
		void onReceiveResult(boolean result, AuthServiceState state);
	}

	/**
	 * Constructor
	 * @param receiver result receiver
	 */
	public GetAuthServiceStateCommand(ResultReceiver receiver) {
		this.mResultReceiver = receiver;
	}

	@Override
	public void execute(Context context) {
		BroadcastReceiver receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Bundle extras = getResultExtras(true);
				boolean result = extras.getBoolean(Constants.EXTRA_RESULT, false);
				AuthServiceState state = AuthServiceState.buildFromExtras(extras);
				mResultReceiver.onReceiveResult(result, state);
			}
		};

		Intent intent = new Intent(Constants.ACTION_GET_AUTH_SERVICE_STATE);
		Log.i(TAG, "send(" + intent.getAction() + ")");
		context.sendOrderedBroadcast(intent, CMD_PERMISSION, receiver, null, 0, null, null);
	}
}
