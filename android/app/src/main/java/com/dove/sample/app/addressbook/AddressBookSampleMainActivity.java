/*
 *  Copyright (C) 2013-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.app.addressbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dove.R;
import com.dove.sample.app.addressbook.logic.AddressBookSampleLogic;
import com.dove.sample.wrapper.auth.command.GetAuthStateCommand;
import com.dove.sample.wrapper.auth.data.AuthState;
import com.dove.sample.wrapper.common.Utils;
import com.dove.sample.wrapper.log.Logger;
import com.dove.sample.wrapper.log.Logger.LogRecorder;
import com.dove.sample.wrapper.rws.addressbook.Entry;
import com.dove.sample.wrapper.rws.addressbook.Entry.UserCodeData;
import com.dove.sample.wrapper.rws.addressbook.EntryArray;
import com.dove.sample.wrapper.rws.addressbook.GetEntryListResponseBody;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * アドレス帳サンプルアプリケーションのメインアクティビティクラスです。<br>
 * android.app.Activity クラスを継承します。<br>
 * Main activity class of the address book sample application.<br>
 * Inherits the  android.app.Activity class.<br>
 * <pre>
 * この、機器情報取得サンプルアプリケーションでは、
 * メイン画面に以下の画面部品を持ちます。
 *   ・アドレス帳ユーザ検索条件入力欄
 *   ・アドレス帳検索ユーザ一覧表示欄
 *   ・新規ユーザ作成ボタン
 *
 * ユーザ検索条件入力欄では、入力された文字列をユーザ名に含むユーザを検索します。
 * 検索ユーザ一覧表示欄では、検索されたユーザ一覧を取得し表示します。
 * 新規ユーザ作成ボタンでは、押下時にユーザ情報ダイアログを表示します。
 *
 * 本クラスでは、Activity インタフェースを実装し、Android システムからのアプリ状態メソッド呼び出しをトリガとして動作を行います。<br>
 * サンプルアプリケーションでは、アプリケーションの生成時に基本となる画面を構成するため、
 * 以下の 1メソッドを実装します。
 * (1) アプリケーション生成時 (onCreate())
 *
 * This sample application to obtain machine information
 * supports the following screen components on the main screen.
 *  - Address book user search conditions entry field
 *  - Address book searched users display area
 *  - "Create user" button
 *
 * On the user search conditions entry field, the user names which include the texts entered by the user are searched.
 * On the searched users display area, the list of searched users is obtained and displayed.
 * "Create user" button displays user information dialog when pressed.
 *
 * This class implements the Activity interface. Application state method call from Android system is used as a trigger.
 * Since the sample application builds the base screen at the time the application is created,
 * the following method is implemented.
 * (1) The time the application is created  (onCreate())
 * </pre>
 */
public class AddressBookSampleMainActivity extends Activity {

    /**
     * Define the prefix for log information with abbreviation package and class name
     */
    private final static String PREFIX = "addressb:AddressBSMainAct:";

    /**
     * アプリケーションのプロダクトID<br>
     * onCreate() 時に AndroidManifest.xml の記載に基づいて設定されます。<br>
     * Product ID of the application<br>
     * Set based on the description of AndroidManifest.xml at the time of onCreate().
     */
    private String productId = "";

    /**
     * Buttonの状態を保持します。
     * Button click flag
     */
    private boolean mIsDialogBtnClicked = false;

    // Setter of mIsDialogBtnClicked
    public void setmIsDialogBtnClicked(boolean mIsDialogBtnClicked) {
        this.mIsDialogBtnClicked = mIsDialogBtnClicked;
    }

