package com.doodle.Message.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.doodle.App;
import com.doodle.Message.model.Messages;
import com.doodle.Message.model.User;
import com.doodle.R;
import com.doodle.utils.AppConstants;
import com.doodle.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MessagingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Messages> arrayList;
    private ArrayList<User> users;
    private String userId;
    private int LEFT_CHAT = 0, RIGHT_CHAT = 1;

    public MessagingAdapter(Context context, ArrayList<Messages> arrayList, ArrayList<User> users, String userId) {
        this.context = context;
        this.arrayList = arrayList;
        this.users = users;
        this.userId = userId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if (i == LEFT_CHAT) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_item_left, viewGroup, false);
            return new LeftChatViewHolder(view);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_item_right, viewGroup, false);
            return new RightChatViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        String userId, userImage = "", message, messageTime, seen;
        userId = arrayList.get(i).getFromUserId();
        message = arrayList.get(i).getContent();
        messageTime = arrayList.get(i).getTimePosted();
        seen = arrayList.get(i).getSeen();

        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                userImage = user.getPhoto();
                break;
            }
        }

        if (holder instanceof LeftChatViewHolder) {
            LeftChatViewHolder viewHolder = (LeftChatViewHolder) holder;

            viewHolder.tvMessage.setText(message);
            viewHolder.tvMessageTime.setText(Utils.chatDateCompare(context, Long.valueOf(messageTime)));

            Glide.with(App.getAppContext())
                    .load(AppConstants.PROFILE_IMAGE + userImage)
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .centerCrop()
                    .dontAnimate()
                    .into(viewHolder.ivProfileImage);

//            if (seen.equals("1")) {
//                viewHolder.tvMessage.setBackgroundResource(R.color.colorWhite);
//            } else {
//                viewHolder.tvMessage.setBackgroundResource(R.color.colorNotSeen);
//            }

        } else {
            RightChatViewHolder viewHolder = (RightChatViewHolder) holder;

            viewHolder.tvMessage.setText(message);
            viewHolder.tvMessageTime.setText(Utils.chatDateCompare(context, Long.valueOf(messageTime)));

            Glide.with(App.getAppContext())
                    .load(AppConstants.PROFILE_IMAGE + userImage)
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .centerCrop()
                    .dontAnimate()
                    .into(viewHolder.ivProfileImage);

//            if (seen.equals("1")) {
//                viewHolder.tvMessage.setBackgroundResource(R.color.colorWhite);
//            } else {
//                viewHolder.tvMessage.setBackgroundResource(R.color.colorNotSeen);
//            }
        }

    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("dd MMM yyyy hh:mm a", cal).toString();
        return date;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (arrayList.get(position).getFromUserId().equals(userId)) {
            return RIGHT_CHAT;
        } else {
            return LEFT_CHAT;
        }
    }

    public class RightChatViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfileImage;
        TextView tvMessage, tvMessageTime;

        public RightChatViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfileImage = itemView.findViewById(R.id.profile_image);
            tvMessage = itemView.findViewById(R.id.message);
            tvMessageTime = itemView.findViewById(R.id.message_time);
        }
    }

    public class LeftChatViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfileImage;
        TextView tvMessage, tvMessageTime;

        public LeftChatViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfileImage = itemView.findViewById(R.id.profile_image);
            tvMessage = itemView.findViewById(R.id.message);
            tvMessageTime = itemView.findViewById(R.id.message_time);
        }
    }

    public void addPagingData(ArrayList<Messages> messages) {
        arrayList.addAll(messages);
        notifyDataSetChanged();
    }

}
