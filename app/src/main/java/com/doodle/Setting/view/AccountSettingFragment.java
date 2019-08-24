package com.doodle.Setting.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import android.widget.Toast;

import com.doodle.R;
import com.doodle.Search.LikerSearch;
import com.doodle.Setting.adapter.EmailAdapter;
import com.doodle.Setting.model.AccountSetting;
import com.doodle.Setting.model.AllEmail;
import com.doodle.Setting.model.Email;
import com.doodle.Setting.model.Question;
import com.doodle.Setting.service.EmailModificationListener;
import com.doodle.Setting.service.SettingService;
import com.doodle.utils.PrefManager;
import com.doodle.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountSettingFragment extends Fragment {

    View view;
    private Toolbar toolbar;
    private LinearLayout securityQuestionChangeLayout, securityQuestionViewLayout, securityQuestionEditLayout, addEmailLayout, passwordChangeLayout, passwordEditLayout;
    private TextView tvSecurityQuestion, tvSecurityQuestionAnswer, tvSecurityQuestionList, tvAddNewEmail, tvEmailVerificationMessage;
    private EditText etSecurityAnswer, etNewEmail, etOldPassword, etNewPassword, etRetypeNewPassword;
    private Spinner securityQuestionListSpinner, emailPrivacySpinner;
    private Button btnCancel, btnSave, btnEmailSave, btnEmailCancel, btnPasswordCancel, btnPasswordReset, btnDeactivate;
    private RecyclerView emailRecyclerView;

    private ProgressDialog progressDialog;
    private AccountSetting accountSetting;
    private ArrayList<Email> emails;
    private ArrayList<String> questions;
    private List<String> privacyList;
    private EmailAdapter emailAdapter;
    private SettingService settingService;
    private PrefManager manager;
    private String deviceId, token, userId;
    private String question, questionId, emailPrivacy;

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
        privacyList = Arrays.asList(getResources().getStringArray(R.array.privacy_list));

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
        addEmailLayout = view.findViewById(R.id.add_email_layout);
        passwordChangeLayout = view.findViewById(R.id.password_change_layout);
        passwordEditLayout = view.findViewById(R.id.password_edit_layout);

        tvSecurityQuestion = view.findViewById(R.id.security_question);
        tvSecurityQuestionAnswer = view.findViewById(R.id.security_question_answer);
        tvSecurityQuestionList = view.findViewById(R.id.security_question_list);
        tvAddNewEmail = view.findViewById(R.id.add_new_email);
        tvEmailVerificationMessage = view.findViewById(R.id.email_verification_message);

        etSecurityAnswer = view.findViewById(R.id.security_answer);
        etNewEmail = view.findViewById(R.id.new_email);
        etOldPassword = view.findViewById(R.id.old_password);
        etNewPassword = view.findViewById(R.id.new_password);
        etRetypeNewPassword = view.findViewById(R.id.retype_new_password);

        securityQuestionListSpinner = view.findViewById(R.id.security_question_list_spinner);
        emailPrivacySpinner = view.findViewById(R.id.email_privacy_spinner);

        btnCancel = view.findViewById(R.id.cancel);
        btnSave = view.findViewById(R.id.save);
        btnEmailSave = view.findViewById(R.id.email_save);
        btnEmailCancel = view.findViewById(R.id.email_cancel);
        btnPasswordCancel = view.findViewById(R.id.password_cancel);
        btnPasswordReset = view.findViewById(R.id.password_reset);
        btnDeactivate = view.findViewById(R.id.deactivate);

        emailRecyclerView = view.findViewById(R.id.email_recycler_view);
        emailRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        EmailModificationListener emailModificationListener = new EmailModificationListener() {
            @Override
            public void onEmailRemove(Email email, int position) {
                Call<String> call = settingService.removeEmail(deviceId, token, userId, userId, email.getEmail());
                removeEmail(call, position, emailRecyclerView);
            }

            @Override
            public void onEmailResendVerification(Email email) {
                Call<String> call = settingService.emailVerificationSend(deviceId, token, userId, userId, email.getEmail());
                emailVerificationSend(call, email);
            }
        };

        emailAdapter = new EmailAdapter(getActivity(), emails, emailModificationListener);
        emailRecyclerView.setAdapter(emailAdapter);

        ArrayAdapter<String> privacyAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, privacyList);
        privacyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        emailPrivacySpinner.setAdapter(privacyAdapter);

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
                etSecurityAnswer.setText("");
                securityQuestionViewLayout.setVisibility(View.VISIBLE);
                securityQuestionEditLayout.setVisibility(View.GONE);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etSecurityAnswer.getText().toString().isEmpty()) {
                    Call<String> call = settingService.updateSecurityQuestion(deviceId, token, userId, userId, questionId, etSecurityAnswer.getText().toString());
                    updateSecurityQuestion(call);
                }
            }
        });

        tvAddNewEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvAddNewEmail.setVisibility(View.GONE);
                addEmailLayout.setVisibility(View.VISIBLE);
            }
        });

        emailPrivacySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                emailPrivacy = String.valueOf(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnEmailCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etNewEmail.setText("");
                tvAddNewEmail.setVisibility(View.VISIBLE);
                addEmailLayout.setVisibility(View.GONE);
            }
        });

        btnEmailSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidEmail(etNewEmail.getText().toString())) {
                    addEmailRequest(etNewEmail.getText().toString(), emailPrivacy);
                } else {
                    etNewEmail.setError(getString(R.string.your_email_address_is_not_valid));
                }
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
                etOldPassword.setText("");
                etNewPassword.setText("");
                etRetypeNewPassword.setText("");
                passwordEditLayout.setVisibility(View.GONE);
            }
        });

        btnPasswordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwordFieldValidation("Old", etOldPassword) && passwordFieldValidation("New", etNewPassword) && passwordFieldValidation("New", etRetypeNewPassword)) {
                    if (passwordMatchValidation(etNewPassword, etRetypeNewPassword)) {
                        Call<String> call =  settingService.updatePassword(deviceId, token, userId, userId, etOldPassword.getText().toString(), etNewPassword.getText().toString(), etRetypeNewPassword.getText().toString());
                        updatePassword(call);
                    }
                }
            }
        });

        btnDeactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deactivateAccount();
            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
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
            tvSecurityQuestion.setText(accountSetting.getProfile().getQuestion());
            tvSecurityQuestionAnswer.setText("******");
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
                question = accountSetting.getQuestions().get(i).getQuestion();
                questionId = String.valueOf(i + 1);
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

    private void removeEmail(Call<String> call, int position, RecyclerView recyclerView) {
        progressDialog.show();
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean status = obj.getBoolean("status");
                    if (status) {
                        emails.remove(position);
                        emailAdapter.notifyDataSetChanged();
//                        recyclerView.getAdapter().notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                progressDialog.hide();
            }
        });
    }

    private void updateSecurityQuestion(Call<String> call) {
        progressDialog.show();
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean status = obj.getBoolean("status");
                    if (status) {
                        etSecurityAnswer.setText("******");
                        tvSecurityQuestion.setText(question);
                        securityQuestionViewLayout.setVisibility(View.VISIBLE);
                        securityQuestionEditLayout.setVisibility(View.GONE);
                        Utils.toast(getContext(), getString(R.string.security_question_has_been_updated), R.drawable.ic_check_black_24dp);
                    } else {
                        Utils.toast(getContext(), getString(R.string.something_went_wrong), R.drawable.ic_info_outline_blue_24dp);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                progressDialog.hide();
            }
        });
    }

    private void updatePassword(Call<String> call) {
        progressDialog.show();
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean status = obj.getBoolean("status");
                    if (status) {
                        etOldPassword.setText("");
                        etNewPassword.setText("");
                        etRetypeNewPassword.setText("");
                        passwordEditLayout.setVisibility(View.GONE);
                        Utils.toast(getContext(), getString(R.string.password_has_been_updated), R.drawable.ic_check_black_24dp);
                    } else {
                        Utils.toast(getContext(), getString(R.string.something_went_wrong), R.drawable.ic_info_outline_blue_24dp);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                progressDialog.hide();
            }
        });
    }

    private void emailVerificationSend(Call<String> call, Email email) {
        progressDialog.show();
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean status = obj.getBoolean("status");
                    if (status) {
                        tvEmailVerificationMessage.setText(getString(R.string.a_verification_email_has_been_resent_to_your_email) + " " + email.getEmail() + ". " + getString(R.string.if_you_need_help_please_contact_with_liker_support));
                        Utils.toast(getContext(), getString(R.string.a_verification_email_has_been_resent_to_your_email), R.drawable.ic_check_black_24dp);
                    } else {
                        Utils.toast(getContext(), getString(R.string.something_went_wrong), R.drawable.ic_info_outline_blue_24dp);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                progressDialog.hide();
            }
        });
    }

    private void addEmailRequest(String emailAddress, String emailPrivacyType) {
        progressDialog.show();
        Call<String> call = settingService.addEmail(deviceId, token, userId, userId, emailAddress, emailPrivacyType);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                emails.add(new Email(emailAddress, "2", emailPrivacyType, "0"));
                emailAdapter.notifyDataSetChanged();
                etNewEmail.setText("");
                addEmailLayout.setVisibility(View.GONE);
                tvAddNewEmail.setVisibility(View.VISIBLE);
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.hide();
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deactivatedAccount(Call<String> call) {
        progressDialog.show();
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean status = obj.getBoolean("status");
                    if (status) {
                        Utils.toast(getContext(), getString(R.string.account_has_been_deactivated), R.drawable.ic_check_black_24dp);
                    } else {
                        Utils.toast(getContext(), getString(R.string.something_went_wrong), R.drawable.ic_info_outline_blue_24dp);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                progressDialog.hide();
            }
        });
    }

    private void deactivateAccount() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        alertBuilder.setCancelable(true);
        alertBuilder.setMessage(getString(R.string.are_you_sure_you_want_to_deactivated_your_account));
        alertBuilder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Call<String> call = settingService.deactivatedAccount(deviceId, token, userId, userId);
//                        deactivatedAccount(call);
                    }
                });
        alertBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    private boolean passwordFieldValidation(String type, EditText editText) {
        if (editText.getText().toString().trim().length() < 8) {
            editText.setError(type + " " + getString(R.string.password_length_is_less_than_8));
            return false;
        } else {
            return true;
        }
    }

    private boolean passwordMatchValidation(EditText newPassword, EditText reNewPassword) {
        if (newPassword.getText().toString().equals(reNewPassword.getText().toString())) {
            return true;
        } else {
            reNewPassword.setError(getString(R.string.new_password_confirm_password_does_not_match));
            return false;
        }
    }

    static boolean isValidEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

}
