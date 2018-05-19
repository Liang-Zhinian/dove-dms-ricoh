/*
 *  Copyright (C) 2013 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.function.print.impl.service;

import com.dove.sample.function.print.attribute.PrintRequestAttribute;
import com.dove.sample.function.print.attribute.standard.Copies;
import com.dove.sample.function.print.attribute.standard.PrintColor;
import com.dove.sample.function.print.attribute.standard.Staple;
import com.dove.sample.function.print.supported.MaxMinSupported;
import com.dove.sample.wrapper.rws.service.printer.Capability;

import java.util.HashMap;
import java.util.Map;

/**
 * 指定したCapabilityオブジェクトから、SupportedAttributeを生成するためのクラスです。
 * The class to create supportedAttribute from specified capability object.
 */
public class SupportedAttributeBuilder {
    private SupportedAttributeBuilder() {
    }

    public static Map<Class<? extends PrintRequestAttribute>, Object> getSupportedAttribute(Capability cap) {
        if( cap == null ) return null;

        Map<Class<? extends PrintRequestAttribute>, Object> retList = new HashMap<Class<? extends PrintRequestAttribute>, Object>();

        if(cap.getCopiesRange() != null ) {
            retList.put(Copies.class, MaxMinSupported.getMaxMinSupported(cap.getCopiesRange()));
        }

        if(cap.getStapleList() != null) {
            retList.put(Staple.class, Staple.getSupportedValue(cap.getStapleList()));
        }

        if(cap.getPrintColorList() != null) {
            retList.put(PrintColor.class, PrintColor.getSupportedValue(cap.getPrintColorList()));
        }


        return retList;
    }
}
