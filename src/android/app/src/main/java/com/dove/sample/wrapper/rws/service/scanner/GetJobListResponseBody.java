/*
 *  Copyright (C) 2015 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.service.scanner;

import java.util.List;
import java.util.Map;

import com.dove.sample.wrapper.common.ArrayElement;
import com.dove.sample.wrapper.common.ResponseBody;

/*
 * @since SmartSDK V2.00
 */
public class GetJobListResponseBody extends ArrayElement<JobInfo> implements ResponseBody {

	GetJobListResponseBody(List<Map<String, Object>> list) {
		super(list);
	}

	@Override
	protected JobInfo createElement(Map<String, Object> values) {
		return new JobInfo(values);
	}

}
