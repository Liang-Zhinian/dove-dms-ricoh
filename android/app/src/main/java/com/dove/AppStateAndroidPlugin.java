package com.dove;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

/**
 * Created by scgough on 27/01/2016.
 */
public class AppStateAndroidPlugin extends ReactContextBaseJavaModule implements Application.ActivityLifecycleCallbacks {

    private static final String PLUGIN_NAME = "AppStateAndroid";

    private ReactContext mReactContext;

    protected Activity activity = null;

    protected Activity getActivity(){
        return this.activity;
    }

    public AppStateAndroidPlugin(ReactApplicationContext reactContext) {
        super(reactContext);

        this.mReactContext = reactContext;
    }

    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           @Nullable WritableMap params) {

        Log.d(PLUGIN_NAME, "Sending Event"+params.toString());
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    @Override
    public void initialize() {
        this.activity = this.getCurrentActivity();
        this.activity.getApplication().registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.d(PLUGIN_NAME, "Resumed");
        if(mReactContext!=null) {
            WritableMap params = Arguments.createMap();
            params.putString("currentAppState", "active");
            sendEvent(mReactContext, "appStateChange", params);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.d(PLUGIN_NAME, "Paused");
        if(mReactContext!=null) {
            WritableMap params = Arguments.createMap();
            params.putString("currentAppState", "background");
            sendEvent(mReactContext, "appStateChange", params);
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.d(PLUGIN_NAME, "Stopped");
        if(mReactContext!=null) {
            WritableMap params = Arguments.createMap();
            params.putString("currentAppState", "stopped");
            sendEvent(mReactContext, "appStateChange", params);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.d(PLUGIN_NAME, "SaveInstanceState");
        if(mReactContext!=null) {
            WritableMap params = Arguments.createMap();
            params.putString("currentAppState", "saveInstanceState");
            sendEvent(mReactContext, "appStateChange", params);
        }

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.d(PLUGIN_NAME, "Destroyed");
        if(mReactContext!=null) {
            WritableMap params = Arguments.createMap();
            params.putString("currentAppState", "destroyed");
            sendEvent(mReactContext, "appStateChange", params);
        }

        Activity myActivity = this.getActivity();
        if (activity == myActivity){
            myActivity.getApplication().unregisterActivityLifecycleCallbacks(this);
        }
    }

    @Override
    public String getName() {
        return PLUGIN_NAME;
    }
}