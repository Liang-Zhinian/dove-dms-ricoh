/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */

package com.dove.sample.wrapper.rws.property;
import java.util.Map;

import com.dove.sample.wrapper.common.Element;
import com.dove.sample.wrapper.common.ResponseBody;

/*
 * @since SmartSDK V2.12
 */
public class GetMachineActionResponseBody extends Element implements ResponseBody{
	
	private static final String KEY_MACHINE_ACTION_WHEN_LIMIT_IS_REACHED = "machineActionWhenLimitIsReached";
	
	GetMachineActionResponseBody(Map<String, Object> values) {
		super(values);
	}

	/*
	 * machineActionWhenLimitIsReached (String)
	 * @since SmartSDK V2.12
	 */
	public String getMachineActionWhenLimitIsReached() {
		return getStringValue(KEY_MACHINE_ACTION_WHEN_LIMIT_IS_REACHED);
	}
}