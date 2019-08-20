package com.doodle.Setting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.doodle.R;
import com.doodle.Setting.model.PrivacyType;

import java.util.ArrayList;

public class CustomPrivacySpinnerAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflter;
    private ArrayList<PrivacyType> arrayList;

    public CustomPrivacySpinnerAdapter(Context applicationContext, ArrayList<PrivacyType> arrayList) {
        this.context = applicationContext;
        this.arrayList = arrayList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.privacy_type_item, null);
        ImageView icon = view.findViewById(R.id.privacy_type_image);
        TextView names = view.findViewById(R.id.privacy_type);
        icon.setImageResource(arrayList.get(i).getPrivacyTypeImage());
        names.setText(arrayList.get(i).getPrivacyType());
        return view;
    }
}