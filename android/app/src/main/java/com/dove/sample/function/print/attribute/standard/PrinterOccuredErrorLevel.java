/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.function.print.attribute.standard;

import java.util.HashMap;
import java.util.Map;

import com.dove.sample.function.print.attribute.PrintServiceAttribute;

/**
 * 指定可能な発生しているエラーの致命度を表すクラスです。
 * The class to indicate the OccuredErrorLevel.
 * 
 * @since SmartSDK V2.12
 */
public enum PrinterOccuredErrorLevel implements PrintServiceAttribute{

	FATAL_ERROR("fatal_error"),
	ERROR("error"),
	WARNING("warning"),
	REPORT("report");

	private final String errorLevel;
	
	private PrinterOccuredErrorLevel(String value) {
        this.errorLevel = value;
    }
	
	@Override
	public Class<?> getCategory() {
		return getClass();
	}

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}
	
	private static volatile Map<String,PrinterOccuredErrorLevel> directory = null;
	private static Map<String,PrinterOccuredErrorLevel> getDirectory() {
        if(directory == null) {
        	PrinterOccuredErrorLevel[] errorArray = values();
            Map<String, PrinterOccuredErrorLevel> s = new HashMap<String, PrinterOccuredErrorLevel>(errorArray.length);
            for(PrinterOccuredErrorLevel error : errorArray) {
                s.put(error.errorLevel, error);
            }
            directory = s;
        }

        return directory;
    }
	
	public static PrinterOccuredErrorLevel fromString(String value) {
        return getDirectory().get(value);
    }

    @Override
    public String toString() {
        return errorLevel;
    }
}
