package com.doodle.Profile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.doodle.App;
import com.doodle.Profile.model.PhotoAlbum;
import com.doodle.Profile.service.PhotoAlbumClickListener;
import com.doodle.R;
import com.doodle.Tool.AppConstants;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private Context context;
    private ArrayList<PhotoAlbum> arrayList;
    private PhotoAlbumClickListener photoAlbumClickListener;

    public AlbumAdapter(Context context, ArrayList<PhotoAlbum> arrayList, PhotoAlbumClickListener photoAlbumClickListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.photoAlbumClickListener = photoAlbumClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_album_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String url = AppConstants.USER_UPLOADED_IMAGES + arrayList.get(i).getImageName();

        viewHolder.title.setText(arrayList.get(i).getTitle());
        viewHolder.count.setText(arrayList.get(i).getTotalImages() + " " + context.getString(R.string.photos));

        Glide.with(App.getAppContext())
                .load(url)
                .placeholder(R.drawable.photo)
                .error(R.drawable.photo)
                .centerCrop()
                .dontAnimate()
                .into(viewHolder.image);

        viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoAlbumClickListener.onAlbumClick(arrayList.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mainLayout;
        ImageView image;
        TextView title, count;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mainLayout = itemView.findViewById(R.id.main_layout);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            count = itemView.findViewById(R.id.count);
        }
    }

}
