/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.service.scanner;

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

public class Scanner extends ApiClient {
	
	private static final String REST_PATH_CAPABILITY	= "/rws/service/scanner/capability";
	private static final String REST_PATH_JOBS			= "/rws/service/scanner/jobs";
	private static final String REST_PATH_STATUS		= "/rws/service/scanner/status";
	private static final String REST_PATH_VALIDATE_CERTIFICATION	= "/rws/service/scanner/validateCertification"; // SmartSDK V2.12
	private static final String REST_PATH_SENT_RESULT	            = "/rws/service/scanner/sentResult"; // SmartSDK V2.12
	
	
	public Scanner() {
		super();
	}
	
	public Scanner(RestContext context) {
		super(context);
	}
	
	/*
	 * GET: /rws/service/scanner/capability
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
	 * GET: /rws/service/scanner/jobs
	 * 
	 * RequestBody:  non
	 * ResponseBody: GetJobListResponseBody
	 * 
	 * @since SmartSDK V2.00
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
	 * POST: /rws/service/scanner/jobs
	 * 
	 * RequestBody:  CreateJobRequestBody
	 * ResponseBody: CreateJobResponseBody
	 */
	public Response<CreateJobResponseBody> createJob(Request request) throws IOException, InvalidResponseException {
		// If you enable this comments, JSON structure that request will be output to the debug log.
		//if (Logger.isDebugEnabled()) {
		//	if (request.hasBody()) {
		//		Logger.debug("scanner createJob json: " + request.getBody().toEntityString());
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
				// SmartSDK V1.xx
				//throw Utils.createInvalidResponseException(restResponse, body);
				// SmartSDK V2.00 or later
				throw createCreateJobInvalidResponseException(restResponse, body);
		}
	}
	
	private CreateJobInvalidResponseException createCreateJobInvalidResponseException(RestResponse response, Map<String, Object> body) {
		CreateJobErrorResponseBody responseBody = null;
		if (body != null) {
			responseBody = new CreateJobErrorResponseBody(body);
		}
		return new CreateJobInvalidResponseException(response, responseBody);
	}
	
	/*
	 * GET: /rws/service/scanner/status
	 * 
	 * RequestBody:  non
	 * ResponseBody: GetScannerStatusResponseBody
	 */
	public Response<GetScannerStatusResponseBody> getScannerStatus(Request request) throws IOException, InvalidResponseException {
		RestResponse restResponse = execute(
				build(RestRequest.METHOD_GET, REST_PATH_STATUS, request));
		Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));
		
		switch (restResponse.getStatusCode()) {
			case 200:
				return new Response<GetScannerStatusResponseBody>(restResponse, new GetScannerStatusResponseBody(body));
			default:
				throw Utils.createInvalidResponseException(restResponse, body);
		}
	}
	
	/*
	 * POST: /rws/service/scanner/validateCertification
	 * 
	 * RequestBody:  ValidateCertificationRequestBody
	 * ResponseBody: ValidateCertificationResponseBody
	 * 
	 * @since SmartSDK V2.12
	 */
	public Response<ValidateCertificationResponseBody> validateCertification(Request request) throws IOException, InvalidResponseException {
		RestResponse restResponse = execute(
				build(RestRequest.METHOD_POST, REST_PATH_VALIDATE_CERTIFICATION, request));
		Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

		switch (restResponse.getStatusCode()) {
			case 200:
				return new Response<ValidateCertificationResponseBody>(restResponse, new ValidateCertificationResponseBody(body));
			default:
				throw Utils.createInvalidResponseException(restResponse, body);
		}
	}
	
	/*
	 * GET: /rws/service/scanner/sentResult
	 * 
	 * RequestBody:  non
	 * ResponseBody: getSentResultResponseBody
	 * 
	 * @since SmartSDK V2.12
	 */
	public Response<GetSentResultResponseBody> getSentResult(Request request) throws IOException, InvalidResponseException {
		RestResponse restResponse = execute(
				build(RestRequest.METHOD_GET, REST_PATH_SENT_RESULT, request));
		Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

		switch (restResponse.getStatusCode()) {
			case 200:
				return new Response<GetSentResultResponseBody>(restResponse, new GetSentResultResponseBody(body));
			default:
				throw Utils.createInvalidResponseException(restResponse, body);
		}
	}
	
}
