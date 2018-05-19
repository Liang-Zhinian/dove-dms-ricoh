/*
 *  Copyright (C) 2013-2017 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.wrapper.rws.service.copy;

import java.util.List;
import java.util.Map;

import com.dove.sample.wrapper.common.ArrayElement;
import com.dove.sample.wrapper.common.Utils;
import com.dove.sample.wrapper.common.WritableElement;

public class JobSetting extends WritableElement {

	private static final String KEY_AUTO_CORRECT_JOB_SETTING			= "autoCorrectJobSetting";
	private static final String KEY_JOB_MODE							= "jobMode";
	private static final String KEY_JOB_STOPPED_TIMEOUT_PERIOD			= "jobStoppedTimeoutPeriod";    // SmartSDK V1.02
	private static final String KEY_ORIGINAL_SIZE						= "originalSize";
	private static final String KEY_ORIGINAL_SIZE_CUSTOM_X				= "originalSizeCustomX";
	private static final String KEY_ORIGINAL_SIZE_CUSTOM_Y				= "originalSizeCustomY";
	private static final String KEY_ORIGINAL_SIZE_CUSTOM_HORIZONTAL 	= "originalSizeCustomHorizontal"; // SmartSDK V2.12
	private static final String KEY_ORIGINAL_SIZE_CUSTOM_VERTICAL 		= "originalSizeCustomVertical"; // SmartSDK V2.12
	private static final String KEY_SCAN_METHOD							= "scanMethod"; // SmartSDK V2.12
	private static final String KEY_ORIGINAL_SIDE						= "originalSide";
	private static final String KEY_ORIGINAL_ORIENTATION				= "originalOrientation";
	private static final String KEY_ORIGINAL_REVERSE_ORIENTATION 		= "originalReverseOrientation"; // SmartSDK V2.12
	private static final String KEY_ORIGINAL_INSERT_EDGE 				= "originalInsertEdge"; // SmartSDK V2.12
	private static final String KEY_ORIGINAL_TYPE						= "originalType";
	private static final String KEY_DARK_BACKGROUND_LEVEL       		= "darkBackgroundLevel";  // SmartSDK v2.30
	private static final String KEY_WIDE_SCAN							= "wideScan"; // SmartSDK V2.12
	private static final String KEY_HIGH_SPEED_COPY 					= "highSpeedCopy"; // SmartSDK V2.12
	private static final String KEY_PRINT_COLOR							= "printColor";
	private static final String KEY_SPECIAL_COLOR_SETTING 				= "specialColorSetting";
	private static final String KEY_COPIES								= "copies";
	private static final String KEY_SHEET_COLLATE						= "sheetCollate";
	private static final String KEY_PRINT_SIDE							= "printSide";
	private static final String KEY_COMBINE								= "combine";
	private static final String KEY_COMBINE_ORDER						= "combineOrder";
	private static final String KEY_COMBINE_SEPARATOR_LINE				= "combineSeparatorLine";
	private static final String KEY_COMBINE_SEPARATOR_LINE_SETTING		= "combineSeparatorLineSetting";
	private static final String KEY_MAGNIFICATION						= "magnification";
	private static final String KEY_CREATE_MARGIN						= "createMargin";  // SmartSDK V2.12
	private static final String KEY_FINE_MAGNIFICATION          		= "fineMagnification"; //SmartSDK V2.12
	private static final String KEY_FINE_MAGNIFICATION_SETTING			= "fineMagnificationSetting"; //SmartSDK V2.12
	private static final String KEY_PAPER_TRAY							= "paperTray";
	private static final String KEY_PAPER_CUT 							= "paperCut"; // SmartSDK V2.12
	private static final String KEY_PRESET_CUT_SETTING 					= "presetCutSetting"; // SmartSDK V2.12
	private static final String KEY_VARIABLE_CUT_SETTING 				= "variableCutSetting"; // SmartSDK V2.12
	private static final String KEY_AUTO_DENSITY						= "autoDensity";
	private static final String KEY_MANUAL_DENSITY						= "manualDensity";
	private static final String KEY_STAPLE								= "staple";
	private static final String KEY_PUNCH								= "punch";
	private static final String KEY_ERASE_CENTER						= "eraseCenter";
	private static final String KEY_ERASE_CENTER_SETTING				= "eraseCenterSetting";
	private static final String KEY_ERASE_BORDER						= "eraseBorder";
	private static final String KEY_ERASE_BORDER_SETTING				= "eraseBorderSetting";
	private static final String KEY_MARGIN								= "margin";
	private static final String KEY_MARGIN_SETTING						= "marginSetting";
	private static final String KEY_CENTERING							= "centering";
	private static final String KEY_ERASE_COLOR							= "eraseColor";
	private static final String KEY_ERASE_COLOR_SETTING					= "eraseColorSetting";
	private static final String KEY_ERASE_INSIDE_1 						= "eraseInside1"; // SmartSDK V2.12
	private static final String KEY_ERASE_INSIDE_2 						= "eraseInside2"; // SmartSDK V2.12
	private static final String KEY_ERASE_INSIDE_3 						= "eraseInside3"; // SmartSDK V2.12
	private static final String KEY_ERASE_INSIDE_4 						= "eraseInside4"; // SmartSDK V2.12
	private static final String KEY_ERASE_INSIDE_5 						= "eraseInside5"; // SmartSDK V2.12
	private static final String KEY_ERASE_INSIDE_1_SETTING 				= "eraseInside1Setting"; // SmartSDK V2.12
	private static final String KEY_ERASE_INSIDE_2_SETTING 				= "eraseInside2Setting"; // SmartSDK V2.12
	private static final String KEY_ERASE_INSIDE_3_SETTING 				= "eraseInside3Setting"; // SmartSDK V2.12
	private static final String KEY_ERASE_INSIDE_4_SETTING				= "eraseInside4Setting"; // SmartSDK V2.12
	private static final String KEY_ERASE_INSIDE_5_SETTING 				= "eraseInside5Setting"; // SmartSDK V2.12
	private static final String KEY_ERASE_OUTSIDE 						= "eraseOutside"; // SmartSDK V2.12
	private static final String KEY_ERASE_OUTSIDE_SETTING 				= "eraseOutsideSetting"; // SmartSDK V2.12
	private static final String KEY_PARTIAL_COPY 						= "partialCopy"; // SmartSDK V2.12
	private static final String KEY_PARTIAL_COPY_SETTING 				= "partialCopySetting"; // SmartSDK V2.12
	private static final String KEY_ADJUST_PRINT_POSITION 				= "adjustPrintPosition"; // SmartSDK V2.12
	private static final String KEY_ADJUST_PRINT_POSITION_SETTING 		= "adjustPrintPositionSetting"; // SmartSDK V2.12
	private static final String KEY_MARGIN_ADJUST						= "marginAdjust"; // SmartSDK V2.12
	private static final String KEY_MARGIN_ADJUST_SETTING 				= "marginAdjustSetting"; // SmartSDK V2.12
	private static final String KEY_BACKGROUND_NUMBERING				= "backgroundNumbering"; // SmartSDK V2.12
	private static final String KEY_BACKGROUND_NUMBERING_SETTING 		= "backgroundNumberingSetting"; // SmartSDK V2.12
	private static final String KEY_PRESET_STAMP						= "presetStamp";
	private static final String KEY_PRESET_STAMP_SETTING				= "presetStampSetting";
	private static final String KEY_USER_STAMP							= "userStamp";
	private static final String KEY_USER_STAMP_SETTING					= "userStampSetting";
	private static final String KEY_DATE_STAMP							= "dateStamp";
	private static final String KEY_DATE_STAMP_SETTING					= "dateStampSetting";
	private static final String KEY_PAGE_STAMP							= "pageStamp";
	private static final String KEY_PAGE_STAMP_SETTING					= "pageStampSetting";
	private static final String KEY_TEXT_STAMP							= "textStamp";
	private static final String KEY_TEXT_STAMP_SETTING					= "textStampSetting";
	private static final String KEY_POSTER 								= "poster"; // SmartSDK V2.12
	private static final String KEY_DOUBLE_COPIES 						= "doubleCopies"; // SmartSDK V2.12
	private static final String KEY_DOUBLE_COPIES_SEPARATOR_LINE 		= "doubleCopiesSeparatorLine"; // SmartSDK V2.12
	private static final String KEY_DOUBLE_COPIES_SEPARATOR_LINE_SETTING = "doubleCopiesSeparatorLineSetting"; // SmartSDK V2.12
	private static final String KEY_COLOR_BALANCE_YELLOW 				= "colorBalanceYellow"; // SmartSDK V2.12
	private static final String KEY_COLOR_BALANCE_MAGENTA 				= "colorBalanceMagenta"; // SmartSDK V2.12
	private static final String KEY_COLOR_BALANCE_CYAN 					= "colorBalanceCyan"; // SmartSDK V2.12
	private static final String KEY_COLOR_BALANCE_BLACK 				= "colorBalanceBlack"; // SmartSDK V2.12
	private static final String KEY_ADJUST_COLOR 						= "adjustColor"; // SmartSDK V2.12
	private static final String KEY_ADJUST_COLOR_SETTING 				 = "adjustColorSetting"; // SmartSDK V2.12
	private static final String KEY_UNAUTHORIZED_COPY_PREVENTION 		 = "unauthorizedCopyPrevention"; // SmartSDK V2.12
	private static final String KEY_UNAUTHORIZED_COPY_PREVENTION_SETTING = "unauthorizedCopyPreventionSetting"; // SmartSDK V2.12
	private static final String KEY_HALF_FOLD 							 = "halfFold"; // SmartSDK V2.12
	private static final String KEY_HALF_FOLD_SETTING 					 = "halfFoldSetting"; // SmartSDK V2.12
	private static final String KEY_SAMPLE_COPY                  		 = "sampleCopy"; // SmartSDK v2.30
	private static final String KEY_3_EDGES_FULL_BLEED           		 = "3EdgesFullBleed";  //SmartSDK v2.30
	private static final String KEY_THIN_PAPER                  		 = "thinPaper"; // SmartSDK v2.40
	private static final String KEY_MULTI_FOLD                    		 = "multiFold";  //SmartSDK v2.30
	private static final String KEY_MULTI_FOLD_SETTING           		 = "multiFoldSetting";  //SmartSDK v2.30  
	private static final String KEY_ADJUST_PRINT_DENSITY        		 = "adjustPrintDensity"; // SmartSDK v2.40
	private static final String KEY_ADJUST_SCAN_POSITION        		 = "adjustScanPosition"; // SmartSDK v2.40
	private static final String KEY_ADJUST_SCAN_POSITION_SETTING         = "adjustScanPositionSetting"; // SmartSDK v2.40
	private static final String KEY_CREEP_ADJUSTMENT           			 = "creepAdjustment"; // SmartSDK v2.40
	private static final String KEY_CREEP_ADJUSTMENT_SETTING    		 = "creepAdjustmentSetting"; // SmartSDK v2.40
	private static final String KEY_INSERT_SEPARATION_SHEET      		 = "insertSeparationSheet"; // SmartSDK v2.40
	private static final String KEY_CHANGE_INSERT_POSITION       		 = "changeInsertPosition"; // SmartSDK v2.40
	private static final String KEY_REVERSE_EJECTION           			 = "reverseEjection"; // SmartSDK v2.40
	
	public JobSetting(Map<String, Object> values) {
		super(values);
	}
    
	/*
	 * autoCorrectJobSetting (Boolean)
	 */
	public Boolean getAutoCorrectJobSetting() {
		return getBooleanValue(KEY_AUTO_CORRECT_JOB_SETTING);
	}
	public void setAutoCorrectJobSetting(Boolean value) {
		setBooleanValue(KEY_AUTO_CORRECT_JOB_SETTING, value);
	}
	public Boolean removeAutoCorrectJobSetting() {
		return removeBooleanValue(KEY_AUTO_CORRECT_JOB_SETTING);
	}

	/*
	 * jobMode (String)
	 */
	public String getJobMode() {
		return getStringValue(KEY_JOB_MODE);
	}
	public void setJobMode(String value) {
		setStringValue(KEY_JOB_MODE, value);
	}
	public String removeJobMode() {
		return removeStringValue(KEY_JOB_MODE);
	}

	/*
	 * jobStoppedTimeoutPeriod (Number)
	 * @since SmartSDK V1.02
	 */
	public Integer getJobStoppedTimeoutPeriod() {
		return getNumberValue(KEY_JOB_STOPPED_TIMEOUT_PERIOD);
	}
	public void setJobStoppedTimeoutPeriod(Integer value) {
		setNumberValue(KEY_JOB_STOPPED_TIMEOUT_PERIOD, value);
	}
	public Integer removeJobStoppedTimeoutPeriod() {
		return removeNumberValue(KEY_JOB_STOPPED_TIMEOUT_PERIOD);
	}

	/*
	 * originalSize (String)
	 */
	public String getOriginalSize() {
		return getStringValue(KEY_ORIGINAL_SIZE);
	}
	public void setOriginalSize(String value) {
		setStringValue(KEY_ORIGINAL_SIZE, value);
	}
	public String removeOriginalSize() {
		return removeStringValue(KEY_ORIGINAL_SIZE);
	}

	/*
	 * originalSizeCustomX (String)
	 */
	public String getOriginalSizeCustomX() {
		return getStringValue(KEY_ORIGINAL_SIZE_CUSTOM_X);
	}
	public void setOriginalSizeCustomX(String value) {
		setStringValue(KEY_ORIGINAL_SIZE_CUSTOM_X, value);
	}
	public String removeOriginalSizeCustomX() {
		return removeStringValue(KEY_ORIGINAL_SIZE_CUSTOM_X);
	}

	/*
	 * originalSizeCustomY (String)
	 */
	public String getOriginalSizeCustomY() {
		return getStringValue(KEY_ORIGINAL_SIZE_CUSTOM_Y);
	}
	public void setOriginalSizeCustomY(String value) {
		setStringValue(KEY_ORIGINAL_SIZE_CUSTOM_Y, value);
	}
	public String removeOriginalSizeCustomY() {
		return removeStringValue(KEY_ORIGINAL_SIZE_CUSTOM_Y);
	}
	
	/*
	 * originalSizeCustomHorizontal (String)
	 * @since SmartSDK V2.12
	 */
	public String getOriginalSizeCustomHorizontal() {
		return getStringValue(KEY_ORIGINAL_SIZE_CUSTOM_HORIZONTAL);
	}
    public void setOriginalSizeCustomHorizontal(String value) {
    	setStringValue(KEY_ORIGINAL_SIZE_CUSTOM_HORIZONTAL,value);
    }
    public String removeOriginalSizeCustomHorizontal() {
    	return removeStringValue(KEY_ORIGINAL_SIZE_CUSTOM_HORIZONTAL);
    }

    /*
	 * originalSizeCustomVertical (String)
	 * @since SmartSDK V2.12
	 */
    public String getOriginalSizeCustomVertical() {
    	return getStringValue(KEY_ORIGINAL_SIZE_CUSTOM_VERTICAL);
    }
    public void setOriginalSizeCustomVertical(String value){
    	setStringValue(KEY_ORIGINAL_SIZE_CUSTOM_VERTICAL,value);
    }
    public String removeOriginalSizeCustomVertical() {
    	return removeStringValue(KEY_ORIGINAL_SIZE_CUSTOM_VERTICAL);
    }
    
    /*
	 * scanMethod (String)
	 * @since SmartSDK V2.12
	 */
    public String getScanMethod() {
    	return getStringValue(KEY_SCAN_METHOD);
    }
    public void setScanMethod(String value) {
    	setStringValue(KEY_SCAN_METHOD,value);
    }
    public String removeScanMethod() {
    	return removeStringValue(KEY_SCAN_METHOD);
    }
    
	/*
	 * originalSide (String)
	 */
	public String getOriginalSide() {
		return getStringValue(KEY_ORIGINAL_SIDE);
	}
	public void setOriginalSide(String value) {
		setStringValue(KEY_ORIGINAL_SIDE, value);
	}
	public String removeOriginalSide() {
		return removeStringValue(KEY_ORIGINAL_SIDE);
	}

	/*
	 * originalOrientation (String)
	 */
	public String getOriginalOrientation() {
		return getStringValue(KEY_ORIGINAL_ORIENTATION);
	}
	public void setOriginalOrientation(String value) {
		setStringValue(KEY_ORIGINAL_ORIENTATION, value);
	}
	public String removeOriginalOrientation() {
		return removeStringValue(KEY_ORIGINAL_ORIENTATION);
	}
	
    /*
     * originalReverseOrientation (Boolean)
     * @since SmartSDK V2.12
     */
    public Boolean getOriginalReverseOrientation() {
    	return getBooleanValue(KEY_ORIGINAL_REVERSE_ORIENTATION);
    }
    public void setOriginalReverseOrientation(Boolean value) {
    	setBooleanValue(KEY_ORIGINAL_REVERSE_ORIENTATION, value);
    }
    public Boolean removeOriginalReverseOrientation() {
    	return removeBooleanValue(KEY_ORIGINAL_REVERSE_ORIENTATION);
    }
    
    /*
     * OriginalInsertEdge (String)
     * @since SmartSDK V2.12
     */
    public String getOriginalInsertEdge() {
    	return getStringValue(KEY_ORIGINAL_INSERT_EDGE);
    }
    public void setOriginalInsertEdge(String value) {
    	setStringValue(KEY_ORIGINAL_INSERT_EDGE,value);
    }
    public String removeOriginalInsertEdge() {
    	return removeStringValue(KEY_ORIGINAL_INSERT_EDGE);
    }

	/*
	 * originalType (String)
	 */
	public String getOriginalType() {
		return getStringValue(KEY_ORIGINAL_TYPE);
	}
	public void setOriginalType(String value) {
		setStringValue(KEY_ORIGINAL_TYPE, value);
	}
	public String removeOriginalType() {
		return removeStringValue(KEY_ORIGINAL_TYPE);
	}
	
	/*
	 * darkBackgroundLevel (String)
	 * @since SmartSDK v2.30
	 */
	public String getDarkBackgroundLevel() {
		return getStringValue(KEY_DARK_BACKGROUND_LEVEL);
	}
	public void setDarkBackgroundLevel(String value) {
		setStringValue(KEY_DARK_BACKGROUND_LEVEL, value);
	}
	public String removeDarkBackgroundLevel() {
		return removeStringValue(KEY_DARK_BACKGROUND_LEVEL);
	}

    /*
     * wideScan (Boolean)
     * @since SmartSDK V2.12
     */
    public Boolean getWideScan() {
    	return getBooleanValue(KEY_WIDE_SCAN);
    }
    public void setWideScan(Boolean value) {
    	setBooleanValue(KEY_WIDE_SCAN,value);
    }
    public Boolean removeWideScan() {
    	return removeBooleanValue(KEY_WIDE_SCAN);
    }
    
    /*
     * highSpeedCopy (Boolean)
     * @since SmartSDK V2.12
     */
    public Boolean getHighSpeedCopy() {
    	return getBooleanValue(KEY_HIGH_SPEED_COPY);
    }
    public void setHighSpeedCopy(Boolean value) {
    	setBooleanValue(KEY_HIGH_SPEED_COPY,value);
    }
    public Boolean removeHighSpeedCopy() {
    	return removeBooleanValue(KEY_HIGH_SPEED_COPY);
    }
	
	/*
	 * printColor (String)
	 */
	public String getPrintColor() {
		return getStringValue(KEY_PRINT_COLOR);
	}
	public void setPrintColor(String value) {
		setStringValue(KEY_PRINT_COLOR, value);
	}
	public String removePrintColor() {
		return removeStringValue(KEY_PRINT_COLOR);
	}

	/*
	 * specialColorSetting (Object)
	 */
	public SpecialColorSetting getSpecialColorSetting() {
		Map<String, Object> value = getObjectValue(KEY_SPECIAL_COLOR_SETTING);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_SPECIAL_COLOR_SETTING, value);
		}
		return new SpecialColorSetting(value);
	}
