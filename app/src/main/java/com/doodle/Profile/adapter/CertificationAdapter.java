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

import com.doodle.Profile.model.Certification;
import com.doodle.Profile.service.AboutComponentUpdateListener;
import com.doodle.R;
import com.doodle.utils.Utils;

import java.util.ArrayList;

public class CertificationAdapter extends RecyclerView.Adapter<CertificationAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Certification> arrayList;
    private AboutComponentUpdateListener aboutComponentUpdateListener;
    private boolean isOwnProfile;

    public CertificationAdapter(Context context, ArrayList<Certification> arrayList, AboutComponentUpdateListener aboutComponentUpdateListener, boolean isOwnProfile) {
        this.context = context;
        this.arrayList = arrayList;
        this.aboutComponentUpdateListener = aboutComponentUpdateListener;
        this.isOwnProfile = isOwnProfile;
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
        String certificateName, instituteName, instituteSite, duration;

        certificateName = arrayList.get(i).getCertificationName();
        instituteName = arrayList.get(i).getInstituteName();
        instituteSite = arrayList.get(i).getCertificationUrl();
        duration = Utils.getMonth(arrayList.get(i).getFromMonth()) + " " + arrayList.get(i).getFromYear() + " Licence " + arrayList.get(i).getLicenseNumber();

        viewHolder.tvCertificateName.setText(certificateName);
        viewHolder.tvInstituteName.setText(instituteName);
        viewHolder.tvDuration.setText(duration);

        if (!instituteSite.isEmpty()) {
            if (!instituteSite.startsWith("http://") && !instituteSite.startsWith("https://")) {
                instituteSite = "http://" + instituteSite;
            }
            viewHolder.tvInstituteSite.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvInstituteSite.setVisibility(View.GONE);
        }

        if (isOwnProfile) {
            viewHolder.ivEdit.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivEdit.setVisibility(View.INVISIBLE);
        }

        String finalInstituteSite = instituteSite;
        viewHolder.tvInstituteSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalInstituteSite));
                context.startActivity(browserIntent);
            }
        });

        viewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aboutComponentUpdateListener.onCertificationUpdate(arrayList.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvCertificateName, tvInstituteName, tvInstituteSite, tvDuration;
        ImageView ivEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCertificateName = itemView.findViewById(R.id.certificate_name);
            tvInstituteName = itemView.findViewById(R.id.institute_name);
            tvInstituteSite = itemView.findViewById(R.id.institute_site);
            tvDuration = itemView.findViewById(R.id.duration);
            ivEdit = itemView.findViewById(R.id.edit);
        }
    }

}
