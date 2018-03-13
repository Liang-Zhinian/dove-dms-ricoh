/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.custom.logic.data;

import com.dove.sample.wrapper.auth.permission.MfpFunctionPermissions;

public class ExtLoginResponse {
    private final String requestId;
    private final boolean result;
    private final String userId;
    private final String userName;
    private final String displayName;
    private final String passwd;
    private final MfpFunctionPermissions mfpFunctionPermissions;

    // options
	private String error;
    private String serialId;
    private String email;
    private String faxNumber;
    private String billingCode;
    private String billingCodeLabel;

    /**
     * Constructor for "External login succeeded"
     * @param requestId
     * @param result
     * @param userId
     * @param userName
     * @param displayName
     * @param passwd
     * @param mfpFuncPermissions
     */
    public ExtLoginResponse(String requestId, boolean result, String userId,
                            String userName,String displayName, String passwd, MfpFunctionPermissions mfpFuncPermissions){
        this.requestId = requestId;
        this.result = result;
        this.userId = userId;
        this.userName = userName;
        this.displayName = displayName;
        this.passwd = passwd;
        this.mfpFunctionPermissions = mfpFuncPermissions;
    }

    public static ExtLoginResponse createExtLoginOKResponse(String requestId, String userId,
            String userName,String displayName, String passwd, MfpFunctionPermissions mfpFuncPermissions) {
    	return new ExtLoginResponse(requestId, true, userId, userName, displayName, passwd, mfpFuncPermissions);
    }

    public static ExtLoginResponse createExtLoginNGResponse(String requestId) {
    	return new ExtLoginResponse(requestId, false, null, null, null, null, null);
    }

    public String getRequestId(){
        return requestId;
    }

    public boolean getResult(){
        return result;
    }

	public String getError(){
		return error;
	}

	public void setError(String error){
		this.error = error;
	}

    public String getUserId(){
        return userId;
    }

    public String getUserName(){
        return userName;
    }

    public String getDisplayName(){
        return displayName;
    }

    public String getPassword(){
        return passwd;
    }

    public MfpFunctionPermissions getMfpFunctionPermissions(){
        return mfpFunctionPermissions;
    }

	public String getSerialId() {
		return serialId;
	}

	public void setSerialId(String serialId) {
		this.serialId = serialId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public String getBillingCode() {
		return billingCode;
	}

	public void setBillingCode(String billingCode) {
		this.billingCode = billingCode;
	}

	public String getBillingCodeLabel() {
		return billingCodeLabel;
	}

	public void setBillingCodeLabel(String billingCodeLabel) {
		this.billingCodeLabel = billingCodeLabel;
	}
}
