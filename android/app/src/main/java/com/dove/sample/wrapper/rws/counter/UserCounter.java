/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */

package com.dove.sample.wrapper.rws.counter;

import java.util.List;
import java.util.Map;

import com.dove.sample.wrapper.common.ArrayElement;
import com.dove.sample.wrapper.common.Element;

/*
 * @since SmartSDK V2.12
 */
public class UserCounter extends Element {
	
	private static final String KEY_COPY_COUNT		= "copyCount";
	private static final String KEY_PRINTER_COUNT	= "printerCount";
	private static final String KEY_FAX_COUNT		= "faxCount";
	private static final String KEY_SCAN_COUNT		= "scanCount";
	private static final String KEY_PROCESS_COUNT	= "processCount";
	private static final String KEY_TOTAL_COUNT		= "totalCount";
	private static final String KEY_LIMIT_COUNT		= "limitCount";// SmartSDK V2.12
	
	UserCounter(Map<String, Object> values) {
		super(values);
	}
	
	/*
	 * copyCount (Object)
	 */
	public CopyCount getCopyCount() {
		Map<String, Object> value = getObjectValue(KEY_COPY_COUNT);
		if (value == null) {
			return null;
		}
		return new CopyCount(value);
	}
	
	/*
	 * printerCount (Object)
	 */
	public PrinterCount getPrinterCount() {
		Map<String, Object> value = getObjectValue(KEY_PRINTER_COUNT);
		if (value == null) {
			return null;
		}
		return new PrinterCount(value);
	}
	
	/*
	 * faxCount (Object)		
	 */
	public FaxCount getFaxCount() {
		Map<String, Object> value = getObjectValue(KEY_FAX_COUNT);
		if (value == null) {
			return null;
		}
		return new FaxCount(value);
	}
	
	/*
	 * scanCount (Object)
	 */
	public ScanCount getScanCount() {
		Map<String, Object> value = getObjectValue(KEY_SCAN_COUNT);
		if (value == null) {
			return null;
		}
		return new ScanCount(value);
	}
	
	/*
	 * processCount (Object)	
	 */
	public ProcessCount getProcessCount() {
		Map<String, Object> value = getObjectValue(KEY_PROCESS_COUNT);
		if (value == null) {
			return null;
		}
		return new ProcessCount(value);
	}
	
	/*
	 * totalCount (Object)
	 */
	public TotalCount getTotalCount() {
		Map<String, Object> value = getObjectValue(KEY_TOTAL_COUNT);
		if (value == null) {
			return null;
		}
		return new TotalCount(value);
	}
	
	/*
	 * limitCount (Object)
	 * @since SmartSDK V2.12
	 */
	public LimitCount getLimitCount() {
		Map<String, Object> value = getObjectValue(KEY_LIMIT_COUNT);
		if (value == null) {
			return null;
		}
		return new LimitCount(value);
	}
	
	public static class CopyCount extends Element {
		
		private static final String KEY_BLACK_LARGE			= "blackLarge";
		private static final String KEY_BLACK_SMALL			= "blackSmall";
		private static final String KEY_MONO_COLOR_LARGE	= "monoColorLarge";
		private static final String KEY_MONO_COLOR_SMALL	= "monoColorSmall";
		private static final String KEY_TWIN_COLOR_LARGE	= "twinColorLarge";
		private static final String KEY_TWIN_COLOR_SMALL	= "twinColorSmall";
		private static final String KEY_FULL_COLOR_LARGE	= "fullColorLarge";
		private static final String KEY_FULL_COLOR_SMALL	= "fullColorSmall";
		private static final String KEY_DUPLEX_SHEET    	= "duplexSheet"; // SmartSDK V2.00
		private static final String KEY_COMBINE_PAGE    	= "combinePage"; // SmartSDK V2.00
		
		CopyCount(Map<String, Object> values) {
			super(values);
		}
		
