/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.counter;

import java.util.List;
import java.util.Map;

import com.dove.sample.wrapper.common.ArrayElement;
import com.dove.sample.wrapper.common.Element;

/*
 * @since SmartSDK V2.12
 */
public class ManageInfo extends Element {
	
	private static final String KEY_COUNT_UNIT_TYPE  = "countUnitType";
	private static final String KEY_MANAGE_INFO_LIST  = "manageInfoList";
	
	ManageInfo(Map<String, Object> values) {
		super(values);
	}
	
	/*
	 * countUnitType (Number)
	 * @since SmartSDK V2.12
	 */
	public Integer getCountUnitType() {
		return getNumberValue(KEY_COUNT_UNIT_TYPE);
	}
	
	/*
	 * manageInfoList (Array[Object])
	 * @since SmartSDK V2.12
	 */
	public ManageInfoList getManageInfoList() {
    	List<Map<String, Object>> value = getArrayValue(KEY_MANAGE_INFO_LIST);
    	if(value == null) {
    		return null;
    	}
    	return new ManageInfoList(value);
    }
	/*
	 * @since SmartSDK V2.12
	 */
	public static class ManageInfoList extends ArrayElement<CountInfo> {
		
		ManageInfoList(List<Map<String, Object>> list) {
            super(list);
        }

        @Override
        protected CountInfo createElement(Map<String, Object> values) {
            return new CountInfo(values);
        }
	}
	
	/*
	 * @since SmartSDK V2.12
	 */
	public static class CountInfo extends Element {
		
		private static final String KEY_NAME 	= "name";
		private static final String KEY_SUPPORT = "support";
		private static final String KEY_VISIBLE = "visible";
		
		CountInfo(Map<String, Object> values) {
			super(values);
		}

		/*
		 * name (String)
		 * @since SmartSDK V2.12
		 */
	    public String getName() {
	    	return getStringValue(KEY_NAME);
	    }
	    
	    /*
	     * support (Boolean)
	     * @since SmartSDK V2.12
	     */
	    public Boolean getSupport() {
	    	return getBooleanValue(KEY_SUPPORT);
	    } 
	    
	    /*
	     * visible (Boolean)
	     * @since SmartSDK V2.12
	     */
	    public Boolean getVisible() {
	    	return getBooleanValue(KEY_VISIBLE);
	    }
	}
}