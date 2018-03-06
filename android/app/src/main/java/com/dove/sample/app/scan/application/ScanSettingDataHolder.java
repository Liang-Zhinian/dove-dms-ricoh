/*
 *  Copyright (C) 2013-2015 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.app.scan.application;

import com.dove.R;
import com.dove.sample.function.scan.ScanService;
import com.dove.sample.function.scan.attribute.standard.FileSetting;
import com.dove.sample.function.scan.attribute.standard.FileSetting.FileFormat;
import com.dove.sample.function.scan.attribute.standard.JobMode;
import com.dove.sample.function.scan.attribute.standard.OriginalPreview;
import com.dove.sample.function.scan.attribute.standard.OriginalSide;
import com.dove.sample.function.scan.attribute.standard.ScanColor;
import com.dove.sample.function.scan.attribute.standard.SendStoredFileSetting;
import com.dove.sample.function.scan.attribute.standard.StoreLocalSetting;
import com.dove.sample.function.scan.supported.FileSettingSupported;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * スキャン設定情報クラスです。
 * Scan setting data class.
 */
public class ScanSettingDataHolder {
	public static String TAG = ScanSettingDataHolder.class.getSimpleName();

	/**
	 * ジョブモードの表示文字列IDと設定値のマップです。
	 * Map of job mode display string ID and setting values.
	 */
	private final static LinkedHashMap<Integer, JobMode> mAllJobModeMap;

    /**
     * 読取カラーの表示文字列IDと設定値のマップです。
     * Map of scan color display string ID and setting values.
     */
	private final static LinkedHashMap<Integer, ScanColor> mAllColorMap;

    /**
     * ファイル形式の表示文字列IDと設定値のマップです。
     * Map of file format display string ID and setting values.
     */
	private final static LinkedHashMap<Integer, FileFormat> mAllFileFormatMap;

    /**
     * マルチページ設定の表示文字列IDと設定値のマップです。
     * Map of multipage setting display string ID and setting values.
     */
	private final static LinkedHashMap<Integer, Boolean> mAllMultiPageMap;

    /**
     * 原稿面の表示文字列IDと設定値のマップです。
     * Map of scan side display string ID and setting values.
     */
	private final static LinkedHashMap<Integer, OriginalSide> mAllSideMap;

    /**
     * プレビュー表示設定の表示文字列IDと設定値のマップです。
     * Map of preview setting display string ID and setting values.
     */
	private final static LinkedHashMap<Integer, OriginalPreview> mAllPreviewMap;


	/**
	 * ジョブモード設定可能値の表示文字列IDのリストです。
	 * List of display string ID for the available job mode setting values.
	 */
	private List<Integer> mSupportedJobModeLabelList;

    /**
     * 読取カラー設定可能値の表示文字列IDのリストです。
     * List of display string ID for the available scan color setting values.
     */
	private List<Integer> mSupportedColorLabelList;

    /**
     * ファイル設定の設定可能値の表示文字列IDのリストです。
     * List of display string ID for the available file setting values.
     */
	private List<Integer> mSupportedFileSettingLabelList;

    /**
     *原稿面設定可能値の表示文字列IDのリストです。
     * List of display string ID for the available scan side setting values.
     */
	private List<Integer> mSupportedSideLabelList;

    /**
     * プレビュー表示設定可能値の表示文字列IDのリストです。
     * List of display string ID for the available preview setting values.
     */
	private List<Integer> mSupportedPreviewLabelList;


	/**
	 * 選択中のジョブモード設定値の表示文字列IDです。
	 * Display string ID of the selected job mode setting value.
	 */
	private int mSelectedJobModeLabel;

    /**
     * 選択中の読取カラー設定値の表示文字列IDです。
     * Display string ID of the selected scan color setting value.
     */
	private int mSelectedColorLabel;

    /**
     * 選択中のファイル設定値の表示文字列IDです。
     * Display string ID of the selected file setting value.
     */
	private int mSelectedFileSettingLabel;

