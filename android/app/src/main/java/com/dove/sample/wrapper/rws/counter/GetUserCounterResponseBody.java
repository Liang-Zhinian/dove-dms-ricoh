/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.counter;

import java.util.Map;

import com.dove.sample.wrapper.common.ResponseBody;

public class GetUserCounterResponseBody extends UserCounter implements ResponseBody {
	
	private static final String KEY_MANAGE_INFO  = "manageInfo"; // SmartSDK V2.12

	GetUserCounterResponseBody(Map<String, Object> values) {
		super(values);
	}
	
	/*
	 * manageInfo (Object)
	 * @since SmartSDK V2.12
	 */
	public ManageInfo getManageInfo() {
		Map<String, Object> value = getObjectValue(KEY_MANAGE_INFO);
		if (value == null) {
			return null;
		}
		return new ManageInfo(value);
	}

	
}
