package com.actnow.android;

import android.content.Context;
import androidx.multidex.MultiDex;

import com.activeandroid.ActiveAndroid;
import com.actnow.android.sdk.AN;
import com.actnow.android.sdk.api.ANApi;


public class ANApplications extends android.app.Application {
    private static AN mAN;
    private static ANApi mANApi;
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
        mAN = new AN();
        mANApi = mAN.getANApi();
    }
    public static ANApi getANApi() {
        return mANApi;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
