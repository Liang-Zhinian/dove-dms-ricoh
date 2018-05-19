/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.function.scan.supported;

import java.util.List;

import com.dove.sample.function.scan.attribute.standard.FileSetting.CompressionLevel;
import com.dove.sample.function.scan.attribute.standard.FileSetting.CompressionMethod;
import com.dove.sample.function.scan.attribute.standard.FileSetting.FileFormat;
import com.dove.sample.wrapper.rws.service.scanner.Capability.FileSettingCapability;

public final class FileSettingSupported {

	private List<CompressionMethod> compMethodList;
	private List<CompressionLevel> compLevelList;
	private List<FileFormat> fileFormatList;
	private List<Boolean> supportedMultipageFormat;
	private MaxLengthSupported supportedFileName;
	private List<Boolean> supportedNameTime;
	private MaxMinSupported supportedStartNo; //SmartSDK V2.12

	public FileSettingSupported(FileSettingCapability capability) {
		compMethodList = CompressionMethod.getSupportedValue(capability.getCompressionMethodList());
		compLevelList = CompressionLevel.getSupportedValue(capability.getCompressionLevelList());
		fileFormatList = FileFormat.getSupportedValue(capability.getFileFormatList());
		supportedMultipageFormat = capability.getMultiPageFormatList();
		supportedFileName = MaxLengthSupported.getMaxLengthSupported(capability.getFileNameLength());
		supportedNameTime = capability.getFileNameTimeStampList();
		supportedStartNo = MaxMinSupported.getMaxMinSupported(capability.getStartNoRange());
	}
	
	public List<CompressionMethod> getCompMethodList() {
		return compMethodList;
	}

	public List<CompressionLevel> getCompLevelList() {
		return compLevelList;
	}

	public List<FileFormat> getFileFormatList() {
		return fileFormatList;
	}

	public List<Boolean> getMultipageFormat() {
		return supportedMultipageFormat;
	}

	public MaxLengthSupported getSupportedFileName() {
		return supportedFileName;
	}

	public List<Boolean> getSupportedNameTime() {
		return supportedNameTime;
	}
	
	/**
	 * @since SmartSDK V2.12
	 */
	public MaxMinSupported getSupportedStartNo(){
		return supportedStartNo;
	}

}
