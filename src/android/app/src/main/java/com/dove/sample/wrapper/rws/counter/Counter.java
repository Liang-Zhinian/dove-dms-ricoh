/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.counter;

import java.io.IOException;
import java.util.Map;

import com.dove.sample.wrapper.client.RestContext;
import com.dove.sample.wrapper.client.RestRequest;
import com.dove.sample.wrapper.client.RestResponse;
import com.dove.sample.wrapper.common.ApiClient;
import com.dove.sample.wrapper.common.GenericJsonDecoder;
import com.dove.sample.wrapper.common.InvalidResponseException;
import com.dove.sample.wrapper.common.Request;
import com.dove.sample.wrapper.common.Response;
import com.dove.sample.wrapper.common.Utils;

public class Counter extends ApiClient {
	
	private static final String REST_PATH_CHARGE_COUNTER	= "/rws/counter/chargeCounter";
	private static final String REST_PATH_MANAGE_COUNTER	= "/rws/counter/manageCounter";
	private static final String REST_PATH_USER_COUNTER		= "/rws/counter/userCounter/%s";
	private static final String REST_PATH_ECO_COUNTER		= "/rws/counter/ecoCounter/%s";
	private static final String REST_PATH_USER_COUNTER_LIST	= "/rws/counter/userCounter"; // SmartSDK V2.12
	
	public Counter() {
		super();
	}
	
	public Counter(RestContext context) {
		super(context);
	}
	
	/*
	 * GET: /rws/counter/chargeCounter
	 * 
	 * RequestBody:  non
	 * ResponseBody: GetChargeCounterResponseBody
	 */
	public Response<GetChargeCounterResponseBody> getChargeCounter(Request request) throws IOException, InvalidResponseException {
		RestResponse restResponse = execute(
				build(RestRequest.METHOD_GET, REST_PATH_CHARGE_COUNTER, request));
		Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));
		
		switch (restResponse.getStatusCode()) {
			case 200:
				return new Response<GetChargeCounterResponseBody>(restResponse, new GetChargeCounterResponseBody(body));
			default:
				throw Utils.createInvalidResponseException(restResponse, body);
		}
	}
	
	/*
	 * GET: /rws/counter/manageCounter
	 * 
	 * RequestBody:  non
	 * ResponseBody: GetManageCounterResponseBody
	 */
	public Response<GetManageCounterResponseBody> getManageCounter(Request request) throws IOException, InvalidResponseException {
		RestResponse restResponse = execute(
				build(RestRequest.METHOD_GET, REST_PATH_MANAGE_COUNTER, request));
		Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));
		
		switch (restResponse.getStatusCode()) {
			case 200:
				return new Response<GetManageCounterResponseBody>(restResponse, new GetManageCounterResponseBody(body));
			default:
				throw Utils.createInvalidResponseException(restResponse, body);
		}
	}
	
	/*
	 * GET: /rws/counter/userCounter/{userId}
	 * 
	 * RequestBody:  non
	 * ResponseBody: GetUserCounterResponseBody
	 */
	public Response<GetUserCounterResponseBody> getUserCounter(Request request, String userId) throws IOException, InvalidResponseException {
		if (userId == null) {
			throw new NullPointerException("userId must not be null.");
		}
		if (userId.trim().length() == 0) {
			throw new IllegalArgumentException("jobId must not be empty.");
		}
		RestResponse restResponse = execute(
				build(RestRequest.METHOD_GET, String.format(REST_PATH_USER_COUNTER, userId), request));
		Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));
		
		switch (restResponse.getStatusCode()) {
			case 200:
				return new Response<GetUserCounterResponseBody>(restResponse, new GetUserCounterResponseBody(body));
			default:
				throw Utils.createInvalidResponseException(restResponse, body);
		}
	}
	
	/*
	 * GET: /rws/counter/ecoCounter/{userId}
	 * 
	 * RequestBody:  non
	 * ResponseBody: GetEcoCounterResponseBody
	 */
	public Response<GetEcoCounterResponseBody> getEcoCounter(Request request, String userId) throws IOException, InvalidResponseException {
		if (userId == null) {
			throw new NullPointerException("userId must not be null.");
		}
		if (userId.trim().length() == 0) {
			throw new IllegalArgumentException("jobId must not be empty.");
		}
		RestResponse restResponse = execute(
				build(RestRequest.METHOD_GET, String.format(REST_PATH_ECO_COUNTER, userId), request));
		Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));
		
		switch (restResponse.getStatusCode()) {
			case 200:
				return new Response<GetEcoCounterResponseBody>(restResponse, new GetEcoCounterResponseBody(body));
			default:
				throw Utils.createInvalidResponseException(restResponse, body);
		}
	}

	
	/*
	 * GET: /rws/counter/userCounter
	 * 
	 * RequestBody:  non
	 * ResponseBody: GetUserCounterListResponseBody
	 * 
	 * @since SmartSDK V2.12
	 */
	public Response<GetUserCounterListResponseBody> getUserCounterList(Request request) throws IOException, InvalidResponseException { 	
		RestResponse restResponse = execute(
				build(RestRequest.METHOD_GET, REST_PATH_USER_COUNTER_LIST, request));
		Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));
		
		switch (restResponse.getStatusCode()) {
			case 200:
				return new Response<GetUserCounterListResponseBody>(restResponse, new GetUserCounterListResponseBody(body));
			default:
				throw Utils.createInvalidResponseException(restResponse, body);
		}
	}
	
}

