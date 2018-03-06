/*
 *  Copyright (C) 2013 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.json;

public interface Decoder {
	
	public <T> T decode(String source, Class<? extends T> clazz) throws DecodedException;
	
}
