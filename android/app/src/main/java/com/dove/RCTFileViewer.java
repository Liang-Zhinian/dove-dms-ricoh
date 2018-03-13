package com.dove;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.webkit.MimeTypeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.Map;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/12/8.
 */

public class RCTFileViewer extends ReactContextBaseJavaModule {

    public RCTFileViewer(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "RCTFileViewer";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        return constants;
    }

    @ReactMethod
    public void open(String path, Promise promise) throws JSONException {
        File file = new File(path);

        if (file.exists()) {
            try {
                String extension = MimeTypeMap.getFileExtensionFromUrl(path);
                String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

                Uri uri = FileProvider.getUriForFile(getReactApplicationContext(), getReactApplicationContext().getPackageName() + ".FileProvider", file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, mimeType);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                getReactApplicationContext().startActivity(intent);

                promise.resolve("Open success!!");
            } catch (android.content.ActivityNotFoundException e) {
                
                promise.reject("Open error!!");
            }
        } else {
            promise.reject("File not found");
        }
    }
}