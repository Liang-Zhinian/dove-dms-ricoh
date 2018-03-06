/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */

package com.dove.sample.wrapper.rws.counter;

import java.util.List;
import java.util.Map;

import com.dove.sample.wrapper.common.ArrayElement;
import com.dove.sample.wrapper.common.Element;
import com.dove.sample.wrapper.common.ResponseBody;

/*
 * @since SmartSDK V2.12
 */
public class GetUserCounterListResponseBody extends Element implements ResponseBody {

	private static final String KEY_MANAGE_INFO 	= "manageInfo";
	private static final String KEY_USER_INFO_LIST  = "userInfoList";

	GetUserCounterListResponseBody(Map<String, Object> values) {
		super(values);
	}
	
	public ManageInfo getManageInfo() {
		Map<String, Object> value = getObjectValue(KEY_MANAGE_INFO);
		if (value == null) {
			return null;
		}
		return new ManageInfo(value);
	}
	
	public UserInfoList getUserInfoList() {
    	List<Map<String, Object>> value = getArrayValue(KEY_USER_INFO_LIST);
    	if(value == null) {
    		return null;
    	}
    	return new UserInfoList(value);
    }
	
	/*
	 * @since SmartSDK V2.12
	 */
	public static class UserInfoList extends ArrayElement<UserInfoDetail> {
		
		UserInfoList(List<Map<String, Object>> list) {
            super(list);
        }

        @Override
        protected UserInfoDetail createElement(Map<String, Object> values) {
            return new UserInfoDetail(values);
        }
	}
	
	/*
	 * @since SmartSDK V2.12
	 */
	public static class UserInfoDetail extends UserCounter {
		
		private static final String KEY_USER_CODE 		= "userCode";
		
		UserInfoDetail(Map<String, Object> values) {
			super(values);
		}
		
		/*
		 * userCode (String)
		 * @since SmartSDK V2.12
		 */
		public String getUserCode(){
			return getStringValue(KEY_USER_CODE);
		}
	}
}
