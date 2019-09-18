package com.doodle.Notification.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.doodle.App;
import com.doodle.Home.view.activity.Home;
import com.doodle.Home.view.activity.StarContributorActivity;
import com.doodle.Notification.model.NotificationItem;
import com.doodle.Notification.service.NotificationClickListener;
import com.doodle.Profile.view.ProfileActivity;
import com.doodle.R;
import com.doodle.Tool.AppConstants;
import com.doodle.Tool.Tools;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.doodle.Tool.Tools.isNullOrEmpty;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context context;
    private ArrayList<NotificationItem> arrayList;
    private NotificationClickListener notificationClickListener;

    public NotificationAdapter(Context context, ArrayList<NotificationItem> arrayList, NotificationClickListener notificationClickListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.notificationClickListener = notificationClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_notification, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        String details, photo, type, seen;
        long time;

        details = arrayList.get(i).getText();
        type = arrayList.get(i).getData().getNotifType();
        seen = arrayList.get(i).getData().getHasSeenDetails();
        photo = AppConstants.PROFILE_IMAGE + arrayList.get(i).getData().getPhoto();
        time = Long.valueOf(arrayList.get(i).getData().getTimeSent());

        viewHolder.notificationDetails.setText(Tools.colorBackground(details));
        viewHolder.notificationTime.setText(getDate(time));

        if (seen.equals("0")) {
            viewHolder.mainLayout.setBackgroundResource(R.color.colorMainBackground);
        } else {
            viewHolder.mainLayout.setBackgroundResource(R.color.colorWhite);
        }

        if (!isNullOrEmpty(photo)) {
            Glide.with(App.getAppContext())
                    .load(photo)
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .centerCrop()
                    .dontAnimate()
                    .into(viewHolder.imageNotification);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationClickListener.onNotificationClick(arrayList.get(i).getData().getId());
                executeAction(Tools.getNotificationTypeActionType(type), arrayList.get(i));
                arrayList.get(i).getData().setHasSeenDetails("1");
                notifyItemChanged(i);
            }
        });

    }

    private void executeAction(int actionType, NotificationItem notificationItem) {
        if (actionType == 1) {
            context.startActivity(new Intent(context, ProfileActivity.class).putExtra("user_id", notificationItem.getData().getFromUserId()).putExtra("user_name", notificationItem.getData().getUsername()));
        } else if (actionType == 2) {
            notificationClickListener.onNotificationPostActionClick(notificationItem.getData().getTypeId(), false);
        } else if (actionType == 3) {
            notificationClickListener.onNotificationPostActionClick(notificationItem.getData().getTypeId(), true);
        } else if (actionType == 4) {
            context.startActivity(new Intent(context, StarContributorActivity.class).putExtra("category_id", "").putExtra("category_name", ""));
        } else if (actionType == 5) {
            context.startActivity(new Intent(context, StarContributorActivity.class).putExtra("category_id", notificationItem.getData().getCategoryId()).putExtra("category_name", notificationItem.getData().getCategoryName()));
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout mainLayout;
        ImageView imageNotification;
        TextView notificationDetails, notificationTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mainLayout = itemView.findViewById(R.id.main_layout);
            imageNotification = itemView.findViewById(R.id.imageNotification);
            notificationDetails = itemView.findViewById(R.id.notificationDetails);
            notificationTime = itemView.findViewById(R.id.notificationTime);
        }
    }

}
