package com.dove;

import android.content.Intent;
import android.os.Bundle;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactInstanceManager;

public class MainActivity extends ReactActivity {
    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "dove";
    }

    private String s;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // test crash handler
//        System.out.println(s.equals("any string"));

//        this.getReactInstanceManager()
//        ReactInstanceManager.builder().addPackage()
//        this.getReactNativeHost().getReactInstanceManager().get
    }


}
