/*
 *  Copyright (C) 2014-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.app.scan.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dove.sample.app.scan.Const;
import com.dove.R;
import com.dove.sample.app.scan.application.ScanSampleApplication;
import com.dove.sample.app.scan.application.StorageSettingDataHolder;
import com.dove.sample.app.scan.application.StorageSettingDataHolder.StorageSendData;
import com.dove.sample.app.scan.application.StorageSettingDataHolder.StorageStoreData;
import com.dove.sample.function.common.SmartSDKApplication;
import com.dove.sample.wrapper.common.InvalidResponseException;
import com.dove.sample.wrapper.common.Request;
import com.dove.sample.wrapper.common.RequestHeader;
import com.dove.sample.wrapper.common.RequestQuery;
import com.dove.sample.wrapper.common.Response;
import com.dove.sample.wrapper.rws.service.storage.Files;
import com.dove.sample.wrapper.rws.service.storage.Folders;
import com.dove.sample.wrapper.rws.service.storage.GetFileListResponseBody;
import com.dove.sample.wrapper.rws.service.storage.GetFileListResponseBody.FileList;
import com.dove.sample.wrapper.rws.service.storage.GetFileListResponseBody.FileListArray;
import com.dove.sample.wrapper.rws.service.storage.GetFileResponseBody;
import com.dove.sample.wrapper.rws.service.storage.GetFolderListResponseBody;
import com.dove.sample.wrapper.rws.service.storage.GetFolderListResponseBody.FolderList;
import com.dove.sample.wrapper.rws.service.storage.GetFolderListResponseBody.FolderListArray;
import com.dove.sample.wrapper.rws.service.storage.GetFolderResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class StorageActivity extends Activity {

    /**
     * StorageActivity（送信時）のリクエストコードです。
     * Request code of storage activity : send stored file mode
     */
    public static final int REQUEST_CODE_STORAGE_ACTIVITY_SEND = 3000;

    /**
     * StorageActivity（ローカル蓄積時）のリクエストコードです。
     * Request code of storage activity : store file mode
     */
    public static final int REQUEST_CODE_STORAGE_ACTIVITY_STORE = 3100;

    /**
     * アイテム種別のキー
     * Item type key
     */
    public static final String KEY_MODE = "mode";

    public static final String VALUE_MODE_SEND_FOLDER = "send_folder";

    public static final String VALUE_MODE_SEND_FILE = "send_file";

    public static final String VALUE_MODE_STORE_FOLDER = "store_folder";

    private String mMode;

    /**
     * OKButtonの状態を保持します
     * InputPass Dialog OK button clicked flag
     */
    private boolean mDialogOKButtonClicked = false;
    
    /**
     * 蓄積ファイル設定
     * Storage setting
     */
    StorageSettingDataHolder mStorageSettingDataHolder;

    /**
     * OKボタン
     * OK button
     */
    private Button mOkButton;

    /**
     * キャンセルボタン
     * Cancel button
     */
    private Button mCancelButton;

    /**
     * フォルダ/ファイルのリストビュー
     * Folder / File list view
     */
    private ListView mItemListView;

    /**
     * フォルダ/ファイルリストのアダプター
     * Folder / Item list adapter
     */
    private ItemListAdapter mItemListAdapter;

    /**
     * ファイル追加読み込みボタンのフッター
     * Footer to display the next file list button.
     */
    private LinearLayout mFooterView;

    /**
     * 選択中のフォルダのデータ
     * Selected folder data
     */
    private ItemData mSelectedFolder;

    /**
     * 選択中のファイルのデータ
     * Selected file data
     */
    private ItemData mSelectedFile;

    /**
     * フォルダリスト
     * Folder list
     */
    private List<FolderList> mFolderList;

    /**
     * ファイルリスト
     * File list
     */
    private List<FileList> mFileList;

    /**
     * フォルダ一覧取得タスク
     * Folder list acquisition task
     */
    private GetFolderListTask mGetFolderListTask;

    /**
     * ファイル一覧取得タスク
     * File list acquisition task
     */
    private GetFileListTask mGetFileListTask;

    /**
     * 次ファイル一覧取得タスク
     * Next file list acquisition task
     */
    private GetFileContinuationListTask mGetFileContinuationListTask;

    /**
     * フォルダパスワード検証タスク
     * Folder password confirmation task
     */
    private ConfirmFolderPassTask mConfirmFolderPassTask;

    /**
     * ファイルパスワード検証タスク
     * File password confirmation task.
     */
    private ConfirmFilePassTask mConfirmFilePassTask;

    private ScanSampleApplication mApplication;
    
    /**
     * Define the prefix for log information with abbreviation package and class name
     */
    private final static String PREFIX = "activity:StorageAct:";

    /**
     * フォルダ/ファイルのデータクラス
     * Data class of folder/file.
     */
    private class ItemData {
        String id;
        String name;
        String pass;

        void set(String id, String name, String pass) {
            this.id = id;
            this.name = name;
            this.pass = pass;
        }

        void reset() {
            id = null;
            name = null;
            pass = null;
        }
    }

    /**
     * アクティビティが生成されると呼び出されます。
     * [処理内容]
     *   (1)起動モードのチェック
     *   (2)参照の初期化
     *   (3)ページタイトルの初期化
     *   (4)フォルダ/ファイル一覧の設定
     *   (5)OKボタンの設定
     *   (6)キャンセルボタンの設定
     *
     * Called when an activity is generated.
     * [Processes]
     *   (1) Check startup mode
     *   (2) Initialize reference
     *   (3) Initialize page title
     *   (4) Set folder/file list
     *   (5) Set OK button
     *   (6) Set Cancel button
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        //(1)
        Intent intent = getIntent();
        if (intent != null) {
            mMode = intent.getStringExtra(KEY_MODE);
        }
        if (!VALUE_MODE_STORE_FOLDER.equals(mMode) &&
            !VALUE_MODE_SEND_FOLDER.equals(mMode) &&
            !VALUE_MODE_SEND_FILE.equals(mMode)) {
            setResult(RESULT_CANCELED);
            finish();
        }

        //(2)
        mSelectedFolder = new ItemData();
        mSelectedFile = new ItemData();

        mStorageSettingDataHolder = ((ScanSampleApplication)getApplication()).getStorageSettingDataHolder();
        if (mMode.equals(VALUE_MODE_STORE_FOLDER)) {
            StorageStoreData storeData = mStorageSettingDataHolder.getStorageStoreData();
            if (storeData!=null) {
                mSelectedFolder.id = storeData.getFolderId();
                mSelectedFolder.name = storeData.getFileName();
                mSelectedFolder.pass = storeData.getFolderPass();
            }
        }
        if (mMode.equals(VALUE_MODE_SEND_FOLDER) ||
                mMode.equals(VALUE_MODE_SEND_FILE)) {
            StorageSendData sendData = mStorageSettingDataHolder.getStorageSendData();
            if (sendData!=null) {
                mSelectedFolder.id = sendData.getFolderId();
                mSelectedFolder.name = sendData.getFileName();
                mSelectedFolder.pass = sendData.getFolderPass();
                mSelectedFile.id = sendData.getFileId();
                mSelectedFile.name = sendData.getFileName();
                mSelectedFile.pass = sendData.getFilePass();
            }
        }

        //(3)
        final TextView txt_title = (TextView) findViewById(R.id.textView_title);
        if (mMode.equals(VALUE_MODE_STORE_FOLDER)
                || mMode.equals(VALUE_MODE_SEND_FOLDER)) {
            txt_title.setText(R.string.txid_storage_t_folder_title);
        } else if (mMode.equals(VALUE_MODE_SEND_FILE)) {
            txt_title.setText(mSelectedFolder.name);
        }

        //(4)
        mFolderList = new ArrayList<FolderList>();
        mFileList = new ArrayList<FileList>();
        mItemListAdapter = new ItemListAdapter();

        mItemListView = (ListView) findViewById(R.id.listview_item_entry);
        mFooterView = (LinearLayout) getLayoutInflater().inflate(R.layout.list_footer_next_item, null);
        mFooterView.setVisibility(View.INVISIBLE);
        mItemListView.addFooterView(mFooterView);
        mItemListView.setAdapter(mItemListAdapter);

        mApplication = (ScanSampleApplication)getApplication();
        mItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Check dialog showing or not
                if(mApplication.ismIsDialogBtnClicked()){
                    return ;
                }
                
                if (mMode.equals(VALUE_MODE_STORE_FOLDER)) {
                    FolderList folder = mFolderList.get(position);
                    if (folder.isSetPassword()) {
                        // Set mIsDialogBtnClicked to true when clicking file list item
                        mApplication.setmIsDialogBtnClicked(true);
                        
                        AlertDialog dialog = createFolderPasswordInputDialog(folder);
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            
                            @Override
                            public void onDismiss(DialogInterface arg0) {
                                // Reset mIsDialogBtnClicked to false if dialog is closed
                                if(!mDialogOKButtonClicked)
                                    mApplication.setmIsDialogBtnClicked(false);
                            }
                        });
                        DialogUtil.showDialog(dialog, DialogUtil.INPUT_DIALOG_WIDTH, 0);
                    } else {
                        mSelectedFolder.set(folder.getFolderId(), folder.getFolderName(), null);
                        mOkButton.setEnabled(true);
                        mItemListView.invalidateViews();
                    }
                } else if ( mMode.equals(VALUE_MODE_SEND_FOLDER) ) {
                    FolderList folder = mFolderList.get(position);
                    if (folder.isSetPassword()) {
                        // Set mIsDialogBtnClicked to true when clicking file list item
                        mApplication.setmIsDialogBtnClicked(true);
                        
                        AlertDialog dialog = createFolderPasswordInputDialog(folder);
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            
                            @Override
                            public void onDismiss(DialogInterface arg0) {
                                // Reset mIsDialogBtnClicked to false if dialog is closed
                                if(!mDialogOKButtonClicked)
                                    mApplication.setmIsDialogBtnClicked(false);
                            }
                        });
                        DialogUtil.showDialog(dialog, DialogUtil.INPUT_DIALOG_WIDTH, 0);
                    } else {
                        mSelectedFolder.set(folder.getFolderId(), folder.getFolderName(), null);
                        mMode = VALUE_MODE_SEND_FILE;
                        String folderName = folder.getFolderName();
                        if ( (folderName == null) || ("".equals(folderName)) ) {
                            txt_title.setText(folder.getFolderId());
                        } else {
                            txt_title.setText(folderName);
                        }
                        mCancelButton.setText(R.string.txid_cmn_b_back);
                        mGetFileListTask = new GetFileListTask();
                        mGetFileListTask.execute();
                    }
                } else if (mMode.equals(VALUE_MODE_SEND_FILE)) {
                    FileList file = mFileList.get(position);
                    if (file.isSetPassword()) {
                        // Set mIsDialogBtnClicked to true when clicking file list item
                        mApplication.setmIsDialogBtnClicked(true);
                        
                        AlertDialog dialog = createFilePasswordInputDialog(file);
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            
                            @Override
                            public void onDismiss(DialogInterface arg0) {
                                // Reset mIsDialogBtnClicked to false if dialog is closed
                                if(!mDialogOKButtonClicked)
                                    mApplication.setmIsDialogBtnClicked(false);
                            }
                        });
                        DialogUtil.showDialog(dialog, DialogUtil.INPUT_DIALOG_WIDTH, 0);
                    } else {
                        mSelectedFile.set(file.getFileId(), file.getFileName(), null);
                        mOkButton.setEnabled(true);
                        mItemListView.invalidateViews();
                    }
                }
            }
        });

        Button btn_next = (Button) mFooterView.findViewById(R.id.btn_next);
        btn_next.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mItemListView.invalidateViews();
                return false;
            }
        });


        //(5)
        mOkButton = (Button)findViewById(R.id.button_ok);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mMode.equals(VALUE_MODE_STORE_FOLDER)) {
                    StorageStoreData data = new StorageStoreData();
                    data.setFolderId(mSelectedFolder.id);
                    data.setFolderName(mSelectedFolder.name);
                    data.setFolderPass(mSelectedFolder.pass);
                    mStorageSettingDataHolder.setStorageStoreData(data);
                } else if (mMode.equals(VALUE_MODE_SEND_FOLDER)
                        || mMode.equals(VALUE_MODE_SEND_FILE)) {
                    StorageSendData data = new StorageSendData();
                    data.setFolderId(mSelectedFolder.id);
                    data.setFolderName(mSelectedFolder.name);
                    data.setFolderPass(mSelectedFolder.pass);
                    data.setFileId(mSelectedFile.id);
                    data.setFileName(mSelectedFile.name);
                    data.setFilePass(mSelectedFile.pass);
                    mStorageSettingDataHolder.setStorageSendData(data);
                }
                setResult(RESULT_OK);
                finish();
            }
        });

        mOkButton.setEnabled(false);
        if (mMode.equals(VALUE_MODE_STORE_FOLDER)) {
            StorageStoreData data = mStorageSettingDataHolder.getStorageStoreData();
            if (data!=null && data.getFolderId()!=null) {
                mOkButton.setEnabled(true);
            }
        } else if (mMode.equals(VALUE_MODE_SEND_FILE)) {
            StorageSendData data = mStorageSettingDataHolder.getStorageSendData();
            if (data!=null && data.getFileId()!=null) {
                mOkButton.setEnabled(true);
            }
        }

        //(6)
        mCancelButton = (Button)findViewById(R.id.button_cancel);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMode.equals(VALUE_MODE_STORE_FOLDER)
                        || mMode.equals(VALUE_MODE_SEND_FOLDER)) {
                    setResult(RESULT_CANCELED);
                    finish();
                } else if (mMode.equals(VALUE_MODE_SEND_FILE)) {
                    mMode = VALUE_MODE_SEND_FOLDER;
                    mSelectedFile.reset();
                    mGetFolderListTask = new GetFolderListTask();
                    mGetFolderListTask.execute();
                    txt_title.setText(R.string.txid_storage_t_folder_title);
                    mOkButton.setEnabled(false);
                    mCancelButton.setText(R.string.txid_cmn_b_cancel);
                }
            }
        });
        if (mMode.equals(VALUE_MODE_SEND_FILE)) {
            mCancelButton.setText(R.string.txid_cmn_b_back);
        } else {
            mCancelButton.setText(R.string.txid_cmn_b_cancel);
        }

    }

    /**
     * フォルダパスワードを入力するダイアログを生成します。
     * Generates a dialog to enter folder password.
     * @param folder 選択フォルダ
     *               Selected folder.
     * @return dialog
     */
    private AlertDialog createFolderPasswordInputDialog(final FolderList folder) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.dlg_enter_pass, null);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle(R.string.txid_storage_m_pass_enter);
        builder.setView(view);

        final EditText txt_pass = (EditText) view.findViewById(R.id.edit_pass);

        builder.setNegativeButton(R.string.txid_cmn_b_ok,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(mDialogOKButtonClicked)
                    return ;
                //Click OK Button, change flag to true
                mDialogOKButtonClicked = true;
                
                mConfirmFolderPassTask = new ConfirmFolderPassTask(folder, txt_pass.getText().toString());
                mConfirmFolderPassTask.execute();
            }
        });

        builder.setPositiveButton(R.string.txid_cmn_b_cancel, null);

        return builder.create();
    }


    /**
     * ファイルパスワードを入力するダイアログを生成します。
     * Creates the dialog to enter the file password.
     * @param folder
     * @return
     */
    private AlertDialog createFilePasswordInputDialog(final FileList file) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.dlg_enter_pass, null);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle(R.string.txid_storage_m_pass_enter);
        builder.setView(view);

        final EditText txt_pass = (EditText) view.findViewById(R.id.edit_pass);

        builder.setNegativeButton(R.string.txid_cmn_b_ok,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(mDialogOKButtonClicked)
                    return ;
                //Click OK Button, change flag to true
                mDialogOKButtonClicked = true;
                
                mConfirmFilePassTask = new ConfirmFilePassTask(file, txt_pass.getText().toString());
                mConfirmFilePassTask.execute();
            }
        });

        builder.setPositiveButton(R.string.txid_cmn_b_cancel, null);

        return builder.create();
    }

    /**
     * 入力されたフォルダパスワードを検証するタスクです。
     * The task to confirm the entered folder password.
     */
    private class ConfirmFolderPassTask extends AsyncTask<Void, Void, Integer> {
        FolderList folder;
        String pass;
        
        //Add new flag to record exit button state
        boolean exitBtnClicked = false;

        private ConfirmFolderPassTask(FolderList folder, String pass){
            this.folder = folder;
            this.pass = pass;
        };

        @Override
        protected Integer doInBackground(Void... params) {
            RequestHeader header = new RequestHeader();
            header.put(RequestHeader.KEY_X_APPLICATION_ID, SmartSDKApplication.getProductId());
            header.put("X-Folder-Password", pass);
            Request request = new Request();
            request.setHeader(header);

            try {
                Folders folders = new Folders();
                Response<GetFolderResponseBody> response = folders.getFolder(request, folder.getFolderId());
                int status = response.getStatusCode();
                return status;
            } catch (InvalidResponseException e) {
                Log.d(Const.TAG, PREFIX + "invalid folder password. " + e.getResponse().getStatusLine());
                return e.getStatusCode();
            } catch (IOException e) {
                Log.w(Const.TAG, PREFIX + e.toString());
            }

            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == 200) {
                //Reset flag when dialog is closed
                mDialogOKButtonClicked = false;
                mApplication.setmIsDialogBtnClicked(false);
                
                mSelectedFolder.set(folder.getFolderId(), folder.getFolderName(), pass);
                if (mMode.equals(VALUE_MODE_STORE_FOLDER)) {
                    mOkButton.setEnabled(true);
                    mItemListView.invalidateViews();
                } else if (mMode.equals(VALUE_MODE_SEND_FOLDER)) {
                    mMode = VALUE_MODE_SEND_FILE;
                    mGetFileListTask = new GetFileListTask();
                    mGetFileListTask.execute();
                }
            } else {                
                AlertDialog.Builder builder = new AlertDialog.Builder(StorageActivity.this);
                builder.setIcon(android.R.drawable.ic_dialog_info);
                builder.setMessage(R.string.txid_storage_m_pass_folder_error);
                builder.setPositiveButton(R.string.txid_cmn_b_exit, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Error message dialog is closed, Reset flag to false
                        mDialogOKButtonClicked = false;
                        if(exitBtnClicked){
                            return ;
                        }
                        exitBtnClicked = true;
                        
                        AlertDialog d = createFolderPasswordInputDialog(folder);
                        d.setOnDismissListener(new DialogInterface.OnDismissListener() {
                              
                            @Override
                            public void onDismiss(DialogInterface arg0) {
                                if(!mDialogOKButtonClicked)
                                    mApplication.setmIsDialogBtnClicked(false);
                            }
                        });
                        DialogUtil.showDialog(d, DialogUtil.INPUT_DIALOG_WIDTH, 0);
                        
                    }
                });

                AlertDialog dialog = builder.show();
                // Add onDismissListener for error message dialog
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    
                    @Override
                    public void onDismiss(DialogInterface arg0) {
                        if(mDialogOKButtonClicked){
                            mApplication.setmIsDialogBtnClicked(false);
                        }
                        mDialogOKButtonClicked = false;
                    }
                });
            }
        }
    }

    /**
     * 入力されたファイルパスワードを検証するタスクです。
     * A task to verify entered folder password.
     */
    private class ConfirmFilePassTask extends AsyncTask<Void, Void, Integer> {
        FileList file;
        String pass;
        
        //Add new flag to record exit button state
        boolean exitBtnClicked = false;
        
        private ConfirmFilePassTask(FileList file, String pass){
            this.file = file;
            this.pass = pass;
        };

        @Override
        protected Integer doInBackground(Void... params) {
            RequestHeader header = new RequestHeader();
            header.put(RequestHeader.KEY_X_APPLICATION_ID, SmartSDKApplication.getProductId());
            header.put("X-File-Password", pass);
            Request request = new Request();
            request.setHeader(header);

            try {
                Files files = new Files();
                Response<GetFileResponseBody> response = files.getFile(request, file.getFileId());
                int status = response.getStatusCode();
                return status;
            } catch (InvalidResponseException e) {
                Log.d(Const.TAG, PREFIX + "invalid file password. " + e.getResponse().getStatusLine());
                return e.getStatusCode();
            } catch (IOException e) {
                Log.w(Const.TAG, PREFIX + e.toString());
            }

            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == 200) {
                //Reset flag when dialog is closed
                mDialogOKButtonClicked = false;
                mApplication.setmIsDialogBtnClicked(false);
                
                mSelectedFile.set(file.getFileId(), file.getFileName(), pass);
                mOkButton.setEnabled(true);
                mItemListView.invalidateViews();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(StorageActivity.this);
                builder.setIcon(android.R.drawable.ic_dialog_info);
                builder.setMessage(R.string.txid_storage_m_pass_file_error);
                builder.setPositiveButton(R.string.txid_cmn_b_exit, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Error message dialog is closed, Reset flag to false
                        mDialogOKButtonClicked = false;
                        if(exitBtnClicked){
                            return ;
                        }
                        exitBtnClicked = true;
                        
                        AlertDialog d = createFilePasswordInputDialog(file);
                        d.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            
                            @Override
                            public void onDismiss(DialogInterface arg0) {
                                if(!mDialogOKButtonClicked)
                                    mApplication.setmIsDialogBtnClicked(false);
                            }
                        });
                        DialogUtil.showDialog(d, DialogUtil.INPUT_DIALOG_WIDTH, 0);
                    }
                });
                
                AlertDialog dialog = builder.show();
                // Add onDismissListener for error message dialog
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    
                    @Override
                    public void onDismiss(DialogInterface arg0) {
                        if(mDialogOKButtonClicked){
                            mApplication.setmIsDialogBtnClicked(false);
                        }
                        mDialogOKButtonClicked = false;
                    }
                });
            }
        }
    }

    /**
     * フォルダ一覧を取得するタスクです。
     * A task to obtain folder list.
     */
    private class GetFolderListTask extends AsyncTask<String, FolderList, ArrayList<FolderList>> {

        @Override
        protected void onPreExecute() {
            mFolderList.clear();
            mFileList.clear();
            mItemListView.removeAllViewsInLayout();
            mItemListAdapter.notifyDataSetChanged();
            mFooterView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected ArrayList<FolderList> doInBackground(String... params) {
            ArrayList<FolderList> itemList = new ArrayList<FolderList>();

            RequestHeader header = new RequestHeader();
            header.put(RequestHeader.KEY_X_APPLICATION_ID, SmartSDKApplication.getProductId());

            RequestQuery query = new RequestQuery();
            query.put("num", "202");
            query.put("offset", "1");

            Request request = new Request();
            request.setHeader(header);
            request.setQuery(query);

            try {
                Folders folders = new Folders();
                Response<GetFolderListResponseBody> folderListResponse = folders.getFolderList(request);
                FolderListArray folderListArray = folderListResponse.getBody().getFolderList();

                for (int i = 0; i < folderListArray.size(); ++i) {
                    if (isCancelled()) {
                        Log.d(Const.TAG, PREFIX + "getFolderList task aborted");
                        break;
                    }

                    FolderList folder = folderListArray.get(i);
                    if ( (folder == null) || ("fax_received_files".equals(folder.getFolderId())) ) {
                        continue;
                    }

                    itemList.add(folder);
                    publishProgress(folder);
                }

            } catch (Exception e) {
                Log.w(Const.TAG, PREFIX + e.toString());
            }
            return itemList;
        }

        @Override
        protected void onProgressUpdate(FolderList... values) {
            mFolderList.add(values[0]);
            mItemListAdapter.notifyDataSetChanged();
            showProgressBar(false);
        }

        @Override
        protected void onPostExecute(ArrayList<FolderList> result) {
            mItemListAdapter.notifyDataSetChanged();
            showProgressBar(false);
        }

    }

    /**
     * ファイル一覧を取得するタスクです。
     * The task to obtain file list.
     */
    private class GetFileListTask extends AsyncTask<String, FileList, ArrayList<FileList>> {
        private String nextLink;

        @Override
        protected void onPreExecute() {
            mFolderList.clear();
            mFileList.clear();
            mItemListView.removeAllViewsInLayout();
            mItemListAdapter.notifyDataSetChanged();
        }

        @Override
        protected ArrayList<FileList> doInBackground(String... params) {
            ArrayList<FileList> itemList = new ArrayList<FileList>();
            Log.d(Const.TAG, PREFIX + "start GetFileListTask");

            RequestHeader header = new RequestHeader();
            header.put(RequestHeader.KEY_X_APPLICATION_ID, SmartSDKApplication.getProductId());

            if (mSelectedFolder.pass!=null) {
                header.put("X-Folder-Password", mSelectedFolder.pass);
            }

            RequestQuery query = new RequestQuery();
            query.put("folderId", mSelectedFolder.id);
            query.put("num", "100");
            query.put("offset", "1");
            query.put("operationType", "scanSend");
            query.put("sort", "createDateTime");
            query.put("order", "down");

            Request request = new Request();
            request.setHeader(header);
            request.setQuery(query);

            try {
                Files files = new Files();
                Response<GetFileListResponseBody> fileListResponse = files.getFileList(request);
                GetFileListResponseBody responseBody = fileListResponse.getBody();
                nextLink = responseBody.getNextLink();

                FileListArray fileListArray = responseBody.getFileList();
                for (int i = 0; i < fileListArray.size(); ++i) {
                    if (isCancelled()) {
                        Log.d(Const.TAG, PREFIX + "getFileList task aborted");
                        break;
                    }

                    FileList file = fileListArray.get(i);

                    itemList.add(file);
                    publishProgress(file);
                }

            } catch (Exception e) {
                Log.w(Const.TAG, PREFIX + e.toString());
            }
            return itemList;
        }

        @Override
        protected void onProgressUpdate(FileList... values) {
            mFileList.add(values[0]);
            mItemListAdapter.notifyDataSetChanged();
            showProgressBar(false);
        }

        @Override
        protected void onPostExecute(ArrayList<FileList> result) {
            if (nextLink!=null) {
                Button btn_next = (Button) mFooterView.findViewById(R.id.btn_next);
                btn_next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mGetFileContinuationListTask = new GetFileContinuationListTask();
                        mGetFileContinuationListTask.execute(nextLink);
                    }
                });
                btn_next.setVisibility(View.VISIBLE);

                ProgressBar prog_next = (ProgressBar)mFooterView.findViewById(R.id.prog_next);
                prog_next.setVisibility(View.GONE);
                mFooterView.setVisibility(View.VISIBLE);
                mItemListView.invalidateViews();
            }
            mItemListAdapter.notifyDataSetChanged();
            showProgressBar(false);
        }

    }

    /**
     * 続きのファイル一覧を取得するタスクです。
     * A task to obtain remaining file list information.
     */
    private class GetFileContinuationListTask extends AsyncTask<String, FileList, ArrayList<FileList>> {

        private Button btn_next;
        private ProgressBar prog_next;
        private String nextLink;

        @Override
        protected void onPreExecute() {
            btn_next = (Button) mFooterView.findViewById(R.id.btn_next);
            btn_next.setVisibility(View.GONE);
            prog_next = (ProgressBar)mFooterView.findViewById(R.id.prog_next);
            prog_next.setVisibility(View.VISIBLE);
            mItemListView.invalidateViews();
        }

        @Override
        protected ArrayList<FileList> doInBackground(String... next) {
            ArrayList<FileList> itemList = new ArrayList<FileList>();
            Log.d(Const.TAG, PREFIX + "start GetFileContinuationListTask");

            RequestHeader header = new RequestHeader();
            header.put(RequestHeader.KEY_X_APPLICATION_ID, SmartSDKApplication.getProductId());
            if (mSelectedFolder.pass!=null) {
                header.put("X-Folder-Password", mSelectedFolder.pass);
            }

            Request request = new Request();
            request.setHeader(header);

            try {
                Files files = new Files();
                Response<GetFileListResponseBody> fileListResponse = files.getFileListContinuationResponse(request, next[0]);
                GetFileListResponseBody responseBody = fileListResponse.getBody();

                nextLink = responseBody.getNextLink();
                if (nextLink!=null) {
                    btn_next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mGetFileContinuationListTask = new GetFileContinuationListTask();
                            mGetFileContinuationListTask.execute(nextLink);
                        }
                    });
                }

                FileListArray fileListArray = responseBody.getFileList();
                for (int i = 0; i < fileListArray.size(); ++i) {
                    if (isCancelled()) {
                        Log.d(Const.TAG, PREFIX + "getFileContinuationList task aborted");
                        break;
                    }

                    FileList file = fileListArray.get(i);
                    itemList.add(file);
                    publishProgress(file);
                }

            } catch (Exception e) {
                Log.w(Const.TAG, PREFIX + e.toString());
            }
            return itemList;
        }

        @Override
        protected void onProgressUpdate(FileList... values) {
            mFileList.add(values[0]);
            mItemListAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(ArrayList<FileList> result) {
            mItemListAdapter.notifyDataSetChanged();
            prog_next.setVisibility(View.GONE);
            btn_next.setVisibility(View.VISIBLE);

            if (nextLink==null) {
                mFooterView.setVisibility(View.INVISIBLE);
            }
            mItemListView.invalidateViews();
        }

    }

    /**
     * このアクティビティが開始した際に呼び出されます。
     * フォルダ一覧取得タスクを開始します。
     * Called when this activity starts.
     * Starts the task to obtain folder list.
     */
    @Override
    public void onStart() {
        super.onStart();
        if (mMode.equals(VALUE_MODE_STORE_FOLDER)
                || mMode.equals(VALUE_MODE_SEND_FOLDER)) {
            mGetFolderListTask = new GetFolderListTask();
            mGetFolderListTask.execute();
        } else if (mMode.equals(VALUE_MODE_SEND_FILE)) {
            mGetFileListTask = new GetFileListTask();
            mGetFileListTask.execute();
        }
    }

    /**
     * このアクティビティが停止した際に呼び出されます。
     * 非同期タスクが実行中だった場合、キャンセルします。
     * Called when this activity is stopped.
     * If asynchronous task is in process, the task is cancelled
     */
    @Override
    public void onStop() {
        if (mGetFolderListTask != null) {
            mGetFolderListTask.cancel(false);
            mGetFolderListTask = null;
        }
        if (mGetFileListTask != null) {
            mGetFileListTask.cancel(false);
            mGetFileListTask = null;
        }
        if (mGetFileContinuationListTask != null) {
            mGetFileContinuationListTask.cancel(false);
            mGetFileContinuationListTask = null;
        }
        if (mConfirmFolderPassTask != null) {
            mConfirmFolderPassTask.cancel(false);
            mConfirmFolderPassTask = null;
        }
        if (mConfirmFilePassTask != null) {
            mConfirmFilePassTask.cancel(false);
            mConfirmFilePassTask = null;
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Reset flag to false when activity is finished
        DialogUtil.storeFileSettingDialogBtnClicked = false;
    }

    /**
     * このアクティビティが再開した際に呼び出されます。
     * Called when this activity is resumed.
     */
    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(DialogUtil.INTENT_ACTION_SUB_ACTIVITY_RESUMED));
    }


    /**
     * プログレスバーの表示・非表示を行います。
     * Displays/hides the progress bar.
     *
     * @param show
     */
    private void showProgressBar(boolean show) {
        ViewGroup itemListArea = (ViewGroup) findViewById(R.id.layout_item_view);
        ViewGroup progressArea = (ViewGroup) findViewById(R.id.layout_progress);

        if (show) {
            itemListArea.setVisibility(View.INVISIBLE);
            progressArea.setVisibility(View.VISIBLE);
        } else {
            progressArea.setVisibility(View.INVISIBLE);
            itemListArea.setVisibility(View.VISIBLE);
        }
    }


    /**
     * フォルダ/ファイルのアダプタークラスです。
     * Adapter class of folder/file list
     */
    private class ItemListAdapter extends BaseAdapter {

        private class ViewHolder {
            ViewGroup container;
            ImageView lock;
            TextView keyDisplay;
        }

        private final LayoutInflater mInflater;

        ItemListAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.list_row_storage_item, null);
                holder.container = (ViewGroup) convertView.findViewById(R.id.item_container);
                holder.lock = (ImageView) convertView.findViewById(R.id.img_lock);
                holder.keyDisplay = (TextView) convertView.findViewById(R.id.txt_key_display);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (mMode.equals(VALUE_MODE_SEND_FOLDER) ||
                    mMode.equals(VALUE_MODE_STORE_FOLDER )) {
                FolderList folder = (FolderList)getItem(position);
                holder.lock.setVisibility(folder.isSetPassword() ? View.VISIBLE: View.INVISIBLE);
                String folderName = folder.getFolderName();
                if ((folderName == null) || ("".equals(folderName))) {
                    holder.keyDisplay.setText(folder.getFolderId());
                } else {
                    holder.keyDisplay.setText(folderName);
                }
                if (folder.getFolderId().equals(mSelectedFolder.id)) {
                    holder.container.setBackgroundResource(R.drawable.sim_bt_button_w);
                } else {
                    holder.container.setBackgroundResource(R.drawable.sim_bt_button_n);
                }
            } else if (mMode.equals(VALUE_MODE_SEND_FILE)) {
                FileList file = (FileList)getItem(position);
                holder.lock.setVisibility(file.isSetPassword() ? View.VISIBLE: View.INVISIBLE);
                holder.keyDisplay.setText(file.getFileName());
                if (file.getFileId().equals(mSelectedFile.id)) {
                    holder.container.setBackgroundResource(R.drawable.sim_bt_button_w);
                } else {
                    holder.container.setBackgroundResource(R.drawable.sim_bt_button_n);
                }
            }

            return convertView;
        }

        @Override
        public int getCount() {
            if (mMode.equals(VALUE_MODE_SEND_FOLDER) ||
                    mMode.equals(VALUE_MODE_STORE_FOLDER )) {
                return mFolderList.size();
            } else if (mMode.equals(VALUE_MODE_SEND_FILE)) {
                return mFileList.size();
            } else {
                /* should never reach here */
                Log.d(Const.TAG, PREFIX + "unknown mode:" + mMode);
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            if (mMode.equals(VALUE_MODE_SEND_FOLDER) ||
                    mMode.equals(VALUE_MODE_STORE_FOLDER )) {
                return mFolderList.get(position);
            } else if (mMode.equals(VALUE_MODE_SEND_FILE)) {
                return mFileList.get(position);
            } else {
                /* should never reach here */
                Log.d(Const.TAG, PREFIX + "unknown mode:" + mMode);
                return null;
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

    }

}
