/*
 *  Copyright (C) 2014 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.function.scan.supported;

import com.dove.sample.function.common.Conversions;
import com.dove.sample.wrapper.rws.service.scanner.Capability;

/**
 * @since SmartSDK V1.01
 */
public final class SendStoredFileSettingStoredFileInfoSupported {

	private final MaxLengthSupported supportedFileIdLength;
	private final MaxLengthSupported supportedFilePasswordLength;
	private final int maxSelectFileNumber;

	static SendStoredFileSettingStoredFileInfoSupported getInstance(Capability.StoredFileInfoCapability capability) {
		if (capability == null) {
			return null;
		}
		return new SendStoredFileSettingStoredFileInfoSupported(capability);
	}

	private SendStoredFileSettingStoredFileInfoSupported(Capability.StoredFileInfoCapability capability) {
		supportedFileIdLength = MaxLengthSupported.getMaxLengthSupported(capability.getFileIdLength());
		supportedFilePasswordLength = MaxLengthSupported.getMaxLengthSupported(capability.getFilePasswordLength());
		maxSelectFileNumber = Conversions.getIntValue(capability.getMaxSelectFileNumber(), 0);
	}

	/**
	 * @since SmartSDK V1.01
	 */
	public MaxLengthSupported getSupportedFileIdLength() {
		return supportedFileIdLength;
	}

	/**
	 * @since SmartSDK V1.01
	 */
	public MaxLengthSupported getSupportedFilePasswordLength() {
		return supportedFilePasswordLength;
	}

	/**
	 * @since SmartSDK V1.01
	 */
	public int getMaxSelectFileNumber() {
		return maxSelectFileNumber;
	}

}
