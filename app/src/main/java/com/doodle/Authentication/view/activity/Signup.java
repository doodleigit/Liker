package com.doodle.Authentication.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.chaos.view.PinView;
import com.doodle.App;
import com.doodle.Authentication.model.City;
import com.doodle.Authentication.model.CitySpinner;
import com.doodle.Authentication.model.CountryInfo;
import com.doodle.Authentication.model.CountrySpinner;
import com.doodle.Authentication.model.Data;
import com.doodle.Authentication.model.ResendStatus;
import com.doodle.Authentication.model.SocialInfo;
import com.doodle.Authentication.model.User;
import com.doodle.Authentication.service.MyService;
import com.doodle.Authentication.view.fragment.ResendEmail;
import com.doodle.Authentication.viewmodel.SignupViewModel;
import com.doodle.Home.view.activity.Home;
import com.doodle.R;
import com.doodle.Tool.AppConstants;
import com.doodle.Tool.ClearableEditText;
import com.doodle.Authentication.service.AuthService;
import com.doodle.Tool.NetworkHelper;
import com.doodle.Tool.PrefManager;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import static com.doodle.Authentication.view.activity.Login.SOCIAL_ITEM;
import static com.doodle.Comment.holder.CommentTextHolder.COMMENT_ITEM_KEY;
import static com.doodle.Comment.holder.CommentTextHolder.POST_ITEM_KEY;
import static com.doodle.Comment.holder.CommentTextHolder.REPLY_KEY;
import static com.doodle.Tool.Tools.isNullOrEmpty;

public class Signup extends AppCompatActivity implements View.OnClickListener, ResendEmail.BottomSheetListener {


    private static final String TAG = "Signup";
    private ViewFlipper mViewFlipper;
    int flipperId = 0;
    protected Animation slideRightIn;
    protected Animation slideRightOut;
    protected Animation slideLeftIn;
    protected Animation slideLeftOut;

    private EditText etPassword, etConFirmPassword;
    private ClearableEditText etFirstName, etLastName, etEmail;
    private String firstName, lastName, email, password, confirmPassword;
    private Button btnSignUp, btnFinish, btnOTPContinue;
    private Spinner spinnerDay, spinnerMonth, spinnerYear, spinnerCountry, spinnerState;

    private String[] dayArr = {"Select", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
    private String[] monthArr = {"Month", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private String[] yearArr = {"Select Year", "2020", "2021", "2022", "2023", "2024", "2025", "2019", "2018", "2017", "2016", "2015", "2014", "2013", "2012", "2011", "2010", "2009", "2008", "2007", "2006", "2005", "2004", "2003", "2002", "2001", "2000", "1999", "1998", "1997", "1996", "1995", "1994", "1993", "1992", "1991", "1990", "1989", "1988", "1987", "1986", "1985", "1984", "1983", "1982", "1981", "1980", "1979", "1978", "1977", "1976", "1975", "1974", "1973", "1972", "1971", "1970"};
    private String[] countryArr = {"Select Country", "Bangladesh", "Nepal", "Bhutan", "China"};
    private String[] stateArr = {"Select State", "Dhaka", "Kathmandu", "Thimphu", "Beijing"};

    private TextView tvAcceptTerms, tvAcceptFinish, tvHeader, tvOr;
    private String originalText, originalTextFinish;
    private ImageView imgAboutSignUp;
    private boolean networkok;
    private String url = "https://www.liker.com/terms";
    private boolean networkOk;

    TextInputLayout firstNameLayout;
    SignupViewModel viewModel;
    private List<Data> dataList;
    private List<String> countryIds;
    private List<String> countryNames;
    ArrayList<CountrySpinner> countrySpinnerList = new ArrayList<>();
    ArrayList<CitySpinner> citySpinnerList = new ArrayList<>();
    private List<CountryInfo> countryInfos;
    private ImageView fbSignUp, twitterSignUp;

    public String mFirstName;
    public String userId;
    public String mlastName;
    public String mEmail;
    public String mPassword;
    public String mRetypePassword;
    public String mGender;
    public String mDay;
    public String mMonth;
    public String mYear;
    public String mCountry;
    public String mCity;
    public String mProvider;
    public String mOauthId;
    public String mToken = "";
    public String mSecret = "";
    public String mSocialName;
    public String mImgUrl;
    User user;
    String otp;
    String mDeviceId;
    private PrefManager manager;
    private String fbProvider;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            countryInfos = intent.getParcelableArrayListExtra(MyService.MY_SERVICE_PAYLOAD);
            displayData();

        }
    };
    private String isApps = "true";

