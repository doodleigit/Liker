package com.doodle.Profile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.doodle.Profile.model.AdvanceSuggestion;
import com.doodle.Profile.service.SuggestionClickListener;
import com.doodle.R;

import java.util.ArrayList;

public class ExperienceSuggestionAdapter extends RecyclerView.Adapter<ExperienceSuggestionAdapter.ViewHolder> {

    private Context context;
    private ArrayList<AdvanceSuggestion> arrayList;
    private SuggestionClickListener suggestionClickListener;

    public ExperienceSuggestionAdapter(Context context, ArrayList<AdvanceSuggestion> arrayList, SuggestionClickListener suggestionClickListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.suggestionClickListener = suggestionClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_suggestion, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvSuggestion.setText(arrayList.get(i).getInstituteName());

        viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suggestionClickListener.onSuggestionClick(arrayList.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        FrameLayout mainLayout;
        TextView tvSuggestion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mainLayout = itemView.findViewById(R.id.main_layout);
            tvSuggestion = itemView.findViewById(R.id.suggestion);
        }
    }

}
