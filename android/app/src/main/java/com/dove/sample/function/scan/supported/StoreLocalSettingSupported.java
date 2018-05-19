/*
 *  Copyright (C) 2014-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.function.scan.supported;

import com.dove.sample.wrapper.rws.service.scanner.Capability.StoreLocalSettingCapability;

/**
 * @since SmartSDK V1.01
 */
public final class StoreLocalSettingSupported {

	private final FolderIdSupported  supportedFolderIdRange;
	private final MaxLengthSupported supportedFolderPasswordLength;
	private final MaxLengthSupported supportedFileNameLength;
	private final MaxLengthSupported supportedFilePasswordLength;
	private final MaxLengthSupported supportedUserNameLength; //SmartSDK V2.12

	public StoreLocalSettingSupported(StoreLocalSettingCapability capability) {
		supportedFolderIdRange = FolderIdSupported.getFolderIdSupported(capability.getFolderIdRange());
		supportedFolderPasswordLength = MaxLengthSupported.getMaxLengthSupported(capability.getFolderPasswordLength());
		supportedFileNameLength = MaxLengthSupported.getMaxLengthSupported(capability.getFileNameLength());
		supportedFilePasswordLength = MaxLengthSupported.getMaxLengthSupported(capability.getFilePasswordLength());
		supportedUserNameLength = MaxLengthSupported.getMaxLengthSupported(capability.getUserNameLength());
	}

	/**
	 * @since SmartSDK V1.01
	 */
	public FolderIdSupported getSupportedFolderIdRange() {
		return supportedFolderIdRange;
	}

	/**
	 * @since SmartSDK V1.01
	 */
	public MaxLengthSupported getSupportedFolderPasswordLength() {
		return supportedFolderPasswordLength;
	}

	/**
	 * @since SmartSDK V1.01
	 */
	public MaxLengthSupported getSupportedFileNameLength() {
		return supportedFileNameLength;
	}

	/**
	 * @since SmartSDK V1.01
	 */
	public MaxLengthSupported getSupportedFilePasswordLength() {
		return supportedFilePasswordLength;
	}
	
	/**
	 * @since SmartSDK V2.12
	 */
	public MaxLengthSupported getSupportedUserNameLength() {
		return supportedUserNameLength;
	}

}
