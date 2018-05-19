/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.custom.logic.command;

import java.util.HashMap;
import java.util.Map;

import jp.co.ricoh.isdk.sdkservice.auth.custom.logic.intent.Constants;
import com.dove.sample.wrapper.auth.custom.logic.data.ExtLoginResponse;
import com.dove.sample.wrapper.auth.permission.MfpFunctionPermissions;
import com.dove.sample.wrapper.auth.permission.SdkjApplicationPermission;
import com.dove.sample.wrapper.common.SsdkCommand;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Command of RES_EXTERNAL_AUTH
 */
public class ExternalAuthResponseCommand implements SsdkCommand {
	private static String TAG = "AuthWrap";
	private static final String CMD_PERMISSION = "jp.co.ricoh.isdk.sdkservice.auth.CUSTOM_LOGIC_CMD_PERMISSION";

	private final ExtLoginResponse response;

	/**
	 * Constructor
	 * @param response ExtLoginResponse
	 */
	public ExternalAuthResponseCommand(ExtLoginResponse response){
		this.response = response;
	}

	@Override
	public void execute(Context context) {
		sendIntent(context);
	}

	/**
	 * Send RES_EXTERNAL_AUTH intent
	 * @param context
	 */
	private void sendIntent(Context context) {
		Intent intent = new Intent(Constants.ACTION_RES_EXTERNAL_AUTH);

		intent.putExtra(Constants.EXTRA_REQUEST_ID, response.getRequestId());
		intent.putExtra(Constants.EXTRA_RESULT, response.getResult());
		intent.putExtra(Constants.EXTRA_ERROR, response.getError());
		intent.putExtra(Constants.EXTRA_USER_ID, response.getUserId());
		intent.putExtra(Constants.EXTRA_PASSWORD, response.getPassword());
		intent.putExtra(Constants.EXTRA_USER_NAME, response.getUserName());
		intent.putExtra(Constants.EXTRA_DISPLAY_NAME, response.getDisplayName());
		intent.putExtra(Constants.EXTRA_SERIAL_ID, response.getSerialId());
		intent.putExtra(Constants.EXTRA_EMAIL, response.getEmail());
		intent.putExtra(Constants.EXTRA_FAX_NUMBER, response.getFaxNumber());
		intent.putExtra(Constants.EXTRA_BILLING_CODE, response.getBillingCode());
		intent.putExtra(Constants.EXTRA_BILLING_CODE_LABEL, response.getBillingCodeLabel());

		MfpFunctionPermissions permissions = response.getMfpFunctionPermissions();
		if (permissions!=null) {
			if (permissions.getCopierPermission()!=null) {
				intent.putExtra(Constants.EXTRA_COPIER_PERMISSION, permissions.getCopierPermission().getName());
			}
			if (permissions.getDocServerPermission()!=null) {
				intent.putExtra(Constants.EXTRA_DOCUMENT_SERVER_PERMISSION, permissions.getDocServerPermission().getName());
			}
			if (permissions.getFaxPermission()!=null) {
				intent.putExtra(Constants.EXTRA_FAX_PERMISSION, permissions.getFaxPermission().getName());
			}
			if (permissions.getScannerPermission()!=null) {
				intent.putExtra(Constants.EXTRA_SCANNER_PERMISSION, permissions.getScannerPermission().getName());
			}
			if (permissions.getPrinterPermission()!=null) {
				intent.putExtra(Constants.EXTRA_PRINTER_PERMISSION, permissions.getPrinterPermission().getName());
			}
			if (permissions.getBrowserPermission()!=null) {
				intent.putExtra(Constants.EXTRA_BROWSER_PERMISSION, permissions.getBrowserPermission().getName());
			}
			if (permissions.getSdkjAppPermissions() != null) {
				HashMap<String, String> sdkjPermissions = new HashMap<String, String>();
				for(Map.Entry<String, SdkjApplicationPermission> entry: permissions.getSdkjAppPermissions().entrySet()) {
					sdkjPermissions.put(entry.getKey(), entry.getValue().getName());
				}
				intent.putExtra(Constants.EXTRA_SDKJ_APPLICATION_PERMISSIONS, sdkjPermissions);
			}
		}

		Log.i(TAG, "send(" + intent.getAction() + ")");
		context.sendBroadcast(intent, CMD_PERMISSION);
	}
}