    /**
     * 選択中の原稿面設定値の表示文字列IDです。
     * Display string ID of the selected scan side setting value.
     */
	private int mSelectedSideLabel;

    /**
     * 選択中のプレビュー表示設定値の表示文字列IDです。
     * Display string ID of the selected preview setting value.
     */
	private int mSelectedPreviewLabel;

    /**
     * 各マップの初期化を行います。
     * [処理内容]
     *   (1)ジョブモード設定のマップの初期化
     *   (2)読取カラー設定のマップの初期化
     *   (3)ファイル形式設定のマップの初期化
     *   (4)マルチページ設定のマップの初期化
     *   (5)原稿面設定のマップの初期化
     *   (6)プレビュー表示設定のマップの初期化
     *
     * Initializes maps.
     * [Processes]
     *   (1) Initializes the map for job mode setting
     *   (2) Initializes the map for scan color setting
     *   (3) Initializes the map for file setting
     *   (4) Initializes the map for multipage setting
     *   (5) Initializes the map for scan side setting
     *   (6) Initializes the map for preview setting
     */
	static {

	    //(1)
	    mAllJobModeMap = new LinkedHashMap<Integer, JobMode>() {
	        {
	            put(R.string.txid_scan_b_jobmode_scan_and_send,        JobMode.SCAN_AND_SEND);
                put(R.string.txid_scan_b_jobmode_scan_and_store_local, JobMode.SCAN_AND_STORE_LOCAL);
                put(R.string.txid_scan_b_jobmode_send_stored_file,     JobMode.SEND_STORED_FILE);
	        }

	    };

		//(2)
		mAllColorMap = new LinkedHashMap<Integer, ScanColor>(){
			{
				put(R.string.txid_scan_b_top_mono_text,                 ScanColor.MONOCHROME_TEXT);
				put(R.string.txid_scan_b_top_mono_text_photo,           ScanColor.MONOCHROME_TEXT_PHOTO);
				put(R.string.txid_scan_b_top_mono_text_lineart,         ScanColor.MONOCHROME_TEXT_LINEART);
				put(R.string.txid_scan_b_top_mono_photo,				ScanColor.MONOCHROME_PHOTO);
				put(R.string.txid_scan_b_top_gray_scale,				ScanColor.GRAYSCALE);
				put(R.string.txid_scan_b_top_full_color_text_photo,	    ScanColor.COLOR_TEXT_PHOTO);
				put(R.string.txid_scan_b_top_full_color_glossy_photo,	ScanColor.COLOR_GLOSSY_PHOTO);
				put(R.string.txid_scan_b_top_auto_color_select,         ScanColor.AUTO_COLOR);
			}
		};

		//(3)
		mAllFileFormatMap = new LinkedHashMap<Integer, FileFormat>(){
			{
				put(R.string.txid_scan_b_top_file_stiff_jpg, 	FileFormat.TIFF_JPEG);
				put(R.string.txid_scan_b_top_file_mtiff, 		FileFormat.TIFF_JPEG);
				put(R.string.txid_scan_b_top_file_spdf, 		FileFormat.PDF);
				put(R.string.txid_scan_b_top_file_mpdf, 		FileFormat.PDF);
			}
		};

		//(4)
		mAllMultiPageMap = new LinkedHashMap<Integer, Boolean>(){
			{
				put(R.string.txid_scan_b_top_file_stiff_jpg, 	false);
				put(R.string.txid_scan_b_top_file_mtiff, 		true);
				put(R.string.txid_scan_b_top_file_spdf, 		false);
				put(R.string.txid_scan_b_top_file_mpdf, 		true);
			}
		};

		//(5)
		mAllSideMap = new LinkedHashMap<Integer, OriginalSide>(){
			{
				put(R.string.txid_scan_b_top_one_sided, 		OriginalSide.ONE_SIDE);
				put(R.string.txid_scan_b_top_top_to_top,		OriginalSide.TOP_TO_TOP);
				put(R.string.txid_scan_b_top_top_to_bottom,	OriginalSide.TOP_TO_BOTTOM);
				put(R.string.txid_scan_b_top_spread,			OriginalSide.SPREAD);
			}
		};

		//(6)
		mAllPreviewMap = new LinkedHashMap<Integer, OriginalPreview>() {
		    {
		        put(R.string.txid_scan_b_other_preview_on,     OriginalPreview.ON);
                put(R.string.txid_scan_b_other_preview_off,    OriginalPreview.OFF);
		    }
		};
	}

