/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.addressbook;

import com.dove.sample.wrapper.common.RequestBody;
import com.dove.sample.wrapper.common.Utils;
import com.dove.sample.wrapper.json.EncodedException;
import com.dove.sample.wrapper.json.JsonUtils;
import com.dove.sample.wrapper.log.Logger;

import java.util.HashMap;
import java.util.Map;

public class UpdateEntryRequestBody extends Entry implements RequestBody {

	private static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";
	
    /**
     * Define the prefix for log information with abbreviation package and class name
     */
    private final static String PREFIX = "addressBook:UpdateEntryReq:";
	
	public UpdateEntryRequestBody() {
		super(new HashMap<String, Object>());
	}
	public UpdateEntryRequestBody(Map<String, Object> values) {
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

}
