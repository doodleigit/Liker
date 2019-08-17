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

public class AddEmailAdapter extends RecyclerView.Adapter<AddEmailAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> arrayList;

    public AddEmailAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_add_phone, viewGroup, false);
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

        TextView tvEmail, tvRemove, tvEmailType;
        Spinner emailPrivacySpinner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvEmail = itemView.findViewById(R.id.email);
            tvRemove = itemView.findViewById(R.id.remove);
            tvEmailType = itemView.findViewById(R.id.email_type);
            emailPrivacySpinner = itemView.findViewById(R.id.email_privacy_spinner);
        }
    }

}
