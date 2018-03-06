/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.service.copy;

import com.dove.sample.wrapper.common.RequestBody;
import com.dove.sample.wrapper.common.Utils;
import com.dove.sample.wrapper.common.WritableElement;
import com.dove.sample.wrapper.json.EncodedException;
import com.dove.sample.wrapper.json.JsonUtils;
import com.dove.sample.wrapper.log.Logger;

import java.util.HashMap;
import java.util.Map;

public class UpdateJobStatusRequestBody extends WritableElement implements RequestBody {

	private static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";

	private static final String KEY_JOB_STATUS			= "jobStatus";
	private static final String KEY_SCANNING_INFO		= "scanningInfo";
	private static final String KEY_PRINTING_INFO       = "printingInfo";  // SmartSDK v2.30
	private static final String KEY_VALIDATE_ONLY		= "validateOnly";
	private static final String KEY_JOB_SETTING			= "jobSetting";
	
    /**
     * Define the prefix for log information with abbreviation package and class name
     */
    private final static String PREFIX = "copy:UpdateJobStatusReq:";

	public UpdateJobStatusRequestBody() {
		super(new HashMap<String, Object>());
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
	 * jobStatus (String)
	 */
	public String getJobStatus() {
		return getStringValue(KEY_JOB_STATUS);
	}
	public void setJobStatus(String value) {
		setStringValue(KEY_JOB_STATUS, value);
	}
	public String removeJobStatus() {
		return removeStringValue(KEY_JOB_STATUS);
	}

	/*
	 * scanningInfo (Object)
	 */
	public ScanningInfo getScanningInfo() {
		Map<String, Object> value = getObjectValue(KEY_SCANNING_INFO);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_SCANNING_INFO, value);
		}
		return new ScanningInfo(value);
	}
//	public void setScanningInfo(ScanningInfo value) {
//		throw new UnsupportedOperationException();
//	}
	public ScanningInfo removeScanningInfo() {
		Map<String, Object> value = removeObjectValue(KEY_SCANNING_INFO);
		if (value == null) {
			return null;
		}
		return new ScanningInfo(value);
	}
	
	/*
	 * printingInfo (Object)
	 * 
	 * @since SmartSDK v2.30
	 */
	public PrintingInfo getPrintingInfo() {
		Map<String, Object> value = getObjectValue(KEY_PRINTING_INFO);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_PRINTING_INFO, value);
		}
		return new PrintingInfo(value);
	}
	public PrintingInfo removePrintingInfo() {
		Map<String, Object> value = removeObjectValue(KEY_PRINTING_INFO);
		if (value == null) {
			return null;
		}
		return new PrintingInfo(value);
	}

	/*
	 * validateOnly (Boolean)
	 */
	public Boolean getValidateOnly() {
		return getBooleanValue(KEY_VALIDATE_ONLY);
	}
	public void setValidateOnly(Boolean value) {
		setBooleanValue(KEY_VALIDATE_ONLY, value);
	}
	public Boolean removeValidateOnly() {
		return removeBooleanValue(KEY_VALIDATE_ONLY);
	}

	/*
	 * jobSetting (Object)
	 */
	public JobSetting getJobSetting() {
		Map<String, Object> value = getObjectValue(KEY_JOB_SETTING);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_JOB_SETTING, value);
		}
		return new JobSetting(value);
	}
//	public void setJobSetting(JobSetting value) {
//		throw new UnsupportedOperationException();
//	}
	public JobSetting removeJobSetting() {
		Map<String, Object> value = removeObjectValue(KEY_JOB_SETTING);
		if (value == null) {
			return null;
		}
		return new JobSetting(value);
	}


	public static class ScanningInfo extends WritableElement {

		private static final String KEY_JOB_STATUS	= "jobStatus";
		private static final String KEY_SADF_AUTO_MODE = "sadfAutoMode";  // SmartSDK V2.12

		ScanningInfo(Map<String, Object> values) {
			super(values);
		}

		/*
		 * jobStatus (String)
		 */
		public String getJobStatus() {
			return getStringValue(KEY_JOB_STATUS);
		}
		public void setJobStatus(String value) {
			setStringValue(KEY_JOB_STATUS, value);
		}
		public String removeJobStatus() {
			return removeStringValue(KEY_JOB_STATUS);
		}
		
		/*
		 * sadfAutoMode (Boolean)
		 * @since SmartSDK V2.12
		 */
		public Boolean getSadfAutoMode() {
			return getBooleanValue(KEY_SADF_AUTO_MODE);
		}
	    public void setSadfAutoMode(Boolean value) {
	    	setBooleanValue(KEY_SADF_AUTO_MODE, value);
	    }
	    public Boolean removeSadfAutoMode() {
	    	return removeBooleanValue(KEY_SADF_AUTO_MODE);
	    }


	}
	
	/*
	 * @since SmartSDK v2.30
	 */
	public static class PrintingInfo extends WritableElement {
		
		private static final String KEY_SAMPLE_COPY_STATUS  = "sampleCopyStatus";  // SmartSDK v2.30

		PrintingInfo(Map<String, Object> values) {
			super(values);
		}
		
		/*
		 * sampleCopyStatus (String)
		 * 
		 * @since SmartSDK v2.30
		 */
		public String getSampleCopyStatus() {
			return getStringValue(KEY_SAMPLE_COPY_STATUS);
		}
		public void setSampleCopyStatus(String value) {
			setStringValue(KEY_SAMPLE_COPY_STATUS, value);
		}
		public String removeSampleCopyStatus() {
			return removeStringValue(KEY_SAMPLE_COPY_STATUS);
		}
		
	
	}	

}
