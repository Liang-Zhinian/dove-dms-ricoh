/*
 *  Copyright (C) 2013 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.function.print.attribute;

import com.dove.sample.function.attribute.AttributeSet;
import com.dove.sample.function.attribute.HashAttributeSet;

/**
 * プリントサービスの属性セットのハッシュを提供するクラスです。
 * The class to provide print serivece attribute hash set.
 */
public class HashPrintServiceAttributeSet extends HashAttributeSet<PrintServiceAttribute> implements PrintServiceAttributeSet {

    public HashPrintServiceAttributeSet() {
        super();
    }

    public HashPrintServiceAttributeSet(PrintServiceAttribute attribute) {
        super(attribute);
    }

    public HashPrintServiceAttributeSet(PrintServiceAttribute[] attributes) {
        super(attributes);
    }

    public HashPrintServiceAttributeSet(AttributeSet attributes) {
        super(attributes);
    }
}
