/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.function.scan.attribute.standard;

import java.util.HashMap;
import java.util.Map;

/**
 * スキャンサービスの状態理由を示す列挙型です
 * The enum to indicate scan service state reason.
 */
public enum ScannerStateReason {

    /**
     * デバイスのカバーが開いています
     * Device cover is open.
     */
    COVER_OPEN("cover_open"),

    /**
     * デバイスで紙詰まりが発生しています
     * Paper jam on device.
     */
    MEDIA_JAM("media_jam"),

    /**
     * スキャナジョブ（読み取り）が停止しています。
     * Scanner job (scanning) is stopped.
     */
    PAUSED("paused"),

    /**
     * スキャナに関する初期設定が操作されています
     * Initial settings for scanner are in operation.
     * 
     * @since SmartSDK V2.00
     */
    OFFLINE("offline"),

    /**
     * 全印刷ジョブ停止中である
     * Print suspended
     * 
     * @since SmartSDK V2.12
     */
    PRINT_SUSPENDED("print_suspended"),
    
    /**
     * 上記以外の理由
     * Other error has been detected.
     */
    OTHER("other");


    private String stateReason;
    private ScannerStateReason(String stateReason) {
        this.stateReason = stateReason;
    }

    private static volatile Map<String, ScannerStateReason> reasons = null;

    private static Map<String, ScannerStateReason> getReasons() {
    	if (reasons == null) {
    		ScannerStateReason[] reasonArray = values();
    		Map<String, ScannerStateReason> m = new HashMap<String, ScannerStateReason>(reasonArray.length);
    		for (ScannerStateReason reason : reasonArray) {
    			m.put(reason.stateReason, reason);
    		}
    		reasons = m;
    	}
    	return reasons;
    }

    public static ScannerStateReason fromString(String value) {
    	return getReasons().get(value);
    }

    public String toString(){
             return this.stateReason;
    }

}
