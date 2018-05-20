/*
 *  Copyright (C) 2017 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.udp;

import java.util.HashMap;
import java.util.Map;

import com.dove.sample.wrapper.common.RequestBody;
import com.dove.sample.wrapper.common.WritableElement;
import com.dove.sample.wrapper.json.EncodedException;
import com.dove.sample.wrapper.json.JsonUtils;

/*
 * @since SmartSDK V2.31
 */
public class SendTransmissionRequestBody extends WritableElement implements RequestBody {

	private static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";
	private static final String KEY_SEND_METHOD = "sendMethod";
	private static final String KEY_DATA = "data";
	private static final String KEY_IP = "ip";
	private static final String KEY_PORT = "port";

	public SendTransmissionRequestBody() {
		super(new HashMap<String, Object>());
	}

	public SendTransmissionRequestBody(Map<String, Object> values) {
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
			e.printStackTrace();
			return "{}";
		}
	}

	/*
	 * sendMethod (String)
	 */
	public String getSendMethod() {
		return getStringValue(KEY_SEND_METHOD);
	}

	public void setSendMethod(String value) {
		setStringValue(KEY_SEND_METHOD, value);
	}

	public String removeSendMethod() {
		return removeStringValue(KEY_SEND_METHOD);
	}

	/*
	 * data (String)
	 */
	public String getData() {
		return getStringValue(KEY_DATA);
	}

	public void setData(String value) {
		setStringValue(KEY_DATA, value);
	}

	public String removeData() {
		return removeStringValue(KEY_DATA);
	}

	/*
	 * ip (String)
	 */
	public String getIp() {
		return getStringValue(KEY_IP);
	}

	public void setIp(String value) {
		setStringValue(KEY_IP, value);
	}

	public String removeIp() {
		return removeStringValue(KEY_IP);
	}

	/*
	 * port (Number)
	 */
	public Integer getPort() {
		return getNumberValue(KEY_PORT);
	}

	public void setPort(Integer value) {
		setNumberValue(KEY_PORT, value);
	}

	public Integer removePort() {
		return removeNumberValue(KEY_PORT);
	}
}
