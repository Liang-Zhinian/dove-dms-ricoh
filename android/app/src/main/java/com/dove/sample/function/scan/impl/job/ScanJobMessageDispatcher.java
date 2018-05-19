/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.function.scan.impl.job;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import com.dove.sample.function.attribute.Attribute;
import com.dove.sample.function.common.SmartSDKApplication;
import com.dove.sample.function.scan.attribute.HashScanJobAttributeSet;
import com.dove.sample.function.scan.attribute.HashScanRequestAttributeSet;
import com.dove.sample.function.scan.attribute.ScanAttributeException;
import com.dove.sample.function.scan.attribute.ScanException;
import com.dove.sample.function.scan.attribute.ScanJobAttribute;
import com.dove.sample.function.scan.attribute.ScanJobAttributeSet;
import com.dove.sample.function.scan.attribute.ScanRequestAttribute;
import com.dove.sample.function.scan.attribute.ScanRequestAttributeSet;
import com.dove.sample.function.scan.attribute.ScanResponseException;
import com.dove.sample.function.scan.attribute.standard.ScanJobFilingInfo;
import com.dove.sample.function.scan.attribute.standard.ScanJobOcringInfo;
import com.dove.sample.function.scan.attribute.standard.ScanJobPrintingInfo;
import com.dove.sample.function.scan.attribute.standard.ScanJobScanningInfo;
import com.dove.sample.function.scan.attribute.standard.ScanJobSendingInfo;
import com.dove.sample.function.scan.attribute.standard.ScanJobState;
import com.dove.sample.function.scan.attribute.standard.ScanJobStateReasons;
import com.dove.sample.function.scan.attribute.standard.ScanJobStoredFileId;
import com.dove.sample.function.scan.impl.service.AsyncJobEventHandler;
import com.dove.sample.function.scan.impl.service.FunctionMessageDispatcher;
import com.dove.sample.wrapper.common.EmptyResponseBody;
import com.dove.sample.wrapper.common.ErrorResponseBody;
import com.dove.sample.wrapper.common.InvalidResponseException;
import com.dove.sample.wrapper.common.Request;
import com.dove.sample.wrapper.common.RequestHeader;
import com.dove.sample.wrapper.common.RequestQuery;
import com.dove.sample.wrapper.common.Response;
import com.dove.sample.wrapper.rws.service.scanner.CreateJobRequestBody;
import com.dove.sample.wrapper.rws.service.scanner.CreateJobResponseBody;
import com.dove.sample.wrapper.rws.service.scanner.GetJobStatusResponseBody;
import com.dove.sample.wrapper.rws.service.scanner.Job;
import com.dove.sample.wrapper.rws.service.scanner.Scanner;
import com.dove.sample.wrapper.rws.service.scanner.UpdateJobStatusRequestBody;

import org.apache.http.HttpStatus;

import android.util.Log;

/**
 * スキャンジョブを管理するクラスです。
 * The class to manage scan jobs
 */
public class ScanJobMessageDispatcher implements AsyncJobEventHandler{

    /**
     * ジョブの状態変化を通知するためのリスナ
     * The listener to notify job state changes
     */
	private JobListener listener;

    private static final String EXCLUSIVE_JOB_SETTINGS_KEY = "error.exclusive_job_settings";

    private Scanner mScanner;
    private Job mJob;

    private final LinkedBlockingQueue<GetJobStatusResponseBody> mJobEventQueue;
    private JobEventDispatcher mJobEventDispatcher;
    
    /**
     * Define the prefix for log information with abbreviation package and class name
     */
    private final static String PREFIX = "job:ScanJobMessageDis:";

	public ScanJobMessageDispatcher(){
        mScanner = new Scanner();
        mJob = null;
        mJobEventQueue = new LinkedBlockingQueue<GetJobStatusResponseBody>();
        mJobEventDispatcher = null;
	}

	/**
	 * ジョブの状態変化用リスナーを登録します。(アプリから指定します)
	 * Registers the listener to notify job state changes (specify from the application)
	 *
	 * @param listener 登録するリスナー
	 *                 The listener to register
	 */
	public void setScanJobListener(JobListener listener){
		this.listener = listener;
	}

