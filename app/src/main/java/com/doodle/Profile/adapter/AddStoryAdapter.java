package com.doodle.Profile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.doodle.R;

import java.util.ArrayList;

public class AddStoryAdapter extends RecyclerView.Adapter<AddStoryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> arrayList;

    public AddStoryAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_add_story, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvStoryTitle, tvChange, tvStory;
        Spinner storyPrivacySpinner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvStoryTitle = itemView.findViewById(R.id.story_title);
            tvChange = itemView.findViewById(R.id.change);
            tvStory = itemView.findViewById(R.id.story);
            storyPrivacySpinner = itemView.findViewById(R.id.story_privacy_spinner);
        }
    }

}
