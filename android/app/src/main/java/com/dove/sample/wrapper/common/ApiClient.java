/*
 *  Copyright (C) 2013 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import com.dove.sample.wrapper.client.BasicRestContext;
import com.dove.sample.wrapper.client.RestClient;
import com.dove.sample.wrapper.client.RestContext;
import com.dove.sample.wrapper.client.RestRequest;
import com.dove.sample.wrapper.client.RestResponse;

/**
 * WebAPIと通信する際のクライアント側の挙動を定義する抽象クラス。
 */
public abstract class ApiClient {

	private final RestContext context;
	
	public ApiClient() {
		context = new BasicRestContext();
	}
	
	public ApiClient(RestContext context) {
		if (context == null) {
			throw new NullPointerException("context must not be null.");
		}
		this.context = context;
	}
	
	public final RestContext getContext() {
		return context;
	}

	protected final RestRequest build(String method, String path, Request request) throws UnsupportedEncodingException {
		RestRequest.Builder builder = new RestRequest.Builder();
		builder.host(context.getHost());
		builder.port(context.getPort());
        builder.scheme(context.getScheme());
		builder.path(path);
		builder.method(method);

		// header
		if (request.hasHeader()) {
			builder.header(request.getHeader().getHeaders());
		}
		
		// query
        if (request.hasQuery()) {
            builder.query(request.getQuery().getQueries());
        }
        
        // body
		if (request.hasBody()) {
            if(request.getBody() instanceof BinaryRequestBody) {
                BinaryRequestBody body = (BinaryRequestBody)request.getBody();
                builder.bodyInputStream(body.getInputStream());
                builder.bodySize(body.getSize());
            } else {
                builder.body(request.getBody().toEntityString());
            }
		}
		
		RestRequest restRequest;
		try {
			restRequest = builder.build();
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		return restRequest;
	}
	
	protected final RestResponse execute(RestRequest request) throws IOException {
		return new RestClient(context).execute(request);
	}
	
}
