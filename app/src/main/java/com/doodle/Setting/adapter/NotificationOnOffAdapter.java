package com.doodle.Setting.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.doodle.R;
import com.doodle.Setting.model.Status;
import com.doodle.Setting.service.SettingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationOnOffAdapter extends RecyclerView.Adapter<NotificationOnOffAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Status> arrayList;
    private ProgressDialog progressDialog;
    private SettingService settingService;
    private String deviceId, token, userId;
    private boolean isChangeAll;

    public NotificationOnOffAdapter(Context context, ArrayList<Status> arrayList, ProgressDialog progressDialog, SettingService settingService,
                                    String deviceId, String token, String userId, boolean isChangeAll) {
        this.context = context;
        this.arrayList = arrayList;
        this.progressDialog = progressDialog;
        this.settingService = settingService;
        this.deviceId = deviceId;
        this.token = token;
        this.userId = userId;
        this.isChangeAll = isChangeAll;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_notification_setting_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvPrivacyTypeName.setText(getTitleFromKey(arrayList.get(i).getNotificationName()));
        viewHolder.tvPushPrivacyStatus.setText(String.valueOf(arrayList.get(i).getPush()).equals("1") ? "On" : "Off");
        viewHolder.tvEmailPrivacyStatus.setText(String.valueOf(arrayList.get(i).getEmail()).equals("1") ? "On" : "Off");
        viewHolder.tvBrowserPrivacyStatus.setText(String.valueOf(arrayList.get(i).getBrowser()).equals("1") ? "On" : "Off");

        viewHolder.cbPushPrivacyCheckBox.setChecked(String.valueOf(arrayList.get(i).getPush()).equals("1"));
        viewHolder.cbEmailPrivacyCheckBox.setChecked(String.valueOf(arrayList.get(i).getEmail()).equals("1"));
        viewHolder.cbBrowserPrivacyCheckBox.setChecked(String.valueOf(arrayList.get(i).getBrowser()).equals("1"));

        if (isChangeAll) {
            viewHolder.ivEdit.setVisibility(View.GONE);
            viewHolder.ivSave.setVisibility(View.VISIBLE);
            viewHolder.ivCancel.setVisibility(View.VISIBLE);

            viewHolder.tvPrivacyTypeName.setVisibility(View.GONE);
            viewHolder.tvEmailPrivacyStatus.setVisibility(View.GONE);
            viewHolder.tvBrowserPrivacyStatus.setVisibility(View.GONE);
            viewHolder.cbPushPrivacyCheckBox.setVisibility(View.VISIBLE);
            viewHolder.cbEmailPrivacyCheckBox.setVisibility(View.VISIBLE);
            viewHolder.cbBrowserPrivacyCheckBox.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivEdit.setVisibility(View.VISIBLE);
            viewHolder.ivSave.setVisibility(View.GONE);
            viewHolder.ivCancel.setVisibility(View.GONE);

            viewHolder.tvPrivacyTypeName.setVisibility(View.VISIBLE);
            viewHolder.tvEmailPrivacyStatus.setVisibility(View.VISIBLE);
            viewHolder.tvBrowserPrivacyStatus.setVisibility(View.VISIBLE);
            viewHolder.cbPushPrivacyCheckBox.setVisibility(View.GONE);
            viewHolder.cbEmailPrivacyCheckBox.setVisibility(View.GONE);
            viewHolder.cbBrowserPrivacyCheckBox.setVisibility(View.GONE);
        }

        viewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.ivEdit.setVisibility(View.GONE);
                viewHolder.ivSave.setVisibility(View.VISIBLE);
                viewHolder.ivCancel.setVisibility(View.VISIBLE);

                viewHolder.tvPrivacyTypeName.setVisibility(View.GONE);
                viewHolder.tvEmailPrivacyStatus.setVisibility(View.GONE);
                viewHolder.tvBrowserPrivacyStatus.setVisibility(View.GONE);
                viewHolder.cbPushPrivacyCheckBox.setVisibility(View.VISIBLE);
                viewHolder.cbEmailPrivacyCheckBox.setVisibility(View.VISIBLE);
                viewHolder.cbBrowserPrivacyCheckBox.setVisibility(View.VISIBLE);
            }
        });

        viewHolder.ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.ivEdit.setVisibility(View.VISIBLE);
                viewHolder.ivSave.setVisibility(View.GONE);
                viewHolder.ivCancel.setVisibility(View.GONE);

                viewHolder.tvPrivacyTypeName.setVisibility(View.VISIBLE);
                viewHolder.tvEmailPrivacyStatus.setVisibility(View.VISIBLE);
                viewHolder.tvBrowserPrivacyStatus.setVisibility(View.VISIBLE);
                viewHolder.cbPushPrivacyCheckBox.setVisibility(View.GONE);
                viewHolder.cbEmailPrivacyCheckBox.setVisibility(View.GONE);
                viewHolder.cbBrowserPrivacyCheckBox.setVisibility(View.GONE);
            }
        });

        viewHolder.ivSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String privacyName = arrayList.get(i).getNotificationName();
                int pushStatus, emailStatus, browserStatus;
                ArrayList<String> onOffs = new ArrayList<>();
                if (viewHolder.cbPushPrivacyCheckBox.isChecked()) {
                    onOffs.add("push");
                    pushStatus = 1;
                } else {
                    pushStatus = 0;
                }
                if (viewHolder.cbEmailPrivacyCheckBox.isChecked()) {
                    onOffs.add("email");
                    emailStatus = 1;
                } else {
                    emailStatus = 0;
                }
                if (viewHolder.cbBrowserPrivacyCheckBox.isChecked()) {
                    onOffs.add("browser");
                    browserStatus = 1;
                } else {
                    browserStatus = 0;
                }
                sendNotificationOnOffRequest(privacyName, onOffs, i, pushStatus, emailStatus, browserStatus);
