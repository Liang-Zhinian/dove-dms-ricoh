/*
 *  Copyright (C) 2017 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.service.network;

import java.util.HashMap;

import com.dove.sample.wrapper.common.RequestBody;
import com.dove.sample.wrapper.common.Utils;
import com.dove.sample.wrapper.common.WritableElement;
import com.dove.sample.wrapper.json.EncodedException;
import com.dove.sample.wrapper.json.JsonUtils;
import com.dove.sample.wrapper.log.Logger;

/*
 * @since SmartSDK v2.40
 */
public class NetworkRebootRequestBody extends WritableElement implements RequestBody{
	
	/**
     * Define the prefix for log information with abbreviation package and class name
     */
    private final static String PREFIX = "network:NetworkRebootReq:";
    
	private static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";
	private static final String KEY_MODE          = "mode";

	public NetworkRebootRequestBody() {
		super(new HashMap<String, Object>());
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
	 * mode (Number)
	 */
	public Integer getMode() {
		return getNumberValue(KEY_MODE);
	}
	public void setMode(Integer value) {
		setNumberValue(KEY_MODE, value);
	}
	public Integer removeMode() {
		return removeNumberValue(KEY_MODE);
	}

}
