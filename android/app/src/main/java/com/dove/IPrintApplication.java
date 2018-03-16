package com.dove;

import android.content.Intent;
import android.content.res.Resources;

import com.dove.sample.app.scan.application.DestinationSettingDataHolder;
import com.dove.sample.app.scan.application.ScanSettingDataHolder;
import com.dove.sample.app.scan.application.StorageSettingDataHolder;
import com.dove.sample.function.print.PrintJob;
import com.dove.sample.function.scan.ScanJob;

public interface IPrintApplication {
    public abstract int getTimeOfWaitingNextOriginal();
    public abstract void setAppState(String activityName, int state, String errMessage, int appType);
    public abstract void initJobSetting();
    public abstract void setmIsWinShowing(boolean b);
    public abstract boolean lockPowerMode();
    public abstract boolean lockOffline();
    public abstract boolean unlockPowerMode();
    public abstract boolean unlockOffline();
    public abstract PrintJob getPrintJob();
    public abstract String getPackageName();
    public abstract void sendBroadcast(Intent intent);
    public abstract SimplePrintStateMachine getStateMachine();
    public abstract Resources getResources();

}