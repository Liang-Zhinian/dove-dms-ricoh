/*
 *  Copyright (C) 2013-2017 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.service.scanner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dove.sample.wrapper.common.ArrayElement;
import com.dove.sample.wrapper.common.Utils;
import com.dove.sample.wrapper.common.WritableElement;

public class JobSetting extends WritableElement {

	private static final String KEY_AUTO_CORRECT_JOB_SETTING	= "autoCorrectJobSetting";
	private static final String KEY_JOB_MODE					= "jobMode";
	private static final String KEY_JOB_STOPPED_TIMEOUT_PERIOD	= "jobStoppedTimeoutPeriod"; // SmartSDK V1.02
	private static final String KEY_WAITTIME_FOR_NEXT_ORIGINAL    				= "waitTimeForNextOriginal";	// SmartSDK V2.10
	private static final String KEY_ORIGINAL_SIZE				= "originalSize";
	private static final String KEY_ORIGINAL_SIZE_CUSTOM_X		= "originalSizeCustomX";
	private static final String KEY_ORIGINAL_SIZE_CUSTOM_Y		= "originalSizeCustomY";
	private static final String KEY_ORIGINAL_SIZE_CUSTOM_HORIZONTAL   	 		= "originalSizeCustomHorizontal";	// SmartSDK V2.10
	private static final String KEY_ORIGINAL_SIZE_CUSTOM_VERTICAL_AUTO_DETECT   = "originalSizeCustomVerticalAutoDetect";	// SmartSDK V2.12
	private static final String KEY_ORIGINAL_SIZE_CUSTOM_VERTICAL   		 	= "originalSizeCustomVertical";	// SmartSDK V2.10
	private static final String KEY_ORIGINAL_TRIMMING    						= "originalTrimming";	// SmartSDK V2.12
	private static final String KEY_ORIGINAL_TRIMMING_SETTING    				= "originalTrimmingSetting";	// SmartSDK V2.12
	private static final String KEY_SCAN_DEVICE					= "scanDevice";
	private static final String KEY_SCAN_METHOD					= "scanMethod";
	private static final String KEY_ORIGINAL_OUTPUT_EXIT		= "originalOutputExit";
	private static final String KEY_SCAN_STAMP    								= "scanStamp";	// SmartSDK V2.12
	private static final String KEY_ORIGINAL_SIDE				= "originalSide";
	private static final String KEY_LAST_PAGE_ONE_SIDED    						= "lastPageOneSided";	// SmartSDK V2.12
	private static final String KEY_SKIP_BACK_SIDE    							= "skipBackSide";	// SmartSDK V2.12
	private static final String KEY_DIVIDE    									= "divide";	// SmartSDK V2.12
	private static final String KEY_DIVIDE_SETTING    							= "divideSetting";	// SmartSDK V2.12
	private static final String KEY_ORIGINAL_ORIENTATION		= "originalOrientation";
	private static final String KEY_ORIGINAL_REVERSE_ORIENTATION    			= "originalReverseOrientation";	// SmartSDK V2.12
	private static final String KEY_ORIGINAL_PREVIEW			= "originalPreview";
	private static final String KEY_SCAN_COLOR					= "scanColor";
	private static final String KEY_THIN_PAPER                  = "thinPaper"; // SmartSDK v2.40
	private static final String KEY_MAGNIFICATION				= "magnification";
	private static final String KEY_MAGNIFICATION_SIZE			= "magnificationSize";
	private static final String KEY_SCAN_RESOLUTION				= "scanResolution";
	private static final String KEY_AUTO_DENSITY				= "autoDensity";
	private static final String KEY_MANUAL_DENSITY				= "manualDensity";
	private static final String KEY_ERASE_CENTER    							= "eraseCenter";	// SmartSDK V2.12
	private static final String KEY_ERASE_CENTER_SETTING    					= "eraseCenterSetting";	// SmartSDK V2.12
	private static final String KEY_ERASE_BORDER    							= "eraseBorder";	// SmartSDK V2.12
	private static final String KEY_ERASE_BORDER_SETTING    					= "eraseBorderSetting";	// SmartSDK V2.12
	private static final String KEY_DROPOUT_COLOR    							= "dropoutColor";	// SmartSDK V2.12
	private static final String KEY_DROPOUT_COLOR_SETTING    					= "dropoutColorSetting";	// SmartSDK V2.12
	private static final String KEY_NEGATIVE_POSITIVE_REVERSAL    				= "negativePositiveReversal";	// SmartSDK V2.12
	private static final String KEY_MIRROR_IMAGE    							= "mirrorImage";	// SmartSDK V2.12
	private static final String KEY_FILE_SETTING				= "fileSetting";
	private static final String KEY_PDF_SETTING					= "pdfSetting";
	private static final String KEY_OCR							= "ocr";
	private static final String KEY_OCR_SETTING					= "ocrSetting";
	private static final String KEY_SECURED_PDF_SETTING			= "securedPdfSetting";
	private static final String KEY_EMAIL_SETTING				= "emailSetting";
	private static final String KEY_DESTINATION_SETTING			= "destinationSetting";
	private static final String KEY_STORE_LOCAL_SETTING			= "storeLocalSetting";       // SmartSDK V1.01
	private static final String KEY_SEND_STORED_FILE_SETTING	= "sendStoredFileSetting";   // SmartSDK V1.01

	JobSetting(Map<String, Object> values) {
		super(values);
	}

	/*
	 * autoCorrectJobSetting (Boolean)
	 */
	public Boolean getAutoCorrectJobSetting() {
		return getBooleanValue(KEY_AUTO_CORRECT_JOB_SETTING);
	}
	public void setAutoCorrectJobSetting(Boolean value) {
		setBooleanValue(KEY_AUTO_CORRECT_JOB_SETTING, value);
	}
	public Boolean removeAutoCorrectJobSetting() {
		return removeBooleanValue(KEY_AUTO_CORRECT_JOB_SETTING);
	}

	/*
	 * jobMode (String)
	 */
	public String getJobMode() {
		return getStringValue(KEY_JOB_MODE);
	}
	public void setJobMode(String value) {
		setStringValue(KEY_JOB_MODE, value);
	}
	public String removeJobMode() {
		return removeStringValue(KEY_JOB_MODE);
	}

	/*
	 * jobStoppedTimeoutPeriod (Number)
	 * @since SmartSDK V1.02
	 */
	public Integer getJobStoppedTimeoutPeriod() {
		return getNumberValue(KEY_JOB_STOPPED_TIMEOUT_PERIOD);
	}
	public void setJobStoppedTimeoutPeriod(Integer value) {
		setNumberValue(KEY_JOB_STOPPED_TIMEOUT_PERIOD, value);
	}
	public Integer removeJobStoppedTimeoutPeriod() {
		return removeNumberValue(KEY_JOB_STOPPED_TIMEOUT_PERIOD);
	}

	/*
	 * waitTimeForNextOriginal (Number)
	 * @since SmartSDK V2.10
	 */
	public Integer getWaitTimeForNextOriginal() {
		return getNumberValue(KEY_WAITTIME_FOR_NEXT_ORIGINAL);
	}
	public void setWaitTimeForNextOriginal(Integer value) {
		setNumberValue(KEY_WAITTIME_FOR_NEXT_ORIGINAL, value);
	}
	public Integer removeWaitTimeForNextOriginal() {
		return removeNumberValue(KEY_WAITTIME_FOR_NEXT_ORIGINAL);
	}

	/*
	 * originalSize (String)
	 */
	public String getOriginalSize() {
		return getStringValue(KEY_ORIGINAL_SIZE);
	}
	public void setOriginalSize(String value) {
		setStringValue(KEY_ORIGINAL_SIZE, value);
	}
	public String removeOriginalSize() {
		return removeStringValue(KEY_ORIGINAL_SIZE);
	}

	/*
	 * originalSizeCustomX (String)
	 */
	public String getOriginalSizeCustomX() {
		return getStringValue(KEY_ORIGINAL_SIZE_CUSTOM_X);
	}
	public void setOriginalSizeCustomX(String value) {
		setStringValue(KEY_ORIGINAL_SIZE_CUSTOM_X, value);
	}
	public String removeOriginalSizeCustomX() {
		return removeStringValue(KEY_ORIGINAL_SIZE_CUSTOM_X);
	}

	/*
	 * originalSizeCustomY (String)
	 */
	public String getOriginalSizeCustomY() {
		return getStringValue(KEY_ORIGINAL_SIZE_CUSTOM_Y);
	}
	public void setOriginalSizeCustomY(String value) {
		setStringValue(KEY_ORIGINAL_SIZE_CUSTOM_Y, value);
	}
	public String removeOriginalSizeCustomY() {
		return removeStringValue(KEY_ORIGINAL_SIZE_CUSTOM_Y);
	}

	/*
	 * originalSizeCustomHorizontal (String)
	 * @since SmartSDK V2.10
	 */
	public String getOriginalSizeCustomHorizontal() {
		return getStringValue(KEY_ORIGINAL_SIZE_CUSTOM_HORIZONTAL);
	}
	public void setOriginalSizeCustomHorizontal(String value) {
		setStringValue(KEY_ORIGINAL_SIZE_CUSTOM_HORIZONTAL, value);
	}
	public String removeOriginalSizeCustomHorizontal() {
		return removeStringValue(KEY_ORIGINAL_SIZE_CUSTOM_HORIZONTAL);
	}

	/*
	 * originalSizeCustomVerticalAutoDetect (Boolean)
	 * @since SmartSDK V2.12
	 */
	public Boolean getOriginalSizeCustomVerticalAutoDetect() {
		return getBooleanValue(KEY_ORIGINAL_SIZE_CUSTOM_VERTICAL_AUTO_DETECT);
	}
	public void setOriginalSizeCustomVerticalAutoDetect(Boolean value) {
		setBooleanValue(KEY_ORIGINAL_SIZE_CUSTOM_VERTICAL_AUTO_DETECT, value);
	}
	public Boolean removeOriginalSizeCustomVerticalAutoDetect() {
		return removeBooleanValue(KEY_ORIGINAL_SIZE_CUSTOM_VERTICAL_AUTO_DETECT);
	}

	/*
	 * originalSizeCustomVertical (String)
	 * @since SmartSDK V2.10
	 */
	public String getOriginalSizeCustomVertical() {
		return getStringValue(KEY_ORIGINAL_SIZE_CUSTOM_VERTICAL);
	}
	public void setOriginalSizeCustomVertical(String value) {
		setStringValue(KEY_ORIGINAL_SIZE_CUSTOM_VERTICAL, value);
	}
	public String removeOriginalSizeCustomVertical() {
		return removeStringValue(KEY_ORIGINAL_SIZE_CUSTOM_VERTICAL);
	}

	/*
	 * originalTrimming (Boolean)
	 * @since SmartSDK V2.12
	 */
	public Boolean getOriginalTrimming() {
		return getBooleanValue(KEY_ORIGINAL_TRIMMING);
	}
	public void setOriginalTrimming(Boolean value) {
		setBooleanValue(KEY_ORIGINAL_TRIMMING, value);
	}
	public Boolean removeOriginalTrimming() {
		return removeBooleanValue(KEY_ORIGINAL_TRIMMING);
	}

	/*
	 * originalTrimmingSetting (Object)
	 * @since SmartSDK V2.12
	 */
	public OriginalTrimmingSetting getOriginalTrimmingSetting() {
		Map<String, Object> value = getObjectValue(KEY_ORIGINAL_TRIMMING_SETTING);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_ORIGINAL_TRIMMING_SETTING, value);
		}
		return new OriginalTrimmingSetting(value);
	}
	public OriginalTrimmingSetting removeOriginalTrimmingSetting() {
		Map<String, Object> value = removeObjectValue(KEY_ORIGINAL_TRIMMING_SETTING);
		if (value == null) {
			return null;
		}
		return new OriginalTrimmingSetting(value);
	}

	/*
	 * scanDevice (String)
	 */
	public String getScanDevice() {
		return getStringValue(KEY_SCAN_DEVICE);
	}
	public void setScanDevice(String value) {
		setStringValue(KEY_SCAN_DEVICE, value);
	}
	public String removeScanDevice() {
		return removeStringValue(KEY_SCAN_DEVICE);
	}

	/*
	 * scanMethod (String)
	 */
	public String getScanMethod() {
		return getStringValue(KEY_SCAN_METHOD);
	}
	public void setScanMethod(String value) {
		setStringValue(KEY_SCAN_METHOD, value);
	}
	public String removeScanMethod() {
		return removeStringValue(KEY_SCAN_METHOD);
	}

	/*
	 * originalOutputExit (String)
	 */
	public String getOriginalOutputExit() {
		return getStringValue(KEY_ORIGINAL_OUTPUT_EXIT);
	}
	public void setOriginalOutputExit(String value) {
		setStringValue(KEY_ORIGINAL_OUTPUT_EXIT, value);
	}
	public String removeOriginalOutputExit() {
		return removeStringValue(KEY_ORIGINAL_OUTPUT_EXIT);
	}

	/*
	 * scanStamp (Boolean)
	 * @since SmartSDK V2.12
	 */
	public Boolean getScanStamp() {
		return getBooleanValue(KEY_SCAN_STAMP);
	}
	public void setScanStamp(Boolean value) {
		setBooleanValue(KEY_SCAN_STAMP, value);
	}
	public Boolean removeScanStamp() {
		return removeBooleanValue(KEY_SCAN_STAMP);
	}

	/*
	 * originalSide (String)
	 */
	public String getOriginalSide() {
		return getStringValue(KEY_ORIGINAL_SIDE);
	}
	public void setOriginalSide(String value) {
		setStringValue(KEY_ORIGINAL_SIDE, value);
	}
	public String removeOriginalSide() {
		return removeStringValue(KEY_ORIGINAL_SIDE);
	}

	/*
	 * lastPageOneSided (Boolean)
	 * @since SmartSDK V2.12
	 */
	public Boolean getLastPageOneSided() {
		return getBooleanValue(KEY_LAST_PAGE_ONE_SIDED);
	}
	public void setLastPageOneSided(Boolean value) {
		setBooleanValue(KEY_LAST_PAGE_ONE_SIDED, value);
	}
	public Boolean removeLastPageOneSided() {
		return removeBooleanValue(KEY_LAST_PAGE_ONE_SIDED);
	}

	/*
	 * skipBackSide (Boolean)
	 * @since SmartSDK V2.12
	 */
	public Boolean getSkipBackSide() {
		return getBooleanValue(KEY_SKIP_BACK_SIDE);
	}
	public void setSkipBackSide(Boolean value) {
		setBooleanValue(KEY_SKIP_BACK_SIDE, value);
	}
	public Boolean removeSkipBackSide() {
		return removeBooleanValue(KEY_SKIP_BACK_SIDE);
	}

	/*
	 * divide (Boolean)
	 * @since SmartSDK V2.12
	 */
	public Boolean getDivide() {
		return getBooleanValue(KEY_DIVIDE);
	}
	public void setDivide(Boolean value) {
		setBooleanValue(KEY_DIVIDE, value);
	}
	public Boolean removeDivide() {
		return removeBooleanValue(KEY_DIVIDE);
	}

	/*
	 * divideSetting (Object)
	 * @since SmartSDK V2.12
	 */
	public DivideSetting getDivideSetting() {
		Map<String, Object> value = getObjectValue(KEY_DIVIDE_SETTING);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_DIVIDE_SETTING, value);
		}
		return new DivideSetting(value);
	}
	public DivideSetting removeDivideSetting() {
		Map<String, Object> value = removeObjectValue(KEY_DIVIDE_SETTING);
		if (value == null) {
			return null;
		}
		return new DivideSetting(value);
	}

	/*
	 * originalOrientation (String)
	 */
	public String getOriginalOrientation() {
		return getStringValue(KEY_ORIGINAL_ORIENTATION);
	}
	public void setOriginalOrientation(String value) {
		setStringValue(KEY_ORIGINAL_ORIENTATION, value);
	}
	public String removeOriginalOrientation() {
		return removeStringValue(KEY_ORIGINAL_ORIENTATION);
	}

	/*
	 * originalReverseOrientation (Boolean)
	 * @since SmartSDK V2.12
	 */
	public Boolean getOriginalReverseOrientation() {
		return getBooleanValue(KEY_ORIGINAL_REVERSE_ORIENTATION);
	}
	public void setOriginalReverseOrientation(Boolean value) {
		setBooleanValue(KEY_ORIGINAL_REVERSE_ORIENTATION, value);
	}
	public Boolean removeOriginalReverseOrientation() {
		return removeBooleanValue(KEY_ORIGINAL_REVERSE_ORIENTATION);
	}

	/*
	 * originalPreview (Boolean)
	 */
	public Boolean getOriginalPreview() {
		return getBooleanValue(KEY_ORIGINAL_PREVIEW);
	}
	public void setOriginalPreview(Boolean value) {
		setBooleanValue(KEY_ORIGINAL_PREVIEW, value);
	}
	public Boolean removeOriginalPreview() {
		return removeBooleanValue(KEY_ORIGINAL_PREVIEW);
	}

	/*
	 * scanColor (String)
	 */
	public String getScanColor() {
		return getStringValue(KEY_SCAN_COLOR);
	}
	public void setScanColor(String value) {
		setStringValue(KEY_SCAN_COLOR, value);
	}
	public String removeScanColor() {
		return removeStringValue(KEY_SCAN_COLOR);
	}
	
	/*
	 * thinPaper (Boolean)
	 * 
	 * @Since SmartSDk v2.40
	 */
	public Boolean getThinPaper() {
		return getBooleanValue(KEY_THIN_PAPER);
	}
	public void setThinPaper(Boolean value) {
		setBooleanValue(KEY_THIN_PAPER, value);
	}
	public Boolean removeThinPaper() {
		return removeBooleanValue(KEY_THIN_PAPER);
	}

	/*
	 * magnification (String)
	 */
	public String getMagnification() {
		return getStringValue(KEY_MAGNIFICATION);
	}
	public void setMagnification(String value) {
		setStringValue(KEY_MAGNIFICATION, value);
	}
	public String removeMagnification() {
		return removeStringValue(KEY_MAGNIFICATION);
	}

	/*
	 * magnificationSize (Object)
	 */
	public MagnificationSize getMagnificationSize() {
		Map<String, Object> value = getObjectValue(KEY_MAGNIFICATION_SIZE);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_MAGNIFICATION_SIZE, value);
		}
		return new MagnificationSize(value);
	}
