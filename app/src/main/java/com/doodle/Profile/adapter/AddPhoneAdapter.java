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

public class AddPhoneAdapter extends RecyclerView.Adapter<AddPhoneAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> arrayList;

    public AddPhoneAdapter(Context context, ArrayList<String> arrayList) {
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

        TextView tvPhoneNumber, tvRemove, tvEdit, tvNumberType;
        Spinner phonePrivacySpinner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPhoneNumber = itemView.findViewById(R.id.phone_number);
            tvRemove = itemView.findViewById(R.id.remove);
            tvEdit = itemView.findViewById(R.id.edit);
            tvNumberType = itemView.findViewById(R.id.number_type);
            phonePrivacySpinner = itemView.findViewById(R.id.phone_privacy_spinner);
        }
    }

}
