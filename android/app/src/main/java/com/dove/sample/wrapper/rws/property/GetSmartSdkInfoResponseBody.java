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
public class GetSmartSdkInfoResponseBody extends Element implements ResponseBody{
	private static final String KEY_VERSION = "version";
	
	GetSmartSdkInfoResponseBody(Map<String, Object> values) {
		super(values);
	}

	/*
	 * version (String)
	 * @since SmartSDK V2.12
	 */
	public String getVersion() {
		return getStringValue(KEY_VERSION);
	}
}