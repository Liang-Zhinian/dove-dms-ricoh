/*
 *  Copyright (C) 2013 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.service.printer;

import java.util.List;
import java.util.Map;

import com.dove.sample.wrapper.common.Element;
import com.dove.sample.wrapper.common.ResponseBody;

public class GetSupportedPDLResponseBody extends Element implements ResponseBody {

	private static final String KEY_PDL		= "pdl";

	GetSupportedPDLResponseBody(Map<String, Object> values) {
		super(values);
	}

	/*
	 * pdl (Array[String])
	 */
	public List<String> getPdl() {
		return getArrayValue(KEY_PDL);
	}

}
