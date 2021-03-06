/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.function.scan.attribute.standard;

import com.dove.sample.function.attribute.MediaSizeName;
import com.dove.sample.function.scan.attribute.ScanRequestAttribute;

import java.util.HashMap;
import java.util.Map;

public final class MagnificationSize implements ScanRequestAttribute {

	private static final String MAGNIFICATION_SIZE = "magnificationSize";

	private MediaSizeName originalSize = null;
	private String customLongEdge = null; //SmartSDK V2.12
	private String customShortEdge = null; //SmartSDK V2.12
	private CustomX customX = null;
	private CustomY customY = null;
	
	/**
	 * @since SmartSDK V2.12
	 */
	public MagnificationSize() {}

	/**
	 * 指定した原稿サイズで変倍サイズ属性を構築する
	 * Builds the scaling size attribute with the specified document size
	 *
	 * @param size 変倍するサイズ
	 *             Scaling size
	 */
	public MagnificationSize(MediaSizeName size) {
		this.originalSize = size;
	}

	/**
	 * 指定した変倍率で変倍サイズ属性を構築する
	 * Builds the scaling size attribute with the specified scaling ratio
	 *
	 * @param x 変倍サイズx方向
	 *        Scaling size in x direction
	 * @param y 変倍サイズy方向
	 *        Scaling size in y direction
	 * @param landscape 原稿方向(横の場合true）
	 *                  Document orientation (true if landscape)
	 */
	public MagnificationSize(int x, int y, boolean landscape) {
		if(landscape){
			this.originalSize = MediaSizeName.CUSTOM_LANDSCAPE;
		} else {
			this.originalSize = MediaSizeName.CUSTOM_SIZE;
		}

		this.customX = new CustomX(x);
		this.customY = new CustomY(y);
	}
	
	/**
	 * @since SmartSDK V2.12
	 */
	public void setCustomXY(int x, int y, boolean landscape) {
		if(landscape){
			this.originalSize = MediaSizeName.CUSTOM_LANDSCAPE;
		} else {
			this.originalSize = MediaSizeName.CUSTOM_SIZE;
		}

		this.customX = new CustomX(x);
		this.customY = new CustomY(y);
		this.customLongEdge = null;
		this.customShortEdge = null;
	}
	
	/**
	 * @since SmartSDK V2.12
	 */
	public void setCustomEdge(int customLongEdge, int customShortEdge, boolean landscape) {
		if(landscape){
			this.originalSize = MediaSizeName.CUSTOM_LANDSCAPE;
		} else {
			this.originalSize = MediaSizeName.CUSTOM_SIZE;
		}

		this.customX = null;
		this.customY = null;
		this.customLongEdge = String.valueOf(customLongEdge);
		this.customShortEdge = String.valueOf(customShortEdge);
	}

	@Override
	public Class<?> getCategory() {
		return MagnificationSize.class;
	}

	@Override
	public String getName() {
		return MAGNIFICATION_SIZE;
	}

	@Override
	public Object getValue() {
		Map<String,Object> returnObject = new HashMap<String,Object>();
		if(originalSize != null)	returnObject.put("size", this.originalSize.getValue());
		if(customLongEdge != null) returnObject.put("customLongEdge", this.customLongEdge);
		if(customShortEdge != null) returnObject.put("customShortEdge", this.customShortEdge);
		if(customX != null)			returnObject.put("customX", String.valueOf(this.customX.getX()));
		if(customY != null)			returnObject.put("customY", String.valueOf(this.customY.getY()));
		return returnObject;
	}

	public static class CustomX {
		private int x;

		public CustomX(int x) {
			this.x = x;
		}

		public int getX() { return this.x; }
		public void setX(int x) { this.x = x; }
	}

	public static class CustomY {
		private int y;

		public CustomY(int y) {
			this.y = y;
		}

		public int getY() { return this.y; }
		public void setY(int y) { this.y = y; }
	}

}
