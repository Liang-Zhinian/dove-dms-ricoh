/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */

package com.dove.sample.app.print.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.dove.PrintApplicationWrapper;
import com.dove.sample.app.print.Const;
import com.dove.R;
import com.dove.R.id;
//import com.dove.sample.app.print.application.PrintSampleApplication;
import com.dove.sample.app.print.application.PrintSettingSupportedHolder;
import com.dove.sample.function.print.PrintFile;
import com.dove.sample.function.print.PrintFile.PDL;
import com.dove.sample.function.print.attribute.standard.Copies;
import com.dove.sample.function.print.attribute.standard.PrintColor;
import com.dove.sample.function.print.attribute.standard.Staple;

import java.util.List;
import java.util.Map;

/**
 * 本サンプル内で表示する各ダイアログを定義したクラスです。
 * This class defines the dialogs displayed in this sample application.
 */
public class DialogUtil {
    /**
     * Define the prefix for log information with abbreviation package and class name
     */
    private final static String PREFIX = "activity:DialogUt:";

    /**
     * ダイアログのデフォルトの横幅
     * Default dialog width
     */
    public static final int DEFAULT_DIALOG_WIDTH = 400;

    /**
     * 入力ダイアログの横幅
     * Entry dialog width
     */
    public static final int INPUT_DIALOG_WIDTH = 600;
    
    /**
     * Add one flag mSelectFItemClicked to indicate status of item on Select File dialog
     */
    private static boolean mSelectFItemClicked = false;

    /**
     * ダイアログを指定された幅で表示します。
     * Displays the dialog in specified width.
     *
     * @param d dialog
     * @param width dialog width
     * @param height dialog height
     */
    public static void showDialog(Dialog d, int width) {
        d.show();
        WindowManager.LayoutParams lp = d.getWindow().getAttributes();
        if (width > 0) {
            lp.width = width;
        }
        d.getWindow().setAttributes(lp);
    }

    /**
     * ダイアログをデフォルトサイズで表示します。
     * Displays the dialog in default size.
     *
     * @param d dialog
     */
    public static void showDialog(Dialog d) {
        showDialog(d, DEFAULT_DIALOG_WIDTH);
    }

    /**
     * 印刷ファイル選択ダイアログを生成します。
     * Creates the print file select dialog.
     *
     * @param context メインアクティビティのコンテキスト
     *                context of PrintMainActivity
     * @param fileList 選択可能なファイル名のリスト
     *                 list of available files
     */
    public static AlertDialog selectFileDialog(final Context context,
            List<String> fileList) {

        final String[] items = fileList.toArray(new String[fileList.size()]);

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(context.getString(R.string.select_file_dlg_title));
        mSelectFItemClicked = false;
        dialog.setNegativeButton(context.getString(R.string.btn_cancel), null);
        dialog.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //If item is clicked, set mSelectFItemClicked to true
            	mSelectFItemClicked = true;
                
                String selectedFile = items[which];
                if(isValidFile(selectedFile)){
                    ((SimplePrintMainActivity)context).getSettingHolder().setSelectedPrintAssetFileName(selectedFile);
                }else{
                    Toast.makeText(context, R.string.error_pdl_not_found, Toast.LENGTH_LONG).show();
                }
            }

