package com.doodle.Setting.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.doodle.R;
import com.doodle.Setting.model.UserInfo;
import com.doodle.Setting.service.SettingService;
import com.doodle.Tool.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewNotificationSettingFragment extends Fragment {

    View view;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private Switch emailNotificationSwitch, pushNotificationSwitch;
    private SettingService settingService;

    private PrefManager manager;
    private String deviceId, token, userId;
    private boolean isFirstTime = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_notification_fragment_layout, container, false);

        initialComponent();
        sendGetUserInfoRequest();

        return view;
    }

    private void initialComponent() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);

        settingService = SettingService.mRetrofit.create(SettingService.class);

        emailNotificationSwitch = view.findViewById(R.id.email_notification_switch);
        pushNotificationSwitch = view.findViewById(R.id.push_notification_switch);

        manager = new PrefManager(getContext());
        deviceId = manager.getDeviceId();
        token = manager.getToken();
        userId = manager.getProfileId();

        toolbar = view.findViewById(R.id.toolbar);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        emailNotificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (!isFirstTime) {
                    sendNotificationOnOffRequest("email", checked, emailNotificationSwitch);
                }
            }
        });

        pushNotificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (!isFirstTime) {
                    sendNotificationOnOffRequest("push", checked, pushNotificationSwitch);
                }
            }
        });

    }

    private void sendGetUserInfoRequest() {
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        Call<UserInfo> call = settingService.getUserInfo(deviceId, token, userId, userId);
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                UserInfo userInfo = response.body();
                if (userInfo != null) {
                    emailNotificationSwitch.setChecked(userInfo.getEmailNotification().equals("1"));
                    pushNotificationSwitch.setChecked(userInfo.getPushNotification().equals("1"));
                } else {
                    emailNotificationSwitch.setChecked(true);
                    pushNotificationSwitch.setChecked(true);
                }
                isFirstTime = false;
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                emailNotificationSwitch.setChecked(true);
                pushNotificationSwitch.setChecked(true);
                isFirstTime = false;
                progressDialog.dismiss();
            }
        });
    }

    private void sendNotificationOnOffRequest(String type, boolean notificationStatus, Switch notificationSwitch) {
        progressDialog.setMessage(getString(R.string.updating));
        progressDialog.show();
        Call<String> call = settingService.setNotificationOnOff(deviceId, userId, token, userId, type, notificationStatus);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean status = obj.getBoolean("status");
                    if (status) {

                    } else {
                        notificationSwitch.setChecked(!notificationStatus);
                        Toast.makeText(getContext(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                notificationSwitch.setChecked(!notificationStatus);
                Toast.makeText(getContext(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
