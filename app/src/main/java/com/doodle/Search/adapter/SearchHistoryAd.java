package com.doodle.Search.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.doodle.App;
import com.doodle.Search.model.SearchHistory;
import com.doodle.R;

import java.util.List;

public class SearchHistoryAd extends ArrayAdapter<SearchHistory> {

    private List<SearchHistory> mSearchHistory;
    private LayoutInflater mInflater;


    public SearchHistoryAd(@NonNull Context context, @NonNull List<SearchHistory> objects) {
        super(context, R.layout.advance_search_history_item, objects);
        mSearchHistory = objects;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.advance_search_history_item, parent, false);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.tvSearchData);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imgSearch);

        SearchHistory item = mSearchHistory.get(position);

        tvName.setText(item.searchText);
        imageView.setImageResource(R.drawable.ic_search_black_24dp);
        return convertView;
    }
}