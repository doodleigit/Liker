package com.doodle.Profile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.doodle.Profile.model.Education;
import com.doodle.R;

import java.util.ArrayList;

public class EducationAdapter extends RecyclerView.Adapter<EducationAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Education> arrayList;

    public EducationAdapter(Context context, ArrayList<Education> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_education, viewGroup, false);
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

        TextView tvInstituteName, tvInstituteSite, tvDegree, tvStudyInfo, tvSummary;
        ImageView ivEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvInstituteName = itemView.findViewById(R.id.institute_name);
            tvInstituteSite = itemView.findViewById(R.id.institute_site);
            tvDegree = itemView.findViewById(R.id.degree);
            tvStudyInfo = itemView.findViewById(R.id.study_info);
            tvSummary = itemView.findViewById(R.id.summary);
            ivEdit = itemView.findViewById(R.id.edit);
        }
    }

}