		/*
		 * blackLarge (Number)
		 */
		public Integer getBlackLarge() {
			return getNumberValue(KEY_BLACK_LARGE);
		}
		
		/*
		 * blackSmall (Number)
		 */
		public Integer getBlackSmall() {
			return getNumberValue(KEY_BLACK_SMALL);
		}
		
		/*
		 * monoColorLarge (Number)
		 */
		public Integer getMonoColorLarge() {
			return getNumberValue(KEY_MONO_COLOR_LARGE);
		}
		
		/*
		 * monoColorSmall (Number)
		 */
		public Integer getMonoColorSmall() {
			return getNumberValue(KEY_MONO_COLOR_SMALL);
		}
		
		/*
		 * twinColorLarge (Number)
		 */
		public Integer getTwinColorLarge() {
			return getNumberValue(KEY_TWIN_COLOR_LARGE);
		}
		
		/*
		 * twinColorSmall (Number)
		 */
		public Integer getTwinColorSmall() {
			return getNumberValue(KEY_TWIN_COLOR_SMALL);
		}
		
		/*
		 * fullColorLarge (Number)
		 */
		public Integer getFullColorLarge() {
			return getNumberValue(KEY_FULL_COLOR_LARGE);
		}
		
		/*
		 * fullColorSmall (Number)
		 */
		public Integer getFullColorSmall() {
			return getNumberValue(KEY_FULL_COLOR_SMALL);
		}
	
		/*
		 * duplexSheet (Number)
		 * @since SmartSDK V2.00
		 */
		public Integer getDuplexSheet() {
			return getNumberValue(KEY_DUPLEX_SHEET);
		}	
		
		/*
		 * combinePage (Number)
		 * @since SmartSDK V2.00
		 */
		public Integer getCombinePage() {
			return getNumberValue(KEY_COMBINE_PAGE);
		}	
		
	}
	
	
	public static class PrinterCount extends Element {
		
		private static final String KEY_BLACK_LARGE			= "blackLarge";
		private static final String KEY_BLACK_SMALL			= "blackSmall";
		private static final String KEY_MONO_COLOR_LARGE	= "monoColorLarge";
		private static final String KEY_MONO_COLOR_SMALL	= "monoColorSmall";
		private static final String KEY_TWIN_COLOR_LARGE	= "twinColorLarge";
		private static final String KEY_TWIN_COLOR_SMALL	= "twinColorSmall";
		private static final String KEY_FULL_COLOR_LARGE	= "fullColorLarge";
		private static final String KEY_FULL_COLOR_SMALL	= "fullColorSmall";
		private static final String KEY_DUPLEX_SHEET    	= "duplexSheet"; // SmartSDK V2.00
		private static final String KEY_COMBINE_PAGE    	= "combinePage"; // SmartSDK V2.00
		
		PrinterCount(Map<String, Object> values) {
			super(values);
		}
		
		/*
		 * blackLarge (Number)
		 */
		public Integer getBlackLarge() {
			return getNumberValue(KEY_BLACK_LARGE);
		}
		
		/*
		 * blackSmall (Number)
		 */
		public Integer getBlackSmall() {
			return getNumberValue(KEY_BLACK_SMALL);
		}
		
		/*
		 * monoColorLarge (Number)
		 */
		public Integer getMonoColorLarge() {
			return getNumberValue(KEY_MONO_COLOR_LARGE);
		}
		
		/*
		 * monoColorSmall (Number)
		 */
		public Integer getMonoColorSmall() {
			return getNumberValue(KEY_MONO_COLOR_SMALL);
		}
		
		/*
		 * twinColorLarge (Number)
		 */
		public Integer getTwinColorLarge() {
			return getNumberValue(KEY_TWIN_COLOR_LARGE);
		}
		
		/*
		 * twinColorSmall (Number)
		 */
		public Integer getTwinColorSmall() {
			return getNumberValue(KEY_TWIN_COLOR_SMALL);
		}
		
