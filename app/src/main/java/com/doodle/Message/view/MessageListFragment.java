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
import com.doodle.Message.model.SenderData;
import com.doodle.Message.model.User;
import com.doodle.Message.model.UserData;
import com.doodle.Message.service.ClickListener;
import com.doodle.Message.service.ListClickResponseService;
import com.doodle.Message.service.MessageService;
import com.doodle.R;
import com.doodle.utils.AppConstants;
import com.doodle.utils.NetworkHelper;
import com.doodle.utils.PrefManager;
import com.doodle.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
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
        getActivity().registerReceiver(broadcastReceiver, filter);
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
            Utils.showNetworkDialog(getChildFragmentManager());
        }

        socket.on("message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject messageJson = new JSONObject(args[0].toString());
                    NewMessage newMessage = new NewMessage();

                    newMessage.setUserId(messageJson.getString("user_id"));
                    newMessage.setToUserId(messageJson.getString("to_user_id"));
                    newMessage.setMessage(messageJson.getString("message"));
                    newMessage.setReturnResult(messageJson.getBoolean("return_result"));
                    newMessage.setTimePosted(messageJson.getString("time_posted"));
                    newMessage.setInsertId(messageJson.getString("insert_id"));
                    newMessage.setUnreadTotal(messageJson.getString("unread_total"));

                    SenderData senderData = new SenderData();
                    senderData.setId(messageJson.getJSONObject("user_data").getString("id"));
                    senderData.setUserId(messageJson.getJSONObject("user_data").getString("user_id"));
                    senderData.setUserName(messageJson.getJSONObject("user_data").getString("user_name"));
                    senderData.setFirstName(messageJson.getJSONObject("user_data").getString("first_name"));
                    senderData.setLastName(messageJson.getJSONObject("user_data").getString("last_name"));
                    senderData.setTotalLikes(messageJson.getJSONObject("user_data").getString("total_likes"));
                    senderData.setGoldStars(messageJson.getJSONObject("user_data").getString("gold_stars"));
                    senderData.setSliverStars(messageJson.getJSONObject("user_data").getString("sliver_stars"));
                    senderData.setPhoto(messageJson.getJSONObject("user_data").getString("photo"));
                    senderData.setEmail(messageJson.getJSONObject("user_data").getString("email"));
                    senderData.setDeactivated(messageJson.getJSONObject("user_data").getString("deactivated"));
                    senderData.setFoundingUser(messageJson.getJSONObject("user_data").getString("founding_user"));
                    senderData.setLearnAboutSite(messageJson.getJSONObject("user_data").getInt("learn_about_site"));
                    senderData.setIsTopCommenter(messageJson.getJSONObject("user_data").getString("is_top_commenter"));
                    senderData.setIsMaster(messageJson.getJSONObject("user_data").getString("is_master"));
                    senderData.setDescription(messageJson.getJSONObject("user_data").getString("description"));

                    newMessage.setSenderData(senderData);

                    setNewMessageToList(newMessage, 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        socket.on("message_own", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject messageJson = new JSONObject(args[0].toString());
                    NewMessage newMessage = new NewMessage();

                    newMessage.setUserId(messageJson.getString("user_id"));
                    newMessage.setToUserId(messageJson.getString("to_user_id"));
                    newMessage.setMessage(messageJson.getString("message"));
                    newMessage.setReturnResult(messageJson.getBoolean("return_result"));
                    newMessage.setTimePosted(messageJson.getString("time_posted"));
                    newMessage.setInsertId(messageJson.getString("insert_id"));
                    newMessage.setUnreadTotal(messageJson.getString("unread_total"));

                    SenderData senderData = new SenderData();
                    senderData.setId(messageJson.getJSONObject("to_user_data").getString("id"));
                    senderData.setUserId(messageJson.getJSONObject("to_user_data").getString("user_id"));
                    senderData.setUserName(messageJson.getJSONObject("to_user_data").getString("user_name"));
                    senderData.setFirstName(messageJson.getJSONObject("to_user_data").getString("first_name"));
                    senderData.setLastName(messageJson.getJSONObject("to_user_data").getString("last_name"));
                    senderData.setTotalLikes(messageJson.getJSONObject("to_user_data").getString("total_likes"));
                    senderData.setGoldStars(messageJson.getJSONObject("to_user_data").getString("gold_stars"));
                    senderData.setSliverStars(messageJson.getJSONObject("to_user_data").getString("sliver_stars"));
                    senderData.setPhoto(messageJson.getJSONObject("to_user_data").getString("photo"));
                    senderData.setEmail(messageJson.getJSONObject("to_user_data").getString("email"));
                    senderData.setDeactivated(messageJson.getJSONObject("to_user_data").getString("deactivated"));
                    senderData.setFoundingUser(messageJson.getJSONObject("to_user_data").getString("founding_user"));
                    senderData.setLearnAboutSite(messageJson.getJSONObject("to_user_data").getInt("learn_about_site"));
                    senderData.setIsTopCommenter(messageJson.getJSONObject("to_user_data").getString("is_top_commenter"));
                    senderData.setIsMaster(messageJson.getJSONObject("to_user_data").getString("is_master"));
                    senderData.setDescription(messageJson.getJSONObject("to_user_data").getString("description"));

                    newMessage.setSenderData(senderData);

                    setNewMessageToList(newMessage, 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
        socket.off("message");
        socket.off("message_own");
    }
}
