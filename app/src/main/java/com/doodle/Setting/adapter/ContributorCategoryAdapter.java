package com.doodle.Setting.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.doodle.R;
import com.doodle.Setting.model.Category;
import com.doodle.Setting.service.ContributionAddListener;
import com.doodle.Setting.service.SettingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContributorCategoryAdapter extends RecyclerView.Adapter<ContributorCategoryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Category> arrayList;
    private SettingService settingService;
    private String deviceId, token, userId;
    private ProgressDialog progressDialog;
    private ContributionAddListener contributionAddListener;

    public ContributorCategoryAdapter(Context context, ArrayList<Category> arrayList, ContributionAddListener contributionAddListener, ProgressDialog progressDialog, SettingService settingService, String deviceId, String token, String userId) {
        this.context = context;
        this.arrayList = arrayList;
        this.contributionAddListener = contributionAddListener;
        this.progressDialog = progressDialog;
        this.settingService = settingService;
        this.deviceId = deviceId;
        this.token = token;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.subcategory_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvName.setText(arrayList.get(i).getInfo().getName());

        if (arrayList.get(i).getInfo().getIsSelected()) {
            viewHolder.ivAdd.setImageResource(R.drawable.ok);
        } else {
            viewHolder.ivAdd.setImageResource(R.drawable.plus);
        }

        ContributorSubCategoryAdapter contributorSubCategoryAdapter = new ContributorSubCategoryAdapter(context, arrayList.get(i).getSubCategories(), contributionAddListener, progressDialog, settingService, arrayList.get(i).getInfo().getId(), deviceId, token, userId);
        viewHolder.recyclerView.setAdapter(contributorSubCategoryAdapter);

        viewHolder.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arrayList.get(i).getInfo().getIsSelected()) {
                    String catId, subCatId, name, actionType;
                    catId = arrayList.get(i).getInfo().getId();
                    subCatId = "0";
                    name = arrayList.get(i).getInfo().getName();
                    actionType = "remove";
                    sendContributorAddRequest(catId, subCatId, name, actionType, false, i);
                } else {
                    String catId, subCatId, name, actionType;
                    catId = arrayList.get(i).getInfo().getId();
                    subCatId = "0";
                    name = arrayList.get(i).getInfo().getName();
                    actionType = "add";
                    sendContributorAddRequest(catId, subCatId, name, actionType, true, i);
                }
            }
        });

        viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.recyclerView.getVisibility() == View.VISIBLE) {
                    viewHolder.recyclerView.setVisibility(View.GONE);
                    viewHolder.ivArrow.setImageResource(R.drawable.arrow_right);
                } else {
                    viewHolder.recyclerView.setVisibility(View.VISIBLE);
                    viewHolder.ivArrow.setImageResource(R.drawable.arrow_down);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mainLayout;
        TextView tvName;
        ImageView ivArrow, ivAdd;
        RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mainLayout = itemView.findViewById(R.id.main_layout);
            tvName = itemView.findViewById(R.id.name);
            ivArrow = itemView.findViewById(R.id.arrow);
            ivAdd = itemView.findViewById(R.id.add);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setNestedScrollingEnabled(false);
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
                        arrayList.get(position).getInfo().setIsSelected(add);
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
