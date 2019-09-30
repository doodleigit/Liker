package com.doodle.Message.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.doodle.Message.adapter.SuggestedListAdapter;
import com.doodle.Message.model.AllFriends;
import com.doodle.Message.model.Friend;
import com.doodle.Message.model.FriendInfo;
import com.doodle.Message.service.MessageService;
import com.doodle.Message.service.SuggestedListClickResponseService;
import com.doodle.R;
import com.doodle.Tool.NetworkHelper;
import com.doodle.Tool.PrefManager;
import com.doodle.Tool.Tools;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewMessageFragment extends Fragment {

    private View view;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private SearchView svToUser;
    private LinearLayoutManager layoutManager;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;

    private PrefManager manager;
    private boolean networkOk;
    private SuggestedListClickResponseService suggestedListClickResponseService;
    private MessageService messageService;
    private SuggestedListAdapter suggestedListAdapter;
    private ArrayList<Friend> friends;
    private String deviceId, profileId, token, userIds;
    int limit = 10;
    int offset = 0;
    private boolean isScrolling;
    private int totalItems;
    private int scrollOutItems;
    private int currentItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_message_fragment_layout, container, false);

        initialComponent();

        return view;
    }

    private void initialComponent() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.loading));

        manager = new PrefManager(getContext());
        deviceId = manager.getDeviceId();
        profileId = manager.getProfileId();
        token = manager.getToken();
        userIds = manager.getProfileId();
        networkOk = NetworkHelper.hasNetworkAccess(getContext());
        messageService = MessageService.mRetrofit.create(MessageService.class);
        friends = new ArrayList<>();

        layoutManager = new LinearLayoutManager(getContext());
        toolbar = view.findViewById(R.id.toolbar);
        svToUser = view.findViewById(R.id.to_user);
        svToUser.setIconifiedByDefault(true);
        svToUser.setFocusable(true);
        svToUser.setIconified(false);
        progressBar = view.findViewById(R.id.progress_bar);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);

        getData();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                getActivity().onBackPressed();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = layoutManager.getChildCount();
                scrollOutItems = layoutManager.findFirstVisibleItemPosition();
                totalItems = layoutManager.getItemCount();

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;
                    getPagination();
                }
            }
        });

        svToUser.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    Call<AllFriends> call = messageService.getAllFriends(deviceId, token, userIds, profileId, userIds, limit, offset);
                    sendSuggestedListRequest(call);
                } else if (newText.length() >= 2) {
                    getSuggestedList(newText);
                }
                return false;
            }
        });

        suggestedListClickResponseService = new SuggestedListClickResponseService() {
            @Override
            public void onListItemClick(Friend friend) {
                FriendInfo friendInfo = new FriendInfo(friend.getUserName(), friend.getUserId(), (friend.getFirstName() + " " + friend.getLastName()),
                        friend.getTotalLikes(), friend.getGoldStars());
                initiateFragment(friendInfo);
            }
        };

    }

    private void getData() {
        if (NetworkHelper.hasNetworkAccess(getContext())) {
            Call<AllFriends> call = messageService.getAllFriends(deviceId, token, userIds, profileId, userIds, limit, offset);
            sendSuggestedListRequest(call);
            progressDialog.show();
        } else {
            Tools.showNetworkDialog(getChildFragmentManager());
        }
    }

    private void initiateFragment(FriendInfo friendInfo) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("friend_info", friendInfo);
        MessagingFragment messagingFragment = new MessagingFragment();
        messagingFragment.setArguments(bundle);
        android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
        transaction.addToBackStack(null);
        transaction.add(R.id.container, messagingFragment).commit();
    }

    private void getPagination() {
        if (NetworkHelper.hasNetworkAccess(getContext())) {
            progressBar.setVisibility(View.VISIBLE);
            Call<AllFriends> call = messageService.getAllFriends(deviceId, token, userIds, profileId, userIds, limit, offset);
            sendSuggestedListPaginationRequest(call);
        }
    }

    private void getSuggestedList(String keyword) {
        Call<ArrayList<Friend>> call = messageService.getSearchUser(deviceId, profileId, token, keyword);
        sendSuggestedSearchListRequest(call);
    }

    private void sendSuggestedListRequest(Call<AllFriends> call) {

        call.enqueue(new Callback<AllFriends>() {

            @Override
            public void onResponse(Call<AllFriends> call, Response<AllFriends> response) {

                AllFriends allFriends = response.body();
                if (allFriends != null) {
                    friends = allFriends.getFriends();
                    suggestedListAdapter = new SuggestedListAdapter(getActivity(), friends, suggestedListClickResponseService);
                    recyclerView.setAdapter(suggestedListAdapter);
                    offset += 10;
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<AllFriends> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                progressDialog.hide();
            }
        });

    }

    private void sendSuggestedListPaginationRequest(Call<AllFriends> call) {

        call.enqueue(new Callback<AllFriends>() {

            @Override
            public void onResponse(Call<AllFriends> call, Response<AllFriends> response) {

                AllFriends allFriends = response.body();
                if (allFriends != null) {
                    friends.addAll(allFriends.getFriends());
                    suggestedListAdapter.addPagingData(friends);
                    offset += 10;
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AllFriends> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void sendSuggestedSearchListRequest(Call<ArrayList<Friend>> call) {

        call.enqueue(new Callback<ArrayList<Friend>>() {

            @Override
            public void onResponse(Call<ArrayList<Friend>> call, Response<ArrayList<Friend>> response) {

                ArrayList<Friend> friend = response.body();
                if (friend != null) {
                    friends = friend;
                    suggestedListAdapter = new SuggestedListAdapter(getActivity(), friends, suggestedListClickResponseService);
                    recyclerView.setAdapter(suggestedListAdapter);
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<ArrayList<Friend>> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                progressDialog.hide();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
