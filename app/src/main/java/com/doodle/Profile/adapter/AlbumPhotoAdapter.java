package com.doodle.Profile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.doodle.App;
import com.doodle.Profile.model.AlbumPhoto;
import com.doodle.R;

import java.util.ArrayList;

public class AlbumPhotoAdapter  extends RecyclerView.Adapter<AlbumPhotoAdapter.ViewHolder> {

    private Context context;
    private ArrayList<AlbumPhoto> arrayList;

    public AlbumPhotoAdapter(Context context, ArrayList<AlbumPhoto> arrayList) {
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
        Glide.with(App.getAppContext())
                .load(arrayList.get(i).getImageName())
                .placeholder(R.drawable.photo)
                .error(R.drawable.photo)
                .centerCrop()
                .dontAnimate()
                .into(viewHolder.image);
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

}
