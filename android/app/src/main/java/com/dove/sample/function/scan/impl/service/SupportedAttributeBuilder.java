/*
 *  Copyright (C) 2013-2014 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.function.scan.impl.service;

import com.dove.sample.function.scan.attribute.ScanRequestAttribute;
import com.dove.sample.function.scan.attribute.standard.AutoCorrectJobSetting;
import com.dove.sample.function.scan.attribute.standard.AutoDensity;
import com.dove.sample.function.scan.attribute.standard.DestinationSetting;
import com.dove.sample.function.scan.attribute.standard.EmailSetting;
import com.dove.sample.function.scan.attribute.standard.FileSetting;
import com.dove.sample.function.scan.attribute.standard.JobMode;
import com.dove.sample.function.scan.attribute.standard.JobStoppedTimeoutPeriod;
import com.dove.sample.function.scan.attribute.standard.Magnification;
import com.dove.sample.function.scan.attribute.standard.MagnificationSize;
import com.dove.sample.function.scan.attribute.standard.ManualDensity;
import com.dove.sample.function.scan.attribute.standard.Ocr;
import com.dove.sample.function.scan.attribute.standard.OcrSetting;
import com.dove.sample.function.scan.attribute.standard.OriginalOrientation;
import com.dove.sample.function.scan.attribute.standard.OriginalOutputExit;
import com.dove.sample.function.scan.attribute.standard.OriginalPreview;
import com.dove.sample.function.scan.attribute.standard.OriginalSide;
import com.dove.sample.function.scan.attribute.standard.OriginalSize;
import com.dove.sample.function.scan.attribute.standard.OriginalSizeCustomX;
import com.dove.sample.function.scan.attribute.standard.OriginalSizeCustomY;
import com.dove.sample.function.scan.attribute.standard.PdfSetting;
import com.dove.sample.function.scan.attribute.standard.ScanColor;
import com.dove.sample.function.scan.attribute.standard.ScanDevice;
import com.dove.sample.function.scan.attribute.standard.ScanMethod;
import com.dove.sample.function.scan.attribute.standard.ScanResolution;
import com.dove.sample.function.scan.attribute.standard.SecuredPdfSetting;
import com.dove.sample.function.scan.attribute.standard.SendStoredFileSetting;
import com.dove.sample.function.scan.attribute.standard.StoreLocalSetting;
import com.dove.sample.function.scan.supported.DestinationSettingSupported;
import com.dove.sample.function.scan.supported.EmailSettingSupported;
import com.dove.sample.function.scan.supported.FileSettingSupported;
import com.dove.sample.function.scan.supported.MagnificationSizeSupported;
import com.dove.sample.function.scan.supported.MagnificationSupported;
import com.dove.sample.function.scan.supported.OcrSettingSupported;
import com.dove.sample.function.scan.supported.PdfSettingSupported;
import com.dove.sample.function.scan.supported.SecuredPdfSettingSupported;
import com.dove.sample.function.scan.supported.SendStoredFileSettingSupported;
import com.dove.sample.function.scan.supported.StoreLocalSettingSupported;
import com.dove.sample.wrapper.rws.service.scanner.Capability;

import java.util.HashMap;
import java.util.Map;

/**
 * 指定したCapabilityオブジェクトから、SupportedAttributeを生成するためのクラスです。
 * The class to create supportedAttribute from specified capability object.
 */
public class SupportedAttributeBuilder {

	private SupportedAttributeBuilder(){
	}

