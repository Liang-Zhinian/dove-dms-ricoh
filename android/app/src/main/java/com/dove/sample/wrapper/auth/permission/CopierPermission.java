/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.permission;

/**
 * CopierPermission.
 *
 */
public enum CopierPermission implements SsdkPermission {

    ALL(PermissionConstants.ALL),
    AUTOCOLOR(PermissionConstants.AUTO_COLOR),
    TWOCOLOR(PermissionConstants.TWO_COLOR),
    SINGLECOLOR(PermissionConstants.SINGLE_COLOR),
    MONOCHRO(PermissionConstants.MONOCHRO),
    NONE(PermissionConstants.NONE);

    private final String name;

    CopierPermission(String name) {
        this.name = name;
    }

	@Override
    public String getName(){
        return name;
    }

}