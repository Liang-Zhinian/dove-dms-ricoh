package com.dove;

import android.app.Activity;
import android.content.Intent;

import com.dove.sample.app.scan.activity.TopActivity;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/8.
 */

public class RCTSimpleIntentModule extends ReactContextBaseJavaModule {

    public RCTSimpleIntentModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "SimpleIntentModule";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        return constants;
    }

    @ReactMethod
    public void start(Promise promise) throws JSONException {

        try {
            Activity currentActivity = getCurrentActivity();
            if (null != currentActivity) {
                Intent intent = new Intent(currentActivity, SimpleActivity.class); //new Intent(currentActivity, TopActivity.class);
                currentActivity.startActivity(intent);
            }

            promise.resolve("Start activity success!!");
        } catch (android.content.ActivityNotFoundException e) {

            promise.reject("Start activity error!!");
        }
    }
}