    private void displayData() {
        //    countryNames.add("Select Country");
        countrySpinnerList.add(new CountrySpinner("0", "Select Country"));
        if (countryInfos != null) {

            for (CountryInfo item : countryInfos
            ) {
                String name = item.getCountryName();
                String id = item.getCountryId();
                countrySpinnerList.add(new CountrySpinner(id, name));
            }


            ArrayAdapter<CountrySpinner> countryAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, countrySpinnerList);
            spinnerCountry.setAdapter(countryAdapter);

        }
    }

    private SocialInfo socialInfo;
    private CallbackManager callbackManager;
    public static final int WEBVIEW_REQUEST_CODE = 100;
    private String mConsumerKey = null, mConsumerSecret = null, mCallbackUrl = null, mTwitterVerifier = null, mAuthVerifier = null;
    private Twitter mTwitter = null;
    private RequestToken mRequestToken = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        callbackManager = CallbackManager.Factory.create();
        mConsumerKey = getString(R.string.com_twitter_sdk_android_CONSUMER_KEY);
        mConsumerSecret = getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET);
        mAuthVerifier = "oauth_verifier";

        viewModel = ViewModelProviders.of(this).get(SignupViewModel.class);
        countryIds = new ArrayList<>();
        countryNames = new ArrayList<>();
        user = new User();
        socialInfo = new SocialInfo();
        networkOk = NetworkHelper.hasNetworkAccess(this);
        manager = new PrefManager(this);
        mDeviceId = manager.getDeviceId();
        etFirstName = (ClearableEditText) findViewById(R.id.etFirstName);
        etFirstName.setOnClickListener(this);
        etLastName = (ClearableEditText) findViewById(R.id.etLastName);
        etLastName.setOnClickListener(this);
        etEmail = (ClearableEditText) findViewById(R.id.etEmail);
        etEmail.setOnClickListener(this);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPassword.setOnClickListener(this);
        etConFirmPassword = (EditText) findViewById(R.id.etConFirmPassword);
        etConFirmPassword.setOnClickListener(this);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);
        btnFinish = (Button) findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(this);
        twitterSignUp = (ImageView) findViewById(R.id.twitterSignUp);
        fbSignUp = (ImageView) findViewById(R.id.fbSignUp);
        twitterSignUp.setOnClickListener(this);
        fbSignUp.setOnClickListener(this);
        btnSignUp.setEnabled(false);

        spinnerDay = findViewById(R.id.spinnerDay);
        spinnerMonth = findViewById(R.id.spinnerMonth);
        spinnerYear = findViewById(R.id.spinnerYear);
        spinnerCountry = findViewById(R.id.spinnerCountry);
        spinnerState = findViewById(R.id.spinnerState);
        findViewById(R.id.imgAboutSignUp).setOnClickListener(this);
        findViewById(R.id.ivCancelSignup).setOnClickListener(this);
        findViewById(R.id.ivPreviousSignup).setOnClickListener(this);
        findViewById(R.id.ivOTPCancel).setOnClickListener(this);
        btnOTPContinue = findViewById(R.id.btnOTPContinue);
        btnOTPContinue.setOnClickListener(this);
        firstNameLayout = (TextInputLayout) findViewById(R.id.firstNameLayout);

        mViewFlipper = (ViewFlipper) findViewById(R.id.viewFlipperContent);
        slideRightIn = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
        slideRightOut = AnimationUtils.loadAnimation(this, R.anim.slide_right_out);
        slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
        slideLeftOut = AnimationUtils.loadAnimation(this, R.anim.slide_left_out);

        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, dayArr);
        spinnerDay.setAdapter(dayAdapter);
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, monthArr);
        spinnerMonth.setAdapter(monthAdapter);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, yearArr);
        spinnerYear.setAdapter(yearAdapter);


        spinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int position, long arg3) {
                // TODO Auto-generated method stub
                String s1 = String.valueOf(spinnerDay.getSelectedItem());

                mDay = s1;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int position, long arg3) {
                // TODO Auto-generated method stub
                String s1 = String.valueOf(spinnerMonth.getSelectedItem());
                //January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December
                switch (s1) {
                    case "January":
                        mMonth = "01";
                        break;
                    case "February":
                        mMonth = "02";
                        break;
                    case "March":
                        mMonth = "03";
                        break;
                    case "April":
                        mMonth = "04";
                        break;
                    case "May":
                        mMonth = "05";
                        break;
                    case "June":
                        mMonth = "06";
                        break;
                    case "July":
                        mMonth = "07";
                        break;
                    case "August":
                        mMonth = "08";
                        break;
                    case "September":
                        mMonth = "09";
                        break;
                    case "October":
                        mMonth = "10";
                        break;
                    case "November":
                        mMonth = "11";
                        break;
                    case "December":
                        mMonth = "12";
                        break;


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int position, long arg3) {
                // TODO Auto-generated method stub
                String s1 = String.valueOf(spinnerYear.getSelectedItem());

                mYear = s1;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int position, long arg3) {
                // TODO Auto-generated method stub
                String s1 = String.valueOf(spinnerCountry.getSelectedItem());

                CountrySpinner country = (CountrySpinner) parent.getSelectedItem();
                String countryId = country.getId();
                String name = country.getName();
                mCountry = countryId;
                int id = Integer.parseInt(countryId);
                if (id > 0) {
                    requestCityData(id);
                    if (!s1.contentEquals("Select Country")) {

                        ArrayAdapter<CitySpinner> stateAdapter = new ArrayAdapter<>(Signup.this, R.layout.spinner_item, citySpinnerList);
                        spinnerState.setAdapter(stateAdapter);
                    }
                }
                // Toast.makeText(Signup.this, "Id" + id, Toast.LENGTH_SHORT).show();
                //   Toast.makeText(Signup.this, "Name" + name, Toast.LENGTH_SHORT).show();
                //  Toast.makeText(context, "Country ID: "+country.getId()+",  Country Name : "+country.getName(), Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        tvAcceptTerms = (TextView) findViewById(R.id.tvAcceptTerms);
        tvHeader = (TextView) findViewById(R.id.tvHeader);
        tvOr = (TextView) findViewById(R.id.tvOr);
        tvAcceptTerms.setOnClickListener(this);
        tvAcceptFinish = (TextView) findViewById(R.id.tvAcceptFinish);
        tvAcceptFinish.setOnClickListener(this);
        originalTextFinish = (String) tvAcceptFinish.getText();
        originalText = (String) tvAcceptTerms.getText();

        SpannableString spannableStr = new SpannableString(originalText);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#46ADE3"));
        spannableStr.setSpan(foregroundColorSpan, 46, originalText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvAcceptTerms.setText(spannableStr);


        final PinView pinView = findViewById(R.id.firstPinView);
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


        if (networkOk) {
            Intent intent = new Intent(this, MyService.class);
            startService(intent);
        } else {
//           showSnackbar();
            Toast.makeText(this, "no internet!", Toast.LENGTH_SHORT).show();
        }

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(MyService.MY_SERVICE_MESSAGE));


        etFirstName.setDrawableClickListener(new ClearableEditText.DrawableClickListener() {
            @Override
            public void onClick() {
                etFirstName.setText(null);
            }
        });
        etLastName.setDrawableClickListener(new ClearableEditText.DrawableClickListener() {
            @Override
            public void onClick() {
                etLastName.setText(null);
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
                mEmail = etEmail.getText().toString();


                if (!TextUtils.isEmpty(email) & !TextUtils.isEmpty(firstName) & !TextUtils.isEmpty(lastName)) {
                    btnSignUp.setBackgroundResource(R.drawable.btn_round_outline);
                    btnSignUp.setEnabled(true);


                } else {
                    btnSignUp.setBackgroundResource(R.drawable.btn_round_outline_disable);
                    btnSignUp.setEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.validateEmailField(etEmail);
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password = etPassword.getText().toString();
                mPassword = etPassword.getText().toString();
//                if (!TextUtils.isEmpty(password) & !TextUtils.isEmpty(email) & !TextUtils.isEmpty(firstName) & !TextUtils.isEmpty(lastName) & !TextUtils.isEmpty(confirmPassword)) {
//                    btnSignUp.setBackgroundResource(R.drawable.btn_round_outline);
//                    btnSignUp.setEnabled(true);
//
//
//                } else {
//                    btnSignUp.setBackgroundResource(R.drawable.btn_round_outline_disable);
//                    btnSignUp.setEnabled(false);
//
//                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                /// bitForMessageSave = 1;
                viewModel.validatePasswordField(etPassword);

            }
        });

        etFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                firstName = etFirstName.getText().toString();
                mFirstName = etFirstName.getText().toString();
                if (!TextUtils.isEmpty(email) & !TextUtils.isEmpty(firstName) & !TextUtils.isEmpty(lastName)) {
                    btnSignUp.setBackgroundResource(R.drawable.btn_round_outline);
                    btnSignUp.setEnabled(true);


                } else {
                    btnSignUp.setBackgroundResource(R.drawable.btn_round_outline_disable);
                    btnSignUp.setEnabled(false);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.validateNameField(etFirstName);
            }
        });


        etLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lastName = etLastName.getText().toString();
                mlastName = etLastName.getText().toString();
                if (!TextUtils.isEmpty(email) & !TextUtils.isEmpty(firstName) & !TextUtils.isEmpty(lastName)) {
                    btnSignUp.setBackgroundResource(R.drawable.btn_round_outline);
                    btnSignUp.setEnabled(true);

                } else {
                    btnSignUp.setBackgroundResource(R.drawable.btn_round_outline_disable);
                    btnSignUp.setEnabled(false);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.validateNameField(etLastName);
            }
        });

        etConFirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                confirmPassword = etConFirmPassword.getText().toString();
                mRetypePassword = etConFirmPassword.getText().toString();
