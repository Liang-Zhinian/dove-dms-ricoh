/*
 *  Copyright (C) 2017 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.system;

import java.util.List;
import java.util.Map;

import com.dove.sample.wrapper.common.Element;
import com.dove.sample.wrapper.common.ResponseBody;

/*
 * @since SmartSDK v2.40
 */
public class AdminAuthResponseBody extends Element implements ResponseBody{
	private static final String KEY_USER_ID = "userID";
	private static final String KEY_USER_ROLE = "userRole";

	public AdminAuthResponseBody(Map<String, Object> values) {
		super(values);
	}
	
	/*
	 * userID (String)
	 */
	public String getUserID() {
		return getStringValue(KEY_USER_ID);
	}
	
	/*
	 * userRole (Object)
	 */
	public UserRole getUserRole() {
		Map<String, Object> value = getObjectValue(KEY_USER_ROLE);
		if (value == null) {
			return null;
		}
		return new UserRole(value);
	}
	
	public static class UserRole extends Element{
		private static final String KEY_ROLE_TYPE  = "roleType";

		UserRole(Map<String, Object> values) {
			super(values);
		}
		
		/*
		 * roleType (Array[String])
		 */
		public List<String> getRoleType() {
			return  getArrayValue(KEY_ROLE_TYPE);
		}
	}
	
	

}
