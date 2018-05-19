/*
 *  Copyright (C) 2013-2017 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.log.printerlog;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import com.dove.sample.wrapper.client.RestContext;
import com.dove.sample.wrapper.client.RestRequest;
import com.dove.sample.wrapper.client.RestResponse;
import com.dove.sample.wrapper.common.ApiClient;
import com.dove.sample.wrapper.common.EmptyResponseBody;
import com.dove.sample.wrapper.common.GenericJsonDecoder;
import com.dove.sample.wrapper.common.InvalidResponseException;
import com.dove.sample.wrapper.common.Request;
import com.dove.sample.wrapper.common.Response;
import com.dove.sample.wrapper.common.Utils;

public class DeviceLog extends ApiClient {
	
	private static final String REST_PATH_JOB_LOG     = "/rws/log/printerlog/devicelog/joblog.json";
	private static final String REST_PATH_ACCESS_LOG  = "/rws/log/printerlog/devicelog/accesslog.json";
	private static final String REST_PATH_ECOLOGY_LOG = "/rws/log/printerlog/devicelog/ecologylog.json";
	private static final String REST_PATH_DEVICE_LOG  = "/rws/log/printerlog/devicelog.json";
	private static final String REST_PATH_IMAGELOG_TRANSFER_SERVERLESS_DESTINATION  = "/rws/log/printerlog/imagelog/transfer/serverless/destination"; //SmartSDK v2.40
	private static final String REST_PATH_IMAGELOG_TRANSFER_SERVERLESS_COLLECTION  = "/rws/log/printerlog/imagelog/transfer/serverless/collection"; //SmartSDK v2.40
	private static final String REST_PATH_IMAGELOG_TRANSFER_SERVERLESS  = "/rws/log/printerlog/imagelog/transfer/serverless"; //SmartSDK v2.40
	private static final String REST_PATH_DEVICELOG_TRANSFER_SERVERLESS_DESTINATION  = "/rws/log/printerlog/devicelog/transfer/serverless/destination"; //SmartSDK v2.40
	private static final String REST_PATH_DEVICELOG_TRANSFER_SERVERLESS_COLLECTION  = "/rws/log/printerlog/devicelog/transfer/serverless/collection"; //SmartSDK v2.40
	private static final String REST_PATH_DEVICELOG_TRANSFER_SERVERLESS  = "/rws/log/printerlog/devicelog/transfer/serverless"; //SmartSDK v2.40

	
	public DeviceLog() {
		super();
	}
	
	public DeviceLog(RestContext context) {
		super(context);
	}
	
	/*
	 * GET: /rws/log/printerlog/devicelog/joblog.json
	 * 
	 * RequestBody:  non
	 * ResponseBody: GetDeviceLogResponseBody
	 */
	public Response<GetDeviceLogResponseBody> getJobLog(Request request) throws IOException, InvalidResponseException {
		RestResponse restResponse = execute(
				build(RestRequest.METHOD_GET, REST_PATH_JOB_LOG, request));
		Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));
		
		switch (restResponse.getStatusCode()) {
			case 200:
				return new Response<GetDeviceLogResponseBody>(restResponse, new GetDeviceLogResponseBody(body));
			default:
				throw Utils.createInvalidResponseException(restResponse, body);
		}
	}
	
	/*
	 * GET: /rws/log/printerlog/devicelog/accesslog.json
	 * 
	 * RequestBody:  non
	 * ResponseBody: GetDeviceLogResponseBody
	 */
	public Response<GetDeviceLogResponseBody> getAccessLog(Request request) throws IOException, InvalidResponseException {
		RestResponse restResponse = execute(
				build(RestRequest.METHOD_GET, REST_PATH_ACCESS_LOG, request));
		Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));
		
		switch (restResponse.getStatusCode()) {
			case 200:
				return new Response<GetDeviceLogResponseBody>(restResponse, new GetDeviceLogResponseBody(body));
			default:
				throw Utils.createInvalidResponseException(restResponse, body);
		}
	}
	
	/*
	 * GET: /rws/log/printerlog/devicelog/ecologylog.json
	 * 
	 * RequestBody:  non
	 * ResponseBody: GetDeviceLogResponseBody
	 */
	public Response<GetDeviceLogResponseBody> getEcologyLog(Request request) throws IOException, InvalidResponseException {
		RestResponse restResponse = execute(
				build(RestRequest.METHOD_GET, REST_PATH_ECOLOGY_LOG, request));
		Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));
		
		switch (restResponse.getStatusCode()) {
			case 200:
				return new Response<GetDeviceLogResponseBody>(restResponse, new GetDeviceLogResponseBody(body));
			default:
				throw Utils.createInvalidResponseException(restResponse, body);
		}
	}
	
	/*
	 * GET: /rws/log/printerlog/devicelog.json
	 * 
	 * RequestBody:  non
	 * ResponseBody: GetDeviceLogResponseBody
	 */
	public Response<GetDeviceLogResponseBody> getDeviceLog(Request request) throws IOException, InvalidResponseException {
		RestResponse restResponse = execute(
				build(RestRequest.METHOD_GET, REST_PATH_DEVICE_LOG, request));
		Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));
		
		switch (restResponse.getStatusCode()) {
			case 200:
				return new Response<GetDeviceLogResponseBody>(restResponse, new GetDeviceLogResponseBody(body));
			default:
				throw Utils.createInvalidResponseException(restResponse, body);
		}
	}
	
	public Response<GetDeviceLogResponseBody> getContinuationResponse(
			Request request, String nextLink) throws IOException, InvalidResponseException {
		
		final URL nextLinkURL = new URL(nextLink);
		final String path = nextLinkURL.getPath();
		final RestRequest restRequest = build(RestRequest.METHOD_GET, path, request);
		try {
			restRequest.setURI(nextLinkURL.toURI());
		} catch (URISyntaxException e) {
			throw new IOException(e);
		}
		
		RestResponse restResponse = execute(restRequest);
		Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));
		
		switch (restResponse.getStatusCode()) {
			case 200:
				return new Response<GetDeviceLogResponseBody>(restResponse, new GetDeviceLogResponseBody(body));
			default:
				throw Utils.createInvalidResponseException(restResponse, body);
		}
	}
	
	/*
	 * GET: /rws/log/printerlog/imagelog/transfer/serverless/destination
	 * 
	 * RequestBody:  non
	 * ResponseBody: GetImageLogDestinationResponseBody
	 * @since SmartSDK V2.40
	 */
	public Response<GetImageLogDestinationResponseBody> getImageLogDestination(Request request) throws IOException, InvalidResponseException {
		RestResponse restResponse = execute(
				build(RestRequest.METHOD_GET, REST_PATH_IMAGELOG_TRANSFER_SERVERLESS_DESTINATION, request));
		Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));
		
		switch (restResponse.getStatusCode()) {
			case 200:
				return new Response<GetImageLogDestinationResponseBody>(restResponse, new GetImageLogDestinationResponseBody(body));
			default:
				throw Utils.createInvalidResponseException(restResponse, body);
		}
	}
	
	/*
     * PUT: /rws/log/printerlog/imagelog/transfer/serverless/destination
     * 
     * RequestBody:  UpdateImageLogDestinationRequestBody
     * ResponseBody: non (EmptyResponseBody)
     * @since SmartSDK V2.40
     */
    public Response<EmptyResponseBody> updateImageLogDestination(Request request) throws IOException, InvalidResponseException {
        RestResponse restResponse = execute(
                build(RestRequest.METHOD_PUT, REST_PATH_IMAGELOG_TRANSFER_SERVERLESS_DESTINATION, request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<EmptyResponseBody>(restResponse, new EmptyResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }
    
    /*
	 * GET: /rws/log/printerlog/imagelog/transfer/serverless/collection
	 * 
	 * RequestBody:  non
	 * ResponseBody: GetImagelogCollectionResponseBody
	 * @since SmartSDK V2.40
	 */
	public Response<GetImageLogCollectionResponseBody> getImagelogCollection(Request request) throws IOException, InvalidResponseException {
		RestResponse restResponse = execute(
				build(RestRequest.METHOD_GET, REST_PATH_IMAGELOG_TRANSFER_SERVERLESS_COLLECTION, request));
		Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));
		
		switch (restResponse.getStatusCode()) {
			case 200:
				return new Response<GetImageLogCollectionResponseBody>(restResponse, new GetImageLogCollectionResponseBody(body));
			default:
				throw Utils.createInvalidResponseException(restResponse, body);
		}
	}
	
	/*
     * PUT: /rws/log/printerlog/imagelog/transfer/serverless/collection
     * 
     * RequestBody:  UpdateImageLogCollectionRequestBody
     * ResponseBody: non (EmptyResponseBody)
     * @since SmartSDK V2.40
     */
    public Response<EmptyResponseBody> updateImagelogCollection(Request request) throws IOException, InvalidResponseException {
        RestResponse restResponse = execute(
                build(RestRequest.METHOD_PUT, REST_PATH_IMAGELOG_TRANSFER_SERVERLESS_COLLECTION, request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<EmptyResponseBody>(restResponse, new EmptyResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }
    
    /*
	 * GET: /rws/log/printerlog/imagelog/transfer/serverless
	 * 
	 * RequestBody:  non
	 * ResponseBody: GetImageLogServerlessResponseBody
	 * @since SmartSDK V2.40
	 */
	public Response<GetImageLogServerlessResponseBody> getImageLogServerless(Request request) throws IOException, InvalidResponseException {
		RestResponse restResponse = execute(
				build(RestRequest.METHOD_GET, REST_PATH_IMAGELOG_TRANSFER_SERVERLESS, request));
		Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));
		
		switch (restResponse.getStatusCode()) {
			case 200:
				return new Response<GetImageLogServerlessResponseBody>(restResponse, new GetImageLogServerlessResponseBody(body));
			default:
				throw Utils.createInvalidResponseException(restResponse, body);
		}
	}
	
	/*
	 * GET: /rws/log/printerlog/devicelog/transfer/serverless/destination
	 * 
	 * RequestBody:  non
	 * ResponseBody: GetDeviceLogDestinationResponseBody
	 * @since SmartSDK V2.40
	 */
	public Response<GetDeviceLogDestinationResponseBody> getDeviceLogDestination(Request request) throws IOException, InvalidResponseException {
		RestResponse restResponse = execute(
				build(RestRequest.METHOD_GET, REST_PATH_DEVICELOG_TRANSFER_SERVERLESS_DESTINATION, request));
		Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));
			
		switch (restResponse.getStatusCode()) {
			case 200:
				return new Response<GetDeviceLogDestinationResponseBody>(restResponse, new GetDeviceLogDestinationResponseBody(body));
			default:
				throw Utils.createInvalidResponseException(restResponse, body);
		}
	}
		
	/*
	 * PUT: /rws/log/printerlog/devicelog/transfer/serverless/destination
     * 
     * RequestBody:  UpdateDeviceLogDestinationRequestBody
     * ResponseBody: non (EmptyResponseBody)
     * @since SmartSDK V2.40
     */
	public Response<EmptyResponseBody> updateDeviceLogDestination(Request request) throws IOException, InvalidResponseException {
		RestResponse restResponse = execute(
				build(RestRequest.METHOD_PUT, REST_PATH_DEVICELOG_TRANSFER_SERVERLESS_DESTINATION, request));
	    Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

	    switch (restResponse.getStatusCode()) {
	    	case 200:
	    		return new Response<EmptyResponseBody>(restResponse, new EmptyResponseBody(body));
	        default:
	            throw Utils.createInvalidResponseException(restResponse, body);
	    }
	}
	
	/*
	 * GET: /rws/log/printerlog/devicelog/transfer/serverless/collection
	 * 
	 * RequestBody:  non
	 * ResponseBody: GetDeviceLogCollectionResponseBody
	 * @since SmartSDK V2.40
	 */
	public Response<GetDeviceLogCollectionResponseBody> getDeviceLogCollection(Request request) throws IOException, InvalidResponseException {
		RestResponse restResponse = execute(
				build(RestRequest.METHOD_GET, REST_PATH_DEVICELOG_TRANSFER_SERVERLESS_COLLECTION, request));
		Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));
			
		switch (restResponse.getStatusCode()) {
			case 200:
				return new Response<GetDeviceLogCollectionResponseBody>(restResponse, new GetDeviceLogCollectionResponseBody(body));
			default:
				throw Utils.createInvalidResponseException(restResponse, body);
		}
	}
		
	/*
	 * PUT: /rws/log/printerlog/devicelog/transfer/serverless/collection
     * 
     * RequestBody:  UpdateDeviceLogCollectionRequestBody
     * ResponseBody: non (EmptyResponseBody)
     * @since SmartSDK V2.40
     */
	public Response<EmptyResponseBody> updateDeviceLogCollection(Request request) throws IOException, InvalidResponseException {
		RestResponse restResponse = execute(
				build(RestRequest.METHOD_PUT, REST_PATH_DEVICELOG_TRANSFER_SERVERLESS_COLLECTION, request));
	    Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

	    switch (restResponse.getStatusCode()) {
	    	case 200:
	    		return new Response<EmptyResponseBody>(restResponse, new EmptyResponseBody(body));
	        default:
	            throw Utils.createInvalidResponseException(restResponse, body);
	    }
	}
	
	/*
	 * GET: /rws/log/printerlog/devicelog/transfer/serverless
	 * 
	 * RequestBody:  non
	 * ResponseBody: GetDeviceLogServerlessResponseBody
	 * @since SmartSDK V2.40
	 */
	public Response<GetDeviceLogServerlessResponseBody> getDeviceLogServerless(Request request) throws IOException, InvalidResponseException {
		RestResponse restResponse = execute(
				build(RestRequest.METHOD_GET, REST_PATH_DEVICELOG_TRANSFER_SERVERLESS, request));
		Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));
		
		switch (restResponse.getStatusCode()) {
			case 200:
				return new Response<GetDeviceLogServerlessResponseBody>(restResponse, new GetDeviceLogServerlessResponseBody(body));
			default:
				throw Utils.createInvalidResponseException(restResponse, body);
		}
	}
	
}
