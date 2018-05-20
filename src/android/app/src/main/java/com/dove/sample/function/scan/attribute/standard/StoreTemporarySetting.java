/*
 *  Copyright (C) 2014-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.function.scan.attribute.standard;

import com.dove.sample.function.scan.attribute.ScanRequestAttribute;

import java.util.HashMap;
import java.util.Map;

/**
 * StoreLocalSetting
 * @since SmartSDK V1.01
 */
public class StoreTemporarySetting implements ScanRequestAttribute {

    private static final String NAME_STORE_TEMPORARY_SETTING = "storeTemporarySetting";

    private static final String NAME_FILE_NAME           = "fileName";

    private String fileName;

    public StoreTemporarySetting() {
        fileName = null;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public Class<?> getCategory() {
        return StoreTemporarySetting.class;
    }

    @Override
    public String getName() {
        return NAME_STORE_TEMPORARY_SETTING;
    }

    @Override
    public Object getValue() {
        Map<String, Object> values = new HashMap<String, Object>();

        if (fileName!=null) {
            values.put(NAME_FILE_NAME, fileName);
        }

        return values;
    }

}