//	public void setSpecialColorSetting(SpecialColorSetting value) {
//		throw new UnsupportedOperationException();
//	}
	public SpecialColorSetting removeSpecialColorSetting() {
		Map<String, Object> value = removeObjectValue(KEY_SPECIAL_COLOR_SETTING);
		if (value == null) {
			return null;
		}
		return new SpecialColorSetting(value);
	}

	/*
	 * copies (Number)
	 */
	public Integer getCopies() {
		return getNumberValue(KEY_COPIES);
	}
	public void setCopies(Integer value) {
		setNumberValue(KEY_COPIES, value);
	}
	public Integer removeCopies() {
		return removeNumberValue(KEY_COPIES);
	}

	/*
	 * sheetCollate (String)
	 */
	public String getSheetCollate() {
		return getStringValue(KEY_SHEET_COLLATE);
	}
	public void setSheetCollate(String value) {
		setStringValue(KEY_SHEET_COLLATE, value);
	}
	public String removeSheetCollate() {
		return removeStringValue(KEY_SHEET_COLLATE);
	}

	/*
	 * printSide (String)
	 */
	public String getPrintSide() {
		return getStringValue(KEY_PRINT_SIDE);
	}
	public void setPrintSide(String value) {
		setStringValue(KEY_PRINT_SIDE, value);
	}
	public String removePrintSide() {
		return removeStringValue(KEY_PRINT_SIDE);
	}

	/*
	 * combine (String)
	 */
	public String getCombine() {
		return getStringValue(KEY_COMBINE);
	}
	public void setCombine(String value) {
		setStringValue(KEY_COMBINE, value);
	}
	public String removeCombine() {
		return removeStringValue(KEY_COMBINE);
	}

	/*
	 * combineOrder (String)
	 */
	public String getCombineOrder() {
		return getStringValue(KEY_COMBINE_ORDER);
	}
	public void setCombineOrder(String value) {
		setStringValue(KEY_COMBINE_ORDER, value);
	}
	public String removeCombineOrder() {
		return removeStringValue(KEY_COMBINE_ORDER);
	}

	/*
	 * combineSeparatorLine (Boolean)
	 */
	public Boolean getCombineSeparatorLine() {
		return getBooleanValue(KEY_COMBINE_SEPARATOR_LINE);
	}
	public void setCombineSeparatorLine(Boolean value) {
		setBooleanValue(KEY_COMBINE_SEPARATOR_LINE, value);
	}
	public Boolean removeCombineSeparatorLine() {
		return removeBooleanValue(KEY_COMBINE_SEPARATOR_LINE);
	}

	/*
	 * combineSeparatorLineSetting (Object)
	 */
	public CombineSeparatorLineSetting getCombineSeparatorLineSetting() {
		Map<String, Object> value = getObjectValue(KEY_COMBINE_SEPARATOR_LINE_SETTING);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_COMBINE_SEPARATOR_LINE_SETTING, value);
		}
		return new CombineSeparatorLineSetting(value);
	}
//	public void setCombineSeparatorLineSetting(CombineSeparatorLineSetting value) {
//		throw new UnsupportedOperationException();
//	}
	public CombineSeparatorLineSetting removeCombineSeparatorLineSetting() {
		Map<String, Object> value = removeObjectValue(KEY_COMBINE_SEPARATOR_LINE_SETTING);
		if (value == null) {
			return null;
		}
		return new CombineSeparatorLineSetting(value);
	}

	/*
	 * magnification (String)
	 */
	public String getMagnification() {
		return getStringValue(KEY_MAGNIFICATION);
	}
	public void setMagnification(String value) {
		setStringValue(KEY_MAGNIFICATION, value);
	}
	public String removeMagnification() {
		return removeStringValue(KEY_MAGNIFICATION);
	}
    
    /*
     * createMargin (Boolean)
     * @since SmartSDK V2.12
     */
    public  Boolean getCreateMargin() {
    	return getBooleanValue(KEY_CREATE_MARGIN);
    }
    public void setCreateMargin(Boolean value) {
    	setBooleanValue(KEY_CREATE_MARGIN, value);
    }
    public Boolean removeCreateMargin() {
    	return removeBooleanValue(KEY_CREATE_MARGIN);
    }

	/*
	 * fineMagnification (Boolean)
	 * @Since SmartSDk V2.12
	 */
	public Boolean getFineMagnification() {
		return getBooleanValue(KEY_FINE_MAGNIFICATION);
	}
	public void setFineMagnification(Boolean value) {
		setBooleanValue(KEY_FINE_MAGNIFICATION,value);
	}
	public Boolean removeFineMagnification() {
		return removeBooleanValue(KEY_FINE_MAGNIFICATION);
	}
	
	/*
	 *  fineMagnificationSetting (Object)
	 *  @Since SmartSDK V2.12
	 */
	public FineMagnificationSetting getFineMagnificationSetting() {
		Map<String, Object> mapValue = getObjectValue(KEY_FINE_MAGNIFICATION_SETTING);
		if (mapValue == null) {
			return null;
		}
		return new FineMagnificationSetting(mapValue);
	}
	public FineMagnificationSetting removeFineMagnificationSetting() {

    	Map<String, Object> value = removeObjectValue(KEY_FINE_MAGNIFICATION_SETTING);
    	if(value == null) {
    		return null;
    	}
    	return new FineMagnificationSetting(value);
    }
	
		

	/*
	 * paperTray (String)
	 */
	public String getPaperTray() {
		return getStringValue(KEY_PAPER_TRAY);
	}
	public void setPaperTray(String value) {
		setStringValue(KEY_PAPER_TRAY, value);
	}
	public String removePaperTray() {
		return removeStringValue(KEY_PAPER_TRAY);
	}
	
    /*
     * paperCut (String)
     * @since SmartSDK V2.12
     */
    public String getPaperCut() {
    	return getStringValue(KEY_PAPER_CUT);
    }
    public void setPaperCut(String value) {
    	setStringValue(KEY_PAPER_CUT,value);
    }
    public String removePaperCut() {
    	return removeStringValue(KEY_PAPER_CUT);
    }

    /*
     * presetCutSetting (Object)
     * @since SmartSDK V2.12
     */
    public PresetCutSetting getPresetCutSetting() {
    	
    	Map<String, Object> value = getObjectValue(KEY_PRESET_CUT_SETTING);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_PRESET_CUT_SETTING, value);
		}
		return new PresetCutSetting(value);
    	
    }
    public PresetCutSetting removePresetCutSetting() {
    	
    	Map<String, Object> value = removeObjectValue(KEY_PRESET_CUT_SETTING);
		if (value == null) {
			return null;
		}
		return new PresetCutSetting(value);
    }

    /*
     * variableCutSetting (Object)
     * @since SmartSDK V2.12
     */
    public VariableCutSetting getVariableCutSetting() {
    	
    	Map<String, Object> value = getObjectValue(KEY_VARIABLE_CUT_SETTING);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_VARIABLE_CUT_SETTING, value);
		}
		return new VariableCutSetting(value);
		
    }
    public VariableCutSetting removeVariableCutSetting() {
    	
    	Map<String, Object> value = removeObjectValue(KEY_VARIABLE_CUT_SETTING);
		if (value == null) {
			return null;
		}
		return new VariableCutSetting(value);
    }

	/*
	 * autoDensity (Boolean)
	 */
	public Boolean getAutoDensity() {
		return getBooleanValue(KEY_AUTO_DENSITY);
	}
	public void setAutoDensity(Boolean value) {
		setBooleanValue(KEY_AUTO_DENSITY, value);
	}
	public Boolean removeAutoDensity() {
		return removeBooleanValue(KEY_AUTO_DENSITY);
	}

	/*
	 * manualDensity (Number)
	 */
	public Integer getManualDensity() {
		return getNumberValue(KEY_MANUAL_DENSITY);
	}
	public void setManualDensity(Integer value) {
		setNumberValue(KEY_MANUAL_DENSITY, value);
	}
	public Integer removeManualDensity() {
		return removeNumberValue(KEY_MANUAL_DENSITY);
	}

	/*
	 * staple (String)
	 */
	public String getStaple() {
		return getStringValue(KEY_STAPLE);
	}
	public void setStaple(String value) {
		setStringValue(KEY_STAPLE, value);
	}
	public String removeStaple() {
		return removeStringValue(KEY_STAPLE);
	}

	/*
	 * punch (String)
	 */
	public String getPunch() {
		return getStringValue(KEY_PUNCH);
	}
	public void setPunch(String value) {
		setStringValue(KEY_PUNCH, value);
	}
	public String removePunch() {
		return removeStringValue(KEY_PUNCH);
	}

	/*
	 * eraseCenter (Boolean)
	 */
	public Boolean getEraseCenter() {
		return getBooleanValue(KEY_ERASE_CENTER);
	}
	public void setEraseCenter(Boolean value) {
		setBooleanValue(KEY_ERASE_CENTER, value);
	}
	public Boolean removeEraseCenter() {
		return removeBooleanValue(KEY_ERASE_CENTER);
	}

	/*
	 * eraseCenterSetting (Object)
	 */
	public EraseCenterSetting getEraseCenterSetting() {
		Map<String, Object> value = getObjectValue(KEY_ERASE_CENTER_SETTING);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_ERASE_CENTER_SETTING, value);
		}
		return new EraseCenterSetting(value);
	}
