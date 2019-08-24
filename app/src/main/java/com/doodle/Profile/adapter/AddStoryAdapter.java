package com.doodle.Profile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.doodle.Profile.model.Story;
import com.doodle.Profile.service.StoryModificationListener;
import com.doodle.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddStoryAdapter extends RecyclerView.Adapter<AddStoryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Story> arrayList;
    private List<String> storyList;
    private StoryModificationListener storyModificationListener;

    public AddStoryAdapter(Context context, ArrayList<Story> arrayList, StoryModificationListener storyModificationListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.storyList = Arrays.asList(context.getResources().getStringArray(R.array.story_type_list));
        this.storyModificationListener = storyModificationListener;
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
        String title = storyList.get(Integer.valueOf(arrayList.get(i).getType()) - 1);

        viewHolder.tvStoryTitle.setText(title);
        viewHolder.tvStory.setText(arrayList.get(i).getDescription());

        viewHolder.tvChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storyModificationListener.onStoryEdit(arrayList.get(i));
            }
        });
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
