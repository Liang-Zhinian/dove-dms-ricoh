package com.dove.sample.wrapper.rws.property;

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
public class UpdateAddressbookRequestBody extends WritableElement implements RequestBody {
	
	/**
     * Define the prefix for log information with abbreviation package and class name
     */
    private final static String PREFIX = "property:UpdateAddressReq:";
    
    private static final String CONTENT_TYPE_JSON         = "application/json; charset=utf-8";
    private static final String KEY_AUTO_DELETE_USER       = "autoDeleteUser";


	public UpdateAddressbookRequestBody() {
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
	 * autoDeleteUser (String)
	 */
	public String getAutoDeleteUser() {
    	return getStringValue(KEY_AUTO_DELETE_USER);
    }
    public void setAutoDeleteUser(String value) {
    	setStringValue(KEY_AUTO_DELETE_USER, value);
    }
    public String removeAutoDeleteUser() {
    	return removeStringValue(KEY_AUTO_DELETE_USER);
    }

}
