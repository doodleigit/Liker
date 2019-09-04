package com.doodle.Authentication.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.doodle.Authentication.model.Country;
import com.doodle.Authentication.model.CountryInfo;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;


public class MyService extends IntentService {

    public static final String TAG = "MyService";
    public static final String MY_SERVICE_MESSAGE = "myServiceMessage";
    public static final String MY_SERVICE_MESSAGE_COUNTRY = "WmyServiceMessage";
    public static final String MY_SERVICE_PAYLOAD = "myServicePayload";
    public static final String MY_SERVICE_PAYLOAD_COUNTRY = "XZCmyServicePayload";

    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

//        AuthService webService =
//                AuthService.retrofitForCountry.create(AuthService.class);
//
//        Call<Country> call = webService.getCountry();
//        ArrayList<CountryInfo> countryInfos = null;
//        Country country;
//        try {
//            country = call.execute().body();
//            Log.d("Country", country.toString());
//            countryInfos = (ArrayList<CountryInfo>) country.getCountry();
//            Log.i(TAG, "onHandleIntent: " + countryInfos);
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.i(TAG, "onHandleIntent: " + e.getMessage());
//        }
//
////        Return the data to MainActivity
//        Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
//        messageIntent.putExtra(MY_SERVICE_PAYLOAD, countryInfos);
//        LocalBroadcastManager manager1 =
//                LocalBroadcastManager.getInstance(getApplicationContext());
//        manager1.sendBroadcast(messageIntent);

    }
}

