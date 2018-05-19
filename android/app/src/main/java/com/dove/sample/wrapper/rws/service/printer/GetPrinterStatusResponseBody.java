/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.service.printer;

import java.util.List;
import java.util.Map;

import com.dove.sample.wrapper.common.ArrayElement;
import com.dove.sample.wrapper.common.Element;
import com.dove.sample.wrapper.common.ResponseBody;

public class GetPrinterStatusResponseBody extends Element implements ResponseBody {

	private static final String KEY_PRINTER_STATUS			= "printerStatus";
	private static final String KEY_PRINTER_STATUS_REASONS	= "printerStatusReasons";
	private static final String KEY_OCCURED_ERROR_LEVEL  	= "occuredErrorLevel"; // SmartSDK V2.12

	public GetPrinterStatusResponseBody(Map<String, Object> values) {
		super(values);
	}
	
	/*
	 * printerStatus (String)
	 */
	public String getPrinterStatus() {
		return getStringValue(KEY_PRINTER_STATUS);
	}
	
	/*
	 * printerStatusReasons (Array[Object])
	 */
	public PrinterStatusReasonsArray getPrinterStatusReasons() {
		List<Map<String, Object>> value = getArrayValue(KEY_PRINTER_STATUS_REASONS);
		if (value == null) {
			return null;
		}
		return new PrinterStatusReasonsArray(value);
	}

	/*
	 * occuredErrorLevel (String)
	 * @since SmartSDK V2.12
	 */
	public String getOccuredErrorLevel() {
		return getStringValue(KEY_OCCURED_ERROR_LEVEL);
	}

	public static class PrinterStatusReasonsArray extends ArrayElement<PrinterStatusReasons> {

		PrinterStatusReasonsArray(List<Map<String, Object>> list) {
			super(list);
		}

		@Override
		protected PrinterStatusReasons createElement(Map<String, Object> values) {
			return new PrinterStatusReasons(values);
		}

	}

	public static class PrinterStatusReasons extends Element {

		private static final String KEY_PRINTER_STATUS_REASON	= "printerStatusReason";
		private static final String KEY_SEVERITY				= "severity";

		PrinterStatusReasons(Map<String, Object> values) {
			super(values);
		}

		/*
		 * printerStatusReason (String)
		 */
		public String getPrinterStatusReason() {
			return getStringValue(KEY_PRINTER_STATUS_REASON);
		}

		/*
		 * severity (String)
		 */
		public String getSeverity() {
			return getStringValue(KEY_SEVERITY);
		}

	}

}