//	public void setEraseCenterSetting(EraseCenterSetting value) {
//		throw new UnsupportedOperationException();
//	}
	public EraseCenterSetting removeEraseCenterSetting() {
		Map<String, Object> value = removeObjectValue(KEY_ERASE_CENTER_SETTING);
		if (value == null) {
			return null;
		}
		return new EraseCenterSetting(value);
	}

	/*
	 * eraseBorder (Boolean)
	 */
	public Boolean getEraseBorder() {
		return getBooleanValue(KEY_ERASE_BORDER);
	}
	public void setEraseBorder(Boolean value) {
		setBooleanValue(KEY_ERASE_BORDER, value);
	}
	public Boolean removeEraseBorder() {
		return removeBooleanValue(KEY_ERASE_BORDER);
	}

	/*
	 * eraseBorderSetting (Object)
	 */
	public EraseBorderSetting getEraseBorderSetting() {
		Map<String, Object> value = getObjectValue(KEY_ERASE_BORDER_SETTING);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_ERASE_BORDER_SETTING, value);
		}
		return new EraseBorderSetting(value);
	}
//	public void setEraseBorderSetting(EraseBorderSetting value) {
//		throw new UnsupportedOperationException();
//	}
	public EraseBorderSetting removeEraseBorderSetting() {
		Map<String, Object> value = removeObjectValue(KEY_ERASE_BORDER_SETTING);
		if (value == null) {
			return null;
		}
		return new EraseBorderSetting(value);
	}

	/*
	 * margin (Boolean)
	 */
	public Boolean getMargin() {
		return getBooleanValue(KEY_MARGIN);
	}
	public void setMargin(Boolean value) {
		setBooleanValue(KEY_MARGIN, value);
	}
	public Boolean removeMargin() {
		return removeBooleanValue(KEY_MARGIN);
	}

	/*
	 * marginSetting (Object)
	 */
	public MarginSetting getMarginSetting() {
		Map<String, Object> value = getObjectValue(KEY_MARGIN_SETTING);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_MARGIN_SETTING, value);
		}
		return new MarginSetting(value);
	}
//	public void setMarginSetting(MarginSetting value) {
//		throw new UnsupportedOperationException();
//	}
	public MarginSetting removeMarginSetting() {
		Map<String, Object> value = removeObjectValue(KEY_MARGIN_SETTING);
		if (value == null) {
			return null;
		}
		return new MarginSetting(value);
	}

	/*
	 * centering (Boolean)
	 */
	public Boolean getCentering() {
		return getBooleanValue(KEY_CENTERING);
	}
	public void setCentering(Boolean value) {
		setBooleanValue(KEY_CENTERING, value);
	}
	public Boolean removeCentering() {
		return removeBooleanValue(KEY_CENTERING);
	}

	/*
	 * eraseColor (Boolean)
	 */
	public Boolean getEraseColor() {
		return getBooleanValue(KEY_ERASE_COLOR);
	}
	public void setEraseColor(Boolean value) {
		setBooleanValue(KEY_ERASE_COLOR, value);
	}
	public Boolean removeEraseColor() {
		return removeBooleanValue(KEY_ERASE_COLOR);
	}

	/*
	 * eraseColorSetting (Array[String])
	 */
	public List<String> getEraseColorSetting() {
		return getArrayValue(KEY_ERASE_COLOR_SETTING);
	}
	public void setEraseColorSetting(List<String> values) {
		setArrayValue(KEY_ERASE_COLOR_SETTING, values);
	}
	public List<String> removeEraseColorSetting() {
		return removeArrayValue(KEY_ERASE_COLOR_SETTING);
	}
    
    /*
     * eraseInside1 (Boolean)
     * @since SmartSDK V2.12
     */
    public Boolean getEraseInside1() {
    	return getBooleanValue(KEY_ERASE_INSIDE_1);
    }
    public void setEraseInside1(Boolean value) {
    	setBooleanValue(KEY_ERASE_INSIDE_1,value);
    }
    public Boolean removeEraseInside1() {
    	return removeBooleanValue(KEY_ERASE_INSIDE_1);
    }
    
    /*
     * eraseInside2 (Boolean)
     * @since SmartSDK V2.12
     */
    public Boolean getEraseInside2() {
    	return getBooleanValue(KEY_ERASE_INSIDE_2);
    }
    public void setEraseInside2(Boolean value) {
    	setBooleanValue(KEY_ERASE_INSIDE_2,value);
    }
    public Boolean removeEraseInside2() {
    	return removeBooleanValue(KEY_ERASE_INSIDE_2);
    }
    /*
     * eraseInside3 (Boolean)
     * @since SmartSDK V2.12
     */
    public Boolean getEraseInside3() {
    	return getBooleanValue(KEY_ERASE_INSIDE_3);
    }
    public void setEraseInside3(Boolean value) {
    	setBooleanValue(KEY_ERASE_INSIDE_3,value);
    }
    public Boolean removeEraseInside3() {
    	return removeBooleanValue(KEY_ERASE_INSIDE_3);
    }
    /*
     * eraseInside4 (Boolean)
     * @since SmartSDK V2.12
     */
    public Boolean getEraseInside4() {
    	return getBooleanValue(KEY_ERASE_INSIDE_4);
    }
    public void setEraseInside4(Boolean value) {
    	setBooleanValue(KEY_ERASE_INSIDE_4,value);
    }
    public Boolean removeEraseInside4() {
    	return removeBooleanValue(KEY_ERASE_INSIDE_4);
    }
    
    /*
     * eraseInside5 (Boolean)
     * @since SmartSDK V2.12
     */
    public Boolean getEraseInside5() {
    	return getBooleanValue(KEY_ERASE_INSIDE_5);
    }
    public void setEraseInside5(Boolean value) {
    	setBooleanValue(KEY_ERASE_INSIDE_5,value);
    }
    public Boolean removeEraseInside5() {
    	return removeBooleanValue(KEY_ERASE_INSIDE_5);
    }

    /*
     * eraseInside1Setting (Object)
     * @since SmartSDK V2.12
     */
    public EraseSetting getEraseInside1Setting() {
    	
    	Map<String, Object> value = getObjectValue(KEY_ERASE_INSIDE_1_SETTING);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_ERASE_INSIDE_1_SETTING, value);
		}
		return new EraseSetting(value);
    }
    public EraseSetting removeEraseInside1Setting() {
    	
    	Map<String, Object> value = removeObjectValue(KEY_ERASE_INSIDE_1_SETTING);
		if (value == null) {
			return null;
		}
		return new EraseSetting(value);
    }
    /*
     * eraseInside2Setting (Object)
     * @since SmartSDK V2.12
     */
    public EraseSetting getEraseInside2Setting() {
    	
    	Map<String, Object> value = getObjectValue(KEY_ERASE_INSIDE_2_SETTING);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_ERASE_INSIDE_2_SETTING, value);
		}
		return new EraseSetting(value);
    }
    public EraseSetting removeEraseInside2Setting() {
    	
    	Map<String, Object> value = removeObjectValue(KEY_ERASE_INSIDE_2_SETTING);
		if (value == null) {
			return null;
		}
		return new EraseSetting(value);
    }
    /*
     * eraseInside3Setting (Object)
     * @since SmartSDK V2.12
     */
    public EraseSetting getEraseInside3Setting() {
    	
    	Map<String, Object> value = getObjectValue(KEY_ERASE_INSIDE_3_SETTING);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_ERASE_INSIDE_3_SETTING, value);
		}
		return new EraseSetting(value);
    }
    public EraseSetting removeEraseInside3Setting() {
    	
    	Map<String, Object> value = removeObjectValue(KEY_ERASE_INSIDE_3_SETTING);
		if (value == null) {
			return null;
		}
		return new EraseSetting(value);
    }
    /*
     * eraseInside4Setting (Object)
     * @since SmartSDK V2.12
     */
    public EraseSetting getEraseInside4Setting() {
    	
    	Map<String, Object> value = getObjectValue(KEY_ERASE_INSIDE_4_SETTING);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_ERASE_INSIDE_4_SETTING, value);
		}
		return new EraseSetting(value);
    }
    public EraseSetting removeEraseInside4Setting() {
    	
    	Map<String, Object> value = removeObjectValue(KEY_ERASE_INSIDE_4_SETTING);
		if (value == null) {
			return null;
		}
		return new EraseSetting(value);
    }
    /*
     * eraseInside5Setting (Object)
     * @since SmartSDK V2.12
     */
    public EraseSetting getEraseInside5Setting() {
    	
    	Map<String, Object> value = getObjectValue(KEY_ERASE_INSIDE_5_SETTING);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_ERASE_INSIDE_5_SETTING, value);
		}
		return new EraseSetting(value);
    }
    public EraseSetting removeEraseInside5Setting() {
    	
    	Map<String, Object> value = removeObjectValue(KEY_ERASE_INSIDE_5_SETTING);
		if (value == null) {
			return null;
		}
		return new EraseSetting(value);
    }
    
    /*
     * eraseOutside (Boolean)
     * @since SmartSDK V2.12
     */
    public Boolean getEraseOutside() {
    	return getBooleanValue(KEY_ERASE_OUTSIDE);
    }
    public void setEraseOutside(Boolean value) {
    	setBooleanValue(KEY_ERASE_OUTSIDE, value);
    }
    public Boolean removeEraseOutside() {
    	return removeBooleanValue(KEY_ERASE_OUTSIDE);
    }

    /*
     * eraseOutsideSetting (Object)
     * @since SmartSDK V2.12
     */
    public EraseSetting getEraseOutsideSetting() {
    	
    	Map<String, Object> value = getObjectValue(KEY_ERASE_OUTSIDE_SETTING);
    	if(value == null) {
    		value = Utils.createElementMap();
			setObjectValue(KEY_ERASE_OUTSIDE_SETTING, value);
    	}
    	return new EraseSetting(value);
    }
    public EraseSetting removeEraseOutsideSetting() {
    	
    	Map<String, Object> value = removeObjectValue(KEY_ERASE_OUTSIDE_SETTING);
		if (value == null) {
			return null;
		}
		return new EraseSetting(value);
    }
    
    /*
     * partialCopy (Boolean)
     * @since SmartSDK V2.12
     */
    public Boolean getPartialCopy() {
    	return getBooleanValue(KEY_PARTIAL_COPY);
    }
    public void setPartialCopy(Boolean value) {
    	setBooleanValue(KEY_PARTIAL_COPY, value);
    }
    public Boolean removePartialCopy() {
    	return removeBooleanValue(KEY_PARTIAL_COPY);
    }

    /*
     * partialCopySetting (Object)
     * @since SmartSDK V2.12
     */
    public PartialCopySetting getPartialCopySetting() {
    	Map<String, Object> value = getObjectValue(KEY_PARTIAL_COPY_SETTING);
    	if(value == null) {
    		value = Utils.createElementMap();
			setObjectValue(KEY_PARTIAL_COPY_SETTING, value);
    	}
    	return new PartialCopySetting(value);
    }
    public PartialCopySetting removePartialCopySetting() {
    	Map<String, Object> value = removeObjectValue(KEY_PARTIAL_COPY_SETTING);
		if (value == null) {
			return null;
		}
		return new PartialCopySetting(value);
    }

    /*
     * adjustPrintPosition (Boolean)
     * @since SmartSDK V2.12
     */
    public Boolean getAdjustPrintPosition() {
    	return getBooleanValue(KEY_ADJUST_PRINT_POSITION);
    }
    public void setAdjustPrintPosition(Boolean value) {
    	setBooleanValue(KEY_ADJUST_PRINT_POSITION, value);
    }
    public Boolean removeAdjustPrintPosition() {
    	return removeBooleanValue(KEY_ADJUST_PRINT_POSITION);
    }
 
    /*
     * adjustPrintPositionSetting (Object)
     * @since SmartSDK V2.12
     */
    public AdjustPrintPositionSetting getAdjustPrintPositionSetting() {
    	Map<String, Object> value = getObjectValue(KEY_ADJUST_PRINT_POSITION_SETTING);
    	if(value == null) {
    		value = Utils.createElementMap();
			setObjectValue(KEY_ADJUST_PRINT_POSITION_SETTING, value);
    	}
    	return new AdjustPrintPositionSetting(value);
    }
    public AdjustPrintPositionSetting removeAdjustPrintPositionSetting() {
    	Map<String, Object> value = removeObjectValue(KEY_ADJUST_PRINT_POSITION_SETTING);
    	if(value == null) {
    		return null;
    	}
    	return new AdjustPrintPositionSetting(value);
    }
    
    /*
     * marginAdjust (Boolean)
     * @since SmartSDK V2.12
     */
    public Boolean getMarginAdjust() {
    	return getBooleanValue(KEY_MARGIN_ADJUST);
    }
    public void setMarginAdjust(Boolean value) {
    	setBooleanValue(KEY_MARGIN_ADJUST, value);
    }
    public Boolean removeMarginAdjust() {
    	return removeBooleanValue(KEY_MARGIN_ADJUST);
    }
    
    /*
     * marginAdjustSetting (Object)
     * @since SmartSDK V2.12
     */
    public MarginAdjustSetting getMarginAdjustSetting() {
    	Map<String, Object> value = getObjectValue(KEY_MARGIN_ADJUST_SETTING);
    	if(value == null) {
    		value = Utils.createElementMap();
			setObjectValue(KEY_MARGIN_ADJUST_SETTING, value);
    	}
    	return new MarginAdjustSetting(value);
    }
    public MarginAdjustSetting removeMarginAdjustSetting() {
    	Map<String, Object> value = removeObjectValue(KEY_MARGIN_ADJUST_SETTING);
    	if(value == null) {
    		return null;
    	}
    	return new MarginAdjustSetting(value);
    }
    
    /*
     * backgroundNumbering (Boolean)
     * @since SmartSDK V2.12
     */
    public Boolean getBackgroundNumbering() {
    	return getBooleanValue(KEY_BACKGROUND_NUMBERING);
    }
    public void setBackgroundNumbering(Boolean value) {
    	setBooleanValue(KEY_BACKGROUND_NUMBERING,value);
    } 
    public Boolean removeBackgroundNumbering() {
    	return removeBooleanValue(KEY_BACKGROUND_NUMBERING);
    }
    
    /*
     * backgroundNumberingSetting (Object)
     * @since SmartSDK V2.12
     */
    public BackgroundNumberingSetting getBackgroundNumberingSetting() {
    	
    	Map<String, Object> value = getObjectValue(KEY_BACKGROUND_NUMBERING_SETTING);
    	if(value == null) {
    		value = Utils.createElementMap();
			setObjectValue(KEY_BACKGROUND_NUMBERING_SETTING, value);
    	}
    	return new BackgroundNumberingSetting(value);
    }
    public BackgroundNumberingSetting removeBackgroundNumberingSetting() {
    	
    	Map<String, Object> value = removeObjectValue(KEY_BACKGROUND_NUMBERING_SETTING);
    	if(value == null) {
    		return null;
    	}
    	return new BackgroundNumberingSetting(value);
    }

	/*
	 * presetStamp (Boolean)
	 */
	public Boolean getPresetStamp() {
		return getBooleanValue(KEY_PRESET_STAMP);
	}
	public void setPresetStamp(Boolean value) {
		setBooleanValue(KEY_PRESET_STAMP, value);
	}
	public Boolean removePresetStamp() {
		return removeBooleanValue(KEY_PRESET_STAMP);
	}

	/*
	 * presetStampSetting (Object)
	 */
	public PresetStampSetting getPresetStampSetting() {
		Map<String, Object> value = getObjectValue(KEY_PRESET_STAMP_SETTING);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_PRESET_STAMP_SETTING, value);
		}
		return new PresetStampSetting(value);
	}