//	public void setMagnificationSize(MagnificationSize value) {
//		throw new UnsupportedOperationException();
//	}
	public MagnificationSize removeMagnificationSize() {
		Map<String, Object> value = removeObjectValue(KEY_MAGNIFICATION_SIZE);
		if (value == null) {
			return null;
		}
		return new MagnificationSize(value);
	}

	/*
	 * scanResolution (String)
	 */
	public String getScanResolution() {
		return getStringValue(KEY_SCAN_RESOLUTION);
	}
	public void setScanResolution(String value) {
		setStringValue(KEY_SCAN_RESOLUTION, value);
	}
	public String removeScanResolution() {
		return removeStringValue(KEY_SCAN_RESOLUTION);
	}

	/*
	 * autoDensity (Boolean)
	 */
	public Boolean getAutoDensity() {
		return getBooleanValue(KEY_AUTO_DENSITY);
	}
	public void setAutoDensity(Boolean value) {
		setBooleanValue(KEY_AUTO_DENSITY, value);
	}
	public Boolean removeAutoDensity() {
		return removeBooleanValue(KEY_AUTO_DENSITY);
	}

	/*
	 * manualDensity (Number)
	 */
	public Integer getManualDensity() {
		return getNumberValue(KEY_MANUAL_DENSITY);
	}
	public void setManualDensity(Integer value) {
		setNumberValue(KEY_MANUAL_DENSITY, value);
	}
	public Integer removeManualDensity() {
		return removeNumberValue(KEY_MANUAL_DENSITY);
	}

	/*
	 * eraseCenter (Boolean)
	 * @since SmartSDK V2.12
	 */
	public Boolean getEraseCenter() {
		return getBooleanValue(KEY_ERASE_CENTER);
	}
	public void setEraseCenter(Boolean value) {
		setBooleanValue(KEY_ERASE_CENTER, value);
	}
	public Boolean removeEraseCenter() {
		return removeBooleanValue(KEY_ERASE_CENTER);
	}

	/*
	 * eraseCenterSetting (Object)
	 * @since SmartSDK V2.12
	 */
	public EraseCenterSetting getEraseCenterSetting() {
		Map<String, Object> value = getObjectValue(KEY_ERASE_CENTER_SETTING);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_ERASE_CENTER_SETTING, value);
		}
		return new EraseCenterSetting(value);
	}
	public EraseCenterSetting removeEraseCenterSetting() {
		Map<String, Object> value = removeObjectValue(KEY_ERASE_CENTER_SETTING);
		if (value == null) {
			return null;
		}
		return new EraseCenterSetting(value);
	}

	/*
	 * eraseBorder (Boolean)
	 * @since SmartSDK V2.12
	 */
	public Boolean getEraseBorder() {
		return getBooleanValue(KEY_ERASE_BORDER);
	}
	public void setEraseBorder(Boolean value) {
		setBooleanValue(KEY_ERASE_BORDER, value);
	}
	public Boolean removeEraseBorder() {
		return removeBooleanValue(KEY_ERASE_BORDER);
	}

	/*
	 * eraseBorderSetting (Object)
	 * @since SmartSDK V2.12
	 */
	public EraseBorderSetting getEraseBorderSetting() {
		Map<String, Object> value = getObjectValue(KEY_ERASE_BORDER_SETTING);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_ERASE_BORDER_SETTING, value);
		}
		return new EraseBorderSetting(value);
	}
	public EraseBorderSetting removeEraseBorderSetting() {
		Map<String, Object> value = removeObjectValue(KEY_ERASE_BORDER_SETTING);
		if (value == null) {
			return null;
		}
		return new EraseBorderSetting(value);
	}

	/*
	 * dropoutColor (Boolean)
	 * @since SmartSDK V2.12
	 */
	public Boolean getDropoutColor() {
		return getBooleanValue(KEY_DROPOUT_COLOR);
	}
	public void setDropoutColor(Boolean value) {
		setBooleanValue(KEY_DROPOUT_COLOR, value);
	}
	public Boolean removeDropoutColor() {
		return removeBooleanValue(KEY_DROPOUT_COLOR);
	}

	/*
	 * dropoutColorSetting (Object)
	 * @since SmartSDK V2.12
	 */
	public DropoutColorSetting getDropoutColorSetting() {
		Map<String, Object> value = getObjectValue(KEY_DROPOUT_COLOR_SETTING);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_DROPOUT_COLOR_SETTING, value);
		}
		return new DropoutColorSetting(value);
	}
	public DropoutColorSetting removeDropoutColorSetting() {
		Map<String, Object> value = removeObjectValue(KEY_DROPOUT_COLOR_SETTING);
		if (value == null) {
			return null;
		}
		return new DropoutColorSetting(value);
	}

	/*
	 * negativePositiveReversal (Boolean)
	 * @since SmartSDK V2.12
	 */
	public Boolean getNegativePositiveReversal() {
		return getBooleanValue(KEY_NEGATIVE_POSITIVE_REVERSAL);
	}
	public void setNegativePositiveReversal(Boolean value) {
		setBooleanValue(KEY_NEGATIVE_POSITIVE_REVERSAL, value);
	}
	public Boolean removeNegativePositiveReversal() {
		return removeBooleanValue(KEY_NEGATIVE_POSITIVE_REVERSAL);
	}

	/*
	 * mirrorImage (Boolean)
	 * @since SmartSDK V2.12
	 */
	public Boolean getMirrorImage() {
		return getBooleanValue(KEY_MIRROR_IMAGE);
	}
	public void setMirrorImage(Boolean value) {
		setBooleanValue(KEY_MIRROR_IMAGE, value);
	}
	public Boolean removeMirrorImage() {
		return removeBooleanValue(KEY_MIRROR_IMAGE);
	}

	/*
	 * fileSetting (Object)
	 */
	public FileSetting getFileSetting() {
		Map<String, Object> value = getObjectValue(KEY_FILE_SETTING);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_FILE_SETTING, value);
		}
		return new FileSetting(value);
	}
