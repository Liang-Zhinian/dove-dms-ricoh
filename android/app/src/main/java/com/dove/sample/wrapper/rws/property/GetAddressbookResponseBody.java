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
public class GetAddressbookResponseBody extends Element implements ResponseBody {
	private static final String KEY_AUTO_DELETE_USER      = "autoDeleteUser";

	GetAddressbookResponseBody(Map<String, Object> values) {
		super(values);
	}
	
	/*
	 * autoDeleteUser (String)
	 */
	public String getAutoDeleteUser() {
        return getStringValue(KEY_AUTO_DELETE_USER);
    }

}
