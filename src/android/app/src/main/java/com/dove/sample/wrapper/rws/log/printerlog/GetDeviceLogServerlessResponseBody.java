/*
 *  Copyright (C) 2017 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.log.printerlog;

import java.util.Map;

import com.dove.sample.wrapper.common.Element;
import com.dove.sample.wrapper.common.ResponseBody;

/*
 * @since SmartSDk v2.40
 */
public class GetDeviceLogServerlessResponseBody extends Element implements ResponseBody {

	private static final String KEY_STATUS_INFO = "statusInfo";
	private static final String KEY_DEVICE_LOG_TRANS_SETTING = "deviceLogTransSetting";

	
	GetDeviceLogServerlessResponseBody(Map<String, Object> values) {
		super(values);
	}

	/*
	 * statusInfo (String)
	 */
	public String getStatusInfo() {
		return getStringValue(KEY_STATUS_INFO);
	}
	
	/*
	 * deviceLogTransSetting (Boolean)
	 */
	public Boolean getDeviceLogTransSetting() {
		return getBooleanValue(KEY_DEVICE_LOG_TRANS_SETTING);
	}
		
}
