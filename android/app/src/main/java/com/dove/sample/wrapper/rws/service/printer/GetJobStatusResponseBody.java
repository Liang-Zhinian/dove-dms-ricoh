/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.service.printer;

import java.util.List;
import java.util.Map;

import com.dove.sample.wrapper.common.ArrayElement;
import com.dove.sample.wrapper.common.Element;
import com.dove.sample.wrapper.common.ResponseBody;
import com.dove.sample.wrapper.rws.service.printer.GetPrinterStatusResponseBody.PrinterStatusReasonsArray;

public class GetJobStatusResponseBody extends JobInfo implements ResponseBody {

	private static final String KEY_EVENT_DETAIL		        = "eventDetail";
	private static final String KEY_JOB_STATUS_REASONS_DETAILS  = "jobStatusReasonsDetails"; // SmartSDK V2.12
	
	public GetJobStatusResponseBody(Map<String, Object> values) {
		super(values);
	}
	
	/*
	 * eventDetail (String)
	 */
	public String getEventDetail() {		
		return getStringValue(KEY_EVENT_DETAIL);
	}
	
	/*
	 * jobStatusReasonsDetails (Arrary[Object])
	 * @Since SmartSDK V2.12
	 */
	public JobStatusReasonsDetailsArray getJobStatusReasonsDetails() {
		List<Map<String, Object>> value = getArrayValue(KEY_JOB_STATUS_REASONS_DETAILS);
		if (value == null) {
			return null;
		}
		return new JobStatusReasonsDetailsArray(value);
	}
	
	/*
	 * @Since SmartSDK V2.12
	 */
	public static class JobStatusReasonsDetailsArray extends ArrayElement<JobStatusReasonsDetails> {

		JobStatusReasonsDetailsArray(List<Map<String, Object>> list) {
			super(list);
		}
		
		@Override
		protected JobStatusReasonsDetails createElement(Map<String, Object> values) {
			return new JobStatusReasonsDetails(values);
		}
	}
	
	/*
	 * @Since SmartSDK V2.12
	 */
	public static class JobStatusReasonsDetails extends Element {
		private static final String KEY_JOB_STATUS_REASON = "jobStatusReason"; // SmartSDk V2.12
		private static final String KEY_MESSAGE_ID = "messageId"; //SamrtSDK V2.12

		JobStatusReasonsDetails(Map<String, Object> values) {
			super(values);
		}
		
		/*
		 * jobStatusReason (String)
		 * @Since SmartSDK V2.12
		 */
		public String getJobStatusReason() {
			return getStringValue(KEY_JOB_STATUS_REASON);
		}
		
		/*
		 * messageId (String)
		 * @Since SmartSDK V2.12
		 */
		public String getMessageId() {
			return getStringValue(KEY_MESSAGE_ID);
		}
		
		
		
	}
}
