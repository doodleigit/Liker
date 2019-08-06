package com.doodle.Profile.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
        String instituteName, instituteSite, degree, studyInfo, summary;

        instituteName = arrayList.get(i).getInstituteName();
        instituteSite = arrayList.get(i).getWebsiteUrl();
        degree = arrayList.get(i).getDegreeName();
        studyInfo = arrayList.get(i).getFieldStudyName() + " GPA-" + arrayList.get(i).getGrade() + " From " + arrayList.get(i).getStartYear() + " to " + arrayList.get(i).getEndYear();
        summary = arrayList.get(i).getDescription();

        viewHolder.tvInstituteName.setText(instituteName);
        viewHolder.tvDegree.setText(degree);
        viewHolder.tvStudyInfo.setText(studyInfo);
        viewHolder.tvSummary.setText(summary);

        if (!instituteSite.isEmpty()) {
            if (!instituteSite.startsWith("http://") && !instituteSite.startsWith("https://")) {
                instituteSite = "http://" + instituteSite;
            }
            viewHolder.tvInstituteSite.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvInstituteSite.setVisibility(View.GONE);
        }

        String finalInstituteSite = instituteSite;
        viewHolder.tvInstituteSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalInstituteSite));
                context.startActivity(browserIntent);
            }
        });
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
