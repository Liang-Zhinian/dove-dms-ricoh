/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.service.fax;

import java.util.List;
import java.util.Map;

import com.dove.sample.wrapper.common.ArrayElement;
import com.dove.sample.wrapper.common.RequestBody;
import com.dove.sample.wrapper.common.Utils;
import com.dove.sample.wrapper.common.WritableElement;
import com.dove.sample.wrapper.json.EncodedException;
import com.dove.sample.wrapper.json.JsonUtils;
import com.dove.sample.wrapper.log.Logger;

/*
 * @since SmartSDK V2.12
 */
public class RemoveFcodeInformationBoxRequestBody extends WritableElement implements RequestBody {
    
    /**
     * Define the prefix for log information with abbreviation package and class name
     */
    private final static String PREFIX = "fax:RmveFcodeInfoBoxReq:";
	
	private static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";

	private static final String KEY_FCODE_INFORMATION_BOX_INFO	= "fcodeInformationBoxInfo";

	RemoveFcodeInformationBoxRequestBody(Map<String, Object> values) {
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

	/*
	 * fcodeInformationBoxInfo (Array[Object])
	 * @since SmartSDK V2.12
	 */
	public FcodeInformationBoxInfoArray getFcodeInformationBoxInfo() {
		List<Map<String, Object>> value = getArrayValue(KEY_FCODE_INFORMATION_BOX_INFO);
		if (value == null) {
			value = Utils.createElementList();
			setArrayValue(KEY_FCODE_INFORMATION_BOX_INFO, value);
		}
		return new FcodeInformationBoxInfoArray(value);
	}
    public FcodeInformationBoxInfoArray removeFcodeInformationBoxInfo() {
		List<Map<String, Object>> value = removeArrayValue(KEY_FCODE_INFORMATION_BOX_INFO);
		if (value == null) {
			return null;
		}
		return new FcodeInformationBoxInfoArray(value);
    }

	/*
	 * @since SmartSDK V2.12
	 */
    public static class FcodeInformationBoxInfoArray extends ArrayElement<FcodeInformationBoxInfo> {
		
    	FcodeInformationBoxInfoArray(List<Map<String, Object>> list) {
			super(list);
		}
		
		/*
		 * @since SmartSDK V2.12
		 */
		public boolean add(FcodeInformationBoxInfo value) {
			if (value == null) {
				throw new NullPointerException("value must not be null.");
			}
			return list.add(value.cloneValues());
		}
		
		/*
		 * @since SmartSDK V2.12
		 */
		public FcodeInformationBoxInfo remove(int index) {
			Map<String, Object> value = list.remove(index);
			if (value == null) {
				return null;
			}
			return new FcodeInformationBoxInfo(value);
		}
		
		/*
		 * @since SmartSDK V2.12
		 */
		public void clear() {
			list.clear();
		}
		
		@Override
		protected FcodeInformationBoxInfo createElement(Map<String, Object> values) {
			return new FcodeInformationBoxInfo(values);
		}        
    }
	
	/*
	 * @since SmartSDK V2.12
	 */
	public static class FcodeInformationBoxInfo extends WritableElement {
		
		private static final String KEY_BOX_ID		= "boxId";
    	private static final String KEY_PASSWORD	= "password";
		
    	FcodeInformationBoxInfo(Map<String, Object> values) {
			super(values);
		}
		
		/*
		 * boxId (Number)
		 * @since SmartSDK V2.12
		 */
		public Integer getBoxId() {
			return getNumberValue(KEY_BOX_ID);
		}
		public void setBoxId(Integer value) {
			setNumberValue(KEY_BOX_ID, value);
		}
		public Integer removeBoxId() {
			return removeNumberValue(KEY_BOX_ID);
		}
		
		/*
		 * password (String)
		 * @since SmartSDK V2.12
		 */
		public String getPassword() {
			return getStringValue(KEY_PASSWORD);
		}
		public void setPassword(String value) {
			setStringValue(KEY_PASSWORD, value);
		}
		public String removePassword() {
			return removeStringValue(KEY_PASSWORD);
		}
		
	}

}
