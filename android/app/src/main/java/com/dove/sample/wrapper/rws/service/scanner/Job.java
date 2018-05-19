/*
 *  Copyright (C) 2013-2015 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.service.scanner;

import java.io.IOException;
import java.util.Map;

import com.dove.sample.wrapper.client.RestContext;
import com.dove.sample.wrapper.client.RestRequest;
import com.dove.sample.wrapper.client.RestResponse;
import com.dove.sample.wrapper.common.ApiClient;
import com.dove.sample.wrapper.common.BinaryResponseBody;
import com.dove.sample.wrapper.common.EmptyResponseBody;
import com.dove.sample.wrapper.common.GenericJsonDecoder;
import com.dove.sample.wrapper.common.InvalidResponseException;
import com.dove.sample.wrapper.common.Request;
import com.dove.sample.wrapper.common.RequestQuery;
import com.dove.sample.wrapper.common.Response;
import com.dove.sample.wrapper.common.Utils;
//import com.dove.sample.wrapper.log.Logger;

public class Job extends ApiClient {
	
	private static final String REST_PATH_JOBS_ID			= "/rws/service/scanner/jobs/%s";
	private static final String REST_PATH_JOBS_ID_FILE		= "/rws/service/scanner/jobs/%s/file";
	private static final String REST_PATH_JOBS_ID_THUMBNAIL	= "/rws/service/scanner/jobs/%s/thumbnail";
	private static final String REST_PATH_JOBS_ID_OCRDATA	= "/rws/service/scanner/jobs/%s/ocrdata";

    private static final String PARAMETER_GET_METHOD_KEY    = "getMethod";

	private final String jobId;
	
	public Job(String jobId) {
		super();
		if (jobId == null) {
			throw new NullPointerException("jobId must not be null.");
		}
		if (jobId.trim().length() == 0) {
    		throw new IllegalArgumentException("jobId must not be empty.");
    	}
		this.jobId = jobId;
	}
	
	public Job(RestContext context, String jobId) {
		super(context);
		if (jobId == null) {
			throw new NullPointerException("jobId must not be null.");
		}
		if (jobId.trim().length() == 0) {
    		throw new IllegalArgumentException("jobId must not be empty.");
    	}
		this.jobId = jobId;
	}
	
	private String getQueryValue(RequestQuery query, String key) {
		if (query == null) {
			return null;
		}
		return query.get(key);
	}
	
	/*
	 * GET: /rws/service/scanner/jobs/{jobId}
	 * 
	 * RequestBody:  non
	 * ResponseBody: GetJobStatusResponseBody
	 */
	public Response<GetJobStatusResponseBody> getJobStatus(Request request) throws IOException, InvalidResponseException {
		RestResponse restResponse = execute(
				build(RestRequest.METHOD_GET, String.format(REST_PATH_JOBS_ID, jobId), request));
		Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));
		
		switch (restResponse.getStatusCode()) {
			case 200:
				return new Response<GetJobStatusResponseBody>(restResponse, new GetJobStatusResponseBody(body));
			default:
				throw Utils.createInvalidResponseException(restResponse, body);
		}
	}
	
	/*
	 * PUT: /rws/service/scanner/jobs/{jobId}
	 * 
	 * RequestBody:  UpdateJobStatusRequestBody
	 * ResponseBody: non (EmptyResponseBody)
	 */
	public Response<EmptyResponseBody> updateJobStatus(Request request) throws IOException, InvalidResponseException {
		// If you enable this comments, JSON structure that request will be output to the debug log.
		//if (Logger.isDebugEnabled()) {
		//	if (request.hasBody()) {
		//		Logger.debug("scanner updateJobStatus json: " + request.getBody().toEntityString());
		//	}
		//}
        
		RestResponse restResponse = execute(
				build(RestRequest.METHOD_PUT, String.format(REST_PATH_JOBS_ID, jobId), request));
		Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));
		
		switch (restResponse.getStatusCode()) {
			case 200:	// validateOnly=true
				return new Response<EmptyResponseBody>(restResponse, new EmptyResponseBody(body));
			case 202:	// validateOnly=false, status change Accepted
				return new Response<EmptyResponseBody>(restResponse, new EmptyResponseBody(body));
			default:
				throw Utils.createInvalidResponseException(restResponse, body);
		}
	}
	
	/*
	 * GET: /rws/service/scanner/jobs/{jobId}/file?getMethod=direct
	 * 
	 * RequestBody:  non
	 * ResponseBody: BinaryResponseBody
	 */
	public Response<BinaryResponseBody> getFile(Request request) throws IOException, InvalidResponseException {
		String getMethod = getQueryValue(request.getQuery(), PARAMETER_GET_METHOD_KEY);
		if ((getMethod != null) && (! "direct".equals(getMethod))) {
			throw new IllegalArgumentException("Invalid parameter: getMethod.");
		}
		
		RestResponse restResponse = execute(
				build(RestRequest.METHOD_GET, String.format(REST_PATH_JOBS_ID_FILE, jobId), request));
		
		switch (restResponse.getStatusCode()) {
			case 200:
				return new Response<BinaryResponseBody>(restResponse, new BinaryResponseBody(restResponse.getBytes()));
			default:
				Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));
				throw Utils.createInvalidResponseException(restResponse, body);
		}
	}
	
	/*
	 * GET: /rws/service/scanner/jobs/{jobId}/file?getMethod=filePath
	 * MultiLink-Panel only
	 * 
	 * RequestBody:  non
	 * ResponseBody: FilePathResponseBody
	 */
	public Response<FilePathResponseBody> getFilePath(Request request) throws IOException, InvalidResponseException {
		String getMethod = getQueryValue(request.getQuery(), PARAMETER_GET_METHOD_KEY);
		if (! "filePath".equals(getMethod)) {
			throw new IllegalArgumentException("Invalid parameter: getMethod.");
		}
		
		RestResponse restResponse = execute(
				build(RestRequest.METHOD_GET, String.format(REST_PATH_JOBS_ID_FILE, jobId), request));
		Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));
		
		switch (restResponse.getStatusCode()) {
			case 200:
				return new Response<FilePathResponseBody>(restResponse, new FilePathResponseBody(body));
			default:
				throw Utils.createInvalidResponseException(restResponse, body);
		}
	}
	
	/*
	 * DELETE: /rws/service/scanner/jobs/{jobId}/file
	 * 
	 * RequestBody:  non
	 * ResponseBody: non (EmptyResponseBody)
	 */
	public Response<EmptyResponseBody> deleteFile(Request request) throws IOException, InvalidResponseException {
		RestResponse restResponse = execute(
				build(RestRequest.METHOD_DELETE, String.format(REST_PATH_JOBS_ID_FILE, jobId), request));
		Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));
		
		switch (restResponse.getStatusCode()) {
			case 200:
				return new Response<EmptyResponseBody>(restResponse, new EmptyResponseBody(body));
			default:
				throw Utils.createInvalidResponseException(restResponse, body);
		}
	}
	
	/*
	 * GET: /rws/service/scanner/jobs/{jobId}/thumbnail?getMethod=direct
	 * 
	 * RequestBody:  non
	 * ResponseBody: BinaryResponseBody
	 */
	public Response<BinaryResponseBody> getThumbnail(Request request) throws IOException, InvalidResponseException {
		String getMethod = getQueryValue(request.getQuery(), PARAMETER_GET_METHOD_KEY);
		if ((getMethod != null) && (! "direct".equals(getMethod))) {
			throw new IllegalArgumentException("Invalid parameter: getMethod.");
		}
		
		RestResponse restResponse = execute(
				build(RestRequest.METHOD_GET, String.format(REST_PATH_JOBS_ID_THUMBNAIL, jobId), request));
		
		switch (restResponse.getStatusCode()) {
			case 200:
				return new Response<BinaryResponseBody>(restResponse, new BinaryResponseBody(restResponse.getBytes()));
			default:
				Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));
				throw Utils.createInvalidResponseException(restResponse, body);
		}
	}
	
	/*
	 * GET: /rws/service/scanner/jobs/{jobId}/thumbnail?getMethod=filePath
	 * MultiLink-Panel only
	 * 
	 * RequestBody:  non
	 * ResponseBody: FilePathResponseBody
	 */
	public Response<FilePathResponseBody> getThumbnailPath(Request request) throws IOException, InvalidResponseException {
		String getMethod = getQueryValue(request.getQuery(), PARAMETER_GET_METHOD_KEY);
		if (! "filePath".equals(getMethod)) {
			throw new IllegalArgumentException("Invalid parameter: getMethod.");
		}
		
		RestResponse restResponse = execute(
				build(RestRequest.METHOD_GET, String.format(REST_PATH_JOBS_ID_THUMBNAIL, jobId), request));
		Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));
		
		switch (restResponse.getStatusCode()) {
			case 200:
				return new Response<FilePathResponseBody>(restResponse, new FilePathResponseBody(body));
			default:
				throw Utils.createInvalidResponseException(restResponse, body);
		}
	}
	
	/*
	 * GET: /rws/service/scanner/jobs/{jobId}/ocrdata
	 * 
	 * RequestBody:  non
	 * ResponseBody: GetOcrTextResponseBody
	 */
	public Response<GetOcrTextResponseBody> getOcrText(Request request) throws IOException, InvalidResponseException {
		RestResponse restResponse = execute(
				build(RestRequest.METHOD_GET, String.format(REST_PATH_JOBS_ID_OCRDATA, jobId), request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));
		
		switch (restResponse.getStatusCode()) {
			case 200:
				return new Response<GetOcrTextResponseBody>(restResponse, new GetOcrTextResponseBody(body));
			default:
				throw Utils.createInvalidResponseException(restResponse, body);
		}
	}

}
