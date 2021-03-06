/*
 *  Copyright (C) 2013 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.function.scan.attribute;

import com.dove.sample.function.attribute.HashAttributeSet;

/**
 * スキャンサービスの属性を管理する属性セットクラスです。
 * 本属性セットはハッシュ構造を持ちます。
 * Attribute set class to manage scan service attributes.
 * This attribute set supports hash structure.
 */
public final class HashScanServiceAttributeSet extends HashAttributeSet<ScanServiceAttribute> implements ScanServiceAttributeSet {

	public HashScanServiceAttributeSet() {
		super();
	}

	public HashScanServiceAttributeSet(ScanServiceAttribute attribute) {
		super(attribute);
	}

	public HashScanServiceAttributeSet(ScanServiceAttribute[] attributes) {
		super(attributes);
	}

	public HashScanServiceAttributeSet(ScanServiceAttributeSet attributes) {
		super(attributes);
	}

}
