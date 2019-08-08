package com.doodle.Profile.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doodle.Profile.adapter.StarAdapter;
import com.doodle.Profile.model.Star;
import com.doodle.Profile.service.ProfileService;
import com.doodle.R;
import com.doodle.utils.PrefManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StarFragment extends Fragment {

    View view;
    TextView tvUserName;
    RecyclerView recyclerView;

    ProgressDialog progressDialog;

    PrefManager manager;
    ProfileService profileService;
    String deviceId, profileName, token, userIds;
    ArrayList<Star> arrayList;
    StarAdapter starAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.star_fragment_layout, container, false);

        initialComponent();
        getData();

        return view;
    }

    private void initialComponent() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();

        profileService = ProfileService.mRetrofit.create(ProfileService.class);
        manager = new PrefManager(getContext());
        deviceId = manager.getDeviceId();
        profileName = manager.getUserName();
        token = manager.getToken();
        userIds = manager.getProfileId();
        arrayList = new ArrayList<>();
        starAdapter = new StarAdapter(getActivity(), arrayList);

        tvUserName = view.findViewById(R.id.user_name);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(starAdapter);
    }

    private void getData() {
        Call<ArrayList<Star>> call = profileService.getStarList(deviceId, token, userIds, profileName);
        getStarList(call);
    }

    private void getStarList(Call<ArrayList<Star>> call) {
        call.enqueue(new Callback<ArrayList<Star>>() {
            @Override
            public void onResponse(Call<ArrayList<Star>> call, Response<ArrayList<Star>> response) {
                if (response.body() != null) {
                    arrayList.addAll(response.body());
                    starAdapter.notifyDataSetChanged();
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<ArrayList<Star>> call, Throwable t) {
                progressDialog.hide();
            }
        });

    }

}