    /**
     * コンストラクタです。
     * 各設定値の文字列を初期化します。
     *
     * Constructor.
     * Initializes the display string of the setting values.
     */
	public ScanSettingDataHolder() {
	    mSelectedJobModeLabel = R.string.txid_scan_b_jobmode_scan_and_send;
		mSelectedColorLabel = R.string.txid_scan_b_top_mono_text;
		mSelectedFileSettingLabel = R.string.txid_scan_b_top_file_mpdf;
		mSelectedSideLabel = R.string.txid_scan_b_top_one_sided;
		mSelectedPreviewLabel = R.string.txid_scan_b_other_preview_on;

		mSupportedJobModeLabelList = new ArrayList<Integer>();
		mSupportedColorLabelList = new ArrayList<Integer>();
		mSupportedFileSettingLabelList = new ArrayList<Integer>();
		mSupportedSideLabelList = new ArrayList<Integer>();
		mSupportedPreviewLabelList = new ArrayList<Integer>();
	}

    /**
     * ScanServiceから各設定の設定可能値一覧を取得します。
     * 指定されたSmartSDKのAPIバージョンでサポートされていない設定値は除去します。
     * [処理内容]
     *   (1)ジョブモード設定可能値を取得する
     *   (2)読取カラー設定可能値を取得する
     *   (3)ファイル形式設定可能値とマルチページ設定可能値を取得する
     *   (4)原稿面設定可能値を取得する
     *   (5)プレビュー設定可能値を取得する
     *
     * Obtains the list of available setting values from ScanService.
     * Removes the unsupported values on the specified SmartSDK API version from the list.
     * [Processes]
     *   (1) Obtains the available setting values for job mode setting.
     *   (2) Obtains the available setting values for scan color setting.
     *   (3) Obtains the available setting values for file setting and multipage setting.
     *   (4) Obtains the available setting values for scan side setting.
     *   (5) Obtains the available setting values for preview setting.
     */
	public void init(ScanService scanService) {

        //(1)
        @SuppressWarnings("unchecked")
        List<JobMode> jobModeList = (List<JobMode>)scanService.getSupportedAttributeValues(JobMode.class);
        List<JobMode> localJobModeList = new ArrayList<JobMode>();
        if(jobModeList != null){
            if (jobModeList.contains(JobMode.SCAN_AND_SEND)) {
                localJobModeList.add(JobMode.SCAN_AND_SEND);
            }
            if (jobModeList.contains(JobMode.SCAN_AND_STORE_LOCAL)) {
                if (scanService.getSupportedAttributeValues(StoreLocalSetting.class) != null) {
                    localJobModeList.add(JobMode.SCAN_AND_STORE_LOCAL);
                }
            }
            if (jobModeList.contains(JobMode.SEND_STORED_FILE)) {
                if (scanService.getSupportedAttributeValues(SendStoredFileSetting.class) != null) {
                    localJobModeList.add(JobMode.SEND_STORED_FILE);
                }
            }
//			if (jobModeList.contains(JobMode.SCAN_AND_STORE_TEMPORARY)) {
//				if (scanService.getSupportedAttributeValues(SendStoredFileSetting.class) != null) {
//					localJobModeList.add(JobMode.SCAN_AND_STORE_TEMPORARY);
//				}
//			}
        }
        setSupportedJobModeList(localJobModeList);

        //(2)
        @SuppressWarnings("unchecked")
        List<ScanColor> colorList = (List<ScanColor>)scanService.getSupportedAttributeValues(ScanColor.class);
        setSupportedColorList(colorList);

        //(3)
        FileSettingSupported fileSettingSupported = (FileSettingSupported)scanService.getSupportedAttributeValues(FileSetting.class);
        if (fileSettingSupported != null) {
            List<FileFormat> fileFormatList = fileSettingSupported.getFileFormatList();
            List<Boolean> multipageFormatList = fileSettingSupported.getMultipageFormat();
            setSupportedFileSettingList(fileFormatList, multipageFormatList);
        }

        //(4)
        @SuppressWarnings("unchecked")
        List<OriginalSide> originalSideList = (List<OriginalSide>)scanService.getSupportedAttributeValues(OriginalSide.class);
        setSupportedSideList(originalSideList);

        //(5)
        @SuppressWarnings("unchecked")
        List<OriginalPreview> originalPreviewList = (List<OriginalPreview>)scanService.getSupportedAttributeValues(OriginalPreview.class);
        setSupportedPreviewList(originalPreviewList);

	}