//                if (!TextUtils.isEmpty(password) & !TextUtils.isEmpty(email) & !TextUtils.isEmpty(firstName) & !TextUtils.isEmpty(lastName) & !TextUtils.isEmpty(confirmPassword)) {
//                    btnSignUp.setBackgroundResource(R.drawable.btn_round_outline);
//                    btnSignUp.setEnabled(true);
//
//                } else {
//                    btnSignUp.setBackgroundResource(R.drawable.btn_round_outline_disable);
//                    btnSignUp.setEnabled(false);
//
//                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                /// bitForMessageSave = 1;
                viewModel.validateConfirmPasswordField(etConFirmPassword);

            }
        });

        socialInfo = getIntent().getExtras().getParcelable(SOCIAL_ITEM);
        if (socialInfo == null) {
            throw new AssertionError("Null data item received!");
        }
        setDate();
    }

    private void setDate() {
        mOauthId = socialInfo.getAuthId();
        mSocialName = socialInfo.getSocialName();
        mImgUrl = socialInfo.getImage();
        mProvider = socialInfo.getProvider();
        if (!socialInfo.getFirstName().isEmpty()) {
            etFirstName.setText(socialInfo.getFirstName());
        }
        if (!socialInfo.getLastName().isEmpty()) {
            etLastName.setText(socialInfo.getLastName());
        }
        if (!socialInfo.getEmail().isEmpty()) {
            etEmail.setText(socialInfo.getEmail());
        }
        if (!socialInfo.getImage().isEmpty()) {
            if (mProvider.equals("facebook")) {
                Picasso.with(Signup.this)
                        .load(socialInfo.getImage())
                        .placeholder(R.drawable.ic_facebook)
                        .into(fbSignUp);
                twitterSignUp.setImageResource(R.drawable.ic_twitter);
            } else if (mProvider.equals("twitter")) {
                Picasso.with(Signup.this)
                        .load(socialInfo.getImage())
                        .placeholder(R.drawable.ic_twitter)
                        .into(twitterSignUp);
                fbSignUp.setImageResource(R.drawable.ic_facebook);
            }
        }
        if (!mProvider.isEmpty()) {
            tvHeader.setText("YOU'RE ALMOST DONE " + "\n" + "WE NEED A FEW MORE DETAILS...");
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        PrefManager manager = new PrefManager(this);
        switch (id) {

            case R.id.btnSignUp:
                Log.d(TAG, "onClick: " + flipperId);
                if (flipperId == 0) {
                    if (viewModel.validateNameField(etFirstName) && viewModel.validateNameField(etLastName) && viewModel.validateEmailField(etEmail)) {
                        flipperId++;
                        mViewFlipper.setInAnimation(slideLeftIn);
                        mViewFlipper.setOutAnimation(slideLeftOut);
                        mViewFlipper.showNext();

                        SpannableString spannableStr = new SpannableString(originalTextFinish);
                        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#46ADE3"));
                        spannableStr.setSpan(foregroundColorSpan, 46, originalTextFinish.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        tvAcceptFinish.setText(spannableStr);
                    }
                }

                break;

            case R.id.btnFinish:
                if (viewModel.validatePasswordField(etPassword) && viewModel.validateConfirmPasswordField(etConFirmPassword) && mGender.isEmpty() && mCountry.isEmpty() && mDay.isEmpty() && mMonth.isEmpty() &&
                        mYear.isEmpty() && mCity.isEmpty() && mProvider.isEmpty() && mOauthId.isEmpty()) {
                    if (networkOk) {
                        requestData(mFirstName, mlastName, mEmail, mPassword, mRetypePassword, mGender, mCountry, mDay, mMonth, mYear, mCity, mProvider, mOauthId, mToken, mSecret, mSocialName, isApps, mImgUrl);
                    } else {
                        showSnackbar(getString(R.string.no_internet));
                    }
                } else {
                    showSnackbar(getString(R.string.all_field_required));
                }
                break;
            case R.id.etFirstName:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                //  Toast.makeText(this, "First name", Toast.LENGTH_SHORT).show();
                break;
            case R.id.etLastName:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                break;
            case R.id.etEmail:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                break;
            case R.id.etPassword:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                break;
            case R.id.etConFirmPassword:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                break;
            case R.id.imgAboutSignUp:
                startActivity(new Intent(this, About.class));
                break;
            case R.id.ivCancelSignup:
                finish();
                break;
            case R.id.ivPreviousSignup:
                flipperId--;
                mViewFlipper.setInAnimation(slideLeftIn);
                mViewFlipper.setOutAnimation(slideLeftOut);
                mViewFlipper.showPrevious();
                break;
            case R.id.ivOTPCancel:
                finish();
                break;
            case R.id.tvAcceptTerms:
                goBrowser(url);
                break;
            case R.id.tvAcceptFinish:
                goBrowser(url);
                break;
            case R.id.btnOTPContinue:
                requestForOTPLogin();
                break;
            case R.id.fbSignUp:
                facebookLogin();
                break;
            case R.id.twitterSignUp:
                loginToTwitter();
                break;

        }
    }

    private void requestForOTPLogin() {

        AuthService webService =
                AuthService.retrofitBase.create(AuthService.class);
        Call<String> call = webService.setOTPLogin(user.userId, mDeviceId, otp);
        sendOTPRequest(call);

    }

    int otpBounceData;
    String otpExpire;
    boolean otpStatus;

    private void sendOTPRequest(Call<String> call) {

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String data = response.body();

                try {
                    JSONObject object = new JSONObject(data);

                    if (isContain(object, "status")) {
                        otpStatus = object.getBoolean("status");
                    }
                    if (isContain(object, "bounce_data")) {
                        otpBounceData = object.getInt("bounce_data");
                    }

                    if (isContain(object, "error")) {
                        JSONObject errorObject = object.getJSONObject("error");
                        if (isContain(errorObject, "verify_otp")) {
                            otpExpire = errorObject.getString("verify_otp");
                        }
                    }

                    if (otpStatus) {
                        // startActivity(new Intent(Signup.this, Liker.class));
                        Intent intent = new Intent(Signup.this, Home.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {

                        if (otpBounceData == 1) {
                            showStatus("Email Invalid");
                        } else if (otpBounceData == 0) {
                            Toast.makeText(Signup.this, "Your OTP Expire", Toast.LENGTH_SHORT).show();
                            showStatus("Your OTP Expire");
                            String msg = "Resend OTP Code.";
                            ResendEmail resendEmail = ResendEmail.newInstance(msg);
                            resendEmail.show(getSupportFragmentManager(), "ResendEmail");

                        } else {
                            String message = "OTP Miss Match";
                            showStatus(message);
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });


    }

    public void goBrowser(String url) {
        if (networkok) {
            if (!url.startsWith("http://") && !url.startsWith("https://"))
                url = "http://" + url;
            try {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(myIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No application can handle this request."
                        + " Please install a webBrowser", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        } else {
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
        }

        // finish();
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


    private void requestData(String mFirstName, String mlastName, String mEmail, String mPassword, String mRetypePassword, String mGender, String mCountry, String mDay, String mMonth, String mYear, String mCity, String mProvider, String mOauthId, String mToken, String mSecret, String mSocialName, String isApps, String mImgUrl) {
        AuthService webService =
                AuthService.retrofitBase.create(AuthService.class);
        Call<String> call = webService.registerUser(mFirstName, mlastName, mEmail, mPassword, mRetypePassword, mGender, mCountry, mDay, mMonth, mYear, mCity, mProvider, mOauthId, mToken, mSecret, mSecret, isApps, mImgUrl);
//        Call<String> call = webService.registerUser(mFirstName, mlastName, mEmail, mPassword, mRetypePassword, "1", "1", "01", "02", "1987", "1", "", "", "", "", "", "");
        sendRequest(call);
    }

    boolean status;

    private void sendRequest(Call<String> call) {
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String message = response.body();

                try {
                    JSONObject object = new JSONObject(new String(message));
                    if (isContain(object, "status")) {
                        user.status = object.getBoolean("status");
                        if (user.status) {

                            if (flipperId == 1) {
                                flipperId++;
                                mViewFlipper.setInAnimation(slideLeftIn);
                                mViewFlipper.setOutAnimation(slideLeftOut);
                                mViewFlipper.showNext();

                            }

                            if (isContain(object, "user_id")) {
                                user.userId = object.getString("user_id");
                                //manager.setProfileId(user.userId);
                                String msg = "A verification email has been sent to your email address. Please confirm and complete your registration.";
                                ResendEmail resendEmail = ResendEmail.newInstance(msg);
                                resendEmail.show(getSupportFragmentManager(), "ResendEmail");
                                //startActivity(new Intent(Signup.this, Liker.class));
                            } else {
                                user.userId = "";
                            }
                        } else {
                            if (isContain(object, "error")) {
                                JSONObject errorObject = object.getJSONObject("error");
                                if (isContain(errorObject, "first_name")) {
                                    user.first_name = errorObject.getString("first_name");
                                    showSnackbar(user.first_name);
                                    mViewFlipper.showPrevious();
                                    flipperId--;


                                } else if (isContain(errorObject, "last_name")) {
                                    user.last_name = errorObject.getString("last_name");
                                    showSnackbar(user.last_name);
                                    mViewFlipper.showPrevious();
                                    flipperId--;

                                } else if (isContain(errorObject, "email")) {
                                    user.email = errorObject.getString("email");
                                    showSnackbar(user.email);
                                    mViewFlipper.showPrevious();
                                    flipperId--;


                                } else if (isContain(errorObject, "password")) {
                                    user.password = errorObject.getString("password");
                                    showSnackbar(user.password);
                                    mViewFlipper.showPrevious();
                                    flipperId--;

                                } else if (isContain(errorObject, "retype_password")) {
                                    user.retype_password = errorObject.getString("retype_password");
                                    showSnackbar(user.retype_password);
                                    mViewFlipper.showPrevious();
                                    flipperId--;

                                } else if (isContain(errorObject, "gender")) {
                                    user.gender = errorObject.getString("gender");
                                    showSnackbar(user.gender);
                                } else if (isContain(errorObject, "country")) {
                                    user.country = errorObject.getString("country");
                                    showSnackbar(user.country);
                                } else if (isContain(errorObject, "dob")) {
                                    user.dob = errorObject.getString("dob");
                                    showSnackbar(user.dob);
                                } else {
                                    mViewFlipper.showPrevious();
                                    flipperId--;
                                }
                            }

                        }
                    } else {
                        status = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("Message", message);


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("message", t.getMessage());
            }
        });
    }

    public boolean isContain(JSONObject jsonObject, String key) {

        return jsonObject != null && jsonObject.has(key) && !jsonObject.isNull(key) ? true : false;
    }

    public void requestCityData(int id) {
        AuthService webService =
                AuthService.retrofitForCity.create(AuthService.class);
        Call<City> call = webService.cities(id);
        sendCityRequest(call);
    }

    City city;


    public void sendCityRequest(Call<City> call) {

        citySpinnerList.add(new CitySpinner("0", "Select State"));
        call.enqueue(new Callback<City>() {
            @Override
            public void onResponse(Call<City> call, Response<City> response) {

                city = response.body();
                dataList = city.getData();
                if (dataList != null) {
                    for (Data item : dataList
                    ) {
                        String id = item.getId();
                        mCity = id;
                        String name = item.getName();
                        citySpinnerList.add(new CitySpinner(id, name));
                    }
                }

                Log.d("Message", city.toString());

            }

            @Override
            public void onFailure(Call<City> call, Throwable t) {

            }
        });
    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.radio_male:
                if (checked)
                    mGender = "01";
                break;
            case R.id.radio_female:
                if (checked)
                    mGender = "02";
                break;
            case R.id.radio_other:
                if (checked)
                    mGender = "03";
                break;
        }
    }

    @Override
    public void onButtonClicked(String text) {
        if (otpExpire != null) {
            if (otpExpire.equalsIgnoreCase("OTP time is expired")) {
                String userId = user.userId;
                resendSignUpOTP(userId);
            }
        } else {
            String userId = user.userId;
            resendSignUpOTP(userId);
//            requestResendEmail();
        }
    }

    private void resendSignUpOTP(String userId) {
        AuthService webService =
                AuthService.retrofitBase.create(AuthService.class);
        Call<String> call = webService.resendSignUpOTP(userId);
        sendResendOTPRequest(call);
    }

    private void sendResendOTPRequest(Call<String> call) {


        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String data = response.body();
                // String message = data.getMessage();
                // Log.d("Message", message);
                showStatus(data);
//                if (flipperId == 1) {
//                    flipperId++;
//                    mViewFlipper.setInAnimation(slideLeftIn);
//                    mViewFlipper.setOutAnimation(slideLeftOut);
//                    mViewFlipper.showNext();
//
//                }
                //  startActivity(new Intent(Signup.this,ForgotPassword.class));
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });


    }

    private void requestResendEmail() {
        AuthService webService =
                AuthService.retrofitBase.create(AuthService.class);
        Call<ResendStatus> call = webService.resendEmail(user.userId);
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
//                if (flipperId == 1) {
//                    flipperId++;
//                    mViewFlipper.setInAnimation(slideLeftIn);
//                    mViewFlipper.setOutAnimation(slideLeftOut);
//                    mViewFlipper.showNext();
//
//                }
                //  startActivity(new Intent(Signup.this,ForgotPassword.class));
            }

            @Override
            public void onFailure(Call<ResendStatus> call, Throwable t) {

            }
        });

    }

    private void showStatus(String message) {
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.signupContainer), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    private void facebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (networkOk) {
                    loadUserProfile(loginResult.getAccessToken());
                } else {
                    Toast.makeText(getApplicationContext(), "no internet!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void loginToTwitter() {
        final ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(mConsumerKey);
        builder.setOAuthConsumerSecret(mConsumerSecret);

        final Configuration configuration = builder.build();
        final TwitterFactory factory = new TwitterFactory(configuration);
        mTwitter = factory.getInstance();
        try {
            mRequestToken = mTwitter.getOAuthRequestToken(mCallbackUrl);
            startWebAuthentication();
        } catch (TwitterException e) {
            e.printStackTrace();
            Log.d("FA", "FA");
        }
    }

    protected void startWebAuthentication() {
        final Intent intent = new Intent(Signup.this,
                TwitterAuthenticationActivity.class);
        intent.putExtra(TwitterAuthenticationActivity.EXTRA_URL,
                mRequestToken.getAuthenticationURL());
        startActivityForResult(intent, WEBVIEW_REQUEST_CODE);
    }

    private void loadUserProfile(AccessToken newAccessToken) {

        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    Log.d("DATA", object.toString());

                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    String email = object.getString("email");
                    String oauthId = object.getString("id");
                    String image_url = "https://graph.facebook.com/" + oauthId + "/picture?type=normal";
                    String name = object.getString("name");

                    socialInfo.setAuthId(oauthId);
                    socialInfo.setFirstName(first_name);
                    socialInfo.setLastName(last_name);
                    socialInfo.setEmail(email);
                    socialInfo.setSocialName(name);
                    socialInfo.setImage(image_url);
                    socialInfo.setProvider("facebook");
                    setDate();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        Bundle paramBundle = new Bundle();
//        paramBundle.putString("fields", "first_name,last_name,email,id");
        paramBundle.putString("fields", "first_name,last_name,email,id,name,gender,birthday");
        request.setParameters(paramBundle);
        request.executeAsync();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WEBVIEW_REQUEST_CODE) {
            if (data != null)
                mTwitterVerifier = data.getExtras().getString(mAuthVerifier);

            twitter4j.auth.AccessToken accessToken;
            try {
                accessToken = mTwitter.getOAuthAccessToken(mRequestToken,
                        mTwitterVerifier);

                long userID = accessToken.getUserId();
                socialInfo.setAuthId(String.valueOf(userID));
                final twitter4j.User user = mTwitter.showUser(userID);
                Log.d("IDS", user.getId() + "");
                String imageUrl = user.getProfileImageURL();
                socialInfo.setImage(imageUrl);
                String username = user.getName();
                socialInfo.setFirstName(username);
                socialInfo.setLastName(username);
                socialInfo.setSocialName(username);
                socialInfo.setProvider("twitter");
                Log.d("Description", user.getDescription());
                setDate();

            } catch (Exception e) {
            }
        }
    }
}
