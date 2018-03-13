/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.permission;

/**
 * PrinterPermission.
 */
public enum PrinterPermission implements SsdkPermission {

    ALL(PermissionConstants.ALL),
    TWOCOLOR(PermissionConstants.TWO_COLOR),
    MONOCHRO(PermissionConstants.MONOCHRO),
    NONE(PermissionConstants.NONE);

    private final String name;

    PrinterPermission(String name) {
        this.name = name;
    }

	@Override
    public String getName(){
        return name;
    }
}
