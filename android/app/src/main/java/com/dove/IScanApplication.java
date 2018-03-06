package com.dove;

import com.dove.sample.app.scan.application.DestinationSettingDataHolder;
import com.dove.sample.app.scan.application.ScanSettingDataHolder;
import com.dove.sample.app.scan.application.StorageSettingDataHolder;
import com.dove.sample.function.scan.ScanJob;

public interface IScanApplication {
    public abstract int getTimeOfWaitingNextOriginal();
    public abstract void setAppState(String activityName, int state, String errMessage, int appType);
    public abstract void initJobSetting();
//    public abstract void setmIsWinShowing(boolean b);
    public abstract boolean lockPowerMode();
    public abstract boolean lockOffline();
    public abstract boolean unlockPowerMode();
    public abstract boolean unlockOffline();
    public abstract ScanSettingDataHolder getScanSettingDataHolder();
    public abstract DestinationSettingDataHolder getDestinationSettingDataHolder();
    public abstract StorageSettingDataHolder getStorageSettingDataHolder();
    public abstract ScanJob getScanJob();

}