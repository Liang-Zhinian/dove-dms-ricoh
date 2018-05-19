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

public class GetRecentDestinationsResponseBody extends Element implements ResponseBody {
    
    private static final String KEY_RECENT_DESTINATIONS_LIST    = "recentDestinationsLists";

    GetRecentDestinationsResponseBody(Map<String, Object> value) {
        super(value);
    }
    
    /*
     * recentDestinationsLists (Array[Object])
     */
    public RecentDestinationArray getRecentDestinationsLists() {
        List<Map<String, Object>> value = getArrayValue(KEY_RECENT_DESTINATIONS_LIST);
        if (value == null) {
            return null;
        }
        return new RecentDestinationArray(value);
    }


    public static class RecentDestinationArray extends ArrayElement<RecentDestination> {

        RecentDestinationArray(List<Map<String, Object>> list) {
            super(list);
        }

        @Override
        protected RecentDestination createElement(Map<String, Object> values) {
            return new RecentDestination(values);
        }
        
    }

    public static class RecentDestination extends Element {

        public static final String KEY_DESTINATION_KIND     = "destinationKind";
        public static final String KEY_TIME                 = "time";
        public static final String KEY_FAX                  = "fax";
        public static final String KEY_IFAX                 = "ifax";// SmartSDK V2.12

        RecentDestination(Map<String, Object> values) {
            super(values);
        }

        /*
         * destinationKind (String)
         */
        public String getDestinationKind() {
            return getStringValue(KEY_DESTINATION_KIND);
        }

        /*
         * time (String)
         */
        public String getTime() {
            return getStringValue(KEY_TIME);
        }

        /*
         * fax (Object)
         */
        public Fax getFax() {
            Map<String, Object> mapValue = getObjectValue(KEY_FAX);
            if (mapValue == null) {
                return null;
            }
            return new Fax(mapValue);
        }

        /*
         * ifax (Object)
         */
        public Ifax getIfax() {
            Map<String, Object> mapValue = getObjectValue(KEY_IFAX);
            if (mapValue == null) {
                return null;
            }
            return new Ifax(mapValue);
        }

    }

    public static class Fax extends Element {

        public static final String KEY_FAX_NUMBER   = "faxNumber";
        public static final String KEY_LINE         = "line";

        public Fax(Map<String, Object> values) {
            super(values);
        }

        /*
         * faxNumber (String)
         */
        public String getFaxNumber() {
            return getStringValue(KEY_FAX_NUMBER);
        }

        /*
         * line (String)
         */
        public String getLine() {
            return getStringValue(KEY_LINE);
        }

    }

    /*
     * Ifax
     * @since SmartSDK V2.12
     */
    public static class Ifax extends Element {

        public static final String KEY_MAIL_ADDRESS      = "mailAddress";// SmartSDK V2.12
        public static final String KEY_DIRECT_SMTP       = "directSmtp";// SmartSDK V2.12

        public Ifax(Map<String, Object> values) {
            super(values);
        }

        /*
         * mailAddress (String)
         */
        public String getMailAddress() {
            return getStringValue(KEY_MAIL_ADDRESS);
        }

        /*
         * directSmtp (Boolean)
         */
        public Boolean getDirectSmtp() {
            return getBooleanValue(KEY_DIRECT_SMTP);
        }

    }

}
