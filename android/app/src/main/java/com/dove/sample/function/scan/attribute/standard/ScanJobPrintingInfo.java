/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.function.scan.attribute.standard;

import com.dove.sample.function.scan.attribute.ScanJobAttribute;
import com.dove.sample.wrapper.rws.service.scanner.GetJobStatusResponseBody;

/**
 * 印刷情報を示す属性クラス
 * The attribute class to indicate printing info.
 * 
 * @since SmartSDK V2.12
 */
public class ScanJobPrintingInfo implements ScanJobAttribute{
	private final ScanJobState jobStatus;
	private final ScanJobStateReasons jobStatusReasons;
	
	public static ScanJobPrintingInfo getInstance(GetJobStatusResponseBody.PrintingInfo info) {
		if (info == null) {
			return null;
		}
		return new ScanJobPrintingInfo(info);
	}
	
	ScanJobPrintingInfo(GetJobStatusResponseBody.PrintingInfo info) {
		jobStatus = ScanJobState.fromString(info.getJobStatus());
		jobStatusReasons = ScanJobStateReasons.getInstance(info.getJobStatusReasons());
	}
	
	public ScanJobState getScanningState() {
		return jobStatus;
	}

	public ScanJobStateReasons getScanningStateReasons() {
		return jobStatusReasons;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof ScanJobPrintingInfo)) {
			return false;
		}
		
		ScanJobPrintingInfo other = (ScanJobPrintingInfo) obj;
		if (!isEqual(jobStatus, other.jobStatus)) {
			return false;
		}
		if (!isEqual(jobStatusReasons, other.jobStatusReasons)) {
			return false;
		}
		return true;
	}

	private boolean isEqual(Object obj1, Object obj2) {
		if (obj1 == null) {
			return (obj2 == null);
		}
		return obj1.equals(obj2);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + (jobStatus == null ? 0 : jobStatus.hashCode());
		result = 31 * result + (jobStatusReasons == null ? 0 : jobStatusReasons.hashCode());
		return result;
	}

	private volatile String cache = null;

	@Override
	public String toString() {
		if (cache == null) {
			StringBuilder sb = new StringBuilder();
			sb.append("{");
			sb.append("jobStatus:").append(jobStatus).append(", ");
			sb.append("jobStatusReasons:").append(jobStatusReasons);
			sb.append("}");
			cache = sb.toString();
		}
		return cache;
	}
	
	@Override
	public Class<?> getCategory() {
		return ScanJobPrintingInfo.class;
	}

	@Override
	public String getName() {
		return ScanJobPrintingInfo.class.getSimpleName();
	}

}
