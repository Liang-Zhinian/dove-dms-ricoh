/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.function.scan.attribute.standard;

import com.dove.sample.function.common.Conversions;
import com.dove.sample.function.scan.attribute.ScanJobAttribute;
import com.dove.sample.wrapper.rws.service.scanner.GetJobStatusResponseBody;

public final class ScanJobScanningInfo implements ScanJobAttribute {
	
	private final ScanJobState scanningState;
	private final ScanJobStateReasons scanningStateReasons;
	private final int scannedCount;
	private final int resetOriginalCount;
	private final int remainingTimeOfWaitingNextOriginal;
	private final Boolean sadfAutoMode; //SmartSDK V2.12
	private final int validPageCount; //SmartSDK V2.12
	private final String scannedThumbnailUri;
	
	public static ScanJobScanningInfo getInstance(GetJobStatusResponseBody.ScanningInfo info) {
		if (info == null) {
			return null;
		}
		return new ScanJobScanningInfo(info);
	}

	ScanJobScanningInfo(GetJobStatusResponseBody.ScanningInfo info) {
		scanningState = ScanJobState.fromString(info.getJobStatus());
		scanningStateReasons = ScanJobStateReasons.getInstance(info.getJobStatusReasons());
		scannedCount = Conversions.getIntValue(info.getScannedCount(), 0);
		resetOriginalCount = Conversions.getIntValue(info.getResetOriginalCount(), 0);
		remainingTimeOfWaitingNextOriginal = Conversions.getIntValue(info.getRemainingTimeOfWaitingNextOriginal(), 0);
		sadfAutoMode = Conversions.getBooleanValue(info.getSadfAutoMode(),false);
		validPageCount = Conversions.getIntValue(info.getValidPageCount(), 0);
		scannedThumbnailUri = info.getScannedThumbnailUri();
	}
	
	public ScanJobState getScanningState() {
		return scanningState;
	}

	public ScanJobStateReasons getScanningStateReasons() {
		return scanningStateReasons;
	}

	public int getScannedCount() {
		return scannedCount;
	}

	public int getResetOriginalCount() {
		return resetOriginalCount;
	}

    public int getRemainingTimeOfWaitingNextOriginal() {
        return remainingTimeOfWaitingNextOriginal;
    }

    /**
     * 取得SADFオートモードの状態
     * 
     * @since SmartSDK V2.12
     */
    public Boolean getSadfAutoMode(){
    	return sadfAutoMode;
    }
    
    /**
     * 取得有効な原稿面数
     * 
     * @since SmartSDK V2.12
     */
    public int getValidPageCount(){
    	return validPageCount;
    }
    
	public String getScannedThumbnailUri() {
		return scannedThumbnailUri;
	}

	/**
	 * @since SmartSDK V2.12
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof ScanJobScanningInfo)) {
			return false;
		}
		ScanJobScanningInfo other = (ScanJobScanningInfo) obj;
		if (!isEqual(scanningState, other.scanningState)) {
			return false;
		}
		if (!isEqual(scanningStateReasons, other.scanningStateReasons)) {
			return false;
		}
		if (scannedCount != other.scannedCount) {
			return false;
		}
		if (resetOriginalCount != other.resetOriginalCount) {
			return false;
		}
        if (remainingTimeOfWaitingNextOriginal != other.remainingTimeOfWaitingNextOriginal) {
            return false;
        }
        if (!isEqual(sadfAutoMode, other.sadfAutoMode)) {
        	return false;
        }
        if (validPageCount != other.validPageCount){
        	return false;
        }
		if (!isEqual(scannedThumbnailUri, other.scannedThumbnailUri)) {
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
		result = 31 * result + (scanningState == null ? 0 : scanningState.hashCode());
		result = 31 * result + (scanningStateReasons == null ? 0 : scanningStateReasons.hashCode());
		result = 31 * result + scannedCount;
		result = 31 * result + resetOriginalCount;
        result = 31 * result + remainingTimeOfWaitingNextOriginal;
        result = 31 * result + (sadfAutoMode == null ? 0: sadfAutoMode.hashCode());
        result = 31 * result + validPageCount;
		result = 31 * result + (scannedThumbnailUri == null ? 0 : scannedThumbnailUri.hashCode());
		return result;
	}

	private volatile String cache = null;

	@Override
	public String toString() {
		if (cache == null) {
			StringBuilder sb = new StringBuilder();
			sb.append("{");
			sb.append("scanningState:").append(scanningState).append(", ");
			sb.append("scanningStateReasons:").append(scanningStateReasons).append(", ");
			sb.append("scannedCount:").append(scannedCount).append(", ");
			sb.append("resetOriginalCount:").append(resetOriginalCount).append(", ");
            sb.append("remainingTimeOfWaitingNextOriginal:").append(remainingTimeOfWaitingNextOriginal).append(", ");
			sb.append("sadfAutoMode:").append(sadfAutoMode).append(", ");
			sb.append("validPageCount:").append(validPageCount).append(", ");
            sb.append("scannedThumbnailUri:").append(scannedThumbnailUri);
			sb.append("}");
			cache = sb.toString();
		}
		return cache;
	}

	@Override
	public Class<?> getCategory() {
		return ScanJobScanningInfo.class;
	}

	@Override
	public String getName() {
		return ScanJobScanningInfo.class.getSimpleName();
	}

}