		/*
		 * fullColorLarge (Number)
		 */
		public Integer getFullColorLarge() {
			return getNumberValue(KEY_FULL_COLOR_LARGE);
		}
		
		/*
		 * fullColorSmall (Number)
		 */
		public Integer getFullColorSmall() {
			return getNumberValue(KEY_FULL_COLOR_SMALL);
		}
		
		/*
		 * duplexSheet (Number)
		 * @since SmartSDK V2.00
		 */
		public Integer getDuplexSheet() {
			return getNumberValue(KEY_DUPLEX_SHEET);
		}	
		
		/*
		 * combinePage (Number)
		 * @since SmartSDK V2.00
		 */
		public Integer getCombinePage() {
			return getNumberValue(KEY_COMBINE_PAGE);
		}
		
	}
	
	
	public static class FaxCount extends Element {
		
		private static final String KEY_BLACK_LARGE			= "blackLarge";
		private static final String KEY_BLACK_SMALL			= "blackSmall";
		private static final String KEY_MONO_COLOR_LARGE	= "monoColorLarge";
		private static final String KEY_MONO_COLOR_SMALL	= "monoColorSmall";
		private static final String KEY_PAGE				= "page";
		private static final String KEY_CHARGE				= "charge";
		
		FaxCount(Map<String, Object> values) {
			super(values);
		}
		
		/*
		 * blackLarge (Number)
		 */
		public Integer getBlackLarge() {
			return getNumberValue(KEY_BLACK_LARGE);
		}
		
		/*
		 * blackSmall (Number)
		 */
		public Integer getBlackSmall() {
			return getNumberValue(KEY_BLACK_SMALL);
		}
		
		/*
		 * monoColorLarge (Number)
		 */
		public Integer getMonoColorLarge() {
			return getNumberValue(KEY_MONO_COLOR_LARGE);
		}
		
		/*
		 * monoColorSmall (Number)
		 */
		public Integer getMonoColorSmall() {
			return getNumberValue(KEY_MONO_COLOR_SMALL);
		}
		
		/*
		 * page (Number)
		 */
		public Integer getPage() {
			return getNumberValue(KEY_PAGE);
		}
		
		/*
		 * charge (Number)
		 */
		public Integer getCharge() {
			return getNumberValue(KEY_CHARGE);
		}
		
	}
	
	public static class ScanCount extends Element {
		
		private static final String KEY_BLACK_LARGE			= "blackLarge";
		private static final String KEY_BLACK_SMALL			= "blackSmall";
		private static final String KEY_FULL_COLOR_LARGE	= "fullColorLarge";
		private static final String KEY_FULL_COLOR_SMALL	= "fullColorSmall";
		
		ScanCount(Map<String, Object> values) {
			super(values);
		}
		
		/*
		 * blackLarge (Number)
		 */
		public Integer getBlackLarge() {
			return getNumberValue(KEY_BLACK_LARGE);
		}
		
		/*
		 * blackSmall (Number)
		 */
		public Integer getBlackSmall() {
			return getNumberValue(KEY_BLACK_SMALL);
		}
		
		/*
		 * fullColorLarge (Number)
		 */
		public Integer getFullColorLarge() {
			return getNumberValue(KEY_FULL_COLOR_LARGE);
		}
		
		/*
		 * fullColorSmall (Number)
		 */
		public Integer getFullColorSmall() {
			return getNumberValue(KEY_FULL_COLOR_SMALL);
		}
		
	}
	
	
	public static class ProcessCount extends Element {
		
		private static final String KEY_BLACK	= "black";
		private static final String KEY_YMC		= "ymc";
		
		ProcessCount(Map<String, Object> values) {
			super(values);
		}
		
		/*
		 * black (Number)
		 */
		public Integer getBlack() {
			return getNumberValue(KEY_BLACK);
		}
		
