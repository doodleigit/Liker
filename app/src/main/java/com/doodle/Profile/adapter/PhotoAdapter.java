package com.doodle.Profile.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.doodle.App;
import com.doodle.Profile.model.RecentPhoto;
import com.doodle.R;
import com.doodle.utils.AppConstants;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private Context context;
    private ArrayList<RecentPhoto> arrayList;

    public PhotoAdapter(Context context, ArrayList<RecentPhoto> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_photo_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String url = AppConstants.USER_UPLOADED_IMAGES + arrayList.get(i).getImageName();
        Glide.with(App.getAppContext())
                .load(url)
                .placeholder(R.drawable.photo)
                .error(R.drawable.photo)
                .centerCrop()
                .dontAnimate()
                .into(viewHolder.image);

        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewFullImage(url);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
        }
    }

    private void viewFullImage(String url) {
        Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
        dialog.setContentView(R.layout.image_full_view);

        Toolbar toolbar = dialog.findViewById(R.id.toolbar);

        PhotoView photoView = dialog.findViewById(R.id.photo_view);
        Glide.with(App.getAppContext())
                .load(url)
                .dontAnimate()
                .into(photoView);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
