package com.dove;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dove.sample.app.addressbook.AddressBookSampleMainActivity;
import com.dove.sample.app.addressbook.Const;
import com.dove.sample.app.addressbook.logic.AddressBookSampleLogic;
import com.dove.sample.wrapper.auth.command.GetAuthStateCommand;
import com.dove.sample.wrapper.auth.data.AuthState;
import com.dove.sample.wrapper.auth.event.AuthStateChangedEvent;
import com.dove.sample.wrapper.common.Utils;
import com.dove.sample.wrapper.json.EncodedException;
import com.dove.sample.wrapper.json.JsonUtils;
import com.dove.sample.wrapper.log.Logger;
import com.dove.sample.wrapper.log.Logger.LogRecorder;
import com.dove.sample.wrapper.rws.addressbook.Entry;
import com.dove.sample.wrapper.rws.addressbook.Entry.UserCodeData;
import com.dove.sample.wrapper.rws.addressbook.EntryArray;
import com.dove.sample.wrapper.rws.addressbook.GetEntryListResponseBody;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * Created by Administrator on 2017/12/8.
 */

public class RCTRicohAuth extends ReactContextBaseJavaModule {
    private final static String PREFIX = "rectmodule:RicohAuth:";

    public RCTRicohAuth(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "RCTRicohAuth";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        return constants;
    }

    private void sendEvent(ReactContext reactContext, String eventName, @Nullable WritableMap params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }

    private String loginUserId = "";

    /**
     * アプリケーションのプロダクトID<br>
     * onCreate() 時に AndroidManifest.xml の記載に基づいて設定されます。<br>
     * Product ID of the application<br>
     * Set based on the description of AndroidManifest.xml at the time of onCreate().
     */
    private String productId = "";
    private AuthState mCurrentState;

    /**
     * Buttonの状態を保持します。
     * Button click flag
     */
    private boolean mIsDialogBtnClicked = false;

    // Setter of mIsDialogBtnClicked
    public void setmIsDialogBtnClicked(boolean mIsDialogBtnClicked) {
        this.mIsDialogBtnClicked = mIsDialogBtnClicked;
    }

    private AuthStateChangedEvent.EventListener mAuthStateChangedEventListener = new AuthStateChangedEvent.EventListener() {
        @Override
        public void onReceiveEvent(AuthStateChangedEvent event) {
            synchronized ((RCTRicohAuth.this)) {
                mCurrentState = event.getAuthState();

                try {
                    String entryInfoStrs = "";
                    entryInfoStrs = JsonUtils.getEncoder().encode(mCurrentState);
                    WritableMap params = Arguments.createMap();
                    params.putString("authState", entryInfoStrs);
                    sendEvent(RCTRicohAuth.this.getReactApplicationContext(), "onAuthStateReceived", params);

                } catch (EncodedException e) {
                    WritableMap params = Arguments.createMap();
                    params.putString("message", e.getLocalizedMessage());
                    sendEvent(RCTRicohAuth.this.getReactApplicationContext(), "onAuthStateReceivedError", params);
                }
            }
        }
    };

    @Override
    public void initialize() {

        Logger.setRecorder(mLogRecoder);

        //Register the log tag of AddressBookSample module which is used in wrapper layer
        System.setProperty("com.dove.sample.log.TAG", Const.TAG);
        Utils.setTagName();

        // 1
        try {
            final ApplicationInfo appInfo = this.getCurrentActivity().getPackageManager()
                    .getApplicationInfo(this.getCurrentActivity().getPackageName(), PackageManager.GET_META_DATA);
            productId = String.valueOf(appInfo.metaData.getInt("productId", 0));
        } catch (PackageManager.NameNotFoundException e) {
            productId = "";
        }

        // 2
        // Add listener and get current auth service state
        AuthStateChangedEvent.addListener(RCTRicohAuth.this.getReactApplicationContext(),
                mAuthStateChangedEventListener);
        //        new GetAuthStateCommand(new GetAuthStateCommand.ResultReceiver() {
        //            @Override
        //            public void onReceiveResult(AuthState authState) {
        //                Log.d(Const.TAG, "Auth request result: " + authState.toString());
        //                String authStateStr = "Auth state: " + authState.toString();
        //
        //                WritableMap params = Arguments.createMap();
        //                params.putString("loginStatus", authState.getLoginStatus().toString());
        //                sendEvent(RCTRicohAuth.this.getReactApplicationContext(), "onLoginStatusReceived", params);
        //
        //                loginUserId = authState.getUserId();
        //                searchUser(authState.getUserName());
        //            }
        //        }).execute(this.getCurrentActivity().getApplicationContext());
    }