	/**
	 * ジョブモード設定可能値の表示文字列IDリストを作成します。
	 * Creates the list of display string ID for the available job mode setting values.
	 *
	 * @param jobModeList ジョブモード設定可能値のリスト
	 *                    List of available job mode setting values
	 */
	private void setSupportedJobModeList(List<JobMode> jobModeList) {
	    mSupportedJobModeLabelList.clear();
	    if (jobModeList != null) {
	        Set<Map.Entry<Integer, JobMode>> entrySet = mAllJobModeMap.entrySet();
            Iterator<Map.Entry<Integer, JobMode>> it = entrySet.iterator();
            while(it.hasNext())
            {
                Map.Entry<Integer, JobMode> entry = it.next();
                if(jobModeList.contains(entry.getValue())) {
                    mSupportedJobModeLabelList.add(entry.getKey());
                }
            }
	    }
	}

    /**
     * 読取カラー設定可能値の表示文字列IDリストを作成します。
     * Creates the list of display string ID for the available scan color setting values.
     *
     * @param colorList 読取カラー設定可能値のリスト
     *                  List of available scan color setting values
     */
	private void setSupportedColorList(List<ScanColor> colorList) {
	    mSupportedColorLabelList.clear();
	    if (colorList != null) {
	        Set<Map.Entry<Integer, ScanColor>> entrySet = mAllColorMap.entrySet();
	        Iterator<Map.Entry<Integer, ScanColor>> it = entrySet.iterator();
	        while(it.hasNext())
	        {
	            Map.Entry<Integer, ScanColor> entry = it.next();
	            if(colorList.contains(entry.getValue())) {
	                mSupportedColorLabelList.add(entry.getKey());
	            }
	        }
	    }
	}

    /**
     * ファイル設定可能値の表示文字列IDリストを作成します。
     * Creates the list of display string ID for the available file setting values.
     *
     * @param fileFormatList ファイル形式設定可能値のリスト
     *                  List of available scan file format setting values
     * @param multiPageFormatList マルチページ設定可能値のリスト
     *                  List of available scan multipage setting values
     */
	private void setSupportedFileSettingList(List<FileFormat> fileFormatList, List<Boolean> multiPageFormatList) {
	    mSupportedFileSettingLabelList.clear();
	    mSupportedFileSettingLabelList.clear();
	    if (fileFormatList != null) {
	        Set<Map.Entry<Integer, FileFormat>> entrySet1 = mAllFileFormatMap.entrySet();
	        Iterator<Map.Entry<Integer, FileFormat>> it1 = entrySet1.iterator();
	        while(it1.hasNext())
	        {
	            Map.Entry<Integer, FileFormat> entry = it1.next();
	            if(fileFormatList.contains(entry.getValue())) {
	                mSupportedFileSettingLabelList.add(entry.getKey());
	            }
	        }
	    }
	    if (multiPageFormatList != null) {
	        Set<Map.Entry<Integer, Boolean>> entrySet2 = mAllMultiPageMap.entrySet();
	        Iterator<Map.Entry<Integer, Boolean>> it2 = entrySet2.iterator();
	        while(it2.hasNext())
	        {
	            Map.Entry<Integer, Boolean> entry = it2.next();
	            if(!multiPageFormatList.contains(entry.getValue())) {
	                mSupportedFileSettingLabelList.remove(entry.getKey());
	            }
	        }
	    }
	}

