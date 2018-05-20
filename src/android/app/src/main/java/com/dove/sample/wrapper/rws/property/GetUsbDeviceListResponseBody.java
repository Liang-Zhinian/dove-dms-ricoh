/*
 *  Copyright (C) 2017 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.property;

import java.util.List;
import java.util.Map;

import com.dove.sample.wrapper.common.ArrayElement;
import com.dove.sample.wrapper.common.Element;
import com.dove.sample.wrapper.common.ResponseBody;

/*
 * @since SmartSDK v2.40
 */
public class GetUsbDeviceListResponseBody extends Element implements ResponseBody{
	private static final String KEY_USB_DEVICE         = "USBdevice";

	public GetUsbDeviceListResponseBody(Map<String, Object> values) {
		super(values);
	}
	
	/*
	 * USBdevice (Array[Object])
	 */
	public USBDeviceList getUSBDevice() {
		List<Map<String, Object>> value = getArrayValue(KEY_USB_DEVICE);
		if (value == null) {
			return null;
		}
		return new USBDeviceList(value);
	}
	
	public static class USBDeviceList extends ArrayElement<USBDevice>{

		protected USBDeviceList(List<Map<String, Object>> list) {
			super(list);
		}

		@Override
		protected USBDevice createElement(Map<String, Object> values) {
			return new USBDevice(values);
		}
		
	}
	
	public static class USBDevice  extends Element{
		private static final String KEY_NUMBER        = "number";
		private static final String KEY_VENDOR_ID     = "vendorid";
		private static final String KEY_PRODUCT_ID    = "productid";
		private static final String KEY_DATA_TYPE     = "datatype";

		USBDevice(Map<String, Object> values) {
			super(values);
		}
		
		/*
		 * number (Int)
		 */
		public Integer getNumber() {
			return getNumberValue(KEY_NUMBER);
		}
		
		/*
		 * vendorid (String)
		 */
		public String getVendorId() {
			return getStringValue(KEY_VENDOR_ID);
		}
		
		/*
		 * productid (String)
		 */
		public String getProductId() {
			return getStringValue(KEY_PRODUCT_ID);
		}
		
		/*
		 * datatype (String)
		 */
		public String getDataType() {
			return getStringValue(KEY_DATA_TYPE);
		}
		
	}
}
