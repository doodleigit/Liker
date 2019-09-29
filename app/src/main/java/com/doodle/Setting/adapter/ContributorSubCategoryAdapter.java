package com.doodle.Setting.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.doodle.R;
import com.doodle.Setting.model.SubCategory;
import com.doodle.Setting.service.ContributionAddListener;
import com.doodle.Setting.service.SettingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContributorSubCategoryAdapter extends RecyclerView.Adapter<ContributorSubCategoryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<SubCategory> arrayList;
    private SettingService settingService;
    private String categoryId, deviceId, token, userId;
    private ProgressDialog progressDialog;
    private ContributionAddListener contributionAddListener;

    public ContributorSubCategoryAdapter(Context context, ArrayList<SubCategory> arrayList, ContributionAddListener contributionAddListener, ProgressDialog progressDialog, SettingService settingService, String categoryId, String deviceId, String token, String userId) {
        this.context = context;
        this.arrayList = arrayList;
        this.contributionAddListener = contributionAddListener;
        this.progressDialog = progressDialog;
        this.settingService = settingService;
        this.categoryId = categoryId;
        this.deviceId = deviceId;
        this.token = token;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.filter_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvName.setText(arrayList.get(i).getName());

        if (arrayList.get(i).getIsSelected()) {
            viewHolder.ivAdd.setImageResource(R.drawable.ok);
        } else {
            viewHolder.ivAdd.setImageResource(R.drawable.plus);
        }

        viewHolder.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arrayList.get(i).getIsSelected()) {
                    String catId, subCatId, name, actionType;
                    catId = arrayList.get(i).getId();
                    subCatId = "0";
                    name = arrayList.get(i).getName();
                    actionType = "remove";
                    sendContributorAddRequest(catId, subCatId, name, actionType, false, i);
                } else {
                    String catId, subCatId, name, actionType;
                    catId = arrayList.get(i).getId();
                    subCatId = "0";
                    name = arrayList.get(i).getName();
                    actionType = "add";
                    sendContributorAddRequest(catId, subCatId, name, actionType, true, i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout mainLayout;
        TextView tvName;
        ImageView ivAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mainLayout = itemView.findViewById(R.id.main_layout);
            tvName = itemView.findViewById(R.id.name);
            ivAdd = itemView.findViewById(R.id.add);
        }
    }

    private void sendContributorAddRequest(String catId, String subCatId, String name, String actionType, boolean add, int position) {
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
                        arrayList.get(position).setIsSelected(add);
                        notifyItemChanged(position);
                        if (add) {
                            contributionAddListener.onContributionAddListener(catId, name);
                        } else {
                            contributionAddListener.onContributionRemoveListener(catId, name);
                        }
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
