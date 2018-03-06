/*
 *  Copyright (C) 2014-2015 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.service.storage;

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

/*
 * @since SmartSDK V1.01
 */
public class Folders extends ApiClient {

    private static final String REST_PATH_FOLDERS       = "/rws/service/storage/folders";
    private static final String REST_PATH_FOLDERS_ID    = "/rws/service/storage/folders/%s";

    /*
     * @since SmartSDK V1.01
     */
    public Folders() {
        super();
    }

    /*
     * @since SmartSDK V1.01
     */
    public Folders(RestContext context) {
        super(context);
    }

    /*
     * GET: /rws/service/storage/folders
     *
     * RequestBody:  non
     * ResponseBody: GetFolderListResponseBody
     *
     * @since SmartSDK V1.01
     */
    public Response<GetFolderListResponseBody> getFolderList(Request request) throws IOException, InvalidResponseException {
        RestResponse restResponse = execute(
                build(RestRequest.METHOD_GET, REST_PATH_FOLDERS, request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<GetFolderListResponseBody>(restResponse, new GetFolderListResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }
    public Response<GetFolderListResponseBody> getFolderListContinuationResponse(Request request, String nextLink) throws IOException, InvalidResponseException {
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
                return new Response<GetFolderListResponseBody>(restResponse, new GetFolderListResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }


    /*
     * GET: /rws/service/storage/folders/{folderId}
     *
     * RequestBody:  non
     * ResponseBody: GetFolderResponseBody
     *
     * @since SmartSDK V1.01
     */
    public Response<GetFolderResponseBody> getFolder(Request request, String folderId) throws IOException, InvalidResponseException {
        if (folderId == null) {
            throw new NullPointerException("folderId must not be null.");
        }
        if (folderId.trim().length() == 0) {
    		throw new IllegalArgumentException("folderId must not be empty.");
    	}
        RestResponse restResponse = execute(
                build(RestRequest.METHOD_GET, String.format(REST_PATH_FOLDERS_ID, folderId), request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<GetFolderResponseBody>(restResponse, new GetFolderResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }
    
    /*
     * PUT: /rws/service/storage/folders/{folderId}
     *
     * RequestBody:  UpdateFolderRequestBody
     * ResponseBody: EmptyResponseBody
     *
     * @since SmartSDK V2.10
     */
    public Response<EmptyResponseBody> updateFolder(Request request, String folderId) throws IOException, InvalidResponseException {
    	if (folderId == null) {
            throw new NullPointerException("folderId must not be null.");
        }
    	if (folderId.trim().length() == 0) {
    		throw new IllegalArgumentException("folderId must not be empty.");
    	}
        RestResponse restResponse = execute(
                build(RestRequest.METHOD_PUT, String.format(REST_PATH_FOLDERS_ID, folderId), request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<EmptyResponseBody>(restResponse, new EmptyResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }
    
    /*
     * DELETE: /rws/service/storage/folders/{folderId}
     *
     * RequestBody:  non
     * ResponseBody: EmptyResponseBody
     *
     * @since SmartSDK V2.10
     */
    public Response<EmptyResponseBody> deleteFolder(Request request, String folderId) throws IOException, InvalidResponseException {
    	if (folderId == null) {
            throw new NullPointerException("folderId must not be null.");
        }
    	if (folderId.trim().length() == 0) {
    		throw new IllegalArgumentException("folderId must not be empty.");
    	}

        RestResponse restResponse = execute(
                build(RestRequest.METHOD_DELETE, String.format(REST_PATH_FOLDERS_ID, folderId), request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<EmptyResponseBody>(restResponse, new EmptyResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }

}
