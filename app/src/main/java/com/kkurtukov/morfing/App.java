package com.kkurtukov.morfing;

import android.app.Application;
import android.content.Context;

import com.kkurtukov.morfing.utilities.debug.LLog;

/**
 * Created by kkurtukov on 03.09.2016.
 */
public class App extends Application {
    private static final String TAG = App.class.getSimpleName();

    /*************************************
     * PRIVATE STATIC FIELDS
     *************************************/
    private static App sInstance;

    /*************************************
     * PUBLIC METHODS
     *************************************/
    @Override
    public void onCreate() {
        super.onCreate();
        LLog.e(TAG, "onCreate");
        sInstance = this;
    }

    /*************************************
     * PUBLIC STATIC METHODS
     *************************************/
    public static Context getContext() {
        LLog.e(TAG, "getContext");
        return sInstance;
    }
}
