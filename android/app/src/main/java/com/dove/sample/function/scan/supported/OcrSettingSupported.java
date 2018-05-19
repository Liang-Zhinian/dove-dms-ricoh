/*
 *  Copyright (C) 2013-2015 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.function.scan.supported;

import java.util.List;

import com.dove.sample.function.common.Conversions;
import com.dove.sample.function.scan.attribute.standard.OcrSetting;
import com.dove.sample.function.scan.attribute.standard.OcrSetting.BlankPageSensitivityLevel;
import com.dove.sample.function.scan.attribute.standard.OcrSetting.OcrLanguage;
import com.dove.sample.wrapper.rws.service.scanner.Capability;

public final class OcrSettingSupported {

    private final List<OcrSetting.OcrLanguage> supportedOcrLanguages;
    private final List<Boolean> supportedOmitBlankPages;
    private final List<OcrSetting.BlankPageSensitivityLevel> supportedBlankPageSensitivityLevels;
    private final List<Boolean> supportedAutoFileNames;
    private final List<Boolean> supportedVerticalJudgments; //SmartSDK V2.00

    public OcrSettingSupported(Capability.OcrSettingCapability capability) {
    	supportedOcrLanguages = OcrLanguage.getSupportedValue(capability.getOcrLanguageList());
        supportedOmitBlankPages = Conversions.getList(capability.getOmitBlankPageList());
        supportedBlankPageSensitivityLevels = BlankPageSensitivityLevel.getSupportedValue(capability.getBlankPageSensitivityLevelList());
        supportedAutoFileNames = Conversions.getList(capability.getAutoFileNameList());
        supportedVerticalJudgments = Conversions.getList(capability.getVerticalJudgmentList());
    }

    public List<OcrSetting.OcrLanguage> getSupportedOcrLanguages() {
        return supportedOcrLanguages;
    }

    public List<Boolean> getSupportedOmitBlankPages() {
        return supportedOmitBlankPages;
    }

    public List<OcrSetting.BlankPageSensitivityLevel> getSupportedBlankPageSensitivityLevels() {
        return supportedBlankPageSensitivityLevels;
    }

    public List<Boolean> getSupportedAutoFileNames() {
        return supportedAutoFileNames;
    }

    /*
     * @since SmartSDK V2.00
     */
    public List<Boolean> getSupportedVerticalJudgments() {
        return supportedVerticalJudgments;
    }

}