    @ReactMethod
    public void getAuthState(Promise promise) throws JSONException {
        try {

            new GetAuthStateCommand(new GetAuthStateCommand.ResultReceiver() {
                @Override
                public void onReceiveResult(AuthState authState) {
                    Log.d(Const.TAG, "Auth request result: " + authState.toString());
                    String authStateStr = "Auth state: " + authState.toString();

                    try {
                        String entryInfoStrs = "";
                        entryInfoStrs = JsonUtils.getEncoder().encode(authState);
                        WritableMap params = Arguments.createMap();
                        params.putString("authState", entryInfoStrs);
                        sendEvent(RCTRicohAuth.this.getReactApplicationContext(), "onAuthStateReceived", params);

                    } catch (EncodedException e) {
                        WritableMap params = Arguments.createMap();
                        params.putString("message", e.getLocalizedMessage());
                        sendEvent(RCTRicohAuth.this.getReactApplicationContext(), "onAuthStateReceivedError", params);
                    }
                }
            }).execute(this.getCurrentActivity().getApplicationContext());

            promise.resolve("getAuthState success!!");
        } catch (android.content.ActivityNotFoundException e) {

            promise.reject("getAuthState error!!");
        }
    }

    @ReactMethod
    public void searchUser(String searchString, Promise promise) {
        try {

            searchUser(searchString);
            promise.resolve("searchUser success!!");
        } catch (android.content.ActivityNotFoundException e) {

            promise.reject("searchUser error!!");
        }
    }

    private void searchUser(String searchString) {
        final SearchUserListAsyncTask searchUserListTask = new SearchUserListAsyncTask();
        searchUserListTask.execute(searchString);
    }

    class SearchUserListAsyncTask extends AsyncTask<String, Void, List<Entry>> {

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
            final List<Entry> entryList = new ArrayList<Entry>();

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
                final GetEntryListResponseBody nextResponse = logic.getContinuationEntryList(nextLink);
                addResultToEntryList(nextResponse, entryList);
                if (nextResponse == null) {
                    return null;
                }
                nextLink = nextResponse.getNextLink();
            }

            // 2
            final List<Entry> hitEntryList = new ArrayList<Entry>();

            for (final Iterator<Entry> iterator = entryList.iterator(); iterator.hasNext(); ) {
                final Entry simpleEntry = (Entry) iterator.next();

                final String name = simpleEntry.getName();
                if (name != null && name.contains(searchStr)) {
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
            for (final Iterator<Entry> iterator = entryArray.iterator(); iterator.hasNext(); ) {
                final Entry entry = iterator.next();

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
            if (hitEntryList == null) {
                final Toast toast = Toast.makeText(RCTRicohAuth.this.getReactApplicationContext(),
                        R.string.err_msg_get_entry_list, Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            // 1-2
            final int hitCount = hitEntryList.size();
            if (hitCount > 5) {
                final TextView messageView = new TextView(RCTRicohAuth.this.getReactApplicationContext());
                messageView.setTextSize(TypedValue.COMPLEX_UNIT_PX, USER_BUTTON_TEXT_SIZE_PIXELS);
                final String message = String.format(
                        RCTRicohAuth.this.getCurrentActivity().getString(R.string.err_msg_user_name_search_over),
                        hitCount);
                final Toast toast = Toast.makeText(RCTRicohAuth.this.getReactApplicationContext(), message,
                        Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            for (final Iterator<Entry> iterator = hitEntryList.iterator(); iterator.hasNext(); ) {
                // 2
                final Entry simpleEntry = iterator.next();
                final String entryId = simpleEntry.getEntryId();
                final Integer registrationNumber = simpleEntry.getRegistrationNumber();
                final String name = simpleEntry.getName();
                final String _loginUserId = simpleEntry.getUserCodeData().getLoginUserName();
                if (_loginUserId.equals(loginUserId)) {
                    try {
                        JSONObject entryJson = new JSONObject();
                        entryJson.put("entryId", simpleEntry.getEntryId());
                        entryJson.put("registrationNumber", simpleEntry.getRegistrationNumber());
                        entryJson.put("name", simpleEntry.getName());
                        entryJson.put("mailAddress", simpleEntry.getMailData().getMailAddress());
                        entryJson.put("loginUserName", simpleEntry.getUserCodeData().getLoginUserName());

                        String entryInfoStrs = entryJson.toString();

                        //                        String entryInfoStrs = "{\"entryId\":\""
                        //                                + simpleEntry.getEntryId()
                        //                                + "\",\"registrationNumber\":\""
                        //                                + simpleEntry.getRegistrationNumber()
                        //                                + "\",\"name\":\""
                        //                                + simpleEntry.getName()
                        //                                + "\",\"mailAddress\":\"\",\"loginUserName\":\""
                        //                                + simpleEntry.getUserCodeData().getLoginUserName()
                        //                                + "\"}";
                        Log.d(Const.TAG,
                                ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                        Log.d(Const.TAG, "Entry JSON string: " + entryInfoStrs);

                        WritableMap params = Arguments.createMap();
                        params.putString("entryInfo", entryInfoStrs);
                        sendEvent(RCTRicohAuth.this.getReactApplicationContext(), "onEntryInfoReceived", params);

                        final Toast toast = Toast.makeText(RCTRicohAuth.this.getReactApplicationContext(),
                                entryInfoStrs, Toast.LENGTH_LONG);
                        toast.show();
                    } catch (JSONException e) {
                        WritableMap params = Arguments.createMap();
                        params.putString("errorMessage", e.getMessage());
                        sendEvent(RCTRicohAuth.this.getReactApplicationContext(), "onError", params);
                    }
                }
            }
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