//                singleNotificationUpdateListener.onUpdateClick(privacyName, onOffs);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private String getTitleFromKey(String key) {
        StringBuilder title = new StringBuilder();
        String initialKey = key.replace("_" , " ");
        String[] wordArray = initialKey.split(" ");
        for (String s : wordArray) {
            title.append(Character.toUpperCase(s.charAt(0))).append(s.substring(1));
        }
        return title.toString();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvPrivacyTypeName, tvPushPrivacyStatus, tvEmailPrivacyStatus, tvBrowserPrivacyStatus;
        CheckBox cbPushPrivacyCheckBox, cbEmailPrivacyCheckBox, cbBrowserPrivacyCheckBox;
        ImageView ivEdit, ivSave, ivCancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPrivacyTypeName = itemView.findViewById(R.id.privacy_type_name);
            tvPushPrivacyStatus = itemView.findViewById(R.id.push_privacy_status);
            tvEmailPrivacyStatus = itemView.findViewById(R.id.email_privacy_status);
            tvBrowserPrivacyStatus = itemView.findViewById(R.id.browser_privacy_status);
            cbPushPrivacyCheckBox = itemView.findViewById(R.id.push_privacy_check_box);
            cbEmailPrivacyCheckBox = itemView.findViewById(R.id.email_privacy_check_box);
            cbBrowserPrivacyCheckBox = itemView.findViewById(R.id.browser_privacy_check_box);
            ivEdit = itemView.findViewById(R.id.edit);
            ivSave = itemView.findViewById(R.id.save);
            ivCancel = itemView.findViewById(R.id.cancel);
        }
    }

    private void sendNotificationOnOffRequest(String singleStatus, ArrayList<String> onOffs, int position, int pushStatus, int emailStatus, int browserStatus) {
        progressDialog.show();
        Call<String> call = settingService.setNotificationOnOff(deviceId, userId, token, userId, singleStatus, onOffs);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean status = obj.getBoolean("status");
                    if (status) {
                        arrayList.get(position).setPush(pushStatus);
                        arrayList.get(position).setEmail(emailStatus);
                        arrayList.get(position).setBrowser(browserStatus);
                        notifyItemChanged(position);
                    } else {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.hide();
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
