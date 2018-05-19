package com.dove;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

import com.dove.sample.app.scan.application.DestinationSettingDataHolder;
import com.dove.sample.app.scan.application.ScanSettingDataHolder;
import com.dove.sample.app.scan.application.StorageSettingDataHolder;
import com.dove.sample.function.scan.ScanJob;

public interface IScanApplication {
    ScanJob getScanJob();
}