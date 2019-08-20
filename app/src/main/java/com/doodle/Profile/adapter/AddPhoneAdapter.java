package com.doodle.Profile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.doodle.Profile.model.Phone;
import com.doodle.Profile.service.PhoneModificationListener;
import com.doodle.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddPhoneAdapter extends RecyclerView.Adapter<AddPhoneAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Phone> arrayList;
    private List<String> numberTypes;
    private PhoneModificationListener phoneModificationListener;

    public AddPhoneAdapter(Context context, ArrayList<Phone> arrayList, PhoneModificationListener phoneModificationListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.numberTypes = Arrays.asList(context.getResources().getStringArray(R.array.phone_type_list));
        this.phoneModificationListener = phoneModificationListener;
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
        viewHolder.tvPhoneNumber.setText(arrayList.get(i).getPhoneNumber());
        viewHolder.tvNumberType.setText(numberTypes.get(Integer.valueOf(arrayList.get(i).getType()) - 1));

        viewHolder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneModificationListener.onPhoneEdit(arrayList.get(i));
            }
        });

        viewHolder.tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneModificationListener.onPhoneRemove(arrayList.get(i), i);
            }
        });
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
