/*
 *  Copyright (C) 2015-2016 RICOH Co.,LTD.
 *  All rights reserved.
 */
package com.dove.sample.app.scan.application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.dove.sample.app.scan.Const;

public class AfterPowerModeLock {


    private static final String APP_EVENT_PERMISSION = "jp.co.ricoh.isdk.sdkservice.common.SdkService.APP_EVENT_PERMISSION";
    private static final String ACTION_POWER_MODE_RESULT = "jp.co.ricoh.isdk.sdkservice.system.PowerMode.POWER_MODE_RESULT";
    private static final String EXTRA_KEY_POWER_MODE = "POWER_MODE";

    private ScanSampleApplication mApplication;

    private volatile boolean isStarted = false;
    private volatile boolean isFinished = false;
    private volatile boolean isLocked = false;
    
    /**
     * Define the prefix for log information with abbreviation package and class name
     */
    private final static String PREFIX = "application:PowerModeLock:";
    
    private BroadcastReceiver mPowerModeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_POWER_MODE_RESULT.equals(action)) {
                if (intent.hasExtra(EXTRA_KEY_POWER_MODE)) {
                    int powerMode = intent.getIntExtra(EXTRA_KEY_POWER_MODE, -1);
                    Log.d(Const.TAG, PREFIX + "POWER_MODE_RESULT received. mode=" + powerMode);

                    if ((powerMode >= 0) && (powerMode <= 3)) {
                        //0: Normal standby state
                        //1: Low-power state
                        //2: Fusing unit off state
                        //3: Silent state
                        startLockThread();
                    }
                }
            }
        }
    };

    private PowerModeLockThread mLockThread = null;

    public AfterPowerModeLock(ScanSampleApplication application) {
        mApplication = application;
    }

    public boolean start() {
        if (isStarted) {
            return false;
        }

        Log.i(Const.TAG, PREFIX + "start");
        registerReceiver();
        isStarted = true;
        return true;
    }

    public boolean finish() {
        if (!isStarted) {
            return false;
        }
        if (isFinished) {
            return true;
        }
        isFinished = true;

        Log.i(Const.TAG, PREFIX + "finish");
        unregisterReceiver();
        mLockThread = null;
        return true;
    }

    public boolean locked() {
        return isLocked;
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_POWER_MODE_RESULT);
        mApplication.registerReceiver(mPowerModeReceiver, filter, APP_EVENT_PERMISSION, null);
    }

    private void unregisterReceiver() {
        mApplication.unregisterReceiver(mPowerModeReceiver);
    }

    private void startLockThread() {
        if (isFinished) {
            return;
        }
        if (mLockThread != null) {
            return;
        }

        mLockThread = new PowerModeLockThread();
        mLockThread.start();
    }

    private class PowerModeLockThread extends Thread {

        @Override
        public void run() {
            Log.i(Const.TAG, PREFIX + "PowerModeLockThread start");
            while (!isFinished) {
                boolean result = mApplication.lockPowerMode();
                if (result) {
                    isLocked = true;
                    Log.i(Const.TAG, PREFIX + "Locked");
                    break;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.i(Const.TAG, PREFIX + "interrupted");
                    break;
                }
            }
            Log.i(Const.TAG, PREFIX + "PowerModeLockThread finish");
        }

    }

}
