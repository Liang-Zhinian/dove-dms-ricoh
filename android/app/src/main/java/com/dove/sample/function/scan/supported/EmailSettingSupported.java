/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.function.scan.supported;

import java.util.ArrayList;
import java.util.List;

import com.dove.sample.function.common.Conversions;
import com.dove.sample.wrapper.rws.service.scanner.Capability;

public final class EmailSettingSupported {
	
	private final MaxLengthSupported supportedSubjectLength;
	private final MaxLengthSupported supportedBodyLength;
	private final List<String> supportedRegistrationBodys; //SmartSDK V2.12
	private final List<BodyTextSupported> supportedBodyTexts; //SmartSDK V2.12
	private final MaxLengthSupported supportedSenderEntryIdLength;
	private final boolean adminAddresAsSender;
	private final List<Boolean> supportedSmimeSignatures;
	private final List<Boolean> supportedSmimeEncryptions;
	private final List<Boolean> supportedReceptionNotices; //SmartSDK V2.12
	private final List<Boolean> supportedLimitEmailSizes; //SmartSDK V2.12
	private final MaxMinSupported supportedMaxEmailSizeRange; //SmartSDK V2.12
	private final List<String> supportedEmailDividings; //SmartSDK V2.12
	private final MaxMinSupported supportedEmailDividingNumberRange; //SmartSDK V2.12
	private final List<String> supportedFileEmailingMethods; //SmartSDK V2.12

	public EmailSettingSupported(Capability.EmailSettingCapability capability) {
		supportedSubjectLength = MaxLengthSupported.getMaxLengthSupported(capability.getSubjectLength());
		supportedBodyLength = MaxLengthSupported.getMaxLengthSupported(capability.getBodyLength());
		supportedRegistrationBodys = Conversions.getList(capability.getRegistrationBodyList());
		supportedBodyTexts = BodyTextSupported.getInstances(capability.getBodyTextList());
		supportedSenderEntryIdLength = MaxLengthSupported.getMaxLengthSupported(capability.getSenderEntryIdLength());
		adminAddresAsSender = Conversions.getBooleanValue(capability.getAdminAddresAsSender(), false);
		supportedSmimeSignatures = Conversions.getList(capability.getSmimeSignatureList());
		supportedSmimeEncryptions = Conversions.getList(capability.getSmimeEncryptionList());
		supportedReceptionNotices = Conversions.getList(capability.getReceptionNoticeList());
		supportedLimitEmailSizes = Conversions.getList(capability.getLimitEmailSizeList());
		supportedMaxEmailSizeRange = MaxMinSupported.getMaxMinSupported(capability.getMaxEmailSizeRange());
		supportedEmailDividings = Conversions.getList(capability.getEmailDividingList());
		supportedEmailDividingNumberRange = MaxMinSupported.getMaxMinSupported(capability.getEmailDividingNumberRange());
		supportedFileEmailingMethods = Conversions.getList(capability.getFileEmailingMethodList());
	}
	
	public MaxLengthSupported getSupportedSubjectLength() {
		return supportedSubjectLength;
	}
	
	public MaxLengthSupported getSupportedBodyLength() {
		return supportedBodyLength;
	}
	
	/**
	 * @since SmartSDK V2.12
	 */
	public List<String> getSupportedRegistrationBodys(){
		return supportedRegistrationBodys;
	}
	
	/**
	 * @since SmartSDK V2.12
	 */
	public List<BodyTextSupported> getSupportedBodyTexts(){
		return supportedBodyTexts;
	}
	
	public MaxLengthSupported getSupportedSenderEntryIdLength() {
		return supportedSenderEntryIdLength;
	}
	
	public boolean isAdminAddresAsSender() {
		return adminAddresAsSender;
	}
	
	public List<Boolean> getSupportedSmimeSignatures() {
		return supportedSmimeSignatures;
	}
	
	public List<Boolean> getSupportedSmimeEncryptions() {
		return supportedSmimeEncryptions;
	}
	
	/**
	 * @since SmartSDK V2.12
	 */
	public List<Boolean> getSupportedReceptionNotices(){
		return supportedReceptionNotices;
	}
	
	/**
	 * @since SmartSDK V2.12
	 */
	public List<Boolean> getSupportedLimitEmailSizes(){
		return supportedLimitEmailSizes;
	}
	
	/**
	 * @since SmartSDK V2.12
	 */
	public MaxMinSupported getSupportedMaxEmailSizeRange(){
		return supportedMaxEmailSizeRange;
	}
	
	/**
	 * @since SmartSDK V2.12
	 */
	public List<String> getSupportedEmailDividings(){
		return supportedEmailDividings;
	}
	
	/**
	 * @since SmartSDK V2.12
	 */
	public MaxMinSupported getSupportedEmailDividingNumberRange(){
		return supportedEmailDividingNumberRange;
	}
	
	/**
	 * @since SmartSDK V2.12
	 */
	public  List<String> getSupportedFileEmailingMethods(){
		return supportedFileEmailingMethods;
	}
	
	/**
	 * @since SmartSDK V2.12
	 */
	public static final class BodyTextSupported {

        private final String id;
        private final String name;
        private final List<String> text;

        static List<BodyTextSupported> getInstances(Capability.BodyTextArray capabilities) {
            if (capabilities == null) {
                return null;
            }

            List<BodyTextSupported> list = new ArrayList<BodyTextSupported>(capabilities.size());
            for (Capability.BodyText bodyText : capabilities) {
                list.add(new BodyTextSupported(bodyText.getId(), bodyText.getName(), bodyText.getText()));
            }
            return list;
        }

        BodyTextSupported(String id, String name, List<String> text) {
            this.id = id;
            this.name = name;
            this.text = new ArrayList<String>(text);
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public List<String> getText() {
            return text;
        }
    }
}
