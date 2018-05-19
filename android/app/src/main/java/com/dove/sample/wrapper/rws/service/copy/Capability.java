/*
 *  Copyright (C) 2013-2017 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.service.copy;

import java.util.List;
import java.util.Map;

import com.dove.sample.wrapper.common.ArrayElement;
import com.dove.sample.wrapper.common.CapabilityElement;
import com.dove.sample.wrapper.common.MagnificationElement;
import com.dove.sample.wrapper.common.MaxLengthElement;
import com.dove.sample.wrapper.common.RangeElement;

public class Capability extends CapabilityElement {

	private static final String KEY_AUTO_CORRECT_JOB_SETTING_LIST                   = "autoCorrectJobSettingList";
	private static final String KEY_JOB_MODE_LIST                                   = "jobModeList";
	private static final String KEY_JOB_STOPPED_TIMEOUT_PERIOD_RANGE                = "jobStoppedTimeoutPeriodRange"; // SmartSDK V1.02
	private static final String KEY_ORIGINAL_SIZE_LIST                              = "originalSizeList";
	private static final String KEY_ORIGINAL_SIZE_CUSTOM_X_RANGE                    = "originalSizeCustomXRange";
	private static final String KEY_ORIGINAL_SIZE_CUSTOM_Y_RANGE                    = "originalSizeCustomYRange";
	private static final String KEY_ORIGINAL_SIZE_CUSTOM_HORIZONTAL_RANGE           = "originalSizeCustomHorizontalRange"; // SmartSDK V2.12
	private static final String KEY_ORIGINAL_SIZE_CUSTOM_VERTICAL_RANGE             = "originalSizeCustomVerticalRange"; // SmartSDK V2.12
	private static final String KEY_SCAN_METHOD_LIST                                = "scanMethodList"; // SmartSDK V2.12
	private static final String KEY_ORIGINAL_SIDE_LIST                              = "originalSideList";
	private static final String KEY_ORIGINAL_ORIENTATION_LIST                       = "originalOrientationList";
	private static final String KEY_ORIGINAL_REVERSE_ORIENTATION_LIST               = "originalReverseOrientationList"; // SmartSDK V2.12
	private static final String KEY_ORIGINAL_INSERT_EDGE_LIST                       = "originalInsertEdgeList"; // SmartSDK V2.12
	private static final String KEY_ORIGINAL_TYPE_LIST                              = "originalTypeList";
	private static final String KEY_DARK_BACKGROUND_LEVEL_LIST                      = "darkBackgroundLevelList";  //SmartSDK v2.30
	private static final String KEY_WIDE_SCAN_LIST                                  = "wideScanList"; // SmartSDK V2.12
	private static final String KEY_HIGH_SPEED_COPY_LIST                            = "highSpeedCopyList"; // SmartSDK V2.12
	private static final String KEY_PRINT_COLOR_LIST                                = "printColorList";
	private static final String KEY_SPECIAL_COLOR_SETTING_CAPABILITY                = "specialColorSettingCapability";
	private static final String KEY_COPIES_RANGE                                    = "copiesRange";
	private static final String KEY_SHEET_COLLATE_LIST                              = "sheetCollateList";
	private static final String KEY_PRINT_SIDE_LIST                                 = "printSideList";
	private static final String KEY_COMBINE_LIST                                    = "combineList";
	private static final String KEY_COMBINE_ORDER_LIST                              = "combineOrderList";
	private static final String KEY_COMBINE_SEPARATOR_LINE_LIST                     = "combineSeparatorLineList";
	private static final String KEY_COMBINE_SEPARATOR_LINE_SETTING_CAPABILITY       = "combineSeparatorLineSettingCapability";
	private static final String KEY_MAGNIFICATION_RANGE                             = "magnificationRange";
	private static final String KEY_CREATE_MARGIN_LIST                              = "createMarginList"; // SmartSDK V2.12
	private static final String KEY_FINE_MAGNIFICATION_LIST                         = "fineMagnificationList"; // SmartSDK V2.12
	private static final String KEY_FINE_MAGNIFICATION_SETTING_CAPABILITY           = "fineMagnificationSettingCapability"; // SmartSDK V2.12
	private static final String KEY_PAPER_TRAY_LIST                                 = "paperTrayList";
	private static final String KEY_PAPER_CUT_LIST                                  = "paperCutList"; // SmartSDK V2.12
	private static final String KEY_PRESET_CUT_SIZE_INFO_LIST                       = "presetCutSizeInfoList"; // SmartSDK V2.12
	private static final String KEY_VARIABLE_CUT_SETTING_CAPABILITY                 = "variableCutSettingCapability"; // SmartSDK V2.12
	private static final String KEY_AUTO_DENSITY_LIST                               = "autoDensityList";
	private static final String KEY_MANUAL_DENSITY_RANGE                            = "manualDensityRange";
	private static final String KEY_STAPLE_LIST                                     = "stapleList";
	private static final String KEY_PUNCH_LIST                                      = "punchList";
	private static final String KEY_ERASE_CENTER_LIST                               = "eraseCenterList";
	private static final String KEY_ERASE_CENTER_SETTING_CAPABILITY                 = "eraseCenterSettingCapability";
	private static final String KEY_ERASE_BORDER_LIST                               = "eraseBorderList";
	private static final String KEY_ERASE_BORDER_SETTING_CAPABILITY                 = "eraseBorderSettingCapability";
	private static final String KEY_MARGIN_LIST                                     = "marginList";
	private static final String KEY_MARGIN_SETTING_CAPABILITY                       = "marginSettingCapability";
	private static final String KEY_CENTERING_LIST                                  = "centeringList";
	private static final String KEY_ERASE_COLOR_LIST                                = "eraseColorList";
	private static final String KEY_ERASE_COLOR_SETTING_LIST                        = "eraseColorSettingList";
	private static final String KEY_ERASE_INSIDE_1_LIST                             = "eraseInside1List"; // SmartSDK V2.12
	private static final String KEY_ERASE_INSIDE_2_LIST                             = "eraseInside2List"; // SmartSDK V2.12
	private static final String KEY_ERASE_INSIDE_3_LIST                             = "eraseInside3List"; // SmartSDK V2.12
	private static final String KEY_ERASE_INSIDE_4_LIST                             = "eraseInside4List"; // SmartSDK V2.12
	private static final String KEY_ERASE_INSIDE_5_LIST                             = "eraseInside5List"; // SmartSDK V2.12
	private static final String KEY_ERASE_INSIDE_1_SETTING_CAPABILITY               = "eraseInside1SettingCapability"; // SmartSDK V2.12
	private static final String KEY_ERASE_INSIDE_2_SETTING_CAPABILITY               = "eraseInside2SettingCapability"; // SmartSDK V2.12
	private static final String KEY_ERASE_INSIDE_3_SETTING_CAPABILITY               = "eraseInside3SettingCapability"; // SmartSDK V2.12
	private static final String KEY_ERASE_INSIDE_4_SETTING_CAPABILITY               = "eraseInside4SettingCapability"; // SmartSDK V2.12
	private static final String KEY_ERASE_INSIDE_5_SETTING_CAPABILITY               = "eraseInside5SettingCapability"; // SmartSDK V2.12
	private static final String KEY_ERASE_OUTSIDE_LIST                              = "eraseOutsideList"; // SmartSDK V2.12
	private static final String KEY_ERASE_OUTSIDE_SETTING_CAPABILITY                = "eraseOutsideSettingCapability"; // SmartSDK V2.12
	private static final String KEY_PARTIAL_COPY_LIST                               = "partialCopyList"; // SmartSDK V2.12
	private static final String KEY_PARTIAL_COPY_SETTING_CAPABILITY                 = "partialCopySettingCapability"; // SmartSDK V2.12
	private static final String KEY_ADJUST_PRINT_POSITION_LIST                      = "adjustPrintPositionList"; // SmartSDK V2.12
	private static final String KEY_ADJUST_PRINT_POSITION_SETTING_CAPABILITY        = "adjustPrintPositionSettingCapability"; // SmartSDK V2.12
	private static final String KEY_MARGIN_ADJUST_LIST                              = "marginAdjustList"; // SmartSDK V2.12
	private static final String KEY_MARGIN_ADJUST_SETTING_CAPABILITY                = "marginAdjustSettingCapability"; // SmartSDK V2.12
	private static final String KEY_BACKGROUND_NUMBERING_LIST                       = "backgroundNumberingList"; // SmartSDK V2.12
	private static final String KEY_BACKGROUND_NUMBERING_SETTING_CAPABILITY         = "backgroundNumberingSettingCapability"; // SmartSDK V2.12
	private static final String KEY_PRESET_STAMP_LIST                               = "presetStampList";
	private static final String KEY_PRESET_STAMP_MAX_NUM                            = "presetStampMaxNum"; // SmartSDK V2.12
	private static final String KEY_PRESET_STAMP_SETTING_CAPABILITY                 = "presetStampSettingCapability";
	private static final String KEY_USER_STAMP_LIST                                 = "userStampList";
	private static final String KEY_USER_STAMP_MAX_NUM                              = "userStampMaxNum"; // SmartSDK V2.12
	private static final String KEY_USER_STAMP_SETTING_CAPABILITY                   = "userStampSettingCapability";
	private static final String KEY_DATE_STAMP_LIST                                 = "dateStampList";
	private static final String KEY_DATE_STAMP_SETTING_CAPABILITY                   = "dateStampSettingCapability";
	private static final String KEY_PAGE_STAMP_LIST                                 = "pageStampList";
	private static final String KEY_PAGE_STAMP_SETTING_CAPABILITY                   = "pageStampSettingCapability";
	private static final String KEY_TEXT_STAMP_LIST                                 = "textStampList";
	private static final String KEY_TEXT_STAMP_SETTING_CAPABILITY                   = "textStampSettingCapability";
	private static final String KEY_POSTER_LIST                                     = "posterList"; // SmartSDK V2.12
	private static final String KEY_DOUBLE_COPIES_LIST                              = "doubleCopiesList"; // SmartSDK V2.12
	private static final String KEY_DOUBLE_COPIES_SEPARATOR_LINE_LIST               = "doubleCopiesSeparatorLineList"; // SmartSDK V2.12
	private static final String KEY_DOUBLE_COPIES_SEPARATOR_LINE_SETTING_CAPABILITY = "doubleCopiesSeparatorLineSettingCapability"; // SmartSDK V2.12
	private static final String KEY_COLOR_BALANCE_YELLOW_RANGE                      = "colorBalanceYellowRange"; // SmartSDK V2.12
	private static final String KEY_COLOR_BALANCE_MAGENTA_RANGE                     = "colorBalanceMagentaRange"; // SmartSDK V2.12
	private static final String KEY_COLOR_BALANCE_CYAN_RANGE                        = "colorBalanceCyanRange"; // SmartSDK V2.12
	private static final String KEY_COLOR_BALANCE_BLACK_RANGE                       = "colorBalanceBlackRange"; // SmartSDK V2.12
	private static final String KEY_ADJUST_COLOR_LIST                               = "adjustColorList"; // SmartSDK V2.12
	private static final String KEY_ADJUST_COLOR_SETTING_CAPABILITY                 = "adjustColorSettingCapability"; // SmartSDK V2.12
	private static final String KEY_UNAUTHORIZED_COPY_PREVENTION_LIST               = "unauthorizedCopyPreventionList"; // SmartSDK V2.12
	private static final String KEY_UNAUTHORIZED_COPY_PREVENTION_SETTING_CAPABILITY = "unauthorizedCopyPreventionSettingCapability"; // SmartSDK V2.12
	private static final String KEY_HALF_FOLD_LIST                                  = "halfFoldList"; // SmartSDK V2.12
	private static final String KEY_HALF_FOLD_SETTING_CAPABILITY                    = "halfFoldSettingCapability"; // SmartSDK V2.12
	private static final String KEY_SAMPLE_COPY_LIST                                = "sampleCopyList";  //SmartSDK v2.30
	private static final String KEY_3_EDGES_FULL_BLEED_LIST                         = "3EdgesFullBleedList";  //SmarSDK v2.30
	private static final String KEY_THIN_PAPER_LIST                                 = "thinPaperList"; //SmartSDK v2.40
	private static final String KEY_MULTI_FOLD_LIST                                 = "multiFoldList";  //SmartSDK v2.30
	private static final String KEY_MULTI_FOLD_SETTING_CAPABILITY                   = "multiFoldSettingCapability";  //SmartSDK v2.30
	private static final String KEY_ADJUST_PRINT_DENSITY_RANGE                      = "adjustPrintDensityRange"; //SmartSDK v2.40  
	private static final String KEY_ADJUST_SCAN_POSITION_LIST                       = "adjustScanPositionList"; //SmartSDK v2.40
	private static final String KEY_ADJUST_SCAN_POSITION_SETTING_CAPABILITY         = "adjustScanPositionSettingCapability"; // SmartSDK v2.40
	private static final String KEY_CREEP_ADJUSTMENT_LIST                           = "creepAdjustmentList"; // SmartSDK v2.40
	private static final String KEY_CREEP_ADJUSTMENT_SETTING_CAPABILITY             = "creepAdjustmentSettingCapability"; // SmartSDK v2.40
	private static final String KEY_INSERT_SEPARATION_SHEET_LIST                    = "insertSeparationSheetList"; // SmartSDK v2.40
	private static final String KEY_CHANGE_INSERT_POSITION_RANGE                    = "changeInsertPositionRange"; // SmartSDK v2.40
	private static final String KEY_REVERSE_EJECTION_LIST                           = "reverseEjectionList"; //SmartSDK v2.40
	 

	public Capability(Map<String, Object> values) {
		super(values);
	}

	/*
	 * autoCorrectJobSettingList (Array[Boolean])
	 */
	public List<Boolean> getAutoCorrectJobSettingList() {
		return getArrayValue(KEY_AUTO_CORRECT_JOB_SETTING_LIST);
	}

	/*
	 * jobModeList (Array[String])
	 */
	public List<String> getJobModeList() {
		return getArrayValue(KEY_JOB_MODE_LIST);
	}

	/*
	 * jobStoppedTimeoutPeriodRange (Range)
	 * @since SmartSDK V1.02
	 */
	public RangeElement getJobStoppedTimeoutPeriodRange() {
		return getRangeValue(KEY_JOB_STOPPED_TIMEOUT_PERIOD_RANGE);
	}

	/*
	 * originalSizeList (Array[String])
	 */
	public List<String> getOriginalSizeList() {
		return getArrayValue(KEY_ORIGINAL_SIZE_LIST);
	}

	/*
	 * originalSizeCustomXRange (Range)
	 */
	public RangeElement getOriginalSizeCustomXRange() {
		return getRangeValue(KEY_ORIGINAL_SIZE_CUSTOM_X_RANGE);
	}

	/*
	 * originalSizeCustomYRange (Range)
	 */
	public RangeElement getOriginalSizeCustomYRange() {
		return getRangeValue(KEY_ORIGINAL_SIZE_CUSTOM_Y_RANGE);
	}

	/*
	 * originalSizeCustomHorizontalRange (Range)
	 * @since SmartSDK V2.12
	 */
	public RangeElement getOriginalSizeCustomHorizontalRange() {
		Map<String, Object> value = getObjectValue(KEY_ORIGINAL_SIZE_CUSTOM_HORIZONTAL_RANGE);
		if (value == null) {
			return null;
		}
		return new RangeElement(value);
	}

	/*
	 * originalSizeCustomVerticalRange (Range)
	 * @since SmartSDK V2.12
	 */
	public RangeElement getOriginalSizeCustomVerticalRange() {
		Map<String, Object> value = getObjectValue(KEY_ORIGINAL_SIZE_CUSTOM_VERTICAL_RANGE);
		if (value == null) {
			return null;
		}
		return new RangeElement(value);
	}

	/*
	 * scanMethodList (Array[String])
	 * @since SmartSDK V2.12
	 */
	public List<String> getScanMethodList() {
		return getArrayValue(KEY_SCAN_METHOD_LIST);
	}

	/*
	 * originalSideList (Array[String])
	 */
	public List<String> getOriginalSideList() {
		return getArrayValue(KEY_ORIGINAL_SIDE_LIST);
	}

	/*
	 * originalOrientationList (Array[String])
	 */
	public List<String> getOriginalOrientationList() {
		return getArrayValue(KEY_ORIGINAL_ORIENTATION_LIST);
	}

	/*
	 * originalReverseOrientationList (Array[Boolean])
	 * @since SmartSDK V2.12
	 */
	public List<Boolean> getOriginalReverseOrientationList() {
		return getArrayValue(KEY_ORIGINAL_REVERSE_ORIENTATION_LIST);
	}

	/*
	 * originalInsertEdgeList (Array[String])
	 * @since SmartSDK V2.12
	 */
	public List<String> getOriginalInsertEdgeList() {
		return getArrayValue(KEY_ORIGINAL_INSERT_EDGE_LIST);
	}

	/*
	 * originalTypeList (Array[String])
	 */
	public List<String> getOriginalTypeList() {
		return getArrayValue(KEY_ORIGINAL_TYPE_LIST);
	}
	
	/*
	 * darkBackgroundLevelList (Array[String])
	 * @since SmartSDK v2.30
	 */
	public List<String> getDarkBackgroundLevelList() {
		return getArrayValue(KEY_DARK_BACKGROUND_LEVEL_LIST);
	}
	
	/*
	 * wideScanList (Array[Boolean]))
	 * @since SmartSDK V2.12
	 */
	public List<Boolean> getWideScanList() {
		return getArrayValue(KEY_WIDE_SCAN_LIST);
	}

	/*
	 * highSpeedCopyList (Array[Boolean])
	 * @since SmartSDK V2.12
	 */
	public List<Boolean> getHighSpeedCopyList() {
		return getArrayValue(KEY_HIGH_SPEED_COPY_LIST);
	}

	/*
	 * printColorList (Array[String])
	 */
	public List<String> getPrintColorList() {
		return getArrayValue(KEY_PRINT_COLOR_LIST);
	}

	/*
	 * specialColorSettingCapability (Object)
	 */
	public SpecialColorSettingCapability getSpecialColorSetting() {
		Map<String, Object> mapValue = getObjectValue(KEY_SPECIAL_COLOR_SETTING_CAPABILITY);
		if (mapValue == null) {
			return null;
		}
		return new SpecialColorSettingCapability(mapValue);
	}

	/*
	 * copiesRange (Range)
	 */
	public RangeElement getCopiesRange() {
		return getRangeValue(KEY_COPIES_RANGE);
	}

	/*
	 * sheetCollateList (Array[String])
	 */
	public List<String> getSheetCollateList() {
		return getArrayValue(KEY_SHEET_COLLATE_LIST);
	}

	/*
	 * printSideList (Array[String])
	 */
	public List<String> getPrintSideList() {
		return getArrayValue(KEY_PRINT_SIDE_LIST);
	}

	/*
	 * combineList (Array[String])
	 */
	public List<String> getCombineList() {
		return getArrayValue(KEY_COMBINE_LIST);
	}

	/*
	 * combineOrderList (Array[String])
	 */
	public List<String> getCombineOrderList() {
		return getArrayValue(KEY_COMBINE_ORDER_LIST);
	}

	/*
	 * combineSeparatorLineList (Array[Boolean])
	 */
	public List<Boolean> getCombineSeparatorLineList() {
		return getArrayValue(KEY_COMBINE_SEPARATOR_LINE_LIST);
	}

	/*
	 * combineSeparatorLineSettingCapability (Object)
	 */
	public CombineSeparatorLineSettingCapability getCombineSeparatorLineSettingCapability() {
		Map<String, Object> value = getObjectValue(KEY_COMBINE_SEPARATOR_LINE_SETTING_CAPABILITY);
		if (value == null) {
			return null;
		}
		return new CombineSeparatorLineSettingCapability(value);
	}

	/*
	 * magnificationRange (Magnification)
	 */
	public MagnificationElement getMagnificationRange() {
		return getMagnificationValue(KEY_MAGNIFICATION_RANGE);
	}

	/*
	 * createMarginList (Array[Boolean])
	 * @since SmartSDK V2.12
	 */
	public List<Boolean> getCreateMarginList() {
		return getArrayValue(KEY_CREATE_MARGIN_LIST);
	}
	
	/*
	 *  fineMagnificationList (Array[Boolean])
	 *  @Since SmartSDK V2.12
	 */
	public List<Boolean> getFineMagnificationList() {
		return getArrayValue(KEY_FINE_MAGNIFICATION_LIST);
	}
	
	
	/*
	 *  fineMagnificationSettingCapability (Object)
	 *  @Since SmartSDK V2.12
	 */
	public FineMagnificationSettingCapability getFineMagnificationSettingCapability() {
		Map<String, Object> mapValue = getObjectValue(KEY_FINE_MAGNIFICATION_SETTING_CAPABILITY);
		if (mapValue == null) {
			return null;
		}
		return new FineMagnificationSettingCapability(mapValue);
	}
	
		
	/*
	 * paperTrayList (Array[String])
	 */
	public List<String> getPaperTrayList() {
		return getArrayValue(KEY_PAPER_TRAY_LIST);
	}

	/*
	 * paperCutList (Array[String])
	 * @since SmartSDK V2.12
	 */
	public List<String> getPaperCutList() {
		return getArrayValue(KEY_PAPER_CUT_LIST);
	}

	/*
	 * presetCutSizeInfoList (Array[Object])
	 * @since SmartSDK V2.12
	 */
	public PresetCutSizeInfoList getPresetCutSizeInfoList() {
		List<Map<String, Object>> value = getArrayValue(KEY_PRESET_CUT_SIZE_INFO_LIST);
		if (value == null) {
			return null;
		}
		return new PresetCutSizeInfoList(value);
	}

	/*
	 * variableCutSettingCapability (Object)
	 * @since SmartSDK V2.12
	 */
	public VariableCutSettingCapability getVariableCutSettingCapability() {
		Map<String, Object> value = getObjectValue(KEY_VARIABLE_CUT_SETTING_CAPABILITY);
		if (value == null) {
			return null;
		}
		return new VariableCutSettingCapability(value);
	}

	/*
	 * autoDensityList (Array[Boolean])
	 */
	public List<Boolean> getAutoDensityList() {
		return getArrayValue(KEY_AUTO_DENSITY_LIST);
	}

	/*
	 * manualDensityRange (Range)
	 */
	public RangeElement getManualDensityRange() {
		return getRangeValue(KEY_MANUAL_DENSITY_RANGE);
	}

	/*
	 * stapleList (Array[String])
	 */
	public List<String> getStapleList() {
		return getArrayValue(KEY_STAPLE_LIST);
	}

	/*
	 * punchList (Array[String])
	 */
	public List<String> getPunchList() {
		return getArrayValue(KEY_PUNCH_LIST);
	}

	/*
	 * eraseCenterList (Array[Boolean])
	 */
	public List<Boolean> getEraseCenterList() {
		return getArrayValue(KEY_ERASE_CENTER_LIST);
	}

	/*
	 * eraseCenterSettingCapability (Object)
	 */
	public EraseCenterSettingCapability getEraseCenterSettingCapability() {
		Map<String, Object> value = getObjectValue(KEY_ERASE_CENTER_SETTING_CAPABILITY);
		if (value == null) {
			return null;
		}
		return new EraseCenterSettingCapability(value);
	}

	/*
	 * eraseBorderList (Array[Boolean])
	 */
	public List<Boolean> getEraseBorderList() {
		return getArrayValue(KEY_ERASE_BORDER_LIST);
	}

	/*
	 * eraseBorderSettingCapability (Object)
	 */
	public EraseBorderSettingCapability getEraseBorderSettingCapability() {
		Map<String, Object> value = getObjectValue(KEY_ERASE_BORDER_SETTING_CAPABILITY);
		if (value == null) {
			return null;
		}
		return new EraseBorderSettingCapability(value);
	}

	/*
	 * marginList (Array[Boolean])
	 */
	public List<Boolean> getMarginList() {
		return getArrayValue(KEY_MARGIN_LIST);
	}

	/*
	 * marginSettingCapability (Object)
	 */
	public MarginSettingCapability getMarginSettingCapability() {
		Map<String, Object> value = getObjectValue(KEY_MARGIN_SETTING_CAPABILITY);
		if (value == null) {
			return null;
		}
		return new MarginSettingCapability(value);
	}

	/*
	 * centeringList (Array[Boolean])
	 */
	public List<Boolean> getCenteringList() {
		return getArrayValue(KEY_CENTERING_LIST);
	}

	/*
	 * eraseColorList (Array[Boolean])
	 */
	public List<Boolean> getEraseColorList() {
		return getArrayValue(KEY_ERASE_COLOR_LIST);
	}

	/*
	 * eraseColorSettingList (Array[String])
	 */
	public List<String> getEraseColorSettingList() {
		return getArrayValue(KEY_ERASE_COLOR_SETTING_LIST);
	}

	/*
	 * eraseInside1List (Array[Boolean])
	 * @since SmartSDK V2.12
	 */
	public List<Boolean> getEraseInside1List() {
		return getArrayValue(KEY_ERASE_INSIDE_1_LIST);
	}

	/*
	 * eraseInside2List (Array[Boolean])
	 * @since SmartSDK V2.12
	 */
	public List<Boolean> getEraseInside2List() {
		return getArrayValue(KEY_ERASE_INSIDE_2_LIST);
	}

	/*
	 * eraseInside3List (Array[Boolean])
	 * @since SmartSDK V2.12
	 */
	public List<Boolean> getEraseInside3List() {
		return getArrayValue(KEY_ERASE_INSIDE_3_LIST);
	}

	/*
	 * eraseInside4List (Array[Boolean])
	 * @since SmartSDK V2.12
	 */
	public List<Boolean> getEraseInside4List() {
		return getArrayValue(KEY_ERASE_INSIDE_4_LIST);
	}

	/*
	 * eraseInside5List (Array[Boolean])
	 * @since SmartSDK V2.12
	 */
	public List<Boolean> getEraseInside5List() {
		return getArrayValue(KEY_ERASE_INSIDE_5_LIST);
	}

	/*
	 * eraseInside1SettingCapability (Object)
	 * @since SmartSDK V2.12
	 */
	public EraseSettingCapability getEraseInside1SettingCapability() {
		Map<String, Object> value = getObjectValue(KEY_ERASE_INSIDE_1_SETTING_CAPABILITY);
		if (value == null) {
			return null;
		}
		return new EraseSettingCapability(value);
	}

	/*
	 * eraseInside2SettingCapability (Object)
	 * @since SmartSDK V2.12
	 */
	public EraseSettingCapability getEraseInside2SettingCapability() {
		Map<String, Object> value = getObjectValue(KEY_ERASE_INSIDE_2_SETTING_CAPABILITY);
		if (value == null) {
			return null;
		}
		return new EraseSettingCapability(value);
	}

	/*
	 * eraseInside3SettingCapability (Object)
	 * @since SmartSDK V2.12
	 */
	public EraseSettingCapability getEraseInside3SettingCapability() {
		Map<String, Object> value = getObjectValue(KEY_ERASE_INSIDE_3_SETTING_CAPABILITY);
		if (value == null) {
			return null;
		}
		return new EraseSettingCapability(value);
	}

	/*
	 * eraseInside4SettingCapability (Object)
	 * @since SmartSDK V2.12
	 */
	public EraseSettingCapability getEraseInside4SettingCapability() {
		Map<String, Object> value = getObjectValue(KEY_ERASE_INSIDE_4_SETTING_CAPABILITY);
		if (value == null) {
			return null;
		}
		return new EraseSettingCapability(value);
	}

	/*
	 * eraseInside5SettingCapability (Object)
	 * @since SmartSDK V2.12
	 */
	public EraseSettingCapability getEraseInside5SettingCapability() {
		Map<String, Object> value = getObjectValue(KEY_ERASE_INSIDE_5_SETTING_CAPABILITY);
		if (value == null) {
			return null;
		}
		return new EraseSettingCapability(value);
	}

	/*
	 * eraseOutsideList (Array[Boolean])
	 * @since SmartSDK V2.12
	 */
	public List<Boolean> getEraseOutsideList() {
		return getArrayValue(KEY_ERASE_OUTSIDE_LIST);
	}

	/*
	 * eraseOutsideSettingCapability (Object)
	 * @since SmartSDK V2.12
	 */
	public EraseSettingCapability getEraseOutsideSettingCapability() {
		Map<String, Object> value = getObjectValue(KEY_ERASE_OUTSIDE_SETTING_CAPABILITY);
		if (value == null) {
			return null;
		}
		return new EraseSettingCapability(value);
	}

	/*
	 * partialCopyList (Array[Boolean])
	 * @since SmartSDK V2.12
	 */
	public List<Boolean> getPartialCopyList() {
		return getArrayValue(KEY_PARTIAL_COPY_LIST);
	}

	/*
	 * partialCopySettingCapability (Object)
	 * @since SmartSDK V2.12
	 */
	public PartialCopySettingCapability getPartialCopySettingCapability() {
		Map<String, Object> value = getObjectValue(KEY_PARTIAL_COPY_SETTING_CAPABILITY);
		if (value == null) {
			return null;
		}
		return new PartialCopySettingCapability(value);
	}

	/*
	 * adjustPrintPositionList (Array[Boolean])
	 * @since SmartSDK V2.12
	 */
	public List<Boolean> getAdjustPrintPositionList() {
		return getArrayValue(KEY_ADJUST_PRINT_POSITION_LIST);
	}

	/*
	 * adjustPrintPositionSettingCapability (Object)
	 * @since SmartSDK V2.12
	 */
	public AdjustPrintPositionSettingCapability getAdjustPrintPositionSettingCapability() {
		Map<String, Object> value = getObjectValue(KEY_ADJUST_PRINT_POSITION_SETTING_CAPABILITY);
		if (value == null) {
			return null;
		}
		return new AdjustPrintPositionSettingCapability(value);
	}

	/*
	 * marginAdjustList (Array[Boolean])
	 * @since SmartSDK V2.12
	 */
	public List<Boolean> getMarginAdjustList() {
		return getArrayValue(KEY_MARGIN_ADJUST_LIST);
	}

	/*
	 * marginAdjustSettingCapability (Object)
	 * @since SmartSDK V2.12
	 */
	public MarginAdjustSettingCapability getMarginAdjustSettingCapability() {
		Map<String, Object> value = getObjectValue(KEY_MARGIN_ADJUST_SETTING_CAPABILITY);
		if (value == null) {
			return null;
		}
		return new MarginAdjustSettingCapability(value);
	}

	/*
	 * backgroundNumberingList (Array[Boolean])
	 * @since SmartSDK V2.12
	 */
	public List<Boolean> getBackgroundNumberingList() {
		return getArrayValue(KEY_BACKGROUND_NUMBERING_LIST);
	}

	/*
	 * backgroundNumberingSettingCapability (Object)
	 * @since SmartSDK V2.12
	 */
	public BackgroundNumberingSettingCapability getBackgroundNumberingSettingCapability() {
		Map<String, Object> value = getObjectValue(KEY_BACKGROUND_NUMBERING_SETTING_CAPABILITY);
		if (value == null) {
			return null;
		}
		return new BackgroundNumberingSettingCapability(value);
	}

	/*
	 * presetStampList (Array[Boolean])
	 */
	public List<Boolean> getPresetStampList() {
		return getArrayValue(KEY_PRESET_STAMP_LIST);
	}

	/*
	 * presetStampMaxNun (Number)
	 * @since SmartSDK V2.12
	 */
	public Integer getPresetStampMaxNum() {
		return getNumberValue(KEY_PRESET_STAMP_MAX_NUM);
	}

	/*
	 * presetStampSettingCapability (Object)
	 */
	public PresetStampSettingCapability getPresetStampSettingCapability() {
		Map<String, Object> mapValue = getObjectValue(KEY_PRESET_STAMP_SETTING_CAPABILITY);
		if (mapValue == null) {
			return null;
		}
		return new PresetStampSettingCapability(mapValue);
	}

	/*
	 * userStampList (Array[Boolean])
	 */
	public List<Boolean> getUserStampList() {
		return getArrayValue(KEY_USER_STAMP_LIST);
	}

	/*
	 * userStampMaxNum (Number)
	 * @since SmartSDK V2.12
	 */
	public Integer getUserStampMaxNum() {
		return getNumberValue(KEY_USER_STAMP_MAX_NUM);
	}

	/*
	 * userStampSettingCapability (Object)
	 */
	public UserStampSettingCapability getUserStampSettingCapability() {
		Map<String, Object> mapValue = getObjectValue(KEY_USER_STAMP_SETTING_CAPABILITY);
		if (mapValue == null) {
			return null;
		}
		return new UserStampSettingCapability(mapValue);
	}

	/*
	 * dateStampList (Array[Boolean])
	 */
	public List<Boolean> getDateStampList() {
		return getArrayValue(KEY_DATE_STAMP_LIST);
	}

	/*
	 * dateStampSettingCapability (Object)
	 */
	public DateStampSettingCapability getDateStampSettingCapability() {
		Map<String, Object> mapValue = getObjectValue(KEY_DATE_STAMP_SETTING_CAPABILITY);
		if (mapValue == null) {
			return null;
		}
		return new DateStampSettingCapability(mapValue);
	}

	/*
	 * pageStampList (Array[Boolean])
	 */
	public List<Boolean> getPageStampList() {
		return getArrayValue(KEY_PAGE_STAMP_LIST);
	}

	/*
	 * pageStampSettingCapability (Object)
	 */
	public PageStampSettingCapability getPageStampSettingCapability() {
		Map<String, Object> mapValue = getObjectValue(KEY_PAGE_STAMP_SETTING_CAPABILITY);
		if (mapValue == null) {
			return null;
		}
		return new PageStampSettingCapability(mapValue);
	}

	/*
	 * textStampList (Array[Boolean])
	 */
	public List<Boolean> getTextStampList() {
		return getArrayValue(KEY_TEXT_STAMP_LIST);
	}

	/*
	 * textStampSettingCapability (Object)
	 */
	public TextStampSettingCapability getTextStampSettingCapability() {
		Map<String, Object> mapValue = getObjectValue(KEY_TEXT_STAMP_SETTING_CAPABILITY);
		if (mapValue == null) {
			return null;
		}
		return new TextStampSettingCapability(mapValue);
	}

	/*
	 * posterList (Array[String])
	 * @since SmartSDK V2.12
	 */
	public List<String> getPosterList() {
		return getArrayValue(KEY_POSTER_LIST);
	}

	/*
	 * doubleCopiesList (Array[Boolean])
	 * @since SmartSDK V2.12
	 */
	public List<Boolean> getDoubleCopiesList() {
		return getArrayValue(KEY_DOUBLE_COPIES_LIST);
	}

	/*
	 * doubleCopiesSeparatorLineList (Array[Boolean])
	 * @since SmartSDK V2.12
	 */
	public List<Boolean> getDoubleCopiesSeparatorLineList() {
		return getArrayValue(KEY_DOUBLE_COPIES_SEPARATOR_LINE_LIST);
	}

	/*
	 * doubleCopiesSeparatorLineSettingCapability (Object)
	 * @since SmartSDK V2.12
	 */
	public DoubleCopiesSeparatorLineSettingCapability getDoubleCopiesSeparatorLineSettingCapability() {
		Map<String, Object> value = getObjectValue(KEY_DOUBLE_COPIES_SEPARATOR_LINE_SETTING_CAPABILITY);
		if (value == null) {
			return null;
		}
		return new DoubleCopiesSeparatorLineSettingCapability(value);
	}

	/*
	 * colorBalanceYellowRange (Range)
	 * @since SmartSDK V2.12
	 */
	public RangeElement getColorBalanceYellowRange() {
		return getRangeValue(KEY_COLOR_BALANCE_YELLOW_RANGE);
	}

	/*
	 * colorBalanceMagentaRange (Range)
	 * 
	 * @since SmartSDK V2.12
	 */
	public RangeElement getColorBalanceMagentaRange() {
		return getRangeValue(KEY_COLOR_BALANCE_MAGENTA_RANGE);
	}

	/*
	 * colorBalanceCyanRange (Range)
	 * @since SmartSDK V2.12
	 */
	public RangeElement getColorBalanceCyanRange() {
		return getRangeValue(KEY_COLOR_BALANCE_CYAN_RANGE);
	}

	/*
	 * colorBalanceBlackRange (Range)
	 * @since SmartSDK V2.12
	 */
	public RangeElement getColorBalanceBlackRange() {
		return getRangeValue(KEY_COLOR_BALANCE_BLACK_RANGE);
	}

	/*
	 * adjustColotList (Array[Boolean])
	 * @since SmartSDK V2.12
	 */
	public List<Boolean> getAdjustColorList() {
		return getArrayValue(KEY_ADJUST_COLOR_LIST);
	}

	/*
	 * adjustColorSettingCapability (Object)
	 * @since SmartSDK V2.12
	 */
	public AdjustColorSettingCapability getAdjustColorSettingCapability() {
		Map<String, Object> value = getObjectValue(KEY_ADJUST_COLOR_SETTING_CAPABILITY);
		if (value == null) {
			return null;
		}
		return new AdjustColorSettingCapability(value);
	}

	/*
	 * unauthorizedCopyPreventionList (Array[Boolean])
	 * @since SmartSDK V2.12
	 */
	public List<Boolean> getUnauthorizedCopyPreventionList() {
		return getArrayValue(KEY_UNAUTHORIZED_COPY_PREVENTION_LIST);
	}

	/*
	 * unauthorizedCopyPreventionSettingCapability (Object)
	 * 
	 * @since SmartSDK V2.12
	 */
	public UnauthorizedCopyPreventionSettingCapability getUnauthorizedCopyPreventionSettingCapability() {
		Map<String, Object> value = getObjectValue(KEY_UNAUTHORIZED_COPY_PREVENTION_SETTING_CAPABILITY);
		if (value == null) {
			return null;
		}
		return new UnauthorizedCopyPreventionSettingCapability(value);
	}

	/*
	 * halfFoldList (Array[Boolean])
	 * @since SmartSDK V2.12
	 */
	public List<Boolean> getHalfFoldList() {
		return getArrayValue(KEY_HALF_FOLD_LIST);
	}

	/*
	 * HalfFoldSettingCapability (Object)
	 * @since SmartSDK V2.12
	 */
	public HalfFoldSettingCapability getHalfFoldSettingCapability() {
		Map<String, Object> value = getObjectValue(KEY_HALF_FOLD_SETTING_CAPABILITY);
		if (value == null) {
			return null;
		}
		return new HalfFoldSettingCapability(value);
	}
	
	/*
	 * sampleCopyList (Array[Boolean])
	 * 
	 * @since SmartSDK v2.30
	 */
	public List<Boolean> getSampleCopyList() {
		return getArrayValue(KEY_SAMPLE_COPY_LIST);
	}
	
	/*
	 * 3EdgesFullBleedList (Array[Boolean])
	 * 
	 * @since SmartSDK v2.30
	 */
	public List<Boolean> get3EdgesFullBleedList() {
		return getArrayValue(KEY_3_EDGES_FULL_BLEED_LIST);
	}
	
	/*
	 * thinPaperList (Array[Boolean])
	 * @Since SmartSDk v2.40
	 */
	public List<Boolean> getThinPaperList() {
		return getArrayValue(KEY_THIN_PAPER_LIST);
	}
	
	
	/*
	 * multiFoldList (Array[String])
	 * 
	 * @since SmartSDK v2.30
	 */
	public List<String> getMultiFoldList() {
		return getArrayValue(KEY_MULTI_FOLD_LIST);
	}		
	
	/*
	 * multiFoldSettingCapability (Object)
	 * 
	 * @since SmartSDK v2.30
	 */
	public MultiFoldSettingCapability getMultiFoldSettingCapability() {
		Map<String, Object> value = getObjectValue(KEY_MULTI_FOLD_SETTING_CAPABILITY);
		if (value == null) {
			return null;
		}
		return new MultiFoldSettingCapability(value);
	}
	
	/*
	 * adjustPrintDensityRange (Range)
	 * 
	 * @Since SmartSDK v2.40
	 */
	public RangeElement getAdjustPrintDensityRange() {
		return getRangeValue(KEY_ADJUST_PRINT_DENSITY_RANGE);
	}
	
	/*
	 * adjustScanPositionList  (Array[Boolean])
	 * @since SmartSDK v2.40
	 */
	public List<Boolean> getAdjustScanPositionList() {
		return getArrayValue(KEY_ADJUST_SCAN_POSITION_LIST);
	}
	
	/*
	 * adjustScanPositionSettingCapability (Object)
	 * @since SmartSDK v2.40
	 */
	public AdjustScanPositionSettingCapability getAdjustScanPositionSettingCapability() {
		Map<String, Object> value = getObjectValue(KEY_ADJUST_SCAN_POSITION_SETTING_CAPABILITY);
		if (value == null) {
			return null;
		}
		return new AdjustScanPositionSettingCapability(value);
	}

	/*
	 * creepAdjustmentList (Array[Boolean])
	 * @since SmartSDK v2.40
	 */
	public List<Boolean> getCreepAdjustmentList() {
		return getArrayValue(KEY_CREEP_ADJUSTMENT_LIST);
	}	
	
	/*
	 * creepAdjustmentSettingCapability (Object)
	 * @since SmartSDK v2.40
	 */
	public CreepAdjustmentSettingCapability getCreepAdjustmentSettingCapability() {
		Map<String, Object> value = getObjectValue(KEY_CREEP_ADJUSTMENT_SETTING_CAPABILITY);
		if (value == null) {
			return null;
		}
		return new CreepAdjustmentSettingCapability(value);
	}
	
	/*
	 * insertSeparationSheetList (Array[String])
	 * @since SmartSDK v2.40
	 */
	public List<String> getInsertSeparationSheetList() {
		return getArrayValue(KEY_INSERT_SEPARATION_SHEET_LIST);
	}	
	
	/*
	 * changeInsertPositionRange (Range)
	 * @since SmartSDK v2.40
	 */
	public RangeElement getChangeInsertPositionRange() {
		return getRangeValue(KEY_CHANGE_INSERT_POSITION_RANGE);
	}
	
	/*
	 * reverseEjectionList (Array[Boolean])
	 * @since SmartSDK v2.40
	 */
	public List<Boolean> getReverseEjectionList() {
		return getArrayValue(KEY_REVERSE_EJECTION_LIST);
	}
	
	public static class SpecialColorSettingCapability extends CapabilityElement {

		private static final String KEY_NON_BLACK_PART_COLOR_LIST = "nonBlackPartColorList";
		private static final String KEY_BLACK_PART_COLOR_LIST = "blackPartColorList";
		private static final String KEY_SINGLE_COLOR_LIST = "singleColorList";
		private static final String KEY_SINGLE_COLOR_DENSITY_RANGE = "singleColorDensityRange";
		private static final String KEY_USER_COLOR_NAME_LIST = "userColorNameList";

		SpecialColorSettingCapability(Map<String, Object> values) {
			super(values);
		}

		/*
		 * nonBlackPartColorList (Array[String])
		 */
		public List<String> getNonBlackPartColorList() {
			return getArrayValue(KEY_NON_BLACK_PART_COLOR_LIST);
		}

		/*
		 * blackPartColorList (Array[String])
		 */
		public List<String> getBlackPartColorList() {
			return getArrayValue(KEY_BLACK_PART_COLOR_LIST);
		}

		/*
		 * singleColorList (Array[String])
		 */
		public List<String> getSingleColorList() {
			return getArrayValue(KEY_SINGLE_COLOR_LIST);
		}

		/*
		 * singleColorDensityRange (Range)
		 */
		public RangeElement getSingleColorDensityRange() {
			return getRangeValue(KEY_SINGLE_COLOR_DENSITY_RANGE);
		}

		/*
		 * userColorNameList (Array[Object])
		 */
		public UserColorNameList getUserColorNameList() {
			List<Map<String, Object>> value = getArrayValue(KEY_USER_COLOR_NAME_LIST);
			if (value == null) {
				return null;
			}
			return new UserColorNameList(value);
		}

	}

	public static class UserColorNameList extends ArrayElement<UserColorName> {

		UserColorNameList(List<Map<String, Object>> list) {
			super(list);
		}

		@Override
		protected UserColorName createElement(Map<String, Object> values) {
			return new UserColorName(values);
		}

	}

	public static class UserColorName extends CapabilityElement {

		private static final String KEY_ID = "id";
		private static final String KEY_NAME = "name";

		UserColorName(Map<String, Object> values) {
			super(values);
		}

		/*
		 * id (String)
		 */
		public String getId() {
			return getStringValue(KEY_ID);
		}

		/*
		 * name (String)
		 */
		public String getName() {
			return getStringValue(KEY_NAME);
		}
	}

	public static class CombineSeparatorLineSettingCapability extends
			CapabilityElement {

		private static final String KEY_LINE_TYPE_LIST = "lineTypeList";
		private static final String KEY_LINE_COLOR_LIST = "lineColorList";

		CombineSeparatorLineSettingCapability(Map<String, Object> value) {
			super(value);
		}

		/*
		 * lineTypeList (Array[String])
		 */
		public List<String> getLineTypeList() {
			return getArrayValue(KEY_LINE_TYPE_LIST);
		}

		/*
		 * lineColorList (Array[String])
		 */
		public List<String> getLineColorList() {
			return getArrayValue(KEY_LINE_COLOR_LIST);
		}

	}

	/*
	 * @since SmartSDK V2.12
	 */
	public static class FineMagnificationSettingCapability extends CapabilityElement {

		private static final String KEY_HORIZONTAL_RANGE = "horizontalRange";
		private static final String KEY_VERTICAL_RANGE = "verticalRange";
		
		FineMagnificationSettingCapability(Map<String, Object> values) {
			super(values);
		}
		
		/*
		 * horizontalRange (Range)
		 * @since SmartSDK V2.12
		 */
		public RangeElement getHorizontalRange() {
			return getRangeValue(KEY_HORIZONTAL_RANGE);
		}

		/*
		 * verticalRange (Range)
		 * @since SmartSDK V2.12
		 */
		public RangeElement getVerticalRange() {
			return getRangeValue(KEY_VERTICAL_RANGE);
		}		
	}

	/*
	 * @since SmartSDK V2.12
	 */
	public static class PresetCutSizeInfoList extends
			ArrayElement<PresetCutSizeInfo> {

		PresetCutSizeInfoList(List<Map<String, Object>> list) {
			super(list);
		}

		@Override
		protected PresetCutSizeInfo createElement(Map<String, Object> values) {
			return new PresetCutSizeInfo(values);
		}

	}

	/*
	 * @since SmartSDK V2.12
	 */
	public static class PresetCutSizeInfo extends CapabilityElement {

		private static final String KEY_ROLL_WIDTH = "rollWidth";
		private static final String KEY_PRESET_CUT_SIZE_LIST = "presetCutSizeList";
		private static final String KEY_PRESET_CUT_LENGTH_LIST = "presetCutLengthList";

		PresetCutSizeInfo(Map<String, Object> values) {
			super(values);
		}

		/*
		 * rollWidth (String)
		 * @since SmartSDK V2.12
		 */
		public String getRollWidth() {
			return getStringValue(KEY_ROLL_WIDTH);
		}

		/*
		 * presetCutSizeList (Array[String])
		 * @since SmartSDK V2.12
		 */
		public List<String> getPresetCutSizeList() {
			return getArrayValue(KEY_PRESET_CUT_SIZE_LIST);
		}

		/*
		 * presetCutLengthList (Array[String])
		 * 
		 * @since SmartSDK V2.12
		 */
		public List<String> getPresetCutLengthList() {
			return getArrayValue(KEY_PRESET_CUT_LENGTH_LIST);
		}

	}

	/*
	 * @since SmartSDK V2.12
	 */
	public static class VariableCutSettingCapability extends CapabilityElement {

		private static final String KEY_VARIABLE_CUT_LENGTH_RANGE = "variableCutLengthRange";

		VariableCutSettingCapability(Map<String, Object> values) {
			super(values);
		}

		/*
		 * variableCutLengthRange (Range)
		 * @since SmartSDK V2.12
		 */
		public RangeElement getVariableCutLengthRange() {
			return getRangeValue(KEY_VARIABLE_CUT_LENGTH_RANGE);
		}
	}

	/*
	 * @since SmartSDK V2.12
	 */
	public static class EraseSettingCapability extends CapabilityElement {

		private static final String KEY_ERASE_X1_RANGE = "eraseX1Range";
		private static final String KEY_ERASE_Y1_RANGE = "eraseY1Range";
		private static final String KEY_ERASE_X2_RANGE = "eraseX2Range";
		private static final String KEY_ERASE_Y2_RANGE = "eraseY2Range";

		EraseSettingCapability(Map<String, Object> values) {
			super(values);
		}

		/*
		 * eraseX1Range (Range)
		 * @since SmartSDK V2.12
		 */
		public RangeElement getEraseX1Range() {
			return getRangeValue(KEY_ERASE_X1_RANGE);
		}

		/*
		 * eraseY1Range (Range)
		 * @since SmartSDK V2.12
		 */
		public RangeElement geteraseY1Range() {
			return getRangeValue(KEY_ERASE_Y1_RANGE);
		}

		/*
		 * eraseX2Range (Range)
		 * @since SmartSDK V2.12
		 */
		public RangeElement geteraseX2Range() {
			return getRangeValue(KEY_ERASE_X2_RANGE);
		}

		/*
		 * eraseY2Range (Range)
		 * @since SmartSDK V2.12
		 */
		public RangeElement geteraseY2Range() {
			return getRangeValue(KEY_ERASE_Y2_RANGE);
		}
	}

	public static class EraseCenterSettingCapability extends CapabilityElement {

		private static final String KEY_ERASE_WIDTH_RANGE = "eraseWidthRange";

		EraseCenterSettingCapability(Map<String, Object> values) {
			super(values);
		}

		/*
		 * eraseWidthRange (Range)
		 */
		public RangeElement getEraseWidthRange() {
			return getRangeValue(KEY_ERASE_WIDTH_RANGE);
		}

	}

	public static class EraseBorderSettingCapability extends CapabilityElement {

		private static final String KEY_ERASE_WIDTH_LEFT_RANGE = "eraseWidthLeftRange";
		private static final String KEY_ERASE_WIDTH_RIGHT_RANGE = "eraseWidthRightRange";
		private static final String KEY_ERASE_WIDTH_TOP_RANGE = "eraseWidthTopRange";
		private static final String KEY_ERASE_WIDTH_BOTTOM_RANGE = "eraseWidthBottomRange";

		EraseBorderSettingCapability(Map<String, Object> values) {
			super(values);
		}

		/*
		 * eraseWidthLeftRange (Range)
		 */
		public RangeElement getEraseWidthLeftRange() {
			return getRangeValue(KEY_ERASE_WIDTH_LEFT_RANGE);
		}

		/*
		 * eraseWidthRightRange (Range)
		 */
		public RangeElement getEraseWidthRightRange() {
			return getRangeValue(KEY_ERASE_WIDTH_RIGHT_RANGE);
		}

		/*
		 * eraseWidthTopRange (Range)
		 */
		public RangeElement getEraseWidthTopRange() {
			return getRangeValue(KEY_ERASE_WIDTH_TOP_RANGE);
		}

		/*
		 * eraseWidthBottomRange (Range)
		 */
		public RangeElement getEraseWidthBottomRange() {
			return getRangeValue(KEY_ERASE_WIDTH_BOTTOM_RANGE);
		}

	}

	public static class MarginSettingCapability extends CapabilityElement {

		private static final String KEY_MARGIN_WIDTH_FRONT_LEFT_RIGHT_RANGE = "marginWidthFrontLeftRightRange";
		private static final String KEY_MARGIN_WIDTH_FRONT_TOP_BOTTOM_RANGE = "marginWidthFrontTopBottomRange";
		private static final String KEY_MARGIN_WIDTH_BACK_LEFT_RIGHT_RANGE = "marginWidthBackLeftRightRange";
		private static final String KEY_MARGIN_WIDTH_BACK_TOP_BOTTOM_RANGE = "marginWidthBackTopBottomRange";

		MarginSettingCapability(Map<String, Object> values) {
			super(values);
		}

		/*
		 * marginWidthFrontLeftRightRange (Range)
		 */
		public RangeElement getMarginWidthFrontLeftRightRange() {
			return getRangeValue(KEY_MARGIN_WIDTH_FRONT_LEFT_RIGHT_RANGE);
		}

		/*
		 * marginWidthFrontTopBottomRange (Range)
		 */
		public RangeElement getMarginWidthFrontTopBottomRange() {
			return getRangeValue(KEY_MARGIN_WIDTH_FRONT_TOP_BOTTOM_RANGE);
		}

		/*
		 * marginWidthBackLeftRightRange (Range)
		 */
		public RangeElement getMarginWidthBackLeftRightRange() {
			return getRangeValue(KEY_MARGIN_WIDTH_BACK_LEFT_RIGHT_RANGE);
		}

		/*
		 * marginWidthBackTopBottomRange (Range)
		 */
		public RangeElement getMarginWidthBackTopBottomRange() {
			return getRangeValue(KEY_MARGIN_WIDTH_BACK_TOP_BOTTOM_RANGE);
		}

	}

	/*
	 * @since SmartSDK V2.12
	 */
	public static class PartialCopySettingCapability extends CapabilityElement {

		private static final String KEY_SCAN_START_POSITION_RANGE = "scanStartPositionRange";
		private static final String KEY_COPY_SIZE_RANGE = "copySizeRange";

		PartialCopySettingCapability(Map<String, Object> values) {
			super(values);
		}

		/*
		 * scanStartPositionRange (Range)
		 * @since SmartSDK V2.12
		 */
		public RangeElement getScanStartPositionRange() {
			return getRangeValue(KEY_SCAN_START_POSITION_RANGE);
		}

		/*
		 * copySizeRange (Range)
		 * @since SmartSDK V2.12
		 */
		public RangeElement getCopySizeRange() {
			return getRangeValue(KEY_COPY_SIZE_RANGE);
		}
	}

	/*
	 * @since SmartSDK V2.12
	 */
	public static class AdjustPrintPositionSettingCapability extends
			CapabilityElement {

		private static final String KEY_ADJUST_LEFT_RIGHT_RANGE = "adjustLeftRightRange";
		private static final String KEY_ADJUST_TOP_BOTTOM_RANGE = "adjustTopBottomRange";

		AdjustPrintPositionSettingCapability(Map<String, Object> values) {
			super(values);
		}

		/*
		 * adjustLeftRightRange (Range)
		 * @since SmartSDK V2.12
		 */
		public RangeElement getAdjustLeftRightRange() {
			return getRangeValue(KEY_ADJUST_LEFT_RIGHT_RANGE);
		}

		/*
		 * adjustTopBottomRange (Range)
		 * @since SmartSDK V2.12
		 */
		public RangeElement getAdjustTopBottomRange() {
			return getRangeValue(KEY_ADJUST_TOP_BOTTOM_RANGE);
		}

	}

	/*
	 * @since SmartSDK V2.12
	 */
	public static class MarginAdjustSettingCapability extends CapabilityElement {

		private static final String KEY_MARGIN_TOP_RANGE = "marginTopRange";
		private static final String KEY_MARGIN_BOTTOM_RANGE = "marginBottomRange";

		MarginAdjustSettingCapability(Map<String, Object> values) {
			super(values);
		}

		/*
		 * marginTopRange (Range)
		 * @since SmartSDK V2.12
		 */
		public RangeElement getMarginTopRange() {
			return getRangeValue(KEY_MARGIN_TOP_RANGE);
		}

		/*
		 * marginBottomRange (Range)
		 * @since SmartSDK V2.12
		 */
		public RangeElement getMarginBottomRange() {
			return getRangeValue(KEY_MARGIN_BOTTOM_RANGE);
		}

	}

	/*
	 * @since SmartSDK V2.12
	 */
	public static class BackgroundNumberingSettingCapability extends
			CapabilityElement {

		private static final String KEY_START_NUMBER_RANGE = "startNumberRange";
		private static final String KEY_SIZE_LIST = "sizeList";
		private static final String KEY_DENSITY_LIST = "densityList";
		private static final String KEY_COLOR_LIST = "colorList";

		BackgroundNumberingSettingCapability(Map<String, Object> values) {
			super(values);
		}

		/*
		 * startNumberRange (Range)
		 * @since SmartSDK V2.12
		 */
		public RangeElement getStartNumberRange() {
			return getRangeValue(KEY_START_NUMBER_RANGE);
		}

		/*
		 * sizeList (Array[String])
		 * @since SmartSDK V2.12
		 */
		public List<String> getSizeList() {
			return getArrayValue(KEY_SIZE_LIST);
		}

		/*
		 * densityList (Array[String])
		 * @since SmartSDK V2.12
		 */
		public List<String> getDensityList() {
			return getArrayValue(KEY_DENSITY_LIST);
		}

		/*
		 * colorList (Array[String])
		 * @since SmartSDK V2.12
		 */
		public List<String> getColorList() {
			return getArrayValue(KEY_COLOR_LIST);
		}
	}

	public static class PresetStampSettingCapability extends CapabilityElement {

		private static final String KEY_POSITION_LIST = "positionList";
		private static final String KEY_COLOR_LIST = "colorList";
		private static final String KEY_STAMP_KIND_LIST = "stampKindList";
		private static final String KEY_STAMP_NAME_LIST = "stampNameList";
		private static final String KEY_PAGE_LIST = "pageList";
		private static final String KEY_STAMP_SIZE_LIST = "stampSizeList";
		private static final String KEY_STAMP_DENSITY_LIST = "stampDensityList";

		PresetStampSettingCapability(Map<String, Object> values) {
			super(values);
		}

		/*
		 * positionList (Array[String])
		 */
		public List<String> getPositionList() {
			return getArrayValue(KEY_POSITION_LIST);
		}

		/*
		 * colorList (Array[String])
		 */
		public List<String> getColorList() {
			return getArrayValue(KEY_COLOR_LIST);
		}

		/*
		 * stampKindList (Array[String])
		 */
		public List<String> getStampKindList() {
			return getArrayValue(KEY_STAMP_KIND_LIST);
		}

		/*
		 * stampNameList (Array[Object])
		 */
		public StampNameList getStampNameList() {
			List<Map<String, Object>> value = getArrayValue(KEY_STAMP_NAME_LIST);
			if (value == null) {
				return null;
			}
			return new StampNameList(value);
		}

		/*
		 * pageList (Array[String])
		 */
		public List<String> getPageList() {
			return getArrayValue(KEY_PAGE_LIST);
		}

		/*
		 * stampSizeList (Array[Number])
		 */
		public List<Integer> getStampSizeList() {
			return getNumberArrayValue(KEY_STAMP_SIZE_LIST);
		}

		/*
		 * stampDensityList (Array[String])
		 */
		public List<String> getStampDensityList() {
			return getArrayValue(KEY_STAMP_DENSITY_LIST);
		}

	}

	public static class StampNameList extends ArrayElement<StampName> {

		StampNameList(List<Map<String, Object>> list) {
			super(list);
		}

		@Override
		protected StampName createElement(Map<String, Object> values) {
			return new StampName(values);
		}

	}

	public static class StampName extends CapabilityElement {

		private static final String KEY_ID = "id";
		private static final String KEY_NAME = "name";
		private static final String KEY_STAMP_STRING = "stampString";// SmartSDK V2.12

		StampName(Map<String, Object> values) {
			super(values);
		}

		/*
		 * id (String)
		 */
		public String getId() {
			return getStringValue(KEY_ID);
		}

		/*
		 * name (String)
		 */
		public String getName() {
			return getStringValue(KEY_NAME);
		}
		
		/*
		 * stampString (String)
		 * @since SmartSDK V2.12
		 */
		public String getStampString() {
			return getStringValue(KEY_STAMP_STRING);
		}
		
	}

	public static class UserStampSettingCapability extends CapabilityElement {

		private static final String KEY_POSITION_LIST = "positionList";
		private static final String KEY_COLOR_LIST = "colorList";
		private static final String KEY_STAMP_KIND_LIST = "stampKindList";
		private static final String KEY_STAMP_NAME_LIST = "stampNameList";
		private static final String KEY_PAGE_LIST = "pageList";

		UserStampSettingCapability(Map<String, Object> values) {
			super(values);
		}

		/*
		 * positionList (Array[String])
		 */
		public List<String> getPositionList() {
			return getArrayValue(KEY_POSITION_LIST);
		}

		/*
		 * colorList (Array[String])
		 */
		public List<String> getColorList() {
			return getArrayValue(KEY_COLOR_LIST);
		}

		/*
		 * stampKindList (Array[String])
		 */
		public List<String> getStampKindList() {
			return getArrayValue(KEY_STAMP_KIND_LIST);
		}

		/*
		 * stampNameList (Array[Object])
		 */
		public StampNameList getStampNameList() {
			List<Map<String, Object>> value = getArrayValue(KEY_STAMP_NAME_LIST);
			if (value == null) {
				return null;
			}
			return new StampNameList(value);
		}

		/*
		 * pageList (Array[String])
		 */
		public List<String> getPageList() {
			return getArrayValue(KEY_PAGE_LIST);
		}

	}

	public static class DateStampSettingCapability extends CapabilityElement {

		private static final String KEY_POSITION_LIST = "positionList";
		private static final String KEY_COLOR_LIST = "colorList";
		private static final String KEY_DATE_FORMAT_LIST = "dateFormatList";
		private static final String KEY_PAGE_LIST = "pageList";
		private static final String KEY_FONT_LIST = "fontList";
		private static final String KEY_FONT_SIZE_LIST = "fontSizeList";

		DateStampSettingCapability(Map<String, Object> values) {
			super(values);
		}

		/*
		 * positionList (Array[String])
		 */
		public List<String> getPositionList() {
			return getArrayValue(KEY_POSITION_LIST);
		}

		/*
		 * colorList (Array[String])
		 */
		public List<String> getColorList() {
			return getArrayValue(KEY_COLOR_LIST);
		}

		/*
		 * dateFormatList (Array[String])
		 */
		public List<String> getDateFormatList() {
			return getArrayValue(KEY_DATE_FORMAT_LIST);
		}

		/*
		 * pageList (Array[String])
		 */
		public List<String> getPageList() {
			return getArrayValue(KEY_PAGE_LIST);
		}

		/*
		 * fontList (Array[String])
		 */
		public List<String> getFontList() {
			return getArrayValue(KEY_FONT_LIST);
		}

		/*
		 * fontSizeList (Array[String])
		 */
		public List<String> getFontSizeList() {
			return getArrayValue(KEY_FONT_SIZE_LIST);
		}

	}

	public static class PageStampSettingCapability extends CapabilityElement {

		private static final String KEY_POSITION_LIST = "positionList";
		private static final String KEY_COLOR_LIST = "colorList";
		private static final String KEY_FONT_LIST = "fontList";
		private static final String KEY_FONT_SIZE_LIST = "fontSizeList";
		private static final String KEY_PAGE_SETTING_CAPABILITY = "pageSettingCapability";

		PageStampSettingCapability(Map<String, Object> values) {
			super(values);
		}

		/*
		 * positionList (Array[String])
		 */
		public List<String> getPositionList() {
			return getArrayValue(KEY_POSITION_LIST);
		}

		/*
		 * colorList (Array[String])
		 */
		public List<String> getColorList() {
			return getArrayValue(KEY_COLOR_LIST);
		}

		/*
		 * fontList (Array[String])
		 */
		public List<String> getFontList() {
			return getArrayValue(KEY_FONT_LIST);
		}

		/*
		 * fontSizeList (Array[String])
		 */
		public List<String> getFontSizeList() {
			return getArrayValue(KEY_FONT_SIZE_LIST);
		}

		/*
		 * pageSettingCapability (Object)
		 */
		public PageSettingCapability getPageSettingCapability() {
			Map<String, Object> mapValue = getObjectValue(KEY_PAGE_SETTING_CAPABILITY);
			if (mapValue == null) {
				return null;
			}
			return new PageSettingCapability(mapValue);
		}

	}

	public static class PageSettingCapability extends CapabilityElement {

		private static final String KEY_FORMAT_LIST = "formatList";
		private static final String KEY_LANGUAGE_LIST = "languageList";// SmartSDK V2.12
		private static final String KEY_FIRST_PAGE_RANGE = "firstPageRange";
		private static final String KEY_FIRST_NUMBER_RANGE = "firstNumberRange";
		private static final String KEY_LAST_NUMBER_RANGE = "lastNumberRange";
		private static final String KEY_TOTAL_PAGE_RANGE = "totalPageRange";

		PageSettingCapability(Map<String, Object> values) {
			super(values);
		}

		/*
		 * formatList (Array[String])
		 */
		public List<String> getFormatList() {
			return getArrayValue(KEY_FORMAT_LIST);
		}

		/*
		 * languageList (Array[String])
		 * @since SmartSDK V2.12
		 */
		public List<String> getLanguageList() {
			return getArrayValue(KEY_LANGUAGE_LIST);
		}

		/*
		 * firstPageRange (Range)
		 */
		public RangeElement getFirstPageRange() {
			return getRangeValue(KEY_FIRST_PAGE_RANGE);
		}

		/*
		 * firstNumberRange (Range)
		 */
		public RangeElement getFirstNumberRange() {
			return getRangeValue(KEY_FIRST_NUMBER_RANGE);
		}

		/*
		 * lastNumberRange (Range)
		 */
		public RangeElement getLastNumberRange() {
			return getRangeValue(KEY_LAST_NUMBER_RANGE);
		}

		/*
		 * totalPageRange (Range)
		 */
		public RangeElement getTotalPageRange() {
			return getRangeValue(KEY_TOTAL_PAGE_RANGE);
		}

	}

	public static class TextStampSettingCapability extends CapabilityElement {
		private static final String KEY_POSITION_LIST = "positionList";
		private static final String KEY_COLOR_LIST = "colorList";
		private static final String KEY_PAGE_LIST = "pageList";
		private static final String KEY_FONT_LIST = "fontList";
		private static final String KEY_FONT_SIZE_LIST = "fontSizeList";
		private static final String KEY_TEXT_STRING_LENGTH = "textStringLength";
		private static final String KEY_FIRST_NUMBER_LENGTH = "firstNumberLength"; // SmartSDK V2.12

		TextStampSettingCapability(Map<String, Object> values) {
			super(values);
		}

		/*
		 * positionList (Array[String])
		 */
		public List<String> getPositionList() {
			return getArrayValue(KEY_POSITION_LIST);
		}

		/*
		 * colorList (Array[String])
		 */
		public List<String> getColorList() {
			return getArrayValue(KEY_COLOR_LIST);
		}

		/*
		 * pageList (Array[String])
		 */
		public List<String> getPageList() {
			return getArrayValue(KEY_PAGE_LIST);
		}

		/*
		 * fontList (Array[String])
		 */
		public List<String> getFontList() {
			return getArrayValue(KEY_FONT_LIST);
		}

		/*
		 * fontSizeList (Array[String])
		 */
		public List<String> getFontSizeList() {
			return getArrayValue(KEY_FONT_SIZE_LIST);
		}

		/*
		 * textStringLength (MaxLength)
		 */
		public MaxLengthElement getTextStringLength() {
			return getMaxLengthValue(KEY_TEXT_STRING_LENGTH);
		}

		/*
		 * firstNumberLength (MaxLength)
		 * @since SmartSDK V2.12
		 */
		public MaxLengthElement getFirstNumberLength() {
			return getMaxLengthValue(KEY_FIRST_NUMBER_LENGTH);
		}

	}

	/*
	 * @since SmartSDK V2.12
	 */
	public static class DoubleCopiesSeparatorLineSettingCapability extends
			CapabilityElement {

		private static final String KEY_LINE_TYPE_LIST = "lineTypeList";
		private static final String KEY_LINE_COLOR_LIST = "lineColorList";

		DoubleCopiesSeparatorLineSettingCapability(Map<String, Object> values) {
			super(values);
		}

		/*
		 * lineTypeList (Array[String])
		 * @since SmartSDK V2.12
		 */
		public List<String> getLineTypeList() {
			return getArrayValue(KEY_LINE_TYPE_LIST);
		}

		/*
		 * lineColorList (Array[String]
		 * @since SmartSDK V2.12
		 */
		public List<String> getLineColorList() {
			return getArrayValue(KEY_LINE_COLOR_LIST);
		}
	}

	/*
	 * @since SmartSDK V2.12
	 */
	public static class AdjustColorSettingCapability extends CapabilityElement {

		private static final String KEY_COLOR_LIST = "colorList";
		private static final String KEY_TASTE_RANGE = "tasteRange";

		AdjustColorSettingCapability(Map<String, Object> values) {
			super(values);
		}

		/*
		 * colorList (Array[String])
		 * @since SmartSDK V2.12
		 */
		public List<String> getColorList() {
			return getArrayValue(KEY_COLOR_LIST);
		}

		/*
		 * tasteRange(Range)
		 * @since SmartSDK V2.12
		 */
		public RangeElement getTasteRange() {
			return getRangeValue(KEY_TASTE_RANGE);
		}
	}

	/*
	 * @since SmartSDK V2.12
	 */
	public static class UnauthorizedCopyPreventionSettingCapability extends CapabilityElement {

		private static final String KEY_KIND_LIST = "kindList";
		private static final String KEY_EFFECT_LIST = "effectList";
		private static final String KEY_TEXT_KIND_LIST = "textKindList";
		private static final String KEY_USER_TEXT_STRING_LENGTH = "userTextStringLength";
		private static final String KEY_FONT_LIST = "fontList";
		private static final String KEY_FONT_SIZE_RANGE = "fontSizeRange";
		private static final String KEY_ANGLE_RANGE = "angleRange";
		private static final String KEY_LINE_SPACING_RANGE = "lineSpacingRange";
		private static final String KEY_REPEAT_LIST = "repeatList";
		private static final String KEY_POSITION_LIST = "positionList";
		private static final String KEY_MASK_PATTERN_LIST = "maskPatternList";
		private static final String KEY_COLOR_LIST = "colorList";
		private static final String KEY_DENSITY_RANGE = "densityRange";

		UnauthorizedCopyPreventionSettingCapability(Map<String, Object> values) {
			super(values);
		}

		/*
		 * kindList (Array[String])
		 * @since SmartSDK V2.12
		 */
		public List<String> getKindList() {
			return getArrayValue(KEY_KIND_LIST);
		}

		/*
		 * effectList (Array[string])
		 * @since SmartSDK V2.12
		 */
		public List<String> getEffectList() {
			return getArrayValue(KEY_EFFECT_LIST);
		}

		/*
		 * textKindList (Array[String])
		 * @since SmartSDK V2.12
		 */
		public List<String> getTextKindList() {
			return getArrayValue(KEY_TEXT_KIND_LIST);
		}

		/*
		 * userTextStringLength (MaxLength)
		 * @since SmartSDK V2.12
		 */
		public MaxLengthElement getUserTextStringLength() {
			return getMaxLengthValue(KEY_USER_TEXT_STRING_LENGTH);
		}

		/*
		 * fontList (Array[String])
		 * @since SmartSDK V2.12
		 */
		public List<String> getFontList() {
			return getArrayValue(KEY_FONT_LIST);
		}

		/*
		 * fontSizeRange (Range)
		 * @since SmartSDK V2.12
		 */
		public RangeElement getFontSizeRange() {
			return getRangeValue(KEY_FONT_SIZE_RANGE);
		}

		/*
		 * angleRange (Range)
		 * @since SmartSDK V2.12
		 */
		public RangeElement getAngleRange() {
			return getRangeValue(KEY_ANGLE_RANGE);
		}

		/*
		 * lineSpacingRange (Range)
		 * @since SmartSDK V2.12
		 */
		public RangeElement getLineSpacingRange() {
			return getRangeValue(KEY_LINE_SPACING_RANGE);
		}

		/*
		 * repeatList (Array[Boolean])
		 * @since SmartSDK V2.12
		 */
		public List<Boolean> getRepeatList() {
			return getArrayValue(KEY_REPEAT_LIST);
		}

		/*
		 * positionList (Array[String])
		 * @since SmartSDK V2.12
		 */
		public List<String> getPositionList() {
			return getArrayValue(KEY_POSITION_LIST);
		}

		/*
		 * maskPatternList (Array[String])
		 * @since SmartSDK V2.12
		 */
		public List<String> getMaskPatternList() {
			return getArrayValue(KEY_MASK_PATTERN_LIST);
		}

		/*
		 * colorList (Array[String])
		 * @since SmartSDK V2.12
		 */
		public List<String> getColorList() {
			return getArrayValue(KEY_COLOR_LIST);
		}

		/*
		 * densityRange (Range)
		 * @since SmartSDK V2.12
		 */
		public RangeElement getDensityRange() {
			return getRangeValue(KEY_DENSITY_RANGE);
		}

	}

	/*
	 * @since SmartSDK V2.12
	 */
	public static class HalfFoldSettingCapability extends CapabilityElement {

		private static final String KEY_FOLDING_DIRECTION_LIST = "foldingDirectionList";
		private static final String KEY_MULTI_SHEET_FOLD_LIST = "multiSheetFoldList";

		HalfFoldSettingCapability(Map<String, Object> values) {
			super(values);
		}

		/*
		 * foldingDirectionList (Array[String])
		 * @since SmartSDK V2.12
		 */
		public List<String> getFoldingDirectionList() {
			return getArrayValue(KEY_FOLDING_DIRECTION_LIST);
		}

		/*
		 * multiSheetFoldList (Array[Boolean])
		 * @since SmartSDK V2.12
		 */
		public List<Boolean> getMultiSheetFoldList() {
			return getArrayValue(KEY_MULTI_SHEET_FOLD_LIST);
		}
	}
	
	/*
	 * @since SmartSDK v2.30
	 */
	public static class MultiFoldSettingCapability extends CapabilityElement {
		private static final String KEY_FOLDING_DIRECTION_LIST             = "foldingDirectionList";  //SmartSDK v2.30
		private static final String KEY_HORIZONTAL_VERTICAL_FOLD_LIST      = "horizontalVerticalFoldList";  //SmartSDK v2.30
		private static final String KEY_OPEN_ORIENTATION_LIST              = "openOrientationList";  //SmartSDK v2.30
		private static final String KEY_MULTI_SHEET_FOLD_LIST              = "multiSheetFoldList";  //SmartSDK v2.30

		MultiFoldSettingCapability(Map<String, Object> values) {
			super(values);
		}
		
		/*
		 * foldingDirectionList (Array[String])
		 * 
		 * @since SmartSDK v2.30
		 */
		public List<String> getFoldingDirectionList() {
			return getArrayValue(KEY_FOLDING_DIRECTION_LIST);
		}
		
		/*
		 * horizontalVerticalFoldList (Array[String])
		 * 
		 * @since SmartSDK v2.30
		 */
		public List<String> getHorizontalVerticalFoldList() {
			return getArrayValue(KEY_HORIZONTAL_VERTICAL_FOLD_LIST);
		}
		
		/*
		 * openOrientationList (Array[String])
		 * 
		 * @since SmartSDK v2.30
		 */
		public List<String> getOpenOrientationList() {
			return getArrayValue(KEY_OPEN_ORIENTATION_LIST);
		}
		
		/*
		 * multiSheetFoldList (Array[Boolean])
		 * 
		 * @since SmartSDK v2.30
		 */
		public List<Boolean> getMultiSheetFoldList() {
			return getArrayValue(KEY_MULTI_SHEET_FOLD_LIST);
		}
		
	}
	
	/*
	 * @since SmartSDK v2.40
	 */
	public static class AdjustScanPositionSettingCapability extends CapabilityElement{
		private static final String KEY_ADJUST_SCAN_POSITION_WIDTH_FRONT_LEFT_RIGHT_RANGE     = "adjustScanPositionWidthFrontLeftRightRange"; 
		private static final String KEY_ADJUST_SCAN_POSITION_WIDTH_FRONT_TOP_BOTTOM_RANGE     = "adjustScanPositionWidthFrontTopBottomRange";
		private static final String KEY_ADJUST_SCAN_POSITION_WIDTH_BACK_LEFT_RIGHT_RANGE      = "adjustScanPositionWidthBackLeftRightRange";
		private static final String KEY_ADJUST_SCAN_POSITION_WIDTH_BACK_TOP_BOTTOM_RANGE      = "adjustScanPositionWidthBackTopBottomRange";

		AdjustScanPositionSettingCapability(Map<String, Object> values) {
			super(values);
		}
		
		/*
		 * adjustScanPositionWidthFrontLeftRightRange (Range)
		 */
		public RangeElement getAdjustScanPositionWidthFrontLeftRightRange() {
			return getRangeValue(KEY_ADJUST_SCAN_POSITION_WIDTH_FRONT_LEFT_RIGHT_RANGE);
		}
		
		/*
		 * adjustScanPositionWidthFrontTopBottomRange (Range)
		 */
		public RangeElement getAdjustScanPositionWidthFrontTopBottomRange() {
			return getRangeValue(KEY_ADJUST_SCAN_POSITION_WIDTH_FRONT_TOP_BOTTOM_RANGE);
		}
		
		/*
		 * adjustScanPositionWidthBackLeftRightRange (Range)
		 */
		public RangeElement getAdjustScanPositionWidthBackLeftRightRange() {
			return getRangeValue(KEY_ADJUST_SCAN_POSITION_WIDTH_BACK_LEFT_RIGHT_RANGE);
		}
		
		/*
		 * adjustScanPositionWidthBackTopBottomRange (Range)
		 */
		public RangeElement getAdjustScanPositionWidthBackTopBottomRange() {
			return getRangeValue(KEY_ADJUST_SCAN_POSITION_WIDTH_BACK_TOP_BOTTOM_RANGE);
		}
	}
	
	/*
	 * @since SmartSDK v2.40
	 */
	public static class CreepAdjustmentSettingCapability extends CapabilityElement{
		private static final String KEY_ADJUSTMENT_VALUE_RANGE         = "adjustmentValueRange";

		CreepAdjustmentSettingCapability(Map<String, Object> values) {
			super(values);
		}
		
		/*
		 * adjustmentValueRange (Range)
		 */
		public RangeElement getAdjustmentValueRange() {
			return getRangeValue(KEY_ADJUSTMENT_VALUE_RANGE);
		}
		
	}
}
