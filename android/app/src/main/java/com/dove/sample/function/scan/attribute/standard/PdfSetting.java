/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.function.scan.attribute.standard;

import java.util.HashMap;
import java.util.Map;

import com.dove.sample.function.scan.attribute.ScanRequestAttribute;

public final class PdfSetting implements ScanRequestAttribute {
	
	private static final String NAME_PDF_SETTING = "pdfSetting";
	
	private static final String NAME_PDF_A = "pdfA";
	private static final String NAME_HIGH_COMPRESSION_PDF = "highCompressionPdf";
	private static final String NAME_HIGH_COMPRESSION_PDF_HIQUALITY_MODE = "highCompressionPdfHiqualityMode"; //SmartSDK V2.12
	private static final String NAME_DIGITAL_SIGNATURE_PDF = "digitalSignaturePdf";

	
	private boolean pdfA;
	private boolean highCompressionPdf;
	private boolean highCompressionPdfHiqualityMode; //SmartSDK V2.12
	private boolean digitalSignaturePdf;
	
	
	public PdfSetting() {
		pdfA = false;
		highCompressionPdf = false;
		highCompressionPdfHiqualityMode = false;
		digitalSignaturePdf = false;
	}
	
	public boolean isPdfA() {
		return pdfA;
	}
	
	public void setPdfA(boolean value) {
		pdfA = value;
	}
	
	public boolean isHighCompressionPdf() {
		return highCompressionPdf;
	}
	
	public void setHighCompressionPdf(boolean value) {
		highCompressionPdf = value;
	}
	
	/**
	 * @since SmartSDK V2.12
	 */
	public boolean isHighCompressionPdfHiqualityMode() {
		return highCompressionPdfHiqualityMode;
	}
	
	/**
	 * @since SmartSDK V2.12
	 */
	public void setHighCompressionPdfHiqualityMode(boolean value) {
		highCompressionPdfHiqualityMode = value;
	}
	
	public boolean isDigitalSignaturePdf() {
		return digitalSignaturePdf;
	}
	
	public void setDigitalSignaturePdf(boolean value) {
		digitalSignaturePdf = value;
	}
	
	
	@Override
	public Class<?> getCategory() {
		return PdfSetting.class;
	}

	@Override
	public String getName() {
		return NAME_PDF_SETTING;
	}

	@Override
	public Object getValue() {
		Map<String, Object> value = new HashMap<String, Object>();
		value.put(NAME_PDF_A, Boolean.valueOf(pdfA));
		value.put(NAME_HIGH_COMPRESSION_PDF, Boolean.valueOf(highCompressionPdf));
		value.put(NAME_HIGH_COMPRESSION_PDF_HIQUALITY_MODE, Boolean.valueOf(highCompressionPdfHiqualityMode));
		value.put(NAME_DIGITAL_SIGNATURE_PDF, Boolean.valueOf(digitalSignaturePdf));
		return value;
	}

}
