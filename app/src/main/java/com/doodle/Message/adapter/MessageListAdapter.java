package com.doodle.Message.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.doodle.App;
import com.doodle.Message.model.ChatUser;
import com.doodle.Message.service.ListClickResponseService;
import com.doodle.R;
import com.doodle.Tool.AppConstants;
import com.doodle.Tool.Tools;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ChatUser> arrayList;
    private ListClickResponseService listClickResponseService;

    public MessageListAdapter(Context context, ArrayList<ChatUser> arrayList, ListClickResponseService listClickResponseService) {
        this.context = context;
        this.arrayList = arrayList;
        this.listClickResponseService = listClickResponseService;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_message, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvUserName.setText(arrayList.get(i).getUserData().getFirstName() + " " + arrayList.get(i).getUserData().getLastName());
        viewHolder.tvMessage.setText(arrayList.get(i).getMessageData().getContent());
        viewHolder.tvMessageTime.setText(Tools.chatDateCompare(context, Long.valueOf(arrayList.get(i).getMessageData().getTimePosted())));

        Glide.with(App.getAppContext())
                .load(AppConstants.PROFILE_IMAGE + arrayList.get(i).getUserData().getPhoto())
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .centerCrop()
                .dontAnimate()
                .into(viewHolder.ivUserImage);

        if (arrayList.get(i).getMessageData().getSeen().equals("1")) {
//            viewHolder.tvMessage.setTypeface(null, Typeface.NORMAL);
            viewHolder.tvMessage.setTextColor(Color.parseColor("#aaaaaa"));
        } else {
//            viewHolder.tvMessage.setTypeface(viewHolder.tvMessage.getTypeface(), Typeface.BOLD);
            viewHolder.tvMessage.setTextColor(Color.parseColor("#000000"));
        }

        viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.get(i).getMessageData().setSeen("0");
                viewHolder.tvMessage.setTypeface(null, Typeface.NORMAL);
                listClickResponseService.onMessageClick(arrayList.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout mainLayout;
        CircleImageView ivUserImage;
        TextView tvUserName, tvMessage, tvMessageTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mainLayout = itemView.findViewById(R.id.mainLayout);
            ivUserImage = itemView.findViewById(R.id.userImage);
            tvUserName = itemView.findViewById(R.id.userName);
            tvMessage = itemView.findViewById(R.id.message);
            tvMessageTime = itemView.findViewById(R.id.messageTime);

        }
    }

    public void addPagingData(ArrayList<ChatUser> chatUsers) {
        arrayList.addAll(chatUsers);
        notifyDataSetChanged();
    }

}
