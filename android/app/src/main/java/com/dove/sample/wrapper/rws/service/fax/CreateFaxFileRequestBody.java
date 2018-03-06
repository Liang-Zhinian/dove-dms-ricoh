/*
 *  Copyright (C) 2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.service.fax;

import java.io.InputStream;

import com.dove.sample.wrapper.common.BinaryRequestBody;

/*
 * @since SmartSDK V2.12
 */
public class CreateFaxFileRequestBody implements BinaryRequestBody {

	private static final String CONTENT_TYPE_STREAM = "application/octet-stream";

    private InputStream mInputStream;
    private int mFileSize;

	public CreateFaxFileRequestBody(InputStream is, int size) {
        this.mInputStream = is;
        this.mFileSize = size;
	}

	@Override
	public String getContentType() {
		return CreateFaxFileRequestBody.CONTENT_TYPE_STREAM;
	}

	@Override
	public String toEntityString() {
		return null;
	}

    @Override
    public InputStream getInputStream() {
        return this.mInputStream;
    }

    @Override
    public int getSize() {
        return this.mFileSize;
    }
}
