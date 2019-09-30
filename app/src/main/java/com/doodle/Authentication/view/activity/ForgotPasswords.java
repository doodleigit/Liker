package com.doodle.Authentication.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.chaos.view.PinView;
import com.doodle.App;
import com.doodle.Authentication.model.Error;
import com.doodle.Authentication.model.ForgotPassword;
import com.doodle.Authentication.model.LoginUser;
import com.doodle.Authentication.model.ResendStatus;
import com.doodle.Authentication.model.UserInfo;
import com.doodle.Authentication.view.fragment.ResendEmail;
import com.doodle.Authentication.viewmodel.LoginViewModel;
import com.doodle.Home.view.activity.Home;
import com.doodle.R;
import com.doodle.Tool.ClearableEditText;
import com.doodle.Authentication.service.AuthService;
import com.doodle.Tool.NetworkHelper;
import com.doodle.Tool.PrefManager;
import com.doodle.Tool.Tools;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.doodle.Tool.AppConstants.POST_IMAGES;
import static com.doodle.Tool.AppConstants.PROFILE_IMAGE;

public class ForgotPasswords extends AppCompatActivity implements View.OnClickListener, ResendEmail.BottomSheetListener {


    private ViewFlipper mViewFlipper;
    int flipperId = 0;
    protected Animation slideRightIn;
    protected Animation slideRightOut;
    protected Animation slideLeftIn;
    protected Animation slideLeftOut;

    private EditText /*etPassword,*/ etConfirmPassword, etNewPassword;
    private ClearableEditText/* etEmail,*/ etForgotEmail;
    private String email, forgotEmail, password, confirmPassword, newPassword;
    private TextView tvForgot, tvSignup;
    private Button btnLogin, btnContinue,/* btnSignIn,*/
            btnOTPContinue, btnFinish;
    private static final String TAG = "ForgotPasswords";
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private boolean networkOk;
    LoginViewModel viewModel;
    private PrefManager manager;
    String otp;
    String mDeviceId;
    public static final String USER_INFO_ITEM_KEY = "user_info_item_key";
    private CircularProgressView progressView;

    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_flipper);


        networkOk = NetworkHelper.hasNetworkAccess(this);
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        manager = new PrefManager(this);
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

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.doodle",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        progressBar=findViewById(R.id.progress_bar);

        mViewFlipper = (ViewFlipper) findViewById(R.id.viewFlipperContent);
        //   findViewById(R.id.btnLogin).setOnClickListener(this);
        //  findViewById(R.id.btnSignIn).setOnClickListener(this);
        findViewById(R.id.btnContinue).setOnClickListener(this);
        findViewById(R.id.btnOTPContinue).setOnClickListener(this);
        // findViewById(R.id.imgAbout).setOnClickListener(this);
        findViewById(R.id.imgEmailAbout).setOnClickListener(this);
        findViewById(R.id.imgOTPAbout).setOnClickListener(this);
        findViewById(R.id.imgResetAbout).setOnClickListener(this);
        findViewById(R.id.tvResendOTP).setOnClickListener(this);


        //  findViewById(R.id.fbLogin).setOnClickListener(this);
        //   findViewById(R.id.twitterLogin).setOnClickListener(this);

        //  etPassword = (EditText) findViewById(R.id.etPassword);
        etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        etConfirmPassword.setOnEditorActionListener(editorListener);
        //   etEmail = (ClearableEditText) findViewById(R.id.etEmail);
        etForgotEmail = (ClearableEditText) findViewById(R.id.etForgotEmail);
        etForgotEmail.setOnEditorActionListener(editorListener);
        //tvForgot = (TextView) findViewById(R.id.tvForgot);

        //btnLogin = (Button) findViewById(R.id.btnLogin);
        //  btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnOTPContinue = (Button) findViewById(R.id.btnOTPContinue);
        btnContinue = (Button) findViewById(R.id.btnContinue);
        btnFinish = (Button) findViewById(R.id.btnFinish);
        progressView = (CircularProgressView) findViewById(R.id.progress_view);
        btnFinish.setOnClickListener(this);
        // btnSignIn.setOnClickListener(this);

        //  findViewById(R.id.etPassword).setOnClickListener(this);
        //   findViewById(R.id.etEmail).setOnClickListener(this);
        // findViewById(R.id.tvSignup).setOnClickListener(this);


        //findViewById(R.id.tvForgot).setOnClickListener(this);
        //   findViewById(R.id.tvCancel).setOnClickListener(this);
        findViewById(R.id.imgEmailCancel).setOnClickListener(this);
        findViewById(R.id.imgOTPCancel).setOnClickListener(this);
        findViewById(R.id.imgResetPasswordCancel).setOnClickListener(this);

        slideRightIn = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
        slideRightOut = AnimationUtils.loadAnimation(this, R.anim.slide_right_out);
        slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
        slideLeftOut = AnimationUtils.loadAnimation(this, R.anim.slide_left_out);

        btnContinue.setEnabled(false);
        // tvForgot.setEnabled(false);
        btnOTPContinue.setEnabled(false);
        btnFinish.setEnabled(false);
        //btnSignIn.setEnabled(false);

        final PinView pinView = findViewById(R.id.firstPinView);
        pinView.setOnEditorActionListener(editorListener);

