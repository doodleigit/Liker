package com.doodle.Message.view;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import com.doodle.Home.service.SocketIOManager;
import com.doodle.Message.adapter.MessageListAdapter;
import com.doodle.Message.model.ChatUser;
import com.doodle.Message.model.FriendInfo;
import com.doodle.Message.model.Message;
import com.doodle.Message.model.MessageData;
import com.doodle.Message.model.NewMessage;
import com.doodle.Message.model.UserData;
import com.doodle.Message.service.ListClickResponseService;
import com.doodle.Message.service.MessageService;
import com.doodle.R;
import com.doodle.Tool.AppConstants;
import com.doodle.Tool.NetworkHelper;
import com.doodle.Tool.PrefManager;
import com.doodle.Tool.Tools;

import java.util.ArrayList;
import java.util.Collections;

import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageListFragment extends Fragment {

    private View view;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    FloatingActionButton fabNewMessage;
    private LinearLayoutManager layoutManager;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;

    private Socket socket;
    private PrefManager manager;
    private boolean networkOk;
    private ListClickResponseService listClickResponseService;
    private MessageService messageService;
    private MessageListAdapter messageListAdapter;
    private ArrayList<ChatUser> chatUsers;
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
        view = inflater.inflate(R.layout.message_list_fragment_layout, container, false);

        initialComponent();

        return view;
    }

    private void initialComponent() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConstants.LIST_MESSAGE_BROADCAST);
        IntentFilter filterMessage = new IntentFilter();
        filterMessage.addAction(AppConstants.NEW_MESSAGE_BROADCAST_FROM_HOME);
        getActivity().registerReceiver(broadcastReceiver, filter);
        getActivity().registerReceiver(newMessageBroadcastReceiver, filterMessage);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.loading));

        socket = SocketIOManager.mSocket;
        manager = new PrefManager(getContext());
        deviceId = manager.getDeviceId();
        profileId = manager.getProfileId();
        token = manager.getToken();
        userIds = manager.getProfileId();
        networkOk = NetworkHelper.hasNetworkAccess(getContext());
        messageService = MessageService.mRetrofit.create(MessageService.class);
        chatUsers = new ArrayList<>();

        layoutManager = new LinearLayoutManager(getContext());
        toolbar = view.findViewById(R.id.toolbar);
        fabNewMessage = view.findViewById(R.id.new_message);
        progressBar = view.findViewById(R.id.progress_bar);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);

        listClickResponseService = new ListClickResponseService() {
            @Override
            public void onMessageClick(ChatUser chatUser) {
                FriendInfo friendInfo = new FriendInfo(chatUser.getUserData().getUserName(), chatUser.getUserData().getUserId(),
                        (chatUser.getUserData().getFirstName() + " " + chatUser.getUserData().getLastName()), chatUser.getUserData().getTotalLikes(), chatUser.getUserData().getGoldStars());
                initiateFragment(friendInfo);
            }
        };

        messageListAdapter = new MessageListAdapter(getActivity(), chatUsers, listClickResponseService);
        recyclerView.setAdapter(messageListAdapter);

        getData();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        fabNewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initiateSuggestedFragment();
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

    }

    private void getData() {
        if (networkOk) {
            progressDialog.show();
            Call<Message> call = messageService.getMessageList(deviceId, profileId, token, userIds, limit, offset);
            sendMessageListRequest(call);
        } else {
            Tools.showNetworkDialog(getChildFragmentManager());
        }
    }

    private void getPagination() {
        if (networkOk) {
            progressBar.setVisibility(View.VISIBLE);
            Call<Message> call = messageService.getMessageList(deviceId, profileId, token, userIds, limit, offset);
            sendMessageListPaginationRequest(call);
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

    private void initiateSuggestedFragment() {
        NewMessageFragment newMessageFragment = new NewMessageFragment();
        android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
        transaction.addToBackStack(null);
        transaction.add(R.id.container, newMessageFragment).commit();
    }

    private void setNewMessageToList(NewMessage newMessage, int type) {
        boolean isNotExist = true;
        if (type == 1) {
            for (int i = 0; i < chatUsers.size(); i++) {
                if (newMessage.getToUserId().equals(chatUsers.get(i).getUserData().getUserId())) {
                    isNotExist = false;
                    chatUsers.get(i).getMessageData().setContent(newMessage.getMessage());
                    chatUsers.get(i).getMessageData().setTimePosted(newMessage.getTimePosted());
                    chatUsers.get(i).getMessageData().setSeen("1");
                    Collections.swap(chatUsers, i, 0);
                    break;
                }
            }
        } else {
            for (int i = 0; i < chatUsers.size(); i++) {
                if (newMessage.getUserId().equals(chatUsers.get(i).getUserData().getUserId())) {
                    isNotExist = false;
                    chatUsers.get(i).getMessageData().setContent(newMessage.getMessage());
                    chatUsers.get(i).getMessageData().setTimePosted(newMessage.getTimePosted());
                    chatUsers.get(i).getMessageData().setSeen("0");
                    Collections.swap(chatUsers, i, 0);
                    break;
                }
            }
        }

        if (isNotExist) {
            MessageData messageData = new MessageData();
            messageData.setId(newMessage.getInsertId());
            messageData.setFromUserId(newMessage.getUserId());
            messageData.setToUserId(newMessage.getToUserId());
            messageData.setContent(newMessage.getMessage());//
            messageData.setTimePosted(newMessage.getTimePosted());//
            if (type == 0) {
                messageData.setSeen("0");
            } else {
                messageData.setSeen("1");
            }
            messageData.setReportId("0");
            messageData.setReportStatus("0");
            messageData.setDeletedBy("0");

            UserData userData = new UserData();
            userData.setId(newMessage.getSenderData().getId());
            userData.setUserId(newMessage.getSenderData().getUserId());
            userData.setUserName(newMessage.getSenderData().getUserName());
            userData.setFirstName(newMessage.getSenderData().getFirstName());
            userData.setLastName(newMessage.getSenderData().getLastName());
            userData.setTotalLikes(newMessage.getSenderData().getTotalLikes());
            userData.setGoldStars(newMessage.getSenderData().getGoldStars());
            userData.setSliverStars(newMessage.getSenderData().getSliverStars());
            userData.setPhoto(newMessage.getSenderData().getPhoto());
            userData.setEmail(newMessage.getSenderData().getEmail());
            userData.setDeactivated(newMessage.getSenderData().getDeactivated());
            userData.setFoundingUser(newMessage.getSenderData().getFoundingUser());
            userData.setLearnAboutSite(String.valueOf(newMessage.getSenderData().getLearnAboutSite()));
            userData.setIsTopCommenter(newMessage.getSenderData().getIsTopCommenter());
            userData.setIsMaster(newMessage.getSenderData().getIsMaster());
            userData.setDescription(newMessage.getSenderData().getDescription());

            String unreadTotal = newMessage.getUnreadTotal();

            ChatUser chatUser = new ChatUser();
            chatUser.setMessageData(messageData);
            chatUser.setUserData(userData);
            chatUser.setUnreadTotal(Integer.valueOf(unreadTotal));
            if (chatUsers.size() > 0) {
                chatUsers.add(0, chatUser);
            } else {
                chatUsers.add(chatUser);
            }
        }

        getActivity().sendBroadcast((new Intent()).setAction(AppConstants.LIST_MESSAGE_BROADCAST));

        Intent intent = new Intent();
        intent.setAction(AppConstants.NEW_MESSAGE_BROADCAST);
        intent.putExtra("new_message", (Parcelable) newMessage);
        intent.putExtra("is_own", type);
        getActivity().sendBroadcast(intent);
    }

    private void sendMessageListRequest(Call<Message> call) {

        call.enqueue(new Callback<Message>() {

            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {

                Message message = response.body();
                if (message != null) {
                    chatUsers.addAll(message.getChatUsers());
                    messageListAdapter.notifyDataSetChanged();
                    offset += 10;
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                progressDialog.hide();
            }
        });

    }

    private void sendMessageListPaginationRequest(Call<Message> call) {

        call.enqueue(new Callback<Message>() {

            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {

                Message message = response.body();
                if (message != null) {
                    chatUsers.addAll(message.getChatUsers());
                    messageListAdapter.notifyDataSetChanged();
                    offset += 10;
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            messageListAdapter.notifyDataSetChanged();
        }
    };

    BroadcastReceiver newMessageBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            NewMessage newMessage = intent.getParcelableExtra("new_message");
            int type = intent.getIntExtra("type", 0);
            setNewMessageToList(newMessage, type);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
        getActivity().unregisterReceiver(newMessageBroadcastReceiver);
    }
}
