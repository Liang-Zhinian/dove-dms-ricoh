/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.service.fax;

import java.util.Map;

import com.dove.sample.wrapper.common.Element;

/*
 * @since SmartSDK V2.12
 */
public class ManualEmailReceive extends Element {

	private static final String KEY_MANUAL_EMAIL_RECEIVE_STATUS	= "manualEmailReceiveStatus";
	private static final String KEY_NEW_EMAIL_RECEIVE			= "newEmailReceive";
	private static final String KEY_STAT_REASON					= "statReason";

	ManualEmailReceive(Map<String, Object> values) {
		super(values);
	}

	/*
	 * manualEmailReceiveStatus (String)
	 * @since SmartSDK V2.12
	 */
	public String getManualEmailReceiveStatus() {
		return getStringValue(KEY_MANUAL_EMAIL_RECEIVE_STATUS);
	}

	/*
	 * newEmailReceive (Number)
	 * @since SmartSDK V2.12
	 */
	public Integer getNewEmailReceive() {
		return getNumberValue(KEY_NEW_EMAIL_RECEIVE);
	}

	/*
	 * statReason (String)
	 * @since SmartSDK V2.12
	 */
	public String getStatReason() {
		return getStringValue(KEY_STAT_REASON);
	}

}
