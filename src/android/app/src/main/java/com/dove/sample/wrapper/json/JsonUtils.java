/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.json;

import com.dove.sample.wrapper.common.Utils;
import com.dove.sample.wrapper.log.Logger;

public final class JsonUtils {
	
	private static volatile Encoder encoder = null;
	private static volatile Decoder decoder = null;
	
    /**
     * Define the prefix for log information with abbreviation package and class name
     */
    private final static String PREFIX = "json:JsonUtils:";
	
	public static Encoder getEncoder() {
		Encoder encoder = JsonUtils.encoder;
		if (encoder == null) {
			synchronized (JsonUtils.class) {
				encoder = JsonUtils.encoder;
				if (encoder == null) {
					JsonUtils.encoder = encoder = loadEncoder();
				}
			}
		}
		return encoder;
	}
	
	public static Decoder getDecoder() {
		Decoder decoder = JsonUtils.decoder;
		if (decoder == null) {
			synchronized (JsonUtils.class) {
				decoder = JsonUtils.decoder;
				if (decoder == null) {
					JsonUtils.decoder = decoder = loadDecoder();
				}
			}
		}
		return decoder;
	}
	
	private static Encoder loadEncoder() {
		String encoderClassName = System.getProperty(Encoder.class.getName());
		if ((encoderClassName != null) && (encoderClassName.length() > 0)) {
			try {
				Class<?> encoderClass = Class.forName(encoderClassName);
				Encoder customizedEncoder = (Encoder) encoderClass.newInstance();
                Logger.info(Utils.getTagName(), PREFIX + "Customized JsonEncoder: " + customizedEncoder.getClass().getName());
				return customizedEncoder;
			} catch (Exception e) {
				throw new IllegalStateException("Cannot instantiation " + encoderClassName, e);
			}
		}
		return new DefaultEncoder();
	}
	
	private static Decoder loadDecoder() {
		String decoderClassName = System.getProperty(Decoder.class.getName());
		if ((decoderClassName != null) && (decoderClassName.length() > 0)) {
			try {
				Class<?> decoderClass = Class.forName(decoderClassName);
				Decoder customizedDecoder = (Decoder) decoderClass.newInstance();
	            Logger.info(Utils.getTagName(), PREFIX + "Customized JsonDecoder: " + customizedDecoder.getClass().getName());
				return customizedDecoder;
			} catch (Exception e) {
				throw new IllegalStateException("Cannot instantiation " + decoderClassName, e);
			}
		}
		return new DefaultDecoder();
	}
	
	private JsonUtils() {}
	
}
