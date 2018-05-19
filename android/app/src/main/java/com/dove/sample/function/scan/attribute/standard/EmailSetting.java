/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.function.scan.attribute.standard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dove.sample.function.scan.attribute.ScanRequestAttribute;

public final class EmailSetting implements ScanRequestAttribute {
	
	private static final String NAME_EMAIL_SETTING = "emailSetting";
	
	private static final String NAME_SUBJECT = "subject";
	private static final String NAME_BODY = "body";
	private static final String NAME_REGISTRATION_BODY = "registrationBody"; //SmartSDK V2.12
	private static final String NAME_SENDER_ENTRY_ID = "senderEntryId";
	private static final String NAME_SMIME_SIGNATURE = "smimeSignature";
	private static final String NAME_SMIME_ENCRYPTION = "smimeEncryption";
	private static final String NAME_RECEPTION_NOTICE = "receptionNotice"; //SmartSDK V2.12
	private static final String NAME_LIMIT_EMAIL_SIZE = "limitEmailSize"; //SmartSDK V2.12
	private static final String NAME_MAX_EMAIL_SIZE = "maxEmailSize"; //SmartSDK V2.12
	private static final String NAME_EMAIL_DIVIDING = "emailDividing"; //SmartSDK V2.12
	private static final String NAME_EMAIL_DIVIDING_NUMBER = "emailDividingNumber"; //SmartSDK V2.12
	private static final String NAME_FILE_EMAILING_METHOD = "fileEmailingMethod"; //SmartSDK V2.12
	
	
	private String subject;
	private String body;
	private RegistrationBody registrationBody; //SmartSDK V2.12
	private String senderEntryId;
	private boolean smimeSignature;
	private boolean smimeEncryption;
	private boolean receptionNotice; //SmartSDK V2.12
	private boolean limitEmailSize; //SmartSDK V2.12
	private int maxEmailSize; //SmartSDK V2.12
	private EmailDividing emailDividing; //SmartSDK V2.12
	private int emailDividingNumber; //SmartSDK V2.12
	private FileEmailingMethod fileEmailingMethod; //SmartSDK V2.12
	
	public EmailSetting() {
		subject = null;
		body = null;
		registrationBody = null;
		senderEntryId = null;
		smimeSignature = false;
		smimeEncryption = false;
		receptionNotice = false;
		limitEmailSize = false;
		maxEmailSize = 0;
		emailDividing = null;
		emailDividingNumber = 0;
		fileEmailingMethod = null;
	}
	
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @since SmartSDK V2.12
	 */
	public RegistrationBody getRegistrationBody(){
		return registrationBody;
	}
	
	/**
	 * @since SmartSDK V2.12
	 */
	public void setRegistrationBody(RegistrationBody registrationBody){
		this.registrationBody = registrationBody;
	}
	
	public String getSenderEntryId() {
		return senderEntryId;
	}

	public void setSenderEntryId(String senderEntryId) {
		this.senderEntryId = senderEntryId;
	}

	public boolean isSmimeSignature() {
		return smimeSignature;
	}

	public void setSmimeSignature(boolean smimeSignature) {
		this.smimeSignature = smimeSignature;
	}

	public boolean isSmimeEncryption() {
		return smimeEncryption;
	}

	public void setSmimeEncryption(boolean smimeEncryption) {
		this.smimeEncryption = smimeEncryption;
	}
	
	/**
	 * @since SmartSDK V2.12
	 */
	public boolean isReceptionNotice(){
		return receptionNotice;
	}
	
	/**
	 * @since SmartSDK V2.12
	 */
	public void setReceptionNotice(boolean value){
		receptionNotice = value;
	}
	
	/**
	 * @since SmartSDK V2.12
	 */
	public boolean isLimitEmailSize(){
		return limitEmailSize;
	}
	
	/**
	 * @since SmartSDK V2.12
	 */
	public void setLimitEmailSize(boolean value){
		limitEmailSize = value;
	}
	
	/**
	 * @since SmartSDK V2.12
	 */
	public int getMaxEmailSize(){
		return maxEmailSize;
	}
	
	/**
	 * @since SmartSDK V2.12
	 */
	public void setMaxEmailSize(int maxEmailSize){
		this.maxEmailSize = maxEmailSize;
	}
	
	/**
	 * @since SmartSDK V2.12
	 */
	public EmailDividing getEmailDividing(){
		return emailDividing;
	}
	
	/**
	 * @since SmartSDK V2.12
	 */
	public void setEmailDividing(EmailDividing emailDividing){
		this.emailDividing = emailDividing;
	}
	
	/**
	 * @since SmartSDK V2.12
	 */
	public int getEmailDividingNumber(){
		return emailDividingNumber;
	}
	
	/**
	 * @since SmartSDK V2.12
	 */
	public void setEmailDividingNumber(int emailDividingNumber){
		this.emailDividingNumber = emailDividingNumber;
	}
	
	/**
	 * @since SmartSDK V2.12
	 */
	public FileEmailingMethod getFileEmailingMethod(){
		return fileEmailingMethod;
	}
	
	/**
	 * @since SmartSDK V2.12
	 */
	public void setFileEmailingMethod(FileEmailingMethod fileEmailingMethod){
		this.fileEmailingMethod = fileEmailingMethod;
	}
	
	@Override
	public Class<?> getCategory() {
		return EmailSetting.class;
	}

