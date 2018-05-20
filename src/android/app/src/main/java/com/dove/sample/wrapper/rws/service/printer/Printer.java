/*
 *  Copyright (C) 2013-2014 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.service.printer;

import java.io.IOException;
import java.util.List;
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
//import com.dove.sample.wrapper.log.Logger;

public class Printer extends ApiClient {

	private static final String REST_PATH_CAPABILITY		= "/rws/service/printer/capability";
	private static final String REST_PATH_JOBS				= "/rws/service/printer/jobs";
	private static final String REST_PATH_STATUS			= "/rws/service/printer/status";
	private static final String REST_PATH_SUPPORTED_PDL		= "/rws/service/printer/supportedPDL";
	private static final String REST_PATH_FILE				= "/rws/service/printer/file";

	public Printer() {
		super();
	}

	public Printer(RestContext context) {
		super(context);
	}

	/*
	 * GET: /rws/service/printer/capability
	 * 
	 * RequestBody:  non
	 * ResponseBody: GetCapabilityResponseBody
	 */
	public Response<GetCapabilityResponseBody> getCapability(Request request) throws IOException, InvalidResponseException {
		RestResponse restResponse = execute(
				build(RestRequest.METHOD_GET, REST_PATH_CAPABILITY, request));
		Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

		switch (restResponse.getStatusCode()) {
			case 200:
				return new Response<GetCapabilityResponseBody>(restResponse, new GetCapabilityResponseBody(body));
			default:
				throw Utils.createInvalidResponseException(restResponse, body);
		}
	}

	/*
	 * GET: /rws/service/printer/jobs
	 * 
	 * RequestBody:  non
	 * ResponseBody: GetJobListResponseBody
	 */
	public Response<GetJobListResponseBody> getJobList(Request request) throws IOException, InvalidResponseException {
		RestResponse restResponse = execute(
				build(RestRequest.METHOD_GET, REST_PATH_JOBS, request));

		switch (restResponse.getStatusCode()) {
			case 200:
				List<Map<String, Object>> body = GenericJsonDecoder.decodeToList(restResponse.makeContentString("UTF-8"));
				return new Response<GetJobListResponseBody>(restResponse, new GetJobListResponseBody(body));
			default:
				Map<String, Object> errorBody = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));
				throw Utils.createInvalidResponseException(restResponse, errorBody);
		}
	}

	/*
	 * POST: /rws/service/printer/jobs
	 * 
	 * RequestBody:  CreateJobRequestBody
	 * ResponseBody: CreateJobResponseBody
	 */
	public Response<CreateJobResponseBody> createJob(Request request) throws IOException, InvalidResponseException {
		// If you enable this comments, JSON structure that request will be output to the debug log.
		//if (Logger.isDebugEnabled()) {
		//	if (request.hasBody()) {
		//		Logger.debug("printer createJob json: " + request.getBody().toEntityString());
		//	}
		//}

		RestResponse restResponse = execute(
				build(RestRequest.METHOD_POST, REST_PATH_JOBS, request));
		Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

		switch (restResponse.getStatusCode()) {
			case 200:	// validateOnly=true
				return new Response<CreateJobResponseBody>(restResponse, new CreateJobResponseBody(body));
			case 201:	// validateOnly=false, job created
				return new Response<CreateJobResponseBody>(restResponse, new CreateJobResponseBody(body));

			default:
				throw Utils.createInvalidResponseException(restResponse, body);
		}
	}

	/*
	 * GET: /rws/service/printer/status
	 * 
	 * RequestBody:  non
	 * ResponseBody: GetPrinterStatusResponseBody
	 */
	public Response<GetPrinterStatusResponseBody> getPrinterStatus(Request request) throws IOException, InvalidResponseException {
		RestResponse restResponse = execute(
				build(RestRequest.METHOD_GET, REST_PATH_STATUS, request));
		Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

		switch (restResponse.getStatusCode()) {
			case 200:
				return new Response<GetPrinterStatusResponseBody>(restResponse, new GetPrinterStatusResponseBody(body));
			default:
				throw Utils.createInvalidResponseException(restResponse, body);
		}
	}

	/*
	 * GET: /rws/service/printer/supportedPDL
	 * 
	 * RequestBody:  non
	 * ResponseBody: GetSupportedPDLResponseBody
	 */
	public Response<GetSupportedPDLResponseBody> getSupportedPDL(Request request) throws IOException, InvalidResponseException {
		RestResponse restResponse = execute(
				build(RestRequest.METHOD_GET, REST_PATH_SUPPORTED_PDL, request));
		Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

		switch (restResponse.getStatusCode()) {
			case 200:
				return new Response<GetSupportedPDLResponseBody>(restResponse, new GetSupportedPDLResponseBody(body));
			default:
				throw Utils.createInvalidResponseException(restResponse, body);
		}
	}

	/*
	 * POST: /rws/service/printer/file
	 * 
	 * RequestBody:  CreatePrintFileRequestBody
	 * ResponseBody: CreatePrintFileResponseBody
	 */
	public Response<CreatePrintFileResponseBody> createPrintFile(Request request) throws IOException, InvalidResponseException {
		RestResponse restResponse = execute(
				build(RestRequest.METHOD_POST, REST_PATH_FILE, request));
		Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

		switch (restResponse.getStatusCode()) {
			case 200:
				return new Response<CreatePrintFileResponseBody>(restResponse, new CreatePrintFileResponseBody(body));
			default:
				throw Utils.createInvalidResponseException(restResponse, body);
		}

	}

}
