package com.doodle.Setting.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.doodle.R;
import com.doodle.Setting.adapter.NotificationOnOffAdapter;
import com.doodle.Setting.model.PrivacyOnOff;
import com.doodle.Setting.service.SettingService;
import com.doodle.utils.PrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationSettingFragment extends Fragment {

    View view;
    private Toolbar toolbar;
    private LinearLayout changeAllLayout, editLayout, saveAllButtonLayout, cancelLayout;
    private RecyclerView recyclerView;

    private ProgressDialog progressDialog;
    private NotificationOnOffAdapter notificationOnOffAdapter;
    private SettingService settingService;
    private PrefManager manager;
    private PrivacyOnOff privacyOnOff;
    private String deviceId, token, userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.notification_setting_fragment_layout, container, false);

        initialComponent();
        sendPrivacyOnOffInfoRequest();

        return view;
    }

    private void initialComponent() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Updating...");
        progressDialog.setCancelable(false);

        settingService = SettingService.mRetrofit.create(SettingService.class);
        manager = new PrefManager(getContext());
        privacyOnOff = new PrivacyOnOff();
        deviceId = manager.getDeviceId();
        token = manager.getToken();
        userId = manager.getProfileId();

        toolbar = view.findViewById(R.id.toolbar);

        changeAllLayout = view.findViewById(R.id.change_all_layout);
        editLayout = view.findViewById(R.id.edit_layout);
        saveAllButtonLayout = view.findViewById(R.id.save_all_button_layout);
        cancelLayout = view.findViewById(R.id.cancel_layout);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        notificationOnOffAdapter = new NotificationOnOffAdapter(getActivity(), privacyOnOff.getStatuses(), progressDialog, settingService, deviceId, token, userId, false);
        recyclerView.setAdapter(notificationOnOffAdapter);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

//        changeAllLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                changeAllLayout.setVisibility(View.GONE);
//                editLayout.setVisibility(View.VISIBLE);
//            }
//        });
//
//        cancelLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                changeAllLayout.setVisibility(View.VISIBLE);
//                editLayout.setVisibility(View.GONE);
//            }
//        });
    }

    private void sendPrivacyOnOffInfoRequest() {
        Call<PrivacyOnOff> call = settingService.getPrivacyOnOff(deviceId, userId, token, userId);
        call.enqueue(new Callback<PrivacyOnOff>() {
            @Override
            public void onResponse(Call<PrivacyOnOff> call, Response<PrivacyOnOff> response) {
                privacyOnOff = response.body();
                if (privacyOnOff != null) {
                    notificationOnOffAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<PrivacyOnOff> call, Throwable t) {

            }
        });

    }

}
