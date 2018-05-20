/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.function.scan.supported;

import java.util.List;

import com.dove.sample.function.common.Conversions;
import com.dove.sample.wrapper.rws.service.scanner.Capability.MagnificationSizeCapability;

public final class MagnificationSizeSupported {

	private final List<String> supportedScanSize;
	private final MaxMinSupported supportedCustomLongEdge; //SmartSDK V2.12
	private final MaxMinSupported supportedCustomShortEdge; //SmartSDK V2.12
	private final MaxMinSupported supportedCustomX;
	private final MaxMinSupported supportedCustomY;

	public MagnificationSizeSupported (MagnificationSizeCapability capability) {
		supportedScanSize = Conversions.getList(capability.getSizeList());
		supportedCustomLongEdge = MaxMinSupported.getMaxMinSupported(capability.getCustomLongEdgeRange());
		supportedCustomShortEdge = MaxMinSupported.getMaxMinSupported(capability.getCustomShortEdgeRange());
		supportedCustomX = MaxMinSupported.getMaxMinSupported(capability.getCustomXRange());
		supportedCustomY = MaxMinSupported.getMaxMinSupported(capability.getCustomYRange());
	}

	public List<String> getSupportedScanSize() {
		return supportedScanSize;
	}

	/**
	 * @since SmartSDK V2.12 
	 */
	public MaxMinSupported getSupportedCustomLongEdge(){
		return supportedCustomLongEdge;
	}
	
	/**
	 * @since SmartSDK V2.12 
	 */
	public MaxMinSupported getSupportedCustomShortEdge(){
		return supportedCustomShortEdge;
	}
	
	public MaxMinSupported getSupportedCustomX() {
		return supportedCustomX;
	}

	public MaxMinSupported getSupportedCustomY() {
		return supportedCustomY;
	}

}
