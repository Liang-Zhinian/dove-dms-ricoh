/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.service.fax;

import java.util.List;
import java.util.Map;

import com.dove.sample.wrapper.common.ArrayElement;
import com.dove.sample.wrapper.common.Element;
import com.dove.sample.wrapper.common.ResponseBody;

/*
 * @since SmartSDK V2.12
 */
public class GetAddressResponseBody extends Element implements ResponseBody {

	private static final String KEY_ADDRESS_COUNT							= "addressCount";
	private static final String KEY_SENDLATER_TIME							= "sendLaterTime";
	private static final String KEY_FROM_ADDRESS							= "fromAddress";
	private static final String KEY_FROM_ADDRESS_NAME						= "fromAddressName";
	private static final String KEY_SEP_CODE_RECEPTION						= "sepCodeReception";
	private static final String KEY_MAIL_ADDRESS_ADD_STATUS                 = "mailAddressAddStatus";
	private static final String KEY_TRANSMISSION_STANDBY_FILE_ADDRESS_LIST	= "transmissionStandbyFileAddressList";
	
	GetAddressResponseBody(Map<String, Object> values) {
		super(values);
	}

	/*
	 * addressCount (Number)
	 * @since SmartSDK V2.12
	 */
	public Integer getAddressCount() {
		return getNumberValue(KEY_ADDRESS_COUNT);
	}

	/*
	 * sendLaterTime (String)
	 * @since SmartSDK V2.12
	 */
	public String getSendLaterTime() {
		return getStringValue(KEY_SENDLATER_TIME);
	}

	/*
	 * fromAddress (String)
	 * @since SmartSDK V2.12
	 */
	public String getFromAddress() {
		return getStringValue(KEY_FROM_ADDRESS);
	}

	/*
	 * fromAddressName (String)
	 * @since SmartSDK V2.12
	 */
	public String getFromAddressName() {
		return getStringValue(KEY_FROM_ADDRESS_NAME);
	}

	/*
	 * sepCodeReception (Boolean)
	 * @since SmartSDK V2.12
	 */
	public Boolean getSepCodeReception() {
		return getBooleanValue(KEY_SEP_CODE_RECEPTION);
	}
	
	/*
	 * mailAddressAddStatus (Boolean)
	 * @since SmartSDK V2.12
	 */
	public Boolean getMailAddressAddStatus() {
		return getBooleanValue(KEY_MAIL_ADDRESS_ADD_STATUS);
	}

	/*
	 * transmissionStandbyFileAddressList (Array[Object])
	 * @since SmartSDK V2.12
	 */
	public TransmissionStandbyFileAddressList getTransmissionStandbyFileAddressList() {
        List<Map<String, Object>> value = getArrayValue(KEY_TRANSMISSION_STANDBY_FILE_ADDRESS_LIST);
        if (value == null) {
            return null;
        }
        return new TransmissionStandbyFileAddressList(value);
    }

	/*
	 * @since SmartSDK V2.12
	 */
    public static class TransmissionStandbyFileAddressList extends ArrayElement<TransmissionStandbyFileAddress> {

    	TransmissionStandbyFileAddressList(List<Map<String, Object>> list) {
            super(list);
        }

        @Override
        protected TransmissionStandbyFileAddress createElement(Map<String, Object> values) {
            return new TransmissionStandbyFileAddress(values);
        }
        
    }
    
    /*
	 * @since SmartSDK V2.12
	 */
    public static class TransmissionStandbyFileAddress extends Element {
        
        private static final String KEY_ADDRESS_ID			= "addressId";
        private static final String KEY_URI          		= "uri";
        private static final String KEY_ADDRESS_NAME    	= "addressName";
        private static final String KEY_DESTINATION_TYPE    = "destinationType";
        private static final String KEY_ENTRY_ID          	= "entryId";
        private static final String KEY_REGISTRATION_NO     = "registrationNo";
        private static final String KEY_DESTINATION_KIND    = "destinationKind";
        private static final String KEY_FAX_ADDRESS_INFO    = "faxAddressInfo";
        private static final String KEY_MAIL_ADDRESS_INFO   = "mailAddressInfo";
        private static final String KEY_FOLDER_INFO			= "folderInfo";

        TransmissionStandbyFileAddress(Map<String, Object> values) {
            super(values);
        }
        
        /*
         * addressId (String)
         * @since SmartSDK V2.12
         */
        public String getAddressId() {
            return getStringValue(KEY_ADDRESS_ID);
        }
        
        /*
         * uri (String)
         * @since SmartSDK V2.12
         */
        public String getUri() {
            return getStringValue(KEY_URI);
        }
        
        /*
         * addressName (String)
         * @since SmartSDK V2.12
         */
        public String getAddressName() {
            return getStringValue(KEY_ADDRESS_NAME);
        }
        
        /*
         * destinationType (String)
         * @since SmartSDK V2.12
         */
        public String getDestinationType() {
            return getStringValue(KEY_DESTINATION_TYPE);
        }
        
