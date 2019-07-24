package com.doodle.Post.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.doodle.R;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<String> mItems;
    private Context mContext;
    public static final String ITEM_KEY = "item_key";
    Drawable bitmapDrawable;
    private RecyclerViewClickListener mListener;

    public ImageAdapter(Context context, List<String> items) {
        this.mContext = context;
        this.mItems = items;
    }


    public interface RecyclerViewClickListener {

        void onClick(View view, int position);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.list_row_image, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(mContext).load("file://" + mItems.get(position).toString())
                .skipMemoryCache(false)
                .into(holder.imgPost);
        //   Vholder.rl_select.setBackgroundColor(Color.parseColor("#FFFFFF"));
        holder.imgPostCancel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mItems.remove(holder.getPosition());
                notifyDataSetChanged();

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder  {

        public ImageView imgPost,imgPostCancel;


        public ViewHolder(View v) {
            super(v);
            imgPost = (ImageView) itemView.findViewById(R.id.imgPost);
            imgPostCancel = (ImageView) itemView.findViewById(R.id.imgPostCancel);

        }


    }


}