//	public void setPresetStampSetting(PresetStampSetting value) {
//		throw new UnsupportedOperationException();
//	}
	public PresetStampSetting removePresetStampSetting() {
		Map<String, Object> value = removeObjectValue(KEY_PRESET_STAMP_SETTING);
		if (value == null) {
			return null;
		}
		return new PresetStampSetting(value);
	}

	/*
	 * userStamp (Boolean)
	 */
	public Boolean getUserStamp() {
		return getBooleanValue(KEY_USER_STAMP);
	}
	public void setUserStamp(Boolean value) {
		setBooleanValue(KEY_USER_STAMP, value);
	}
	public Boolean removeUserStamp() {
		return removeBooleanValue(KEY_USER_STAMP);
	}

	/*
	 * userStampSetting (Object)
	 */
	public UserStampSetting getUserStampSetting() {
		Map<String, Object> value = getObjectValue(KEY_USER_STAMP_SETTING);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_USER_STAMP_SETTING, value);
		}
		return new UserStampSetting(value);
	}
//	public void setUserStampSetting(UserStampSetting value) {
//		throw new UnsupportedOperationException();
//	}
	public UserStampSetting removeUserStampSetting() {
		Map<String, Object> value = removeObjectValue(KEY_USER_STAMP_SETTING);
		if (value == null) {
			return null;
		}
		return new UserStampSetting(value);
	}

	/*
	 * dateStamp (Boolean)
	 */
	public Boolean getDateStamp() {
		return getBooleanValue(KEY_DATE_STAMP);
	}
	public void setDateStamp(Boolean value) {
		setBooleanValue(KEY_DATE_STAMP, value);
	}
	public Boolean removeDateStamp() {
		return removeBooleanValue(KEY_DATE_STAMP);
	}

	/*
	 * dateStampSetting (Object)
	 */
	public DateStampSetting getDateStampSetting() {
		Map<String, Object> value = getObjectValue(KEY_DATE_STAMP_SETTING);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_DATE_STAMP_SETTING, value);
		}
		return new DateStampSetting(value);
	}
//	public void setDateStampSetting(DateStampSetting value) {
//		throw new UnsupportedOperationException();
//	}
	public DateStampSetting removeDateStampSetting() {
		Map<String, Object> value = removeObjectValue(KEY_DATE_STAMP_SETTING);
		if (value == null) {
			return null;
		}
		return new DateStampSetting(value);
	}

	/*
	 * pageStamp (Boolean)
	 */
	public Boolean getPageStamp() {
		return getBooleanValue(KEY_PAGE_STAMP);
	}
	public void setPageStamp(Boolean value) {
		setBooleanValue(KEY_PAGE_STAMP, value);
	}
	public Boolean removePageStamp() {
		return removeBooleanValue(KEY_PAGE_STAMP);
	}

	/*
	 * pageStampSetting (Object)
	 */
	public PageStampSetting getPageStampSetting() {
		Map<String, Object> value = getObjectValue(KEY_PAGE_STAMP_SETTING);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_PAGE_STAMP_SETTING, value);
		}
		return new PageStampSetting(value);
	}
//	public void setPageStampSetting(PageStampSetting value) {
//		throw new UnsupportedOperationException();
//	}
	public PageStampSetting removePageStampSetting() {
		Map<String, Object> value = removeObjectValue(KEY_PAGE_STAMP_SETTING);
		if (value == null) {
			return null;
		}
		return new PageStampSetting(value);
	}

	/*
	 * textStamp (Boolean)
	 */
	public Boolean getTextStamp() {
		return getBooleanValue(KEY_TEXT_STAMP);
	}
	public void setTextStamp(Boolean value) {
		setBooleanValue(KEY_TEXT_STAMP, value);
	}
	public Boolean removeTextStamp() {
		return removeBooleanValue(KEY_TEXT_STAMP);
	}

	/*
	 * textStampSetting (Object)
	 */
	public TextStampSetting getTextStampSetting() {
		Map<String, Object> value = getObjectValue(KEY_TEXT_STAMP_SETTING);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_TEXT_STAMP_SETTING, value);
		}
		return new TextStampSetting(value);
	}
