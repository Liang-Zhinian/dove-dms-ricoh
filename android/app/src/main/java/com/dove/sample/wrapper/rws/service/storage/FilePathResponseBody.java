/*
 *  Copyright (C) 2013 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.service.storage;

import java.util.Map;

import com.dove.sample.wrapper.common.Element;
import com.dove.sample.wrapper.common.ResponseBody;

public class FilePathResponseBody extends Element implements ResponseBody {

    private static final String KEY_FILE_PATH       = "filePath";
    private static final String KEY_FILE_ROTATE     = "rotate";

    FilePathResponseBody(Map<String, Object> values) {
        super(values);
    }

    /*
     * filePath (String)
     */
    public String getFilePath() {
        return getStringValue(KEY_FILE_PATH);
    }

    /*
     * rotate (Number)
     */
    public Integer getRotate() {
        return getNumberValue(KEY_FILE_ROTATE);
    }

}
