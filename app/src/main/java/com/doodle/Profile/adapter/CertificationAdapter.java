package com.doodle.Profile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.doodle.Profile.model.Certification;
import com.doodle.R;

import java.util.ArrayList;

public class CertificationAdapter extends RecyclerView.Adapter<CertificationAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Certification> arrayList;

    public CertificationAdapter(Context context, ArrayList<Certification> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_certificate, viewGroup, false);
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

        TextView tvCertificateName, tvInstituteName, tvSummary, tvDuration;
        ImageView ivEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCertificateName = itemView.findViewById(R.id.certificate_name);
            tvInstituteName = itemView.findViewById(R.id.institute_name);
            tvSummary = itemView.findViewById(R.id.summary);
            tvDuration = itemView.findViewById(R.id.duration);
            ivEdit = itemView.findViewById(R.id.edit);
        }
    }

}
