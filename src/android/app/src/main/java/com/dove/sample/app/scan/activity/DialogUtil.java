/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.app.scan.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.dove.R;
import com.dove.sample.app.scan.application.DestinationSettingDataHolder;
import com.dove.sample.app.scan.application.ScanSampleApplication;
import com.dove.sample.app.scan.application.ScanSettingDataHolder;
import com.dove.sample.app.scan.application.StorageSettingDataHolder;
import com.dove.sample.app.scan.application.StorageSettingDataHolder.StorageStoreData;
import com.dove.sample.function.scan.attribute.standard.AddressbookDestinationSetting;
import com.dove.sample.function.scan.attribute.standard.DestinationSettingItem;
import com.dove.sample.function.scan.attribute.standard.FtpAddressManualDestinationSetting;
import com.dove.sample.function.scan.attribute.standard.MailAddressManualDestinationSetting;
import com.dove.sample.function.scan.attribute.standard.NcpAddressManualDestinationSetting;
import com.dove.sample.function.scan.attribute.standard.NcpAddressManualDestinationSetting.ConnectionType;
import com.dove.sample.function.scan.attribute.standard.SmbAddressManualDestinationSetting;
//import com.dove.PrintMainActivity;

import java.util.List;

/**
 * 本サンプル内で表示する各ダイアログを定義したクラスです。
 * This class defines the dialogs displayed in this sample application.
 */
public class DialogUtil {

    /**
     * アクティビティが前面表示した際に通知されるブロードキャストインテントのアクション名
     * The action name of the broadcast intent to be sent when the activity displayed.
     */
    public static final String INTENT_ACTION_SUB_ACTIVITY_RESUMED = "com.dove.sample.app.scan.SUB_ACTIVITY_RESUMED";

    /**
     * WebAPIでのFTPフォルダ直接入力設定：ポート番号のデフォルト値です。
     * Default FTP port number
     */
    private static final int DEFAULT_PORT_VALUE = 21;

    /**
     * ダイアログのデフォルトの横幅
     * Default dialog width
     */
    private static final int DEFAULT_DIALOG_WIDTH = 400;

    /**
     * ダイアログのデフォルトの縦幅
     * Default dialog height
     */
    public static final int DEFAULT_DIALOG_HEIGHT = 600;

    /**
     * 入力ダイアログの横幅
     * Entry dialog width
     */
    public static final int INPUT_DIALOG_WIDTH = 600;

    /**
     * StoreFileSetting DialogのButtonの最前面表示状態を保持します。
     * Button of StoreFileSetting dialog clicked flag
     */
    public static boolean storeFileSettingDialogBtnClicked = false;
    
    /**
     * Pass DialogのButtonの最前面表示状態を保持します。
     * Button of folder dialog clicked flag
     */
    public static boolean filePassDialogBtnClicked = false; 
    