//	public void setTextStampSetting(TextStampSetting value) {
//		throw new UnsupportedOperationException();
//	}
	public TextStampSetting removeTextStampSetting() {
		Map<String, Object> value = removeObjectValue(KEY_TEXT_STAMP_SETTING);
		if (value == null) {
			return null;
		}
		return new TextStampSetting(value);
	}
    
    /*
     * poster (String)
     * @since SmartSDK V2.12
     */
    public String getPoster() {
    	return getStringValue(KEY_POSTER);
    }
    public void setPoster(String value) {
    	setStringValue(KEY_POSTER,value);
    }
    public String removePoster() {
    	return removeStringValue(KEY_POSTER);
    }

    /*
     * doubleCopies (Boolean)
     * @since SmartSDK V2.12
     */
    public Boolean getDoubleCopies() {
    	return getBooleanValue(KEY_DOUBLE_COPIES);
    }
    public void setDoubleCopies(Boolean value) {
    	setBooleanValue(KEY_DOUBLE_COPIES,value);
    }
    public Boolean removeDoubleCopies() {
    	return removeBooleanValue(KEY_DOUBLE_COPIES);
    }

    /*
     * doubleCopiesSeparatorLine (Boolean)
     * @since SmartSDK V2.12
     */
    public Boolean getDoubleCopiesSeparatorLine() {
    	return getBooleanValue(KEY_DOUBLE_COPIES_SEPARATOR_LINE);
    }
    public void setDoubleCopiesSeparatorLine(Boolean value) {
    	setBooleanValue(KEY_DOUBLE_COPIES_SEPARATOR_LINE,value);
    }
    public Boolean removeDoubleCopiesSeparatorLine() {
    	return removeBooleanValue(KEY_DOUBLE_COPIES_SEPARATOR_LINE);
    }

    /*
     * doubleCopiesSeparatorLineSetting (Object)
     * @since SmartSDK V2.12
     */
    public DoubleCopiesSeparatorLineSetting getDoubleCopiesSeparatorLineSetting() {
    	
    	Map<String, Object> value = getObjectValue(KEY_DOUBLE_COPIES_SEPARATOR_LINE_SETTING);
    	if(value == null) {
    		value = Utils.createElementMap();
			setObjectValue(KEY_DOUBLE_COPIES_SEPARATOR_LINE_SETTING, value);
    	}
    	return new DoubleCopiesSeparatorLineSetting(value);
    }
    public DoubleCopiesSeparatorLineSetting removeDoubleCopiesSeparatorLineSetting() {
    	
    	Map<String, Object> value = removeObjectValue(KEY_DOUBLE_COPIES_SEPARATOR_LINE_SETTING);
    	if(value == null) {
    		return null;
    	}
    	return new DoubleCopiesSeparatorLineSetting(value);
    }

    /*
     * colorBalanceYellow (Number)
     * @since SmartSDK V2.12
     */
    public Integer getColorBalanceYellow() {
    	return getNumberValue(KEY_COLOR_BALANCE_YELLOW);
    }
    public void setColorBalanceYellow(Integer value) {
    	setNumberValue(KEY_COLOR_BALANCE_YELLOW,value);
    }
    public Integer removeColorBalanceYellow() {
    	return removeNumberValue(KEY_COLOR_BALANCE_YELLOW);
    }

    /*
     * colorBalanceMagenta (Number)
     * @since SmartSDK V2.12
     */
    public Integer getColorBalanceMagenta() {
    	return getNumberValue(KEY_COLOR_BALANCE_MAGENTA);
    }
    public void setColorBalanceMagenta(Integer value) {
    	setNumberValue(KEY_COLOR_BALANCE_MAGENTA, value);
    }
    public Integer removeColorBalanceMagenta() {
    	return removeNumberValue(KEY_COLOR_BALANCE_MAGENTA);
    }
   
    /*
     * colorBalanceCyan (Number)
     * @since SmartSDK V2.12
     */
    public Integer getColorBalanceCyan() {
    	return getNumberValue(KEY_COLOR_BALANCE_CYAN);
    }
    public void setColorBalanceCyan(Integer value) {
    	setNumberValue(KEY_COLOR_BALANCE_CYAN,value);
    }
    public Integer removeColorBalanceCyan() {
    	return removeNumberValue(KEY_COLOR_BALANCE_CYAN);
    }

    /*
     * colorBalanceBlack (Number)
     * @since SmartSDK V2.12
     */
    public Integer getColorBalanceBlack() {
    	return getNumberValue(KEY_COLOR_BALANCE_BLACK);
    }
    public void setColorBalanceBlack(Integer value) {
    	setNumberValue(KEY_COLOR_BALANCE_BLACK,value);
    }
    public Integer removeColorBalanceBlack() {
    	return removeNumberValue(KEY_COLOR_BALANCE_BLACK);
    }

    /*
     * adjustColor (Boolean)
     * @since SmartSDK V2.12
     */
    public Boolean getAdjustColor() {
    	return getBooleanValue(KEY_ADJUST_COLOR);
    }
    public void setAdjustColor(Boolean value) {
    	setBooleanValue(KEY_ADJUST_COLOR,value);
    }
    public Boolean removeAdjustColor() {
    	return removeBooleanValue(KEY_ADJUST_COLOR);
    }

    /*
     * adjustColorSetting (Array[Object])
     * @since SmartSDK V2.12
     */
    public AdjustColorSettingArray getAdjustColorSetting() {
    	List<Map<String, Object>> value = getArrayValue(KEY_ADJUST_COLOR_SETTING);
    	if(value == null) {
    		value = Utils.createElementList();
			setArrayValue(KEY_ADJUST_COLOR_SETTING, value);
		}
		return new AdjustColorSettingArray(value);
    }
    public AdjustColorSettingArray removeAdjustColorSetting() {
    	List<Map<String, Object>> value = removeArrayValue(KEY_ADJUST_COLOR_SETTING);
    	if(value == null) {
    		return null;
    	}
    	return new AdjustColorSettingArray(value);
    }

    /*
     * unauthorizedCopyPrevention (Boolean)
     * @since SmartSDK V2.12
     */
    public Boolean getUnauthorizedCopyPrevention() {
    	return getBooleanValue(KEY_UNAUTHORIZED_COPY_PREVENTION);
    }
    public void setUnauthorizedCopyPrevention(Boolean value) {
    	setBooleanValue(KEY_UNAUTHORIZED_COPY_PREVENTION,value);
    }
    public Boolean removeUnauthorizedCopyPrevention() {
    	return removeBooleanValue(KEY_UNAUTHORIZED_COPY_PREVENTION);
    }

    /*
     * unauthorizedCopyPreventionSetting (Object)
     * @since SmartSDK V2.12
     */
    public UnauthorizedCopyPreventionSetting getUnauthorizedCopyPreventionSetting() {
    	
    	Map<String, Object> value = getObjectValue(KEY_UNAUTHORIZED_COPY_PREVENTION_SETTING);
    	if(value == null) {
    		value = Utils.createElementMap();
			setObjectValue(KEY_UNAUTHORIZED_COPY_PREVENTION_SETTING, value);
    	}
    	return new UnauthorizedCopyPreventionSetting(value);
    }
    public UnauthorizedCopyPreventionSetting removeUnauthorizedCopyPreventionSetting() {

    	Map<String, Object> value = removeObjectValue(KEY_UNAUTHORIZED_COPY_PREVENTION_SETTING);
    	if(value == null) {
    		return null;
    	}
    	return new UnauthorizedCopyPreventionSetting(value);
    }

    /*
     * halfFold (Boolean)
     * @since SmartSDK V2.12
     */
    public Boolean getHalfFold() {
    	return getBooleanValue(KEY_HALF_FOLD);
    }
    public void setHalfFold(Boolean value) {
    	setBooleanValue(KEY_HALF_FOLD, value);
    }
    public Boolean removeHalfFold() {
    	return removeBooleanValue(KEY_HALF_FOLD);
    }

    /*
     * halfFoldSetting (Object)
     * @since SmartSDK V2.12
     */
    public HalfFoldSetting getHalfFoldSetting() {

    	Map<String, Object> value = getObjectValue(KEY_HALF_FOLD_SETTING);
    	if(value == null) {
    		value = Utils.createElementMap();
			setObjectValue(KEY_HALF_FOLD_SETTING, value);
    	}
    	return new HalfFoldSetting(value);
    }
    public HalfFoldSetting removeHalfFoldSetting() {

    	Map<String, Object> value = removeObjectValue(KEY_HALF_FOLD_SETTING);
    	if(value == null) {
    		return null;
    	}
    	return new HalfFoldSetting(value);
    }
    
    /*
     * sampleCopy (Boolean)
     * @since SmartSDK v2.30
     */
	public Boolean getSampleCopy() {
		return getBooleanValue(KEY_SAMPLE_COPY);
	}
	public void setSampleCopy(Boolean value) {
		setBooleanValue(KEY_SAMPLE_COPY, value);
	}
	public Boolean removeSampleCopy() {
		return removeBooleanValue(KEY_SAMPLE_COPY);
	}
	
	/*
	 * 3EdgesFullBleed (Boolean)
	 * @since SmartSDK v2.30
	 */
	public Boolean get3EdgesFullBleed() {
		return getBooleanValue(KEY_3_EDGES_FULL_BLEED);
	}
	public void set3EdgesFullBleed(Boolean value) {
		setBooleanValue(KEY_3_EDGES_FULL_BLEED, value);
	}
	public Boolean remove3EdgesFullBleed() {
		return removeBooleanValue(KEY_3_EDGES_FULL_BLEED);
	}
	
	/*
	 * thinPaper (Boolean)
	 * @since SmartSDK v2.40
	 */
	public Boolean getThinPaper() {
		return getBooleanValue(KEY_THIN_PAPER);
	}
	public void setThinPaper(Boolean value) {
		setBooleanValue(KEY_THIN_PAPER, value);
	}
	public Boolean removeThinPaper() {
		return removeBooleanValue(KEY_THIN_PAPER);
	}
		
	/*
	 * multiFold (String)
	 * @since SmartSDK v2.30
	 */
	public String getMultiFold() {
		return getStringValue(KEY_MULTI_FOLD);
	}
	public void setMultiFold(String value) {
		setStringValue(KEY_MULTI_FOLD, value);
	}
	public String removeMultiFold() {
		return removeStringValue(KEY_MULTI_FOLD);
	}
	
	/*
	 * multiFoldSetting (Object)
	 * @since SmartSDK v2.30
	 */
	public MultiFoldSetting getMultiFoldSetting() {
		Map<String, Object> value = getObjectValue(KEY_MULTI_FOLD_SETTING);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_MULTI_FOLD_SETTING, value);
		}
		return new MultiFoldSetting(value);
	}
	public MultiFoldSetting removeMultiFoldSetting() {
		Map<String, Object> value = removeObjectValue(KEY_MULTI_FOLD_SETTING);
		if (value == null) {
			return null;
		}
		return new MultiFoldSetting(value);
	}
	
	/*
	 * adjustPrintDensity (Number)
	 * @since SmartSDK v2.40
	 */
	public Integer getAdjustPrintDensity() {
    	return getNumberValue(KEY_ADJUST_PRINT_DENSITY);
    }
    public void setAdjustPrintDensity(Integer value) {
    	setNumberValue(KEY_ADJUST_PRINT_DENSITY,value);
    }
    public Integer removeAdjustPrintDensity() {
    	return removeNumberValue(KEY_ADJUST_PRINT_DENSITY);
    }
    
    /*
     * adjustScanPosition (Boolean)
     * @since SmartSDK v2.40
     */
    public Boolean getAdjustScanPosition() {
    	return getBooleanValue(KEY_ADJUST_SCAN_POSITION);
    }
    public void setAdjustScanPosition(Boolean value) {
    	setBooleanValue(KEY_ADJUST_SCAN_POSITION,value);
    }
    public Boolean removeAdjustScanPosition() {
    	return removeBooleanValue(KEY_ADJUST_SCAN_POSITION);
    }
    
    /*
     * adjustScanPositionSetting (Object)
     * @since SmartSDK v2.40
     */
    public AdjustScanPositionSetting getAdjustScanPositionSetting() {
		Map<String, Object> value = getObjectValue(KEY_ADJUST_SCAN_POSITION_SETTING);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_ADJUST_SCAN_POSITION_SETTING, value);
		}
		return new AdjustScanPositionSetting(value);
	}
    public AdjustScanPositionSetting removeAdjustScanPositionSetting() {
		Map<String, Object> value = removeObjectValue(KEY_ADJUST_SCAN_POSITION_SETTING);
		if (value == null) {
			return null;
		}
		return new AdjustScanPositionSetting(value);
	}
    
    /*
     * creepAdjustment (Boolean)
     * @since SmartSDK v2.40
     */
    public Boolean getCreepAdjustment() {
    	return getBooleanValue(KEY_CREEP_ADJUSTMENT);
    }
    public void setCreepAdjustment(Boolean value) {
    	setBooleanValue(KEY_CREEP_ADJUSTMENT,value);
    }
    public Boolean removeCreepAdjustment() {
    	return removeBooleanValue(KEY_CREEP_ADJUSTMENT);
    }
    
    /*
     * creepAdjustmentSetting (Object)
     * @since SmartSDK v2.40
     */
    public CreepAdjustmentSetting getCreepAdjustmentSetting() {
		Map<String, Object> value = getObjectValue(KEY_CREEP_ADJUSTMENT_SETTING);
		if (value == null) {
			value = Utils.createElementMap();
			setObjectValue(KEY_CREEP_ADJUSTMENT_SETTING, value);
		}
		return new CreepAdjustmentSetting(value);
	}
    public CreepAdjustmentSetting removeCreepAdjustmentSetting() {
    	Map<String, Object> value = removeObjectValue(KEY_CREEP_ADJUSTMENT_SETTING);
    	if(value == null) {
    		return null;
    	}
    	return new CreepAdjustmentSetting(value);
    }
    
    /*
     * insertSeparationSheet (String)
     * @since SmartSDK v2.40
     */
    public String getInsertSeparationSheet() {
		return getStringValue(KEY_INSERT_SEPARATION_SHEET);
	}
	public void setInsertSeparationSheet(String value) {
		setStringValue(KEY_INSERT_SEPARATION_SHEET, value);
	}
	public String removeInsertSeparationSheet() {
		return removeStringValue(KEY_INSERT_SEPARATION_SHEET);
	}
	
	/*
	 * changeInsertPosition (Number)
	 * @since SmartSDK v2.40
	 */
	public Integer getChangeInsertPosition() {
    	return getNumberValue(KEY_CHANGE_INSERT_POSITION);
    }
    public void setChangeInsertPosition(Integer value) {
    	setNumberValue(KEY_CHANGE_INSERT_POSITION,value);
    }
    public Integer removeChangeInsertPosition() {
    	return removeNumberValue(KEY_CHANGE_INSERT_POSITION);
    }
    
    /*
     * reverseEjection (Boolean)
     * @since SmartSDK v2.40
     */
    public Boolean getReverseEjection() {
    	return getBooleanValue(KEY_REVERSE_EJECTION);
    }
    public void setReverseEjection(Boolean value) {
    	setBooleanValue(KEY_REVERSE_EJECTION,value);
    }
    public Boolean removeReverseEjection() {
    	return removeBooleanValue(KEY_REVERSE_EJECTION);
    }
		
	public static class SpecialColorSetting extends WritableElement {

		private static final String KEY_NON_BLACK_PART_COLOR	= "nonBlackPartColor";
		private static final String KEY_BLACK_PART_COLOR		= "blackPartColor";
		private static final String KEY_SINGLE_COLOR			= "singleColor";
		private static final String KEY_SINGLE_COLOR_DENSITY	= "singleColorDensity";

		SpecialColorSetting(Map<String, Object> values) {
			super(values);
		}

		/*
		 * nonBlackPartColor (String)
		 */
		public String getNonBlackPartColor() {
			return getStringValue(KEY_NON_BLACK_PART_COLOR);
		}
		public void setNonBlackPartColor(String value) {
			setStringValue(KEY_NON_BLACK_PART_COLOR, value);
		}
		public String removeNonBlackPartColor() {
			return removeStringValue(KEY_NON_BLACK_PART_COLOR);
		}

		/*
		 * blackPartColor (String)
		 */
		public String getBlackPartColor() {
			return getStringValue(KEY_BLACK_PART_COLOR);
		}
		public void setBlackPartColor(String value) {
			setStringValue(KEY_BLACK_PART_COLOR, value);
		}
		public String removeBlackPartColor() {
			return removeStringValue(KEY_BLACK_PART_COLOR);
		}

		/*
		 * singleColor (String)
		 */
		public String getSingleColor() {
			return getStringValue(KEY_SINGLE_COLOR);
		}
		public void setSingleColor(String value) {
			setStringValue(KEY_SINGLE_COLOR, value);
		}
		public String removeSingleColor() {
			return removeStringValue(KEY_SINGLE_COLOR);
		}

		/*
		 * singleColorDensity (Number)
		 */
		public Integer getSingleColorDensity() {
			return getNumberValue(KEY_SINGLE_COLOR_DENSITY);
		}
		public void setSingleColorDensity(Integer value) {
			setNumberValue(KEY_SINGLE_COLOR_DENSITY, value);
		}
		public Integer removeSingleColorDensity() {
			return removeNumberValue(KEY_SINGLE_COLOR_DENSITY);
		}
	}

	public static class CombineSeparatorLineSetting extends WritableElement {

		private static final String KEY_LINE_TYPE		= "lineType";
		private static final String KEY_COLOR_TYPE	= "lineColor";

		CombineSeparatorLineSetting(Map<String, Object> value) {
			super(value);
		}

		/*
		 * lineType (String)
		 */
		public String getLineType() {
			return getStringValue(KEY_LINE_TYPE);
		}
		public void setLineType(String value) {
			setStringValue(KEY_LINE_TYPE, value);
		}
		public String removeLineType() {
			return removeStringValue(KEY_LINE_TYPE);
		}

		/*
		 * lineColor (String)
		 */
		public String getLineColor() {
			return getStringValue(KEY_COLOR_TYPE);
		}
		public void setLineColor(String value) {
			setStringValue(KEY_COLOR_TYPE, value);
		}
		public String removeLineColor() {
			return removeStringValue(KEY_COLOR_TYPE);
		}

	}
	
	/*
	 * @since SmartSDK V2.12
	 */
	public static class FineMagnificationSetting extends WritableElement {

		private static final String KEY_HORIZONTAL = "horizontal";
		private static final String KEY_VERTICAL   = "vertical";
		
		FineMagnificationSetting(Map<String, Object> values) {
			super(values);
		}
		
		/*
		 * horizontal (String)
		 */
		public String getHorizontal() {
			return getStringValue(KEY_HORIZONTAL);
		}
		public void setHorizontal(String value) {
			setStringValue(KEY_HORIZONTAL, value);
		}
		public String removeHorizontal() {
			return removeStringValue(KEY_HORIZONTAL);
		}

		/*
		 * vertical (String)
		 */
		public String getVertical() {
			return getStringValue(KEY_VERTICAL);
		}
		public void setVertical(String value) {
			setStringValue(KEY_VERTICAL, value);
		}
		public String removeVertical() {
			return removeStringValue(KEY_VERTICAL);
		}	
	}


	/*
	 * @since SmartSDK V2.12
	 */
	public static class PresetCutSetting extends WritableElement {
		private static final String  KEY_PRESET_CUT_SIZE ="presetCutSize";
		private static final String KEY_PRESET_CUT_LENGTH = "presetCutLength"; // SmartSDK V2.12
		
		PresetCutSetting(Map<String, Object> values) {
			super(values);
		}
		
		
		
		/*
		 * presetCutSize (String)
		 * @since SmartSDK V2.12
		 */
		public String getPresetCutSize() {
			return getStringValue(KEY_PRESET_CUT_SIZE);
		}
	    public void setPresetCutSize(String value) {
	    	setStringValue(KEY_PRESET_CUT_SIZE,value);
	    }
	    public String removePresetCutSize() {
	    	return removeStringValue(KEY_PRESET_CUT_SIZE);
	    }
	    
	    /*
	     * presetCutLength (String)
	     * @Since V2.12
	     */
		public String getPresetCutLength() {
			return getStringValue(KEY_PRESET_CUT_LENGTH);
		}
		public void setPresetCutLength(String value) {
			setStringValue(KEY_PRESET_CUT_LENGTH, value);
		}
		public String removePresetCutLength() {
			return removeStringValue(KEY_PRESET_CUT_LENGTH);
		}
	}

	/*
	 * @since SmartSDK V2.12
	 */
	public static class VariableCutSetting extends WritableElement {
		
		private static final String KEY_VARIABLE_CUT_LENGTH = "variableCutLength";
		
		VariableCutSetting(Map<String, Object> values) {
			super(values);
			
		}
		/*
		 * variableCutLength (String)
		 * @since SmartSDK V2.12
		 */
		public String getVariableCutLength() {
			return getStringValue(KEY_VARIABLE_CUT_LENGTH);
		}
	    public void setVariableCutLength(String value) {
	    	setStringValue(KEY_VARIABLE_CUT_LENGTH,value);
	    }
	    public String removeVariableCutLength() {
	    	return removeStringValue(KEY_VARIABLE_CUT_LENGTH);
	    }
	}

	/*
	 * @since SmartSDK V2.12
	 */
	public static class EraseSetting extends WritableElement {
		
		private static final String KEY_ERASE_X_1 = "eraseX1";
	    private static final String KEY_ERASE_Y_1 = "eraseY1";
	    private static final String KEY_ERASE_X_2 = "eraseX2";
	    private static final String KEY_ERASE_Y_2 = "eraseY2";
	    
	    EraseSetting(Map<String, Object> values) {
			super(values);
		}
	    /*
	     * eraseX1 (String)
	     * @since SmartSDK V2.12
	     */
	    public String getEraseX1() {
	    	return getStringValue(KEY_ERASE_X_1);
	    }
	    public void setEraseX1(String value) {
	    	setStringValue(KEY_ERASE_X_1, value);
	    }
	    public String removeEraseX1() {
	    	return removeStringValue(KEY_ERASE_X_1);
	    }

	    /*
	     * eraseY1 (String)
	     * @since SmartSDK V2.12
	     */
	    public String getEraseY1() {
	    	return getStringValue(KEY_ERASE_Y_1);
	    }
	    public void setEraseY1(String value) {
	    	setStringValue(KEY_ERASE_Y_1, value);
	    }
	    public String removeEraseY1() {
	    	return removeStringValue(KEY_ERASE_Y_1);
	    }
	    
	    /*
	     * eraseX2 (String)
	     * @since SmartSDK V2.12
	     */
	    public String getEraseX2() {
	    	return getStringValue(KEY_ERASE_X_2);
	    }
	    public void setEraseX2(String value) {
	    	setStringValue(KEY_ERASE_X_2, value);
	    }
	    public String removeEraseX2() {
	    	return removeStringValue(KEY_ERASE_X_2);
	    }

	    
	    /*
	     * eraseY2 (String)
	     * @since SmartSDK V2.12
	     */
	    public String getEraseY2() {
	    	return getStringValue(KEY_ERASE_Y_2);
	    }
	    public void setEraseY2(String value) {
	    	setStringValue(KEY_ERASE_Y_2, value);
	    }
	    public String removeEraseY2() {
	    	return removeStringValue(KEY_ERASE_Y_2);
	    }
	    
	}

	public static class EraseCenterSetting extends WritableElement {

		private static final String KEY_ERASE_WIDTH			= "eraseWidth";

		EraseCenterSetting(Map<String, Object> values) {
			super(values);
		}

		/*
		 * eraseWidth (String)
		 */
		public String getEraseWidth() {
			return getStringValue(KEY_ERASE_WIDTH);
		}
		public void setEraseWidth(String value) {
			setStringValue(KEY_ERASE_WIDTH, value);
		}
		public String removeEraseWidth() {
			return removeStringValue(KEY_ERASE_WIDTH);
		}

	}

	public static class EraseBorderSetting extends WritableElement {

		private static final String KEY_ERASE_WIDTH_LEFT		= "eraseWidthLeft";
		private static final String KEY_ERASE_WIDTH_RIGHT		= "eraseWidthRight";
		private static final String KEY_ERASE_WIDTH_TOP			= "eraseWidthTop";
		private static final String KEY_ERASE_WIDTH_BOTTOM		= "eraseWidthBottom";

		EraseBorderSetting(Map<String, Object> values) {
			super(values);
		}

		/*
		 * eraseWidthLeft (String)
		 */
		public String getEraseWidthLeft() {
			return getStringValue(KEY_ERASE_WIDTH_LEFT);
		}
		public void setEraseWidthLeft(String value) {
			setStringValue(KEY_ERASE_WIDTH_LEFT, value);
		}
		public String removeEraseWidthLeft() {
			return removeStringValue(KEY_ERASE_WIDTH_LEFT);
		}

		/*
		 * eraseWidthRight (String)
		 */
		public String getEraseWidthRight() {
			return getStringValue(KEY_ERASE_WIDTH_RIGHT);
		}
		public void setEraseWidthRight(String value) {
			setStringValue(KEY_ERASE_WIDTH_RIGHT, value);
		}
		public String removeEraseWidthRight() {
			return removeStringValue(KEY_ERASE_WIDTH_RIGHT);
		}

		/*
		 * eraseWidthTop (String)
		 */
		public String getEraseWidthTop() {
			return getStringValue(KEY_ERASE_WIDTH_TOP);
		}
		public void setEraseWidthTop(String value) {
			setStringValue(KEY_ERASE_WIDTH_TOP, value);
		}
		public String removeEraseWidthTop() {
			return removeStringValue(KEY_ERASE_WIDTH_TOP);
		}

		/*
		 * eraseWidthBottom (String)
		 */
		public String getEraseWidthBottom() {
			return getStringValue(KEY_ERASE_WIDTH_BOTTOM);
		}
		public void setEraseWidthBottom(String value) {
			setStringValue(KEY_ERASE_WIDTH_BOTTOM, value);
		}
		public String removeEraseWidthBottom() {
			return removeStringValue(KEY_ERASE_WIDTH_BOTTOM);
		}

	}

	
	public static class MarginSetting extends WritableElement {

		private static final String KEY_MARGIN_WIDTH_FRONT_LEFT_RIGHT		= "marginWidthFrontLeftRight";
		private static final String KEY_MARGIN_WIDTH_FRONT_TOP_BOTTOM		= "marginWidthFrontTopBottom";
		private static final String KEY_MARGIN_WIDTH_BACK_LEFT_RIGHT		= "marginWidthBackLeftRight";
		private static final String KEY_MARGIN_WIDTH_BACK_TOP_BOTTOM		= "marginWidthBackTopBottom";

		MarginSetting(Map<String, Object> values) {
			super(values);
		}

		/*
		 * marginWidthFrontLeftRight (String)
		 */
		public String getMarginWidthFrontLeftRight() {
			return getStringValue(KEY_MARGIN_WIDTH_FRONT_LEFT_RIGHT);
		}
		public void setMarginWidthFrontLeftRight(String value) {
			setStringValue(KEY_MARGIN_WIDTH_FRONT_LEFT_RIGHT, value);
		}
		public String removeMarginWidthFrontLeftRight() {
			return removeStringValue(KEY_MARGIN_WIDTH_FRONT_LEFT_RIGHT);
		}

		/*
		 * marginWidthFrontTopBottom (String)
		 */
		public String getMarginWidthFrontTopBottom() {
			return getStringValue(KEY_MARGIN_WIDTH_FRONT_TOP_BOTTOM);
		}
		public void setMarginWidthFrontTopBottom(String value) {
			setStringValue(KEY_MARGIN_WIDTH_FRONT_TOP_BOTTOM, value);
		}
		public String removeMarginWidthFrontTopBottom() {
			return removeStringValue(KEY_MARGIN_WIDTH_FRONT_TOP_BOTTOM);
		}

		/*
		 * marginWidthBackLeftRight (String)
		 */
		public String getMarginWidthBackLeftRight() {
			return getStringValue(KEY_MARGIN_WIDTH_BACK_LEFT_RIGHT);
		}
		public void setMarginWidthBackLeftRight(String value) {
			setStringValue(KEY_MARGIN_WIDTH_BACK_LEFT_RIGHT, value);
		}
		public String removeMarginWidthBackLeftRight() {
			return removeStringValue(KEY_MARGIN_WIDTH_BACK_LEFT_RIGHT);
		}

		/*
		 * marginWidthBackTopBottom (String)
		 */
		public String getMarginWidthBackTopBottom() {
			return getStringValue(KEY_MARGIN_WIDTH_BACK_TOP_BOTTOM);
		}
		public void setMarginWidthBackTopBottom(String value) {
			setStringValue(KEY_MARGIN_WIDTH_BACK_TOP_BOTTOM, value);
		}
		public String removeMarginWidthBackTopBottom() {
			return removeStringValue(KEY_MARGIN_WIDTH_BACK_TOP_BOTTOM);
		}

	}
	
	/*
	 * @since SmartSDK V2.12
	 */
	public static class PartialCopySetting extends WritableElement {
		
		private static final String KEY_SCAN_START_POSITION = "scanStartPosition";
		private  static final String KEY_COPY_SIZE = "copySize";
		
		PartialCopySetting(Map<String, Object> values) {
			super(values);
		}
		/*
		 * scanStartPosition (String)
		 * @since SmartSDK V2.12
		 */
		public String getScanStartPosition() {
			return getStringValue(KEY_SCAN_START_POSITION);
		}
		public void setScanStartPosition(String value) {
			setStringValue(KEY_SCAN_START_POSITION,value);
		}
		public String removeScanStartPosition() {
			return removeStringValue(KEY_SCAN_START_POSITION);
		}
		
		/*
		 * copySize (String)
		 * @since SmartSDK V2.12
		 */
		public String getCopySize() {
			return getStringValue(KEY_COPY_SIZE);
		}
		public void setCopySize(String value) {
			setStringValue(KEY_COPY_SIZE, value);
		}
		public String removeCopySize() {
			return removeStringValue(KEY_COPY_SIZE);
		}
	}
	
	/*
	 * @since SmartSDK V2.12
	 */
	public static class  AdjustPrintPositionSetting extends WritableElement {
		
		private static final String KEY_ADJUST_LEFT_RIGHT ="adjustLeftRight";
		private static final String KEY_ADJUST_TOP_BOTTOM = "adjustTopBottom";
		
		AdjustPrintPositionSetting(Map<String, Object> values) {
			super(values);
		}
		/*
		 * adjustLeftRight (String)
		 * @since SmartSDK V2.12
		 */
		public String getAdjustLeftRight() {
			return getStringValue(KEY_ADJUST_LEFT_RIGHT);
		}
	    public void setAdjustLeftRight(String value) {
	    	setStringValue(KEY_ADJUST_LEFT_RIGHT, value);
	    }
	    public String removeAdjustLeftRight() {
	    	return removeStringValue(KEY_ADJUST_LEFT_RIGHT);
	    }

	    /*
	     * adjustTopbottom (String)
	     * @since SmartSDK V2.12
	     */
	    public String getAdjustTopBottom() {
	    	return getStringValue(KEY_ADJUST_TOP_BOTTOM);
	    }
	    public void setAdjustTopBottom(String value) {
	    	setStringValue(KEY_ADJUST_TOP_BOTTOM,value);
	    }
	    public String removeAdjustTopBottom() {
	    	return removeStringValue(KEY_ADJUST_TOP_BOTTOM);
	    }
	}

	/*
	 * @since SmartSDK V2.12
	 */
	public static class MarginAdjustSetting extends WritableElement { 
		
		private static final String KEY_MARGIN_TOP ="marginTop";
		private static final String KEY_MARGIN_BOTTOM = "marginBottom";
		
		MarginAdjustSetting(Map<String, Object> values) {
			super(values);
		}
		/*
		 * marginTop (String)
		 * @since SmartSDK V2.12
		 */
		public String getMarginTop() {
			return getStringValue(KEY_MARGIN_TOP);
		}
	    public void setMarginTop(String value) {
	    	setStringValue(KEY_MARGIN_TOP, value);
	    }
	    public String removeMarginTop() {
	    	return removeStringValue(KEY_MARGIN_TOP);
	    }

	    /*
	     * marginBotton (String)
	     * @since SmartSDK V2.12
	     */
	    public String getMarginBottom() {
	    	return getStringValue(KEY_MARGIN_BOTTOM);
	    }
	    public void setMarginBottom(String value) {
	    	setStringValue(KEY_MARGIN_BOTTOM,value);
	    }
	    public String removeMarginBottom() {
	    	return removeStringValue(KEY_MARGIN_BOTTOM);
	    }
	}

	/*
	 * @since SmartSDK V2.12
	 */
	public static class BackgroundNumberingSetting extends WritableElement {
		
		private static final String KEY_START_NUMBER = "startNumber";
		private static final String KEY_SIZE = "size";
		private static final String KEY_DENSITY = "density";
		private static final String KEY_COLOR ="color";
		
		BackgroundNumberingSetting(Map<String, Object> values) {
			super(values);
		}
		/*
		 * startNumber (Number)
		 * @since SmartSDK V2.12
		 */
		public Integer getStartNumber() {
			return getNumberValue(KEY_START_NUMBER);
		}
	    public void setStartNumber(Integer value) {
	    	setNumberValue(KEY_START_NUMBER, value);
	    }
	    public Integer removeStartNumber() {
	    	return removeNumberValue(KEY_START_NUMBER);
	    }

	    /*
	     * size (String)
	     * @since SmartSDK V2.12
	     */
	    public String getSize() {
	    	return getStringValue(KEY_SIZE);
	    }
	    public void setSize(String value){
	    	setStringValue(KEY_SIZE, value);
	    }
	    public String removeSize() {
	    	return removeStringValue(KEY_SIZE);
	    }

	    /*
	     * density (String)
	     * @since SmartSDK V2.12
	     */
	    public String getDensity() {
	    	return getStringValue(KEY_DENSITY);
	    }
	    public void setDensity(String value) {
	    	setStringValue(KEY_DENSITY,value);
	    }
	    public String removeDensity() {
	    	return removeStringValue(KEY_DENSITY);
	    }

	    /*
	     * color (String)
	     * @since SmartSDK V2.12
	     */
	    public String getColor() { 
	    	return getStringValue(KEY_COLOR);
	    }
	    public void setColor(String value) {
	    	setStringValue(KEY_COLOR, value);
	    }
	    public String removeColor() {
	    	return removeStringValue(KEY_COLOR);
	    }
	}

	public static class PresetStampSetting extends WritableElement {

		private static final String KEY_POSITION 		= "position";
		private static final String KEY_COLOR			= "color";
		private static final String KEY_STAMP_KIND		= "stampKind";
		private static final String KEY_PAGE			= "page";
		private static final String KEY_STAMP_SIZE		= "stampSize";
		private static final String KEY_STAMP_DENSITY	= "stampDensity";

		PresetStampSetting(Map<String, Object> values) {
			super(values);
		}

		/*
		 * position (String)
		 */
		public String getPosition() {
			return getStringValue(KEY_POSITION);
		}
		public void setPosition(String value) {
			setStringValue(KEY_POSITION, value);
		}
		public String removePosition() {
			return removeStringValue(KEY_POSITION);
		}

		/*
		 * color (String)
		 */
		public String getColor() {
			return getStringValue(KEY_COLOR);
		}
		public void setColor(String value) {
			setStringValue(KEY_COLOR, value);
		}
		public String removeColor() {
			return removeStringValue(KEY_COLOR);
		}

		/*
		 * stampKind (String)
		 */
		public String getStampKind() {
			return getStringValue(KEY_STAMP_KIND);
		}
		public void setStampKind(String value) {
			setStringValue(KEY_STAMP_KIND, value);
		}
		public String removeStampKind() {
			return removeStringValue(KEY_STAMP_KIND);
		}

		/*
		 * page (String)
		 */
		public String getPage() {
			return getStringValue(KEY_PAGE);
		}
		public void setPage(String value) {
			setStringValue(KEY_PAGE, value);
		}
		public String removePage() {
			return removeStringValue(KEY_PAGE);
		}

		/*
		 * stampSize (Number)
		 */
		public Integer getStampSize() {
			return getNumberValue(KEY_STAMP_SIZE);
		}
		public void setStampSize(Integer value) {
			setNumberValue(KEY_STAMP_SIZE, value);
		}
		public Integer removeStampSize() {
			return removeNumberValue(KEY_STAMP_SIZE);
		}

		/*
		 * stampDensity (String)
		 */
		public String getStampDensity() {
			return getStringValue(KEY_STAMP_DENSITY);
		}
		public void setStampDensity(String value) {
			setStringValue(KEY_STAMP_DENSITY, value);
		}
		public String removeStampDensity() {
			return removeStringValue(KEY_STAMP_DENSITY);
		}

	}

	public static class UserStampSetting extends WritableElement {

		private static final String KEY_POSITION 		= "position";
		private static final String KEY_COLOR			= "color";
		private static final String KEY_STAMP_KIND		= "stampKind";
		private static final String KEY_PAGE			= "page";

		UserStampSetting(Map<String, Object> values) {
			super(values);
		}

		/*
		 * position (String)
		 */
		public String getPosition() {
			return getStringValue(KEY_POSITION);
		}
		public void setPosition(String value) {
			setStringValue(KEY_POSITION, value);
		}
		public String removePosition() {
			return removeStringValue(KEY_POSITION);
		}

		/*
		 * color (String)
		 */
		public String getColor() {
			return getStringValue(KEY_COLOR);
		}
		public void setColor(String value) {
			setStringValue(KEY_COLOR, value);
		}
		public String removeColor() {
			return removeStringValue(KEY_COLOR);
		}

		/*
		 * stampKind (String)
		 */
		public String getStampKind() {
			return getStringValue(KEY_STAMP_KIND);
		}
		public void setStampKind(String value) {
			setStringValue(KEY_STAMP_KIND, value);
		}
		public String removeStampKind() {
			return removeStringValue(KEY_STAMP_KIND);
		}

		/*
		 * page (String)
		 */
		public String getPage() {
			return getStringValue(KEY_PAGE);
		}
		public void setPage(String value) {
			setStringValue(KEY_PAGE, value);
		}
		public String removePage() {
			return removeStringValue(KEY_PAGE);
		}

	}

	public static class DateStampSetting extends WritableElement {

		private static final String KEY_POSITION 		= "position";
		private static final String KEY_COLOR			= "color";
		private static final String KEY_DATE_FORMAT		= "dateFormat";
		private static final String KEY_PAGE			= "page";
		private static final String KEY_FONT			= "font";
		private static final String KEY_FONT_SIZE		= "fontSize";

		DateStampSetting(Map<String, Object> values) {
			super(values);
		}

		/*
		 * position (String)
		 */
		public String getPosition() {
			return getStringValue(KEY_POSITION);
		}
		public void setPosition(String value) {
			setStringValue(KEY_POSITION, value);
		}
		public String removePosition() {
			return removeStringValue(KEY_POSITION);
		}

		/*
		 * color (String)
		 */
		public String getColor() {
			return getStringValue(KEY_COLOR);
		}
		public void setColor(String value) {
			setStringValue(KEY_COLOR, value);
		}
		public String removeColor() {
			return removeStringValue(KEY_COLOR);
		}

		/*
		 * dateFormat (String)
		 */
		public String getDateFormat() {
			return getStringValue(KEY_DATE_FORMAT);
		}
		public void setDateFormat(String value) {
			setStringValue(KEY_DATE_FORMAT, value);
		}
		public String removeDateFormat() {
			return removeStringValue(KEY_DATE_FORMAT);
		}

		/*
		 * page (String)
		 */
		public String getPage() {
			return getStringValue(KEY_PAGE);
		}
		public void setPage(String value) {
			setStringValue(KEY_PAGE, value);
		}
		public String removePage() {
			return removeStringValue(KEY_PAGE);
		}

		/*
		 * font (String)
		 */
		public String getFont() {
			return getStringValue(KEY_FONT);
		}
		public void setFont(String value) {
			setStringValue(KEY_FONT, value);
		}
		public String removeFont() {
			return removeStringValue(KEY_FONT);
		}

		/*
		 * fontSize (String)
		 */
		public String getFontSize() {
			return getStringValue(KEY_FONT_SIZE);
		}
		public void setFontSize(String value) {
			setStringValue(KEY_FONT_SIZE, value);
		}
		public String removeFontSize() {
			return removeStringValue(KEY_FONT_SIZE);
		}

	}

	public static class PageStampSetting extends WritableElement {

		private static final String KEY_POSITION 		= "position";
		private static final String KEY_COLOR			= "color";
		private static final String KEY_FONT			= "font";
		private static final String KEY_FONT_SIZE		= "fontSize";
		private static final String KEY_PAGE_SETTING	= "pageSetting";

		PageStampSetting(Map<String, Object> values) {
			super(values);
		}

		/*
		 * position (String)
		 */
		public String getPosition() {
			return getStringValue(KEY_POSITION);
		}
		public void setPosition(String value) {
			setStringValue(KEY_POSITION, value);
		}
		public String removePosition() {
			return removeStringValue(KEY_POSITION);
		}

		/*
		 * color (String)
		 */
		public String getColor() {
			return getStringValue(KEY_COLOR);
		}
		public void setColor(String value) {
			setStringValue(KEY_COLOR, value);
		}
		public String removeColor() {
			return removeStringValue(KEY_COLOR);
		}

		/*
		 * font (String)
		 */
		public String getFont() {
			return getStringValue(KEY_FONT);
		}
		public void setFont(String value) {
			setStringValue(KEY_FONT, value);
		}
		public String removeFont() {
			return removeStringValue(KEY_FONT);
		}

		/*
		 * fontSize (String)
		 */
		public String getFontSize() {
			return getStringValue(KEY_FONT_SIZE);
		}
		public void setFontSize(String value) {
			setStringValue(KEY_FONT_SIZE, value);
		}
		public String removeFontSize() {
			return removeStringValue(KEY_FONT_SIZE);
		}

		/*
		 * pageSetting (Object)
		 */
		public PageSetting getPageSetting() {
			Map<String, Object> value = getObjectValue(KEY_PAGE_SETTING);
			if (value == null) {
				value = Utils.createElementMap();
				setObjectValue(KEY_PAGE_SETTING, value);
			}
			return new PageSetting(value);
		}
//		public void setPageSetting(PageSetting value) {
//			throw new UnsupportedOperationException();
//		}
		public PageSetting removePageSetting() {
			Map<String, Object> value = removeObjectValue(KEY_PAGE_SETTING);
			if (value == null) {
				return null;
			}
			return new PageSetting(value);
		}

	}

	public static class PageSetting extends WritableElement {

		private static final String KEY_FORMAT 			= "format";
		private static final String KEY_LANGUAGE 		= "language";// SmartSDK V2.12
		private static final String KEY_FIRST_PAGE		= "firstPage";
		private static final String KEY_FIRST_NUMBER	= "firstNumber";
		private static final String KEY_LAST_NUMBER		= "lastNumber";
		private static final String KEY_TOTAL_PAGE		= "totalPage";

		PageSetting(Map<String, Object> values) {
			super(values);
		}

		/*
		 * format (String)
		 */
		public String getFormat() {
			return getStringValue(KEY_FORMAT);
		}
		public void setFormat(String value) {
			setStringValue(KEY_FORMAT, value);
		}
		public String removeFormat() {
			return removeStringValue(KEY_FORMAT);
		}
		
		/*
		 * language (String)
		 * @since SmartSDK V2.12
		 */
		public String getLanguage() {
			return getStringValue(KEY_LANGUAGE);
		}
		public void setLanguage(String value) {
			setStringValue(KEY_LANGUAGE, value);
		}
		public String removeLanguage() {
			return removeStringValue(KEY_LANGUAGE);
		}

		/*
		 * firstPage (Number)
		 */
		public Integer getFirstPage() {
			return getNumberValue(KEY_FIRST_PAGE);
		}
		public void setFirstPage(Integer value) {
			setNumberValue(KEY_FIRST_PAGE, value);
		}
		public Integer removeFirstPage() {
			return removeNumberValue(KEY_FIRST_PAGE);
		}

		/*
		 * firstNumber (Number)
		 */
		public Integer getFirstNumber() {
			return getNumberValue(KEY_FIRST_NUMBER);
		}
		public void setFirstNumber(Integer value) {
			setNumberValue(KEY_FIRST_NUMBER, value);
		}
		public Integer removeFirstNumber() {
			return removeNumberValue(KEY_FIRST_NUMBER);
		}

		/*
		 * lastNumber (Number)
		 */
		public Integer getLastNumber() {
			return getNumberValue(KEY_LAST_NUMBER);
		}
		public void setLastNumber(Integer value) {
			setNumberValue(KEY_LAST_NUMBER, value);
		}
		public Integer removeLastNumber() {
			return removeNumberValue(KEY_LAST_NUMBER);
		}

		/*
		 * totalPage (Number)
		 */
		public Integer getTotalPage() {
			return getNumberValue(KEY_TOTAL_PAGE);
		}
		public void setTotalPage(Integer value) {
			setNumberValue(KEY_TOTAL_PAGE, value);
		}
		public Integer removeTotalPage() {
			return removeNumberValue(KEY_TOTAL_PAGE);
		}

	}

	public static class TextStampSetting extends WritableElement {

		private static final String KEY_POSITION 		= "position";
		private static final String KEY_COLOR			= "color";
		private static final String KEY_PAGE			= "page";
		private static final String KEY_FONT			= "font";
		private static final String KEY_FONT_SIZE		= "fontSize";
		private static final String KEY_TEXT_STRING		= "textString";
		private static final String KEY_FIRST_NUMBER    = "firstNumber";  		// SmartSDK V2.12

		TextStampSetting(Map<String, Object> values) {
			super(values);
		}

		/*
		 * position (String)
		 */
		public String getPosition() {
			return getStringValue(KEY_POSITION);
		}
		public void setPosition(String value) {
			setStringValue(KEY_POSITION, value);
		}
		public String removePosition() {
			return removeStringValue(KEY_POSITION);
		}

		/*
		 * color (String)
		 */
		public String getColor() {
			return getStringValue(KEY_COLOR);
		}
		public void setColor(String value) {
			setStringValue(KEY_COLOR, value);
		}
		public String removeColor() {
			return removeStringValue(KEY_COLOR);
		}

		/*
		 * page (String)
		 */
		public String getPage() {
			return getStringValue(KEY_PAGE);
		}
		public void setPage(String value) {
			setStringValue(KEY_PAGE, value);
		}
		public String removePage() {
			return removeStringValue(KEY_PAGE);
		}

		/*
		 * font (String)
		 */
		public String getFont() {
			return getStringValue(KEY_FONT);
		}
		public void setFont(String value) {
			setStringValue(KEY_FONT, value);
		}
		public String removeFont() {
			return removeStringValue(KEY_FONT);
		}

		/*
		 * fontSize (String)
		 */
		public String getFontSize() {
			return getStringValue(KEY_FONT_SIZE);
		}
		public void setFontSize(String value) {
			setStringValue(KEY_FONT_SIZE, value);
		}
		public String removeFontSize() {
			return removeStringValue(KEY_FONT_SIZE);
		}

		/*
		 * textString (String)
		 */
		public String getTextString() {
			return getStringValue(KEY_TEXT_STRING);
		}
		public void setTextString(String value) {
			setStringValue(KEY_TEXT_STRING, value);
		}
		public String removeTextString() {
			return removeStringValue(KEY_TEXT_STRING);
		}
		
		/*
		 * firstNumber (String)
		 * @since SmartSDK V2.12
		 */
		public String getFirstNumber()
		{					
			return getStringValue(KEY_FIRST_NUMBER);
		}
	    public void setFirstNumber(String value) {
	    	setStringValue(KEY_FIRST_NUMBER, value);
	    }
	    public String removeFirstNumber() {
	    	return removeStringValue(KEY_FIRST_NUMBER);
	    }

	}

	/*
	 * @since SmartSDK V2.12
	 */	    
	public static class DoubleCopiesSeparatorLineSetting extends WritableElement {
		
		private static final String KEY_LINE_TYPE = "lineType";
		private static final String KEY_LINE_COLOR = "lineColor";

		DoubleCopiesSeparatorLineSetting(Map<String, Object> values) {
			super(values);
		}
		/*
		 * lineType (String)
		 * @since SmartSDK V2.12
		 */
		public String getLineType() {
			return getStringValue(KEY_LINE_TYPE);
		}
	    public void setLineType(String value) {
	    	setStringValue(KEY_LINE_TYPE,value);
	    }
	    public String removeLineType() {
	    	return removeStringValue(KEY_LINE_TYPE);
	    }

	    /*
	     * lineColor (String)
	     * @since SmartSDK V2.12
	     */
	    public String getLineColor() {
	    	return getStringValue(KEY_LINE_COLOR);
	    }
	    public void setLineColor(String value) {
	    	setStringValue(KEY_LINE_COLOR,value);
	    }
	    public String removeLineColor() {
	    	return removeStringValue(KEY_LINE_COLOR);
	    }
	}
	
	/*
	 * @since SmartSDK V2.12
	 */
	public static class AdjustColorSettingArray extends ArrayElement<AdjustColorSetting> {

		protected AdjustColorSettingArray(List<Map<String, Object>> list) {
			super(list);
		}

		public boolean add(AdjustColorSetting value) {
			if (value == null) {
				throw new NullPointerException("value must not be null.");
			}
			return list.add(value.cloneValues());
		}

		public AdjustColorSetting remove(int index) {
			Map<String, Object> value = list.remove(index);
			if (value == null) {
				return null;
			}
			return createElement(value);
		}

		public void clear() {
			list.clear();
		}

		@Override
		protected AdjustColorSetting createElement(Map<String, Object> values) {
			return new AdjustColorSetting(values);
		}
	}
	
	/*
	 * @since SmartSDK V2.12
	 */
	public static class  AdjustColorSetting extends WritableElement {
		
		private static final String KEY_COLOR = "color";
		private static final String KEY_TASTE = "taste";
		
		AdjustColorSetting(Map<String, Object> values) {
			super(values);
		}
		
		/*
		 * color (String)
		 * @since SmartSDK V2.12
		 */
		public String getColor() {
			return getStringValue(KEY_COLOR);
		}
	    public void setColor(String value) {
	    	setStringValue(KEY_COLOR, value);
	    }
	    public String removeColor() {
	    	return removeStringValue(KEY_COLOR);
	    }
		
		/*
		 * taste (Number)
		 * @since SmartSDK V2.12
		 */
	    public Integer getTaste() {
	    	return getNumberValue(KEY_TASTE);
	    }
	    public void setTaste(Integer value) {
	    	setNumberValue(KEY_TASTE, value);
	    }
	    public Integer removeTaste() {
	    	return removeNumberValue(KEY_TASTE);
	    }
	}

	/*
	 * @since SmartSDK V2.12
	 */
	public static class UnauthorizedCopyPreventionSetting extends WritableElement {
		
		private static final String KEY_KIND 			="kind";
		private static final String KEY_EFFECT 			= "effect";
		private static final String KEY_TEXT_KIND 		= "textKind";
		private static final String KEY_USER_TEXT_STRING= "userTextString";
		private static final String KEY_FONT			= "font";
		private static final String KEY_FONT_SIZE 		= "fontSize";
		private static final String KEY_ANGLE 			= "angle";
		private static final String KEY_LINE_SPACING 	= "lineSpacing";
		private static final String KEY_REPEAT 			= "repeat";
		private static final String KEY_POSITION 		= "position";
		private static final String KEY_MASK_PATTERN 	= "maskPattern";
		private static final String KEY_COLOR 			= "color";
		private static final String KEY_DENSITY 		= "density";
		
		UnauthorizedCopyPreventionSetting(Map<String, Object> values) {
			super(values);
		}
		/*
		 * kind (String)
		 * @since SmartSDK V2.12
		 */
		public String getKind() {
			return getStringValue(KEY_KIND);
		}
	    public void setKind(String value) {
	    	setStringValue(KEY_KIND, value);
	    }
	    public String removeKind() {
	    	return removeStringValue(KEY_KIND);
	    }
	    
	    /*
	     * effect (String)
	     * @since SmartSDK V2.12
	     */
	    public String getEffect() {
	    	return getStringValue(KEY_EFFECT);
	    }
	    public void setEffect(String value) {
	    	setStringValue(KEY_EFFECT, value);
	    }
	    public String removeEffect() {
	    	return removeStringValue(KEY_EFFECT);
	    }

	    /*
	     * textKind (String)
	     * @since SmartSDK V2.12
	     */
	    public String getTextKind() {
	    	return getStringValue(KEY_TEXT_KIND);
	    }
	    public void setTextKind(String value) {
	    	setStringValue(KEY_TEXT_KIND,value);
	    }
	    public String removeTextKind() {
	    	return removeStringValue(KEY_TEXT_KIND);
	    }

	    /*
	     * userTextString (String)
	     * @since SmartSDK V2.12
	     */
	    public String getUserTextString() {
	    	return getStringValue(KEY_USER_TEXT_STRING);
	    }
	    public void setUserTextString(String value) {
	    	setStringValue(KEY_USER_TEXT_STRING,value);
	    }
	    public String removeUserTextString() {
	    	return removeStringValue(KEY_USER_TEXT_STRING);
	    }

	    /*
	     * font (String)
	     * @since SmartSDK V2.12
	     */
	    public String getFont() {
	    	return getStringValue(KEY_FONT);
	    }
	    public void setFont(String value) {
	    	setStringValue(KEY_FONT,value);
	    }
	    public String removeFont() {
	    	return removeStringValue(KEY_FONT);
	    }

	    /*
	     * fontSize (Number)
	     * @since SmartSDK V2.12
	     */
	    public Integer getFontSize() {
	    	return getNumberValue(KEY_FONT_SIZE);
	    }
	    public void setFontSize(Integer value) {
	    	setNumberValue(KEY_FONT_SIZE,value);
	    }
	    public Integer removeFontSize() {
	    	return removeNumberValue(KEY_FONT_SIZE);
	    }

	    /*
	     * angle (Number)
	     * @since SmartSDK V2.12
	     */
	    public Integer getAngle() {
	    	return getNumberValue(KEY_ANGLE);
	    }
	    public void setAngle(Integer value) {
	    	setNumberValue(KEY_ANGLE,value);
	    }
	    public Integer removeAngle() {
	    	return removeNumberValue(KEY_ANGLE);
	    }

	    /*
	     * lineSpacing (Number)
	     * @since SmartSDK V2.12
	     */
	    public Integer getLineSpacing() {
	    	return getNumberValue(KEY_LINE_SPACING);
	    }
	    public void setLineSpacing(Integer value) {
	    	setNumberValue(KEY_LINE_SPACING,value);
	    }
	    public Integer removeLineSpacing() {
	    	return removeNumberValue(KEY_LINE_SPACING);
	    }

	    /*
	     * repeat (Boolean)
	     * @since SmartSDK V2.12
	     */
	    public Boolean getRepeat() {
	    	return getBooleanValue(KEY_REPEAT);
	    }
	    public void setRepeat(Boolean value) {
	    	setBooleanValue(KEY_REPEAT,value);
	    }
	    public Boolean removeRepeat() {
	    	return removeBooleanValue(KEY_REPEAT);
	    }

	    /*
	     * position (String)
	     * @since SmartSDK V2.12
	     */
	    public String getPosition() {
	    	return getStringValue(KEY_POSITION);
	    }
	    public void setPosition(String value) {
	    	setStringValue(KEY_POSITION,value);
	    }
	    public String removePosition() {
	    	return removeStringValue(KEY_POSITION);
	    }

	    /*
	     * maskPattern (String)
	     * @since SmartSDK V2.12
	     */
	    public String getMaskPattern() {
	    	return getStringValue(KEY_MASK_PATTERN);
	    }
	    public void setMaskPattern(String value) {
	    	setStringValue(KEY_MASK_PATTERN,value);
	    }
	    public String removeMaskPattern() {
	    	return removeStringValue(KEY_MASK_PATTERN);
	    }

	    /*
	     * color (String)
	     * @since SmartSDK V2.12
	     */
	    public String getColor() {
	    	return getStringValue(KEY_COLOR);
	    }
	    public void setColor(String value) {
	    	setStringValue(KEY_COLOR,value);
	    }
	    public String removeColor() {
	    	return removeStringValue(KEY_COLOR);
	    }

	    /*
	     * density (Number)
	     * @since SmartSDK V2.12
	     */
	    public Integer getDensity() {
	    	return getNumberValue(KEY_DENSITY);
	    }
	    public void setDensity(Integer value) {
	    	setNumberValue(KEY_DENSITY,value);
	    }
	    public Integer removeDensity() {
	    	return removeNumberValue(KEY_DENSITY);
	    }
	}
	
	/*
	 * @since SmartSDK V2.12
	 */
	public static class HalfFoldSetting extends WritableElement {

		private static final String KEY_FOLDING_DIRECTION = "foldingDirection";
		private static final String KEY_MULTI_SHEET_FOLD = "multiSheetFold";
		
		HalfFoldSetting(Map<String, Object> values) {
			super(values);
		}
		/*
		 * foldingDirection (String)
		 * @since SmartSDK V2.12
		 */
		public String getFoldingDirection() {
			return getStringValue(KEY_FOLDING_DIRECTION);
		}
	    public void setFoldingDirection(String value) {
	    	setStringValue(KEY_FOLDING_DIRECTION, value);
	    }
	    public String removeFoldingDirection() {
	    	return removeStringValue(KEY_FOLDING_DIRECTION);
	    }

	    /*
	     * multiSheetFold (Boolean)
	     * @since SmartSDK V2.12
	     */
	    public Boolean getMultiSheetFold() {
	    	return getBooleanValue(KEY_MULTI_SHEET_FOLD);
	    }
	    public void setMultiSheetFold(Boolean value) {
	    	setBooleanValue(KEY_MULTI_SHEET_FOLD, value);
	    }
	    public Boolean removeMultiSheetFold() {
	    	return removeBooleanValue(KEY_MULTI_SHEET_FOLD);
	    }
	}
	
	/*
	 * @since SmartSDK v2.30
	 */
	public static class MultiFoldSetting extends WritableElement {
		
		private static final String KEY_FOLDING_DIRECTION           = "foldingDirection";   //SmartSDK v2.30
		private static final String KEY_HORIZONTAL_VERTICAL_FOLD    = "horizontalVerticalFold";   //SmartSDk v2.30
		private static final String KEY_OPEN_ORIENTATION            = "openOrientation";    //SmartSDK v2.30 
		private static final String KEY_MULTI_SHEET_FOLD              = "multiSheetFold";     //SmartSDK v2.30

		MultiFoldSetting(Map<String, Object> values) {
			super(values);
		}
		
		/*
		 * foldingDirection (String)
		 * @since SmartSDK v2.30
		 */
		public String getFoldingDirection() {
			return getStringValue(KEY_FOLDING_DIRECTION);
		}
		public void setFoldingDirection(String value) {
			setStringValue(KEY_FOLDING_DIRECTION, value);
		}
		public String removeFoldingDirection() {
			return removeStringValue(KEY_FOLDING_DIRECTION);
		}
		
		/*
		 * horizontalVerticalFold (String)
		 * @since SmartSDK v2.30
		 */
		public String getHorizontalVerticalFold() {
			return getStringValue(KEY_HORIZONTAL_VERTICAL_FOLD);
		}
		public void setHorizontalVerticalFold(String value) {
			setStringValue(KEY_HORIZONTAL_VERTICAL_FOLD, value);
		}
		public String removeHorizontalVerticalFold() {
			return removeStringValue(KEY_HORIZONTAL_VERTICAL_FOLD);
		}
		
		/*
		 * openOrientation (String)
		 * @since SmartSDK v2.30
		 */
		public String getOpenOrientation() {
			return getStringValue(KEY_OPEN_ORIENTATION);
		}
		public void setOpenOrientation(String value) {
			setStringValue(KEY_OPEN_ORIENTATION, value);
		}
		public String removeOpenOrientation() {
			return removeStringValue(KEY_OPEN_ORIENTATION);
		}
		
		/*
		 * multiSheetFold (Boolean)
		 * @since SmartSDK v2.30
		 */
		public Boolean getMultiSheetFold() {
			return getBooleanValue(KEY_MULTI_SHEET_FOLD);
		}
		public void setMultiSheetFold(Boolean value) {
			setBooleanValue(KEY_MULTI_SHEET_FOLD, value);
		}
		public Boolean removeMultiSheetFold() {
			return removeBooleanValue(KEY_MULTI_SHEET_FOLD);
		}
		
	}
	
	/*
	 * @since SmartSDK v2.40
	 */
	public static class AdjustScanPositionSetting extends WritableElement{
		private static final String KEY_ADJUST_SCAN_POSITION_WIDTH_FRONT_LEFT_RIGHT    = "adjustScanPositionWidthFrontLeftRight";
		private static final String KEY_ADJUST_SCAN_POSITION_WIDTH_FRONT_TOP_BOTTOM    = "adjustScanPositionWidthFrontTopBottom";
		private static final String KEY_ADJUST_SCAN_POSITION_WIDTH_BACK_LEFT_RIGHT     = "adjustScanPositionWidthBackLeftRight";
		private static final String KEY_ADJUST_SCAN_POSITION_WIDTH_BACK_TOP_BOTTOM     = "adjustScanPositionWidthBackTopBottom";

		AdjustScanPositionSetting(Map<String, Object> values) {
			super(values);
		}
		
		/*
		 * adjustScanPositionWidthFrontLeftRight (String)
		 */
		public String getAdjustScanPositionWidthFrontLeftRight() {
			return getStringValue(KEY_ADJUST_SCAN_POSITION_WIDTH_FRONT_LEFT_RIGHT);
		}
		public void setAdjustScanPositionWidthFrontLeftRight(String value) {
			setStringValue(KEY_ADJUST_SCAN_POSITION_WIDTH_FRONT_LEFT_RIGHT, value);
		}
		public String removeAdjustScanPositionWidthFrontLeftRight() {
			return removeStringValue(KEY_ADJUST_SCAN_POSITION_WIDTH_FRONT_LEFT_RIGHT);
		}
		
		/*
		 * adjustScanPositionWidthFrontTopBottom (String)
		 */
		public String getAdjustScanPositionWidthFrontTopBottom() {
			return getStringValue(KEY_ADJUST_SCAN_POSITION_WIDTH_FRONT_TOP_BOTTOM);
		}
		public void setAdjustScanPositionWidthFrontTopBottom(String value) {
			setStringValue(KEY_ADJUST_SCAN_POSITION_WIDTH_FRONT_TOP_BOTTOM, value);
		}
		public String removeAdjustScanPositionWidthFrontTopBottom() {
			return removeStringValue(KEY_ADJUST_SCAN_POSITION_WIDTH_FRONT_TOP_BOTTOM);
		}
		
		/*
		 * adjustScanPositionWidthBackLeftRight (String)
		 */
		public String getAdjustScanPositionWidthBackLeftRight() {
			return getStringValue(KEY_ADJUST_SCAN_POSITION_WIDTH_BACK_LEFT_RIGHT);
		}
		public void setAdjustScanPositionWidthBackLeftRight(String value) {
			setStringValue(KEY_ADJUST_SCAN_POSITION_WIDTH_BACK_LEFT_RIGHT, value);
		}
		public String removeAdjustScanPositionWidthBackLeftRight() {
			return removeStringValue(KEY_ADJUST_SCAN_POSITION_WIDTH_BACK_LEFT_RIGHT);
		}
		
		/*
		 * adjustScanPositionWidthBackTopBottom (String)
		 */
		public String getAdjustScanPositionWidthBackTopBottom() {
			return getStringValue(KEY_ADJUST_SCAN_POSITION_WIDTH_BACK_TOP_BOTTOM);
		}
		public void setAdjustScanPositionWidthBackTopBottom(String value) {
			setStringValue(KEY_ADJUST_SCAN_POSITION_WIDTH_BACK_TOP_BOTTOM, value);
		}
		public String removeAdjustScanPositionWidthBackTopBottom() {
			return removeStringValue(KEY_ADJUST_SCAN_POSITION_WIDTH_BACK_TOP_BOTTOM);
		}
		
	}
	
	/*
	 * @since SmartSDK v2.40
	 */
	public static class CreepAdjustmentSetting extends WritableElement{
		private static final String KEY_ADJUSTMENT_VALUE      = "adjustmentValue"; 

		CreepAdjustmentSetting(Map<String, Object> values) {
			super(values);
		}
		
		/*
		 * adjustmentValue (String)
		 */
		public String getAdjustmentValue() {
			return getStringValue(KEY_ADJUSTMENT_VALUE);
		}
		public void setAdjustmentValue(String value) {
			setStringValue(KEY_ADJUSTMENT_VALUE, value);
		}
		public String removeAdjustmentValue() {
			return removeStringValue(KEY_ADJUSTMENT_VALUE);
		}
		
	}

}
