/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.custom.tracking.command;

import java.util.ArrayList;

import jp.co.ricoh.isdk.sdkservice.auth.custom.tracking.Subject;
import jp.co.ricoh.isdk.sdkservice.auth.custom.tracking.intent.Constants;
import com.dove.sample.wrapper.common.SsdkCommand;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SetUserPermissionsCommand implements SsdkCommand {
	private static String TAG = "AuthWrap";
	private static final String CMD_PERMISSION = "jp.co.ricoh.isdk.sdkservice.auth.CUSTOM_TRACKING_CMD_PERMISSION";

	private final ArrayList<Subject> mSubjects;
	private final ResultReceiver mResultReceiver;

	public interface ResultReceiver {
		void onReceiveResult(boolean result);
	}

	/**
	 * Constructor
	 * @param subjects
	 * @param resultReceiver
	 */
	public SetUserPermissionsCommand(ArrayList<Subject> subjects, ResultReceiver resultReceiver){
		this.mSubjects = subjects;
		this.mResultReceiver = resultReceiver;
	}

	/**
	 * Constructor
	 * @param subjects
	 */
	public SetUserPermissionsCommand(ArrayList<Subject> subjects) {
		this(subjects, null);
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
		Intent intent = new Intent(Constants.ACTION_SET_USER_PERMISSIONS);
		intent.putExtra(Constants.EXTRA_SUBJECT_LIST, mSubjects);
		Log.i(TAG, "send(" + intent.getAction() + ")");
		if (receiver==null) {
			context.sendOrderedBroadcast(intent, CMD_PERMISSION);
		} else {
			context.sendOrderedBroadcast(intent, CMD_PERMISSION, receiver, null, 0, null, null);
		}
	}

}
