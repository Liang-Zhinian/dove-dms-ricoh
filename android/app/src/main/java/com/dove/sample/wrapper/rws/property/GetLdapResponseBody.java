/*
 *  Copyright (C) 2013 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.property;

import java.util.Map;

import com.dove.sample.wrapper.common.ResponseBody;

public class GetLdapResponseBody extends Ldap implements ResponseBody {

    GetLdapResponseBody(Map<String, Object> values) {
        super(values);
    }

}