//	public void setFileSetting(FileSetting value) {
//		throw new UnsupportedOperationException();
//	}
	public FileSetting removeFileSetting() {
		Map<String, Object> value = removeObjectValue(KEY_FILE_SETTING);
		if (value == null) {
			return null;
		}
		return new FileSetting(value);
	}

	/*
	 * pdfSetting (Object)
	 */
	public PdfSetting getPdfSetting() {
		Map<String, Object> value = getObjectValue(KEY_PDF_SETTING);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_PDF_SETTING, value);
		}
		return new PdfSetting(value);
	}
//	public void setPdfSetting(PdfSetting value) {
//		throw new UnsupportedOperationException();
//	}
	public PdfSetting removePdfSetting() {
		Map<String, Object> value = removeObjectValue(KEY_PDF_SETTING);
		if (value == null) {
			return null;
		}
		return new PdfSetting(value);
	}

	/*
	 * ocr (Boolean)
	 */
	public Boolean getOcr() {
		return getBooleanValue(KEY_OCR);
	}
	public void setOcr(Boolean value) {
		setBooleanValue(KEY_OCR, value);
	}
	public Boolean removeOcr() {
		return removeBooleanValue(KEY_OCR);
	}

	/*
	 * ocrSetting (Object)
	 */
	public OcrSetting getOcrSetting() {
		Map<String, Object> value = getObjectValue(KEY_OCR_SETTING);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_OCR_SETTING, value);
		}
		return new OcrSetting(value);
	}
//	public void setOcrSetting(OcrSetting value) {
//		throw new UnsupportedOperationException();
//	}
	public OcrSetting removeOcrSetting() {
		Map<String, Object> value = removeObjectValue(KEY_OCR_SETTING);
		if (value == null) {
			return null;
		}
		return new OcrSetting(value);
	}

	/*
	 * securedPdfSetting (Object)
	 */
	public SecuredPdfSetting getSecuredPdfSetting() {
		Map<String, Object> value = getObjectValue(KEY_SECURED_PDF_SETTING);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_SECURED_PDF_SETTING, value);
		}
		return new SecuredPdfSetting(value);
	}
//	public void setSecuredPdfSetting(SecuredPdfSetting value) {
//		throw new UnsupportedOperationException();
//	}
	public SecuredPdfSetting removeSecuredPdfSetting() {
		Map<String, Object> value = removeObjectValue(KEY_SECURED_PDF_SETTING);
		if (value == null) {
			return null;
		}
		return new SecuredPdfSetting(value);
	}

	/*
	 * emailSetting (Object)
	 */
	public EmailSetting getEmailSetting() {
		Map<String, Object> value = getObjectValue(KEY_EMAIL_SETTING);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_EMAIL_SETTING, value);
		}
		return new EmailSetting(value);
	}
//	public void setEmailSetting(EmailSetting value) {
//		throw new UnsupportedOperationException();
//	}
	public EmailSetting removeEmailSetting() {
		Map<String, Object> value = removeObjectValue(KEY_EMAIL_SETTING);
		if (value == null) {
			return null;
		}
		return new EmailSetting(value);
	}

	/*
	 * destinationSetting (Array[Object])
	 */
	public DestinationSettingArray getDestinationSetting() {
		List<Map<String, Object>> value = getArrayValue(KEY_DESTINATION_SETTING);
		if (value == null) {
			value = Utils.createElementList();
			setArrayValue(KEY_DESTINATION_SETTING, value);
		}
		return new DestinationSettingArray(value);
	}
//	public void setDestinationSetting(DestinationSetting value) {
//		throw new UnsupportedOperationException();
//	}
	public DestinationSettingArray removeDestinationSetting() {
		List<Map<String, Object>> value = removeArrayValue(KEY_DESTINATION_SETTING);
		if (value == null) {
			return null;
		}
		return new DestinationSettingArray(value);
	}

	/*
	 * storeLocalSetting (Object)
	 * @since SmartSDK V1.01
	 */
	public StoreLocalSetting getStoreLocalSetting() {
		Map<String, Object> value = getObjectValue(KEY_STORE_LOCAL_SETTING);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_STORE_LOCAL_SETTING, value);
		}
		return new StoreLocalSetting(value);
	}
//	public void setStoreLocalSetting(StoreLocalSetting value) {
//		throw new UnsupportedOperationException();
//	}
	public StoreLocalSetting removeStoreLocalSetting() {
		Map<String, Object> value = removeObjectValue(KEY_STORE_LOCAL_SETTING);
		if (value == null) {
			return null;
		}
		return new StoreLocalSetting(value);
	}

	/*
	 * sendStoredFileSetting (Object)
	 * @since SmartSDK V1.01
	 */
	public SendStoredFileSetting getSendStoredFileSetting() {
		Map<String, Object> value = getObjectValue(KEY_SEND_STORED_FILE_SETTING);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_SEND_STORED_FILE_SETTING, value);
		}
		return new SendStoredFileSetting(value);
	}
