package com.doodle.Message.view;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.doodle.Home.model.Headers;
import com.doodle.Home.service.SocketIOManager;
import com.doodle.Message.adapter.MessagingAdapter;
import com.doodle.Message.model.ChatUser;
import com.doodle.Message.model.Chats;
import com.doodle.Message.model.FriendInfo;
import com.doodle.Message.model.Message;
import com.doodle.Message.model.MessageSeenParam;
import com.doodle.Message.model.MessageSendParam;
import com.doodle.Message.model.Messages;
import com.doodle.Message.model.NewMessage;
import com.doodle.Message.model.User;
import com.doodle.Message.service.MessageService;
import com.doodle.R;
import com.doodle.utils.AppConstants;
import com.doodle.utils.NetworkHelper;
import com.doodle.utils.PrefManager;
import com.doodle.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import io.socket.client.Ack;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagingFragment extends Fragment {

    private View view;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private TextView tvUserName, tvLikes, tvStars;
    private EditText etReply;
    private ImageView tvSend;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;

    private Socket socket;
    private PrefManager manager;
    private boolean networkOk;
    private MessageService messageService;
    private MessagingAdapter messagingAdapter;
    private FriendInfo friendInfo;
    private ArrayList<Messages> messages;
    private ArrayList<User> users;
    private String deviceId, profileId, token, userName, likes, stars, userIds, toUserId, chatUserName;
    int limit = 20;
    int offset = 0;
    private boolean isScrolling;
    private int totalItems;
    private int scrollOutItems;
    private int currentItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.messaging_fragment_layout, container, false);

        initialComponent();

        return view;
    }

    private void initialComponent() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConstants.NEW_MESSAGE_BROADCAST);
        getActivity().registerReceiver(broadcastReceiver, filter);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.loading));

        socket = SocketIOManager.mSocket;
        manager = new PrefManager(getContext());
        deviceId = manager.getDeviceId();
        profileId = manager.getProfileId();
        token = manager.getToken();
        userIds = manager.getProfileId();
        friendInfo = (FriendInfo) getArguments().getSerializable("friend_info");
        chatUserName = friendInfo.getUserName();
        toUserId = friendInfo.getToUserId();
        userName = friendInfo.getFullName();
        likes = friendInfo.getLikes();
        stars = friendInfo.getStars();
        networkOk = NetworkHelper.hasNetworkAccess(getContext());
        messageService = MessageService.mRetrofit.create(MessageService.class);
        messages = new ArrayList<>();
        users = new ArrayList<>();

        tvUserName = view.findViewById(R.id.user_name);
        tvLikes = view.findViewById(R.id.likes);
        tvStars = view.findViewById(R.id.stars);
        tvSend = view.findViewById(R.id.send);
        etReply = view.findViewById(R.id.reply);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        toolbar = view.findViewById(R.id.toolbar);
        progressBar = view.findViewById(R.id.progress_bar);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);

        messagingAdapter = new MessagingAdapter(getActivity(), messages, users, userIds);
        recyclerView.setAdapter(messagingAdapter);

        getData();
        setData();
        messageSeen();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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

        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String reply = etReply.getText().toString();
                if (!reply.isEmpty()) {
                    sendReply(reply);
                }
            }
        });

    }

    private void getData() {
        if (networkOk) {
            Call<Chats> call = messageService.getAllMessage(deviceId, profileId, token, userIds, chatUserName, limit, offset);
            sendAllMessageRequest(call);
            progressDialog.show();
        } else {
            Utils.showNetworkDialog(getChildFragmentManager());
        }
    }

    private void sendReply(String reply) {
        Headers headers = new Headers();
        MessageSendParam messageSendParam = new MessageSendParam();
        headers.setDeviceId(deviceId);
        headers.setIsApps(true);
        headers.setSecurityToken(token);
        messageSendParam.setToUserId(toUserId);
        messageSendParam.setMessage(reply);
        messageSendParam.setUserId(userIds);
        messageSendParam.setHeaders(headers);
        Gson gson = new Gson();
        String json = gson.toJson(messageSendParam);
        socket.emit("message", json, new Ack() {
            @Override
            public void call(Object... args) {

            }
        });
        etReply.setText("");
    }

    private void messageSeen() {
        Headers headers = new Headers();
        MessageSeenParam messageSeenParam = new MessageSeenParam();
        headers.setDeviceId(deviceId);
        headers.setIsApps(true);
        headers.setSecurityToken(token);
        messageSeenParam.setChatUserId(toUserId);
        messageSeenParam.setUserId(userIds);
        messageSeenParam.setHeaders(headers);
        Gson gson = new Gson();
        String json = gson.toJson(messageSeenParam);
        socket.emit("seen_message", json, new Ack() {
            @Override
            public void call(Object... args) {

            }
        });
    }

    private void setData() {
        tvUserName.setText(userName);
        tvLikes.setText(likes + " " + getString(R.string.likes));
        tvStars.setText(stars + " " + getString(R.string.stars));
    }

    private void getPagination() {
        if (networkOk) {
            progressBar.setVisibility(View.VISIBLE);
            Call<Chats> call = messageService.getAllMessage(deviceId, profileId, token, userIds, chatUserName, limit, offset);
            sendAllMessagePaginationRequest(call);
        }
    }

    private void sendAllMessageRequest(Call<Chats> call) {

        call.enqueue(new Callback<Chats>() {

            @Override
            public void onResponse(Call<Chats> call, Response<Chats> response) {

                Chats chats = response.body();
                if (chats != null) {
                    Collections.reverse(chats.getMessages());
                    messages.addAll(chats.getMessages());
                    users = chats.getUsers();
                    messagingAdapter.notifyDataSetChanged();
                    offset += limit;
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<Chats> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                progressDialog.hide();
            }
        });

    }

    private void sendAllMessagePaginationRequest(Call<Chats> call) {

        call.enqueue(new Callback<Chats>() {

            @Override
            public void onResponse(Call<Chats> call, Response<Chats> response) {

                Chats chats = response.body();
                if (chats != null) {
                    Collections.reverse(chats.getMessages());
                    messages.addAll(chats.getMessages());
                    messagingAdapter.notifyDataSetChanged();
                    offset += limit;
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Chats> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            NewMessage newMessage = intent.getParcelableExtra("new_message");
            int isOwnMessage = intent.getIntExtra("is_own", 0);
            for (User user : users) {
                if (user.getUserId().equals(newMessage.getUserId())) {
                    Messages msg = new Messages();
                    msg.setId(newMessage.getInsertId());
                    msg.setFromUserId(newMessage.getUserId());
                    msg.setToUserId(newMessage.getToUserId());
                    msg.setContent(newMessage.getMessage());
                    msg.setTimePosted(newMessage.getTimePosted());
                    msg.setSeen(String.valueOf(isOwnMessage));
                    msg.setReportId("0");
                    msg.setReportStatus("0");
                    msg.setDeletedBy("0");
                    if (messages.size() > 0) {
                        messages.add(0, msg);
                    } else {
                        messages.add(msg);
                    }
                    offset++;
                    break;
                }
            }
            if (isOwnMessage == 0) {
                messageSeen();
            }
            messagingAdapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(0);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }
}
