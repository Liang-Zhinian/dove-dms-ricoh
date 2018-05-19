/*
 *  Copyright (C) 2013 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.addressbook;

import java.util.Map;

import com.dove.sample.wrapper.common.ResponseBody;

public class UpdateTagResponseBody extends Tag implements ResponseBody {

	UpdateTagResponseBody(Map<String, Object> values) {
		super(values);
	}

}
