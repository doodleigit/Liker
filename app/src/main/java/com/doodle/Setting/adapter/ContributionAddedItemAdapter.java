package com.doodle.Setting.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.doodle.R;
import com.doodle.Setting.model.AddedCategory;
import com.doodle.Setting.service.SettingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContributionAddedItemAdapter extends RecyclerView.Adapter<ContributionAddedItemAdapter.ViewHolder> {

    private Context context;
    private ArrayList<AddedCategory> arrayList;
    private SettingService settingService;
    private String deviceId, token, userId;
    private ProgressDialog progressDialog;

    public ContributionAddedItemAdapter(Context context, ArrayList<AddedCategory> arrayList, ProgressDialog progressDialog, SettingService settingService, String deviceId, String token, String userId) {
        this.context = context;
        this.arrayList = arrayList;
        this.progressDialog = progressDialog;
        this.settingService = settingService;
        this.deviceId = deviceId;
        this.token = token;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_title_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvCatName.setText(arrayList.get(i).getName());
        viewHolder.mainLayout.setBackgroundResource(R.drawable.rectangle_corner_round_five);
        viewHolder.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String catId, subCatId, actionType;
                catId = arrayList.get(i).getId();
                subCatId = "0";
                actionType = "remove";

                sendContributorRemoveRequest(catId, subCatId, actionType, i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mainLayout;
        TextView tvCatName;
        ImageView ivClose;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mainLayout = itemView.findViewById(R.id.main_layout);
            tvCatName = itemView.findViewById(R.id.categoryName);
            ivClose = itemView.findViewById(R.id.close);
        }
    }

    private void sendContributorRemoveRequest(String catId, String subCatId, String actionType, int position) {
        progressDialog.show();
        Call<String> call = settingService.setContributorCategory(deviceId, userId, token, userId, catId, actionType, subCatId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean status = obj.getBoolean("status");
                    if (status) {
                        arrayList.remove(position);
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, context.getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.hide();
                Toast.makeText(context, context.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
