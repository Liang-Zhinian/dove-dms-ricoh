/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.function.scan.supported;

import com.dove.sample.wrapper.common.MagnificationElement;

public final class MagnificationSupported {

	private Boolean fittingSupported;
	private String maxValue, minValue, step;
	private Boolean userDefined; //SmartSDK V2.12

	public MagnificationSupported(MagnificationElement value) {
		fittingSupported = value.getFitting();
		maxValue = value.getMaxValue();
		minValue = value.getMinValue();
		step = value.getStep();
		userDefined = value.getUserDefined();
	}

	public Boolean getFittingSupported() {
		return fittingSupported;
	}

	public String getMaxValue() {
		return maxValue;
	}

	public String getMinValue() {
		return minValue;
	}

	public String getStep() {
		return step;
	}

	/*
	 * @since SmartSDK V2.12
	 */
	public Boolean getUserDefined() {
		return userDefined;
	}

}
