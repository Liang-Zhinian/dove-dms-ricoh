/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.service.fax;

import java.io.IOException;
import java.util.Map;
import java.util.List;

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
//import com.dove.sample.wrapper.log.Logger;

public class Fax extends ApiClient {

    private static final String REST_PATH_CAPABILITY                = "/rws/service/fax/capability";
    private static final String REST_PATH_JOBS                      = "/rws/service/fax/jobs";
    private static final String REST_PATH_LOG_COMMUNICATION         = "/rws/service/fax/log/communication";
    private static final String REST_PATH_RECENT_DESTINATIONS       = "/rws/service/fax/recentDestinations";
    private static final String REST_PATH_STATUS                    = "/rws/service/fax/status";
    private static final String REST_PATH_FCODE_INFORMATION_BOX     = "/rws/service/fax/fcodeInformationBox"; // SmartSDK V2.12
    private static final String REST_PATH_FCODE_PERSONAL_BOX        = "/rws/service/fax/fcodePersonalBox"; // SmartSDK V2.12
    private static final String REST_PATH_FILE				        = "/rws/service/fax/file"; // SmartSDK V2.12
    private static final String REST_PATH_MANUAL_EMAIL_RECEIVE      = "/rws/service/fax/manualEmailReceive"; // SmartSDK V2.12
    
    public Fax() {
        super();
    }

    public Fax(RestContext context) {
        super(context);
    }

    /*
     * GET: /rws/service/fax/capability
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
     * GET: /rws/service/fax/jobs
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
     * POST: /rws/service/fax/jobs
     * 
     * RequestBody:  CreateJobRequestBody
     * ResponseBody: CreateJobResponseBody
     */
    public Response<CreateJobResponseBody> createJob(Request request) throws IOException, InvalidResponseException {
        // If you enable this comments, JSON structure that request will be output to the debug log.
        //if (Logger.isDebugEnabled()) {
        //    if (request.hasBody()) {
        //        Logger.debug("fax createJob json: " + request.getBody().toEntityString());
        //    }
        //}

        RestResponse restResponse = execute(
                build(RestRequest.METHOD_POST, REST_PATH_JOBS, request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:   // validateOnly=true
                return new Response<CreateJobResponseBody>(restResponse, new CreateJobResponseBody(body));
            case 201:   // validateOnly=false, job created
                return new Response<CreateJobResponseBody>(restResponse, new CreateJobResponseBody(body));

            default:
                // SmartSDK V1.xx
                //throw Utils.createInvalidResponseException(restResponse, body);
                // @since SmartSDK V2.00 or later
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
     * GET: /rws/service/fax/log/communication
     * 
     * RequestBody:  non
     * ResponseBody: GetCommunicationLogResponseBody
     */
    public Response<GetCommunicationLogResponseBody> getCommunicationLog(Request request) throws IOException, InvalidResponseException {
        RestResponse restResponse = execute(
                build(RestRequest.METHOD_GET, REST_PATH_LOG_COMMUNICATION, request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<GetCommunicationLogResponseBody>(restResponse, new GetCommunicationLogResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }

    /*
     * GET: /rws/service/fax/recentDestinations
     * 
     * RequestBody:  non
     * ResponseBody: GetRecentDestinationsResponseBody
     */
    public Response<GetRecentDestinationsResponseBody> getRecentDestinations(Request request) throws IOException, InvalidResponseException {
        RestResponse restResponse = execute(
                build(RestRequest.METHOD_GET, REST_PATH_RECENT_DESTINATIONS, request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<GetRecentDestinationsResponseBody>(restResponse, new GetRecentDestinationsResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }

    /*
     * GET: /rws/service/fax/status
     * 
     * RequestBody:  non
     * ResponseBody: GetFaxStatusResponseBody
     */
    public Response<GetFaxStatusResponseBody> getFaxStatus(Request request) throws IOException, InvalidResponseException {
        RestResponse restResponse = execute(
                build(RestRequest.METHOD_GET, REST_PATH_STATUS, request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<GetFaxStatusResponseBody>(restResponse, new GetFaxStatusResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }

    /*
     * POST: /rws/service/fax/status
     * 
     * RequestBody:  PostFaxStatusUserActionRequestBody
     * ResponseBody: non (EmptyResponseBody)
     * 
     * @since SmartSDK V2.00
     */
    public Response<EmptyResponseBody> postFaxStatusUserAction(Request request) throws IOException, InvalidResponseException {
        RestResponse restResponse = execute(
                build(RestRequest.METHOD_POST, REST_PATH_STATUS, request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<EmptyResponseBody>(restResponse, new EmptyResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }

    /*
     * GET: /rws/service/fax/fcodeInformationBox
     * 
     * RequestBody:  non
     * ResponseBody: GetFcodeInformationBoxResponseBody
     * 
     * @since SmartSDK V2.12
     */
    public Response<GetFcodeInformationBoxResponseBody> getFcodeInformationBox(Request request) throws IOException, InvalidResponseException {

        RestResponse restResponse = execute(
                build(RestRequest.METHOD_GET, REST_PATH_FCODE_INFORMATION_BOX, request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<GetFcodeInformationBoxResponseBody>(restResponse, new GetFcodeInformationBoxResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }

    /*
     * POST: /rws/service/fax/fcodeInformationBox
     * 
     * RequestBody:  RemoveFcodeInformationBoxRequestBody
     * ResponseBody: non (EmptyResponseBody)
     * 
     * @since SmartSDK V2.12
     */
    public Response<EmptyResponseBody> removeFcodeInformationBox(Request request) throws IOException, InvalidResponseException {

        RestResponse restResponse = execute(
                build(RestRequest.METHOD_POST, REST_PATH_FCODE_INFORMATION_BOX, request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<EmptyResponseBody>(restResponse, new EmptyResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }

    /*
     * GET: /rws/service/fax/fcodePersonalBox
     * 
     * RequestBody:  non
     * ResponseBody: GetFcodePersonalBoxResponseBody
     * 
     * @since SmartSDK V2.12
     */
    public Response<GetFcodePersonalBoxResponseBody> getFcodePersonalInfoBox(Request request) throws IOException, InvalidResponseException {

        RestResponse restResponse = execute(
                build(RestRequest.METHOD_GET, REST_PATH_FCODE_PERSONAL_BOX, request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<GetFcodePersonalBoxResponseBody>(restResponse, new GetFcodePersonalBoxResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }

    /*
     * POST: /rws/service/fax/file
     * 
     * RequestBody:  CreateFaxFileRequestBody
     * ResponseBody: CreateFaxFileResponseBody
     * 
     * @since SmartSDK V2.12
     */
    public Response<CreateFaxFileResponseBody> createFaxFile(Request request) throws IOException, InvalidResponseException {

        RestResponse restResponse = execute(
                build(RestRequest.METHOD_POST, REST_PATH_FILE, request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<CreateFaxFileResponseBody>(restResponse, new CreateFaxFileResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }

    /*
     * POST: /rws/service/fax/manualEmailReceive
     * 
     * RequestBody:  non
     * ResponseBody: non (EmptyResponseBody)
     * 
     * @since SmartSDK V2.12
     */
    public Response<EmptyResponseBody> executeManualEmailReceive(Request request) throws IOException, InvalidResponseException {

        RestResponse restResponse = execute(
                build(RestRequest.METHOD_POST, REST_PATH_MANUAL_EMAIL_RECEIVE, request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<EmptyResponseBody>(restResponse, new EmptyResponseBody(body));
            default:
                throw createCreateJobInvalidResponseException(restResponse, body);
        }
    }

}
