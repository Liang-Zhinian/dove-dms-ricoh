package com.dove;

import android.app.Application;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/12/8.
 */

public class RCTRicohScannerReactPackage implements ReactPackage {
    private Application mApplication;

    public RCTRicohScannerReactPackage(Application app){
        mApplication = app;
    }

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        //Registering the module.
        return Arrays.<NativeModule>asList(new RCTRicohScannerModule(reactContext, mApplication));
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }
}