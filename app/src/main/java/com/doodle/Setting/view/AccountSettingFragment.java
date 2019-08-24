package com.doodle.Setting.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.doodle.R;
import com.doodle.Setting.adapter.EmailAdapter;
import com.doodle.Setting.model.AccountSetting;
import com.doodle.Setting.model.AllEmail;
import com.doodle.Setting.model.Email;
import com.doodle.Setting.model.Question;
import com.doodle.Setting.service.SettingService;
import com.doodle.utils.PrefManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountSettingFragment extends Fragment {

    View view;
    private Toolbar toolbar;
    private LinearLayout securityQuestionChangeLayout, securityQuestionViewLayout, securityQuestionEditLayout, passwordChangeLayout, passwordEditLayout;
    private TextView tvSecurityQuestion, tvSecurityQuestionAnswer, tvSecurityQuestionList, tvAddNewEmail;
    private EditText etSecurityAnswer, etOldPassword, etNewPassword, etRetypeNewPassword;
    private Spinner securityQuestionListSpinner;
    private Button btnCancel, btnSave, btnPasswordCancel, btnPasswordReset, btnDeactivate;
    private RecyclerView emailRecyclerView;

    private ProgressDialog progressDialog;
    private AccountSetting accountSetting;
    private ArrayList<Email> emails;
    private ArrayList<String> questions;
    private EmailAdapter emailAdapter;
    private SettingService settingService;
    private PrefManager manager;
    private String deviceId, token, userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.account_setting_fragment_layout, container, false);

        initialComponent();
        sendAccountViewRequest();
        sendEmailsRequest();

        return view;
    }

    private void initialComponent() {
        accountSetting = new AccountSetting();
        emails = new ArrayList<>();
        questions = new ArrayList<>();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);

        settingService = SettingService.mRetrofit.create(SettingService.class);
        manager = new PrefManager(getContext());
        deviceId = manager.getDeviceId();
        token = manager.getToken();
        userId = manager.getProfileId();

        toolbar = view.findViewById(R.id.toolbar);
        securityQuestionChangeLayout = view.findViewById(R.id.security_question_change_layout);
        securityQuestionViewLayout = view.findViewById(R.id.security_question_view_layout);
        securityQuestionEditLayout = view.findViewById(R.id.security_question_edit_layout);
        passwordChangeLayout = view.findViewById(R.id.password_change_layout);
        passwordEditLayout = view.findViewById(R.id.password_edit_layout);

        tvSecurityQuestion = view.findViewById(R.id.security_question);
        tvSecurityQuestionAnswer = view.findViewById(R.id.security_question_answer);
        tvSecurityQuestionList = view.findViewById(R.id.security_question_list);
        tvAddNewEmail = view.findViewById(R.id.add_new_email);

        etSecurityAnswer = view.findViewById(R.id.security_answer);
        etOldPassword = view.findViewById(R.id.old_password);
        etNewPassword = view.findViewById(R.id.new_password);
        etRetypeNewPassword = view.findViewById(R.id.retype_new_password);

        securityQuestionListSpinner = view.findViewById(R.id.security_question_list_spinner);

        btnCancel = view.findViewById(R.id.cancel);
        btnSave = view.findViewById(R.id.save);
        btnPasswordCancel = view.findViewById(R.id.password_cancel);
        btnPasswordReset = view.findViewById(R.id.password_reset);
        btnDeactivate = view.findViewById(R.id.deactivate);

        emailRecyclerView = view.findViewById(R.id.email_recycler_view);
        emailRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        emailAdapter = new EmailAdapter(getActivity(), emails);
        emailRecyclerView.setAdapter(emailAdapter);

        securityQuestionChangeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                securityQuestionViewLayout.setVisibility(View.GONE);
                securityQuestionEditLayout.setVisibility(View.VISIBLE);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                securityQuestionViewLayout.setVisibility(View.VISIBLE);
                securityQuestionEditLayout.setVisibility(View.GONE);
            }
        });

        passwordChangeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordEditLayout.setVisibility(View.VISIBLE);
            }
        });

        btnPasswordCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordEditLayout.setVisibility(View.GONE);
            }
        });

    }

    private void setData() {
        if (accountSetting.getProfile().getSecurityQuestion().equals("null")) {
            securityQuestionViewLayout.setVisibility(View.GONE);
            tvSecurityQuestion.setText("");
            tvSecurityQuestionAnswer.setText("");
        } else {
            securityQuestionViewLayout.setVisibility(View.VISIBLE);
            tvSecurityQuestion.setText(accountSetting.getProfile().getSecurityQuestion());
            tvSecurityQuestionAnswer.setText(accountSetting.getProfile().getSecurityAnswer());
        }

        for (Question question : accountSetting.getQuestions()) {
            questions.add(question.getQuestion());
        }

        ArrayAdapter<String> questionAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, questions);
        questionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        securityQuestionListSpinner.setAdapter(questionAdapter);

        securityQuestionListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tvSecurityQuestionList.setText(accountSetting.getQuestions().get(i).getQuestion());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void sendAccountViewRequest() {
        Call<AccountSetting> call = settingService.getAccountView(deviceId, userId, token, userId);
        call.enqueue(new Callback<AccountSetting>() {
            @Override
            public void onResponse(Call<AccountSetting> call, Response<AccountSetting> response) {
                accountSetting = response.body();
                if (accountSetting != null) {
                    setData();
                }
            }

            @Override
            public void onFailure(Call<AccountSetting> call, Throwable t) {

            }
        });
    }

    private void sendEmailsRequest() {
        Call<AllEmail> call = settingService.getEmails(deviceId, userId, token, userId);
        call.enqueue(new Callback<AllEmail>() {
            @Override
            public void onResponse(Call<AllEmail> call, Response<AllEmail> response) {
                AllEmail allEmail = response.body();
                if (allEmail != null) {
                    emails.addAll(allEmail.getEmails());
                    emailAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<AllEmail> call, Throwable t) {

            }
        });
    }

}
