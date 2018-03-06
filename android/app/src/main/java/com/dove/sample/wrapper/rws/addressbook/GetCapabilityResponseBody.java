/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.addressbook;

import java.util.List;
import java.util.Map;

import com.dove.sample.wrapper.common.Element;
import com.dove.sample.wrapper.common.ResponseBody;

public class GetCapabilityResponseBody extends Element implements ResponseBody {
	
	private static final String KEY_APPLICATION		= "application";
	private static final String KEY_ADDRESS_MAX_NUM	= "addressMaxNum";
	private static final String KEY_USER_MAX_NUM	= "userMaxNum";
	private static final String KEY_TAG_MAX_NUM		= "tagMaxNum";
	private static final String KEY_GROUP_MAX_NUM	= "groupMaxNum";
	private static final String KEY_FUNCTIONS	    = "functions"; // SmartSDK V2.12
	
	GetCapabilityResponseBody(Map<String, Object> values) {
		super(values);
	}
	
	/*
	 * application (Array[String])
	 */
	public List<String> getApplication() {
		return getArrayValue(KEY_APPLICATION);
	}
	
	/*
	 * addressMaxNum (Int)
	 */
	public Integer getAddressMaxNum() {
		return getNumberValue(KEY_ADDRESS_MAX_NUM);
	}
	
	/*
	 * userMaxNum (Int)
	 */
	public Integer getUserMaxNum() {
		return getNumberValue(KEY_USER_MAX_NUM);
	}
	
	/*
	 * tagMaxNum (Int)
	 */
	public Integer getTagMaxNum() {
		return getNumberValue(KEY_TAG_MAX_NUM);
	}
	
	/*
	 * groupMaxNum (Int)
	 */
	public Integer getGroupMaxNum() {
		return getNumberValue(KEY_GROUP_MAX_NUM);
	}

	/*
	 * functions (Object)
	 * @since SmartSDK V2.12
	 */
	public Functions getFunctions() {
		Map<String, Object> value = getObjectValue(KEY_FUNCTIONS);
		if (value == null) {
			return null;
		}
		return new Functions(value);
	}
	
	public static class Functions extends Element {

		private static final String KEY_PHONETIC_NAME = "phoneticName"; // SmartSDK V2.12

		Functions(Map<String, Object> value) {
			super(value);
		}
		
	    /*
	     * phoneticName (Boolean)
	     * @since SmartSDK V2.12
	     */
	    public Boolean getPhoneticName() {
	    	return getBooleanValue(KEY_PHONETIC_NAME);
	    } 
	}
}