    /**********************************************************
     * 上位層からの要求
     * Request from the upper layer
     **********************************************************/
	/**
	 * スキャンジョブ開始要求を投げます。
	 * Throws the request to start the scan job.
	 *
	 * @param attributes スキャン要求属性セット
	 *                   Scan request attribute set
     * @throws ScanException
	 */
	public boolean requestStartScanJob(ScanRequestAttributeSet attributes) throws ScanException {
        if(this.mJob != null) {
            throw new ScanException("Can not start scan(), because running scan process.");
        }

        // SubscribedIdの発行とジョブの非同期イベントを登録する
        // Issues an subscribed ID and registers the asynchronous job event
        String subscribedId = FunctionMessageDispatcher.getInstance().addAsyncJobEventListener(this);
        if( subscribedId == null ) {
            FunctionMessageDispatcher.getInstance().removeAsyncJobEventListener(this);
            throw new ScanException("Can not start scan(), because SDKService doesn't return a response.");
        }

        RequestHeader header = new RequestHeader();
        header.put(RequestHeader.KEY_X_SUBSCRIPTION_ID, subscribedId);
        header.put(RequestHeader.KEY_X_APPLICATION_ID, SmartSDKApplication.getProductId());

        CreateJobRequestBody body = getCreateJobRequestBody(attributes);
        body.setValidateOnly(false);

        Request req = new Request();
        req.setBody(body);
        req.setHeader(header);

        try {
            Response<CreateJobResponseBody> resp = mScanner.createJob(req);
            if(resp.getResponse().getStatusCode() == HttpStatus.SC_CREATED) {

                String jobId = resp.getBody().getJobId();

                listener.setJobId(jobId);
                mJob = new Job(jobId);
                startJobEventDispatcher(jobId);

                return true;
            }

        } catch (IOException e) {
            FunctionMessageDispatcher.getInstance().removeAsyncJobEventListener(this);
            throw new ScanException(e.getMessage());

        } catch (InvalidResponseException e) {
            if( e.getStatusCode() == HttpStatus.SC_BAD_REQUEST) {
                if(e.hasBody()) {
                    for(ErrorResponseBody.Errors errors : e.getBody().getErrors()) {
                        if(errors.getMessageId().equals(EXCLUSIVE_JOB_SETTINGS_KEY)) {
                            FunctionMessageDispatcher.getInstance().removeAsyncJobEventListener(this);
                            throw new ScanAttributeException(errors.getMessage());
                        }
                    }
                }
            }
            FunctionMessageDispatcher.getInstance().removeAsyncJobEventListener(this);
            throw new ScanResponseException(e);
        }

        FunctionMessageDispatcher.getInstance().removeAsyncJobEventListener(this);
        return false;
	}

	/**
	 * スキャンジョブ検証を投げます。
	 * Throws scan job validation
	 *
     * @param attributes スキャン要求属性セット
     *                   Scan request attribute set
	 */
	public boolean verifyScanJob(ScanRequestAttributeSet attributes) throws ScanException {
        CreateJobRequestBody body = new CreateJobRequestBody();
        body.setValidateOnly(true);

        if( attributes != null) {
            for(Attribute attr : attributes.getCollection()){
                body.getJobSetting().setValue(attr.getName(), ((ScanRequestAttribute)attr).getValue());
            }
        }

        RequestHeader header = new RequestHeader();
        header.put(RequestHeader.KEY_X_APPLICATION_ID, SmartSDKApplication.getProductId());

        Request req = new Request();
        req.setBody(body);
        req.setHeader(header);

        if(this.mJob == null){
            return verifyScanJob(req);
        }else{
            return verifyContinueScanJob(req);
        }
	}

    private boolean verifyScanJob(Request request)  throws ScanException {

        try {
            Response<CreateJobResponseBody> scannerJobResp = mScanner.createJob(request);

            return (scannerJobResp.getResponse().getStatusCode() == HttpStatus.SC_OK);

        } catch(InvalidResponseException e) {
            if( e.getStatusCode() == HttpStatus.SC_BAD_REQUEST) {
                ErrorResponseBody.ErrorsArray errors = e.getBody().getErrors();
                String exclusiveJobSettingsValue = null;
                if(errors != null){
                    for(ErrorResponseBody.Errors error : errors) {
                        if(error.getMessageId().equals(EXCLUSIVE_JOB_SETTINGS_KEY))
                            exclusiveJobSettingsValue = error.getMessage();
                    }
                }
                if(exclusiveJobSettingsValue != null) {
                    throw new ScanAttributeException(exclusiveJobSettingsValue);
                }
            }
            throw new ScanResponseException(e);
        } catch (IOException e) {
            throw new ScanException(e.getMessage());
        }
    }