            public boolean isValidFile(String fileName){
                boolean result = false;

                if(null != fileName){
                    PDL selectedPDL = ((SimplePrintMainActivity)context).currentPDL(fileName);

                    if(null != selectedPDL){
                        result = true;
                    }
                }
                return result;
            }
        });
        return dialog.create();
    }

    /**
     * 印刷枚数設定ダイアログを生成します。
     * Creates number of prints setting dialog
     *
     * @param context メインアクティビティのコンテキスト
     *                Context of PrintMainActivity
     */
    public static AlertDialog createPrintCountDialog(final Context context){

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(context.getResources().getString(R.string.print_count_dlg_title));

        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.print_count_dialog, null);
        dialog.setView(view);

        final AlertDialog d = dialog.create();

        TextView countText = (TextView)view.findViewById(R.id.text_count);
        String defaultCount = ((SimplePrintMainActivity)context).getSettingHolder().getSelectedCopiesValue().getValue().toString();
        countText.setText(defaultCount);

        Button okBtn = (Button)view.findViewById(R.id.print_count_bt_ok);
        okBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText textView = (EditText)view.findViewById(id.text_count);
                SpannableStringBuilder input = (SpannableStringBuilder)textView.getText();

                if(null != input){
                    String str = input.toString();
                    int inputCount = 0;
                    try{
                        inputCount = Integer.valueOf(str);
                    }catch(Exception e){
                        Log.d(Const.TAG, PREFIX + e.toString());
                    }
                    Copies printCount = new Copies(inputCount);
                    ((SimplePrintMainActivity)context).getSettingHolder().setSelectedCopiesValue(printCount);
                }
                d.dismiss();
            }
        });

        Button cancelButton = (Button)view.findViewById(R.id.print_count_bt_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        return d;
    }

    /**
     * 印刷カラー設定ダイアログを生成します。
     * Creates print color setting dialog.
     *
     * @param context メインアクティビティのコンテキスト
     *                Context of PrintMainActivity
     * @param colorList 設定可能な色を表すPrintColorオブジェクトのリスト
     *                  List of supported PrintColor
     */
    public static AlertDialog createPrintColorDialog(final Context context, List<PrintColor> colorList){
        final String[] items = new String[colorList.size()];
        int i = 0;
        for(PrintColor color : colorList) {
            items[i] = PrintColorUtil.getPrintColorResourceString(context, color);
            i++;
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(context.getString(R.string.print_color_dlg_title));
        dialog.setNegativeButton(context.getString(R.string.btn_cancel), null);
        dialog.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                String selectedPrintColor = items[which];
                ((SimplePrintMainActivity) context).getSettingHolder()
                        .setSelectedPrintColorValue(PrintColorUtil.getPrintColorFromResourceString(context,selectedPrintColor));

            }
        });
        return dialog.create();
    }

    /**
     * その他の設定ダイアログを生成します。
     * ここでは、ステープル設定をセットします。
     * Creates other setting dialog
     * Staple setting is set here.
     *
     * @param context メインアクティビティのコンテキスト
     *                Context of PrintMainActivity
     */
    public static AlertDialog createOtherSettingDialog(final Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        final View parent = inflater.inflate(R.layout.dlg_setting_other, null);

        final ViewFlipper viewFlipper = (ViewFlipper)parent.findViewById(R.id.flipper_setting);
        final ListView listView_detail = (ListView)parent.findViewById(R.id.listview_detail);
        final TextView text_detail_title = (TextView)parent.findViewById(R.id.text_title_detail);

//        PrintSampleApplication app = (PrintSampleApplication)((PrintMainActivity)context).getApplication();
        PrintApplicationWrapper app = ((SimplePrintMainActivity)context).getmApplication();
        Map<PDL, PrintSettingSupportedHolder> supportedMap = app.getSettingSupportedDataHolders();
        PrintFile.PDL currentPDL = ((SimplePrintMainActivity)context).getSettingHolder().getSelectedPDL();

        if(null == currentPDL){
            return null;
        }

        /*============ Staple Setting ================*/
        final LinearLayout layout_staple = (LinearLayout)parent.findViewById(R.id.include_setting_staple);
        final ArrayAdapter<String> adapter_staple = new ArrayAdapter<String>(context, R.layout.list_row_setting_detail, R.id.text_detail);
        final TextView text_category_staple = (TextView)layout_staple.findViewById(R.id.text_category);
        TextView text_value_staple = (TextView)layout_staple.findViewById(R.id.text_value);
        text_category_staple.setText(context.getString(R.string.staple_dlg_title));
        text_value_staple.setText(StapleUtil.getStapleString(context, (Staple)((SimplePrintMainActivity)context).getSettingHolder().getSelectedStaple()));

        final List<Staple> stapleList = supportedMap.get(currentPDL).getSelectableStapleList();

        if(null == stapleList){
            return null;
        }

        for(Staple staple : stapleList){
            adapter_staple.add(StapleUtil.getStapleString(context, staple));
        }

        layout_staple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_detail_title.setText(R.string.staple_dlg_title);
                listView_detail.setAdapter(adapter_staple);
                viewFlipper.setDisplayedChild(1);
            }
        });
        /*===============================================*/

        listView_detail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getAdapter().equals(adapter_staple)) {
                    // update staple setting
                    String value = adapter_staple.getItem(position);
                    TextView text_value = (TextView)layout_staple.findViewById(R.id.text_value);
                    text_value.setText(value);
                    ((SimplePrintMainActivity)context).getSettingHolder().setSelectedStaple(
                            stapleList.get(position));
                } else {
                    /* should never reach this point */
                }
                viewFlipper.setDisplayedChild(0);
            }
        });

        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(context.getString(R.string.other_settings_dlg_title));
        dialog.setNegativeButton(context.getString(R.string.btn_close), null);
        dialog.setView(parent);
        return dialog.create();
    }

	public static boolean isSelectFItemClicked() {
		return mSelectFItemClicked;
	}

	public static void setSelectFItemClicked(boolean mSelectFItemClicked) {
		DialogUtil.mSelectFItemClicked = mSelectFItemClicked;
	}

	

}
