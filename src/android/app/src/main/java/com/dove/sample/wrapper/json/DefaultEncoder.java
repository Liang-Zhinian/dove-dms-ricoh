/*
 *  Copyright (C) 2013 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.json;

import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONException;

class DefaultEncoder implements Encoder {
	
	public String encode(Object source) throws EncodedException {
		try {
			return JSON.encode(source);
		} catch (JSONException e) {
			throw new EncodedException(e.getMessage());
		}
	}

}
