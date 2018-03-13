/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.data;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import jp.co.ricoh.isdk.sdkservice.auth.intent.Constants;
import com.dove.sample.wrapper.auth.permission.CertificateUserPermission;
import com.dove.sample.wrapper.auth.permission.CopierColorModePermission;
import com.dove.sample.wrapper.auth.permission.DocumentAdminPermission;
import com.dove.sample.wrapper.auth.permission.MachineAdminPermission;
import com.dove.sample.wrapper.auth.permission.NetworkAdminPermission;
import com.dove.sample.wrapper.auth.permission.PrinterColorModePermission;
import com.dove.sample.wrapper.auth.permission.SupervisorPermission;
import com.dove.sample.wrapper.auth.permission.UserAdminPermission;

import android.os.Bundle;

/**
 * Auth State
 */
public class AuthState {
    public enum LOGIN_STATUS {LOGIN, LOGOUT}

    ;

    private final String mUserID;
    private final String mUserName;
    private final LOGIN_STATUS mLoginStatus;
    private final boolean mIsCopierAvailable;
    private final Map<CopierColorModePermission, Boolean> mCopierColorAvailabilities;
    private final boolean mIsDocumentServerAvailable;
    private final boolean mIsFaxAvailable;
    private final boolean mIsScannerAvailable;
    private final boolean mIsPrinterAvailable;
    private final Map<PrinterColorModePermission, Boolean> mPrinterColorAvailabilities;
    private final boolean mIsBrowserAvailable;
    private final Map<String, Boolean> mSdkjApplicationAvailabilities;
    private final MachineAdminPermission mMachineAdminPermission;
    private final UserAdminPermission mUserAdminPermission;
    private final DocumentAdminPermission mDocumentAdminPermission;
    private final NetworkAdminPermission mNetworkAdminPermission;
    private final CertificateUserPermission mCertificateUserPermission;
    private final SupervisorPermission mSupervisorPermission;

    /**
     * Build object from the extras of SYS_NOTIFY_AUTH_STATE_CHANGED intent or the output of GET_AUTH_STATE intent
     *
     * @param extras Bundle
     * @return Auth state
     */
    public static AuthState buildFromExtras(Bundle extras) {
        return new AuthState(extras);
    }

