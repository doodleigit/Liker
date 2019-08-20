package com.doodle.Profile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.doodle.Profile.model.Email;
import com.doodle.Profile.service.EmailModificationListener;
import com.doodle.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddEmailAdapter extends RecyclerView.Adapter<AddEmailAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Email> arrayList;
    private List<String> emailTypes;
    private EmailModificationListener emailModificationListener;

    public AddEmailAdapter(Context context, ArrayList<Email> arrayList, EmailModificationListener emailModificationListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.emailTypes = Arrays.asList(context.getResources().getStringArray(R.array.phone_type_list));
        this.emailModificationListener = emailModificationListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_add_email, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvEmail.setText(arrayList.get(i).getEmail());
        viewHolder.tvEmailType.setText(emailTypes.get(Integer.valueOf(arrayList.get(i).getType()) - 1));

        viewHolder.tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailModificationListener.onEmailRemove(arrayList.get(i), i);
            }
        });
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
