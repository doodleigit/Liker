package com.doodle.Setting.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.doodle.Profile.model.Star;
import com.doodle.R;
import com.doodle.Setting.adapter.BlockUserAdapter;
import com.doodle.Setting.adapter.CustomPrivacySpinnerAdapter;
import com.doodle.Setting.adapter.UserForBlockAdapter;
import com.doodle.Setting.model.BlockUser;
import com.doodle.Setting.model.Friend;
import com.doodle.Setting.model.Permissions;
import com.doodle.Setting.model.PrivacyInfo;
import com.doodle.Setting.model.PrivacyType;
import com.doodle.Setting.service.SettingService;
import com.doodle.Setting.service.UserBlockClickListener;
import com.doodle.utils.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrivacyAndSecurityFragment extends Fragment {

    View view;
    private Toolbar toolbar;
    private FrameLayout friendRequestPrivacyLayout, postOnWallPrivacyLayout, friendListPrivacyLayout, photosPrivacyLayout, messagePrivacyLayout;
    private LinearLayout blockChangeLayout, findUserLayout;
    private Spinner friendRequestSpinner, postOnWallSpinner, friendListSpinner, photosSpinner, messageSpinner;
    private TextView tvFindUser;
    private EditText etFindUserEmail;
    private ImageView ivFriendRequestPrivacyIcon, ivPostOnWallPrivacyIcon, ivFriendListPrivacyIcon, ivPhotosPrivacyIcon, ivMessagePrivacyIcon;
    private RecyclerView findUserRecyclerView, recyclerView;
    private Button btnBlockCancel;

    private ProgressDialog progressDialog;

    private BlockUserAdapter blockUserAdapter;
    private UserForBlockAdapter userForBlockAdapter;
    private SettingService settingService;
    private PrefManager manager;
    private PrivacyInfo privacyInfo;
    private ArrayList<PrivacyType> privacyTwoTypes, privacyThreeTypes;
    private ArrayList<BlockUser> blockUsers;
    private ArrayList<Friend> friends;
    private String deviceId, token, userId;
    private boolean isFirstTime = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.privacy_security_fragment_layout, container, false);

        initialComponent();
        spinnerItemSelectListener();
        sendPrivacyAndSecuritySettingRequest();

        return view;
    }

    private void initialComponent() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);

        settingService = SettingService.mRetrofit.create(SettingService.class);
        manager = new PrefManager(getContext());
        deviceId = manager.getDeviceId();
        token = manager.getToken();
        userId = manager.getProfileId();

        privacyInfo = new PrivacyInfo();
        privacyInfo.setBlockUsers(new ArrayList<>());
        privacyInfo.setPermissions(new Permissions());
        privacyTwoTypes = new ArrayList<>();
        privacyThreeTypes = new ArrayList<>();
        blockUsers = new ArrayList<>();
        friends = new ArrayList<>();

        privacyTwoTypes.add(new PrivacyType("Public", R.drawable.public_));
        privacyTwoTypes.add(new PrivacyType("Nobody", R.drawable.friends));

        privacyThreeTypes.add(new PrivacyType("Public", R.drawable.public_));
        privacyThreeTypes.add(new PrivacyType("Only Me", R.drawable.only_me));
        privacyThreeTypes.add(new PrivacyType("Friends", R.drawable.friends));

        toolbar = view.findViewById(R.id.toolbar);

        friendRequestPrivacyLayout = view.findViewById(R.id.friend_request_privacy_layout);
        postOnWallPrivacyLayout = view.findViewById(R.id.post_on_wall_privacy_layout);
        friendListPrivacyLayout = view.findViewById(R.id.friend_list_privacy_layout);
        photosPrivacyLayout = view.findViewById(R.id.photos_privacy_layout);
        messagePrivacyLayout = view.findViewById(R.id.message_privacy_layout);

        blockChangeLayout = view.findViewById(R.id.block_change_layout);
        findUserLayout = view.findViewById(R.id.find_user_layout);

        friendRequestSpinner = view.findViewById(R.id.friend_request_spinner);
        postOnWallSpinner = view.findViewById(R.id.post_on_wall_spinner);
        friendListSpinner = view.findViewById(R.id.friend_list_spinner);
        photosSpinner = view.findViewById(R.id.photos_spinner);
        messageSpinner = view.findViewById(R.id.message_spinner);

        tvFindUser = view.findViewById(R.id.find_user);

        etFindUserEmail = view.findViewById(R.id.find_user_email);

        ivFriendRequestPrivacyIcon = view.findViewById(R.id.friend_request_privacy_icon);
        ivPostOnWallPrivacyIcon = view.findViewById(R.id.post_on_wall_privacy_icon);
        ivFriendListPrivacyIcon = view.findViewById(R.id.friend_list_privacy_icon);
        ivPhotosPrivacyIcon = view.findViewById(R.id.photos_privacy_icon);
        ivMessagePrivacyIcon = view.findViewById(R.id.message_privacy_icon);

        findUserRecyclerView = view.findViewById(R.id.find_user_recycler_view);
        findUserRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        btnBlockCancel = view.findViewById(R.id.block_cancel);

        UserBlockClickListener userBlockClickListener = new UserBlockClickListener() {
            @Override
            public void onBlockClick(Friend friend, int position) {
                setBlockedUserRequest(friend, position);
            }

            @Override
            public void onUnBlockClick(BlockUser blockUser, int position) {
                setUnBlockedUserRequest(blockUser.getUserId(), position);
            }
        };

        blockUserAdapter = new BlockUserAdapter(getActivity(), blockUsers, userBlockClickListener);
        userForBlockAdapter = new UserForBlockAdapter(getActivity(), friends, userBlockClickListener);

        recyclerView.setAdapter(blockUserAdapter);
        findUserRecyclerView.setAdapter(userForBlockAdapter);

        CustomPrivacySpinnerAdapter customTwoPrivacySpinnerAdapter = new CustomPrivacySpinnerAdapter(getContext(), privacyTwoTypes);
        friendRequestSpinner.setAdapter(customTwoPrivacySpinnerAdapter);
        CustomPrivacySpinnerAdapter customThreePrivacySpinnerAdapter = new CustomPrivacySpinnerAdapter(getContext(), privacyThreeTypes);
        postOnWallSpinner.setAdapter(customThreePrivacySpinnerAdapter);
        friendListSpinner.setAdapter(customThreePrivacySpinnerAdapter);
        photosSpinner.setAdapter(customThreePrivacySpinnerAdapter);
        messageSpinner.setAdapter(customThreePrivacySpinnerAdapter);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        friendRequestPrivacyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFirstTime = false;
                friendRequestSpinner.performClick();
            }
        });

        postOnWallPrivacyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFirstTime = false;
                postOnWallSpinner.performClick();
            }
        });

        friendListPrivacyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFirstTime = false;
                friendListSpinner.performClick();
            }
        });

        photosPrivacyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFirstTime = false;
                photosSpinner.performClick();
            }
        });

        messagePrivacyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFirstTime = false;
                messageSpinner.performClick();
            }
        });

        blockChangeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blockChangeLayout.setVisibility(View.GONE);
                findUserLayout.setVisibility(View.VISIBLE);
                btnBlockCancel.setVisibility(View.VISIBLE);
            }
        });

        btnBlockCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etFindUserEmail.setText("");
                friends.clear();
                userForBlockAdapter.notifyDataSetChanged();
                blockChangeLayout.setVisibility(View.VISIBLE);
                findUserLayout.setVisibility(View.GONE);
                btnBlockCancel.setVisibility(View.GONE);
            }
        });

        tvFindUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etFindUserEmail.getText().toString().length() >= 2) {
                    sendFindUserRequest(etFindUserEmail.getText().toString());
                } else {
                    etFindUserEmail.setError(getString(R.string.please_input));
                }
            }
        });

        etFindUserEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etFindUserEmail.getText().toString().isEmpty()) {
                    friends.clear();
                    userForBlockAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void setData() {
        friendRequestSpinner.setSelection(Integer.valueOf(privacyInfo.getPermissions().getFriendSendPermission()));
        postOnWallSpinner.setSelection(Integer.valueOf(privacyInfo.getPermissions().getWallPermission()));
        friendListSpinner.setSelection(Integer.valueOf(privacyInfo.getPermissions().getFriendSeePermission()));
        photosSpinner.setSelection(Integer.valueOf(privacyInfo.getPermissions().getPhotosSeePermission()));
        messageSpinner.setSelection(Integer.valueOf(privacyInfo.getPermissions().getMessageSendPermission()));

        ivFriendRequestPrivacyIcon.setImageResource(getTwoTypePrivacyDrawable(privacyInfo.getPermissions().getFriendSendPermission()));
        ivPostOnWallPrivacyIcon.setImageResource(getThreeTypePrivacyDrawable(privacyInfo.getPermissions().getWallPermission()));
        ivFriendListPrivacyIcon.setImageResource(getThreeTypePrivacyDrawable(privacyInfo.getPermissions().getFriendSeePermission()));
        ivPhotosPrivacyIcon.setImageResource(getThreeTypePrivacyDrawable(privacyInfo.getPermissions().getPhotosSeePermission()));
        ivMessagePrivacyIcon.setImageResource(getThreeTypePrivacyDrawable(privacyInfo.getPermissions().getMessageSendPermission()));

        blockUserAdapter.notifyDataSetChanged();
    }

    private void spinnerItemSelectListener() {
        friendRequestSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!isFirstTime)
                    sendPrivacyAndSecuritySettingRequest("friend_send_permission", String.valueOf(i), ivFriendRequestPrivacyIcon, getTwoTypePrivacyDrawable(String.valueOf(i)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        postOnWallSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!isFirstTime)
                    sendPrivacyAndSecuritySettingRequest("wall_permission", String.valueOf(i), ivPostOnWallPrivacyIcon, getThreeTypePrivacyDrawable(String.valueOf(i)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        friendListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!isFirstTime)
                    sendPrivacyAndSecuritySettingRequest("friend_see_permission", String.valueOf(i), ivFriendListPrivacyIcon, getThreeTypePrivacyDrawable(String.valueOf(i)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        photosSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!isFirstTime)
                    sendPrivacyAndSecuritySettingRequest("photos_see_permission", String.valueOf(i), ivPhotosPrivacyIcon, getThreeTypePrivacyDrawable(String.valueOf(i)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        messageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!isFirstTime)
                    sendPrivacyAndSecuritySettingRequest("message_send_permission", String.valueOf(i), ivMessagePrivacyIcon, getThreeTypePrivacyDrawable(String.valueOf(i)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private int getTwoTypePrivacyDrawable(String type) {
        if (type.equals("1")) {
            return R.drawable.friends;
        } else {
            return R.drawable.public_;
        }
    }

    private int getThreeTypePrivacyDrawable(String type) {
        if (type.equals("1")) {
            return R.drawable.only_me;
        } else if (type.equals("2")) {
            return R.drawable.friends;
        } else {
            return R.drawable.public_;
        }
    }

    private void sendPrivacyAndSecuritySettingRequest() {
        progressDialog.show();
        Call<PrivacyInfo> call = settingService.getPrivacyAndSecuritySetting(deviceId, userId, token, userId);
        call.enqueue(new Callback<PrivacyInfo>() {
            @Override
            public void onResponse(Call<PrivacyInfo> call, Response<PrivacyInfo> response) {
                privacyInfo = response.body();
                if (privacyInfo != null) {
                    blockUsers.addAll(privacyInfo.getBlockUsers());
                    setData();
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<PrivacyInfo> call, Throwable t) {
                progressDialog.hide();
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendPrivacyAndSecuritySettingRequest(String privacyComponentType, String privacyType, ImageView privacyIcon, int resource) {
        progressDialog.show();
        Call<String> call = settingService.setPrivacyUpdate(deviceId, userId, token, userId, privacyComponentType, privacyType);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean status = obj.getBoolean("status");
                    if (status) {
                        privacyIcon.setImageResource(resource);
                        Toast.makeText(getContext(), "Privacy Updated", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.hide();
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendFindUserRequest(String searchKey) {
        progressDialog.show();
        Call<ArrayList<Friend>> call = settingService.getSearchUser(deviceId, userId, token, searchKey);
        call.enqueue(new Callback<ArrayList<Friend>>() {
            @Override
            public void onResponse(Call<ArrayList<Friend>> call, Response<ArrayList<Friend>> response) {
                friends.clear();
                ArrayList<Friend> arrayList = response.body();
                if (arrayList != null) {
                    friends.addAll(arrayList);
                }
                userForBlockAdapter.notifyDataSetChanged();
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<ArrayList<Friend>> call, Throwable t) {
                progressDialog.hide();
            }
        });
    }

    private void setBlockedUserRequest(Friend friend, int position) {
        progressDialog.show();
        Call<String> call = settingService.setBlockedUser(deviceId, userId, token, userId, friend.getUserId());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                BlockUser blockUser = new BlockUser();
                blockUser.setUserId(friend.getUserId());
                blockUser.setFirstName(friend.getFirstName());
                blockUser.setLastName(friend.getLastName());
                blockUser.setUserName(friend.getUserName());
                blockUser.setTotalLikes(friend.getTotalLikes());
                blockUser.setGoldStars(friend.getGoldStars());
                blockUser.setSliverStars(friend.getSliverStars());
                blockUser.setPhoto(friend.getPhoto());

                blockUsers.add(blockUser);
                friends.remove(position);
                blockUserAdapter.notifyDataSetChanged();
                userForBlockAdapter.notifyDataSetChanged();
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.hide();
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUnBlockedUserRequest(String blockedId, int position) {
        progressDialog.show();
        Call<String> call = settingService.setUnBlockedUser(deviceId, userId, token, userId, blockedId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                blockUsers.remove(position);
                blockUserAdapter.notifyDataSetChanged();
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.hide();
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
