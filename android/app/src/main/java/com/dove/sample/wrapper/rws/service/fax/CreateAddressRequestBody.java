/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.service.fax;

import com.dove.sample.wrapper.common.RequestBody;
import com.dove.sample.wrapper.common.Utils;
import com.dove.sample.wrapper.common.WritableElement;
import com.dove.sample.wrapper.json.EncodedException;
import com.dove.sample.wrapper.json.JsonUtils;
import com.dove.sample.wrapper.log.Logger;

import java.util.HashMap;
import java.util.Map;

/*
 * @since SmartSDK V2.12
 */
public class CreateAddressRequestBody extends WritableElement implements RequestBody {
	
	private static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";
	
    private static final String KEY_ADDRESS_NAME    	= "addressName";
    private static final String KEY_DESTINATION_TYPE    = "destinationType";
    private static final String KEY_ENTRY_ID          	= "entryId";
    private static final String KEY_REGISTRATION_NO     = "registrationNo";
    private static final String KEY_DESTINATION_KIND    = "destinationKind";
    private static final String KEY_FAX_ADDRESS_INFO    = "faxAddressInfo";
    private static final String KEY_MAIL_ADDRESS_INFO   = "mailAddressInfo";
	
    /**
     * Define the prefix for log information with abbreviation package and class name
     */
    private final static String PREFIX = "fax:CreateAddressReq:";
    
	public CreateAddressRequestBody() {
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
	 * addressName (String)
	 * @since SmartSDK V2.12
	 */
	public String getAddressName() {
		return getStringValue(KEY_ADDRESS_NAME);
	}
	public void setAddressName(String value) {
		setStringValue(KEY_ADDRESS_NAME, value);
	}
	public String removeAddressName() {
		return removeStringValue(KEY_ADDRESS_NAME);
	}
	
	/*
	 * destinationType (String)
	 * @since SmartSDK V2.12
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
		return removeStringValue(KEY_ENTRY_ID);
	}
	
	/*
	 * registrationNo (Number)
	 * @since SmartSDK V2.12
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
	 * destinationKind (String)
	 * @since SmartSDK V2.12
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
     * faxAddressInfo (Object)
     * @since SmartSDK V2.12
     */
    public FaxAddressInfo getFaxAddressInfo() {
        Map<String, Object> mapValue = getObjectValue(KEY_FAX_ADDRESS_INFO);
        if (mapValue == null) {
            mapValue = Utils.createElementMap();
            setObjectValue(KEY_FAX_ADDRESS_INFO, mapValue);
        }
        return new FaxAddressInfo(mapValue);
    }
    public FaxAddressInfo removeFaxAddressInfo() {
        Map<String, Object> mapValue = removeObjectValue(KEY_FAX_ADDRESS_INFO);
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
            mapValue = Utils.createElementMap();
            setObjectValue(KEY_MAIL_ADDRESS_INFO, mapValue);
        }
        return new MailAddressInfo(mapValue);
    }
    public MailAddressInfo removeMailAddressInfo() {
        Map<String, Object> mapValue = removeObjectValue(KEY_MAIL_ADDRESS_INFO);
        if (mapValue == null) {
            return null;
        }
        return new MailAddressInfo(mapValue);
    }
	
	/*
	 * @since SmartSDK V2.12
	 */
	public static class FaxAddressInfo extends WritableElement {
		
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
		public void setFaxNumber(String value) {
			setStringValue(KEY_FAX_NUMBER, value);
		}
		public String removeFaxNumber() {
			return removeStringValue(KEY_FAX_NUMBER);
		}
		
		/*
		 * subCode (String)
		 * @since SmartSDK V2.12
		 */
		public String getSubCode() {
			return getStringValue(KEY_SUB_CODE);
		}		
		public void setSubCode(String value) {
			setStringValue(KEY_SUB_CODE, value);
		}
		public String removeSubCode() {
			return removeStringValue(KEY_SUB_CODE);
		}
		
		/*
		 * sidPassword (String)
		 * @since SmartSDK V2.12
		 */
		public String getSidPassword() {
			return getStringValue(KEY_SID_PASSWORD);
		}		
		public void setSidPassword(String value) {
			setStringValue(KEY_SID_PASSWORD, value);
		}
		public String removeSidPassword() {
			return removeStringValue(KEY_SID_PASSWORD);
		}
		
		/*
		 * sepCode (String)
		 * @since SmartSDK V2.12
		 */
		public String getSepCode() {
			return getStringValue(KEY_SEP_CODE);
		}		
		public void setSepCode(String value) {
			setStringValue(KEY_SEP_CODE, value);
		}
		public String removeSepCode() {
			return removeStringValue(KEY_SEP_CODE);
		}	
		
		/*
		 * pwdPassword (String)
		 * @since SmartSDK V2.12
		 */
		public String getPwdPassword() {
			return getStringValue(KEY_PWD_PASSWORD);
		}		
		public void setPwdPassword(String value) {
			setStringValue(KEY_PWD_PASSWORD, value);
		}
		public String removePwdPassword() {
			return removeStringValue(KEY_PWD_PASSWORD);
		}	
		
		/*
		 * subAddress (String)
		 * @since SmartSDK V2.12
		 */
		public String getSubAddress() {
			return getStringValue(KEY_SUB_ADDRESS);
		}		
		public void setSubAddress(String value) {
			setStringValue(KEY_SUB_ADDRESS, value);
		}
		public String removeSubAddress() {
			return removeStringValue(KEY_SUB_ADDRESS);
		}	
		
		/*
		 * uui (String)
		 * @since SmartSDK V2.12
		 */
		public String getUui() {
			return getStringValue(KEY_UUI);
		}		
		public void setUui(String value) {
			setStringValue(KEY_UUI, value);
		}
		public String removeUui() {
			return removeStringValue(KEY_UUI);
		}	
		
		/*
		 * line (String)
		 * @since SmartSDK V2.12
		 */
		public String getLine() {
			return getStringValue(KEY_LINE);
		}		
		public void setLine(String value) {
			setStringValue(KEY_LINE, value);
		}
		public String removeLine() {
			return removeStringValue(KEY_LINE);
		}
		
	}
    
    /*
	 * @since SmartSDK V2.12
	 */
    public static class MailAddressInfo extends WritableElement {

    	private static final String KEY_MAIL_ADDRESS	= "mailAddress";
    	private static final String KEY_DIRECT_SMTP		= "directSmtp";
    	
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
		public void setMailAddress(String value) {
			setStringValue(KEY_MAIL_ADDRESS, value);
		}
		public String removeMailAddress() {
			return removeStringValue(KEY_MAIL_ADDRESS);
		}
        
        /*
         * directSmtp (Boolean)
         * @since SmartSDK V2.12
         */
        public Boolean getDirectSmtp() {
            return getBooleanValue(KEY_DIRECT_SMTP);
        }		
		public void setDirectSmtp(Boolean value) {
			setBooleanValue(KEY_DIRECT_SMTP, value);
		}
		public Boolean removeDirectSmtp() {
			return removeBooleanValue(KEY_DIRECT_SMTP);
		}
    	
    }
	
}
