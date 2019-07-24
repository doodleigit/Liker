package com.doodle.Post.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.doodle.App;
import com.doodle.Post.model.Mim;
import com.doodle.R;
import com.doodle.utils.AppConstants;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class MimAdapter extends RecyclerView.Adapter<MimAdapter.ViewHolder> {

    private List<Mim> mItems;
    private Context mContext;
    public static final String ITEM_KEY = "item_key";
    Drawable bitmapDrawable;
    private RecyclerViewClickListener mListener;

    public MimAdapter(Context context, List<Mim> items, RecyclerViewClickListener listener) {
        this.mContext = context;
        this.mItems = items;
        mListener = listener;
    }


    public interface RecyclerViewClickListener {

        void onClick(View view, int position);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.mim_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Mim item = mItems.get(position);
        String mimColor = item.getMimColor();
        if (mimColor.startsWith("#")) {
            int mColor = Color.parseColor(mimColor);
            ColorDrawable cd = new ColorDrawable(mColor);
            holder.mimContent.setBackground(cd);
        } else {
            String imgUrl = AppConstants.MIM_IMAGE + mimColor;
//            Picasso.with(App.getAppContext())
//                    .load(imgUrl)
//                    .placeholder(R.drawable.profile)
//                    .into((Target) holder.mimContent);
            Picasso.with(App.getAppContext()).load(imgUrl).into(target);
            holder.mimContent.setBackground(bitmapDrawable);
        }


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View mView;
        public LinearLayout mimContent;
        private RecyclerViewClickListener mListener;


        public ViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            mimContent = (LinearLayout) itemView.findViewById(R.id.mimContent);
            mListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            // mBitmap = bitmap;

            bitmapDrawable = new BitmapDrawable(mContext.getResources(), bitmap);

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };
}