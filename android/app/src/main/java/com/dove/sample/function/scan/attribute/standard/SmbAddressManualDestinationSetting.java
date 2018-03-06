/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.function.scan.attribute.standard;

import java.util.HashMap;
import java.util.Map;

public final class SmbAddressManualDestinationSetting implements DestinationSettingItem {
	
	private static final String NAME_DESTINATION_TYPE = "destinationType";
	private static final String DESTINATION_TYPE_MANUAL = "manual";
	
	private static final String NAME_MANUAL_DESTINATION_SETTING = "manualDestinationSetting";
	
	private static final String NAME_DESTINATION_KIND = "destinationKind";
	private static final String DESTINATION_KIND_SMB = "smb";
	
	private static final String NAME_SMB_ADDRESS_INFO = "smbAddressInfo";
	
	private static final String NAME_PATH = "path";
	private static final String NAME_USER_NAME = "userName";
	private static final String NAME_PASSWORD = "password";
	
	private static final String NAME_USE_LOGIN_USER_AUTH_INFO = "useLoginUserAuthInfo"; //SmartSDK V2.12
	
	private String path;
	private String userName;
	private String password;
	
	private boolean useLoginUserAuthInfo; //SmartSDK V2.12
	
	public SmbAddressManualDestinationSetting() {
		path = null;
		userName = null;
		password = null;
		useLoginUserAuthInfo = false;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @since SmartSDK V2.12
	 */
	public boolean isUseLoginUserAuthInfo(){
		return useLoginUserAuthInfo;
	}
	
	/**
	 * @since SmartSDK V2.12
	 */
	public void setUseLoginUserAuthInfo(boolean value){
		useLoginUserAuthInfo = value;
	}

	@Override
	public Object getValue() {
		Map<String, Object> smbAddressInfo = new HashMap<String, Object>();
		if (path != null) {
			smbAddressInfo.put(NAME_PATH, path);
		}
		if (userName != null) {
			smbAddressInfo.put(NAME_USER_NAME, userName);
		}
		if (password != null) {
			smbAddressInfo.put(NAME_PASSWORD, password);
		}
		
		Map<String, Object> manualDestinationSetting = new HashMap<String, Object>();
		manualDestinationSetting.put(NAME_DESTINATION_KIND, DESTINATION_KIND_SMB);
		manualDestinationSetting.put(NAME_SMB_ADDRESS_INFO, smbAddressInfo);
		
		manualDestinationSetting.put(NAME_USE_LOGIN_USER_AUTH_INFO, useLoginUserAuthInfo);
		
		Map<String, Object> destinationSetting = new HashMap<String, Object>();
		destinationSetting.put(NAME_DESTINATION_TYPE, DESTINATION_TYPE_MANUAL);
		destinationSetting.put(NAME_MANUAL_DESTINATION_SETTING, manualDestinationSetting);
		
		return destinationSetting;
	}
	
}
