/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.permission;

public enum ScannerPermission implements SsdkPermission {

	/**
	 * "all": All features are available.
	 * "none": Not available
	 */
	ALL(PermissionConstants.ALL),
	NONE(PermissionConstants.NONE);

	private final String name;
	ScannerPermission(String name) {
		this.name = name;
	}

	@Override
	public String getName(){
		return name;
	}

}
