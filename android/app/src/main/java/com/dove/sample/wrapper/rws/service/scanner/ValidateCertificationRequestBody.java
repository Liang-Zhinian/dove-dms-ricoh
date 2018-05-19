/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.service.scanner;

import com.dove.sample.wrapper.common.ArrayElement;
import com.dove.sample.wrapper.common.RequestBody;
import com.dove.sample.wrapper.common.Utils;
import com.dove.sample.wrapper.common.WritableElement;
import com.dove.sample.wrapper.json.EncodedException;
import com.dove.sample.wrapper.json.JsonUtils;
import com.dove.sample.wrapper.log.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * @since SmartSDK V2.12
 */
public class ValidateCertificationRequestBody extends WritableElement implements RequestBody {
    
    /**
     * Define the prefix for log information with abbreviation package and class name
     */
    private final static String PREFIX = "scanner:ValidCertReqBody:";
	
	private static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";
	
	private static final String KEY_CERTIFICATION_KIND	            = "certificationKind";
	private static final String KEY_CIPHER		                    = "cipher";
	private static final String KEY_SIGNATURE		                = "signature";
	private static final String KEY_ADDRESSBOOK_DESTINATION_SETTING	= "addressbookDestinationSetting";
	
	public ValidateCertificationRequestBody() {
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
	 * certificationKind (String)
	 * @since SmartSDK V2.12
	 */
	public String getCertificationKind() {
		return getStringValue(KEY_CERTIFICATION_KIND);
	}
	public void setCertificationKind(String value) {
		setStringValue(KEY_CERTIFICATION_KIND, value);
	}
	public String removeCertificationKind() {
		return removeStringValue(KEY_CERTIFICATION_KIND);
	}
	
	/*
	 * cipher (Boolean)
	 * @since SmartSDK V2.12
	 */
	public Boolean getCipher() {
		return getBooleanValue(KEY_CIPHER);
	}
	public void setCipher(Boolean value) {
		setBooleanValue(KEY_CIPHER, value);
	}
	public Boolean removeCipher() {
		return removeBooleanValue(KEY_CIPHER);
	}
	
	/*
	 * signature (Boolean)
	 * @since SmartSDK V2.12
	 */
	public Boolean getSignature() {
		return getBooleanValue(KEY_SIGNATURE);
	}
	public void setSignature(Boolean value) {
		setBooleanValue(KEY_SIGNATURE, value);
	}
	public Boolean removeSignature() {
		return removeBooleanValue(KEY_SIGNATURE);
	}
	
	/*
	 * addressbookDestinationSetting (Object)
	 * @since SmartSDK V2.12
	 */
	public AddressbookDestinationSettingArray getAddressbookDestinationSetting() {
		List<Map<String, Object>> value = getArrayValue(KEY_ADDRESSBOOK_DESTINATION_SETTING);
		if (value == null) {
			value = Utils.createElementList();
			setArrayValue(KEY_ADDRESSBOOK_DESTINATION_SETTING, value);
		}
		return new AddressbookDestinationSettingArray(value);
	}	
	public AddressbookDestinationSettingArray removeAddressbookDestinationSetting() {
		List<Map<String, Object>> value = removeArrayValue(KEY_ADDRESSBOOK_DESTINATION_SETTING);
		if (value == null) {
			return null;
		}
		return new AddressbookDestinationSettingArray(value);
	}	
	
	/*
	 * @since SmartSDK V2.12
	 */
	public static class AddressbookDestinationSettingArray extends ArrayElement<AddressbookDestinationSetting> {

		AddressbookDestinationSettingArray(List<Map<String, Object>> list) {
			super(list);
		}
		
		/*
		 * @since SmartSDK V2.12
		 */
		public boolean add(AddressbookDestinationSetting value) {
			if (value == null) {
				throw new NullPointerException("value must not be null.");
			}
			return list.add(value.cloneValues());
		}
		
		/*
		 * @since SmartSDK V2.12
		 */
		public AddressbookDestinationSetting remove(int index) {
			Map<String, Object> value = list.remove(index);
			if (value == null) {
				return null;
			}
			return createElement(value);
		}
		
		/*
		 * @since SmartSDK V2.12
		 */
		public void clear() {
			list.clear();
		}

		@Override
		protected AddressbookDestinationSetting createElement(
				Map<String, Object> values) {
			return new AddressbookDestinationSetting(values);
		}
	}
	
	/*
	 * @since SmartSDK V2.12
	 */
	public static class AddressbookDestinationSetting extends WritableElement {
		
		private static final String KEY_DESTINATION_KIND	= "destinationKind";		
		private static final String KEY_ENTRY_ID			= "entryId";
		
		AddressbookDestinationSetting(Map<String, Object> values) {
			super(values);
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
		
	}
	
}
