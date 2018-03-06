/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.service.scanner;

import java.util.Map;

import com.dove.sample.wrapper.common.Element;
import com.dove.sample.wrapper.common.ResponseBody;

/*
 * @since SmartSDK V2.12
 */
public class ValidateCertificationResponseBody extends Element implements ResponseBody {

	private static final String KEY_CERTIFICATE_VALIDATE_RESULT	= "certificateValidateResult";
	private static final String KEY_CERTIFICATE_VALIDATE_REASON	= "certificateValidateReason";
	private static final String KEY_NEAREST_EXPIRATION_ENTRY_ID	= "nearestExpirationEntryId";
	private static final String KEY_EXPIRATION_ENTRY_ID_NUMBER	= "expirationEntryIdNumber";
	private static final String KEY_NEAREST_EXPIRATION_TIME		= "nearestExpirationTime";

	ValidateCertificationResponseBody(Map<String, Object> values) {
		super(values);
	}

	/*
	 * certificateValidateResult (String)
	 * @since SmartSDK V2.12
	 */
	public String getCertificateValidateResult() {
		return getStringValue(KEY_CERTIFICATE_VALIDATE_RESULT);
	}

	/*
	 * certificateValidateReason (String)
	 * @since SmartSDK V2.12
	 */
	public String getCertificateValidateReason() {
		return getStringValue(KEY_CERTIFICATE_VALIDATE_REASON);
	}

	/*
	 * nearestExpirationEntryId (String)
	 * @since SmartSDK V2.12
	 */
	public String getNearestExpirationEntryId() {
		return getStringValue(KEY_NEAREST_EXPIRATION_ENTRY_ID);
	}
	
	/*
	 * expirationEntryIdNumber (Number)
	 * @since SmartSDK V2.12
	 */
	public Integer getExpirationEntryIdNumber() {
		return getNumberValue(KEY_EXPIRATION_ENTRY_ID_NUMBER);
	}

	/*
	 * nearestExpirationTime (String)
	 * @since SmartSDK V2.12
	 */
	public String getMearestExpirationTime() {
		return getStringValue(KEY_NEAREST_EXPIRATION_TIME);
	}

}
