/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.auth.permission;

import java.util.HashMap;


/**
 * MfpFunctionPermissions
 */
public class MfpFunctionPermissions {

    private CopierPermission mCopierPermission;
    private PrinterPermission mPrinterPermission;
    private ScannerPermission scannerPermission;
    private FaxPermission faxPermission;
    private DocumentServerPermission docServerPermission;
    private BrowserPermission browserPermission;
    private HashMap<String, SdkjApplicationPermission> sdkjAppPermissions;

	public MfpFunctionPermissions(){};

	public CopierPermission getCopierPermission() {
		return mCopierPermission;
	}

	public void setCopierPermission(CopierPermission copierPermission) {
		this.mCopierPermission = copierPermission;
	}

	public PrinterPermission getPrinterPermission() {
		return mPrinterPermission;
	}

	public void setPrinterPermission(PrinterPermission printerPermission) {
		this.mPrinterPermission = printerPermission;
	}

	public ScannerPermission getScannerPermission() {
		return scannerPermission;
	}

	public void setScannerPermission(ScannerPermission scannerPermission) {
		this.scannerPermission = scannerPermission;
	}

	public FaxPermission getFaxPermission() {
		return faxPermission;
	}

	public void setFaxPermission(FaxPermission faxPermission) {
		this.faxPermission = faxPermission;
	}

	public DocumentServerPermission getDocServerPermission() {
		return docServerPermission;
	}

	public void setDocServerPermission(DocumentServerPermission docServerPermission) {
		this.docServerPermission = docServerPermission;
	}

	public BrowserPermission getBrowserPermission() {
		return browserPermission;
	}

	public void setBrowserPermission(BrowserPermission browserPermission) {
		this.browserPermission = browserPermission;
	}

	public HashMap<String, SdkjApplicationPermission> getSdkjAppPermissions() {
		return sdkjAppPermissions;
	}

	public void setSdkjAppPermissions(HashMap<String, SdkjApplicationPermission> sdkjAppPermissions) {
		this.sdkjAppPermissions = sdkjAppPermissions;
	}
}
