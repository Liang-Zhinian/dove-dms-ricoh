/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.function.scan.attribute.standard;

import java.util.HashMap;
import java.util.Map;

public final class MailAddressManualDestinationSetting implements DestinationSettingItem {
	
	private static final String NAME_DESTINATION_TYPE = "destinationType";
	private static final String DESTINATION_TYPE_MANUAL = "manual";
	
	private static final String NAME_MANUAL_DESTINATION_SETTING = "manualDestinationSetting";
	
	private static final String NAME_DESTINATION_KIND = "destinationKind";
	private static final String DESTINATION_KIND_MAIL = "mail";
	
	private static final String NAME_MAIL_ADDRESS_INFO = "mailAddressInfo";
	
	private static final String NAME_MAIL_ADDRESS = "mailAddress";
	private static final String NAME_MAIL_TO_CC_BCC = "mailToCcBcc";
	
	private static final String NAME_USE_LOGIN_USER_AUTH_INFO = "useLoginUserAuthInfo"; //SmartSDK V2.12
	
	private String mailAddress;
	private MailToCcBcc mailToCcBcc;
	
	private boolean useLoginUserAuthInfo; //SmartSDK V2.12
	
	
	public MailAddressManualDestinationSetting() {
		mailAddress = null;
		mailToCcBcc = null;
		useLoginUserAuthInfo = false;
	}
	
	public String getMailAddress() {
		return mailAddress;
	}


	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}


	public MailToCcBcc getMailToCcBcc() {
		return mailToCcBcc;
	}


	public void setMailToCcBcc(MailToCcBcc mailToCcBcc) {
		this.mailToCcBcc = mailToCcBcc;
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
		Map<String, Object> mailAddressInfo = new HashMap<String, Object>();
		if (mailAddress != null) {
			mailAddressInfo.put(NAME_MAIL_ADDRESS, mailAddress);
		}
		if (mailToCcBcc != null) {
			mailAddressInfo.put(NAME_MAIL_TO_CC_BCC, mailToCcBcc.getValue());
		}
		
		Map<String, Object> manualDestinationSetting = new HashMap<String, Object>();
		manualDestinationSetting.put(NAME_DESTINATION_KIND, DESTINATION_KIND_MAIL);
		manualDestinationSetting.put(NAME_MAIL_ADDRESS_INFO, mailAddressInfo);
		
		manualDestinationSetting.put(NAME_USE_LOGIN_USER_AUTH_INFO, useLoginUserAuthInfo);
		
		Map<String, Object> destinationSetting = new HashMap<String, Object>();
		destinationSetting.put(NAME_DESTINATION_TYPE, DESTINATION_TYPE_MANUAL);
		destinationSetting.put(NAME_MANUAL_DESTINATION_SETTING, manualDestinationSetting);
		
		return destinationSetting;
	}

	
	public static enum MailToCcBcc {
		
		TO("to"),
		CC("cc"),
		BCC("bcc");
		
		private final String value;
		
		private MailToCcBcc(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
		
	}

}