    /**
     * 原稿面設定可能値の表示文字列IDリストを作成します。
     * Creates the list of display string ID for the available scan side setting values.
     *
     * @param sideList 原稿面設定可能値のリスト
     *                  List of available scan side setting values
     */
	private void setSupportedSideList(List<OriginalSide> sideList) {
	    mSupportedSideLabelList.clear();
	    if (sideList != null) {
	        Set<Map.Entry<Integer, OriginalSide>> entrySet = mAllSideMap.entrySet();
	        Iterator<Map.Entry<Integer, OriginalSide>> it = entrySet.iterator();
	        while(it.hasNext())
	        {
	            Map.Entry<Integer, OriginalSide> entry = it.next();
	            if(sideList.contains(entry.getValue())) {
	                mSupportedSideLabelList.add(entry.getKey());
	            }
	        }
	    }
	}

    /**
     * プレビュー設定可能値の表示文字列IDリストを作成します。
     * Creates the list of display string ID for the available preview setting values.
     *
     * @param previewList プレビュー設定可能値のリスト
     *                  List of available preview setting values
     */
    private void setSupportedPreviewList(List<OriginalPreview> previewList) {
        mSupportedPreviewLabelList.clear();
        if (previewList != null) {
            Set<Map.Entry<Integer, OriginalPreview>> entrySet = mAllPreviewMap.entrySet();
            Iterator<Map.Entry<Integer, OriginalPreview>> it = entrySet.iterator();
            while(it.hasNext())
            {
                Map.Entry<Integer, OriginalPreview> entry = it.next();
                if(previewList.contains(entry.getValue())) {
                    mSupportedPreviewLabelList.add(entry.getKey());
                }
            }
        }
    }

    /**
     * 選択中のジョブモード設定値の表示文字列IDを取得します。
     * Obtains the display string ID of the selected job mode setting value.
     */
    public Integer getSelectedJobModeLabel() {
        return mSelectedJobModeLabel;
    }

    /**
     * 選択中のジョブモード設定値を取得します。
     * Obtains the selected job mode setting value.
     */
    public JobMode getSelectedJobModeValue() {
        return mAllJobModeMap.get(mSelectedJobModeLabel);
    }

    /**
     * 指定されたジョブモード設定値を選択状態にします。
     * Changes the selection state of the specified job mode setting value to "selected".
     */
    public void setSelectedJobMode(Integer id) {
        if (mSupportedJobModeLabelList.contains(id)) {
            mSelectedJobModeLabel = id;
        }
    }


    /**
     * 選択中の読取カラー設定値の表示文字列IDを取得します。
     * Obtains the display string ID of the selected scan color setting value.
     */
	public Integer getSelectedColorLabel() {
		return mSelectedColorLabel;
	}

    /**
     * 選択中の読取カラー設定値を取得します。
     * Obtains the selected scan color setting value.
     */
	public ScanColor getSelectedColorValue() {
		return mAllColorMap.get(mSelectedColorLabel);
	}

    /**
     * 指定された読取カラー設定値を選択状態にします。
     * Changes the selection state of the specified scan color setting value to "selected."
     * @param id
     */
	public void setSelectedColor(Integer id) {
		if(mSupportedColorLabelList.contains(id)) {
			mSelectedColorLabel = id;
		}
	}