    private boolean verifyContinueScanJob(Request request)  throws ScanException {
        try {
            Response<EmptyResponseBody> resp = mJob.updateJobStatus(request);

            return (resp.getResponse().getStatusCode() == HttpStatus.SC_OK);

        } catch(InvalidResponseException e) {
            if( e.getStatusCode() == HttpStatus.SC_BAD_REQUEST) {
                ErrorResponseBody.ErrorsArray errors = e.getBody().getErrors();
                String exclusiveJobSettingsValue = null;
                if(errors != null) {
                    for(ErrorResponseBody.Errors error : errors) {
                        if(error.getMessageId().equals(EXCLUSIVE_JOB_SETTINGS_KEY))
                            exclusiveJobSettingsValue = error.getMessage();
                    }
                }
                if(exclusiveJobSettingsValue != null) {
                    throw new ScanAttributeException(exclusiveJobSettingsValue);
                }
            }
            throw new ScanResponseException(e);
        } catch (IOException e) {
            throw new ScanException(e.getMessage());
        }
    }

	/**
	 * スキャンジョブ中止イベントを投げます。
	 * Throws scan job cancel event
	 */
	public boolean requestCancelScanJob() throws ScanException {
        if(this.mJob == null){
            throw new ScanException("Can not cancel(), because scan is not running.");
        }

        UpdateJobStatusRequestBody body = new UpdateJobStatusRequestBody();
        body.setJobStatus(ScanJobState.CANCELED.toString());
        body.setValidateOnly(Boolean.FALSE);

        RequestHeader header = new RequestHeader();
        header.put(RequestHeader.KEY_X_APPLICATION_ID, SmartSDKApplication.getProductId());

        Request req = new Request();
        req.setBody(body);
        req.setHeader(header);

        Response<EmptyResponseBody> scanJobResp;

        try {
            scanJobResp = mJob.updateJobStatus(req);

            if(scanJobResp.getResponse().getStatusCode() == HttpStatus.SC_ACCEPTED){
                return true;
            }
        } catch(InvalidResponseException e) {
            throw new ScanResponseException(e);
        } catch (IOException e) {
            throw new ScanException(e.getMessage());
        }
        return false;
	}

	/**
	 * スキャンジョブ再開イベントを投げます。
	 * Throws scan job resume event
	 */
	public boolean requestContinueScanJob(HashScanRequestAttributeSet attributes) throws ScanException {
        if(this.mJob == null){
            throw new ScanException("Can not cancel(), because scan is not running.");
        }

        UpdateJobStatusRequestBody body = new UpdateJobStatusRequestBody();
        body.getScanningInfo().setJobStatus(ScanJobState.PROCESSING.toString());
        body.setValidateOnly(Boolean.FALSE);

        if(attributes != null) {
            for(Attribute attr : attributes.getCollection()){
                body.getJobSetting().setValue(attr.getName(), ((ScanRequestAttribute)attr).getValue());
            }
        }

        RequestHeader header = new RequestHeader();
        header.put(RequestHeader.KEY_X_APPLICATION_ID, SmartSDKApplication.getProductId());

        Request req = new Request();
        req.setBody(body);
        req.setHeader(header);

        Response<EmptyResponseBody> scanJobResp;

        try {
            scanJobResp = mJob.updateJobStatus(req);
            if(scanJobResp.getResponse().getStatusCode() == HttpStatus.SC_ACCEPTED) {
                return true;
            }
        } catch (IOException e) {
            throw new ScanException(e.getMessage());
        } catch (InvalidResponseException e) {
            if( e.getStatusCode() == HttpStatus.SC_BAD_REQUEST) {
                for(ErrorResponseBody.Errors errors : e.getBody().getErrors()) {
                    if(errors.getMessageId().equals(EXCLUSIVE_JOB_SETTINGS_KEY)) {
                        throw new ScanAttributeException(errors.getMessage());
                    }
                }
            }
            throw new ScanResponseException(e);
        }

        return false;
	}

