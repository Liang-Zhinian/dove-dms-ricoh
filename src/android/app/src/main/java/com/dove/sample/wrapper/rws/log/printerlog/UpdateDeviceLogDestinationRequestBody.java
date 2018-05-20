/*
 *  Copyright (C) 2017 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.log.printerlog;

import java.util.HashMap;
import java.util.Map;

import com.dove.sample.wrapper.common.RequestBody;
import com.dove.sample.wrapper.common.Utils;
import com.dove.sample.wrapper.common.WritableElement;
import com.dove.sample.wrapper.json.EncodedException;
import com.dove.sample.wrapper.json.JsonUtils;
import com.dove.sample.wrapper.log.Logger;

/*
 * @since SmartSDk v2.40
 */
public class UpdateDeviceLogDestinationRequestBody extends WritableElement implements RequestBody {

	/**
     * Define the prefix for log information with abbreviation package and class name
     */
    private final static String PREFIX = "printerlog:updateDevDesReq:";
	private static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";
	private static final String KEY_HDD_PATH = "hddPath";
	private static final String KEY_FILE_NAME_FORMAT = "fileNameFormat";

	public UpdateDeviceLogDestinationRequestBody() {
		super(new HashMap<String, Object>());
	}

	public UpdateDeviceLogDestinationRequestBody(Map<String, Object> values) {
		super(values);
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
	 * hddPath (String)
	 * 
	 */
	public String getHddPath() {
		return getStringValue(KEY_HDD_PATH);
	}

	public void setHddPath(String value) {
		setStringValue(KEY_HDD_PATH, value);
	}

	public String removeHddPath() {
		return removeStringValue(KEY_HDD_PATH);
	}

	/*
	 * fileNameFormat (String)
	 * 
	 */
	public String getFileNameFormat() {
		return getStringValue(KEY_FILE_NAME_FORMAT);
	}

	public void setFileNameFormat(String value) {
		setStringValue(KEY_FILE_NAME_FORMAT, value);
	}

	public String removeFileNameFormat() {
		return removeStringValue(KEY_FILE_NAME_FORMAT);
	}
}
