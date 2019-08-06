package com.doodle.Profile.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doodle.Profile.adapter.FriendsAdapter;
import com.doodle.Profile.model.AllFriend;
import com.doodle.Profile.model.Friend;
import com.doodle.Profile.service.ProfileService;
import com.doodle.R;
import com.doodle.utils.PrefManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsFragment extends Fragment {

    View view;
    private RecyclerView recyclerView;

    private ProfileService profileService;
    private PrefManager manager;
    private FriendsAdapter friendsAdapter;
    private ArrayList<Friend> friends;
    private String deviceId, profileId, token, userIds;
    int limit = 10;
    int offset = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.friends_fragment_layout, container, false);

        initialComponent();
        getData();

        return view;
    }

    private void initialComponent() {
        profileService = ProfileService.mRetrofit.create(ProfileService.class);
        manager = new PrefManager(getContext());
        friends = new ArrayList<>();
        deviceId = manager.getDeviceId();
        profileId = manager.getProfileId();
        token = manager.getToken();
        userIds = manager.getProfileId();
        friendsAdapter = new FriendsAdapter(getActivity(), friends);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.setAdapter(friendsAdapter);
    }

    private void getData() {
        Call<AllFriend> call = profileService.getAllFriends(deviceId, token, userIds, profileId, userIds, limit, offset);
        sendFriendListRequest(call);
    }

    private void sendFriendListRequest(Call<AllFriend> call) {

        call.enqueue(new Callback<AllFriend>() {

            @Override
            public void onResponse(Call<AllFriend> call, Response<AllFriend> response) {

                AllFriend allFriend = response.body();
                if (allFriend != null) {
                    friends.addAll(allFriend.getFriends());
                    friendsAdapter.notifyDataSetChanged();
                    offset += 10;
                }
//                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<AllFriend> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
//                progressDialog.hide();
            }
        });

    }

}
