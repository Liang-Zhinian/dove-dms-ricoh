/*
 *  Copyright (C) 2017 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.system;

import java.util.HashMap;
import java.util.Map;

import com.dove.sample.wrapper.common.RequestBody;
import com.dove.sample.wrapper.common.Utils;
import com.dove.sample.wrapper.common.WritableElement;
import com.dove.sample.wrapper.json.EncodedException;
import com.dove.sample.wrapper.json.JsonUtils;
import com.dove.sample.wrapper.log.Logger;

/*
 * @since SmartSDK v2.40
 */
public class AdminAuthRequestBody extends WritableElement implements RequestBody{
	
	 /**
     * Define the prefix for log information with abbreviation package and class name
     */
    private final static String PREFIX = "system:AdminAuthReq:";
	
	private static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";
	
	private static final String KEY_CREDENTIAL = "credential";

	public AdminAuthRequestBody() {
		super(new HashMap<String, Object>());
	}


	@Override
	public String getContentType() {
		return CONTENT_TYPE_JSON;
	}

	@Override
	public String toEntityString() {
		try {
			return JsonUtils.getEncoder().encode(values);
		} catch (EncodedException e) {
            Logger.warn(Utils.getTagName(), PREFIX + e.toString());
			return "{}";
		}
	}
	
	/*
	 * credential (Object)
	 */
	public Credential getCredential() {
		Map<String, Object> value = getObjectValue(KEY_CREDENTIAL);
		if (value == null) {
			value = Utils.createElementMap();
            setObjectValue(KEY_CREDENTIAL, value);
		}
		return new Credential(value);
	}
	public Credential removeCredential() {
		Map<String, Object> value = removeObjectValue(KEY_CREDENTIAL);
		if (value == null) {
			return null;
		}
		return new Credential(value);
	}
	
	public static class Credential extends WritableElement{
		private static final String KEY_USER_ID = "userID";
		private static final String KEY_PASSWORD = "password";

		Credential(Map<String, Object> values) {
			super(values);
		}
		
		/*
		 * userID (String)
		 */
		public String getUserID() {
			return getStringValue(KEY_USER_ID);
		}
		public void setUserID(String value) {
			setStringValue(KEY_USER_ID, value);
		}
		public String removeUserID() {
			return removeStringValue(KEY_USER_ID);
		}
		
		/*
		 * password (String)
		 */
		public String getPassword() {
			return getStringValue(KEY_PASSWORD);
		}
		public void setPassword(String value) {
			setStringValue(KEY_PASSWORD, value);
		}
		public String removePassword() {
			return removeStringValue(KEY_PASSWORD);
		}
	}


}
