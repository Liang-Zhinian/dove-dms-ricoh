/*
 *  Copyright (C) 2017 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.log.printerlog;

import com.dove.sample.wrapper.common.ArrayElement;
import java.util.List;

import java.util.Map;

import com.dove.sample.wrapper.common.Element;
import com.dove.sample.wrapper.common.ResponseBody;

/*
 * @since SmartSDk v2.40
 */
public class GetImageLogCollectionResponseBody extends Element implements ResponseBody {

	private static final String KEY_COLLECT_SETTING = "collectSetting";
	private static final String KEY_SUPPORT_APPLICATION = "supportApplication";
	private static final String KEY_TARGET_APPLICATION = "targetApplication";
	private static final String KEY_MONO_RESOLUTION = "monoResolution";
	private static final String KEY_COLOR_RESOLUTION = "colorResolution";
	
	GetImageLogCollectionResponseBody(Map<String, Object> values) {
		super(values);
	}

	/*
	 * collectSetting (Boolean)
	 */
	public Boolean getCollectSetting() {
		return getBooleanValue(KEY_COLLECT_SETTING);
	}

	/*
	 * supportApplication (Array[String])
	 */
	public List<String> getSupportApplication() {
		return getArrayValue(KEY_SUPPORT_APPLICATION);
	}

	/*
	 * targetApplication (Array[String])
	 */
	public List<String> getTargetApplication() {
		return getArrayValue(KEY_TARGET_APPLICATION);
	}

	/*
	 * monoResolution (Array[Object])
	 */
	public MonoResolutionArray getMonoResolution() {
		List<Map<String, Object>> value = getArrayValue(KEY_MONO_RESOLUTION);
		if (value == null) {
			return null;
		}
		return new MonoResolutionArray(value);
	}

	/*
	 * colorResolution (Array[Object])
	 */
	public ColorResolutionArray getColorResolution() {
		List<Map<String, Object>> value = getArrayValue(KEY_COLOR_RESOLUTION);
		if (value == null) {
			return null;
		}
		return new ColorResolutionArray(value);
	}

	public static class MonoResolutionArray extends ArrayElement<MonoResolution> {

		MonoResolutionArray(List<Map<String, Object>> list) {
			super(list);
		}

		@Override
		protected MonoResolution createElement(Map<String, Object> values) {
			return new MonoResolution(values);
		}

	}

	public static class MonoResolution extends Element {

		private static final String KEY_APPLICATION = "application";
		private static final String KEY_VALUE = "value";

		MonoResolution(Map<String, Object> values) {
			super(values);
		}

		/*
		 * application (String)
		 */
		public String getApplication() {
			return getStringValue(KEY_APPLICATION);
		}

		/*
		 * value (Int)
		 */
		public Integer getValue() {
			return getNumberValue(KEY_VALUE);
		}

	}

	public static class ColorResolutionArray extends ArrayElement<ColorResolution> {

		ColorResolutionArray(List<Map<String, Object>> list) {
			super(list);
		}

		@Override
		protected ColorResolution createElement(Map<String, Object> values) {
			return new ColorResolution(values);
		}

	}

	public static class ColorResolution extends Element {
		private static final String KEY_APPLICATION = "application";
		private static final String KEY_VALUE = "value";

		ColorResolution(Map<String, Object> values) {
			super(values);
		}
		
		/*
		 * application (String)
		 */
		public String getApplication() {
			return getStringValue(KEY_APPLICATION);
		}

		/*
		 * value (Int)
		 */
		public Integer getValue() {
			return getNumberValue(KEY_VALUE);
		}

	}
}