	public static Map<Class<? extends ScanRequestAttribute>,Object> getSupportedAttribute(Capability cap) {

		Map<Class<? extends ScanRequestAttribute>, Object> retList = new HashMap<Class<? extends ScanRequestAttribute>, Object>();

        if( cap == null ) {
            return null;
        }

		/*
		 * Capability#getXXXを取得し、値が格納されている場合のみCapabilityとして登録します。
		 * Obtains Capability#getXXX and registers as capability only if value is stored.
		 */

		if( cap.getAutoCorrectJobSettingList() != null ) {
			retList.put(AutoCorrectJobSetting.class, AutoCorrectJobSetting.getSupportedValue(cap.getAutoCorrectJobSettingList()));
		}
		if ( cap.getJobModeList() != null ) {
			retList.put(JobMode.class, JobMode.getSupportedValue(cap.getJobModeList()));
		}
        if ( cap.getJobStoppedTimeoutPeriodRange() != null ) {
            // SmartSDK V1.02
            retList.put(JobStoppedTimeoutPeriod.class, JobStoppedTimeoutPeriod.getSupportedValue(cap.getJobStoppedTimeoutPeriodRange()));
        }
		if ( cap.getOriginalSizeList() != null ) {
			retList.put(OriginalSize.class, OriginalSize.getSupportedValue(cap.getOriginalSizeList()));
		}
		if( cap.getOriginalSizeCustomXRange() != null ) {
			retList.put(OriginalSizeCustomX.class, OriginalSizeCustomX.getSupportedValue(cap.getOriginalSizeCustomXRange()));
		}
		if( cap.getOriginalSizeCustomYRange() != null ) {
			retList.put(OriginalSizeCustomY.class, OriginalSizeCustomY.getSupportedValue(cap.getOriginalSizeCustomYRange()));
		}
		if( cap.getScanDeviceList() != null ) {
			retList.put(ScanDevice.class, ScanDevice.getSupportedValue(cap.getScanDeviceList()));
		}
		if( cap.getScanMethodList() != null ) {
			retList.put(ScanMethod.class, ScanMethod.getSupportedValue(cap.getScanMethodList()));
		}
		if( cap.getOriginalOutputExitList() != null ) {
			retList.put(OriginalOutputExit.class, OriginalOutputExit.getSupportedValue(cap.getOriginalOutputExitList()));
		}
		if( cap.getOriginalSideList() != null ) {
			retList.put(OriginalSide.class, OriginalSide.getSupportedValue(cap.getOriginalSideList()));
		}
		if( cap.getOriginalOrientationList() != null) {
			retList.put(OriginalOrientation.class, OriginalOrientation.getSupportedValue(cap.getOriginalOrientationList()));
		}
		if( cap.getOriginalPreviewList() != null ) {
			retList.put(OriginalPreview.class, OriginalPreview.getSupportedValue(cap.getOriginalPreviewList()));
		}
		if( cap.getScanColorList() != null ) {
			retList.put(ScanColor.class, ScanColor.getSupportedValue(cap.getScanColorList()));
		}
		if( cap.getMagnificationRange() != null ) {
			retList.put(Magnification.class, new MagnificationSupported(cap.getMagnificationRange()));
		}
		if( cap.getMagnificationSize() != null ) {
			retList.put(MagnificationSize.class, new MagnificationSizeSupported(cap.getMagnificationSize()));
		}
		if( cap.getScanResolutionList() != null ) {
            retList.put(ScanResolution.class, ScanResolution.getSupportedValue(cap.getScanResolutionList()));
		}
		if ( cap.getAutoDensityList() != null ) {
			retList.put(AutoDensity.class, AutoDensity.getSupportedValue(cap.getAutoDensityList()));
		}
		if( cap.getManualDensityRange() != null ) {
            retList.put(ManualDensity.class,ManualDensity.getSupportedValue(cap.getManualDensityRange()));
		}
		if( cap.getFileSettingCapability() != null ) {
			retList.put(FileSetting.class, new FileSettingSupported(cap.getFileSettingCapability()));
		}
		if( cap.getPdfSettingCapability() != null ) {
            retList.put(PdfSetting.class, new PdfSettingSupported(cap.getPdfSettingCapability()));
		}
		if( cap.getOcrList() != null ) {
            retList.put(Ocr.class, Ocr.getSupportedValue(cap.getOcrList()));
		}
		if( cap.getOcrSettingCapability() != null ) {
            retList.put(OcrSetting.class, new OcrSettingSupported(cap.getOcrSettingCapability()));
		}
		if( cap.getSecuredPdfSettingCapability() != null ) {
            retList.put(SecuredPdfSetting.class, new SecuredPdfSettingSupported(cap.getSecuredPdfSettingCapability()));
		}
		if( cap.getEmailSettingCapability() != null ) {
			retList.put(EmailSetting.class, new EmailSettingSupported(cap.getEmailSettingCapability()));
		}
		if( cap.getDestinationSettingCapability() != null ) {
			retList.put(DestinationSetting.class, new DestinationSettingSupported(cap.getDestinationSettingCapability()));
		}
		if( cap.getStoreLocalSettingCapability() != null ) {
		    // SmartSDK V1.01
			retList.put(StoreLocalSetting.class, new StoreLocalSettingSupported(cap.getStoreLocalSettingCapability()));
		}
		if( cap.getSendStoredFileSettingCapability() != null ) {
		    // SmartSDK V1.01
			retList.put(SendStoredFileSetting.class, new SendStoredFileSettingSupported(cap.getSendStoredFileSettingCapability()));
		}

		return retList;
	}
}
