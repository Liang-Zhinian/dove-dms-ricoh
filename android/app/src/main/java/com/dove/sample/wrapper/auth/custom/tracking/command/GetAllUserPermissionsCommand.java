/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.custom.tracking.command;

import jp.co.ricoh.isdk.sdkservice.auth.custom.tracking.intent.Constants;
import jp.co.ricoh.ssdk.sample.framework.common.SsdkEvent;
import com.dove.sample.wrapper.auth.custom.tracking.event.UserPermissionsEvent;
import com.dove.sample.wrapper.common.SsdkCommand;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

/**
 * Command to obtain print permissions of all users registered to the machine's address book<br>
 * This command is used to initialize user print permissions at the time the machine is started.
 */
public class GetAllUserPermissionsCommand implements SsdkCommand {
	private static String TAG = "AuthWrap";
	private static final String CMD_PERMISSION = "jp.co.ricoh.isdk.sdkservice.auth.CUSTOM_TRACKING_CMD_PERMISSION";
	private static final String EVENT_PERMISSION = "jp.co.ricoh.isdk.sdkservice.auth.CUSTOM_TRACKING_CMD_PERMISSION";

	private final UserPermissionReceiver mUserPermissionReceiver;
	private final ResultReceiver mResultReceiver;

	/**
	 * User permission data receiver
	 */
	public interface UserPermissionReceiver {
		void onReceive(UserPermissionsEvent event);
	}

	/**
	 * Command execution result receiver
	 */
	public interface ResultReceiver {
		void onReceiveResult(boolean result);
	}

	/**
	 * Constructor
	 * @param permissionReceiver
	 * @param resultReceiver
	 */
	public GetAllUserPermissionsCommand(UserPermissionReceiver permissionReceiver, ResultReceiver resultReceiver) {
		this.mUserPermissionReceiver = permissionReceiver;
		this.mResultReceiver = resultReceiver;
	}

	/**
	 * Constructor
	 * @param permissionReceiver
	 */
	public GetAllUserPermissionsCommand(UserPermissionReceiver permissionReceiver) {
		this(permissionReceiver, null);
	}
	@Override
	public void execute(final Context context) {
		// Register receiver
		final BroadcastReceiver permissionReceiver = new PermissionReceiver(context);
		IntentFilter filter = new IntentFilter(Constants.ACTION_USER_PERMISSIONS);
		context.registerReceiver(permissionReceiver, filter, EVENT_PERMISSION, null);

		BroadcastReceiver resultReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context returnContext, Intent intent) {
				Bundle extras = this.getResultExtras(true);
				boolean result =  extras.getBoolean(Constants.EXTRA_RESULT, false);
				if (mResultReceiver!=null) {
					mResultReceiver.onReceiveResult(result);
				}
				if (!result) {
					// unregister the receiver if the execution is failed
					context.unregisterReceiver(permissionReceiver);
				}
			}
		};

		// Send broadcast
		sendIntent(context, resultReceiver);
	}

	private void sendIntent(Context context, BroadcastReceiver receiver) {
		Intent intent = new Intent(Constants.ACTION_GET_ALL_USER_PERMISSIONS);
		Log.i(TAG, "send(" + intent.getAction() + ")");
		if (receiver==null) {
			context.sendOrderedBroadcast(intent, CMD_PERMISSION);
		} else {
			context.sendOrderedBroadcast(intent, CMD_PERMISSION, receiver, null, 0, null, null);
		}
	}

	private class PermissionReceiver extends BroadcastReceiver {
		private final Context mContext;

		public PermissionReceiver(Context context) {
			this.mContext = context;
		}

		@Override
		public void onReceive(Context returnContext, Intent intent) {
			UserPermissionsEvent event = new UserPermissionsEvent(new SsdkEvent(intent));

			// notify to the listener
			mUserPermissionReceiver.onReceive(event);

			// Unregister receiver
			if (event.isLast()) {
				mContext.unregisterReceiver(this);
			}
		}
	}

}
