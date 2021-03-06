/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.function.scan;

import android.util.Log;

import com.dove.sample.function.common.SmartSDKApplication;
//import com.dove.sample.network.ApiNetwork;
import com.dove.sample.wrapper.common.BinaryResponseBody;
import com.dove.sample.wrapper.common.InvalidResponseException;
import com.dove.sample.wrapper.common.Request;
import com.dove.sample.wrapper.common.RequestQuery;
import com.dove.sample.wrapper.common.Response;
import com.dove.sample.wrapper.rws.service.scanner.FilePathResponseBody;
import com.dove.sample.wrapper.rws.service.scanner.Job;

import java.io.IOException;
import java.io.InputStream;

/**
 * スキャン画像に関する操作機能を提供します。
 * Provides the functions to operate scanned images.
 */
public class ScanImage {
    private Job mJob = null;
    
    /**
     * Define the prefix for log information with abbreviation package and class name
     */
    private final static String PREFIX = "scan:ScanImg:";

    /**
     * ジョブを指定して画像操作オブジェクトを生成します。
     * ジョブは正常に終了した場合だけ、画像操作を行えます。
     * 正常に終了していない場合(ジョブ実行中/ジョブ異常終了など)は、本オブジェクトメソッドを実行しても
     * 操作に失敗します。
     * Specifies a job and creates the image operation object.
     * Image can be operated only when the job ends successfully.
     * If the job does not end correctly (e.g. job processing, job ended with error),
     * the operation fails even if this object method is executed.
     *
     * @param scanJob
     */
    public ScanImage(ScanJob scanJob) {
        this.mJob = new Job(scanJob.getCurrentJobId());
    }


    /**
     * ページ単位でスキャン画像を保存しているパスを取得します。
     * 本APIはMultiLink-Panelでのみ動作します。
     * 取得するパスは絶対パスで取得できます。
     * 取得に失敗した場合は、nullが返ります。
     * Obtains the path which stores the scanned images by page.
     * This API runs only on MultiLink-Panel.
     * The path can be obtained in absolute path.
     * If failing to obtain the path, null is returned.
     *
     * @param pageNo 取得するページ番号
     *               Page number to obtain
     * @return スキャン画像を保存しているパス
     *         The path to save scanned images
     */
    public String getImageFilePath(int pageNo){
        RequestQuery query = new RequestQuery();
        query.put("pageNo", Integer.toString(pageNo));
        query.put("getMethod", "filePath");

        Request req = new Request();
        req.setQuery(query);

        try{
            Response<FilePathResponseBody> response = mJob.getFilePath(req);
            return response.getBody().getFilePath();
        }catch( IOException ex ) {
            Log.d(SmartSDKApplication.getTagName(), PREFIX + ex.toString());
        }catch( InvalidResponseException ex) {
            Log.d(SmartSDKApplication.getTagName(), PREFIX + ex.toString());
        }
        return null;
    }
    
    /**
     * @since SmartSDK V2.12
     */
    public String getImageFilePathByDividedFileNo(int dividedFileNo){
    	RequestQuery query = new RequestQuery();
        query.put("dividedFileNo", Integer.toString(dividedFileNo));
        query.put("getMethod", "filePath");

        Request req = new Request();
        req.setQuery(query);

        try{
            Response<FilePathResponseBody> response = mJob.getFilePath(req);
            return response.getBody().getFilePath();
        }catch( IOException ex ) {
            Log.d(SmartSDKApplication.getTagName(), PREFIX + ex.toString());
        }catch( InvalidResponseException ex) {
            Log.d(SmartSDKApplication.getTagName(), PREFIX + ex.toString());
        }
        return null;
    }

    /**
     * ページ単位でスキャン画像をインプットストリームとして取得します。
     * 取得に失敗した場合は、nullが返ります。
     * Obtains the scanned images as input stream by page.
     * If failing to obtain the images, null is returned.
     *
     * @param  pageNo 取得するページ番号
     *               Page number to obtain
     * @return スキャン画像のインプットストリーム
     *         Input stream for scanned image
     */
    public InputStream getImageInputStream(int pageNo) {
        RequestQuery query = new RequestQuery();
        query.put("pageNo", Integer.toString(pageNo));
        query.put("getMethod", "direct");

        Request req = new Request();
        req.setQuery(query);

        try{
            Response<BinaryResponseBody> response = mJob.getFile(req);
            return response.getBody().getInputStream();
        }catch(IOException ex) {
            Log.d(SmartSDKApplication.getTagName(), PREFIX + ex.toString());
        }catch(InvalidResponseException ex){
            Log.d(SmartSDKApplication.getTagName(), PREFIX + ex.toString());
        }

        return null;
    }

    public Response<BinaryResponseBody> getImageBinaryResponse() {
        RequestQuery query = new RequestQuery();
//        query.put("pageNo", Integer.toString(pageNo));
        query.put("getMethod", "direct");
        Request request = new Request();
        request.setQuery(query);

        try{
            Response<BinaryResponseBody> response = mJob.getFile(request);
            return response;
        } catch (InvalidResponseException e) {
            Log.d(SmartSDKApplication.getTagName(), PREFIX + e.toString());
        } catch (IOException e) {
            Log.d(SmartSDKApplication.getTagName(), PREFIX + e.toString());
        }

        return null;
    }
    
    /**
     * @since SmartSDK V2.12
     */
    public InputStream getImageInputStreamByDividedFileNo(int dividedFileNo){
        RequestQuery query = new RequestQuery();
        query.put("dividedFileNo", Integer.toString(dividedFileNo));
        query.put("getMethod", "direct");

        Request req = new Request();
        req.setQuery(query);

        try{
            Response<BinaryResponseBody> response = mJob.getFile(req);
            return response.getBody().getInputStream();
        }catch(IOException ex) {
            Log.d(SmartSDKApplication.getTagName(), PREFIX + ex.toString());
        }catch(InvalidResponseException ex){
            Log.d(SmartSDKApplication.getTagName(), PREFIX + ex.toString());
        }

        return null;
    }

    /**
     * スキャン結果の画像を機器から削除します。
     * 本操作を行った後に、画像を復旧する手段は提供しません。
     * Deletes the scanned image from device.
     * After this operation, there is no method to recover the image.
     * @return
     */
    public boolean deleteImage() {
        Request request = new Request();

        try{
            mJob.deleteFile(request);
            return true;
        }catch(IOException ex){
            Log.d(SmartSDKApplication.getTagName(), PREFIX + ex.toString());
        }catch(InvalidResponseException ex){
            Log.d(SmartSDKApplication.getTagName(), PREFIX + ex.toString());
        }

        return false;
    }
}