		/*
		 * ymc (Number)
		 */
		public Integer getYmc() {
			return getNumberValue(KEY_YMC);
		}
		
	}
	
	public static class TotalCount extends Element {
		
		private static final String KEY_BLACK_TOTAL		= "blackTotal";
		private static final String KEY_BLACK_ACCOUNT	= "blackAccount";
		private static final String KEY_COLOR_TOTAL		= "colorTotal";
		private static final String KEY_COLOR_ACCOUNT	= "colorAccount";
		private static final String KEY_SCAN_TOTAL		= "scanTotal";

		TotalCount(Map<String, Object> values) {
			super(values);
		}
		
		/*
		 * blackTotal (Number)
		 */
		public Integer getBlackTotal() {
			return getNumberValue(KEY_BLACK_TOTAL);
		}
		
		/*
		 * blackAccount (Number)
		 */
		public Integer getBlackAccount() {
			return getNumberValue(KEY_BLACK_ACCOUNT);
		}
		
		/*
		 * colorTotal (Number)
		 */
		public Integer getColorTotal() {
			return getNumberValue(KEY_COLOR_TOTAL);
		}
		
		/*
		 * colorAccount (Number)
		 */
		public Integer getColorAccount() {
			return getNumberValue(KEY_COLOR_ACCOUNT);
		}
		
		/*
		 * scanTotal (Number)
		 */
		public Integer getScanTotal() {
			return getNumberValue(KEY_SCAN_TOTAL);
		}
		
	}
	
	/*
	 * @since SmartSDK V2.12
	 */
	public static class LimitCount extends Element {
		
		private static final String KEY_LIMIT_USE = "limitUse";
		private static final String KEY_LIMIT_MAX = "limitMax";
		private static final String KEY_LIMIT_COUNT_DETAIL_LIST = "limitCountDetailList";
		
		LimitCount(Map<String, Object> values) {
			super(values);
		}
		
		/*
		 * limitUse (Number)
		 * @since SmartSDK V2.12
		 */
		public Integer getLimitUse() {
			return getNumberValue(KEY_LIMIT_USE);
		}
		
		/*
		 * limitMax (Number)
		 * @since SmartSDK V2.12
		 */
		public Integer getLimitMax() {
			return getNumberValue(KEY_LIMIT_MAX);
		}
		
		public LimitCountDetailList getLimitCountDetailList() {
			List<Map<String, Object>> value = getArrayValue(KEY_LIMIT_COUNT_DETAIL_LIST);
			if (value == null) {
				return null;
			}
			return new LimitCountDetailList(value);
		}
	}

	/*
	 * @since SmartSDK V2.12
	 */
	public static class LimitCountDetailList extends ArrayElement<LimitCountDetail> {

		LimitCountDetailList(List<Map<String, Object>> list) {
			super(list);
		}

		@Override
		protected LimitCountDetail createElement(Map<String, Object> values) {
			return new LimitCountDetail(values);
		}
	}
	
	/*
	 * @since SmartSDK V2.12
	 */
	public static class LimitCountDetail extends Element {
		
		private static final String KEY_NAME 			= "name";
		private static final String KEY_PREVIOUS_USAGE  = "previousUsage";
		private static final String KEY_RESET_TIME 		= "resetTime";
		
		LimitCountDetail(Map<String, Object> values) {
			super(values);
		}
	   
		/* 
		 * name (String)
		 * @since SmartSDK V2.12
		 */
		public String getName() {
	    	return getStringValue(KEY_NAME);
	    }
	    
	    /*
	     * previousUsage (Number)
	     * @since SmartSDK V2.12
	     */
		public Integer getPreviousUsage() {
	    	return getNumberValue(KEY_PREVIOUS_USAGE);
	    }
	    
	    /*
	     * resetTime (String)
	     * @since SmartSDK V2.12
	     */
		public String getResetTime() {
	    	return getStringValue(KEY_RESET_TIME);
	    }
	}
}

