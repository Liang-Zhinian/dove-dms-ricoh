/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.custom.ui.data;

import java.util.HashMap;
import java.util.Locale;

import jp.co.ricoh.isdk.sdkservice.auth.custom.ui.intent.Constants;
import com.dove.sample.wrapper.auth.permission.BrowserPermission;
import com.dove.sample.wrapper.auth.permission.CertificateUserPermission;
import com.dove.sample.wrapper.auth.permission.CopierColorModePermission;
import com.dove.sample.wrapper.auth.permission.CopierPermission;
import com.dove.sample.wrapper.auth.permission.DocumentAdminPermission;
import com.dove.sample.wrapper.auth.permission.DocumentServerPermission;
import com.dove.sample.wrapper.auth.permission.FaxPermission;
import com.dove.sample.wrapper.auth.permission.MachineAdminPermission;
import com.dove.sample.wrapper.auth.permission.NetworkAdminPermission;
import com.dove.sample.wrapper.auth.permission.PrinterColorModePermission;
import com.dove.sample.wrapper.auth.permission.PrinterPermission;
import com.dove.sample.wrapper.auth.permission.ScannerPermission;
import com.dove.sample.wrapper.auth.permission.SdkjApplicationPermission;
import com.dove.sample.wrapper.auth.permission.SupervisorPermission;
import com.dove.sample.wrapper.auth.permission.UserAdminPermission;
import android.os.Bundle;

public class AuthServiceState {
	public enum STATE {
		INITIAL_WAIT,
		LOGOUT,
		LOGIN_OPERATING,
		LOGIN,
		LOGIN_CANCEL,
		LOGOUT_OPERATING,
		LOGOUT_CANCEL,
		LOGIN_RESTRICT;
	}

	public enum TRIGGER {
		SYSTEM_STARTED,
		LOGIN_STARTED,
		USER_LOGIN,
		LOGOUT_STARTED,
		USER_LOGOUT,
		LOGIN_FAILED,
		LOGOUT_FAILED,
		LOGIN_CANCELLED,
	}

	private final STATE state;
	private final TRIGGER trigger;
	private final String errorCode;

	private final String userId;
	private final String userName;

	private final CopierPermission copierPermission;
	private final HashMap<CopierColorModePermission, Boolean> copierColorModePermission;
	private final DocumentServerPermission documentServerPermission;
	private final FaxPermission faxPermission;
	private final ScannerPermission scannerPermission;
	private final PrinterPermission printerPermission;
	private final HashMap<PrinterColorModePermission, Boolean> printerColorModePermission;
	private final BrowserPermission browserPermission;
	private final HashMap<String, SdkjApplicationPermission> sdkjApplicationPermissions;

	private final MachineAdminPermission machineAdminPermission;
	private final UserAdminPermission userAdminPermission;
	private final DocumentAdminPermission documentAdminPermission;
	private final NetworkAdminPermission networkAdminPermission;
	private final CertificateUserPermission certificateUserPermission;
	private final SupervisorPermission supervisorPermission;

	private final String supplicantTermType;
	private final String supplicantDevice;
	private final String supplicantRequestApplication;

	/**
	 * Build object from SYS_NOTIFY_AUTH_SERVICE_STATE_CHANGED or GET_AUTH_SERVICE_STATE intent extras
	 * @param extras SYS_NOTIFY_AUTH_SERVICE_STATE_CHANGED or GET_AUTH_SERVICE_STATE intent extras
	 * @return AuthServiceState
	 */
	public static AuthServiceState buildFromExtras(Bundle extras) {
		return new AuthServiceState(extras);
	}

	public STATE getState() {
		return state;
	}

