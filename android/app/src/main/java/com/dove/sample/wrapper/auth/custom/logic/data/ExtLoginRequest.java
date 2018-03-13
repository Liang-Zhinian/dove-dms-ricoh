/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.custom.logic.data;

import jp.co.ricoh.isdk.sdkservice.auth.custom.logic.intent.Constants;
import android.os.Bundle;

/**
 * Ext login request class
 */
public class ExtLoginRequest {
	private final String requestID;
	private final String userID;
	private final String password;
	private final String extensionInfo;
	private final String supplicantTermType;
	private final String supplicantDevice;
	private final String supplicantRequestApplication;

	public static ExtLoginRequest buildFromExtras(Bundle extras) {
		return new ExtLoginRequest(extras);
	}

	public String getRequestID() {
		return requestID;
	}

	public String getUserID() {
		return userID;
	}

	public String getPassword() {
		return password;
	}

	public String getExtInfo() {
		return extensionInfo;
	}

	public String getSupplicantTermType() {
		return supplicantTermType;
	}

	public String getSupplicantDevice() {
		return supplicantDevice;
	}

	public String getSupplicantReqApp() {
		return supplicantRequestApplication;
	}

	private ExtLoginRequest(Bundle extras) {
		this.requestID = extras.getString(Constants.EXTRA_REQUEST_ID);
		this.userID = extras.getString(Constants.EXTRA_USER_ID);
		this.password = extras.getString(Constants.EXTRA_PASSWORD);
		this.extensionInfo = extras.getString(Constants.EXTRA_EXTENSION_INFO);
		this.supplicantTermType = extras.getString(Constants.EXTRA_SUPPLICANT_TERM_TYPE);
		this.supplicantDevice = extras.getString(Constants.EXTRA_SUPPLICANT_DEVICE);
		this.supplicantRequestApplication = extras.getString(Constants.EXTRA_SUPPLICANT_REQUEST_APPLICATION);
	}
}
