/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.function.scan.attribute.standard;

import com.dove.sample.function.scan.attribute.ScanRequestAttribute;
import com.dove.sample.function.scan.supported.MaxMinSupported;
import com.dove.sample.wrapper.common.RangeElement;

public final class OriginalSizeCustomY implements ScanRequestAttribute {

	private static final String SCAN_ORIGINAL_CUSTOM_SIZE_Y = "originalSizeCustomY";

	private final String sizeY;

	public OriginalSizeCustomY(int y) {
		this.sizeY = Integer.toString(y);
	}

    public OriginalSizeCustomY(String y) {
        this.sizeY = y;
    }

    @Override
    public String toString() {
        return this.sizeY;
    }

	@Override
	public Class<?> getCategory() {
		return OriginalSizeCustomY.class;
	}

	@Override
	public String getName() {
		return SCAN_ORIGINAL_CUSTOM_SIZE_Y;
	}

	@Override
	public Object getValue() {
		return this.sizeY;
	}


	public static MaxMinSupported getSupportedValue(RangeElement values) {
		return MaxMinSupported.getMaxMinSupported(values);
	}
}
