/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.service.fax;

import java.util.Map;

import com.dove.sample.wrapper.common.Element;
import com.dove.sample.wrapper.common.ResponseBody;

/*
 * @since SmartSDK V2.12
 */
public class CreateFaxFileResponseBody extends Element implements ResponseBody {

	private static final String KEY_FILE_ID	= "fileId";

	CreateFaxFileResponseBody(Map<String, Object> values) {
		super(values);
	}

	/*
	 * fileId (String)
	 * @since SmartSDK V2.12
	 */
	public String getFileId() {
		return getStringValue(KEY_FILE_ID);
	}

}
