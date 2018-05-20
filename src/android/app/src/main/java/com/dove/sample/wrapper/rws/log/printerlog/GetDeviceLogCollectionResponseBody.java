/*
 *  Copyright (C) 2017 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.log.printerlog;

import java.util.List;

import java.util.Map;

import com.dove.sample.wrapper.common.Element;
import com.dove.sample.wrapper.common.ResponseBody;

/*
 * @since SmartSDk v2.40
 */
public class GetDeviceLogCollectionResponseBody extends Element implements ResponseBody {

	private static final String KEY_COLLECT_SETTING = "collectSetting";
	private static final String KEY_SUPPORT_JOB_LOG_TYPE = "supportJobLogType";
	private static final String KEY_JOB_LOG_TYPE = "jobLogType";
	
	
	GetDeviceLogCollectionResponseBody(Map<String, Object> values) {
		super(values);
	}

	/*
	 * collectSetting (Boolean)
	 */
	public Boolean getCollectSetting() {
		return getBooleanValue(KEY_COLLECT_SETTING);
	}

	/*
	 * supportJobLogType (Array[String])
	 */
	public List<String> getSupportJobLogType() {
		return getArrayValue(KEY_SUPPORT_JOB_LOG_TYPE);
	}

	/*
	 * jobLogType (Array[String])
	 */
	public List<String> getJobLogType() {
		return getArrayValue(KEY_JOB_LOG_TYPE);
	}
	
}
