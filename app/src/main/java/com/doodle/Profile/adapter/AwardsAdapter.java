package com.doodle.Profile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.doodle.Profile.model.Awards;
import com.doodle.Profile.service.AboutComponentUpdateListener;
import com.doodle.R;
import com.doodle.Tool.Tools;

import java.util.ArrayList;

public class AwardsAdapter extends RecyclerView.Adapter<AwardsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Awards> arrayList;
    private AboutComponentUpdateListener aboutComponentUpdateListener;
    private boolean isOwnProfile;

    public AwardsAdapter(Context context, ArrayList<Awards> arrayList, AboutComponentUpdateListener aboutComponentUpdateListener, boolean isOwnProfile) {
        this.context = context;
        this.arrayList = arrayList;
        this.aboutComponentUpdateListener = aboutComponentUpdateListener;
        this.isOwnProfile = isOwnProfile;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_awards, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String awardsName, instituteNameDuration, summary;

        awardsName = arrayList.get(i).getAwardName();
        instituteNameDuration = arrayList.get(i).getInstituteName() + " - " + Tools.getMonth(arrayList.get(i).getMonth()) + " " + arrayList.get(i).getYear();
        summary = arrayList.get(i).getDescription();

        viewHolder.tvAwardsName.setText(awardsName);
        viewHolder.tvInstituteNameDuration.setText(instituteNameDuration);
        viewHolder.tvSummary.setText(summary);

        if (isOwnProfile) {
            viewHolder.ivEdit.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivEdit.setVisibility(View.INVISIBLE);
        }

        viewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aboutComponentUpdateListener.onAwardsUpdate(arrayList.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvAwardsName, tvInstituteNameDuration, tvSummary;
        ImageView ivEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAwardsName = itemView.findViewById(R.id.awards_name);
            tvInstituteNameDuration = itemView.findViewById(R.id.institute_name_duration);
            tvSummary = itemView.findViewById(R.id.summary);
            ivEdit = itemView.findViewById(R.id.edit);
        }
    }

}
