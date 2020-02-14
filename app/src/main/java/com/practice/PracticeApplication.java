package com.practice;

import android.app.Application;

import com.easyvolley.NetworkClient;

/**
 * Application instance. We are using this for global settings.
 */
public class PracticeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialise the networking client
        NetworkClient.init(this);
    }

}
