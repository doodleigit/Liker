package com.doodle.Profile.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doodle.Profile.adapter.StarAdapter;
import com.doodle.R;

import java.util.ArrayList;

public class StarFragment extends Fragment {

    View view;
    TextView tvUserName;
    RecyclerView recyclerView;

    ArrayList<String> arrayList;
    StarAdapter starAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.star_fragment_layout, container, false);

        initialComponent();

        return view;
    }

    private void initialComponent() {
        arrayList = new ArrayList<>();
        starAdapter = new StarAdapter(getActivity(), arrayList);

        tvUserName = view.findViewById(R.id.user_name);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(starAdapter);
    }

}