//	public void setSendStoredFileSetting(SendStoredFileSetting value) {
//		throw new UnsupportedOperationException();
//	}
	public SendStoredFileSetting removeSendStoredFileSetting() {
		Map<String, Object> value = removeObjectValue(KEY_SEND_STORED_FILE_SETTING);
		if (value == null) {
			return null;
		}
		return new SendStoredFileSetting(value);
	}

	public static class MagnificationSize extends WritableElement {

		private static final String KEY_SIZE		= "size";
		private static final String KEY_CUSTOM_LONG_EDGE	= "customLongEdge"; // Smart V2.12
		private static final String KEY_CUSTOM_SHORT_EDGE	= "customShortEdge"; // Smart V2.12
		private static final String KEY_CUSTOM_X	= "customX";
		private static final String KEY_CUSTOM_Y	= "customY";

		MagnificationSize(Map<String, Object> values) {
			super(values);
		}

		/*
		 * size (String)
		 */
		public String getSize() {
			return getStringValue(KEY_SIZE);
		}
		public void setSize(String value) {
			setStringValue(KEY_SIZE, value);
		}
		public String removeSize() {
			return removeStringValue(KEY_SIZE);
		}

		/*
		 * customLongEdge (String)
		 * @since SmartSDK V2.12
		 */
		public String getCustomLongEdge() {
			return getStringValue(KEY_CUSTOM_LONG_EDGE);
		}
		public void setCustomLongEdge(String value) {
			setStringValue(KEY_CUSTOM_LONG_EDGE, value);
		}
		public String removeCustomLongEdge() {
			return removeStringValue(KEY_CUSTOM_LONG_EDGE);
		}

		/*
		 * customShortEdge (String)
		 * @since SmartSDK V2.12
		 */
		public String getCustomShortEdge() {
			return getStringValue(KEY_CUSTOM_SHORT_EDGE);
		}
		public void setCustomShortEdge(String value) {
			setStringValue(KEY_CUSTOM_SHORT_EDGE, value);
		}
		public String removeCustomShortEdge() {
			return removeStringValue(KEY_CUSTOM_SHORT_EDGE);
		}

		/*
		 * customX (String)
		 */
		public String getCustomX() {
			return getStringValue(KEY_CUSTOM_X);
		}
		public void setCustomX(String value) {
			setStringValue(KEY_CUSTOM_X, value);
		}
		public String removeCustomX() {
			return removeStringValue(KEY_CUSTOM_X);
		}

		/*
		 * customY (String)
		 */
		public String getCustomY() {
			return getStringValue(KEY_CUSTOM_Y);
		}
		public void setCustomY(String value) {
			setStringValue(KEY_CUSTOM_Y, value);
		}
		public String removeCustomY() {
			return removeStringValue(KEY_CUSTOM_Y);
		}
	}

	public static class FileSetting extends WritableElement {

		private static final String KEY_COMPRESSION_METHOD		= "compressionMethod";
		private static final String KEY_COMPRESSION_LEVEL		= "compressionLevel";
		private static final String KEY_FILE_FORMAT				= "fileFormat";
		private static final String KEY_MULTI_PAGE_FORMAT		= "multiPageFormat";
		private static final String KEY_FILE_NAME				= "fileName";
		private static final String KEY_FILE_NAME_TIME_STAMP	= "fileNameTimeStamp";
		private static final String KEY_START_NO				= "startNo"; // SmartSDK V2.12

		FileSetting(Map<String, Object> values) {
			super(values);
		}

		/*
		 * compressionMethod (String)
		 */
		public String getCompressionMethod() {
			return getStringValue(KEY_COMPRESSION_METHOD);
		}
		public void setCompressionMethod(String value) {
			setStringValue(KEY_COMPRESSION_METHOD, value);
		}
		public String removeCompressionMethod() {
			return removeStringValue(KEY_COMPRESSION_METHOD);
		}

		/*
		 * compressionLevel (String)
		 */
		public String getCompressionLevel() {
			return getStringValue(KEY_COMPRESSION_LEVEL);
		}
		public void setCompressionLevel(String value) {
			setStringValue(KEY_COMPRESSION_LEVEL, value);
		}
		public String removeCompressionLevel() {
			return removeStringValue(KEY_COMPRESSION_LEVEL);
		}

		/*
		 * fileFormat (String)
		 */
		public String getFileFormat() {
			return getStringValue(KEY_FILE_FORMAT);
		}
		public void setFileFormat(String value) {
			setStringValue(KEY_FILE_FORMAT, value);
		}
		public String removeFileFormat() {
			return removeStringValue(KEY_FILE_FORMAT);
		}

		/*
		 * multiPageFormat (Boolean)
		 */
		public Boolean getMultiPageFormat() {
			return getBooleanValue(KEY_MULTI_PAGE_FORMAT);
		}
		public void setMultiPageFormat(Boolean value) {
			setBooleanValue(KEY_MULTI_PAGE_FORMAT, value);
		}
		public Boolean removeMultiPageFormat() {
			return removeBooleanValue(KEY_MULTI_PAGE_FORMAT);
		}

		/*
		 * fileName (String)
		 */
		public String getFileName() {
			return getStringValue(KEY_FILE_NAME);
		}
		public void setFileName(String value) {
			setStringValue(KEY_FILE_NAME, value);
		}
		public String removeFileName() {
			return removeStringValue(KEY_FILE_NAME);
		}

		/*
		 * fileNameTimeStamp (Boolean)
		 */
		public Boolean getFileNameTimeStamp() {
			return getBooleanValue(KEY_FILE_NAME_TIME_STAMP);
		}
		public void setFileNameTimeStamp(Boolean value) {
			setBooleanValue(KEY_FILE_NAME_TIME_STAMP, value);
		}
		public Boolean removeFileNameTimeStamp() {
			return removeBooleanValue(KEY_FILE_NAME_TIME_STAMP);
		}

		/*
		 * startNo (String)
		 * @since SmartSDK V2.12
		 */
		public String getStartNo() {
			return getStringValue(KEY_START_NO);
		}
		public void setStartNo(String value) {
			setStringValue(KEY_START_NO, value);
		}
		public String removeStartNo() {
			return removeStringValue(KEY_START_NO);
		}

	}

	public static class PdfSetting extends WritableElement {

		private static final String KEY_PDF_A					= "pdfA";
		private static final String KEY_HIGH_COMPRESSION_PDF	= "highCompressionPdf";
		private static final String KEY_HIGH_COMPRESSION_PDF_HIQUALITY_MODE	= "highCompressionPdfHiqualityMode"; // Smart V2.12
		private static final String KEY_DIGITAL_SIGNATURE_PDF	= "digitalSignaturePdf";
		
		PdfSetting(Map<String, Object> values) {
			super(values);
		}

		/*
		 * pdfA (Boolean)
		 */
		public Boolean getPdfA() {
			return getBooleanValue(KEY_PDF_A);
		}
		public void setPdfA(Boolean value) {
			setBooleanValue(KEY_PDF_A, value);
		}
		public Boolean removePdfA() {
			return removeBooleanValue(KEY_PDF_A);
		}

		/*
		 * highCompressionPdf (Boolean)
		 */
		public Boolean getHighCompressionPdf() {
			return getBooleanValue(KEY_HIGH_COMPRESSION_PDF);
		}
		public void setHighCompressionPdf(Boolean value) {
			setBooleanValue(KEY_HIGH_COMPRESSION_PDF, value);
		}
		public Boolean removeHighCompressionPdf() {
			return removeBooleanValue(KEY_HIGH_COMPRESSION_PDF);
		}

		/*
		 * highCompressionPdfHiqualityMode (Boolean)
		 * @since SmartSDK V2.12
		 */
		public Boolean getHighCompressionPdfHiqualityMode() {
			return getBooleanValue(KEY_HIGH_COMPRESSION_PDF_HIQUALITY_MODE);
		}
		public void setHighCompressionPdfHiqualityMode(Boolean value) {
			setBooleanValue(KEY_HIGH_COMPRESSION_PDF_HIQUALITY_MODE, value);
		}
		public Boolean removeHighCompressionPdfHiqualityMode() {
			return removeBooleanValue(KEY_HIGH_COMPRESSION_PDF_HIQUALITY_MODE);
		}

		/*
		 * digitalSignaturePdf (Boolean)
		 */
		public Boolean getDigitalSignaturePdf() {
			return getBooleanValue(KEY_DIGITAL_SIGNATURE_PDF);
		}
		public void setDigitalSignaturePdf(Boolean value) {
			setBooleanValue(KEY_DIGITAL_SIGNATURE_PDF, value);
		}
		public Boolean removeDigitalSignaturePdf() {
			return removeBooleanValue(KEY_DIGITAL_SIGNATURE_PDF);
		}

	}

	public static class OcrSetting extends WritableElement {

		private static final String KEY_OCR_LANGUAGE					= "ocrLanguage";
		private static final String KEY_OMIT_BLANK_PAGE					= "omitBlankPage";
		private static final String KEY_BLANK_PAGE_SENSITIVITY_LEVEL	= "blankPageSensitivityLevel";
		private static final String KEY_AUTO_FILE_NAME					= "autoFileName";
		private static final String KEY_VERTICAL_JUDGMENT				= "verticalJudgment";  // SmartSDK V2.00

		OcrSetting(Map<String, Object> values) {
			super(values);
		}

		/*
		 * ocrLanguage (String)
		 */
		public String getOcrLanguage() {
			return getStringValue(KEY_OCR_LANGUAGE);
		}
		public void setOcrLanguage(String value) {
			setStringValue(KEY_OCR_LANGUAGE, value);
		}
		public String removeOcrLanguage() {
			return removeStringValue(KEY_OCR_LANGUAGE);
		}

		/*
		 * omitBlankPage (Boolean)
		 */
		public Boolean getOmitBlankPage() {
			return getBooleanValue(KEY_OMIT_BLANK_PAGE);
		}
		public void setOmitBlankPage(Boolean value) {
			setBooleanValue(KEY_OMIT_BLANK_PAGE, value);
		}
		public Boolean removeOmitBlankPage() {
			return removeBooleanValue(KEY_OMIT_BLANK_PAGE);
		}

		/*
		 * blankPageSensitivityLevel (String)
		 */
		public String getBlankPageSensitivityLevel() {
			return getStringValue(KEY_BLANK_PAGE_SENSITIVITY_LEVEL);
		}
		public void setBlankPageSensitivityLevel(String value) {
			setStringValue(KEY_BLANK_PAGE_SENSITIVITY_LEVEL, value);
		}
		public String removeBlankPageSensitivityLevel() {
			return removeStringValue(KEY_BLANK_PAGE_SENSITIVITY_LEVEL);
		}

		/*
		 * autoFileName (Boolean)
		 */
		public Boolean getAutoFileName() {
			return getBooleanValue(KEY_AUTO_FILE_NAME);
		}
		public void setAutoFileName(Boolean value) {
			setBooleanValue(KEY_AUTO_FILE_NAME, value);
		}
		public Boolean removeAutoFileName() {
			return removeBooleanValue(KEY_AUTO_FILE_NAME);
		}

		/*
		 * verticalJudgment (Boolean)
		 * @since SmartSDK V2.00
		 */
		public Boolean getVerticalJudgment() {
			return getBooleanValue(KEY_VERTICAL_JUDGMENT);
		}
		public void setVerticalJudgment(Boolean value) {
			setBooleanValue(KEY_VERTICAL_JUDGMENT, value);
		}
		public Boolean removeVerticalJudgment() {
			return removeBooleanValue(KEY_VERTICAL_JUDGMENT);
		}

	}

	public static class SecuredPdfSetting extends WritableElement {

		private static final String KEY_ENCRYPTION					= "encryption";
		private static final String KEY_ENCRYPTION_PASSWORD			= "encryptionPassword";
		private static final String KEY_ENCRYPTION_LEVEL			= "encryptionLevel";
		private static final String KEY_DOCUMENT_SECURITY			= "documentSecurity";
		private static final String KEY_DOCUMENT_SECURITY_PASSWORD	= "documentSecurityPassword";
		private static final String KEY_ALLOW_PRINT					= "allowPrint";
		private static final String KEY_ALLOW_EDIT					= "allowEdit";
		private static final String KEY_ALLOW_COPY_AND_EXTRACT		= "allowCopyAndExtract";

		SecuredPdfSetting(Map<String, Object> values) {
			super(values);
		}

		/*
		 * encryption (Boolean)
		 */
		public Boolean getEncryption() {
			return getBooleanValue(KEY_ENCRYPTION);
		}
		public void setEncryption(Boolean value) {
			setBooleanValue(KEY_ENCRYPTION, value);
		}
		public Boolean removeEncryption() {
			return removeBooleanValue(KEY_ENCRYPTION);
		}

		/*
		 * encryptionPassword (String)
		 */
		public String getEncryptionPassword() {
			return getStringValue(KEY_ENCRYPTION_PASSWORD);
		}
		public void setEncryptionPassword(String value) {
			setStringValue(KEY_ENCRYPTION_PASSWORD, value);
		}
		public String removeEncryptionPassword() {
			return removeStringValue(KEY_ENCRYPTION_PASSWORD);
		}

		/*
		 * encryptionLevel (String)
		 */
		public String getEncryptionLevel() {
			return getStringValue(KEY_ENCRYPTION_LEVEL);
		}
		public void setEncryptionLevel(String value) {
			setStringValue(KEY_ENCRYPTION_LEVEL, value);
		}
		public String removeEncryptionLevel() {
			return removeStringValue(KEY_ENCRYPTION_LEVEL);
		}

		/*
		 * documentSecurity (Boolean)
		 */
		public Boolean getDocumentSecurity() {
			return getBooleanValue(KEY_DOCUMENT_SECURITY);
		}
		public void setDocumentSecurity(Boolean value) {
			setBooleanValue(KEY_DOCUMENT_SECURITY, value);
		}
		public Boolean removeDocumentSecurity() {
			return removeBooleanValue(KEY_DOCUMENT_SECURITY);
		}

		/*
		 * documentSecurityPassword (String)
		 */
		public String getDocumentSecurityPassword() {
			return getStringValue(KEY_DOCUMENT_SECURITY_PASSWORD);
		}
		public void setDocumentSecurityPassword(String value) {
			setStringValue(KEY_DOCUMENT_SECURITY_PASSWORD, value);
		}
		public String removeDocumentSecurityPassword() {
			return removeStringValue(KEY_DOCUMENT_SECURITY_PASSWORD);
		}

		/*
		 * allowPrint (String)
		 */
		public String getAllowPrint() {
			return getStringValue(KEY_ALLOW_PRINT);
		}
		public void setAllowPrint(String value) {
			setStringValue(KEY_ALLOW_PRINT, value);
		}
		public String removeAllowPrint() {
			return removeStringValue(KEY_ALLOW_PRINT);
		}

		/*
		 * allowEdit (Boolean)
		 */
		public Boolean getAllowEdit() {
			return getBooleanValue(KEY_ALLOW_EDIT);
		}
		public void setAllowEdit(Boolean value) {
			setBooleanValue(KEY_ALLOW_EDIT, value);
		}
		public Boolean removeAllowEdit() {
			return removeBooleanValue(KEY_ALLOW_EDIT);
		}

		/*
		 * allowCopyAndExtract (Boolean)
		 */
		public Boolean getAllowCopyAndExtract() {
			return getBooleanValue(KEY_ALLOW_COPY_AND_EXTRACT);
		}
		public void setAllowCopyAndExtract(Boolean value) {
			setBooleanValue(KEY_ALLOW_COPY_AND_EXTRACT, value);
		}
		public Boolean removeAllowCopyAndExtract() {
			return removeBooleanValue(KEY_ALLOW_COPY_AND_EXTRACT);
		}

	}

	public static class EmailSetting extends WritableElement {

		private static final String KEY_SUBJECT					= "subject";
		private static final String KEY_BODY					= "body";
		private static final String KEY_REGISTRATION_BODY		= "registrationBody"; // SmartSDK V2.12
		private static final String KEY_SENDER_ENTRY_ID			= "senderEntryId";
		private static final String KEY_SMIME_SIGNATURE			= "smimeSignature";
		private static final String KEY_SMIME_ENCRYPTION		= "smimeEncryption";
		private static final String KEY_RECEPTION_NOTICE		= "receptionNotice"; // SmartSDK V2.12
		private static final String KEY_LIMIT_EMAIL_SIZE		= "limitEmailSize"; // SmartSDK V2.12
		private static final String KEY_MAX_EMAIL_SIZE			= "maxEmailSize"; // SmartSDK V2.12
		private static final String KEY_EMAIL_DIVIDING			= "emailDividing"; // SmartSDK V2.12
		private static final String KEY_EMAIL_DIVIDING_NUMBER	= "emailDividingNumber"; // SmartSDK V2.12
		private static final String KEY_FILE_EMAILING_METHOD	= "fileEmailingMethod"; // SmartSDK V2.12


		EmailSetting(Map<String, Object> values) {
			super(values);
		}

		/*
		 * subject (String)
		 */
		public String getSubject() {
			return getStringValue(KEY_SUBJECT);
		}
		public void setSubject(String value) {
			setStringValue(KEY_SUBJECT, value);
		}
		public String removeSubject() {
			return removeStringValue(KEY_SUBJECT);
		}

		/*
		 * body (String)
		 */
		public String getBody() {
			return getStringValue(KEY_BODY);
		}
		public void setBody(String value) {
			setStringValue(KEY_BODY, value);
		}
		public String removeBody() {
			return removeStringValue(KEY_BODY);
		}

		/*
		 * registrationBody (String)
		 * @since SmartSDK V2.12
		 */
		public String getRegistrationBody() {
			return getStringValue(KEY_REGISTRATION_BODY);
		}
		public void setRegistrationBody(String value) {
			setStringValue(KEY_REGISTRATION_BODY, value);
		}
		public String removeRegistrationBody() {
			return removeStringValue(KEY_REGISTRATION_BODY);
		}

		/*
		 * senderEntryId (String)
		 */
		public String getSenderEntryId() {
			return getStringValue(KEY_SENDER_ENTRY_ID);
		}
		public void setSenderEntryId(String value) {
			setStringValue(KEY_SENDER_ENTRY_ID, value);
		}
		public String removeSenderEntryId() {
			return removeStringValue(KEY_SENDER_ENTRY_ID);
		}

		/*
		 * smimeSignature (Boolean)
		 */
		public Boolean getSmimeSignature() {
			return getBooleanValue(KEY_SMIME_SIGNATURE);
		}
		public void setSmimeSignature(Boolean value) {
			setBooleanValue(KEY_SMIME_SIGNATURE, value);
		}
		public Boolean removeSmimeSignature() {
			return removeBooleanValue(KEY_SMIME_SIGNATURE);
		}

		/*
		 * smimeEncryption (Boolean)
		 */
		public Boolean getSmimeEncryption() {
			return getBooleanValue(KEY_SMIME_ENCRYPTION);
		}
		public void setSmimeEncryption(Boolean value) {
			setBooleanValue(KEY_SMIME_ENCRYPTION, value);
		}
		public Boolean removeSmimeEncryption() {
			return removeBooleanValue(KEY_SMIME_ENCRYPTION);
		}
		

		/*
		 * receptionNotice (Boolean)
		 * @since SmartSDK V2.12
		 */
		public Boolean getReceptionNotice() {
			return getBooleanValue(KEY_RECEPTION_NOTICE);
		}
		public void setReceptionNotice(Boolean value) {
			setBooleanValue(KEY_RECEPTION_NOTICE, value);
		}
		public Boolean removeReceptionNotice() {
			return removeBooleanValue(KEY_RECEPTION_NOTICE);
		}

		/*
		 * limitEmailSize (Boolean)
		 * @since SmartSDK V2.12
		 */
		public Boolean getLimitEmailSize() {
			return getBooleanValue(KEY_LIMIT_EMAIL_SIZE);
		}
		public void setLimitEmailSize(Boolean value) {
			setBooleanValue(KEY_LIMIT_EMAIL_SIZE, value);
		}
		public Boolean removeLimitEmailSize() {
			return removeBooleanValue(KEY_LIMIT_EMAIL_SIZE);
		}

		/*
		 * maxEmailSize (Number)
		 * @since SmartSDK V2.12
		 */
		public Integer getMaxEmailSize() {
			return getNumberValue(KEY_MAX_EMAIL_SIZE);
		}
		public void setMaxEmailSize(Integer value) {
			setNumberValue(KEY_MAX_EMAIL_SIZE, value);
		}
		public Integer removeMaxEmailSize() {
			return removeNumberValue(KEY_MAX_EMAIL_SIZE);
		}

		/*
		 * emailDividing (String)
		 * @since SmartSDK V2.12
		 */
		public String getEmailDividing() {
			return getStringValue(KEY_EMAIL_DIVIDING);
		}
		public void setEmailDividing(String value) {
			setStringValue(KEY_EMAIL_DIVIDING, value);
		}
		public String removeEmailDividing() {
			return removeStringValue(KEY_EMAIL_DIVIDING);
		}

		/*
		 * emailDividingNumber (Number)
		 * @since SmartSDK V2.12
		 */
		public Integer getEmailDividingNumber() {
			return getNumberValue(KEY_EMAIL_DIVIDING_NUMBER);
		}
		public void setEmailDividingNumber(Integer value) {
			setNumberValue(KEY_EMAIL_DIVIDING_NUMBER, value);
		}
		public Integer removeEmailDividingNumber() {
			return removeNumberValue(KEY_EMAIL_DIVIDING_NUMBER);
		}

		/*
		 * fileEmailingMethod (String)
		 * @since SmartSDK V2.12
		 */
		public String getFileEmailingMethod() {
			return getStringValue(KEY_FILE_EMAILING_METHOD);
		}
		public void setFileEmailingMethod(String value) {
			setStringValue(KEY_FILE_EMAILING_METHOD, value);
		}
		public String removeFileEmailingMethod() {
			return removeStringValue(KEY_FILE_EMAILING_METHOD);
		}
	}

	public static class DestinationSettingArray extends ArrayElement<DestinationSetting> {

		DestinationSettingArray(List<Map<String, Object>> list) {
			super(list);
		}

		public boolean add(DestinationSetting value) {
			if (value == null) {
				throw new NullPointerException("value must not be null.");
			}
			return list.add(value.cloneValues());
		}

		public DestinationSetting remove(int index) {
			Map<String, Object> value = list.remove(index);
			if (value == null) {
				return null;
			}
			return createElement(value);
		}

		public void clear() {
			list.clear();
		}

		@Override
		protected DestinationSetting createElement(Map<String, Object> values) {
			return new DestinationSetting(values);
		}

	}

	public static class DestinationSetting extends WritableElement {

		private static final String KEY_DESTINATION_TYPE				= "destinationType";
		private static final String KEY_ADDRESSBOOK_DESTINATION_SETTING	= "addressbookDestinationSetting";
		private static final String KEY_MANUAL_DESTINATION_SETTING		= "manualDestinationSetting";

		public DestinationSetting() {
			super(new HashMap<String, Object>());
		}

		DestinationSetting(Map<String, Object> values) {
			super(values);
		}

		/*
		 * destinationType (String)
		 */
		public String getDestinationType() {
			return getStringValue(KEY_DESTINATION_TYPE);
		}
		public void setDestinationType(String value) {
			setStringValue(KEY_DESTINATION_TYPE, value);
		}
		public String removeDestinationType() {
			return removeStringValue(KEY_DESTINATION_TYPE);
		}

		/*
		 * addressbookDestinationSetting (Object)
		 */
		public AddressbookDestinationSetting getAddressbookDestinationSetting() {
			Map<String, Object> value = getObjectValue(KEY_ADDRESSBOOK_DESTINATION_SETTING);
			if (value == null) {
				value = Utils.createElementMap();
				setObjectValue(KEY_ADDRESSBOOK_DESTINATION_SETTING, value);
			}
			return new AddressbookDestinationSetting(value);
		}
//		public void setAddressbookDestinationSetting(AddressbookDestinationSetting value) {
//			throw new UnsupportedOperationException();
//		}
		public AddressbookDestinationSetting removeAddressbookDestinationSetting() {
			Map<String, Object> value = removeObjectValue(KEY_ADDRESSBOOK_DESTINATION_SETTING);
			if (value == null) {
				return null;
			}
			return new AddressbookDestinationSetting(value);
		}

		/*
		 * manualDestinationSetting (Object)
		 */
		public ManualDestinationSetting getManualDestinationSetting() {
			Map<String, Object> value = getObjectValue(KEY_MANUAL_DESTINATION_SETTING);
			if (value == null) {
				value = Utils.createElementMap();
				setObjectValue(KEY_MANUAL_DESTINATION_SETTING, value);
			}
			return new ManualDestinationSetting(value);
		}
//		public void setManualDestinationSetting(ManualDestinationSetting value) {
//			throw new UnsupportedOperationException();
//		}
		public ManualDestinationSetting removeManualDestinationSetting() {
			Map<String, Object> value = removeObjectValue(KEY_MANUAL_DESTINATION_SETTING);
			if (value == null) {
				return null;
			}
			return new ManualDestinationSetting(value);
		}

	}

	public static class AddressbookDestinationSetting extends WritableElement {

		private static final String KEY_DESTINATION_KIND	= "destinationKind";
		private static final String KEY_ENTRY_ID			= "entryId";
		private static final String KEY_REGISTRATION_NO		= "registrationNo";
		private static final String KEY_MAIL_TO_CC_BCC		= "mailToCcBcc";

		AddressbookDestinationSetting(Map<String, Object> values) {
			super(values);
		}

		/*
		 * destinationKind (String)
		 */
		public String getDestinationKind() {
			return getStringValue(KEY_DESTINATION_KIND);
		}
		public void setDestinationKind(String value) {
			setStringValue(KEY_DESTINATION_KIND, value);
		}
		public String removeDestinationKind() {
			return removeStringValue(KEY_DESTINATION_KIND);
		}

		/*
		 * entryId (String)
		 */
		public String getEntryId() {
			return getStringValue(KEY_ENTRY_ID);
		}
		public void setEntryId(String value) {
			setStringValue(KEY_ENTRY_ID, value);
		}
		public String removeEntryId() {
			return removeStringValue(KEY_ENTRY_ID);
		}

		/*
		 * registrationNo (Number)
		 */
		public Integer getRegistrationNo() {
			return getNumberValue(KEY_REGISTRATION_NO);
		}
		public void setRegistrationNo(Integer value) {
			setNumberValue(KEY_REGISTRATION_NO, value);
		}
		public Integer removeRegistrationNo() {
			return removeNumberValue(KEY_REGISTRATION_NO);
		}

		/*
		 * mailToCcBcc (String)
		 */
		public String getMailToCcBcc() {
			return getStringValue(KEY_MAIL_TO_CC_BCC);
		}
		public void setMailToCcBcc(String value) {
			setStringValue(KEY_MAIL_TO_CC_BCC, value);
		}
		public String removeMailToCcBcc() {
			return removeStringValue(KEY_MAIL_TO_CC_BCC);
		}

	}

	public static class ManualDestinationSetting extends WritableElement {

		private static final String KEY_DESTINATION_KIND	= "destinationKind";
		private static final String KEY_MAIL_ADDRESS_INFO	= "mailAddressInfo";
		private static final String KEY_SMB_ADDRESS_INFO	= "smbAddressInfo";
		private static final String KEY_FTP_ADDRESS_INFO	= "ftpAddressInfo";
		private static final String KEY_NCP_ADDRESS_INFO	= "ncpAddressInfo";
		private static final String KEY_USE_LOGIN_USER_AUTH_INFO	= "useLoginUserAuthInfo"; // SmartSDK V2.12

		ManualDestinationSetting(Map<String, Object> values) {
			super(values);
		}

		/*
		 * destinationKind (String)
		 */
		public String getDestinationKind() {
			return getStringValue(KEY_DESTINATION_KIND);
		}
		public void setDestinationKind(String value) {
			setStringValue(KEY_DESTINATION_KIND, value);
		}
		public String removeDestinationKind() {
			return removeStringValue(KEY_DESTINATION_KIND);
		}

		/*
		 * mailAddressInfo (Object)
		 */
		public MailAddressInfo getMailAddressInfo() {
			Map<String, Object> value = getObjectValue(KEY_MAIL_ADDRESS_INFO);
			if (value == null) {
				value = Utils.createElementMap();
				setObjectValue(KEY_MAIL_ADDRESS_INFO, value);
			}
			return new MailAddressInfo(value);
		}
//		public void setMailAddressInfo(MailAddressInfo value) {
//			throw new UnsupportedOperationException();
//		}
		public MailAddressInfo removeMailAddressInfo() {
			Map<String, Object> value = removeObjectValue(KEY_MAIL_ADDRESS_INFO);
			if (value == null) {
				return null;
			}
			return new MailAddressInfo(value);
		}

		/*
		 * smbAddressInfo (Object)
		 */
		public SmbAddressInfo getSmbAddressInfo() {
			Map<String, Object> value = getObjectValue(KEY_SMB_ADDRESS_INFO);
			if (value == null) {
				value = Utils.createElementMap();
				setObjectValue(KEY_SMB_ADDRESS_INFO, value);
			}
			return new SmbAddressInfo(value);
		}
//		public void setSmbAddressInfo(SmbAddressInfo value) {
//			throw new UnsupportedOperationException();
//		}
		public SmbAddressInfo removeSmbAddressInfo() {
			Map<String, Object> value = removeObjectValue(KEY_SMB_ADDRESS_INFO);
			if (value == null) {
				return null;
			}
			return new SmbAddressInfo(value);
		}

		/*
		 * ftpAddressInfo (Object)
		 */
		public FtpAddressInfo getFtpAddressInfo() {
			Map<String, Object> value = getObjectValue(KEY_FTP_ADDRESS_INFO);
			if (value == null) {
				value = Utils.createElementMap();
				setObjectValue(KEY_FTP_ADDRESS_INFO, value);
			}
			return new FtpAddressInfo(value);
		}
//		public void setFtpAddressInfo(FtpAddressInfo value) {
//			throw new UnsupportedOperationException();
//		}
		public FtpAddressInfo removeFtpAddressInfo() {
			Map<String, Object> value = removeObjectValue(KEY_FTP_ADDRESS_INFO);
			if (value == null) {
				return null;
			}
			return new FtpAddressInfo(value);
		}

		/*
		 * ncpAddressInfo (Object)
		 */
		public NcpAddressInfo getNcpAddressInfo() {
			Map<String, Object> value = getObjectValue(KEY_NCP_ADDRESS_INFO);
			if (value == null) {
				value = Utils.createElementMap();
				setObjectValue(KEY_NCP_ADDRESS_INFO, value);
			}
			return new NcpAddressInfo(value);
		}
//		public void setNcpAddressInfo(NcpAddressInfo value) {
//			throw new UnsupportedOperationException();
//		}
		public NcpAddressInfo removeNcpAddressInfo() {
			Map<String, Object> value = removeObjectValue(KEY_NCP_ADDRESS_INFO);
			if (value == null) {
				return null;
			}
			return new NcpAddressInfo(value);
		}

		/*
		 * useLoginUserAuthInfo (Boolean)
		 * @since SmartSDK V2.12
		 */
		public Boolean getUseLoginUserAuthInfo() {
			return getBooleanValue(KEY_USE_LOGIN_USER_AUTH_INFO);
		}
		public void setUseLoginUserAuthInfo(Boolean value) {
			setBooleanValue(KEY_USE_LOGIN_USER_AUTH_INFO, value);
		}
		public Boolean removeUseLoginUserAuthInfo() {
			return removeBooleanValue(KEY_USE_LOGIN_USER_AUTH_INFO);
		}

	}

	public static class MailAddressInfo extends WritableElement {

		private static final String KEY_MAIL_ADDRESS		= "mailAddress";
		private static final String KEY_MAIL_TO_CC_BCC		= "mailToCcBcc";

		MailAddressInfo(Map<String, Object> values) {
			super(values);
		}

		/*
		 * mailAddress (String)
		 */
		public String getMailAddress() {
			return getStringValue(KEY_MAIL_ADDRESS);
		}
		public void setMailAddress(String value) {
			setStringValue(KEY_MAIL_ADDRESS, value);
		}
		public String removeMailAddress() {
			return removeStringValue(KEY_MAIL_ADDRESS);
		}

		/*
		 * mailToCcBcc (String)
		 */
		public String getMailToCcBcc() {
			return getStringValue(KEY_MAIL_TO_CC_BCC);
		}
		public void setMailToCcBcc(String value) {
			setStringValue(KEY_MAIL_TO_CC_BCC, value);
		}
		public String removeMailToCcBcc() {
			return removeStringValue(KEY_MAIL_TO_CC_BCC);
		}

	}

	public static class SmbAddressInfo extends WritableElement {

		private static final String KEY_PATH				= "path";
		private static final String KEY_USER_NAME			= "userName";
		private static final String KEY_PASSWORD			= "password";

		SmbAddressInfo(Map<String, Object> values) {
			super(values);
		}

		/*
		 * path (String)
		 */
		public String getPath() {
			return getStringValue(KEY_PATH);
		}
		public void setPath(String value) {
			setStringValue(KEY_PATH, value);
		}
		public String removePath() {
			return removeStringValue(KEY_PATH);
		}

		/*
		 * userName (String)
		 */
		public String getUserName() {
			return getStringValue(KEY_USER_NAME);
		}
		public void setUserName(String value) {
			setStringValue(KEY_USER_NAME, value);
		}
		public String removeUserName() {
			return removeStringValue(KEY_USER_NAME);
		}

		/*
		 * password (String)
		 */
		public String getPassword() {
			return getStringValue(KEY_PASSWORD);
		}
		public void setPassword(String value) {
			setStringValue(KEY_PASSWORD, value);
		}
		public String removePassword() {
			return removeStringValue(KEY_PASSWORD);
		}

	}

	public static class FtpAddressInfo extends WritableElement {

		private static final String KEY_SERVER_NAME			= "serverName";
		private static final String KEY_PATH				= "path";
		private static final String KEY_USER_NAME			= "userName";
		private static final String KEY_PASSWORD			= "password";
		private static final String KEY_CHARACTER_CODE		= "characterCode";
		private static final String KEY_PORT				= "port";

		FtpAddressInfo(Map<String, Object> values) {
			super(values);
		}

		/*
		 * serverName (String)
		 */
		public String getServerName() {
			return getStringValue(KEY_SERVER_NAME);
		}
		public void setServerName(String value) {
			setStringValue(KEY_SERVER_NAME, value);
		}
		public String removeServerName() {
			return removeStringValue(KEY_SERVER_NAME);
		}

		/*
		 * path (String)
		 */
		public String getPath() {
			return getStringValue(KEY_PATH);
		}
		public void setPath(String value) {
			setStringValue(KEY_PATH, value);
		}
		public String removePath() {
			return removeStringValue(KEY_PATH);
		}

		/*
		 * userName (String)
		 */
		public String getUserName() {
			return getStringValue(KEY_USER_NAME);
		}
		public void setUserName(String value) {
			setStringValue(KEY_USER_NAME, value);
		}
		public String removeUserName() {
			return removeStringValue(KEY_USER_NAME);
		}

		/*
		 * password (String)
		 */
		public String getPassword() {
			return getStringValue(KEY_PASSWORD);
		}
		public void setPassword(String value) {
			setStringValue(KEY_PASSWORD, value);
		}
		public String removePassword() {
			return removeStringValue(KEY_PASSWORD);
		}

		/*
		 * characterCode (String)
		 */
		public String getCharacterCode() {
			return getStringValue(KEY_CHARACTER_CODE);
		}
		public void setCharacterCode(String value) {
			setStringValue(KEY_CHARACTER_CODE, value);
		}
		public String removeCharacterCode() {
			return removeStringValue(KEY_CHARACTER_CODE);
		}

		/*
		 * port (Number)
		 */
		public Integer getPort() {
			return getNumberValue(KEY_PORT);
		}
		public void setPort(Integer value) {
			setNumberValue(KEY_PORT, value);
		}
		public Integer removePort() {
			return removeNumberValue(KEY_PORT);
		}

	}

	public static class NcpAddressInfo extends WritableElement {

		private static final String KEY_PATH				= "path";
		private static final String KEY_USER_NAME			= "userName";
		private static final String KEY_PASSWORD			= "password";
		private static final String KEY_CONNECTION_TYPE		= "connectionType";

		NcpAddressInfo(Map<String, Object> values) {
			super(values);
		}

		/*
		 * path (String)
		 */
		public String getPath() {
			return getStringValue(KEY_PATH);
		}
		public void setPath(String value) {
			setStringValue(KEY_PATH, value);
		}
		public String removePath() {
			return removeStringValue(KEY_PATH);
		}

		/*
		 * userName (String)
		 */
		public String getUserName() {
			return getStringValue(KEY_USER_NAME);
		}
		public void setUserName(String value) {
			setStringValue(KEY_USER_NAME, value);
		}
		public String removeUserName() {
			return removeStringValue(KEY_USER_NAME);
		}

		/*
		 * password (String)
		 */
		public String getPassword() {
			return getStringValue(KEY_PASSWORD);
		}
		public void setPassword(String value) {
			setStringValue(KEY_PASSWORD, value);
		}
		public String removePassword() {
			return removeStringValue(KEY_PASSWORD);
		}

		/*
		 * connectionType (String)
		 */
		public String getConnectionType() {
			return getStringValue(KEY_CONNECTION_TYPE);
		}
		public void setConnectionType(String value) {
			setStringValue(KEY_CONNECTION_TYPE, value);
		}
		public String removeConnectionType() {
			return removeStringValue(KEY_CONNECTION_TYPE);
		}

	}

	/*
	 * @since SmartSDK V1.01
	 */
	public static class StoreLocalSetting extends WritableElement {

		private static final String KEY_FOLDER_ID			= "folderId";
		private static final String KEY_FOLDER_PASSWORD		= "folderPassword";
		private static final String KEY_FILE_NAME			= "fileName";
		private static final String KEY_FILE_PASSWORD		= "filePassword";
		private static final String KEY_USER_NAME			= "userName"; // SmartSDK V2.12
		private static final String KEY_ACL_INFO			= "aclInfo"; // SmartSDK V2.12

		StoreLocalSetting(Map<String, Object> values) {
			super(values);
		}

		/*
		 * folderId (String)
		 * @since SmartSDK V1.01
		 */
		public String getFolderId() {
			return getStringValue(KEY_FOLDER_ID);
		}
		public void setFolderId(String value) {
			setStringValue(KEY_FOLDER_ID, value);
		}
		public String removeFolderId() {
			return removeStringValue(KEY_FOLDER_ID);
		}

		/*
		 * folderPassword (String)
		 * @since SmartSDK V1.01
		 */
		public String getFolderPassword() {
			return getStringValue(KEY_FOLDER_PASSWORD);
		}
		public void setFolderPassword(String value) {
			setStringValue(KEY_FOLDER_PASSWORD, value);
		}
		public String removeFolderPassword() {
			return removeStringValue(KEY_FOLDER_PASSWORD);
		}

		/*
		 * fileName (String)
		 * @since SmartSDK V1.01
		 */
		public String getFileName() {
			return getStringValue(KEY_FILE_NAME);
		}
		public void setFileName(String value) {
			setStringValue(KEY_FILE_NAME, value);
		}
		public String removeFileName() {
			return removeStringValue(KEY_FILE_NAME);
		}

		/*
		 * filePassword (String)
		 * @since SmartSDK V1.01
		 */
		public String getFilePassword() {
			return getStringValue(KEY_FILE_PASSWORD);
		}
		public void setFilePassword(String value) {
			setStringValue(KEY_FILE_PASSWORD, value);
		}
		public String removeFilePassword() {
			return removeStringValue(KEY_FILE_PASSWORD);
		}
		

		/*
		 * userName (String)
		 * @since SmartSDK V2.12
		 */
		public String getUserName() {
			return getStringValue(KEY_USER_NAME);
		}
		public void setUserName(String value) {
			setStringValue(KEY_USER_NAME, value);
		}
		public String removeUserName() {
			return removeStringValue(KEY_USER_NAME);
		}

		/*
		 * AclInfo (Array[Object])
		 * @since SmartSDK V2.12
		 */
		public AclInfoArray getAclInfo() {
			List<Map<String, Object>> value = getArrayValue(KEY_ACL_INFO);
			if (value == null) {
				value = Utils.createElementList();
				setArrayValue(KEY_ACL_INFO, value);
			}
			return new AclInfoArray(value);
		}
		public AclInfoArray removeAclInfo() {
			List<Map<String, Object>> value = removeArrayValue(KEY_ACL_INFO);
			if (value == null) {
				return null;
			}
			return new AclInfoArray(value);
		}

	}

	/*
	 * @since SmartSDK V1.01
	 */
	public static class SendStoredFileSetting extends WritableElement {

		private static final String FOLDER_INFO			= "folderInfo";
		private static final String STORED_FILE_INFO	= "storedFileInfo";

		SendStoredFileSetting(Map<String, Object> values) {
			super(values);
		}

		/*
		 * folderInfo (Object)
		 * @since SmartSDK V1.01
		 */
		public FolderInfo getFolderInfo() {
			Map<String, Object> value = getObjectValue(FOLDER_INFO);
			if (value == null) {
				value = Utils.createElementMap();
				setObjectValue(FOLDER_INFO, value);
			}
			return new FolderInfo(value);
		}
//		public void setFolderInfo(FolderInfo value) {
//			throw new UnsupportedOperationException();
//		}
		public FolderInfo removeFolderInfo() {
			Map<String, Object> value = removeObjectValue(FOLDER_INFO);
			if (value == null) {
				return null;
			}
			return new FolderInfo(value);
		}

		/*
		 * storedFileInfo (Array[Object])
		 * @since SmartSDK V1.01
		 */
		public StoredFileInfoArray getStoredFileInfo() {
			List<Map<String, Object>> value = getArrayValue(STORED_FILE_INFO);
			if (value == null) {
				value = Utils.createElementList();
				setArrayValue(STORED_FILE_INFO, value);
			}
			return new StoredFileInfoArray(value);
		}
//		public void setStoredFileInfo(StoredFileInfoArray value) {
//			throw new UnsupportedOperationException();
//		}
		public StoredFileInfoArray removeStoredFileInfo() {
			List<Map<String, Object>> value = removeArrayValue(STORED_FILE_INFO);
			if (value == null) {
				return null;
			}
			return new StoredFileInfoArray(value);
		}

	}

	/*
	 * @since SmartSDK V1.01
	 */
	public static class FolderInfo extends WritableElement {

		private static final String KEY_FOLDER_ID			= "folderId";
		private static final String KEY_FOLDER_PASSWORD		= "folderPassword";

		FolderInfo(Map<String, Object> values) {
			super(values);
		}

		/*
		 * folderId (String)
		 * @since SmartSDK V1.01
		 */
		public String getFolderId() {
			return getStringValue(KEY_FOLDER_ID);
		}
		public void setFolderId(String value) {
			setStringValue(KEY_FOLDER_ID, value);
		}
		public String removeFolderId() {
			return removeStringValue(KEY_FOLDER_ID);
		}

		/*
		 * folderPassword (String)
		 * @since SmartSDK V1.01
		 */
		public String getFolderPassword() {
			return getStringValue(KEY_FOLDER_PASSWORD);
		}
		public void setFolderPassword(String value) {
			setStringValue(KEY_FOLDER_PASSWORD, value);
		}
		public String removeFolderPassword() {
			return removeStringValue(KEY_FOLDER_PASSWORD);
		}

	}

	/*
	 * @since SmartSDK V1.01
	 */
	public static class StoredFileInfoArray extends ArrayElement<StoredFileInfo> {

		StoredFileInfoArray(List<Map<String, Object>> list) {
			super(list);
		}

		/*
		 * @since SmartSDK V1.01
		 */
		public boolean add(StoredFileInfo value) {
			if (value == null) {
				throw new NullPointerException("value must not be null.");
			}
			return list.add(value.cloneValues());
		}

		/*
		 * @since SmartSDK V1.01
		 */
		public StoredFileInfo remove(int index) {
			Map<String, Object> value = list.remove(index);
			if (value == null) {
				return null;
			}
			return createElement(value);
		}

		/*
		 * @since SmartSDK V1.01
		 */
		public void clear() {
			list.clear();
		}

		@Override
		protected StoredFileInfo createElement(Map<String, Object> values) {
			return new StoredFileInfo(values);
		}

	}

	/*
	 * @since SmartSDK V1.01
	 */
	public static class StoredFileInfo extends WritableElement {

		private static final String KEY_FILE_ID			= "fileId";
		private static final String KEY_FILE_PASSWORD	= "filePassword";

		public StoredFileInfo() {
			super(new HashMap<String, Object>());
		}

		StoredFileInfo(Map<String, Object> values) {
			super(values);
		}

		/*
		 * fileId (String)
		 * @since SmartSDK V1.01
		 */
		public String getFileId() {
			return getStringValue(KEY_FILE_ID);
		}
		public void setFileId(String value) {
			setStringValue(KEY_FILE_ID, value);
		}
		public String removeFileId() {
			return removeStringValue(KEY_FILE_ID);
		}

		/*
		 * filePassword (String)
		 * @since SmartSDK V1.01
		 */
		public String getFilePassword() {
			return getStringValue(KEY_FILE_PASSWORD);
		}
		public void setFilePassword(String value) {
			setStringValue(KEY_FILE_PASSWORD, value);
		}
		public String removeFilePassword() {
			return removeStringValue(KEY_FILE_PASSWORD);
		}

	}

	/*
	 * @since SmartSDK V2.12
	 */
	public static class OriginalTrimmingSetting extends WritableElement {

		private static final String KEY_ORIGINAL_SIZE_CUSTOM_HORIZONTAL2	= "originalSizeCustomHorizontal2";
		private static final String KEY_ORIGINAL_SIZE_CUSTOM_VERTICAL2		= "originalSizeCustomVertical2";
		private static final String KEY_ORIGINAL_SIZE_CUSTOM_HORIZONTAL3	= "originalSizeCustomHorizontal3";
		private static final String KEY_ORIGINAL_SIZE_CUSTOM_VERTICAL3		= "originalSizeCustomVertical3";

		OriginalTrimmingSetting(Map<String, Object> values) {
			super(values);
		}

		/*
		 * originalSizeCustomHorizontal2 (String)
		 * @since SmartSDK V2.12
		 */
		public String getOriginalSizeCustomHorizontal2() {
			return getStringValue(KEY_ORIGINAL_SIZE_CUSTOM_HORIZONTAL2);
		}
		public void setOriginalSizeCustomHorizontal2(String value) {
			setStringValue(KEY_ORIGINAL_SIZE_CUSTOM_HORIZONTAL2, value);
		}
		public String removeOriginalSizeCustomHorizontal2() {
			return removeStringValue(KEY_ORIGINAL_SIZE_CUSTOM_HORIZONTAL2);
		}

		/*
		 * originalSizeCustomVertical2 (String)
		 * @since SmartSDK V2.12
		 */
		public String getOriginalSizeCustomVertical2() {
			return getStringValue(KEY_ORIGINAL_SIZE_CUSTOM_VERTICAL2);
		}
		public void setOriginalSizeCustomVertical2(String value) {
			setStringValue(KEY_ORIGINAL_SIZE_CUSTOM_VERTICAL2, value);
		}
		public String removeOriginalSizeCustomVertical2() {
			return removeStringValue(KEY_ORIGINAL_SIZE_CUSTOM_VERTICAL2);
		}

		/*
		 * originalSizeCustomHorizontal3 (String)
		 * @since SmartSDK V2.12
		 */
		public String getOriginalSizeCustomHorizontal3() {
			return getStringValue(KEY_ORIGINAL_SIZE_CUSTOM_HORIZONTAL3);
		}
		public void setOriginalSizeCustomHorizontal3(String value) {
			setStringValue(KEY_ORIGINAL_SIZE_CUSTOM_HORIZONTAL3, value);
		}
		public String removeOriginalSizeCustomHorizontal3() {
			return removeStringValue(KEY_ORIGINAL_SIZE_CUSTOM_HORIZONTAL3);
		}

		/*
		 * originalSizeCustomVertical3 (String)
		 * @since SmartSDK V2.12
		 */
		public String getOriginalSizeCustomVertical3() {
			return getStringValue(KEY_ORIGINAL_SIZE_CUSTOM_VERTICAL3);
		}
		public void setOriginalSizeCustomVertical3(String value) {
			setStringValue(KEY_ORIGINAL_SIZE_CUSTOM_VERTICAL3, value);
		}
		public String removeOriginalSizeCustomVertical3() {
			return removeStringValue(KEY_ORIGINAL_SIZE_CUSTOM_VERTICAL3);
		}
	}

	/*
	 * @since SmartSDK V2.12
	 */
	public static class DivideSetting extends WritableElement {

		private static final String KEY_PER_SHEETS		= "perSheets";
		private static final String KEY_DIVISION_CHECK	= "divisionCheck";

		DivideSetting(Map<String, Object> values) {
			super(values);
		}

		/*
		 * perSheets (Number)
		 * @since SmartSDK V2.12
		 */
		public Integer getPerSheets() {
			return getNumberValue(KEY_PER_SHEETS);
		}
		public void setPerSheets(Integer value) {
			setNumberValue(KEY_PER_SHEETS, value);
		}
		public Integer removePerSheets() {
			return getNumberValue(KEY_PER_SHEETS);
		}

		/*
		 * divisionCheck (Boolean)
		 * @since SmartSDK V2.12
		 */
		public Boolean getDivisionCheck() {
			return getBooleanValue(KEY_DIVISION_CHECK);
		}
		public void setDivisionCheck(Boolean value) {
			setBooleanValue(KEY_DIVISION_CHECK, value);
		}
		public Boolean removeDivisionCheck() {
			return getBooleanValue(KEY_DIVISION_CHECK);
		}
	}

	/*
	 * @since SmartSDK V2.12
	 */
	public static class EraseCenterSetting extends WritableElement {

		private static final String KEY_ERASE_WIDTH		= "eraseWidth";
		
		EraseCenterSetting(Map<String, Object> values) {
			super(values);
		}

		/*
		 * eraseWidth (String)
		 * @since SmartSDK V2.12
		 */
		public String getEraseWidth() {
			return getStringValue(KEY_ERASE_WIDTH);
		}
		public void setEraseWidth(String value) {
			setStringValue(KEY_ERASE_WIDTH, value);
		}
		public String removeEraseWidth() {
			return getStringValue(KEY_ERASE_WIDTH);
		}
	}

	/*
	 * @since SmartSDK V2.12
	 */
	public static class EraseBorderSetting extends WritableElement {

		private static final String KEY_ERASE_WIDTH_LEFT	= "eraseWidthLeft";
		private static final String KEY_ERASE_WIDTH_RIGHT	= "eraseWidthRight";
		private static final String KEY_ERASE_WIDTH_TOP		= "eraseWidthTop";
		private static final String KEY_ERASE_WIDTH_BOTTOM	= "eraseWidthBottom";
		
		EraseBorderSetting(Map<String, Object> values) {
			super(values);
		}

		/*
		 * eraseWidthLeft (String)
		 * @since SmartSDK V2.12
		 */
		public String getEraseWidthLeft() {
			return getStringValue(KEY_ERASE_WIDTH_LEFT);
		}
		public void setEraseWidthLeft(String value) {
			setStringValue(KEY_ERASE_WIDTH_LEFT, value);
		}
		public String removeEraseWidthLeft() {
			return getStringValue(KEY_ERASE_WIDTH_LEFT);
		}

		/*
		 * eraseWidthRight (String)
		 * @since SmartSDK V2.12
		 */
		public String getEraseWidthRight() {
			return getStringValue(KEY_ERASE_WIDTH_RIGHT);
		}
		public void setEraseWidthRight(String value) {
			setStringValue(KEY_ERASE_WIDTH_RIGHT, value);
		}
		public String removeEraseWidthRight() {
			return getStringValue(KEY_ERASE_WIDTH_RIGHT);
		}

		/*
		 * eraseWidthTop (String)
		 * @since SmartSDK V2.12
		 */
		public String getEraseWidthTop() {
			return getStringValue(KEY_ERASE_WIDTH_TOP);
		}
		public void setEraseWidthTop(String value) {
			setStringValue(KEY_ERASE_WIDTH_TOP, value);
		}
		public String removeEraseWidthTop() {
			return getStringValue(KEY_ERASE_WIDTH_TOP);
		}

		/*
		 * eraseWidthBottom (String)
		 * @since SmartSDK V2.12
		 */
		public String getEraseWidthBottom() {
			return getStringValue(KEY_ERASE_WIDTH_BOTTOM);
		}
		public void setEraseWidthBottom(String value) {
			setStringValue(KEY_ERASE_WIDTH_BOTTOM, value);
		}
		public String removeEraseWidthBottom() {
			return getStringValue(KEY_ERASE_WIDTH_BOTTOM);
		}
	}

	/*
	 * @since SmartSDK V2.12
	 */
	public static class DropoutColorSetting extends WritableElement {

		private static final String KEY_ERASE_COLOR			= "eraseColor";
		private static final String KEY_ERASE_COLOR_LEVEL	= "eraseColorLevel";
		
		DropoutColorSetting(Map<String, Object> values) {
			super(values);
		}

		/*
		 * eraseColor (String)
		 * @since SmartSDK V2.12
		 */
		public String getEraseColor() {
			return getStringValue(KEY_ERASE_COLOR);
		}
		public void setEraseColor(String value) {
			setStringValue(KEY_ERASE_COLOR, value);
		}
		public String removeEraseColor() {
			return getStringValue(KEY_ERASE_COLOR);
		}

		/*
		 * eraseColorLevel (String)
		 * @since SmartSDK V2.12
		 */
		public String getEraseColorLevel() {
			return getStringValue(KEY_ERASE_COLOR_LEVEL);
		}
		public void setEraseColorLevel(String value) {
			setStringValue(KEY_ERASE_COLOR_LEVEL, value);
		}
		public String removeEraseColorLevel() {
			return getStringValue(KEY_ERASE_COLOR_LEVEL);
		}
	}

	/*
	 * @since SmartSDK V2.12
	 */
	public static class AclInfoArray extends ArrayElement<AclInfo> {

		AclInfoArray(List<Map<String, Object>> list) {
			super(list);
		}
		
		/*
		 * @since SmartSDK V2.12
		 */
		public boolean add(AclInfo value) {
			if (value == null) {
				throw new NullPointerException("value must not be null.");
			}
			return list.add(value.cloneValues());
		}
		
		/*
		 * @since SmartSDK V2.12
		 */
		public AclInfo remove(int index) {
			Map<String, Object> value = list.remove(index);
			if (value == null) {
				return null;
			}
			return createElement(value);
		}
		
		/*
		 * @since SmartSDK V2.12
		 */
		public void clear() {
			list.clear();
		}

		@Override
		protected AclInfo createElement(Map<String, Object> values) {
			return new AclInfo(values);
		}
	}

	/*
	 * @since SmartSDK V2.12
	 */
	public static class AclInfo extends WritableElement {

		private static final String KEY_ENTRY_ID	= "entryId";
		private static final String KEY_AUTHORITY	= "authority";
		
		AclInfo(Map<String, Object> values) {
			super(values);
		}

		/*
		 * entryId (String)
		 * @since SmartSDK V2.12
		 */
		public String getEntryId() {
			return getStringValue(KEY_ENTRY_ID);
		}
		public void setEntryId(String value) {
			setStringValue(KEY_ENTRY_ID, value);
		}
		public String removeEntryId() {
			return getStringValue(KEY_ENTRY_ID);
		}

		/*
		 * authority (String)
		 * @since SmartSDK V2.12
		 */
		public String getAuthority() {
			return getStringValue(KEY_AUTHORITY);
		}
		public void setAuthority(String value) {
			setStringValue(KEY_AUTHORITY, value);
		}
		public String removeAuthority() {
			return getStringValue(KEY_AUTHORITY);
		}
	}
}