    private AuthState(Bundle extras) {
        this.mUserID = extras.getString(Constants.EXTRA_USER_ID);
        this.mUserName = extras.getString(Constants.EXTRA_USER_NAME);
        this.mLoginStatus = (extras.getBoolean(Constants.EXTRA_LOGIN_STATUS)) ? LOGIN_STATUS.LOGIN : LOGIN_STATUS.LOGOUT;
        this.mIsCopierAvailable = extras.getBoolean(Constants.EXTRA_IS_COPIER_AVAILABLE, false);

        @SuppressWarnings("unchecked")
        HashMap<String, Boolean> orgCopierColorAvailabilities = (HashMap<String, Boolean>) extras.getSerializable(Constants.EXTRA_COPIER_COLOR_AVAILABILITIES);
        this.mCopierColorAvailabilities = new HashMap<CopierColorModePermission, Boolean>();
        if (orgCopierColorAvailabilities != null) {
            for (String key : orgCopierColorAvailabilities.keySet()) {
                mCopierColorAvailabilities.put(getEnum(CopierColorModePermission.class, key), orgCopierColorAvailabilities.get(key));
            }
        }

        this.mIsDocumentServerAvailable = extras.getBoolean(Constants.EXTRA_IS_DOCUMENT_SERVER_AVAILABLE);
        this.mIsFaxAvailable = extras.getBoolean(Constants.EXTRA_IS_FAX_AVAILABLE);
        this.mIsScannerAvailable = extras.getBoolean(Constants.EXTRA_IS_SCANNER_AVAILABLE);
        this.mIsPrinterAvailable = extras.getBoolean(Constants.EXTRA_IS_PRINTER_AVAILABLE);

        @SuppressWarnings("unchecked")
        HashMap<String, Boolean> orgPrinterColorAvailabilities = (HashMap<String, Boolean>) extras.getSerializable(Constants.EXTRA_PRINTER_COLOR_AVAILABILITIES);
        this.mPrinterColorAvailabilities = new HashMap<PrinterColorModePermission, Boolean>();
        if (orgPrinterColorAvailabilities != null) {
            for (String key : orgPrinterColorAvailabilities.keySet()) {
                mPrinterColorAvailabilities.put(getEnum(PrinterColorModePermission.class, key), orgPrinterColorAvailabilities.get(key));
            }
        }

        this.mIsBrowserAvailable = extras.getBoolean(Constants.EXTRA_IS_BROWSER_AVAILABLE, false);

        @SuppressWarnings("unchecked")
        HashMap<String, Boolean> orgSdkApplicationAvailabilities = (HashMap<String, Boolean>) extras.getSerializable(Constants.EXTRA_SDKJ_APPLICATION_AVAILABILITIES);
        this.mSdkjApplicationAvailabilities = new HashMap<String, Boolean>();
        if (orgSdkApplicationAvailabilities != null) {
            for (String key : orgSdkApplicationAvailabilities.keySet()) {
                mSdkjApplicationAvailabilities.put(key, orgSdkApplicationAvailabilities.get(key));
            }
        }

        this.mMachineAdminPermission = getEnum(MachineAdminPermission.class, extras.getString(Constants.EXTRA_MACHINE_ADMIN_PERMISSION));
        this.mUserAdminPermission = getEnum(UserAdminPermission.class, extras.getString(Constants.EXTRA_USER_ADMIN_PERMISSION));
        this.mDocumentAdminPermission = getEnum(DocumentAdminPermission.class, extras.getString(Constants.EXTRA_DOCUMENT_ADMIN_PERMISSION));
        this.mNetworkAdminPermission = getEnum(NetworkAdminPermission.class, extras.getString(Constants.EXTRA_NETWORK_ADMIN_PERMISSION));
        this.mCertificateUserPermission = getEnum(CertificateUserPermission.class, extras.getString(Constants.EXTRA_CERTIFICATE_USER_PERMISSION));
        this.mSupervisorPermission = getEnum(SupervisorPermission.class, extras.getString(Constants.EXTRA_SUPERVISOR_PERMISSION));
    }

    public String getUserId() {
        return mUserID;
    }

    public String getUserName() {
        return mUserName;
    }

    public LOGIN_STATUS getLoginStatus() {
        return mLoginStatus;
    }

    public boolean isCopierAvailable() {
        return mIsCopierAvailable;
    }

    public HashMap<CopierColorModePermission, Boolean> getCopierColorAvailabilities() {
        return new HashMap<CopierColorModePermission, Boolean>(mCopierColorAvailabilities);
    }

    public boolean isDocumentServerAvailable() {
        return mIsDocumentServerAvailable;
    }

    public boolean isFaxAvailable() {
        return mIsFaxAvailable;
    }

    public boolean isScannerAvailable() {
        return mIsScannerAvailable;
    }

    public boolean isPrinterAvailable() {
        return mIsPrinterAvailable;
    }

    public HashMap<PrinterColorModePermission, Boolean> getPrinterColorAvailabilities() {
        return new HashMap<PrinterColorModePermission, Boolean>(mPrinterColorAvailabilities);
    }

    public boolean isBrowserAvailable() {
        return mIsBrowserAvailable;
    }

    public HashMap<String, Boolean> getSdkjApplicationAvailabilities() {
        return new HashMap<String, Boolean>(mSdkjApplicationAvailabilities);
    }

    public MachineAdminPermission getMachineAdminPermission() {
        return mMachineAdminPermission;
    }

    public UserAdminPermission getUserAdminPermission() {
        return mUserAdminPermission;
    }

    public DocumentAdminPermission getDocumentAdminPermission() {
        return mDocumentAdminPermission;
    }

    public NetworkAdminPermission getNetworkAdminPermission() {
        return mNetworkAdminPermission;
    }

    public CertificateUserPermission getCertificateUserPermission() {
        return mCertificateUserPermission;
    }

    public SupervisorPermission getSupervisorPermission() {
        return mSupervisorPermission;
    }

    private <T extends Enum<T>> T getEnum(Class<T> enumType, String name) {
        return (name == null) ? null : (Enum.valueOf(enumType, name.toUpperCase(Locale.ENGLISH)));
    }

    public String toString() {
        return "userId: " + mUserID + ", userName: " + mUserName;
    }
}