    /**
     * アプリケーション生成時にシステムから呼び出されます。<br>
     * Called by system at the time the application is created.
     * <pre>
     * ここでは、
     * 1. AndroidManifest.xml から、アプリケーションのプロダクトID の読み込み
     * を行います。
     * また、
     * 2. activity_addressbook_sample_main.xml からアプリケーションのレイアウトの読み込み
     * 3. 新規作成ボタンのアクション設定
     * 4. ユーザー検索条件入力欄へのアクション設定
     * を行い、基本の画面を構成します。
     *
     * Here, the following process is performed.
     * 1. Load application product ID from AndroidManifest.xml
     * The following processes are also performed.
     * 2. Load application layout from activity_addressbook_sample_main.xml
     * 3. Set action for "Create user" button
     * 4. Set action for user search conditions entry field
     * Base screen is built.
     * </pre>
     *
     * @param savedInstanceState
     * @see android.app.Activity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.setRecorder(mLogRecoder);

        //Register the log tag of AddressBookSample module which is used in wrapper layer
        System.setProperty("com.dove.sample.log.TAG", Const.TAG);
        Utils.setTagName();

        // 1
        try {
            final ApplicationInfo appInfo =
                    getPackageManager().getApplicationInfo(
                            this.getPackageName(), PackageManager.GET_META_DATA);
            productId = String.valueOf(appInfo.metaData.getInt("productId", 0));
        } catch (PackageManager.NameNotFoundException e) {
            productId = "";
        }

        // 2
        getWindow().setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_address_book_sample_main);

        // 3
        final TextView authStateText = (TextView) this.findViewById(R.id.auth_state_txt);
        final TextView loginStatusText = (TextView) this.findViewById(R.id.login_status_txt);
        authStateText.setText("Auth state: ");
        new GetAuthStateCommand(new GetAuthStateCommand.ResultReceiver() {
            @Override
            public void onReceiveResult(AuthState authState) {
                Log.d(Const.TAG, "Auth request result: " + authState.toString());
                String authStateStr = "Auth state: " + authState.toString();
                loginStatusText.setText("Login status: " + authState.getLoginStatus().toString());
                authStateText.setText(authStateStr);

                searchUser(authState.getUserName());
            }
        }).execute(getApplicationContext());

    }


    private void searchUser(String searchString) {
        final SearchUserListAsyncTask2 searchUserListTask =
                new SearchUserListAsyncTask2();
        searchUserListTask.execute(searchString);
    }

    class SearchUserListAsyncTask2
            extends AsyncTask<String, Void, List<Entry>> {

        private static final int USER_BUTTON_TEXT_SIZE_PIXELS = 20;
        private static final int USER_BUTTON_HEIGHT_PIXELS = 55;

        private ProgressBar progress = null;

        /**
         * タスク開始時<br>
         * At the time the task starts
         * <pre>
         * 本サンプルアプリケーションでは、
         * アプリUIの検索ユーザ一覧表示領域をクリアし、スピナープログレスを表示します。
         * In this sample application,
         * the searched user display area is cleared on application UI and a progress spinner is displayed.
         * </pre>
         * この部分をカスタマイズすることで、アプリケーション固有の UI に対応できます。<br>
         * Customizing this part enables corresponding with the application-specific UI.
         */
        @Override
        protected void onPreExecute() {
            final LinearLayout userListLayout =
                    (LinearLayout) findViewById(R.id.user_list_layout);

            userListLayout.removeAllViews();
            final TextView entryInfoText = (TextView) findViewById(R.id.entry_info_txt);
            entryInfoText.setText("");

            progress = new ProgressBar(AddressBookSampleMainActivity.this);
            userListLayout.addView(progress,
                    new LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT));
        }

        /**
         * タスク(非同期)処理<br>
         * Task (asynchronous) process
         * <pre>
         * アドレス帳エントリ一覧を取得します。
         * WebAPI を使用して、アドレス帳エントリ一覧を取得します。
         * その後、取得した一覧から検索結果に合致するエントリを抜き出します。
         * この結果は、onPostExecute() 実行時の引数として渡されます。
         * 1. アドレス帳のユーザー(エントリ)情報を全て取得します。
         * 2. 取得したユーザー(エントリ)リストから検索条件に一致するものを抜き出します。
         *
         * Obtains the list of entries in the address book.
         * Obtains the list of entries in the address book using web API.
         * Extracts the entries that match the search result from the obtained entries.
         * The result is passed as an argument when onPostExecute() is executed.
         * 1. Obtains all user (entry) information in the address book.
         * 2. Extracts the users (entries) that match the search result from the obtained user (entry) information.
         * </pre>
         *
         * @param searchStrs 検索条件<br>
         *                   Search conditions
         */
        @Override
        protected List<Entry> doInBackground(String... searchStrs) {

            final String searchStr = searchStrs[0];

            final AddressBookSampleLogic logic = new AddressBookSampleLogic(productId);

            // 1
            final List<Entry> entryList =
                    new ArrayList<Entry>();

            GetEntryListResponseBody firstResponse = logic.getEntryList();
            if (firstResponse == null) {
                return null;
            }
            addResultToEntryList(firstResponse, entryList);

            String nextLink = firstResponse.getNextLink();
            while (nextLink != null && !"".equals(nextLink)) {
                if (isCancelled()) {
                    return null;
                }
                final GetEntryListResponseBody nextResponse =
                        logic.getContinuationEntryList(nextLink);
                addResultToEntryList(nextResponse, entryList);
                if (nextResponse == null) {
                    return null;
                }
                nextLink = nextResponse.getNextLink();
            }

            // 2
            final List<Entry> hitEntryList =
                    new ArrayList<Entry>();

            for (final Iterator<Entry> iterator = entryList.iterator();
                 iterator.hasNext(); ) {
                final Entry simpleEntry =
                        (Entry) iterator.next();

                final String name = simpleEntry.getName();
                if (name != null
                        && name.contains(searchStr)) {
                    hitEntryList.add(simpleEntry);
                }

            }

            return hitEntryList;
        }

        /**
         * WebAPI から取得したエントリリスト情報をユーザーリストに追加します。<br>
         * Adds the entry list information obtained by web API to the user list.
         *
         * @param result エントリリスト情報<br>
         *               Entry list information
         * @param list   ユーザーリスト<br>
         *               User list
         */
        private void addResultToEntryList(GetEntryListResponseBody result, List<Entry> list) {
            if (result == null) {
                return;
            }
            final EntryArray entryArray = result.getEntriesData();
            for (final Iterator<Entry> iterator = entryArray.iterator();
                 iterator.hasNext(); ) {
                final Entry entry = iterator.next();

                final String entryId = entry.getEntryId();
                final Integer registrationNumber = entry.getRegistrationNumber();
                final String name = entry.getName();

//                final SimpleUserData simpleEntry =
//                        new SimpleUserData(entryId, registrationNumber, name);
                list.add(entry);
            }
        }

        /**
         * タスク処理終了時<br>
         * At the time the task process ends
         * <pre>
         * 本サンプルアプリケーションでは、
         * 取得した検索結果エントリ一覧をアプリUIの検索ユーザ一覧表示領域に表示します。
         * 1. 検索条件に一致したユーザー(エントリ)を一覧表示します。
         * 1-1. 情報取得に失敗している場合、Toast を表示して終了します。
         * 1-2. 検索条件に一致したユーザー(エントリ)が 5件以上の場合、再検索を促します。
         * 2. WebAPI の実行結果(引数として渡される result) から、情報を取得します。
         * 3. 文字列を整形し、Button に設定します。
         * 4. Button を検索ユーザ一覧表示領域に追加します。
         *
         * In this sample application,
         * the obtained searched entries are displayed on the searched user display area on application UI.
         * 1. Displays the users (entries) that matched the search conditions.
         * 1-1. If failed to obtain information, the application displays a toast and ends.
         * 1-2. If there are 5 ore more users (entries) that match the search result, the application prompts the user to try the search again.
         * 2. Obtains information from the web API result (the result passed as an argument).
         * 3. Forms the string and sets the string to the button.
         * 4. Adds the button to the searched user display area.
         * </pre>
         * この部分をカスタマイズすることで、アプリケーション固有の UI に対応できます。<br>
         * Customizing this part enables corresponding with the application-specific UI.
         */
        @Override
        protected void onPostExecute(List<Entry> hitEntryList) {
            // Reset mIsDialogBtnClicked flag to false
            setmIsDialogBtnClicked(false);

            // 1
            final LinearLayout userListLayout =
                    (LinearLayout) findViewById(R.id.user_list_layout);
            userListLayout.removeView(progress);

            // 1-1
            if (hitEntryList == null) {
                final Toast toast =
                        Toast.makeText(AddressBookSampleMainActivity.this,
                                R.string.err_msg_get_entry_list, Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            // 1-2
            final int hitCount = hitEntryList.size();
            if (hitCount > 5) {
                final TextView messageView = new TextView(AddressBookSampleMainActivity.this);
                messageView.setTextSize(TypedValue.COMPLEX_UNIT_PX, USER_BUTTON_TEXT_SIZE_PIXELS);
                final String message =
                        String.format(
                                getString(R.string.err_msg_user_name_search_over),
                                hitCount);
                messageView.setText(message);
                userListLayout.addView(messageView);
                return;
            }

            for (final Iterator<Entry> iterator = hitEntryList.iterator();
                 iterator.hasNext(); ) {
                // 2
                final Entry simpleEntry = iterator.next();
                final String entryId = simpleEntry.getEntryId();
                final Integer registrationNumber = simpleEntry.getRegistrationNumber();
                final String name = simpleEntry.getName();

                // 3
//                final Button userButton = new Button(AddressBookSampleMainActivity.this);
//                userButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, USER_BUTTON_TEXT_SIZE_PIXELS);
//                userButton.setText(registrationNumber.toString() + " : " + name);
//                userButton.setOnClickListener(new UserButtonOnClickListener(entryId));
//
//                // 4
//                userListLayout.addView(userButton,
//                        new LinearLayout.LayoutParams(
//                                LinearLayout.LayoutParams.MATCH_PARENT,
//                                USER_BUTTON_HEIGHT_PIXELS));

                final TextView entryInfoText = (TextView) findViewById(R.id.entry_info_txt);
                final UserCodeData userCodeData = simpleEntry.getUserCodeData();
                String entryInfoStrs = "Entry info: "
                        + "entryId: " + simpleEntry.getEntryId()
                        + ", name: " + simpleEntry.getName();
                if (userCodeData != null) {
                    entryInfoStrs += ", {"
                            + userCodeData.getLoginUserName()
                            + ", "
                            + (userCodeData.getLoginPassword() != null ? userCodeData.getLoginPassword() : "N/A")
                            + "}";
                }
                entryInfoText.setText(entryInfoStrs);
            }
        }
    }

    /**
     * ダイアログに含まれるボタン(BUTTON_NEGATIVE,BUTTON_NEUTRAL,BUTTON_POSITIVE)のclickableを一括で設定します。<br>
     * Specify clickable of buttons included in the dialog(BUTTON_NEGATIVE,BUTTON_NEUTRAL,BUTTON_POSITIVE) at once.
     *
     * @param dialog    clickableを設定したいダイアログ<br>
     *                  dialog to specify clickable
     * @param clickable true:クリック有効<br>
     *                  false：クリック無効<br>
     *                  true:Clink is valid.<br>
     *                  false：Click is invalid.
     */
    private void setButtonClickable(AlertDialog dialog, boolean clickable) {
        Button btn = null;
        btn = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        if (btn != null) {
            btn.setClickable(clickable);
        }

        btn = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        if (btn != null) {
            btn.setClickable(clickable);
        }

        btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        if (btn != null) {
            btn.setClickable(clickable);
        }
    }

    /**
     * com.dove.sample.wrapper.log.Logger.LogRecorderインターフェースの実装クラスです。<br>
     * com.dove.sample.wrapper.log.Logger.LogRecorder Interface implementation class.
     * <pre>
     * LogRecorderインターフェース実装クラスをcom.dove.sample.wrapper.log.Loggerクラスへ登録することで、wrapperパッケージのデバッグログがlogcatに出力されるようになります。
     * 本サンプルアプリケーションでは、onCreate()時にLogger.setRecorder()メソッドを呼び出して登録を行っています。
     * The wrapper package debug log can be output to logcat by registering LogRecorder interface implementation class to com.dove.sample.wrapper.log.Logger class.
     * This sample application registers by calling Logger.setRecorder() method in the case of onCreate().
     * </pre>
     */
    private final LogRecorder mLogRecoder = new LogRecorder() {
        @Override
        public void logging(String level, String tag, String msg) {

            if ("debug".equals(level)) {
                Log.d(tag, msg);
            } else if ("info".equals(level)) {
                Log.i(tag, msg);
            } else if ("warn".equals(level)) {
                Log.w(tag, msg);
            }

        }
    };
}
