package com.dove;

import android.app.Activity;
import android.os.Bundle;

public class SendCrashActivity extends Activity {

    private final static String PREFIX = "activity:SendCrashAct:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_crash);

    }
}
