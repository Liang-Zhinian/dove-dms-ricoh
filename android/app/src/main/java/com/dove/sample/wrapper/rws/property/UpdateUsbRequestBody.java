/*
 *  Copyright (C) 2017 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.property;

import java.util.HashMap;

import com.dove.sample.wrapper.common.RequestBody;
import com.dove.sample.wrapper.common.Utils;
import com.dove.sample.wrapper.common.WritableElement;
import com.dove.sample.wrapper.json.EncodedException;
import com.dove.sample.wrapper.json.JsonUtils;
import com.dove.sample.wrapper.log.Logger;

/*
 * @since SmartSDk v2.40
 */
public class UpdateUsbRequestBody extends WritableElement implements RequestBody{
	
	private final static String PREFIX = "property:UpdateUsbReq:";			
	private static final String CONTENT_TYPE_JSON               = "application/json; charset=utf-8";
	private static final String KEY_NOTIFY_UNSUPPORT            = "notifyUnsupport";

	public UpdateUsbRequestBody() {
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
	 * notifyUnsupport (String)
	 */
	public String getNotifyUnsupport() {
		return getStringValue(KEY_NOTIFY_UNSUPPORT);
	}
	public void setNotifyUnsupport(String value) {
		setStringValue(KEY_NOTIFY_UNSUPPORT, value);
	}
	public String removeNotifyUnsupport(){
		return removeStringValue(KEY_NOTIFY_UNSUPPORT);
	}

}
