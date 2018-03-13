/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.permission;

public enum CopierColorModePermission implements SsdkPermission {

    AUTOCOLOR(PermissionConstants.AUTO_COLOR),
    FULLCOLOR(PermissionConstants.FULL_COLOR),
    TWOCOLOR(PermissionConstants.TWO_COLOR),
    SINGLECOLOR(PermissionConstants.SINGLE_COLOR),
    MONOCHRO(PermissionConstants.MONOCHRO);

    private final String name;

    CopierColorModePermission(String name) {
        this.name = name;
    }

	@Override
    public String getName(){
        return name;
    }

}
