/*
 *  Copyright (C) 2013 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.function.scan.attribute.standard;

import com.dove.sample.function.scan.attribute.ScanRequestAttribute;
import com.dove.sample.function.scan.supported.MaxMinSupported;
import com.dove.sample.wrapper.common.RangeElement;

public final class ManualDensity implements ScanRequestAttribute {
	
	private static final String NAME_MANUAL_DENSITY = "manualDensity";
	
	private final int density;
	
	public ManualDensity(int density) {
		this.density = density;
	}

    @Override
    public String toString() {
        return Integer.toString(density);
    }

	@Override
	public Class<?> getCategory() {
		return ManualDensity.class;
	}

	@Override
	public String getName() {
		return NAME_MANUAL_DENSITY;
	}

	@Override
	public Object getValue() {
		return Integer.valueOf(density);
	}

    public static MaxMinSupported getSupportedValue(RangeElement value){
        return MaxMinSupported.getMaxMinSupported(value);
    }

}
