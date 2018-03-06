/*
 *  Copyright (C) 2013-2017 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.property;

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

public class DeviceProperty extends ApiClient {

    private static final String REST_PATH_DEVICEINFO    		        = "/rws/property/deviceInfo";
    private static final String REST_PATH_PROPERTY_LDAP 		        = "/rws/property/ldap/%d";
    private static final String REST_PATH_LDAP_SEARCH			        = "/rws/property/ldap"; // SmartSDK V2.12
    private static final String REST_PATH_PAPER_WEIGHT_LIST             = "/rws/property/paperWeightList"; // SmartSDK V2.12    
    private static final String REST_PATH_EMAIL                         = "/rws/property/email"; // SmartSDK V2.12
    private static final String REST_PATH_SMARTSDKINFO                  = "/rws/property/smartSdkInfo"; // SmartSDK V2.12
    private static final String REST_PATH_USELIMIT_MACHINE_ACTION       = "/rws/property/useLimit/machineAction"; // SmartSDK V2.12
    private static final String REST_PATH_USELIMIT_ENHANCED_USELIMIT    = "/rws/property/useLimit/enhancedUseLimit"; // SmartSDK V2.12
    private static final String REST_PATH_NETWORK                       = "/rws/property/network";  //SmartSDK v2.31
    private static final String REST_PATH_ADDRESSBOOK                   = "/rws/property/addressbook"; // SmartSDK v2.40
    private static final String REST_PATH_USB_DEVICE_LIST               = "/rws/property/usbDeviceList"; // SmartSDK v2.40
    private static final String REST_PATH_USB                           = "/rws/property/usb"; // SmartSDK v2.40

    public DeviceProperty() {
        super();
    }

    public DeviceProperty(RestContext context) {
        super(context);
    }

    /*
     * GET: /rws/property/deviceInfo
     * 
     * RequestBody:  non
     * ResponseBody: GetDeviceInfoResponseBody
     */
    public Response<GetDeviceInfoResponseBody> getDeviceInfo(Request request) throws IOException, InvalidResponseException {
        RestResponse restResponse = execute(
                build(RestRequest.METHOD_GET, REST_PATH_DEVICEINFO, request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<GetDeviceInfoResponseBody>(restResponse, new GetDeviceInfoResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }

    
    /*
     * GET: /rws/property/ldap/{num}
     * 
     * RequestBody:  non
     * ResponseBody: GetLdapResponseBody
     */
    public Response<GetLdapResponseBody> getLdap(Request request, int num) throws IOException, InvalidResponseException {
        RestResponse restResponse = execute(
                build(RestRequest.METHOD_GET, String.format(REST_PATH_PROPERTY_LDAP, num), request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<GetLdapResponseBody>(restResponse, new GetLdapResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }

    /*
     * PUT: /rws/property/ldap/{num}
     * 
     * RequestBody:  UpdateLdapRequestBody
     * ResponseBody: UpdateLdapResponseBody
     */
    public Response<UpdateLdapResponseBody> updateLdap(Request request, int num) throws IOException, InvalidResponseException {
        RestResponse restResponse = execute(
                build(RestRequest.METHOD_PUT, String.format(REST_PATH_PROPERTY_LDAP, num), request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<UpdateLdapResponseBody>(restResponse, new UpdateLdapResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }
    
    /*
     * GET: /rws/property/paperWeight
     * 
     * RequestBody:  non
     * ResponseBody: GetPaperWeightListResponseBody
     * 
     * @since SmartSDK V2.12
     */
    public Response<GetPaperWeightListResponseBody> getPaperWeightList(Request request) throws IOException, InvalidResponseException {
    	RestResponse restResponse = execute(
                build(RestRequest.METHOD_GET, REST_PATH_PAPER_WEIGHT_LIST, request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<GetPaperWeightListResponseBody>(restResponse, new GetPaperWeightListResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }
    
    /*
     * GET: /rws/property/ldap
     * 
     * RequestBody:  non
     * ResponseBody: GetLdapSearchResponseBody
     * 
     * @since SmartSDK V2.12
     */
    public Response<GetLdapSettingResponseBody> getLdapSetting(Request request) throws IOException, InvalidResponseException {
        RestResponse restResponse = execute(
                build(RestRequest.METHOD_GET, REST_PATH_LDAP_SEARCH, request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<GetLdapSettingResponseBody>(restResponse, new GetLdapSettingResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }

    /*
     * GET: /rws/property/email
     * 
     * RequestBody:  non
     * ResponseBody: GetEmailResponseBody
     * 
     * @since SmartSDK V2.12
     */
    public Response<GetEmailResponseBody> getEmail(Request request) throws IOException, InvalidResponseException {
        RestResponse restResponse = execute(
                build(RestRequest.METHOD_GET, REST_PATH_EMAIL, request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<GetEmailResponseBody>(restResponse, new GetEmailResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }

    /*
     * PUT: /rws/property/email
     * 
     * RequestBody:  UpdateEmailRequestBody
     * ResponseBody: non (EmptyResponseBody)
     * 
     * @since SmartSDK V2.12
     */
    public Response<EmptyResponseBody> updateEmail(Request request) throws IOException, InvalidResponseException {
        RestResponse restResponse = execute(
                build(RestRequest.METHOD_PUT, REST_PATH_EMAIL, request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
            	return new Response<EmptyResponseBody>(restResponse, new EmptyResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }

    /*
     * GET: /rws/property/smartSdkInfo
     * 
     * RequestBody:  non
     * ResponseBody: GetSmartSdkInfoResponseBody
     * 
     * @since SmartSDK V2.12
     */
    public Response<GetSmartSdkInfoResponseBody> getSmartSdkInfo(Request request) throws IOException, InvalidResponseException {
        RestResponse restResponse = execute(
                build(RestRequest.METHOD_GET, REST_PATH_SMARTSDKINFO, request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<GetSmartSdkInfoResponseBody>(restResponse, new GetSmartSdkInfoResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }

    /*
     * GET: /rws/property/useLimit/machineAction
     * 
     * RequestBody:  non
     * ResponseBody: GetMachineActionResponseBody
     * 
     * @since SmartSDK V2.12
     */
    public Response<GetMachineActionResponseBody> getMachineAction(Request request) throws IOException, InvalidResponseException {
        RestResponse restResponse = execute(
                build(RestRequest.METHOD_GET, REST_PATH_USELIMIT_MACHINE_ACTION, request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<GetMachineActionResponseBody>(restResponse, new GetMachineActionResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }

    /*
     * PUT: /rws/property/useLimit/machineAction
     * 
     * RequestBody:  UpdateMachineRequestBody
     * ResponseBody: non (EmptyResponseBody)
     * 
     * @since SmartSDK V2.12
     */
    public Response<EmptyResponseBody> updateMachineAction(Request request) throws IOException, InvalidResponseException {
        RestResponse restResponse = execute(
                build(RestRequest.METHOD_PUT, REST_PATH_USELIMIT_MACHINE_ACTION, request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
            	return new Response<EmptyResponseBody>(restResponse, new EmptyResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }

    /*
     * GET: /rws/property/useLimit/enhancedUseLimit
     * 
     * RequestBody:  non
     * ResponseBody: GetEnhancedUseLimitResponseBody
     * 
     * @since SmartSDK V2.12
     */
    public Response<GetEnhancedUseLimitResponseBody> getEnhancedUseLimit(Request request) throws IOException, InvalidResponseException {
        RestResponse restResponse = execute(
                build(RestRequest.METHOD_GET, REST_PATH_USELIMIT_ENHANCED_USELIMIT, request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<GetEnhancedUseLimitResponseBody>(restResponse, new GetEnhancedUseLimitResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }
    
    /*
     * PUT: /rws/property/useLimit/enhancedUseLimit
     * 
     * RequestBody:  UpdateEnhancedUseLimitRequestBody
     * ResponseBody: non (EmptyResponseBody)
     * 
     * @since SmartSDK V2.12
     */
    public Response<EmptyResponseBody> updateEnhancedUseLimit(Request request) throws IOException, InvalidResponseException {
        RestResponse restResponse = execute(
                build(RestRequest.METHOD_PUT, REST_PATH_USELIMIT_ENHANCED_USELIMIT, request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<EmptyResponseBody>(restResponse, new EmptyResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }
    
    /*
     * GET: /rws/property/network
     * 
     * RequestBody:  non
     * ResponseBody: GetNetworkResponseBody
     * 
     * @since SmartSDK V2.31
     */
    public Response<GetNetworkResponseBody> getNetwork(Request request) throws IOException, InvalidResponseException {
    	RestResponse restResponse = execute(
                build(RestRequest.METHOD_GET, REST_PATH_NETWORK, request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<GetNetworkResponseBody>(restResponse, new GetNetworkResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }
    
    /*
     * GET: /rws/property/addressbook
     * 
     * RequestBody:  non
     * ResponseBody: GetAddressbookResponseBody
     * @since SmartSDK v2.40
     */
    public Response<GetAddressbookResponseBody> getAddressbook(Request request) throws IOException, InvalidResponseException {
        RestResponse restResponse = execute(
                build(RestRequest.METHOD_GET, REST_PATH_ADDRESSBOOK, request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<GetAddressbookResponseBody>(restResponse, new GetAddressbookResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }
    
    /*
     * PUT: /rws/property/addressbook
     * 
     * RequestBody:  UpdateAddressbookRequestBody
     * ResponseBody: non (EmptyResponseBody)
     * @since SmartSDK V2.40
     */
    public Response<EmptyResponseBody> updateAddressbook(Request request) throws IOException, InvalidResponseException {
        RestResponse restResponse = execute(
                build(RestRequest.METHOD_PUT, REST_PATH_ADDRESSBOOK, request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<EmptyResponseBody>(restResponse, new EmptyResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }
    
    /*
     * GET: /rws/property/usbDeviceList
     * 
     * RequestBody:  non
     * ResponseBody: GetUsbDeviceListResponseBody
     * @since SmartSDK v2.40
     */
    public Response<GetUsbDeviceListResponseBody> getUsbDeviceList(Request request) throws IOException, InvalidResponseException {
        RestResponse restResponse = execute(
                build(RestRequest.METHOD_GET, REST_PATH_USB_DEVICE_LIST, request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<GetUsbDeviceListResponseBody>(restResponse, new GetUsbDeviceListResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }
    
    /*
     * PUT: /rws/property/usbDeviceList
     * 
     * RequestBody:  UpdateUsbDeviceListRequestBody
     * ResponseBody: non (EmptyResponseBody)
     * @since SmartSDK V2.40
     */
    public Response<EmptyResponseBody> updateUsbDeviceList(Request request) throws IOException, InvalidResponseException {
        RestResponse restResponse = execute(
                build(RestRequest.METHOD_PUT, REST_PATH_USB_DEVICE_LIST, request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<EmptyResponseBody>(restResponse, new EmptyResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }
    
    /*
     * GET: /rws/property/usb
     * 
     * RequestBody:  non
     * ResponseBody: GetUsbResponseBody
     * @since SmartSDK v2.40
     */
    public Response<GetUsbResponseBody> getUsb(Request request) throws IOException, InvalidResponseException {
        RestResponse restResponse = execute(
                build(RestRequest.METHOD_GET, REST_PATH_USB, request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<GetUsbResponseBody>(restResponse, new GetUsbResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }
    
    /*
     * PUT: /rws/property/usb
     * 
     * RequestBody:  UpdateUsbRequestBody
     * ResponseBody: non (EmptyResponseBody)
     * @since SmartSDK V2.40
     */
    public Response<EmptyResponseBody> updateUsb(Request request) throws IOException, InvalidResponseException {
        RestResponse restResponse = execute(
                build(RestRequest.METHOD_PUT, REST_PATH_USB, request));
        Map<String, Object> body = GenericJsonDecoder.decodeToMap(restResponse.makeContentString("UTF-8"));

        switch (restResponse.getStatusCode()) {
            case 200:
                return new Response<EmptyResponseBody>(restResponse, new EmptyResponseBody(body));
            default:
                throw Utils.createInvalidResponseException(restResponse, body);
        }
    }
}
