/*
 *  Copyright (C) 2015 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.addressbook;

import java.util.List;
import java.util.Map;

import com.dove.sample.wrapper.common.Element;
import com.dove.sample.wrapper.common.ResponseBody;

/*
 * @since SmartSDK V2.00
 */
public class SearchEntryResponseBody extends Element implements ResponseBody {

	private static final String KEY_ENTRY_NUM			= "entryNum";
	private static final String KEY_ACQUIRED_NUMBER		= "acquiredNumber";
	private static final String KEY_NEXT_LINK			= "nextLink";
	private static final String KEY_ENTRIES_DATA		= "entriesData";

	SearchEntryResponseBody(Map<String, Object> values) {
		super(values);
	}

	/*
	 * entryNum (Number)
	 * @since SmartSDK V2.00
	 */
	public Integer getEntryNum() {
		return getNumberValue(KEY_ENTRY_NUM);
	}

	/*
	 * acquiredNumber (Number)
	 * @since SmartSDK V2.00
	 */
	public Integer getAcquiredNumber() {
		return getNumberValue(KEY_ACQUIRED_NUMBER);
	}

	/*
	 * nextLink (String)
	 * @since SmartSDK V2.00
	 */
	public String getNextLink() {
		return getStringValue(KEY_NEXT_LINK);
	}

	/*
	 * entriesData (Array[Object])
	 * @since SmartSDK V2.00
	 */
	public EntryArray getEntriesData() {
		List<Map<String, Object>> value = getArrayValue(KEY_ENTRIES_DATA);
		if (value == null) {
			return null;
		}
		return new EntryArray(value);
	}

}
