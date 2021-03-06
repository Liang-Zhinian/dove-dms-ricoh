/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.service.fax;

import java.util.List;
import java.util.Map;

import com.dove.sample.wrapper.common.ArrayElement;
import com.dove.sample.wrapper.common.Element;
import com.dove.sample.wrapper.common.ResponseBody;

/*
 * @since SmartSDK V2.12
 */
public class GetFcodeInformationBoxResponseBody extends Element implements ResponseBody {

	private static final String KEY_FCODE_INFORMATION_BOX_INFO	= "fcodeInformationBoxInfo";

	GetFcodeInformationBoxResponseBody(Map<String, Object> values) {
		super(values);
	}

	/*
	 * fcodeInformationBoxInfoArray (Array[Object])
	 * @since SmartSDK V2.12
	 */
	public FcodeInformationBoxInfoArray getFcodeInformationBoxInfoArray() {
        List<Map<String, Object>> value = getArrayValue(KEY_FCODE_INFORMATION_BOX_INFO);
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

        @Override
        protected FcodeInformationBoxInfo createElement(Map<String, Object> values) {
            return new FcodeInformationBoxInfo(values);
        }
        
    }
    
    /*
	 * @since SmartSDK V2.12
	 */
    public static class FcodeInformationBoxInfo extends Element {

    	private static final String KEY_BOX_ID			= "boxId";
    	private static final String KEY_FCODE			= "fcode";
    	private static final String KEY_PASSWORD_LOCK	= "passwordLock";
    	private static final String KEY_BOX_NAME		= "boxName";
    	private static final String KEY_DOCUMENT		= "document";

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

    	/*
    	 * fcode (String)
    	 * @since SmartSDK V2.12
    	 */
    	public String getFcode() {
    		return getStringValue(KEY_FCODE);
    	}

    	/*
    	 * passwordLock (Boolean)
    	 * @since SmartSDK V2.12
    	 */
    	public Boolean getPasswordLock() {
    		return getBooleanValue(KEY_PASSWORD_LOCK);
    	}

    	/*
    	 * boxName (String)
    	 * @since SmartSDK V2.12
    	 */
    	public String getBoxName() {
    		return getStringValue(KEY_BOX_NAME);
    	}

    	/*
    	 * document (Boolean)
    	 * @since SmartSDK V2.12
    	 */
    	public Boolean getDocument() {
    		return getBooleanValue(KEY_DOCUMENT);
    	}

    }

}