//        etEmail.setDrawableClickListener(new ClearableEditText.DrawableClickListener() {
//            @Override
//            public void onClick() {
//                etEmail.setText(null);
//            }
//        });
        etForgotEmail.setDrawableClickListener(new ClearableEditText.DrawableClickListener() {
            @Override
            public void onClick() {
                etForgotEmail.setText(null);
            }
        });
        pinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged() called with: s = [" + s + "], start = [" + start + "], before = [" + before + "], count = [" + count + "]");
                otp = pinView.getText().toString();
                if (!TextUtils.isEmpty(otp)) {
                    btnOTPContinue.setEnabled(true);
                    btnOTPContinue.setBackgroundResource(R.drawable.btn_round_outline);
                } else {
                    btnOTPContinue.setEnabled(false);
                    btnOTPContinue.setBackgroundResource(R.drawable.btn_round_outline_disable);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newPassword = etNewPassword.getText().toString();


                if (!TextUtils.isEmpty(confirmPassword) && !TextUtils.isEmpty(newPassword)) {

                    btnFinish.setBackgroundResource(R.drawable.btn_round_outline);
                    btnFinish.setEnabled(true);

                } else {
                    btnFinish.setBackgroundResource(R.drawable.btn_round_outline_disable);
                    btnFinish.setEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.validatePasswordField(etNewPassword);
            }
        });
        etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                confirmPassword = etConfirmPassword.getText().toString();


                if (!TextUtils.isEmpty(confirmPassword) && !TextUtils.isEmpty(newPassword)) {
                    btnFinish.setBackgroundResource(R.drawable.btn_round_outline);
                    btnFinish.setEnabled(true);

                } else {
                    btnFinish.setBackgroundResource(R.drawable.btn_round_outline_disable);
                    btnFinish.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.validatePasswordField(etConfirmPassword);
            }
        });

//        etEmail.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                email = etEmail.getText().toString();
//
//
//                if (!TextUtils.isEmpty(password) & !TextUtils.isEmpty(email)) {
//                    tvForgot.setTextColor(Color.parseColor("#1485CC"));
//                    btnSignIn.setBackgroundResource(R.drawable.btn_round_outline);
//                    btnSignIn.setEnabled(true);
//
//                } else {
//                    btnSignIn.setBackgroundResource(R.drawable.btn_round_outline_disable);
//                    tvForgot.setTextColor(Color.parseColor("#89C3E7"));
//                    btnSignIn.setEnabled(false);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                //  viewModel.validateEmailField(etEmail);
//            }
//        });


