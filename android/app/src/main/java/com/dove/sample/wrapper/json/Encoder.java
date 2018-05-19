/*
 *  Copyright (C) 2013 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.json;

public interface Encoder {
	
	public String encode(Object source) throws EncodedException;

}
