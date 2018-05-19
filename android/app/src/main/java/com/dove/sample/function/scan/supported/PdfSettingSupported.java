/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.function.scan.supported;

import java.util.List;

import com.dove.sample.function.common.Conversions;
import com.dove.sample.wrapper.rws.service.scanner.Capability;

public final class PdfSettingSupported {

    private final List<Boolean> supportedPdfA;
    private final List<Boolean> supportedHighCompressionPdf;
    private final List<Boolean> supportedHighCompressionPdfHiqualityModes; //SmartSDK V2.12
    private final List<Boolean> supportedDigitalSignaturePdf;

    public PdfSettingSupported(Capability.PdfSettingCapability capability) {
        this.supportedPdfA				 	= Conversions.getList(capability.getPdfAList());
        this.supportedHighCompressionPdf	= Conversions.getList(capability.getHighCompressedPdfList());
        this.supportedHighCompressionPdfHiqualityModes = Conversions.getList(capability.getHighCompressionPdfHiqualityModeList());
        this.supportedDigitalSignaturePdf	= Conversions.getList(capability.getDigitalSignaturePdfList());
    }

    public List<Boolean> getSupportedPdfA() {
    	return supportedPdfA;
    }
    
    public List<Boolean> getSupportedHighCompressionPdf() {
    	return supportedHighCompressionPdf;
    }
    
    /**
     * @since SmartSDK V2.12
     */
    public List<Boolean> getSupportedHighCompressionPdfHiqualityModes() {
    	return supportedHighCompressionPdfHiqualityModes;
    }
    
    public List<Boolean> supportedDigitalSignaturePdf() {
    	return supportedDigitalSignaturePdf;
    }
    
}
