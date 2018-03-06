/*
 *  Copyright (C) 2017 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.log.printerlog;
import com.dove.sample.wrapper.common.ArrayElement;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

import com.dove.sample.wrapper.common.RequestBody;
import com.dove.sample.wrapper.common.Utils;
import com.dove.sample.wrapper.common.WritableElement;
import com.dove.sample.wrapper.json.EncodedException;
import com.dove.sample.wrapper.json.JsonUtils;
import com.dove.sample.wrapper.log.Logger;

/*
 * @since SmartSDK v2.40
 */
public class UpdateImageLogCollectionRequestBody extends WritableElement implements RequestBody {
	
	/**
     * Define the prefix for log information with abbreviation package and class name
     */
    private final static String PREFIX = "printerlog:updateImgCollReq:";
    private static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";
    private static final String KEY_COLLECT_SETTING = "collectSetting";
    private static final String KEY_TARGET_APPLICATION = "targetApplication";
    private static final String KEY_MONO_RESOLUTION = "monoResolution";
    private static final String KEY_COLOR_RESOLUTION = "colorResolution";
    
    public UpdateImageLogCollectionRequestBody() {
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
     * collectSetting (Boolean)
     */
    public Boolean getCollectSetting() {
    	return getBooleanValue(KEY_COLLECT_SETTING);
    }
    public void setCollectSetting(Boolean value) {
    	setBooleanValue(KEY_COLLECT_SETTING, value);
    }
    public Boolean removeCollectSetting() {
    	return removeBooleanValue(KEY_COLLECT_SETTING);
    }
    
    /*
     * targetApplication (Array[String])
     */
    public List<String> getTargetApplication() {
    	return getArrayValue(KEY_TARGET_APPLICATION);
    }
    public void setTargetApplication(List<String> value) {
    	setArrayValue(KEY_TARGET_APPLICATION, value);
    }
    public List<String> removeTargetApplication() {
    	return removeArrayValue(KEY_TARGET_APPLICATION);
    }
    
	/*
	 * monoResolution (Array[Object])
	 */
	public MonoResolutionArray getMonoResolution() {
		List<Map<String, Object>> value = getArrayValue(KEY_MONO_RESOLUTION);
		if (value == null) {
			value = Utils.createElementList();
			setArrayValue(KEY_MONO_RESOLUTION, value);
		}
		return new MonoResolutionArray(value);
	}	
	public MonoResolutionArray removeMonoResolution() {
		List<Map<String, Object>> value = removeArrayValue(KEY_MONO_RESOLUTION);
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
			value = Utils.createElementList();
			setArrayValue(KEY_COLOR_RESOLUTION, value);
		}
		return new ColorResolutionArray(value);
	}
	public ColorResolutionArray removeColorResolution() {
		List<Map<String, Object>> value = removeArrayValue(KEY_COLOR_RESOLUTION);
		if (value == null) {
			return null;
		}
		return new ColorResolutionArray(value);
	}

    public static class MonoResolutionArray extends ArrayElement<MonoResolution> {

        MonoResolutionArray(List<Map<String, Object>> list) {
            super(list);
        }

        public boolean add(MonoResolution value) {
            if (value == null) {
                throw new NullPointerException("value must not be null.");
            }
            return list.add(value.cloneValues());
        }

        public MonoResolution remove(int index) {
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
        protected MonoResolution createElement(Map<String, Object> values) {
            return new MonoResolution(values);
        }
    }

    public static class MonoResolution extends WritableElement {

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
	    public void setApplication(String value) {
	    	setStringValue(KEY_APPLICATION, value);
	    }
	    public String removeApplication() {
	    	return removeStringValue(KEY_APPLICATION);
	    }
	    
	    /*
	     * value (Int)
	     */
	    public Integer getValue() {
	    	return getNumberValue(KEY_VALUE);
	    }
	    public void setValue(Integer value) {
	    	setNumberValue(KEY_VALUE, value);
	    }
	    public Integer removeValue() {
	    	return removeNumberValue(KEY_VALUE);
	    }

    }

    public static class ColorResolutionArray extends ArrayElement<ColorResolution> {

        ColorResolutionArray(List<Map<String, Object>> list) {
            super(list);
        }

        public boolean add(ColorResolution value) {
            if (value == null) {
                throw new NullPointerException("value must not be null.");
            }
            return list.add(value.cloneValues());
        }

        public ColorResolution remove(int index) {
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
        protected ColorResolution createElement(Map<String, Object> values) {
            return new ColorResolution(values);
        }

    }

    public static class ColorResolution extends WritableElement {
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
	    public void setApplication(String value) {
	    	setStringValue(KEY_APPLICATION, value);
	    }
	    public String removeApplication() {
	    	return removeStringValue(KEY_APPLICATION);
	    }
    
	    /*
	     * value (Int)
	     */
	    public Integer getValue() {
	    	return getNumberValue(KEY_VALUE);
	    }
	    public void setValue(Integer value) {
	    	setNumberValue(KEY_VALUE, value);
	    }
	    public Integer removeValue() {
	    	return removeNumberValue(KEY_VALUE);
	    }

    }
}