	public TRIGGER getTrigger() {
		return trigger;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public CopierPermission getCopierPermission() {
		return copierPermission;
	}

	public HashMap<CopierColorModePermission, Boolean> getCopierColorModePermission() {
		return copierColorModePermission;
	}

	public DocumentServerPermission getDocumentServerPermission() {
		return documentServerPermission;
	}

	public FaxPermission getFaxPermission() {
		return faxPermission;
	}

	public ScannerPermission getScannerPermission() {
		return scannerPermission;
	}

	public PrinterPermission getPrinterPermission() {
		return printerPermission;
	}

	public HashMap<PrinterColorModePermission, Boolean> getPrinterColorModePermission() {
		return printerColorModePermission;
	}

	public BrowserPermission getBrowserPermission() {
		return browserPermission;
	}

	public HashMap<String, SdkjApplicationPermission> getSdkjApplicationPermissions() {
		return sdkjApplicationPermissions;
	}

	public MachineAdminPermission getMachineAdminPermission() {
		return machineAdminPermission;
	}

	public UserAdminPermission getUserAdminPermission() {
		return userAdminPermission;
	}

	public DocumentAdminPermission getDocumentAdminPermission() {
		return documentAdminPermission;
	}

	public NetworkAdminPermission getNetworkAdminPermission() {
		return networkAdminPermission;
	}

	public CertificateUserPermission getCertificateUserPermission() {
		return certificateUserPermission;
	}

	public SupervisorPermission getSupervisorPermission() {
		return supervisorPermission;
	}

	public String getSupplicantTermType() {
		return supplicantTermType;
	}

	public String getSupplicantDevice() {
		return supplicantDevice;
	}

	public String getSupplicantRequestApplication() {
		return supplicantRequestApplication;
	}

	private AuthServiceState(Bundle extras) {
		this.state 				= getEnum(STATE.class, extras.getString(Constants.EXTRA_STATE));
		this.trigger 				= getEnum(TRIGGER.class, extras.getString(Constants.EXTRA_TRIGGER));
		this.errorCode 			= extras.getString(Constants.EXTRA_ERROR_CODE);
		this.userId 				= extras.getString(Constants.EXTRA_USER_ID);
		this.userName 			= extras.getString(Constants.EXTRA_USER_NAME);
		this.copierPermission 	= getEnum(CopierPermission.class, extras.getString(Constants.EXTRA_COPIER_PERMISSION));

		@SuppressWarnings("unchecked")
		HashMap<String, Boolean> orgCopierColorModePermission = (HashMap<String, Boolean>)extras.getSerializable(Constants.EXTRA_COPIER_COLOR_MODE_PERMISSION);
		this.copierColorModePermission = new HashMap<CopierColorModePermission, Boolean>();
		if (orgCopierColorModePermission!=null) {
			for(String key: orgCopierColorModePermission.keySet()) {
				copierColorModePermission.put(getEnum(CopierColorModePermission.class, key), orgCopierColorModePermission.get(key));
			}
		}

		this.documentServerPermission 	= getEnum(DocumentServerPermission.class, extras.getString(Constants.EXTRA_DOCUMENT_SERVER_PERMISSION));
		this.faxPermission 				= getEnum(FaxPermission.class, extras.getString(Constants.EXTRA_FAX_PERMISSION));
		this.scannerPermission 			= getEnum(ScannerPermission.class, extras.getString(Constants.EXTRA_SCANNER_PERMISSION));
		this.printerPermission 			= getEnum(PrinterPermission.class, extras.getString(Constants.EXTRA_PRINTER_PERMISSION));

		@SuppressWarnings("unchecked")
		HashMap<String, Boolean> orgPrinterColorModePermission = (HashMap<String, Boolean>)extras.getSerializable(Constants.EXTRA_PRINTER_COLOR_MODE_PERMISSION);
		this.printerColorModePermission = new HashMap<PrinterColorModePermission, Boolean>();
		if (orgPrinterColorModePermission!=null) {
			for(String key: orgPrinterColorModePermission.keySet()) {
				printerColorModePermission.put(getEnum(PrinterColorModePermission.class, key), orgPrinterColorModePermission.get(key));
			}
		}

		this.browserPermission = getEnum(BrowserPermission.class, extras.getString(Constants.EXTRA_BROWSER_PERMISSION));

		@SuppressWarnings("unchecked")
		HashMap<String, String> orgSdkApplicationPermission = (HashMap<String, String>)extras.getSerializable(Constants.EXTRA_SDKJ_APPLICATION_PERMISSIONS);
		this.sdkjApplicationPermissions = new HashMap<String, SdkjApplicationPermission>();
		if (orgSdkApplicationPermission!=null) {
			for(String key: orgSdkApplicationPermission.keySet()) {
				sdkjApplicationPermissions.put(key, getEnum(SdkjApplicationPermission.class, orgSdkApplicationPermission.get(key)));
			}
		}

		this.machineAdminPermission 		= getEnum(MachineAdminPermission.class, extras.getString(Constants.EXTRA_MACHINE_ADMIN_PERMISSION));
		this.userAdminPermission 			= getEnum(UserAdminPermission.class, extras.getString(Constants.EXTRA_USER_ADMIN_PERMISSION));
		this.documentAdminPermission 		= getEnum(DocumentAdminPermission.class, extras.getString(Constants.EXTRA_DOCUMENT_ADMIN_PERMISSION));
		this.networkAdminPermission 		= getEnum(NetworkAdminPermission.class, extras.getString(Constants.EXTRA_NETWORK_ADMIN_PERMISSION));
		this.certificateUserPermission 		= getEnum(CertificateUserPermission.class, extras.getString(Constants.EXTRA_CERTIFICATE_USER_PERMISSION));
		this.supervisorPermission 			= getEnum(SupervisorPermission.class, extras.getString(Constants.EXTRA_SUPERVISOR_PERMISSION));
		this.supplicantTermType 			= extras.getString(Constants.EXTRA_SUPPLICANT_TERM_TYPE);
		this.supplicantDevice 				= extras.getString(Constants.EXTRA_SUPPLICANT_DEVICE);
		this.supplicantRequestApplication 	= extras.getString(Constants.EXTRA_SUPPLICANT_REQUEST_APPLICATION);
	}

	private <T extends Enum<T>> T getEnum(Class<T> enumType, String name) {
		return (name==null) ? null : (Enum.valueOf(enumType, name.toUpperCase(Locale.ENGLISH)));
	}
}
