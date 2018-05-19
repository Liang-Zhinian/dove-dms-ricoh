/*
 *  Copyright (C) 2017 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.service.network;

import java.util.Map;

import com.dove.sample.wrapper.common.Element;
import com.dove.sample.wrapper.common.ResponseBody;

/*
 * @since SmartSDK v2.40
 */
public class GetRebootResponseBody extends Element implements ResponseBody{
	private static final String KEY_STATUS    = "status";

	public GetRebootResponseBody(Map<String, Object> values) {
		super(values);
	}
	
	/*
	 * status (String)
	 */
	public String getStatus() {
		return getStringValue(KEY_STATUS);
	}

}
