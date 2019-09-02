package com.doodle.Notification.adapter;

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
import com.doodle.Notification.model.NotificationItem;
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

    public NotificationAdapter(Context context, ArrayList<NotificationItem> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_notification, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        String details, photo;
        long time;
        details = arrayList.get(i).getText();

        photo = AppConstants.PROFILE_IMAGE + arrayList.get(i).getData().getPhoto();
        time = Long.valueOf(arrayList.get(i).getData().getTimeSent());

        viewHolder.notificationDetails.setText(Tools.colorBackground(details));
        viewHolder.notificationTime.setText(getDate(time));
        if (!isNullOrEmpty(photo)) {
            Glide.with(App.getAppContext())
                    .load(photo)
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .centerCrop()
                    .dontAnimate()
                    .into(viewHolder.imageNotification);
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

        ImageView imageNotification;
        TextView notificationDetails, notificationTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageNotification = itemView.findViewById(R.id.imageNotification);
            notificationDetails = itemView.findViewById(R.id.notificationDetails);
            notificationTime = itemView.findViewById(R.id.notificationTime);
        }
    }

    public void addPagingData(ArrayList<NotificationItem> notificationItems) {
        arrayList.addAll(notificationItems);
        notifyDataSetChanged();
    }

}
