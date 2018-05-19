/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.service.scanner;

import java.util.List;
import java.util.Map;

import com.dove.sample.wrapper.common.ArrayElement;
import com.dove.sample.wrapper.common.Element;
import com.dove.sample.wrapper.common.ResponseBody;

/*
 * @since SmartSDK V2.12
 */
public class GetSentResultResponseBody extends Element implements ResponseBody {
	
	private static final String KEY_SENT_RESULTS	= "sentResults";
	
	GetSentResultResponseBody(Map<String, Object> values) {
		super(values);
	}
	
	/*
	 * sentResults (Array[Object])
	 * @since SmartSDK V2.12
	 */
	public SentResultsArray getSentResults() {
		List<Map<String, Object>> value = getArrayValue(KEY_SENT_RESULTS);
		if (value == null) {
			return null;
		}
		return new SentResultsArray(value);
	}	
	
	/*
	 * @since SmartSDK V2.12
	 */
	public static class SentResultsArray extends ArrayElement<SentResults> {
		
		SentResultsArray(List<Map<String, Object>> list) {
			super(list);
		}
		
		@Override
		protected SentResults createElement(Map<String, Object> values) {
			return new SentResults(values);
		}
	}
	
	/*
	 * @since SmartSDK V2.12
	 */
	public static class SentResults extends Element {
		
		private static final String KEY_ID					= "id";
		private static final String KEY_TIME				= "time";
		private static final String KEY_DESTINATION			= "destination";
		private static final String KEY_DESTINATION_COUNT	= "destinationCount";
		private static final String KEY_MODE				= "mode";
		private static final String KEY_MAIL_ENCRYPT		= "mailEncrypt";
		private static final String KEY_USER_NAME			= "userName";
		private static final String KEY_FILE_NAME			= "fileName";
		private static final String KEY_PAGE_COUNT		    = "pageCount";
		private static final String KEY_RESULT			    = "result";
		
		SentResults(Map<String, Object> values) {
			super(values);
		}
		
		/*
		 * id (String)
		 * @since SmartSDK V2.12
		 */
		public String getId() {
			return getStringValue(KEY_ID);
		}	
		
		/*
		 * time (String)
		 * @since SmartSDK V2.12
		 */
		public String getTime() {
			return getStringValue(KEY_TIME);
		}
		
		/*
		 * destination (String)
		 * @since SmartSDK V2.12
		 */
		public String getDestination() {
			return getStringValue(KEY_DESTINATION);
		}
		
		/*
		 * destinationCount (String)
		 * @since SmartSDK V2.12
		 */
		public String getDestinationCount() {
			return getStringValue(KEY_DESTINATION_COUNT);
		}
		
		/*
		 * mode (String)
		 * @since SmartSDK V2.12
		 */
		public String getMode() {
			return getStringValue(KEY_MODE);
		}
		
		/*
		 * mailEncrypt (Boolean)
		 * @since SmartSDK V2.12
		 */
		public Boolean getMailEncrypt() {
			return getBooleanValue(KEY_MAIL_ENCRYPT);
		}
		
		/*
		 * userName (String)
		 * @since SmartSDK V2.12
		 */
		public String getUserName() {
			return getStringValue(KEY_USER_NAME);
		}
		
		/*
		 * fileName (String)
		 * @since SmartSDK V2.12
		 */
		public String getFileName() {
			return getStringValue(KEY_FILE_NAME);
		}
		
		/*
		 * pageCount (String)
		 * @since SmartSDK V2.12
		 */
		public String getPageCount() {
			return getStringValue(KEY_PAGE_COUNT);
		}
		
		/*
		 * result (String)
		 * @since SmartSDK V2.12
		 */
		public String getResult() {
			return getStringValue(KEY_RESULT);
		}	
	}
	
}