    /**
     * スキャンジョブ終了イベントを投げます。
     * Throws scan job end event
     */
    public boolean requestEndScanJob() throws ScanException {
        if(this.mJob == null){
            throw new ScanException("Can not cancel(), because scan is not running.");
        }

        UpdateJobStatusRequestBody body = new UpdateJobStatusRequestBody();
        body.getScanningInfo().setJobStatus(ScanJobState.COMPLETED.toString());
        body.setValidateOnly(Boolean.FALSE);

        RequestHeader header = new RequestHeader();
        header.put(RequestHeader.KEY_X_APPLICATION_ID, SmartSDKApplication.getProductId());

        Request req = new Request();
        req.setBody(body);
        req.setHeader(header);

        Response<EmptyResponseBody> scanJobResp;

        try {
            scanJobResp = mJob.updateJobStatus(req);

            if(scanJobResp.getResponse().getStatusCode() == HttpStatus.SC_ACCEPTED){
                return true;
            }
        } catch(InvalidResponseException e) {
            throw new ScanResponseException(e);
        } catch(IOException e) {
            throw new ScanException(e.getMessage());
        }
        return false;
    }

    /**
     * スキャンジョブ停止イベントを投げます。
     * Throws scan job stop event
     * 
     * @since SmartSDK V1.01
     */
    public boolean requestStopScanJob() throws ScanException {
        if(this.mJob == null){
            throw new ScanException("Can not stop(), because scan is not running.");
        }

        UpdateJobStatusRequestBody body = new UpdateJobStatusRequestBody();
        body.getScanningInfo().setJobStatus(ScanJobState.PROCESSING_STOPPED.toString());
        body.setValidateOnly(Boolean.FALSE);

        RequestHeader header = new RequestHeader();
        header.put(RequestHeader.KEY_X_APPLICATION_ID, SmartSDKApplication.getProductId());

        Request req = new Request();
        req.setBody(body);
        req.setHeader(header);

        Response<EmptyResponseBody> scanJobResp;

        try {
            scanJobResp = mJob.updateJobStatus(req);

            if(scanJobResp.getResponse().getStatusCode() == HttpStatus.SC_ACCEPTED){
                return true;
            }
        } catch(InvalidResponseException e) {
            throw new ScanResponseException(e);
        } catch(IOException e) {
            throw new ScanException(e.getMessage());
        }
        return false;
    }

	/**
	 * ジョブ情報を取得します。
	 * Obtains job information
	 *
     * @return ジョブ状態
     *         取得に失敗もしくは、内部状態として実行できない場合は空のセットが返ります。
     *         Job state.
     *         If failing to obtain information or cannot be executed as internal state,
     *         an empty set is returned.
	 */
	public ScanJobAttributeSet requestJobStates(){
		if (this.mJob == null) {
			return new HashScanJobAttributeSet();
		}

		RequestQuery query = new RequestQuery();
		query.put("includeJobSetting", "false");
		Request request = new Request();
		request.setQuery(query);

		try {
			Response<GetJobStatusResponseBody> response = mJob.getJobStatus(request);
            return jobStatusResponseToAttributes(response.getBody());

		} catch (IOException e) {
            Log.d(SmartSDKApplication.getTagName(), PREFIX + e.toString());
		} catch (InvalidResponseException e) {
            Log.d(SmartSDKApplication.getTagName(), PREFIX + e.toString());
		}
		return new HashScanJobAttributeSet();
	}

    /**
     * ジョブを破棄します。
     * ジョブが終了した際に、ジョブに関するオブジェクトを破棄します。
     * Discards the job
     * The objects related to the job are discarded at the time the job ends.
     */
    public void destroyJob() {
        FunctionMessageDispatcher.getInstance().removeAsyncJobEventListener(this);
        finishJobEventDispatcher();
        mJobEventQueue.clear();
        this.listener = null;
        this.mScanner = null;
        this.mJob = null;
    }

    /**********************************************************
     * メンバメソッド
     * private method
     **********************************************************/
	
	/**
	 * RESTジョブ情報からScanJobAttributeSetを構築します。
	 * Builds ScanJobAttributeSet from REST job information.
	 *
	 * @param response RESTジョブ情報取得結果
	 *                 REST job information obtained result
	 * @return ScanJobAttributeのセット
	 *         ScanJobAttribute set
	 */
    private ScanJobAttributeSet jobStatusResponseToAttributes(GetJobStatusResponseBody response) {
        ScanJobAttributeSet jobAttributes = new HashScanJobAttributeSet();
        if (response != null) {
            putTo(jobAttributes, ScanJobState.fromString(response.getJobStatus()));
            putTo(jobAttributes, ScanJobStateReasons.getInstance(response.getJobStatusReasons()));
            putTo(jobAttributes, ScanJobScanningInfo.getInstance(response.getScanningInfo()));
            putTo(jobAttributes, ScanJobOcringInfo.getInstance(response.getOcringInfo()));
            putTo(jobAttributes, ScanJobFilingInfo.getInstance(response.getFilingInfo()));
            putTo(jobAttributes, ScanJobSendingInfo.getInstance(response.getSendingInfo()));
            putTo(jobAttributes, ScanJobPrintingInfo.getInstance(response.getPrintingInfo()));
            putTo(jobAttributes, ScanJobStoredFileId.getInstance(response.getStoredFileId()));
        }
        return jobAttributes;
    }