	@Override
	public String getName() {
		return NAME_EMAIL_SETTING;
	}

	@Override
	public Object getValue() {
		Map<String, Object> values = new HashMap<String, Object>();
		
		if (subject != null) {
			values.put(NAME_SUBJECT, subject);
		}
		if (body != null) {
			values.put(NAME_BODY, body);
		}
		if (registrationBody != null){
			values.put(NAME_REGISTRATION_BODY, registrationBody);
		}
		if (senderEntryId != null) {
			values.put(NAME_SENDER_ENTRY_ID, senderEntryId);
		}
		values.put(NAME_SMIME_SIGNATURE, Boolean.valueOf(smimeSignature));
		values.put(NAME_SMIME_ENCRYPTION, Boolean.valueOf(smimeEncryption));
		values.put(NAME_RECEPTION_NOTICE, Boolean.valueOf(receptionNotice));
		values.put(NAME_LIMIT_EMAIL_SIZE, Boolean.valueOf(limitEmailSize));
		values.put(NAME_MAX_EMAIL_SIZE, Integer.valueOf(maxEmailSize));
		if (emailDividing != null) {
			values.put(NAME_EMAIL_DIVIDING, emailDividing);
		}
		values.put(NAME_EMAIL_DIVIDING_NUMBER, Integer.valueOf(emailDividingNumber));
		if (fileEmailingMethod != null){
			values.put(NAME_FILE_EMAILING_METHOD, fileEmailingMethod);
		}
		return values;
	}
	
	/**
	 * 登録本文を示すクラスです。
	 * The enum type of RegistrationBody
	 * 
	 * @since SmartSDK V2.12
	 */
	public static enum RegistrationBody {
		
		BODY1("body1"),
		BODY2("body2"),
		BODY3("body3"),
		BODY4("body4"),
		BODY5("body5");
		
		private final String value;
		
		private RegistrationBody(String value){
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
		
		private static volatile Map<String, RegistrationBody> directory = null;

        private static Map<String, RegistrationBody> getDirectory() {
            if(directory == null) {
                Map<String, RegistrationBody> d = new HashMap<String, RegistrationBody>();

                for(RegistrationBody registrationBody : values()) {
                    d.put(registrationBody.getValue(), registrationBody);
                }

                directory = d;
            }
            return directory;
        }

        private static RegistrationBody fromString(String value) {
            return getDirectory().get(value);
        }

        public static List<RegistrationBody> getSupportedValue(List<String> values) {
        	if (values == null) {
        		return Collections.emptyList();
        	}

            List<RegistrationBody> list = new ArrayList<RegistrationBody>();
            for(String value : values) {
            	RegistrationBody level = fromString(value);
                if (level != null) {
                    list.add(level);
                }
            }
            return list;
        }
	}

	/**
	 * メールサイズ制限オーバー時分割を示すクラスです。
     * The enum type of EmailDividing
     * 
	 * @since SmartSDK V2.12
	 */
	public static enum EmailDividing {
		
		PER_MAX_SIZE("per_max_size"),
		PER_PAGE("per_page"),
		NONE("none");
		
		private final String value;
		
		private EmailDividing(String value){
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
		
		private static volatile Map<String, EmailDividing> directory = null;

        private static Map<String, EmailDividing> getDirectory() {
            if(directory == null) {
                Map<String, EmailDividing> d = new HashMap<String, EmailDividing>();

                for(EmailDividing emailDividing : values()) {
                    d.put(emailDividing.getValue(), emailDividing);
                }

                directory = d;
            }
            return directory;
        }

        private static EmailDividing fromString(String value) {
            return getDirectory().get(value);
        }

        public static List<EmailDividing> getSupportedValue(List<String> values) {
        	if (values == null) {
        		return Collections.emptyList();
        	}

            List<EmailDividing> list = new ArrayList<EmailDividing>();
            for(String value : values) {
            	EmailDividing level = fromString(value);
                if (level != null) {
                    list.add(level);
                }
            }
            return list;
        }
	}
	
	/**
	 * 文書送信方法を示すクラスです。
     * The enum type of FileEmailingMethod
     * 
	 * @since SmartSDK V2.12
	 */
	public static enum FileEmailingMethod {
		
		ATTACH_TO_EMAIL("attach_to_email"),
		SEND_URL_LINK("send_url_link"),
		AUTO_DETECT("auto_detect");
		
		private final String value;
		
		private FileEmailingMethod(String value){
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
		
		private static volatile Map<String, FileEmailingMethod> directory = null;

        private static Map<String, FileEmailingMethod> getDirectory() {
            if(directory == null) {
                Map<String, FileEmailingMethod> d = new HashMap<String, FileEmailingMethod>();

                for(FileEmailingMethod fileEmailingMethod : values()) {
                    d.put(fileEmailingMethod.getValue(), fileEmailingMethod);
                }

                directory = d;
            }
            return directory;
        }

        private static FileEmailingMethod fromString(String value) {
            return getDirectory().get(value);
        }

        public static List<FileEmailingMethod> getSupportedValue(List<String> values) {
        	if (values == null) {
        		return Collections.emptyList();
        	}

            List<FileEmailingMethod> list = new ArrayList<FileEmailingMethod>();
            for(String value : values) {
            	FileEmailingMethod level = fromString(value);
                if (level != null) {
                    list.add(level);
                }
            }
            return list;
        }
	}
}
