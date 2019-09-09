package com.doodle.Authentication.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.doodle.App;
import com.doodle.Authentication.model.LoginUser;
import com.doodle.Authentication.model.UserInfo;
import com.doodle.Authentication.service.AuthService;
import com.doodle.Authentication.view.fragment.ResendEmail;
import com.doodle.Home.view.activity.Home;
import com.doodle.R;
import com.doodle.Tool.ClearableEditText;
import com.doodle.Tool.NetworkHelper;
import com.doodle.Tool.PrefManager;
import com.doodle.Tool.Tools;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;
import com.onesignal.OneSignal;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.doodle.Tool.AppConstants.POST_IMAGES;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private ClearableEditText etEmail;
    private EditText etPassword;
    private String email, password;
    private TextView tvForgot;
    private Button btnSignIn;
    private PrefManager manager;
    private String mDeviceId;
    private boolean networkOk;
    private CircularProgressView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);

        manager = new PrefManager(this);
        networkOk = NetworkHelper.hasNetworkAccess(this);
        findViewById(R.id.fbLogin).setOnClickListener(this);
        findViewById(R.id.twitterLogin).setOnClickListener(this);
        etEmail = (ClearableEditText) findViewById(R.id.etEmail);
        findViewById(R.id.etEmail).setOnClickListener(this);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvForgot = (TextView) findViewById(R.id.tvForgot);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(this);
        progressView = (CircularProgressView) findViewById(R.id.progress_view);
        findViewById(R.id.tvForgot).setOnClickListener(this);
        findViewById(R.id.imgAbout).setOnClickListener(this);
        findViewById(R.id.tvCancel).setOnClickListener(this);

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                Log.d("debug", "User:" + userId);
                manager.setDeviceId(userId);
                if (registrationId != null)
                    Log.d("debug", "registrationId:" + registrationId);

            }
        });


        etEmail.setDrawableClickListener(new ClearableEditText.DrawableClickListener() {
            @Override
            public void onClick() {
                etEmail.setText(null);
            }
        });

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                email = etEmail.getText().toString();


                if (!TextUtils.isEmpty(password) & !TextUtils.isEmpty(email)) {
                    tvForgot.setTextColor(Color.parseColor("#1485CC"));
                    btnSignIn.setBackgroundResource(R.drawable.btn_round_outline);
                    btnSignIn.setEnabled(true);

                } else {
                    btnSignIn.setBackgroundResource(R.drawable.btn_round_outline_disable);
                    tvForgot.setTextColor(Color.parseColor("#89C3E7"));
                    btnSignIn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //  viewModel.validateEmailField(etEmail);
            }
        });


        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password = etPassword.getText().toString();
                if (!TextUtils.isEmpty(password) & !TextUtils.isEmpty(email)) {
                    tvForgot.setTextColor(Color.parseColor("#1485CC"));
                    btnSignIn.setBackgroundResource(R.drawable.btn_round_outline);
                    btnSignIn.setEnabled(true);

                } else {
                    btnSignIn.setBackgroundResource(R.drawable.btn_round_outline_disable);
                    tvForgot.setTextColor(Color.parseColor("#89C3E7"));
                    btnSignIn.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                /// bitForMessageSave = 1;

                //    viewModel.validatePasswordField(etPassword);

            }
        });

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.fbLogin:
                startActivity(new Intent(this, FBLogin.class));
                break;
            case R.id.twitterLogin:
                startActivity(new Intent(this, MyTwitter.class));
                finish();
                break;
            case R.id.etEmail:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                break;
            case R.id.etPassword:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                break;
            case R.id.btnSignIn:
                if (networkOk) {
                    progressView.setVisibility(View.VISIBLE);
                    progressView.startAnimation();
                    mDeviceId = manager.getDeviceId();
                    requestData(email, password, mDeviceId);

                } else {
                    Tools.showNetworkDialog(getSupportFragmentManager());
                    progressView.setVisibility(View.GONE);
                    progressView.stopAnimation();


                }
                break;

            case R.id.tvForgot:
            /*    if (flipperId == 0) {
                    flipperId++;
                    mViewFlipper.setInAnimation(slideLeftIn);
                    mViewFlipper.setOutAnimation(slideLeftOut);
                    mViewFlipper.showNext();

                }*/
                startActivity(new Intent(this, ForgotPassword.class));
                finish();

                break;

            case R.id.imgAbout:
                startActivity(new Intent(this, About.class));
                break;


        }

    }

    private void requestData(String email, String password, String mDeviceId) {
        AuthService webService =
                AuthService.retrofitBase.create(AuthService.class);

        Call<LoginUser> call = webService.loginUser(email, password, mDeviceId);
        sendRequest(call);


    }

    UserInfo userInfo;

    private void sendRequest(Call<LoginUser> call) {
        call.enqueue(new Callback<LoginUser>() {
            @Override
            public void onResponse(Call<LoginUser> call, Response<LoginUser> response) {

                LoginUser loginUser = response.body();
                boolean status = loginUser.isStatus();

                if (status) {
                    String mToken = loginUser.getToken();
                    manager.setToken(mToken);
                    showSnackbar("login success!");
                    UserInfo userInfo = loginUser.getUserInfo();
                    Gson gson = new Gson();
                    String json = gson.toJson(userInfo);
                    manager.setUserInfo(json);
                    String profileName = userInfo.getFirstName() + " " + userInfo.getLastName();
                    String userName = userInfo.getUserName();
                    String photo = userInfo.getPhoto();
                    App.setProfilePhoto(photo);
                    String profileId = userInfo.getUserId();
                    manager.setProfileName(profileName);
                    manager.setProfileImage(POST_IMAGES + photo);
                    manager.setProfileId(profileId);
                    manager.setUserName(userName);
                    //   startActivity(new Intent(ForgotPassword.this, Liker.class));
//                    Intent intent=new Intent(ForgotPassword.this,Home.class);
//                    intent.putExtra(USER_INFO_ITEM_KEY, (Parcelable) userInfo);
//                    startActivity(intent);

                    startActivity(new Intent(Login.this, Home.class));
                    finish();
                } else {
                    String msg = "Username and password miss match";
                    showStatus(msg);

                    if (loginUser.getIsVerified() != null && loginUser.getBounceData() != null) {
                        if (loginUser.getIsVerified().equalsIgnoreCase("0") && loginUser.getBounceData().equalsIgnoreCase("0")) {
                            userInfo = loginUser.getUserInfo();
                            if (userInfo != null) {
                                String message = "A verification email has been sent to your email address. Please confirm and complete your registration.";
                                ResendEmail resendEmail = ResendEmail.newInstance(message);
                                resendEmail.show(getSupportFragmentManager(), "ResendEmail");
                            }

                        } else if (loginUser.getBounceData().equalsIgnoreCase(String.valueOf(1)) || loginUser.getBounceData().equalsIgnoreCase(String.valueOf(2))) {
                            Toast.makeText(Login.this, "Email is invalid", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                progressView.setVisibility(View.GONE);
                progressView.stopAnimation();

            }

            @Override
            public void onFailure(Call<LoginUser> call, Throwable t) {
                Log.d("message", t.getMessage());
                progressView.setVisibility(View.GONE);
                progressView.stopAnimation();

            }
        });
    }


    public void showSnackbar(String message) {
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.signupContainer), message, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });

// Changing message text color
        snackbar.setActionTextColor(Color.RED);

// Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    private void showStatus(String message) {
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.signupContainer), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

}
