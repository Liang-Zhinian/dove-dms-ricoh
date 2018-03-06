/*
 *  Copyright (C) 2017 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.property;

import java.util.Map;

import com.dove.sample.wrapper.common.Element;
import com.dove.sample.wrapper.common.ResponseBody;

/*
 * @since SmartSDK v2.40
 */
public class GetUsbResponseBody extends Element implements ResponseBody{
	private static final String KEY_NOTIFY_UNSUPPORT    = "notifyUnsupport";

	public GetUsbResponseBody(Map<String, Object> values) {
		super(values);
	}
	
	/*
	 * notifyUnsupport (String)
	 */
	public String getNotifyUnsupport() {
		return getStringValue(KEY_NOTIFY_UNSUPPORT);
	}

	
}
