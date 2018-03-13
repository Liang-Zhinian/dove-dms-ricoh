/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.permission;

public enum PrinterColorModePermission implements SsdkPermission {

    FULLCOLOR(PermissionConstants.FULL_COLOR),
    TWOCOLOR(PermissionConstants.TWO_COLOR),
    MONOCHRO(PermissionConstants.MONOCHRO);

    private final String name;

    PrinterColorModePermission(String name) {
        this.name = name;
    }

	@Override
    public String getName(){
        return name;
    }
}
