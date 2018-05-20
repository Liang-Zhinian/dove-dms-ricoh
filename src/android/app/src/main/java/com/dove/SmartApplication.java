package com.dove;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.widget.Toast;

import com.dove.sample.function.common.SmartSDKApplication;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public abstract class SmartApplication extends ReactContextBaseJavaModule {
    protected SmartSDKApplication mApplication;
    protected int timeOfWaitingNextOriginal = 0;
    protected boolean mIsWinShowing = false;
    private boolean mIsForeground = false;
    private boolean mRequestedSystemResetLock = false;

    public SmartApplication(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "RCTScannerModule";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        return constants;
    }

    protected void sendEvent(ReactContext reactContext,
                           String eventName,
                           @Nullable WritableMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    public boolean lockPowerMode() {
        return mApplication.lockPowerMode();
    }

    public boolean lockOffline() {
        return mApplication.lockOffline();
    }

    public boolean unlockPowerMode() {
        return mApplication.unlockPowerMode();
    }

    public boolean unlockOffline() {
        return mApplication.unlockOffline();
    }

    public void setAppState(String activityName, int state, String errMessage, int appType) {
        mApplication.setAppState(activityName, state, errMessage, appType);
    }

    public void unregisterReceiver(BroadcastReceiver mPowerModeReceiver) {
        mApplication.unregisterReceiver(mPowerModeReceiver);
    }

    public void registerReceiver(BroadcastReceiver mPowerModeReceiver, IntentFilter filter, String appEventPermission, Object o) {
        mApplication.registerReceiver(mPowerModeReceiver, filter, appEventPermission, (Handler)o);
    }

    public String getPackageName() {
        return mApplication.getPackageName();
    }

    public void sendBroadcast(Intent intent) {
        mApplication.sendBroadcast(intent);
    }

    public int getTimeOfWaitingNextOriginal() {
        return timeOfWaitingNextOriginal;
    }

    public boolean ismIsWinShowing() {
        return mIsWinShowing;
    }
    public void setmIsWinShowing(boolean mIsWinShowing) {
        this.mIsWinShowing = mIsWinShowing;
    }

    protected void processSystemResetLock() {
        if (mIsForeground && mRequestedSystemResetLock) {
            mApplication.lockSystemReset();
        }
    }

    protected void processSystemResetUnlock() {
        mApplication.unlockSystemReset();
    }
}
