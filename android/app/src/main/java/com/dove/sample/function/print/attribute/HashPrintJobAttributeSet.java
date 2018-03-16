/*
 *  Copyright (C) 2013 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.function.print.attribute;

import com.dove.sample.function.attribute.AttributeSet;
import com.dove.sample.function.attribute.HashAttributeSet;

/**
 * ジョブの属性セットのハッシュを提供するクラスです。
 * The class to provide job attribute hash set.
 */
public class HashPrintJobAttributeSet extends HashAttributeSet<PrintJobAttribute> implements PrintJobAttributeSet {

    public HashPrintJobAttributeSet() {
        super();
    }

    public HashPrintJobAttributeSet(PrintJobAttribute attribute) {
        super(attribute);
    }

    public HashPrintJobAttributeSet(PrintJobAttribute[] attributes) {
        super(attributes);
    }

    public HashPrintJobAttributeSet(AttributeSet attributes) {
        super(attributes);
    }
}