    /**
	 * attributeSetに属性を追加します。attributeがnullの場合は登録されません。
	 * Adds the attribute to attributeSet. If the attribute is null, the attribute is not registered.
	 *
	 * @param attributeSet 追加属性を格納する属性セット
	 *                     Attribute set to store additional attribute
	 * @param attribute 追加する属性
	 *                  Attribute to add
	 */
	private void putTo(ScanJobAttributeSet attributeSet, ScanJobAttribute attribute) {
		if (attribute != null) {
			attributeSet.add(attribute);
		}
	}

    /**
     * 指定したジョブ要求属性から、RESTアクセスする際のリクエストを生成します。
     * Creates the request for accessing REST from the specified job request attribute.
     */
    private CreateJobRequestBody getCreateJobRequestBody(ScanRequestAttributeSet attributes){
        CreateJobRequestBody body = new CreateJobRequestBody();

        if( attributes == null ) {
            return body;
        }

        for(Attribute attr : attributes.getCollection()){
            body.getJobSetting().setValue(attr.getName(), ((ScanRequestAttribute)attr).getValue());
        }

        return body;
    }

    /**********************************************************
     * SDKサービスからの非同期イベント発行(FunctionMessageDispatcher経由)
     * Issue asynchronous event from SDK service (via FunctionMessageDispatcher)
     **********************************************************/

	@Override
	public void onReceiveJobEvent(GetJobStatusResponseBody event) {
	    try {
            mJobEventQueue.put(event);
        } catch (InterruptedException e) {
            Log.d(SmartSDKApplication.getTagName(), PREFIX + e.toString());
        }
	}

	@Override
	public String getJobId() {
        if(this.listener == null) return null;
		return this.listener.getJobId();
	}

	/**
	 * ジョブイベント配信を開始します。
	 * Starts job event delivery.
	 *
	 * @param jobId
	 * @throws NullPointerException jobId がnullの場合
	 *                              When jobId is null
	 */
	private void startJobEventDispatcher(String jobId) {
	    finishJobEventDispatcher();
	    mJobEventDispatcher = new JobEventDispatcher(jobId);
	    mJobEventDispatcher.start();
	}

	/**
	 * ジョブイベント配信を終了します。
	 * Ends job event delivery.
	 */
	private void finishJobEventDispatcher() {
	    if (mJobEventDispatcher != null) {
	        mJobEventDispatcher.cancel();
	        mJobEventDispatcher = null;
	    }
	}

	/**
	 * ジョブイベントを配信するスレッドクラスです。
	 * ジョブ開始要求の応答レスポンスが返ってくる前に受信したジョブイベントをキューイングしておき、
	 * ジョブ開始確定後に、キューイングしたイベントのリスナ通知を開始します。
	 * The thread class to deliver job events.
	 * Queues the job event received before the response for job start request is returned,
	 * and starts listener notification of the queued event after the job start is confirmed.
	 */
	private class JobEventDispatcher extends Thread {

	    private final String mJobId;

	    private volatile boolean mCanceled = false;

	    JobEventDispatcher(String jobId) {
	        if (jobId == null) {
	            throw new NullPointerException("jobId must not be null.");
	        }
	        mJobId = jobId;
	    }

	    @Override
	    public void run() {
	        Log.d(SmartSDKApplication.getTagName(), PREFIX + "scan job event dispatcher start (" + mJobId + ")");

	        while (!mCanceled) {
	            GetJobStatusResponseBody event;
	            try {
	                event = mJobEventQueue.take();

	                // only own jobId
	                if (!mJobId.equals(event.getJobId())) {
	                    continue;
	                }

	                if (listener != null) {
	                    listener.onChangeJobStatus(jobStatusResponseToAttributes(event));
	                }

	            } catch (InterruptedException ignore) {
	            }
	        }

	        Log.d(SmartSDKApplication.getTagName(), PREFIX + "scan job event dispatcher finish (" + mJobId + ")");
	    }

	    void cancel() {
	        mCanceled = true;
	        interrupt();
	    }

	}

}
