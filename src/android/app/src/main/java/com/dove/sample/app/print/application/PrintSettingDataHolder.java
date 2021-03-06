/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.app.print.application;

import android.content.res.Resources;
import android.util.Log;

import com.dove.sample.app.print.Const;
import com.dove.sample.function.print.PrintFile;
import com.dove.sample.function.print.attribute.HashPrintRequestAttributeSet;
import com.dove.sample.function.print.attribute.PrintException;
import com.dove.sample.function.print.attribute.PrintRequestAttributeSet;
import com.dove.sample.function.print.attribute.standard.Copies;
import com.dove.sample.function.print.attribute.standard.PrintColor;
import com.dove.sample.function.print.attribute.standard.Staple;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 印刷に必要な各種設定値を定義するクラスです。
 * Print setting data class.
 */
public class PrintSettingDataHolder {

    /**
     * Define the prefix for log information with abbreviation package and class name
     */
    private final static String PREFIX = "activity:PrintSetDatHol:";

    /**
     * 印刷ファイルのPDL
     * PDL setting
     */
    private PrintFile.PDL mSelectedPDL;

    /**
     * ステープルの設定
     * Staple setting
     */
    private Staple mSelectedStapleValue;

    /**
     * 印刷部数
     * Number of copies
     */
    private Copies mSelectedCopiesValue;

    /**
     * 印刷ファイルの名称
     * File name
     */
    private String mSelectedPrintAssetFileName;

    /**
     * 印刷カラー設定
     * Print color setting
     */
    private PrintColor mSelectedPrintColorValue;


    /***********************************************************
     * 公開メソッド
     * public methods
     ***********************************************************/

    /**
     * 指定されたリソースから印刷ファイルのオブジェクトを生成します。
     * Create PrintFile object from the specified resources.
     *
     * @param resources
     * @return
     * @throws PrintException
     */
    public PrintFile getPrintFile(Resources resources) throws PrintException {
        InputStream is = null;
        PrintFile printfile = null;
        try {
            is = resources.getAssets().open(mSelectedPrintAssetFileName);
        } catch (IOException e) {
            Log.w(Const.TAG, PREFIX + e.toString());
            try {
                is = new FileInputStream(mSelectedPrintAssetFileName);
            } catch (FileNotFoundException ex) {
                Log.w(Const.TAG, PREFIX + ex.toString());

                return null;
            }
        }

        printfile = (new PrintFile.Builder()).localFileInputStream(is).pdl(mSelectedPDL).build();
        return printfile;
    }

    public PrintFile getPrintFile() throws PrintException {
        PrintFile printfile = null;
        Log.i(Const.TAG, PREFIX + "printer file path: " + mSelectedPrintAssetFileName);
        printfile = (new PrintFile.Builder()).printerFilePath(mSelectedPrintAssetFileName).pdl(mSelectedPDL).build();
        return printfile;
    }

    /**
     * 現在の設定値からプリント要求用の属性セットを生成します。
     * Create Print request attribute set from current print settings.
     *
     * @return
     */
    public PrintRequestAttributeSet getPrintRequestAttributeSet() {
        PrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();

        if (mSelectedStapleValue != null) attributeSet.add(mSelectedStapleValue);
        if (mSelectedCopiesValue != null) attributeSet.add(mSelectedCopiesValue);
        if (mSelectedPrintColorValue != null) attributeSet.add(mSelectedPrintColorValue);

        return attributeSet;
    }

    /***********************************************************
     * 設定値のセッター
     * Setter methods
     ***********************************************************/

    /**
     * ステープル設定値に指定された値をセットします。
     * Set the staple setting to the specified value.
     *
     * @param value
     */
    public void setSelectedStaple(Staple value) {
        mSelectedStapleValue = value;
    }

    /**
     * 印刷部数設定値に指定された値をセットします。
     * Set the number of copies to the specified value.
     *
     * @param selectedCopiesValue
     */
    public void setSelectedCopiesValue(Copies selectedCopiesValue) {
        mSelectedCopiesValue = selectedCopiesValue;
    }

    /**
     * 印刷ファイルの名称に指定された値をセットします。
     * Set the print file name to the specified value.
     *
     * @param selectedPrintAssetFileName
     */
    public void setSelectedPrintAssetFileName(String selectedPrintAssetFileName) {
        mSelectedPrintAssetFileName = selectedPrintAssetFileName;
    }

    /**
     * PDL設定に指定された値をセットします。
     * Set the PDL to the specified value.
     *
     * @param pdl
     */
    public void setSelectedPDL(PrintFile.PDL pdl) {
        mSelectedPDL = pdl;
    }

    /**
     * 印刷カラー設定に指定された値をセットします。
     * Set the print color setting to the specified value
     *
     * @param printColor
     */
    public void setSelectedPrintColorValue(PrintColor printColor) {
        mSelectedPrintColorValue = printColor;
    }

    /***********************************************************
     * 設定値のゲッター
     * Getter methods
     ***********************************************************/

    /**
     * 現在のステープルの設定値を取得します。
     * Obtains the current staple setting value.
     *
     * @return
     */
    public Staple getSelectedStaple() {
        return mSelectedStapleValue;
    }

    /**
     * 現在の印刷部数の設定値を取得します。
     * Get the current number of pages.
     *
     * @return
     */
    public Copies getSelectedCopiesValue() {
        return mSelectedCopiesValue;
    }

    /**
     * 現在の印刷ファイル名を取得します。
     * Get the current print file name.
     *
     * @return
     */
    public String getSelectedPrintAssetFileName() {
        return mSelectedPrintAssetFileName;
    }

    /**
     * 現在のPDL設定値を取得します。
     * Get the current PDL setting value.
     *
     * @return
     */
    public PrintFile.PDL getSelectedPDL() {
        return mSelectedPDL;
    }

    /**
     * 現在の印刷カラー設定値を取得します。
     * Get the current print color setting value.
     *
     * @return
     */
    public PrintColor getSelectedPrintColorValue() {
        return mSelectedPrintColorValue;
    }
}