        /*
         * entryId (String)
         * @since SmartSDK V2.12
         */
        public String getEntryId() {
            return getStringValue(KEY_ENTRY_ID);
        }
        
        /*
         * registrationNo (Number)
         * @since SmartSDK V2.12
         */
        public Integer getRegistrationNo() {
            return getNumberValue(KEY_REGISTRATION_NO);
        }
        
        /*
         * entryId (String)
         * @since SmartSDK V2.12
         */
        public String getDestinationKind() {
            return getStringValue(KEY_DESTINATION_KIND);
        }
        
        /*
         * FaxAddressInfo (Object)
         * @since SmartSDK V2.12
         */
        public FaxAddressInfo getFaxAddressInfo() {
            Map<String, Object> mapValue = getObjectValue(KEY_FAX_ADDRESS_INFO);
            if (mapValue == null) {
                return null;
            }
            return new FaxAddressInfo(mapValue);
        }
        
        /*
         * mailAddressInfo (Object)
         * @since SmartSDK V2.12
         */
        public MailAddressInfo getMailAddressInfo() {
            Map<String, Object> mapValue = getObjectValue(KEY_MAIL_ADDRESS_INFO);
            if (mapValue == null) {
                return null;
            }
            return new MailAddressInfo(mapValue);
        }
        
        /*
         * FolderInfo (Object)
         * @since SmartSDK V2.12
         */
        public FolderInfo getFolderInfo() {
            Map<String, Object> mapValue = getObjectValue(KEY_FOLDER_INFO);
            if (mapValue == null) {
                return null;
            }
            return new FolderInfo(mapValue);
        }
    }
    
    /*
	 * @since SmartSDK V2.12
	 */
    public static class FaxAddressInfo extends Element {

    	private static final String KEY_FAX_NUMBER		= "faxNumber";
    	private static final String KEY_SUB_CODE		= "subCode";
    	private static final String KEY_SID_PASSWORD	= "sidPassword";
    	private static final String KEY_SEP_CODE		= "sepCode";
    	private static final String KEY_PWD_PASSWORD	= "pwdPassword";
    	private static final String KEY_SUB_ADDRESS		= "subAddress";
    	private static final String KEY_UUI				= "uui";
    	private static final String KEY_LINE			= "line";
    	
		FaxAddressInfo(Map<String, Object> values) {
			super(values);
		}
        
        /*
         * faxNumber (String)
         * @since SmartSDK V2.12
         */
        public String getFaxNumber() {
            return getStringValue(KEY_FAX_NUMBER);
        }
        
        /*
         * subCode (String)
         * @since SmartSDK V2.12
         */
        public String getSubCode() {
            return getStringValue(KEY_SUB_CODE);
        }
        
        /*
         * sidPassword (String)
         * @since SmartSDK V2.12
         */
        public String getSidPassword() {
            return getStringValue(KEY_SID_PASSWORD);
        }
        
        /*
         * sepCode (String)
         * @since SmartSDK V2.12
         */
        public String getSepCode() {
            return getStringValue(KEY_SEP_CODE);
        }
        
        /*
         * pwdPassword (String)
         * @since SmartSDK V2.12
         */
        public String getPwdPassword() {
            return getStringValue(KEY_PWD_PASSWORD);
        }
        
        /*
         * subAddress (String)
         * @since SmartSDK V2.12
         */
        public String getSubAddress() {
            return getStringValue(KEY_SUB_ADDRESS);
        }
        
        /*
         * uui (String)
         * @since SmartSDK V2.12
         */
        public String getUui() {
            return getStringValue(KEY_UUI);
        }
        
        /*
         * line (String)
         * @since SmartSDK V2.12
         */
        public String getLine() {
            return getStringValue(KEY_LINE);
        }
    	
    }
    
    /*
	 * @since SmartSDK V2.12
	 */
    public static class MailAddressInfo extends Element {

    	private static final String KEY_MAIL_ADDRESS	= "mailAddress";
    	private static final String KEY_DIRECT_SMTP		= "directSmtp";
    	private static final String KEY_CIPHER			= "cipher";
    	
    	MailAddressInfo(Map<String, Object> values) {
			super(values);
		}
        
        /*
         * mailAddress (String)
         * @since SmartSDK V2.12
         */
        public String getMailAddress() {
            return getStringValue(KEY_MAIL_ADDRESS);
        }
        
        /*
         * directSmtp (Boolean)
         * @since SmartSDK V2.12
         */
        public Boolean getDirectSmtp() {
            return getBooleanValue(KEY_DIRECT_SMTP);
        }

        /*
         * cipher (Boolean)
         * @since SmartSDK V2.12
         */
        public Boolean getCipher() {
            return getBooleanValue(KEY_CIPHER);
        }
        
    }

    /*
     * @since SmartSDK V2.12
     */
    public static class FolderInfo extends Element {
    	
    	private static final String KEY_KIND = "kind";
    	
    	FolderInfo(Map<String, Object> values) {
			super(values);
		}

    	/*
         * kind (String)
         * @since SmartSDK V2.12
         */
        public String getKind() {
            return getStringValue(KEY_KIND);
        }
    	
    }
}
