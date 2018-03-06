/*
 *  Copyright (C) 2013 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.addressbook;

import java.util.List;
import java.util.Map;

import com.dove.sample.wrapper.common.ArrayElement;

public class EntryArray extends ArrayElement<Entry> {
	
	EntryArray(List<Map<String, Object>> list) {
		super(list);
	}
	
	@Override
	protected Entry createElement(Map<String, Object> values) {
		return new Entry(values);
	}
	
}
