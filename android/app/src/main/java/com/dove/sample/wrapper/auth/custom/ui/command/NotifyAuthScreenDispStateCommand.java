/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.custom.ui.command;

import jp.co.ricoh.isdk.sdkservice.auth.custom.ui.intent.Constants;
import com.dove.sample.wrapper.common.SsdkCommand;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotifyAuthScreenDispStateCommand implements SsdkCommand {
	private static String TAG = "AuthWrap";
	private static final String CMD_PERMISSION = "jp.co.ricoh.isdk.sdkservice.auth.CUSTOM_UI_CMD_PERMISSION";
	private final boolean mDispState;

	/**
	 * Constructor
	 * @param dispState true if screen is displayed and false otherwise
	 */
	public NotifyAuthScreenDispStateCommand(boolean dispState) {
		mDispState = dispState;
	}

	@Override
	public void execute(Context context) {
		Intent intent = new Intent(Constants.ACTION_NOTIFY_AUTH_SCREEN_DISP_STATE);
		intent.putExtra(Constants.EXTRA_DISP_STATE,mDispState);
		Log.i(TAG, "send(" + intent.getAction() + ") dispState:" + mDispState);
		context.sendBroadcast(intent, CMD_PERMISSION);
	}

}
