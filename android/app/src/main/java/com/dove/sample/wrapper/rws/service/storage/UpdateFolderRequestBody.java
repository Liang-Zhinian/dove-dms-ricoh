/*
 *  Copyright (C) 2015-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.service.storage;

import com.dove.sample.wrapper.common.RequestBody;
import com.dove.sample.wrapper.common.Utils;
import com.dove.sample.wrapper.common.WritableElement;
import com.dove.sample.wrapper.json.EncodedException;
import com.dove.sample.wrapper.json.JsonUtils;
import com.dove.sample.wrapper.log.Logger;

import java.util.Map;

/*
 * @since SmartSDK V2.10
 */
public class UpdateFolderRequestBody extends WritableElement implements RequestBody {
    
    /**
     * Define the prefix for log information with abbreviation package and class name
     */
    private final static String PREFIX = "storage:UpdateFolderReq:";
	
	private static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";
	
	private static final String KEY_CHANGED_FIELD 			= "changedField";
	private static final String KEY_FOLDER_NAME 			= "folderName";
	private static final String KEY_IS_FOLDER_LOCKED 		= "isFolderLocked";
	private static final String KEY_NEW_PASSWORD 			= "newPassword";

	public UpdateFolderRequestBody(Map<String, Object> values) {
		super(values);
	}
	
	@Override
	public String getContentType() {
		return CONTENT_TYPE_JSON;
	}
	
	@Override
	public String toEntityString() {
		try {
			return JsonUtils.getEncoder().encode(values);
		} catch (EncodedException e) {
            Logger.warn(Utils.getTagName(), PREFIX + e.toString());
			return "{}";
		}
	}
	
	/*
	 * changedField (String)
	 * @since SmartSDK V2.10
	 */
    public String getChangedField() {
    	return getStringValue(KEY_CHANGED_FIELD);
    }
    public void setChangedField(String value) {
    	setStringValue(KEY_CHANGED_FIELD, value);
    }
    public String removeChangedField() {
    	return removeStringValue(KEY_CHANGED_FIELD);
    }
    
    /*
     * folderNmae (String)
     * @since SmartSDK V2.10
     */
    public String getFolderName() {
    	return getStringValue(KEY_FOLDER_NAME);
    }
    public void setFolderName(String value) {
    	setStringValue(KEY_FOLDER_NAME, value);
    }
    public String removeFolderName() {
    	return removeStringValue(KEY_FOLDER_NAME);
    }

    /*
     * isFolderLocked (Boolean)
     * @since SmartSDK V2.10
     */
    public Boolean getIsFolderLocked() {
    	return getBooleanValue(KEY_IS_FOLDER_LOCKED);
    }
    public void setIsFolderLocked(Boolean value) {
    	setBooleanValue(KEY_IS_FOLDER_LOCKED, value);
    }
    public Boolean removeIsFolderLocked() {
    	return removeBooleanValue(KEY_IS_FOLDER_LOCKED);
    }

    /*
     * newPassword (String)
     * @since SmartSDK V2.10
     */
    public String getNewPassword() {
    	return getStringValue(KEY_NEW_PASSWORD);
    }
    public void setNewPassword(String value) {
    	setStringValue(KEY_NEW_PASSWORD, value);
    }
    public String removeNewPassword() {
    	return removeStringValue(KEY_NEW_PASSWORD);
    }

}