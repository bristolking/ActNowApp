package com.actnow.android.sdk;

import com.actnow.android.sdk.api.ANApi;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AN {
    private final static String ENDPOINT = "http://actnow.cancri.biz/";
    private final ANApi mANApi;

    public AN() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mANApi = retrofit.create(ANApi.class);
    }
    public ANApi getANApi() {
        return mANApi;
    }
}
