package com.doodle.Setting.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.doodle.R;
import com.doodle.Setting.model.BlockUser;
import com.doodle.Setting.service.UserBlockClickListener;

import java.util.ArrayList;

public class BlockUserAdapter extends RecyclerView.Adapter<BlockUserAdapter.ViewHolder> {

    private Context context;
    private ArrayList<BlockUser> arrayList;
    private UserBlockClickListener userBlockClickListener;

    public BlockUserAdapter(Context context, ArrayList<BlockUser> arrayList, UserBlockClickListener userBlockClickListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.userBlockClickListener = userBlockClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_block_user, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.tvUserName.setText(arrayList.get(i).getFirstName() + " " + arrayList.get(i).getLastName());
        viewHolder.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userBlockClickListener.onUnBlockClick(arrayList.get(i), i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvUserName;
        ImageView ivRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUserName = itemView.findViewById(R.id.user_name);
            ivRemove = itemView.findViewById(R.id.remove);
        }
    }

}
