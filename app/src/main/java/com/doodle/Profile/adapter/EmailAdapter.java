package com.doodle.Profile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.doodle.Profile.model.Email;
import com.doodle.R;

import java.util.ArrayList;

public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Email> arrayList;
    private boolean isOwnProfile;

    public EmailAdapter(Context context, ArrayList<Email> arrayList, boolean isOwnProfile) {
        this.context = context;
        this.arrayList = arrayList;
        this.isOwnProfile = isOwnProfile;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_email_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvEmail.setText(arrayList.get(i).getEmail());

        if (arrayList.get(i).getPermissionType().equals("1")) {
            viewHolder.tvPermissionIcon.setImageResource(R.drawable.only_me);
        } else if (arrayList.get(i).getPermissionType().equals("2")) {
            viewHolder.tvPermissionIcon.setImageResource(R.drawable.friends);
        } else {
            viewHolder.tvPermissionIcon.setImageResource(R.drawable.public_);
        }

        if (isOwnProfile) {
            viewHolder.tvPermissionIcon.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvPermissionIcon.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvEmail;
        ImageView tvPermissionIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvEmail = itemView.findViewById(R.id.email);
            tvPermissionIcon = itemView.findViewById(R.id.permission_icon);
        }
    }

}
