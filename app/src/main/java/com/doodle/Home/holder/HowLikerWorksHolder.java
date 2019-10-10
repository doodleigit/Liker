package com.doodle.Home.holder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doodle.Authentication.model.UserInfo;
import com.doodle.Home.model.PostItem;
import com.doodle.Home.service.HomeService;
import com.doodle.R;
import com.doodle.Setting.view.SettingActivity;
import com.doodle.Tool.AppConstants;
import com.doodle.Tool.PrefManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HowLikerWorksHolder extends RecyclerView.ViewHolder {

    private Context mContext;
    private TextView tvClickHere;
    private ImageView ivClose;
    private RelativeLayout howLikerWorks;

    private HomeService webService;
    private PrefManager manager;
    private String deviceId, profileId, token, userIds;

    public HowLikerWorksHolder(@NonNull View itemView, Context context) {
        super(itemView);

        mContext = context;
        tvClickHere = itemView.findViewById(R.id.click_here);
        ivClose = itemView.findViewById(R.id.close);
        howLikerWorks = itemView.findViewById(R.id.how_liker_works);

        webService = HomeService.mRetrofit.create(HomeService.class);
        manager = new PrefManager(mContext);
        deviceId = manager.getDeviceId();
        profileId = manager.getProfileId();
        token = manager.getToken();
        userIds = manager.getProfileId();
    }

    public void setItem(PostItem item, int position) {

        tvClickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                howLikerWorks();
            }
        });

        howLikerWorks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                howLikerWorks();
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setHowLikerWork();
            }
        });

    }

    private void howLikerWorks() {
        Intent termsIntent = new Intent(mContext, SettingActivity.class);
        termsIntent.putExtra("type", mContext.getString(R.string.how_liker_works));
        termsIntent.putExtra("link", mContext.getString(R.string.how_liker_works_link));
        mContext.startActivity(termsIntent);
    }

    private void setHowLikerWork() {
        Call<String> call = webService.setHowLikerWork(deviceId, token, profileId, userIds);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            boolean status = object.getBoolean("status");
                            if (status) {
                                Gson gson = new Gson();
                                String json = manager.getUserInfo();
                                UserInfo userInfo = gson.fromJson(json, UserInfo.class);
                                userInfo.setLearnAboutSite("1");
                                String jsonInfo = gson.toJson(userInfo);
                                manager.setUserInfo(jsonInfo);
                                mContext.sendBroadcast((new Intent().putExtra("type", "0")).setAction(AppConstants.COMMON_CHANGE_BROADCAST));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("onSuccess", response.body().toString());
                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

}
