/*
 *  Copyright (C) 2013 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.common;

import java.io.InputStream;

public interface BinaryRequestBody extends RequestBody {

	public InputStream getInputStream();
	public int getSize();

}
