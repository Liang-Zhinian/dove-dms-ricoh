/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.custom.ui.data;

/**
 * LoginRequestInput.
 */
public class LoginRequestInput {
	public enum AUTH_DEVICE {
		sopExtDevice,
		other
	}

	private String userId;
	private String password;
    private String extensionInfo;
	private AUTH_DEVICE authDevice;

    public LoginRequestInput(){}

	public LoginRequestInput(String userId, String password) {
		this.userId = userId;
		this.password = password;
	}

    /**
     * Get userId
     * @return userId
     */
    public String getUserId(){
        return userId;
    }

    /**
     * Set userId
     * @param userId userId
     */
    public void setUserId(String userId){
        this.userId = userId;
    }

    /**
     * Get password
     * @return password
     */
    public String getPassword(){
        return password;
    }

    /**
     * Set password
     * @param password password
     */
    public void setPassword(String password){
        this.password = password;
    }

    /**
     * Get extra information.
     * @return extra info
     */
    public String getExtInfo(){
        return extensionInfo;
    }

    /**
     * Set extra info
     * @param extInfo extra info
     */
    public void setExtInfo(String extInfo){
        this.extensionInfo = extInfo;
    }

	/**
	 * Set auth device
	 * @return auth device
	 */
	public AUTH_DEVICE getAuthDevice() { return authDevice; }

	/**
	 * get auth device
	 * @param authDevice auth device
	 */
	public void setAuthDevice(AUTH_DEVICE authDevice) { this.authDevice = authDevice; }

}
