/*
 *  Copyright (C) 2017 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.property;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dove.sample.wrapper.common.ArrayElement;
import com.dove.sample.wrapper.common.RequestBody;
import com.dove.sample.wrapper.common.Utils;
import com.dove.sample.wrapper.common.WritableElement;
import com.dove.sample.wrapper.json.EncodedException;
import com.dove.sample.wrapper.json.JsonUtils;
import com.dove.sample.wrapper.log.Logger;

/*
 * @since SmartSDK v2.40
 */
public class UpdateUsbDeviceListRequestBody extends WritableElement implements RequestBody{
	
	private final static String PREFIX = "property:UpdateUsbDevReq:";
		
	private static final String CONTENT_TYPE_JSON               = "application/json; charset=utf-8";
	private static final String KEY_USB_DEVICE                  = "USBdevice";

	public UpdateUsbDeviceListRequestBody() {
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
	 * USBdevice (Array[Object])
	 */
	public USBDeviceArray getUSBDevice() {
		List<Map<String, Object>> value = getArrayValue(KEY_USB_DEVICE);
		if (value == null) {
			value = Utils.createElementList();
			setArrayValue(KEY_USB_DEVICE, value);
		}
		return new USBDeviceArray(value);
	}
	public USBDeviceArray removeUSBDevice() {
		List<Map<String, Object>> value = removeArrayValue(KEY_USB_DEVICE);
		if (value == null) {
			return null;
		}
		return new USBDeviceArray(value);
	}
	
	public static class USBDeviceArray extends ArrayElement<USBDevice> {

		USBDeviceArray(List<Map<String, Object>> list) {
			super(list);
		}
		
		public boolean add(USBDevice value) {
			if (value == null) {
				throw new NullPointerException("value must not be null.");
			}
			return list.add(value.cloneValues());
		}
		
		public USBDevice remove(int index) {
			Map<String, Object> value = list.remove(index);
			if (value == null) {
				return null;
			}
			return createElement(value);
		}
		
		public void clear() {
			list.clear();
		}

		@Override
		protected USBDevice createElement(Map<String, Object> values) {
			return new USBDevice(values);
		}
		
	}
	
	public static class USBDevice extends WritableElement {
		private static final String KEY_NUMBER       = "number";
		private static final String KEY_VENDOR_ID    = "vendorid";
		private static final String KEY_PRODUCT_ID   = "productid";

		USBDevice(Map<String, Object> values) {
			super(values);
		}
		
		/*
		 * number (Int)
		 */
		public Integer getNumber() {
			return getNumberValue(KEY_NUMBER);
		}
		public void setNumber(Integer value) {
			setNumberValue(KEY_NUMBER, value);
		}
		public Integer removeNumber() {
			return removeNumberValue(KEY_NUMBER);
		}
		
		/*
		 * vendorid (String)
		 */
		public String getVendorId() {
			return getStringValue(KEY_VENDOR_ID);
		}
		public void setVendorId(String value) {
			setStringValue(KEY_VENDOR_ID, value);
		}
		public String removeVendorId(){
			return removeStringValue(KEY_VENDOR_ID);
		}
		
		/*
		 * productid (String)
		 */
		public String getProductId() {
			return getStringValue(KEY_PRODUCT_ID);
		}
		public void setProductId(String value) {
			setStringValue(KEY_PRODUCT_ID, value);
		}
		public String removeProductId() {
			return removeStringValue(KEY_PRODUCT_ID);
		}
		
	}

}
