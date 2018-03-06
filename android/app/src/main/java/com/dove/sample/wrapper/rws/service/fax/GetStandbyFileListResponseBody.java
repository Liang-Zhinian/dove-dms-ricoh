/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.service.fax;

import java.util.List;
import java.util.Map;

import com.dove.sample.wrapper.common.ArrayElement;
import com.dove.sample.wrapper.common.Element;
import com.dove.sample.wrapper.common.ResponseBody;

public class GetStandbyFileListResponseBody extends Element implements ResponseBody  {
    
    private static final String KEY_TRANSMISSION_STANDBY_FILE_LISTS = "transmissionStandbyFileLists";

    GetStandbyFileListResponseBody(Map<String, Object> value) {
        super(value);
    }

    /*
     * transmissionStandbyFileLists (Array[Object])
     */
    public StandbyFileArray getTransmissionStandbyFileLists() {
        List<Map<String, Object>> value = getArrayValue(KEY_TRANSMISSION_STANDBY_FILE_LISTS);
        if (value == null) {
            return null;
        }
        return new StandbyFileArray(value);
    }


    public static class StandbyFileArray extends ArrayElement<StandbyFile> {

        StandbyFileArray(List<Map<String, Object>> list) {
            super(list);
        }

        @Override
        protected StandbyFile createElement(Map<String, Object> values) {
            return new StandbyFile(values);
        }
        
    }
    
    public static class StandbyFile extends Element {

        public static final String KEY_STANDBY_DOC_NUM      			= "standbyDocNum";
        public static final String KEY_URI                  			= "uri";
        public static final String KEY_SCAN_DATE            			= "scanDate";
        public static final String KEY_STANDBY_DOC_KIND    		 	    = "standbyDocKind";
        public static final String KEY_DESTINATION_KIND     			= "destinationKind";
        public static final String KEY_DESTINATION_KIND_DETAIL     		= "destinationKindDetail"; // SmartSDK V2.12
        public static final String KEY_ADDRESS_NAME         			= "addressName";
        public static final String KEY_ADDRESS_NAME_EXCLUDE_EXTENSION	= "addressNameExcludeExtension"; // SmartSDK V2.12
        public static final String KEY_ADDRESS_COUNT        			= "addressCount";
        public static final String KEY_PAGE_COUNT           			= "pageCount";
        public static final String KEY_DOCUMENT_NUMBER      			= "documentNumber";
        public static final String KEY_TX_STATE             			= "txState";
        public static final String KEY_SEND_LATER_TIME      			= "sendLaterTime";

        StandbyFile(Map<String, Object> values) {
            super(values);
        }

        /*
         * standbyDocNum (String)
         */
        public String getStandbyDocNum() {
            return getStringValue(KEY_STANDBY_DOC_NUM);
        }

        /*
         * uri (String)
         */
        public String getUri() {
            return getStringValue(KEY_URI);
        }

        /*
         * scanDate (String)
         */
        public String getScanDate() {
            return getStringValue(KEY_SCAN_DATE);
        }

        /*
         * standbyDocKind (String)
         */
        public String getStandbyDocKind() {
            return getStringValue(KEY_STANDBY_DOC_KIND);
        }

        /*
         * destinationKind (String)
         */
        public String getDestinationKind() {
            return getStringValue(KEY_DESTINATION_KIND);
        }

        /*
         * destinationKindDetail (String)
         * @since SmartSDK V2.12
         */
        public String getDestinationKindDetail() {
            return getStringValue(KEY_DESTINATION_KIND_DETAIL);
        }

        /*
         * addressName (String)
         */
        public String getAddressName() {
            return getStringValue(KEY_ADDRESS_NAME);
        }

        /*
         * addressNameExcludeExtension (String)
         * @since SmartSDK V2.12
         */
        public String getAddressNameExcludeExtension() {
            return getStringValue(KEY_ADDRESS_NAME_EXCLUDE_EXTENSION);
        }

        /*
         * addressCount (Number)
         */
        public Integer getAddressCount() {
            return getNumberValue(KEY_ADDRESS_COUNT);
        }

        /*
         * pageCount (Number)
         */
        public Integer getPageCount() {
            return getNumberValue(KEY_PAGE_COUNT);
        }

        /*
         * documentNumber (String)
         */
        public String getDocumentNumber() {
            return getStringValue(KEY_DOCUMENT_NUMBER);
        }

        /*
         * txState (String)
         */
        public String getTxState() {
            return getStringValue(KEY_TX_STATE);
        }

        /*
         * sendLaterTime (String)
         */
        public String getSendLaterTime() {
            return getStringValue(KEY_SEND_LATER_TIME);
        }

    }

}
