/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.service.fax;

import java.io.IOException;
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

public class TransmissionStandbyFile extends ApiClient {

    private static final String REST_PATH_TRANSMISSION_STANDBY_FILE         = "/rws/service/fax/transmissionStandbyFile";
    private static final String REST_PATH_TRANSMISSION_STANDBY_FILE_NUMBER  = "/rws/service/fax/transmissionStandbyFile/%s";
    private static final String REST_PATH_TRANSMISSION_STANDBY_FILE_NUMBER_ADDRESS					= "/rws/service/fax/transmissionStandbyFile/%s/address"; // SmartSDK V2.12
    private static final String REST_PATH_TRANSMISSION_STANDBY_FILE_NUMBER_ADDRESS_DESTINATION_ID  	= "/rws/service/fax/transmissionStandbyFile/%s/address/%s"; // SmartSDK V2.12
    private static final String REST_PATH_TRANSMISSION_STANDBY_FILE_NUMBER_LOCK  					= "/rws/service/fax/transmissionStandbyFile/%s/lock"; // SmartSDK V2.12
    
    public TransmissionStandbyFile() {
        super();
    }

    public TransmissionStandbyFile(RestContext context) {
        super(context);
    }

    /*
     * GET: /rws/service/fax/transmissionStandbyFile
     * 
     * RequestBody:  non
     * ResponseBody: GetStandbyFileListResponseBody
     */
    public Response<GetStandbyFileListResponseBody> getStandbyFileList(Request request) throws IOException, InvalidResponseException {
        RestResponse restResponse = execute(
                build(RestRequest.METHOD_GET, REST_PATH_TRANSMISSION_STANDBY_FILE, request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<GetStandbyFileListResponseBody>(restResponse, new GetStandbyFileListResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }

    /*
     * PUT: /rws/service/fax/transmissionStandbyFile/{docNumber}
     * 
     * RequestBody:  UpdateStandbyFileRequestBody
     * ResponseBody: non (EmptyResponseBody)
     */
    public Response<EmptyResponseBody> updateFile(Request request, String docNumber) throws IOException, InvalidResponseException {
        if (docNumber == null) {
            throw new NullPointerException("docNumber must not be null.");
        }
        if (docNumber.trim().length() == 0) {
        	throw new IllegalArgumentException("docNumber must not be empty.");
        }

        RestResponse restResponse = execute(
                build(RestRequest.METHOD_PUT, String.format(REST_PATH_TRANSMISSION_STANDBY_FILE_NUMBER, docNumber), request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<EmptyResponseBody>(restResponse, new EmptyResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }

    /*
     * POST: /rws/service/fax/transmissionStandbyFile/{docNumber}
     * 
     * RequestBody:  non
     * ResponseBody: non (EmptyResponseBody)
     */
    public Response<EmptyResponseBody> transmitFile(Request request, String docNumber) throws IOException, InvalidResponseException {
        if (docNumber == null) {
            throw new NullPointerException("docNumber must not be null.");
        }
        if (docNumber.trim().length() == 0) {
        	throw new IllegalArgumentException("docNumber must not be empty.");
        }

        RestResponse restResponse = execute(
                build(RestRequest.METHOD_POST, String.format(REST_PATH_TRANSMISSION_STANDBY_FILE_NUMBER, docNumber), request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:   // validateOnly=true
                return new Response<EmptyResponseBody>(restResponse, new EmptyResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }

    /*
     * DELETE: /rws/service/fax/transmissionStandbyFile/{docNumber}
     * 
     * RequestBody:  non
     * ResponseBody: non (EmptyResponseBody)
     */
    public Response<EmptyResponseBody> deleteFile(Request request, String docNumber) throws IOException, InvalidResponseException {
        if (docNumber == null) {
            throw new NullPointerException("docNumber must not be null.");
        }
        if (docNumber.trim().length() == 0) {
        	throw new IllegalArgumentException("docNumber must not be empty.");
        }

        RestResponse restResponse = execute(
                build(RestRequest.METHOD_DELETE, String.format(REST_PATH_TRANSMISSION_STANDBY_FILE_NUMBER, docNumber), request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<EmptyResponseBody>(restResponse, new EmptyResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }

    /*
     * GET: /rws/service/fax/transmissionStandbyFile/{docNumber}/address
     * 
     * RequestBody:  non
     * ResponseBody: GetAddressResponseBody
     * 
     * @since SmartSDK V2.12
     */
    public Response<GetAddressResponseBody> getAddress(Request request, String docNumber) throws IOException, InvalidResponseException {
        if (docNumber == null) {
            throw new NullPointerException("docNumber must not be null.");
        }
        if (docNumber.trim().length() == 0) {
        	throw new IllegalArgumentException("docNumber must not be empty.");
        }

        RestResponse restResponse = execute(
                build(RestRequest.METHOD_GET, String.format(REST_PATH_TRANSMISSION_STANDBY_FILE_NUMBER_ADDRESS, docNumber), request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<GetAddressResponseBody>(restResponse, new GetAddressResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }

    /*
     * POST: /rws/service/fax/transmissionStandbyFile/{docNumber}/address
     * 
     * RequestBody:  CreateAddressRequestBody
     * ResponseBody: non (EmptyResponseBody)
     * 
     * @since SmartSDK V2.12
     */
    public Response<EmptyResponseBody> createAddress(Request request, String docNumber) throws IOException, InvalidResponseException {
        if (docNumber == null) {
            throw new NullPointerException("docNumber must not be null.");
        }
        if (docNumber.trim().length() == 0) {
        	throw new IllegalArgumentException("docNumber must not be empty.");
        }

        RestResponse restResponse = execute(
                build(RestRequest.METHOD_POST, String.format(REST_PATH_TRANSMISSION_STANDBY_FILE_NUMBER_ADDRESS, docNumber), request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<EmptyResponseBody>(restResponse, new EmptyResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }

    /*
     * PUT: /rws/service/fax/transmissionStandbyFile/{docNumber}/address/{destinationId}
     * 
     * RequestBody:  UpdateAddressRequestBody
     * ResponseBody: non (EmptyResponseBody)
     * 
     * @since SmartSDK V2.12
     */
    public Response<EmptyResponseBody> updateAddress(Request request, String docNumber,String destinationId) throws IOException, InvalidResponseException {
        if (docNumber == null) {
            throw new NullPointerException("docNumber must not be null.");
        }
        if (docNumber.trim().length() == 0) {
        	throw new IllegalArgumentException("docNumber must not be empty.");
        }
        if (destinationId == null) {
            throw new NullPointerException("destinationId must not be null.");
        }
        if (destinationId.trim().length() == 0) {
        	throw new IllegalArgumentException("destinationId must not be empty.");
        }

        RestResponse restResponse = execute(
                build(RestRequest.METHOD_PUT, String.format(REST_PATH_TRANSMISSION_STANDBY_FILE_NUMBER_ADDRESS_DESTINATION_ID, docNumber,destinationId), request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<EmptyResponseBody>(restResponse, new EmptyResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }

    /*
     * DELETE: /rws/service/fax/transmissionStandbyFile/{docNumber}/address/{destinationId}
     * 
     * RequestBody:  non
     * ResponseBody: DeleteAddressResponseBody
     * 
     * @since SmartSDK V2.12
     */
    public Response<DeleteAddressResponseBody> deleteAddress(Request request, String docNumber,String destinationId) throws IOException, InvalidResponseException {
    	if (docNumber == null) {
            throw new NullPointerException("docNumber must not be null.");
        }
        if (docNumber.trim().length() == 0) {
        	throw new IllegalArgumentException("docNumber must not be empty.");
        }
        if (destinationId == null) {
            throw new NullPointerException("destinationId must not be null.");
        }
        if (destinationId.trim().length() == 0) {
        	throw new IllegalArgumentException("destinationId must not be empty.");
        }

        RestResponse restResponse = execute(
                build(RestRequest.METHOD_DELETE, String.format(REST_PATH_TRANSMISSION_STANDBY_FILE_NUMBER_ADDRESS_DESTINATION_ID, docNumber,destinationId), request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<DeleteAddressResponseBody>(restResponse, new DeleteAddressResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }

    /*
     * POST: /rws/service/fax/transmissionStandbyFile/{docNumber}/lock
     * 
     * RequestBody:  non
     * ResponseBody: CreateLockResponseBody
     * 
     * @since SmartSDK V2.12
     */
    public Response<CreateLockResponseBody> createLock(Request request, String docNumber) throws IOException, InvalidResponseException {
        if (docNumber == null) {
            throw new NullPointerException("docNumber must not be null.");
        }
        if (docNumber.trim().length() == 0) {
        	throw new IllegalArgumentException("docNumber must not be empty.");
        }

        RestResponse restResponse = execute(
                build(RestRequest.METHOD_POST, String.format(REST_PATH_TRANSMISSION_STANDBY_FILE_NUMBER_LOCK, docNumber), request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<CreateLockResponseBody>(restResponse, new CreateLockResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }

    /*
     * PUT: /rws/service/fax/transmissionStandbyFile/{docNumber}/lock
     * 
     * RequestBody:  non
     * ResponseBody: non (EmptyResponseBody)
     * 
     * @since SmartSDK V2.12
     */
    public Response<EmptyResponseBody> updateLock(Request request, String docNumber) throws IOException, InvalidResponseException {
        if (docNumber == null) {
            throw new NullPointerException("docNumber must not be null.");
        }
        if (docNumber.trim().length() == 0) {
        	throw new IllegalArgumentException("docNumber must not be empty.");
        }

        RestResponse restResponse = execute(
                build(RestRequest.METHOD_PUT, String.format(REST_PATH_TRANSMISSION_STANDBY_FILE_NUMBER_LOCK, docNumber), request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<EmptyResponseBody>(restResponse, new EmptyResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }

    /*
     * DELETE: /rws/service/fax/transmissionStandbyFile/{docNumber}/lock
     * 
     * RequestBody:  non
     * ResponseBody: non (EmptyResponseBody)
     * 
     * @since SmartSDK V2.12
     */
    public Response<EmptyResponseBody> deleteLock(Request request, String docNumber) throws IOException, InvalidResponseException {
        if (docNumber == null) {
            throw new NullPointerException("docNumber must not be null.");
        }
        if (docNumber.trim().length() == 0) {
        	throw new IllegalArgumentException("docNumber must not be empty.");
        }

        RestResponse restResponse = execute(
                build(RestRequest.METHOD_DELETE, String.format(REST_PATH_TRANSMISSION_STANDBY_FILE_NUMBER_LOCK, docNumber), request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<EmptyResponseBody>(restResponse, new EmptyResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }

}