    /**
     * 選択中のファイル設定値の表示文字列IDを取得します。
     * Obtains the display string ID of the selected scan color setting value.
     */
	public Integer getSelectedFileSettingLabel() {
		return mSelectedFileSettingLabel;
	}

    /**
     * 選択中のファイル形式設定値を取得します。
     * Obtains the selected scan color setting value.
     */
	public FileFormat getSelectedFileFormatValue() {
		return mAllFileFormatMap.get(mSelectedFileSettingLabel);
	}

    /**
     * 選択中のマルチページ形式設定値を取得します。
     * Obtains the selected multipage setting value.
     */
	public Boolean getSelectedMultiPageValue() {
		return mAllMultiPageMap.get(mSelectedFileSettingLabel);
	}

    /**
     * 指定されたファイル設定値を選択状態にします。
     * Changes the selection state of the specified file setting value to "selected."
     * @param id
     */
	public void setSelectedFileSetting(Integer id) {
		if(mSupportedFileSettingLabelList.contains(id)) {
			mSelectedFileSettingLabel = id;
		}
	}


    /**
     * 選択中の原稿面設定値の表示文字列IDを取得します。
     * Obtains the display string ID of the selected scan side setting value.
     */
	public Integer getSelectedSideLabel() {
		return mSelectedSideLabel;
	}

    /**
     * 選択中の原稿面設定値を取得します。
     * Obtains the selected scan side setting value.
     */
	public OriginalSide getSelectedSideValue() {
		return mAllSideMap.get(mSelectedSideLabel);
	}

    /**
     * 指定された原稿面設定値を選択状態にします。
     * Changes the selection state of the specified scan side setting value to "selected."
     * @param id
     */
	public void setSelectedSide(Integer id) {
		if(mSupportedSideLabelList.contains(id)) {
			mSelectedSideLabel = id;
		}
	}


    /**
     * 選択中のプレビュー表示設定値の表示文字列IDを取得します。
     * Obtains the display string ID of the selected preview setting value.
     */
    public Integer getSelectedPreviewLabel() {
        return mSelectedPreviewLabel;
    }

    /**
     * 選択中のプレビュー表示設定値を取得します。
     * Obtains the selected preview setting value.
     */
    public OriginalPreview getSelectedPreviewValue() {
        return mAllPreviewMap.get(mSelectedPreviewLabel);
    }

    /**
     * 指定されたプレビュー表示設定値を選択状態にします。
     * Changes the selection state of the specified preview setting value to "selected."
     * @param id
     */
    public void setSelectedPreview(Integer id) {
        if(mSupportedPreviewLabelList.contains(id)) {
            mSelectedPreviewLabel = id;
        }
    }

    /**
     * ジョブモード設定可能値のリストを取得します。
     * Obtains the display string ID of the job mode setting value to select
     */
    public List<Integer> getJobModeLabelList() {
        return mSupportedJobModeLabelList;
    }

    /**
     * 読取カラー設定可能値のリストを取得します。
     * Obtains the display string ID of the scan color setting value to select
     */
	public List<Integer> getColorLabelList() {
		return mSupportedColorLabelList;
	}

    /**
     * ファイル設定可能値のリストを取得します。
     * Obtains the display string ID of the file setting value to select
     */
	public List<Integer> getFileSettingLabelList() {
		return mSupportedFileSettingLabelList;
	}

    /**
     * 原稿面設定可能値のリストを取得します。
     * Obtains the display string ID of the scan side setting value to select
     */
	public List<Integer> getSideLabelList() {
		return mSupportedSideLabelList;
	}

    /**
     * プレビュー設定可能値のリストを取得します。
     * Obtains the display string ID of the preview setting value to select
     */
    public List<Integer> getPreviewLabelList() {
        return mSupportedPreviewLabelList;
    }

}