    /**
     * ダイアログを指定された幅で表示します。
     * Displays the dialog in specified width.
     *
     * @param d dialog
     * @param width dialog width
     * @param height dialog height
     */
    public static void showDialog(Dialog d, int width, int height) {
        d.show();
        WindowManager.LayoutParams lp = d.getWindow().getAttributes();
        if (width > 0) {
            lp.width = width;
        }
        if (height > 0) {
            lp.height = height;
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
        showDialog(d, DEFAULT_DIALOG_WIDTH, DEFAULT_DIALOG_HEIGHT);
    }

    /**
     * ジョブモード設定ダイアログを生成します。
     * Creates the job mode setting dialog.
     */
    public static AlertDialog createJobModeDialog(final Context context, final ScanSettingDataHolder scanSettingDataHolder) {
        final List<Integer> list = scanSettingDataHolder.getJobModeLabelList();
        final String[] items = new String[list.size()];
        for(int i=0; i<items.length; ++i) {
            items[i] = context.getResources().getString(list.get(i));
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(context.getString(R.string.txid_scan_t_jobmode_title));
        dialog.setNegativeButton(context.getString(R.string.txid_cmn_b_close), null);
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scanSettingDataHolder.setSelectedJobMode(list.get(which));
            }
        });
        return dialog.create();
    }

    /**
     * 宛先形式選択ダイアログを生成します。
     * [処理内容]
     *   (1)メール・アドレス帳選択のとき
     *        - アドレス帳を開きます。
     *   (2)メール・直接入力のとき
     *        - メールアドレス直接入力画面を開きます。
     *   (3)フォルダ・アドレス帳選択のとき
     *        - アドレス帳を開きます。
     *   (4)フォルダ・直接入力のとき
     *        - フォルダ設定直接入力画面を開きます。
     *
     * Creates the destination format selection dialog.
     * [Processes]
     *   (1) For email: address book
     *        - Address book opens.
     *   (2) For email: manual entry
     *        - Email address manual entry screen opens.
     *   (3) For folder: address book
     *        - Address book opens.
     *   (4) For folder: manual entry
     *        - Folder manual entry screen opens.
     *
     * @param context
     * @param destHolder
     * @return dialog
     */
    public static AlertDialog createDestTypeDialog(final Context context, final DestinationSettingDataHolder destHolder) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final View parent = inflater.inflate(R.layout.dlg_dest_type, null);
        final ScanSampleApplication mApplication = (ScanSampleApplication)context.getApplicationContext();
        
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(context.getString(R.string.txid_scan_t_kind_title));
        dialog.setNegativeButton(context.getString(R.string.txid_cmn_b_cancel), null);
        dialog.setView(parent);
        final AlertDialog d = dialog.create();

        //set OnDismissListener for dialog
        d.setOnDismissListener(new DialogInterface.OnDismissListener() {
            
            @Override
            public void onDismiss(DialogInterface paramDialogInterface) {
                //If not button is clicked, reset mIsWinShowing to false
                if(!mApplication.ismIsDialogBtnClicked()){
                    mApplication.setmIsWinShowing(false);
                }
            }
        });
        
        //(1)
        Button btn_add_add = (Button)parent.findViewById(R.id.btn_address_address_book);
        btn_add_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check dialog showing or not
                if(mApplication.ismIsDialogBtnClicked()){
                    return ;
                }
                mApplication.setmIsDialogBtnClicked(true);
                
                d.dismiss();
                Intent intent = new Intent(context, AddressActivity.class);
                intent.putExtra(AddressActivity.KEY_DEST_KIND, AddressbookDestinationSetting.DestinationKind.MAIL.toString());
                ((Activity) context).startActivityForResult(intent, AddressActivity.REQUEST_CODE_ADDRESS_ACTIVITY);
            }
        });

        //(2)
        Button btn_add_direct = (Button)parent.findViewById(R.id.btn_address_direct);
        btn_add_direct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check dialog showing or not
                if(mApplication.ismIsDialogBtnClicked()){
                    return ;
                }
                mApplication.setmIsDialogBtnClicked(true);

                d.dismiss();
                AlertDialog dialog = createManualInputMailDialog(context, destHolder);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    
                    @Override
                    public void onDismiss(DialogInterface paramDialogInterface) {

                        // Reset flag to default value when dialog dismiss
                        mApplication.setmIsDialogBtnClicked(false);
                        mApplication.setmIsWinShowing(false);
                    }
                });
                showDialog(dialog, INPUT_DIALOG_WIDTH, 0);
            }
        });

        //(3)
        Button btn_folder_add = (Button)parent.findViewById(R.id.btn_folder_address_book);
        btn_folder_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check dialog showing or not
                if(mApplication.ismIsDialogBtnClicked()){
                    return ;
                }
                mApplication.setmIsDialogBtnClicked(true);
                
                d.dismiss();
                Intent intent = new Intent(context, AddressActivity.class);
                intent.putExtra(AddressActivity.KEY_DEST_KIND, AddressbookDestinationSetting.DestinationKind.FOLDER.toString());
                ((Activity) context).startActivityForResult(intent, AddressActivity.REQUEST_CODE_ADDRESS_ACTIVITY);
            }
        });

        //(4)
        Button btn_folder_direct = (Button)parent.findViewById(R.id.btn_folder_direct);
        btn_folder_direct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check dialog showing or not
                if(mApplication.ismIsDialogBtnClicked()){
                    return ;
                }
                mApplication.setmIsDialogBtnClicked(true);
                
                d.dismiss();
                AlertDialog dialog = createManualInputFolderDialog(context, destHolder);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    
                    @Override
                    public void onDismiss(DialogInterface paramDialogInterface) {
                        // Reset flag to default value when dialog dismiss
                        mApplication.setmIsDialogBtnClicked(false);
                        mApplication.setmIsWinShowing(false);
                    }
                });
                showDialog(dialog, INPUT_DIALOG_WIDTH, DEFAULT_DIALOG_HEIGHT);
            }
        });

        return d;
    }

    /**
     * 宛先メールアドレス直接入力ダイアログを生成します。
     * [処理内容]
     *   (1)ダイアログの生成と画面設定
     *   (2)メールアドレス入力エリアの設定
     *   (3)OKボタンの設定
     *   (4)キャンセルボタンの設定
     *
     * Creates the email destination manual entry dialog.
     * [Processes]
     *   (1) Create dialog and set screen
     *   (2) Set email address entry area
     *   (3) Set OK button
     *   (4) Set Cancel button
     *
     * @param context
     * @param destHolder
     * @return dialog
     */
    private static AlertDialog createManualInputMailDialog(final Context context, final DestinationSettingDataHolder destHolder) {

        //(1)
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.dlg_dest_manual_mail, null);
        builder.setTitle(context.getResources().getString(R.string.txid_dest_t_manual_input_mail_title));
        builder.setView(view);

        //(2)
        final EditText edit_address = (EditText)view.findViewById(R.id.edit_address);
        DestinationSettingItem destItem = destHolder.getDestinationSettingItem();
        if( destItem instanceof MailAddressManualDestinationSetting) {
            MailAddressManualDestinationSetting dest = (MailAddressManualDestinationSetting)destItem;
            edit_address.setText(dest.getMailAddress());
        }

        //(3)
        builder.setNegativeButton(context.getResources().getString(R.string.txid_dest_b_manual_input_folder_ok),
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if( edit_address.getText().toString().equals("") ) {
                        destHolder.setDestinationSettingItem(null);
                    } else {
                        DestinationSettingItem dest = DestinationSettingDataHolder.createMailAddressManualDestinationSetting(
                                edit_address.getText().toString());
                        destHolder.setDestinationSettingItem(dest);
                    }
//                    ((PrintMainActivity)context).updateDestinationLabel(null);
                    ((ScanMainActivity)context).updateDestinationLabel(null);
                }
        });

        //(4)
        builder.setPositiveButton(context.getResources().getString(R.string.txid_dest_b_manual_input_folder_cancel), null);

        return builder.create();
    }

    /**
     * 宛先フォルダ直接入力ダイアログを生成します。
     * [処理内容]
     *   (1)ダイアログの生成と画面設定
     *   (2)各設定の初期値と入力可否設定
     *   (3)フォルダタイプ切替ラジオボタンの設定
     *   (4)OKボタンの設定
     * Creates the folder destination manual entry dialog.
     * [Processes]
     *   (1) Create dialog and set screen
     *   (2) Set default settings and entry availability
     *   (3) Set folder type switching radio button
     *   (4) Set OK button
     *
     * @param context
     * @param destHolde
     * @return dialog
     */
    private static AlertDialog createManualInputFolderDialog(final Context context, final DestinationSettingDataHolder destHolder) {

        //(1)
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.dlg_dest_manual_folder, null);
        builder.setTitle(context.getResources().getString(R.string.txid_dest_t_manual_input_folder_title));
        builder.setView(view);
        builder.setPositiveButton(context.getResources().getString(R.string.txid_dest_b_manual_input_folder_cancel), null);

        final EditText edit_server = (EditText)view.findViewById(R.id.edit_server);
        final EditText edit_path = (EditText)view.findViewById(R.id.edit_path);
        final EditText edit_user = (EditText)view.findViewById(R.id.edit_user);
        final EditText edit_pass = (EditText)view.findViewById(R.id.edit_pass);
        final EditText edit_port = (EditText)view.findViewById(R.id.edit_port);
        final RadioGroup radioGroup_folder = (RadioGroup)view.findViewById(R.id.radioGroup_folder);
        final RadioButton radioBtn_ftp = (RadioButton)view.findViewById(R.id.radioBtn_ftp);
        final RadioButton radioBtn_ncp = (RadioButton)view.findViewById(R.id.radioBtn_ncp);
        final RadioButton radioBtn_smb = (RadioButton)view.findViewById(R.id.radioBtn_smb);
        final RadioGroup radioGroup_connect = (RadioGroup)view.findViewById(R.id.radioGroup_connect);
        final RadioButton radioBtn_nds = (RadioButton)view.findViewById(R.id.radioBtn_nds);
        final RadioButton radioBtn_bindery = (RadioButton)view.findViewById(R.id.radioBtn_bindery);

        //(2)
        final DestinationSettingItem dest = destHolder.getDestinationSettingItem();
        if (dest instanceof SmbAddressManualDestinationSetting) {

            SmbAddressManualDestinationSetting item = (SmbAddressManualDestinationSetting)dest;
            radioBtn_smb.setChecked(true);
            edit_server.setEnabled(false);
            edit_port.setEnabled(false);
            edit_path.requestFocus();
            edit_path.setText(item.getPath());
            edit_user.setText(item.getUserName());
            edit_pass.setText(item.getPassword());
            radioGroup_connect.setEnabled(false);
            radioGroup_connect.clearCheck();
            radioBtn_nds.setEnabled(false);
            radioBtn_bindery.setEnabled(false);

        } else if (dest instanceof FtpAddressManualDestinationSetting) {

            FtpAddressManualDestinationSetting item = (FtpAddressManualDestinationSetting)dest;
            radioBtn_ftp.setChecked(true);
            edit_server.setEnabled(true);
            edit_port.setEnabled(true);
            edit_server.requestFocus();
            edit_server.setText(item.getServerName());
            edit_path.setText(item.getPath());
            edit_user.setText(item.getUserName());
            edit_pass.setText(item.getPassword());
            edit_port.setText(Integer.toString(item.getPort()));
            radioGroup_connect.setEnabled(false);
            radioGroup_connect.clearCheck();
            radioBtn_nds.setEnabled(false);
            radioBtn_bindery.setEnabled(false);

        } else if (dest instanceof NcpAddressManualDestinationSetting) {

            NcpAddressManualDestinationSetting item = (NcpAddressManualDestinationSetting)dest;
            radioBtn_ncp.setChecked(true);
            edit_server.setEnabled(false);
            edit_port.setEnabled(false);
            edit_path.requestFocus();
            edit_path.setText(item.getPath());
            edit_user.setText(item.getUserName());
            edit_pass.setText(item.getPassword());
            radioGroup_connect.setEnabled(true);
            radioBtn_nds.setEnabled(true);
            radioBtn_bindery.setEnabled(true);

            if(item.getConnectionType() == ConnectionType.NDS) {
                radioBtn_nds.setChecked(true);
            } else if(item.getConnectionType() == ConnectionType.BINDERY) {
                radioBtn_bindery.setChecked(true);
            } else {
                /* never reach here */
                radioBtn_nds.setChecked(true);
            }

        } else {
            // default
            radioBtn_smb.setChecked(true);
            edit_server.setEnabled(false);
            edit_port.setEnabled(false);
            radioGroup_connect.setEnabled(false);
            radioGroup_connect.clearCheck();
            radioBtn_nds.setEnabled(false);
            radioBtn_bindery.setEnabled(false);
            edit_path.requestFocus();

        }

        //(3)
        radioGroup_folder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            int selected_connectType_id = radioBtn_nds.getId();

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(radioBtn_ftp.isChecked()) {

                    if(radioGroup_connect.getCheckedRadioButtonId() != -1) {
                        selected_connectType_id = radioGroup_connect.getCheckedRadioButtonId();
                    }
                    edit_server.setEnabled(true);
                    edit_port.setEnabled(true);
                    radioGroup_connect.setEnabled(false);
                    radioGroup_connect.clearCheck();
                    radioBtn_nds.setEnabled(false);
                    radioBtn_bindery.setEnabled(false);

                } else if(radioBtn_ncp.isChecked()){

                    edit_server.setEnabled(false);
                    edit_port.setEnabled(false);
                    radioGroup_connect.setEnabled(true);
                    radioBtn_nds.setEnabled(true);
                    radioBtn_bindery.setEnabled(true);
                    radioGroup_connect.check(selected_connectType_id);
                    if(edit_server.isFocused() || edit_port.isFocused()) {
                        edit_path.requestFocus();
                    }

                } else if(radioBtn_smb.isChecked()) {

                    if(radioGroup_connect.getCheckedRadioButtonId() != -1) {
                        selected_connectType_id = radioGroup_connect.getCheckedRadioButtonId();
                    }
                    edit_server.setEnabled(false);
                    edit_port.setEnabled(false);
                    radioGroup_connect.setEnabled(false);
                    radioGroup_connect.clearCheck();
                    radioBtn_nds.setEnabled(false);
                    radioBtn_bindery.setEnabled(false);
                    if(edit_server.isFocused() || edit_port.isFocused()) {
                        edit_path.requestFocus();
                    }
                } else {
                    /* never reach here */
                }
            }
        });

        //(4)
        builder.setNegativeButton(context.getResources().getString(R.string.txid_dest_b_manual_input_folder_ok),
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DestinationSettingItem dest;

                    // path cannot be empty.
                    if (edit_path.getText().toString().equals(null) || edit_path.getText().toString().equals("")) {
                        destHolder.setDestinationSettingItem(null);
                    } else if (radioBtn_ftp.isChecked()) {

                        String str_port = edit_port.getText().toString();
                        int port = ("").equals(str_port)? DEFAULT_PORT_VALUE: Integer.valueOf(str_port);

                        dest = DestinationSettingDataHolder.createFtpManualDestinationSetting(
                                edit_server.getText().toString(),
                                edit_path.getText().toString(),
                                edit_user.getText().toString(),
                                edit_pass.getText().toString(),
                                port);
                        destHolder.setDestinationSettingItem(dest);

                    } else if (radioBtn_ncp.isChecked()) {

                        ConnectionType connectionType = null;
                        if( radioBtn_nds.isChecked()) {
                            connectionType = ConnectionType.NDS;
                        } else if (radioBtn_bindery.isChecked()) {
                            connectionType = ConnectionType.BINDERY;
                        } else {
                            /* never reach here */
                        }

                        dest = DestinationSettingDataHolder.createNcpManualDestinationSetting(
                                edit_path.getText().toString(),
                                edit_user.getText().toString(),
                                edit_pass.getText().toString(),
                                connectionType);
                        destHolder.setDestinationSettingItem(dest);

                    } else if (radioBtn_smb.isChecked()) {

                        dest = DestinationSettingDataHolder.createSmbManualDestinationSetting(
                                edit_path.getText().toString(),
                                edit_user.getText().toString(),
                                edit_pass.getText().toString());
                        destHolder.setDestinationSettingItem(dest);

                    } else {
                        /* do nothing */
                    }
                    //((PrintMainActivity)context).updateDestinationLabel(null);
                    ((ScanMainActivity)context).updateDestinationLabel(null);
                }
        });

        return builder.create();
    }

    /**
     * ファイル蓄積設定ダイアログです。
     * File storage settings dialog.
     *
     * @param context
     * @param storageSetting
     * @return
     */
    public static class StoreFileSettingDialog extends AlertDialog {
        private final StorageSettingDataHolder storageSetting;

        private final String tmpFilePass;
        private final String tmpFolderId;
        private final String tmpFolderName;
        private final String tmpFolderPass;

        private final TextView txtFolder;
        private final Button btnOk;
        

        protected StoreFileSettingDialog(final Context context, final StorageSettingDataHolder storageSetting) {
            super(context);
            this.storageSetting = storageSetting;

            final LayoutInflater inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.dlg_storage_setting, null);
            setTitle(R.string.txid_storage_t_store_title);
            setView(view);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            StorageStoreData storageStoreData = storageSetting.getStorageStoreData();
            tmpFilePass = storageStoreData==null ? null: storageStoreData.getFilePass();
            tmpFolderId = storageStoreData==null ? null: storageStoreData.getFolderId();
            tmpFolderName = storageStoreData==null ? null: storageStoreData.getFolderName();
            tmpFolderPass = storageStoreData==null ? null: storageStoreData.getFolderPass();

            final EditText edit_filename = (EditText)view.findViewById(R.id.edit_filename);
            final TextView txt_pass = (TextView)view.findViewById(R.id.txt_pass);
            txtFolder = (TextView)view.findViewById(R.id.txt_folder);

            txt_pass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Check dialog is showing or not
                    if(storeFileSettingDialogBtnClicked){
                        return ;
                    }
                    storeFileSettingDialogBtnClicked = true;
                    
                    AlertDialog d = createFilePassSetDialog(context, storageSetting);
                    d.setOnDismissListener(new OnDismissListener(){
                        @Override
                        public void onDismiss(DialogInterface paramDialogInterface) {
                            // Reset flag to default value if dialog dismiss
                            storeFileSettingDialogBtnClicked = false;
                            filePassDialogBtnClicked = false;
                            
                            StorageStoreData storeData = storageSetting.getStorageStoreData();
                            if (storeData!=null && storeData.getFilePass()!=null) {
                                txt_pass.setText(storeData.getFilePass());
                            }
                        }
                    });
                    showDialog(d, INPUT_DIALOG_WIDTH, 0);                    
                }
            });

            txtFolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Check dialog is showing or not
                    if(storeFileSettingDialogBtnClicked){
                        return ;
                    }
                    storeFileSettingDialogBtnClicked = true;
                    
                    Intent intent = new Intent(context, StorageActivity.class);
                    intent.putExtra(StorageActivity.KEY_MODE, StorageActivity.VALUE_MODE_STORE_FOLDER);
                    ((Activity)context).startActivity(intent);
                }
            });

            btnOk = (Button)view.findViewById(R.id.btn_ok);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Check dialog is showing or not
                    if(storeFileSettingDialogBtnClicked){
                        return ;
                    }
                    
                    StorageStoreData storeData = storageSetting.getStorageStoreData();
                    if (storeData==null) {
                        storeData = new StorageStoreData();
                        storageSetting.setStorageStoreData(storeData);
                    }
                    storeData.setFileName(edit_filename.getText().toString());
                    storeData.setFilePass(txt_pass.getText().toString());
                    dismiss();
                }
            });

            Button btn_cancel = (Button)view.findViewById(R.id.btn_cancel);
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Check dialog is showing or not
                    if(storeFileSettingDialogBtnClicked){
                        return ;
                    }
                    
                    cancelSelectedItem();
                    dismiss();
                }
            });

            StorageStoreData storeData = storageSetting.getStorageStoreData();
            if (storeData!=null && storeData.getFileName()!=null) {
                edit_filename.setText(storeData.getFileName());
            }
            if (storeData!=null && storeData.getFilePass()!=null) {
                txt_pass.setText(storeData.getFilePass());
            }
            if (storeData!=null && storeData.getFolderId()!=null) {
                String folderName = storeData.getFolderName();
                if ( (folderName == null) || ("".equals(folderName)) ) {
                    txtFolder.setText(storeData.getFolderId());
                } else {
                    txtFolder.setText(folderName);
                }
                btnOk.setEnabled(true);
            } else {
                btnOk.setEnabled(false);
            }
        }

        @Override
        public void onWindowFocusChanged(boolean hasFocus) {
            if (hasFocus) {
                StorageStoreData storeData = storageSetting.getStorageStoreData();
                if (storeData!=null && storeData.getFolderId()!=null) {
                    String folderName = storeData.getFolderName();
                    if ( (folderName == null) || ("".equals(folderName)) ) {
                        txtFolder.setText(storeData.getFolderId());
                    } else {
                        txtFolder.setText(folderName);
                    }
                    btnOk.setEnabled(true);
                }
            }
        }

        @Override
        public void onBackPressed() {
            cancelSelectedItem();
        }

        private void cancelSelectedItem() {
            StorageStoreData storeData = storageSetting.getStorageStoreData();
            if (storeData!=null) {
                storeData.setFilePass(tmpFilePass);
                storeData.setFolderId(tmpFolderId);
                storeData.setFolderName(tmpFolderName);
                storeData.setFolderPass(tmpFolderPass);
            }
        }
    }

    /**
     * ファイルのパスワード設定ダイアログを生成します。
     * 入力した２つのパスワードが一致する必要があります。
     * Generates a file password setting dialog.
     * Two entered passwords must match.
     *
     * @param context
     * @param storageSetting
     * @return dialog
     */
    public static AlertDialog createFilePassSetDialog(final Context context, final StorageSettingDataHolder storageSetting) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.dlg_set_pass, null);
        builder.setTitle(context.getResources().getString(R.string.txid_storage_t_file_pass));
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final EditText edit_pass1 = (EditText)view.findViewById(R.id.edit_pass1);
        final EditText edit_pass2 = (EditText)view.findViewById(R.id.edit_pass2);

        final Button btn_ok = (Button)view.findViewById(R.id.btn_ok);
        
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check dialog is showing or not
                if(filePassDialogBtnClicked){
                    return ;
                }
                filePassDialogBtnClicked = true;
                
                if (!edit_pass1.getText().toString().equals(edit_pass2.getText().toString())) {
                    AlertDialog.Builder b = new AlertDialog.Builder(context);
                    b.setMessage(R.string.txid_storage_m_pass_file_not_match);
                    b.setPositiveButton(R.string.txid_cmn_b_exit, new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            // Reset flag to default value if dialog dismiss
                            filePassDialogBtnClicked = false;
                        }
                        
                    });
                    AlertDialog tmpDialog = b.show();
                    tmpDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        
                        @Override
                        public void onDismiss(DialogInterface arg0) {
                            filePassDialogBtnClicked = false;
                        }
                    });
                } else {
                    StorageStoreData storeData = storageSetting.getStorageStoreData();
                    if (storeData==null) {
                        storeData = new StorageStoreData();
                    }
                    storeData.setFilePass(edit_pass1.getText().toString());
                    storageSetting.setStorageStoreData(storeData);
                    dialog.dismiss();
                    // Reset flag to default value if dialog dismiss
                    filePassDialogBtnClicked = false;
                }
            }
        });

        final Button btn_cancel = (Button)view.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filePassDialogBtnClicked){
                    return ;
                }
                
                filePassDialogBtnClicked = true;                
                dialog.dismiss();                
            }
        });

        return dialog;
    }

    /**
     * 読取カラー設定ダイアログを生成します。
     * Creates the scan color setting dialog.
     *
     * @param context
     * @param scanSettingDataHolder
     * @return dialog
     */
    public static AlertDialog createColorSettingDialog(final Context context, final ScanSettingDataHolder scanSettingDataHolder) {

        final List<Integer> list = scanSettingDataHolder.getColorLabelList();
        final String[] items = new String[list.size()];
        for(int i=0; i<items.length; ++i) {
            items[i] = context.getResources().getString(list.get(i));
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(context.getString(R.string.txid_scan_t_top_color_title));
        dialog.setNegativeButton(context.getString(R.string.txid_cmn_b_close), null);
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scanSettingDataHolder.setSelectedColor(list.get(which));
            }
        });
        return dialog.create();
    }

    /**
     * ファイル形式設定ダイアログを生成します。
     * Creates the file setting dialog.
     *
     * @param context
     * @param scanSettingDataHolder
     * @return dialog
     */
    public static AlertDialog createFileSettingDialog(final Context context, final ScanSettingDataHolder scanSettingDataHolder) {

        final List<Integer> list = scanSettingDataHolder.getFileSettingLabelList();
        final String[] items = new String[list.size()];
        for(int i=0; i<items.length; ++i) {
            items[i] = context.getResources().getString(list.get(i));
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(context.getString(R.string.txid_scan_t_top_file_title));
        dialog.setNegativeButton(context.getString(R.string.txid_cmn_b_close), null);
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scanSettingDataHolder.setSelectedFileSetting(list.get(which));
            }
        });
        return dialog.create();
    }

    /**
     * スキャン面設定ダイアログを生成します。
     * Creates the scan side setting dialog.
     *
     * @param context
     * @param scanSettingDataHolder
     * @return dialog
     */
    public static AlertDialog createSideSettingDialog(final Context context, final ScanSettingDataHolder scanSettingDataHolder) {

        final List<Integer> list = scanSettingDataHolder.getSideLabelList();
        final String[] items = new String[list.size()];
        for(int i=0; i<items.length; ++i) {
            items[i] = context.getResources().getString(list.get(i));
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(context.getString(R.string.txid_scan_t_top_side_title));
        dialog.setNegativeButton(context.getString(R.string.txid_cmn_b_close), null);
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scanSettingDataHolder.setSelectedSide(list.get(which));
            }
        });
        return dialog.create();
    }

    /**
     * その他のスキャン設定ダイアログを生成します。
     * [処理内容]
     *   (1)ダイアログの画面設定
     *   (2)各設定項目のセット
     *      ここでは、プレビュー設定をセットします。
     *   (3)各設定項目が選択されたときの処理設定
     *   (4)ダイアログの生成
     *
     * Creates the other setting dialog.
     * [Processes]
     *   (1) Set screen
     *   (2) Set settings
     *       Preview setting is set here.
     *   (3) Set processes for the time each setting is set
     *   (4) Create dialog
     *
     * @param context
     * @param scanSettingDataHolder
     * @return dialog
     */
    public static AlertDialog createOtherSettingDialog(final Context context, final ScanSettingDataHolder scanSettingDataHolder) {

        //(1)
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.dlg_setting_other, null);

        final ViewFlipper viewFlipper = (ViewFlipper)view.findViewById(R.id.flipper_setting);
        final ListView listView_detail = (ListView)view.findViewById(R.id.listview_detail);
        final TextView text_detail_title = (TextView)view.findViewById(R.id.text_title_detail);

        //(2)
        /*============ Preview Setting ================*/

        final LinearLayout layout_preview = (LinearLayout)view.findViewById(R.id.include_setting_preview);
        final ArrayAdapter<String> adapter_preview = new ArrayAdapter<String>(context, R.layout.list_row_setting_detail, R.id.text_detail);
        final TextView text_category_preview = (TextView)layout_preview.findViewById(R.id.text_category);
        final TextView text_value_preview = (TextView)layout_preview.findViewById(R.id.text_value);
        text_category_preview.setText(context.getString(R.string.txid_scan_b_other_preview_title));
        text_value_preview.setText(scanSettingDataHolder.getSelectedPreviewLabel());

        final List<Integer> list_preview = scanSettingDataHolder.getPreviewLabelList();
        for(int i=0; i<list_preview.size(); ++i) {
            adapter_preview.add(context.getResources().getString(list_preview.get(i)));
        }

        layout_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_detail_title.setText(R.string.txid_scan_b_other_preview_title);
                listView_detail.setAdapter(adapter_preview);
                viewFlipper.setDisplayedChild(1);
            }
        });
        /*===============================================*/

        //(3)
        listView_detail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(parent.getAdapter().equals(adapter_preview)) {
                	// Update preview setting
                    String value = adapter_preview.getItem(position);
                    TextView text_value = (TextView)layout_preview.findViewById(R.id.text_value);
                    text_value.setText(value);
                    scanSettingDataHolder.setSelectedPreview(list_preview.get(position));

                } else {
                    /* should never reach this point */
                }
                viewFlipper.setDisplayedChild(0);
            }
        });

        //(4)
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(context.getString(R.string.txid_scan_t_top_other_title));
        dialog.setNegativeButton(context.getString(R.string.txid_cmn_b_close), null);
        dialog.setView(view);
        return dialog.create();
    }

}
