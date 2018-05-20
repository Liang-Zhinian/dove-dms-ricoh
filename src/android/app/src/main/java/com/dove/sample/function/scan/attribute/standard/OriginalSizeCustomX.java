/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.function.scan.attribute.standard;

import com.dove.sample.function.scan.attribute.ScanRequestAttribute;
import com.dove.sample.function.scan.supported.MaxMinSupported;
import com.dove.sample.wrapper.common.RangeElement;

public final class OriginalSizeCustomX implements ScanRequestAttribute {

	private static final String SCAN_ORIGINAL_CUSTOM_SIZE_X = "originalSizeCustomX";

	private final String sizeX;

	public OriginalSizeCustomX(int x) {
		this.sizeX = Integer.toString(x);
	}

	public OriginalSizeCustomX(String x){
	    this.sizeX = x;
	}

    @Override
    public String toString() {
        return this.sizeX;
    }

	@Override
	public Class<?> getCategory() {
		return OriginalSizeCustomX.class;
	}

	@Override
	public String getName() {
		return SCAN_ORIGINAL_CUSTOM_SIZE_X;
	}

	@Override
	public Object getValue() {
		return this.sizeX;
	}


	public static MaxMinSupported getSupportedValue(RangeElement values) {
		return MaxMinSupported.getMaxMinSupported(values);
	}
}