//        etPassword.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                password = etPassword.getText().toString();
//                if (!TextUtils.isEmpty(password) & !TextUtils.isEmpty(email)) {
//                    tvForgot.setTextColor(Color.parseColor("#1485CC"));
//                    btnSignIn.setBackgroundResource(R.drawable.btn_round_outline);
//                    btnSignIn.setEnabled(true);
//
//                } else {
//                    btnSignIn.setBackgroundResource(R.drawable.btn_round_outline_disable);
//                    tvForgot.setTextColor(Color.parseColor("#89C3E7"));
//                    btnSignIn.setEnabled(false);
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                /// bitForMessageSave = 1;
//
//                //    viewModel.validatePasswordField(etPassword);
//
//            }
//        });


        etForgotEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                forgotEmail = etForgotEmail.getText().toString();

                if (!TextUtils.isEmpty(forgotEmail)) {
                    btnContinue.setBackgroundResource(R.drawable.btn_round_outline);
                    btnContinue.setEnabled(true);

                } else {
                    btnContinue.setBackgroundResource(R.drawable.btn_round_outline_disable);

                    btnContinue.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    private TextView.OnEditorActionListener editorListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (actionId) {
                case EditorInfo.IME_ACTION_GO:
                    if (networkOk) {
                        progressBar.setVisibility(View.VISIBLE);
                        loginDisable(true,btnContinue);
                        requestForgotPassword();

                    } else {
                        Tools.showNetworkDialog(getSupportFragmentManager());
                        progressBar.setVisibility(View.GONE);

                    }
                    break;
                case EditorInfo.IME_ACTION_DONE:
                    if (networkOk) {
                        progressBar.setVisibility(View.VISIBLE);
                        loginDisable(true,btnOTPContinue);
                        requestForOTP();
                    } else {
                        Tools.showNetworkDialog(getSupportFragmentManager());
                        progressBar.setVisibility(View.GONE);

                    }


                    break;
                case EditorInfo.IME_ACTION_SEND:

                    if (networkOk) {
                        progressBar.setVisibility(View.VISIBLE);
                        loginDisable(true,btnFinish);
                        requestNewPassword();

                    } else {
                        Tools.showNetworkDialog(getSupportFragmentManager());
                        progressBar.setVisibility(View.GONE);

                    }


                    break;
            }
            return false;
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onHomeClick();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onHomeClick();
            return true;
        }
        return false;
        //  return super.onKeyDown(keyCode, event);
    }


    private void onHomeClick() {

        if (flipperId > 0) {
            mViewFlipper.setInAnimation(slideRightIn);
            mViewFlipper.setOutAnimation(slideRightOut);
            mViewFlipper.showPrevious();
            flipperId--;
        } else {

            this.overridePendingTransition(0, 0);
            ForgotPasswords.this.finish();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
//            case R.id.btnLogin:
//
//                if (flipperId == 0) {
//                    flipperId++;
//                    mViewFlipper.setInAnimation(slideLeftIn);
//                    mViewFlipper.setOutAnimation(slideLeftOut);
//                    mViewFlipper.showNext();
//
//                }
//
//
//                break;

//            case R.id.tvForgot:
//                if (flipperId == 0) {
//                    flipperId++;
//                    mViewFlipper.setInAnimation(slideLeftIn);
//                    mViewFlipper.setOutAnimation(slideLeftOut);
//                    mViewFlipper.showNext();
//
//                }
//
//                break;


            case R.id.btnContinue:
                if (flipperId == 0) {
//                    flipperId++;
//                    mViewFlipper.setInAnimation(slideLeftIn);
//                    mViewFlipper.setOutAnimation(slideLeftOut);
//                    mViewFlipper.showNext();

                    if (networkOk) {
                        progressBar.setVisibility(View.VISIBLE);
                        loginDisable(true,btnContinue);
                        requestForgotPassword();

                    } else {
                        Tools.showNetworkDialog(getSupportFragmentManager());
                        progressBar.setVisibility(View.GONE);

                    }

                }
                break;
            case R.id.btnOTPContinue:
                if (flipperId == 1) {
                    if (networkOk) {
                        progressBar.setVisibility(View.VISIBLE);
                        loginDisable(true,btnOTPContinue);
                        requestForOTP();
                    } else {
                        Tools.showNetworkDialog(getSupportFragmentManager());
                        progressBar.setVisibility(View.GONE);

                    }
                }
                break;

            case R.id.imgEmailCancel:
                startActivity(new Intent(this, Login.class));
                finish();
                break;
            case R.id.imgOTPCancel:
                if (flipperId == 1) {
                    mViewFlipper.setInAnimation(slideRightIn);
                    mViewFlipper.setOutAnimation(slideRightOut);
                    mViewFlipper.showPrevious();
                    flipperId--;
                }
                break;
            case R.id.imgResetPasswordCancel:
                if (flipperId == 2) {
                    mViewFlipper.setInAnimation(slideRightIn);
                    mViewFlipper.setOutAnimation(slideRightOut);
                    mViewFlipper.showPrevious();
                    flipperId--;
                }
                break;
//            case R.id.imgAbout:
//                startActivity(new Intent(ForgotPasswords.this, About.class));
//                break;
            case R.id.imgEmailAbout:
                startActivity(new Intent(ForgotPasswords.this, About.class));
                break;
            case R.id.imgOTPAbout:
                startActivity(new Intent(ForgotPasswords.this, About.class));
                break;
            case R.id.imgResetAbout:
                startActivity(new Intent(ForgotPasswords.this, About.class));
                break;
//            case R.id.etEmail:
//                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//                break;
//            case R.id.etPassword:
//                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//                break;
//            case R.id.tvSignup:
//                startActivity(new Intent(ForgotPasswords.this, Signup.class));
//                //finish();
//                break;
            case R.id.btnFinish:
                if (networkOk) {
                    progressBar.setVisibility(View.VISIBLE);
                    loginDisable(true,btnFinish);
                    requestNewPassword();

                } else {
                    Tools.showNetworkDialog(getSupportFragmentManager());
                    progressBar.setVisibility(View.GONE);

                }

                break;
//            case R.id.fbLogin:
//                startActivity(new Intent(this, FBLogin.class));
//                break;
//            case R.id.twitterLogin:
//                startActivity(new Intent(this, MyTwitter.class));
//                finish();
//                break;
//            case R.id.btnSignIn:
//                if (networkOk) {
//                    progressView.setVisibility(View.VISIBLE);
//                    progressView.startAnimation();
//                    mDeviceId = manager.getDeviceId();
//                    requestData(email, password, mDeviceId);
//
//                } else {
//                    Tools.showNetworkDialog(getSupportFragmentManager());
//                    progressView.setVisibility(View.GONE);
//                    progressView.stopAnimation();
//
//
//                }
//                break;

            case R.id.tvResendOTP:
                if (networkOk) {
                    progressBar.setVisibility(View.VISIBLE);
                    loginDisable(true,btnContinue);
                    requestResendForgotPassword();

                } else {
                    Tools.showNetworkDialog(getSupportFragmentManager());
                    progressBar.setVisibility(View.GONE);

                }
                break;

        }
    }

    private void requestResendForgotPassword() {

        AuthService webService =
                AuthService.retrofitBase.create(AuthService.class);
        Call<ForgotPassword> call = webService.forgotPassword(forgotEmail);
        resendForgotPasswordRequest(call);


    }

    private void resendForgotPasswordRequest(Call<ForgotPassword> call) {
        call.enqueue(new Callback<ForgotPassword>() {
            @Override
            public void onResponse(Call<ForgotPassword> call, Response<ForgotPassword> response) {
                forgotPassword = response.body();
                boolean status = forgotPassword.isStatus();
                if (status) {
                    String message =getString(R.string.password_request_send_to_your_email);
                    showStatus(message);
                /*    flipperId++;
                    mViewFlipper.setInAnimation(slideLeftIn);
                    mViewFlipper.setOutAnimation(slideLeftOut);
                    mViewFlipper.showNext();*/
                } else if (forgotPassword.getError() != null) {
                    Error error = forgotPassword.getError();
                    String message = error.getEmail();
                    showStatus(message);
                } else {
                    String message = getString(R.string.invalid_email_address);
                    showStatus(message);
                }

                progressBar.setVisibility(View.GONE);
                loginDisable(false,btnContinue);

            }

            @Override
            public void onFailure(Call<ForgotPassword> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                loginDisable(false,btnContinue);
            }
        });

    }

    private void requestNewPassword() {
        AuthService webService =
                AuthService.retrofitBase.create(AuthService.class);
        Call<String> call = webService.setNewPassword(forgotPassword.getUserId(), otp, confirmPassword, newPassword);
        sendNewPasswordRequest(call);
    }

    private void sendNewPasswordRequest(Call<String> call) {

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject object = new JSONObject(response.body());
                    status = object.getBoolean("status");
                    if (status) {
                        String message = getString(R.string.successfully_set_password);
                        showStatus(message);

                        if (flipperId == 2) {
//                            mViewFlipper.setInAnimation(slideRightIn);
//                            mViewFlipper.setOutAnimation(slideRightOut);
//                            mViewFlipper.showPrevious();
//                            flipperId--;
//                            mViewFlipper.showPrevious();
//                            flipperId--;
//                            mViewFlipper.showPrevious();
//                            flipperId--;
                            //   mViewFlipper.setDisplayedChild(1);
                            // mViewFlipper.setDisplayedChild(mViewFlipper.indexOfChild(findViewById(R.id.loginContent)));

                            Tools.toast(ForgotPasswords.this,getString(R.string.reset_password_success),R.drawable.ic_check_black_24dp);
                            startActivity(new Intent(ForgotPasswords.this, Login.class));
                            finish();
                        }
                        // startActivity(new Intent(ForgotPasswords.this, Liker.class));

                    } else {
                        String message = getString(R.string.reset_password_fail);
                        showStatus(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                progressBar.setVisibility(View.GONE);
                loginDisable(false,btnFinish);

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                progressBar.setVisibility(View.GONE);
                loginDisable(false,btnFinish);

            }
        });
    }

    private void requestForOTP() {


        AuthService webService =
                AuthService.retrofitBase.create(AuthService.class);
        Call<String> call = webService.setOTP(forgotPassword.getUserId(), otp);
        sendOTPRequest(call);


    }

    boolean status;

    private void sendOTPRequest(Call<String> call) {

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject object = new JSONObject(response.body());
                    status = object.getBoolean("status");
                    if (status) {
                        flipperId++;
                        mViewFlipper.setInAnimation(slideLeftIn);
                        mViewFlipper.setOutAnimation(slideLeftOut);
                        mViewFlipper.showNext();
                        String message = getString(R.string.successfully_set_code);
                        showStatus(message);
                    } else {
                        String message = getString(R.string.failed_to_set_code);
                        showStatus(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                progressBar.setVisibility(View.GONE);
                loginDisable(false,btnOTPContinue);

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                progressBar.setVisibility(View.GONE);
                loginDisable(false,btnOTPContinue);


            }
        });

    }

    private void showSnackbar(String message) {
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
                    showSnackbar(getString(R.string.login_success));
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
                    manager.setProfileImage(PROFILE_IMAGE + photo);
                    manager.setProfileId(profileId);
                    manager.setUserName(userName);
                    //   startActivity(new Intent(ForgotPasswords.this, Liker.class));
//                    Intent intent=new Intent(ForgotPasswords.this,Home.class);
//                    intent.putExtra(USER_INFO_ITEM_KEY, (Parcelable) userInfo);
//                    startActivity(intent);

                    Intent intent = new Intent(ForgotPasswords.this, Home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    String msg = getString(R.string.username_and_password_miss_match);
                    showStatus(msg);

                    if (loginUser.getIsVerified() != null && loginUser.getBounceData() != null) {
                        if (loginUser.getIsVerified().equalsIgnoreCase("0") && loginUser.getBounceData().equalsIgnoreCase("0")) {
                            userInfo = loginUser.getUserInfo();
                            if (userInfo != null) {
                                String message = getString(R.string.a_verification_email_has_been_sent_to_your_email_address);
                                ResendEmail resendEmail = ResendEmail.newInstance(message);
                                resendEmail.show(getSupportFragmentManager(), "ResendEmail");
                            }

                        } else if (loginUser.getBounceData().equalsIgnoreCase(String.valueOf(1)) || loginUser.getBounceData().equalsIgnoreCase(String.valueOf(2))) {
                            Toast.makeText(ForgotPasswords.this, getString(R.string.email_is_invalid), Toast.LENGTH_SHORT).show();
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


    private void requestResendEmail() {
        AuthService webService =
                AuthService.retrofitBase.create(AuthService.class);
        Call<ResendStatus> call = webService.resendEmail(userInfo.getUserId());
        sendEmailRequest(call);


    }

    private void sendEmailRequest(Call<ResendStatus> call) {

        call.enqueue(new Callback<ResendStatus>() {
            @Override
            public void onResponse(Call<ResendStatus> call, Response<ResendStatus> response) {
                ResendStatus data = response.body();
                String message = data.getMessage();
                // Log.d("Message", message);
                showStatus(message);
            }

            @Override
            public void onFailure(Call<ResendStatus> call, Throwable t) {

            }
        });

    }

    private void requestForgotPassword() {
        AuthService webService =
                AuthService.retrofitBase.create(AuthService.class);
        Call<com.doodle.Authentication.model.ForgotPassword> call = webService.forgotPassword(forgotEmail);
        sendForgotPasswordRequest(call);
    }

    private void loginDisable(boolean disable,View view) {
        if (disable) {
            view.setBackgroundResource(R.drawable.btn_round_outline_disable);
            view.setEnabled(false);
        } else {
            view.setBackgroundResource(R.drawable.btn_round_outline);
            view.setEnabled(true);
        }
    }

   ForgotPassword forgotPassword;

    private void sendForgotPasswordRequest(Call<com.doodle.Authentication.model.ForgotPassword> call) {

        call.enqueue(new Callback<com.doodle.Authentication.model.ForgotPassword>() {
            @Override
            public void onResponse(Call<com.doodle.Authentication.model.ForgotPassword> call, Response<com.doodle.Authentication.model.ForgotPassword> response) {
                forgotPassword = response.body();
                boolean status = forgotPassword.isStatus();
                if (status) {
                    String message = getString(R.string.password_request_send_to_your_email);
                    showStatus(message);
                    flipperId++;
                    mViewFlipper.setInAnimation(slideLeftIn);
                    mViewFlipper.setOutAnimation(slideLeftOut);
                    mViewFlipper.showNext();
                } else if (forgotPassword.getError() != null) {
                    Error error = forgotPassword.getError();
                    String message = error.getEmail();
                    showStatus(message);
                } else {
                    String message = getString(R.string.invalid_email_address);
                    showStatus(message);
                }

                progressBar.setVisibility(View.GONE);
                loginDisable(false,btnContinue);

            }

            @Override
            public void onFailure(Call<com.doodle.Authentication.model.ForgotPassword> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                loginDisable(false,btnContinue);
            }
        });

    }

//    public void mySnackbar() {
//        final Snackbar snackbar = Snackbar.make(findViewById(R.id.signupContainer), "This is a SnackBar", Snackbar.LENGTH_SHORT);
//        snackbar.setAction("Set Action", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(ForgotPasswords.this, "Tap Action Button!", Toast.LENGTH_SHORT).show();
//                snackbar.dismiss();
//            }
//        });
//
//        snackbar.setActionTextColor(Color.RED);
//        snackbar.show();
//    }

    private void showSnackbars(String message) {
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


    @Override
    public void onButtonClicked(String text) {

        requestResendEmail();

    }
}
