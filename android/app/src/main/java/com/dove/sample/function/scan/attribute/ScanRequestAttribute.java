/*
 *  Copyright (C) 2013 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.function.scan.attribute;

import com.dove.sample.function.attribute.Attribute;

/**
 * スキャン要求属性のインターフェイスです
 * The interface of scan request attributes.
 */
public interface ScanRequestAttribute extends Attribute {

	/**
	 * この属性の設定される一意の値を返します。
	 * Returns a unique value set to this attribute.
	 * @return
	 */
	public abstract Object getValue();
}
