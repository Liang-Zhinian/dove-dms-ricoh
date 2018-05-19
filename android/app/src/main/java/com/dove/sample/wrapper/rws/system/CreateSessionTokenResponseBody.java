/*
 *  Copyright (C) 2014 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.system;

import java.util.Map;

import com.dove.sample.wrapper.common.Element;
import com.dove.sample.wrapper.common.ResponseBody;

/*
 * @since SmartSDK V1.02
 */
public class CreateSessionTokenResponseBody extends Element implements ResponseBody {

    private static final String KEY_SESSION_TOKEN   = "sessionToken";

    CreateSessionTokenResponseBody(Map<String, Object> values) {
        super(values);
    }

    /*
     * SessionToken (String)
     * @since SmartSDK V1.02
     */
    public String getSessionToken() {
        return getStringValue(KEY_SESSION_TOKEN);
    }

}
