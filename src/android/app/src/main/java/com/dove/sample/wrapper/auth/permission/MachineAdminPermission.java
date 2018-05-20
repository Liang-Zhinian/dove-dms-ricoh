/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.permission;

public enum MachineAdminPermission {
	/**
	 * "all": All features are available.
	 * "none": Not available
	 */
	ALL(PermissionConstants.ALL),
	NONE(PermissionConstants.NONE);

	private final String name;
	MachineAdminPermission(String name) {
		this.name = name;
	}

	public String getName(){
		return name;
	}

}
