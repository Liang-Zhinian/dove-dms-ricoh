/*
 *  Copyright (C) 2015 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.service.scanner;

import com.dove.sample.wrapper.client.RestResponse;
import com.dove.sample.wrapper.common.InvalidResponseException;

/**
 * @since SmartSDK V2.00
 */
public class CreateJobInvalidResponseException extends InvalidResponseException {

	private final CreateJobErrorResponseBody body;

	public CreateJobInvalidResponseException(RestResponse response, CreateJobErrorResponseBody body) {
		super(response, body);
		this.body = body;
	}

	@Override
	public CreateJobErrorResponseBody getBody() {
		return body;
	}

}
