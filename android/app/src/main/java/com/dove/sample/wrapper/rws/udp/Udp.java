/*
 *  Copyright (C) 2017 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.udp;

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
/*
 * @since SmartSDK V2.31
 */
public class Udp extends ApiClient{
	private static final String REST_PATH_TRANSMISSION = "/rws/udp/transmission"; 
	
	public Udp() {
		super();
	}
	
	public Udp(RestContext context ) {
		super(context);
	}
	
	/*
     * PUT: /rws/udp/transmission
     * 
     * RequestBody:  SendTransmissionRequestBody
     * ResponseBody: non (EmptyResponseBody)
     */
    public Response<EmptyResponseBody> sendTransmission(Request request) throws IOException, InvalidResponseException {
        RestResponse restResponse = execute(
                build(RestRequest.METHOD_PUT, REST_PATH_TRANSMISSION, request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:  
                return new Response<EmptyResponseBody>(restResponse, new EmptyResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }

}
