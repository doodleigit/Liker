package com.doodle.Profile.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.doodle.Profile.adapter.AddEmailAdapter;
import com.doodle.Profile.adapter.AddPhoneAdapter;
import com.doodle.Profile.adapter.AddStoryAdapter;
import com.doodle.Profile.adapter.AwardsAdapter;
import com.doodle.Profile.adapter.CertificationAdapter;
import com.doodle.Profile.adapter.EducationAdapter;
import com.doodle.Profile.adapter.EmailAdapter;
import com.doodle.Profile.adapter.ExperienceAdapter;
import com.doodle.Profile.adapter.ExperienceSuggestionAdapter;
import com.doodle.Profile.adapter.PhoneAdapter;
import com.doodle.Profile.adapter.SocialAdapter;
import com.doodle.Profile.adapter.StoryAdapter;
import com.doodle.Profile.adapter.SuggestionAdapter;
import com.doodle.Profile.model.Awards;
import com.doodle.Profile.model.Certification;
import com.doodle.Profile.model.Cities;
import com.doodle.Profile.model.City;
import com.doodle.Profile.model.Country;
import com.doodle.Profile.model.CountryInfo;
import com.doodle.Profile.model.Education;
import com.doodle.Profile.model.Email;
import com.doodle.Profile.model.Experience;
import com.doodle.Profile.model.AdvanceSuggestion;
import com.doodle.Profile.model.Links;
import com.doodle.Profile.model.Phone;
import com.doodle.Profile.model.PhoneCountry;
import com.doodle.Profile.model.ProfileInfo;
import com.doodle.Profile.model.Story;
import com.doodle.Profile.service.AboutComponentUpdateListener;
import com.doodle.Profile.service.EmailModificationListener;
import com.doodle.Profile.service.PhoneModificationListener;
import com.doodle.Profile.service.ProfileService;
import com.doodle.Profile.service.StoryModificationListener;
import com.doodle.Profile.service.SuggestionClickListener;
import com.doodle.R;
import com.doodle.Tool.PrefManager;
import com.doodle.Tool.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutFragment extends Fragment {

    View view;

    private LinearLayout birthDayLayout, birthYearLayout, emailLayout, phoneLayout, addressLayout;
    private TextView tvFullName, tvIntro, tvGender, tvBirthDay, tvBirthYear, tvAddress, tvCity, tvCountry;
    private ImageView ivEditUserInfo, ivAddEducation, ivAddExperience, ivAddAwards, ivAddCertificate, ivAddSocialLinks;
    private RecyclerView emailRecyclerView, phoneRecyclerView, storyRecyclerView, educationRecyclerView, experienceRecyclerView, awardsRecyclerView, certificationRecyclerView, socialRecyclerView;

    private AlertDialog.Builder alertDialog;
    private ProgressDialog progressDialog;

    private ProfileService profileService;
    private PrefManager manager;

    private ProfileInfo profileInfo;
    private ArrayList<CountryInfo> countries;
    private ArrayList<Email> emails;
    private ArrayList<Phone> phones;
    private ArrayList<Story> stories;
    private ArrayList<Education> educations;
    private ArrayList<Experience> experiences;
    private ArrayList<Awards> awards;
    private ArrayList<Certification> certifications;
    private ArrayList<Links> links;
    private ArrayList<PhoneCountry> phoneCountries;

    private String deviceId, profileUserId, token, userId;
    private boolean isOwnProfile;

    private EmailAdapter emailAdapter;
    private PhoneAdapter phoneAdapter;
    private StoryAdapter storyAdapter;
    private EducationAdapter educationAdapter;
    private ExperienceAdapter experienceAdapter;
    private AwardsAdapter awardsAdapter;
    private CertificationAdapter certificationAdapter;
    private SocialAdapter socialAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.about_layout, container, false);

        initialComponent();
        initialAllClickListener();
        getData();

        return view;
    }

    private void initialComponent() {
        profileService = ProfileService.mRetrofit.create(ProfileService.class);
        manager = new PrefManager(getContext());

        privacyList = Arrays.asList(getResources().getStringArray(R.array.privacy_list));
        countries = new ArrayList<>();
        emails = new ArrayList<>();
        phones = new ArrayList<>();
        stories = new ArrayList<>();
        educations = new ArrayList<>();
        experiences = new ArrayList<>();
        awards = new ArrayList<>();
        certifications = new ArrayList<>();
        links = new ArrayList<>();
        phoneCountries = new ArrayList<>();

        profileUserId = getArguments().getString("user_id");
        deviceId = manager.getDeviceId();
        token = manager.getToken();
        userId = manager.getProfileId();

        alertDialog = new AlertDialog.Builder(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();

        birthDayLayout = view.findViewById(R.id.birth_day_layout);
        birthYearLayout = view.findViewById(R.id.birth_year_layout);
        emailLayout = view.findViewById(R.id.email_layout);
        phoneLayout = view.findViewById(R.id.phone_layout);
        addressLayout = view.findViewById(R.id.address_layout);
        tvFullName = view.findViewById(R.id.user_name);
        tvIntro = view.findViewById(R.id.intro);
        tvGender = view.findViewById(R.id.gender);
        tvBirthDay = view.findViewById(R.id.birth_day);
        tvBirthYear = view.findViewById(R.id.birth_year);
        tvAddress = view.findViewById(R.id.address);
        tvCity = view.findViewById(R.id.city);
        tvCountry = view.findViewById(R.id.country);
        ivEditUserInfo = view.findViewById(R.id.edit_user_info);
        ivAddEducation = view.findViewById(R.id.add_education);
        ivAddExperience = view.findViewById(R.id.add_experience);
        ivAddAwards = view.findViewById(R.id.add_awards);
        ivAddCertificate = view.findViewById(R.id.add_certificate);
        ivAddSocialLinks = view.findViewById(R.id.add_social_links);
        emailRecyclerView = view.findViewById(R.id.email_recycler_view);
        emailRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        emailRecyclerView.setNestedScrollingEnabled(false);
        phoneRecyclerView = view.findViewById(R.id.phone_recycler_view);
        phoneRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        phoneRecyclerView.setNestedScrollingEnabled(false);
        storyRecyclerView = view.findViewById(R.id.storyRecyclerView);
        storyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        storyRecyclerView.setNestedScrollingEnabled(false);
        educationRecyclerView = view.findViewById(R.id.educationRecyclerView);
        educationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        educationRecyclerView.setNestedScrollingEnabled(false);
        experienceRecyclerView = view.findViewById(R.id.experienceRecyclerView);
        experienceRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        experienceRecyclerView.setNestedScrollingEnabled(false);
        awardsRecyclerView = view.findViewById(R.id.awardsRecyclerView);
        awardsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        awardsRecyclerView.setNestedScrollingEnabled(false);
        certificationRecyclerView = view.findViewById(R.id.certificationRecyclerView);
        certificationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        certificationRecyclerView.setNestedScrollingEnabled(false);
        socialRecyclerView = view.findViewById(R.id.socialRecyclerView);
        socialRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        socialRecyclerView.setNestedScrollingEnabled(false);

        ownProfileCheck();

        AboutComponentUpdateListener aboutComponentUpdateListener = new AboutComponentUpdateListener() {
            @Override
            public void onEducationUpdate(Education education) {
                addEducation(true, education.getId(), education);
            }

            @Override
            public void onExperienceUpdate(Experience experience) {
                addExperience(true, experience.getId(), experience);
            }

            @Override
            public void onAwardsUpdate(Awards awards) {
                addAwards(true, awards.getId(), awards);
            }

            @Override
            public void onCertificationUpdate(Certification certification) {
                addCertification(true, certification.getId(), certification);
            }

            @Override
            public void onLinkUpdate(Links lnk) {
                addSocialSites(true, lnk);
            }
        };

        emailAdapter = new EmailAdapter(getActivity(), emails, isOwnProfile);
        phoneAdapter = new PhoneAdapter(getActivity(), phones, isOwnProfile);
        storyAdapter = new StoryAdapter(getActivity(), stories);
        educationAdapter = new EducationAdapter(getActivity(), educations, aboutComponentUpdateListener, isOwnProfile);
        experienceAdapter = new ExperienceAdapter(getActivity(), experiences, aboutComponentUpdateListener, isOwnProfile);
        awardsAdapter = new AwardsAdapter(getActivity(), awards, aboutComponentUpdateListener, isOwnProfile);
        certificationAdapter = new CertificationAdapter(getActivity(), certifications, aboutComponentUpdateListener, isOwnProfile);
        socialAdapter = new SocialAdapter(getActivity(), links, aboutComponentUpdateListener, isOwnProfile);

        emailRecyclerView.setAdapter(emailAdapter);
        phoneRecyclerView.setAdapter(phoneAdapter);
        storyRecyclerView.setAdapter(storyAdapter);
        educationRecyclerView.setAdapter(educationAdapter);
        experienceRecyclerView.setAdapter(experienceAdapter);
        awardsRecyclerView.setAdapter(awardsAdapter);
        certificationRecyclerView.setAdapter(certificationAdapter);
        socialRecyclerView.setAdapter(socialAdapter);
    }

    private void initialAllClickListener() {
        ivEditUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editUserInfo();
            }
        });
        ivAddEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEducation(false, "", null);
            }
        });
        ivAddExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExperience(false, "", null);
            }
        });
        ivAddAwards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAwards(false, "", null);
            }
        });
        ivAddCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCertification(false, "", null);
            }
        });
        ivAddSocialLinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSocialSites(false, null);
            }
        });

    }

    private void ownProfileCheck() {
        if (userId.equals(profileUserId)) {
            isOwnProfile = true;
            ivEditUserInfo.setVisibility(View.VISIBLE);
            ivAddEducation.setVisibility(View.VISIBLE);
            ivAddAwards.setVisibility(View.VISIBLE);
            ivAddCertificate.setVisibility(View.VISIBLE);
            ivAddExperience.setVisibility(View.VISIBLE);
            ivAddSocialLinks.setVisibility(View.VISIBLE);
        } else {
            isOwnProfile = false;
            ivEditUserInfo.setVisibility(View.INVISIBLE);
            ivAddEducation.setVisibility(View.INVISIBLE);
            ivAddAwards.setVisibility(View.INVISIBLE);
            ivAddCertificate.setVisibility(View.INVISIBLE);
            ivAddExperience.setVisibility(View.INVISIBLE);
            ivAddSocialLinks.setVisibility(View.INVISIBLE);
        }
    }

    private void setData() {
        tvFullName.setText(profileInfo.getFirstName() + " " + profileInfo.getLastName());
//        if (!profileInfo.getIntro().equals("")) {
//            tvIntro.setText(profileInfo.getIntro());
//            tvIntro.setVisibility(View.VISIBLE);
//        } else {
//            tvIntro.setVisibility(View.GONE);
//        }
        tvIntro.setVisibility(View.GONE);
        tvGender.setText(profileInfo.getSex().equals("1") ? "Male" : "Female");
        tvBirthDay.setText(profileInfo.getBirthDate() + " " + Tools.getMonth(profileInfo.getBirthMonth()));
        tvBirthYear.setText(profileInfo.getBirthYear());
        tvAddress.setText(profileInfo.getAddress().isEmpty() ? "Not yet" : profileInfo.getAddress());
        tvCity.setText(profileInfo.getCurrentCityCity().isEmpty() ? "Not yet" : profileInfo.getCurrentCityCity());
        tvCountry.setText(profileInfo.getCurrentCityCountry().isEmpty() ? "Not yet" : profileInfo.getCurrentCityCountry());
    }

    private void getData() {
        Call<String> call = profileService.getProfileInfo(deviceId, token, userId, profileUserId, userId);
        sendProfileInfoRequest(call);
        sendPhoneCountryListRequest();
    }

    private void clearData() {
        emails.clear();
        phones.clear();
        stories.clear();
        educations.clear();
        experiences.clear();
        awards.clear();
        certifications.clear();
        links.clear();
    }

    private void notifyAllAdapter() {
        emailAdapter.notifyDataSetChanged();
        phoneAdapter.notifyDataSetChanged();
        educationAdapter.notifyDataSetChanged();
        experienceAdapter.notifyDataSetChanged();
        awardsAdapter.notifyDataSetChanged();
        certificationAdapter.notifyDataSetChanged();
        socialAdapter.notifyDataSetChanged();
    }

    private void ownProfileComponentVisibility() {
        if (emails.size() == 0) {
            emailLayout.setVisibility(View.GONE);
        }
        if (phones.size() == 0) {
            phoneLayout.setVisibility(View.GONE);
        }
    }

    String numberType = "", storyType = "", phoneNumberPrivacyType = "", emailPrivacyType = "", storyPrivacyType = "";
    Phone phone;

    String instituteName = "", instituteType = "", designationName = "", companyName = "", awardsName = "", certificationName = "", licenseNumber = "", certificationUrl = "", degreeName = "", oldLink = "", link = "", type = "",
            fieldStudyName = "", websiteUrl = "", locationName = "", permissionType = "", grade = "", date = "", startYear = "", endYear = "", fromDate = "", toDate = "", description = "", media = "",
            locationActualName = "", locationCountryName = "", locationLatitude = "", locationLongitude = "";
    boolean currentlyWorked, isExpired;
    List<String> privacyList;

    private void editUserInfo() {
        Dialog dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        dialog.setContentView(R.layout.edit_user_info_layout);

        final String[] year = {""};
        final String[] month = {""};
        final String[] day = {""};
        final String[] gender = {""};
        final String[] livesCountryId = {""};
        final String[] livesCountryName = {""};
        final String[] homeCountryId = {""};
        final String[] homeCountryName = {""};
        final String[] livesCityId = {""};
        final String[] livesCityName = {""};
        final String[] homeCityId = {""};
        final String[] homeCityName = {""};
        final String[] yearPermission = {""};
        final String[] dayMonthPermission = {""};
        List<String> phoneTypes = Arrays.asList(getResources().getStringArray(R.array.phone_type_list));
        List<String> storyTypes = Arrays.asList(getResources().getStringArray(R.array.story_type_list));
        List<String> types = new ArrayList<>();
        List<String> genders = Arrays.asList(getResources().getStringArray(R.array.gender_list));
        List<String> months = Arrays.asList(getResources().getStringArray(R.array.month_list));
        ArrayList<String> years = new ArrayList<>();
        ArrayList<String> days = new ArrayList<>();
        ArrayList<String> countryList = new ArrayList<>();
        ArrayList<City> livesCityList = new ArrayList<>();
        ArrayList<City> homeCityList = new ArrayList<>();
        ArrayList<String> livesCityNameList = new ArrayList<>();
        ArrayList<String> homeCityNameList = new ArrayList<>();
        years.add(getString(R.string.select_year));
        days.add(getString(R.string.select_day));
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        int totalDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        for (int i = 1930; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }
        for (int i = 1; i <= totalDay; i++) {
            days.add(Integer.toString(i));
        }
        for (CountryInfo countryInfo : countries) {
            countryList.add(countryInfo.getCountryName());
        }

        Toolbar toolbar = dialog.findViewById(R.id.toolbar);
        LinearLayout addEmailLayout, addPhoneLayout, addLivesInLayout, addHomeStateLayout, addStoryLayout;
        RelativeLayout livesInAddressLayout, homeStateAddressLayout;
        TextView tvStoryTitle, tvAddEmail, tvEmailVerificationMessage, tvAddPhone, tvAddStory, tvLivesInAddress, tvHomeStateAddress;
        ImageView ivLivesInEdit, ivHomeStateEdit;
        EditText etFirstName, etLastName, etHeadline, etAddress, etNewEmail, etNewPhone, etStoryDetails;
        Spinner genderSpinner, birthYearSpinner, birthMonthSpinner, birthDaySpinner, birthYearPrivacySpinner, birthDayPrivacySpinner, emailPrivacySpinner, phoneCountrySpinner, phonePrivacySpinner, phoneTypeSpinner, livesCountrySpinner, livesStateSpinner, homeCountrySpinner,
                homeStateSpinner, storyPrivacySpinner, addStorySpinner;
        RecyclerView emailRecyclerView, phoneRecyclerView, storyRecycleView;
        Button btnEmailCancel, btnEmailSave, btnPhoneCancel, btnPhoneSave, btnLivesCancel, btnLivesSave, btnHomeStateCancel, btnHomeStateSave, btnStoryCancel, btnStorySave;
        FloatingActionButton fabDone;

        addEmailLayout = dialog.findViewById(R.id.add_email_layout);
        addPhoneLayout = dialog.findViewById(R.id.add_phone_layout);
        addLivesInLayout = dialog.findViewById(R.id.add_lives_in_layout);
        addHomeStateLayout = dialog.findViewById(R.id.add_home_state_layout);
        addStoryLayout = dialog.findViewById(R.id.add_story_layout);

        livesInAddressLayout = dialog.findViewById(R.id.lives_in_address_layout);
        homeStateAddressLayout = dialog.findViewById(R.id.home_state_address_layout);

        tvStoryTitle = dialog.findViewById(R.id.story_title);
        tvAddEmail = dialog.findViewById(R.id.add_email);
        tvEmailVerificationMessage = dialog.findViewById(R.id.email_verification_message);
        tvAddPhone = dialog.findViewById(R.id.add_phone);
        tvAddStory = dialog.findViewById(R.id.add_story);
        tvLivesInAddress = dialog.findViewById(R.id.lives_in_address);
        tvHomeStateAddress = dialog.findViewById(R.id.home_state_address);

        ivLivesInEdit = dialog.findViewById(R.id.lives_in_edit);
        ivHomeStateEdit = dialog.findViewById(R.id.home_state_edit);

        etFirstName = dialog.findViewById(R.id.first_name);
        etLastName = dialog.findViewById(R.id.last_name);
        etHeadline = dialog.findViewById(R.id.headline);
        etAddress = dialog.findViewById(R.id.address);
        etNewEmail = dialog.findViewById(R.id.new_email);
        etNewPhone = dialog.findViewById(R.id.new_phone);
        etStoryDetails = dialog.findViewById(R.id.story_details);

        genderSpinner = dialog.findViewById(R.id.gender_spinner);
        birthYearSpinner = dialog.findViewById(R.id.birth_year_spinner);
        birthMonthSpinner = dialog.findViewById(R.id.birth_month_spinner);
        birthDaySpinner = dialog.findViewById(R.id.birth_day_spinner);
        birthYearPrivacySpinner = dialog.findViewById(R.id.birth_year_privacy_spinner);
        birthDayPrivacySpinner = dialog.findViewById(R.id.birth_day_privacy_spinner);
        emailPrivacySpinner = dialog.findViewById(R.id.email_privacy_spinner);
        phoneCountrySpinner = dialog.findViewById(R.id.phone_country_spinner);
        phonePrivacySpinner = dialog.findViewById(R.id.phone_privacy_spinner);
        phoneTypeSpinner = dialog.findViewById(R.id.phone_type_spinner);
        livesCountrySpinner = dialog.findViewById(R.id.lives_country_spinner);
        livesStateSpinner = dialog.findViewById(R.id.lives_state_spinner);
        homeCountrySpinner = dialog.findViewById(R.id.home_country_spinner);
        homeStateSpinner = dialog.findViewById(R.id.home_state_spinner);
        storyPrivacySpinner = dialog.findViewById(R.id.story_privacy_spinner);
        addStorySpinner = dialog.findViewById(R.id.add_story_spinner);

        emailRecyclerView = dialog.findViewById(R.id.email_recycler_view);
        emailRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        phoneRecyclerView = dialog.findViewById(R.id.phone_recycler_view);
        phoneRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        storyRecycleView = dialog.findViewById(R.id.story_recycler_view);
        storyRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        btnEmailCancel = dialog.findViewById(R.id.email_cancel);
        btnEmailSave = dialog.findViewById(R.id.email_save);
        btnPhoneCancel = dialog.findViewById(R.id.phone_cancel);
        btnPhoneSave = dialog.findViewById(R.id.phone_save);
        btnLivesCancel = dialog.findViewById(R.id.lives_cancel);
        btnLivesSave = dialog.findViewById(R.id.lives_save);
        btnHomeStateCancel = dialog.findViewById(R.id.home_state_cancel);
        btnHomeStateSave = dialog.findViewById(R.id.home_state_save);
        btnStoryCancel = dialog.findViewById(R.id.story_cancel);
        btnStorySave = dialog.findViewById(R.id.story_save);
        fabDone = dialog.findViewById(R.id.done);

        ArrayList<String> phoneCountryCodes = new ArrayList<>();
        for (PhoneCountry phoneCountry : phoneCountries) {
            if (phoneCountry.getCountryName().equals("Select")) {
                phoneCountryCodes.add(phoneCountry.getCountryName());
            } else {
                phoneCountryCodes.add(phoneCountry.getCountryIsoCode2() + "(" + phoneCountry.getCountryPhoneCode() + ")");
            }

        }
        ArrayAdapter<String> phoneCountryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, phoneCountryCodes);
        phoneCountryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        phoneCountrySpinner.setAdapter(phoneCountryAdapter);

        ArrayAdapter<String> phoneTypesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, phoneTypes);
        phoneTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        phoneTypeSpinner.setAdapter(phoneTypesAdapter);

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, genders);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        ArrayAdapter<String> birthYearAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, years);
        birthYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        birthYearSpinner.setAdapter(birthYearAdapter);

        ArrayAdapter<String> birthMonthAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, months);
        birthMonthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        birthMonthSpinner.setAdapter(birthMonthAdapter);

        ArrayAdapter<String> birthDayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, days);
        birthDayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        birthDaySpinner.setAdapter(birthDayAdapter);

        ArrayAdapter<String> countryListAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, countryList);
        countryListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        livesCountrySpinner.setAdapter(countryListAdapter);
        homeCountrySpinner.setAdapter(countryListAdapter);

        ArrayAdapter<String> privacyAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, privacyList);
        privacyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        birthYearPrivacySpinner.setAdapter(privacyAdapter);
        birthDayPrivacySpinner.setAdapter(privacyAdapter);
        emailPrivacySpinner.setAdapter(privacyAdapter);
        phonePrivacySpinner.setAdapter(privacyAdapter);

        EmailModificationListener emailModificationListener = new EmailModificationListener() {
            @Override
            public void onEmailRemove(Email email, int position) {
                Call<String> call = profileService.removeEmail(deviceId, token, userId, userId, email.getEmail());
                removeEmail(call, position, emailRecyclerView);
            }

            @Override
            public void onEmailResendVerification(Email email) {
                Call<String> call = profileService.emailVerificationSend(deviceId, token, userId, userId, email.getEmail());
                emailVerificationSend(call, email, tvEmailVerificationMessage);
            }
        };

        PhoneModificationListener phoneModificationListener = new PhoneModificationListener() {
            @Override
            public void onPhoneEdit(Phone phone) {

            }

            @Override
            public void onPhoneRemove(Phone phone, int position) {
                Call<String> call = profileService.removePhone(deviceId, token, userId, userId, phone.getPhoneNumber());
                removePhone(call, position, phoneRecyclerView);
            }
        };

        StoryModificationListener storyModificationListener = new StoryModificationListener() {
            @Override
            public void onStoryEdit(Story story) {

            }
        };

        AddEmailAdapter addEmailAdapter = new AddEmailAdapter(getActivity(), profileInfo.getEmails(), emailModificationListener);
        emailRecyclerView.setAdapter(addEmailAdapter);

        AddPhoneAdapter addPhoneAdapter = new AddPhoneAdapter(getActivity(), profileInfo.getPhones(), phoneModificationListener);
        phoneRecyclerView.setAdapter(addPhoneAdapter);

        AddStoryAdapter addStoryAdapter = new AddStoryAdapter(getActivity(), profileInfo.getStories(), storyModificationListener);
        storyRecycleView.setAdapter(addStoryAdapter);

        etFirstName.setText(profileInfo.getFirstName());
        etLastName.setText(profileInfo.getLastName());
        etHeadline.setText(profileInfo.getHeadLine());
        etAddress.setText(profileInfo.getAddress());
        if (!profileInfo.getCurrentCityCity().equals("")) {
            tvLivesInAddress.setText(profileInfo.getCurrentCityCity() + ", " + profileInfo.getCurrentCityCountry());
        } else {
            tvLivesInAddress.setText("Not Yet");
        }
        genderSpinner.setSelection(Integer.valueOf(profileInfo.getSex()));

        for (int i = 0; i < years.size(); i++) {
            if (years.get(i).equals(profileInfo.getBirthYear())) {
                birthYearSpinner.setSelection(i);
                break;
            }
        }

        for (int i = 0; i < days.size(); i++) {
            if (days.get(i).equals(profileInfo.getBirthDate())) {
                birthDaySpinner.setSelection(i);
                break;
            }
        }

        birthMonthSpinner.setSelection(Integer.valueOf(profileInfo.getBirthMonth()));
        birthYearPrivacySpinner.setSelection(Integer.valueOf(profileInfo.getYearPermission()));
        birthDayPrivacySpinner.setSelection(Integer.valueOf(profileInfo.getDayMonthPermission()));

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                gender[0] = String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        birthYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                year[0] = years.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        birthMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                month[0] = String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        birthDaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                day[0] = String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        birthYearPrivacySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                yearPermission[0] = String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        birthDayPrivacySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                dayMonthPermission[0] = String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        livesCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                livesCountryId[0] = countries.get(i).getCountryId();
                livesCountryName[0] = countries.get(i).getCountryName();
                sendCityListRequest(livesCountryId[0], livesCityList, livesCityNameList, livesStateSpinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        homeCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                homeCountryId[0] = countries.get(i).getCountryId();
                homeCountryName[0] = countries.get(i).getCountryName();
                sendCityListRequest(homeCountryId[0], homeCityList, homeCityNameList, homeStateSpinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        livesStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                livesCityId[0] = livesCityList.get(i).getId();
                livesCityName[0] = livesCityList.get(i).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        homeStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                homeCityId[0] = homeCityList.get(i).getId();
                homeCityName[0] = homeCityList.get(i).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tvAddEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEmailLayout.setVisibility(View.VISIBLE);
            }
        });

        btnEmailCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEmailLayout.setVisibility(View.GONE);
                etNewEmail.setText("");
            }
        });

        btnEmailSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAddress = etNewEmail.getText().toString();
                addEmailRequest(emailAddress, emailPrivacyType, addEmailAdapter, etNewEmail, addEmailLayout);
            }
        });

        tvAddPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPhoneLayout.setVisibility(View.VISIBLE);
            }
        });

        btnPhoneCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPhoneLayout.setVisibility(View.GONE);
                etNewPhone.setText("");
                phoneCountrySpinner.setSelection(0);
            }
        });

        btnPhoneSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = etNewPhone.getText().toString();
                addPhoneRequest(phoneNumber, numberType, phoneNumberPrivacyType, phone, addPhoneAdapter, etNewPhone, phoneCountrySpinner, addPhoneLayout);
            }
        });

        ivLivesInEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLivesInLayout.setVisibility(View.VISIBLE);
                livesInAddressLayout.setVisibility(View.GONE);
            }
        });

        btnLivesCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLivesInLayout.setVisibility(View.GONE);
                livesInAddressLayout.setVisibility(View.VISIBLE);
//                livesCountrySpinner.setSelection(0);
//                livesStateSpinner.setSelection(0);
            }
        });

        btnLivesSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLivePlaceRequest("1", "1", livesCountryId[0], livesCountryName[0], livesCityId[0], livesCityName[0], addLivesInLayout, livesInAddressLayout,
                        tvLivesInAddress);
            }
        });

        ivHomeStateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addHomeStateLayout.setVisibility(View.VISIBLE);
                homeStateAddressLayout.setVisibility(View.GONE);
            }
        });

        btnHomeStateCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addHomeStateLayout.setVisibility(View.GONE);
                homeStateAddressLayout.setVisibility(View.VISIBLE);
//                homeCountrySpinner.setSelection(0);
//                homeStateSpinner.setSelection(0);
            }
        });

        btnHomeStateSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLivePlaceRequest("2", "1", homeCountryId[0], homeCountryName[0], homeCityId[0], homeCityName[0], addHomeStateLayout, homeStateAddressLayout,
                        tvHomeStateAddress);
            }
        });

        tvAddStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                types.clear();
                for (int i = 0; i < storyTypes.size(); i++) {
                    for (Story story : profileInfo.getStories()) {
                        if (!story.getType().equals(String.valueOf(i + 1))) {
                            types.add(storyTypes.get(i));
                        }
                    }
                }
                ArrayAdapter<String> storyTypeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, types);
                storyTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                addStorySpinner.setAdapter(storyTypeAdapter);
                addStorySpinner.performClick();
            }
        });

        btnStoryCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStoryLayout.setVisibility(View.GONE);
                etStoryDetails.setText("");
            }
        });

        btnStorySave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = etStoryDetails.getText().toString();
                setStoryRequest(storyType, storyPrivacyType, description, addStoryAdapter, etStoryDetails, addStoryLayout);
            }
        });

        phoneCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                phone = new Phone("", phoneCountries.get(position).getCountryId(), "", "",
                        "1", phoneCountries.get(position).getCountryName(), phoneCountries.get(position).getCountryIsoCode2(), phoneCountries.get(position).getCountryPhoneCode());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        phoneTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                numberType = String.valueOf(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        emailPrivacySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                emailPrivacyType = String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        phonePrivacySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                phoneNumberPrivacyType = String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        storyPrivacySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                storyPrivacyType = String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addStorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                for (int i = 0; i < storyTypes.size(); i++) {
                    if (storyTypes.get(i).equals(types.get(position))) {
                        storyType = String.valueOf(i + 1);
                    }
                }
                tvStoryTitle.setText(storyTypes.get(position + 1));
                addStoryLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                user_id: 26444
//                first_name: June
//                last_name: Ramirez
//                headline: Hello Hi Bye Bye
//                sex: 1
//                address: dhaka
//                location_name:
//                year: 1990
//                month: 01
//                date: 14
//                year_permission: 0
//                day_month_permission: 0
//                location_actual_name:
//                location_latitude:
//                location_longitude:
//                location_country_name:
                String firstName, lastName, headline, address;
                firstName = etFirstName.getText().toString();
                lastName = etLastName.getText().toString();
                headline = etHeadline.getText().toString();
                address = etAddress.getText().toString();

                Call<String> call = profileService.setIntro(deviceId, token, userId, userId, firstName, lastName, headline, gender[0], address, "", year[0], month[0], day[0], yearPermission[0],
                        dayMonthPermission[0], "", "", "", "");
                sendSetIntroRequest(call, dialog);
            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void addEducation(boolean isUpdate, String educationId, Education education) {
        Dialog dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        dialog.setContentView(R.layout.add_education_layout);

        instituteName = "";
        degreeName = "";
        fieldStudyName = "";
        websiteUrl = "";
        locationName = "";
        permissionType = "0";
        grade = "";
        startYear = "";
        endYear = "";
        description = "";
        locationActualName = "";
        locationCountryName = "";
        locationLatitude = "";
        locationLongitude = "";

        ArrayList<AdvanceSuggestion> advanceSuggestions = new ArrayList<>();
        ArrayList<String> searchLocations = new ArrayList<>();
        List<String> instituteList = Arrays.asList(getResources().getStringArray(R.array.institute_list));
        ArrayList<String> years = new ArrayList<>();
        years.add(getString(R.string.select_year));
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 1930; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }

        Toolbar toolbar = dialog.findViewById(R.id.toolbar);
        Button btnRemove;
        EditText etInstituteName, etSearchPlace, etSiteAddress, etDegree, etStudyMajor, etGrade, etSummary;
        Spinner instituteSpinner, fromYearSpinner, toYearSpinner, privacySpinner;
        FloatingActionButton fabDone;
        RecyclerView institutionNameRecyclerView, searchPlaceRecyclerView;

        btnRemove = dialog.findViewById(R.id.remove);
        etInstituteName = dialog.findViewById(R.id.institute_name);
        etSearchPlace = dialog.findViewById(R.id.search_place);
        etSiteAddress = dialog.findViewById(R.id.site_address);
        etDegree = dialog.findViewById(R.id.degree);
        etStudyMajor = dialog.findViewById(R.id.study_major);
        etGrade = dialog.findViewById(R.id.grade);
        etSummary = dialog.findViewById(R.id.summary);

        instituteSpinner = dialog.findViewById(R.id.institute_spinner);
        fromYearSpinner = dialog.findViewById(R.id.from_year_spinner);
        toYearSpinner = dialog.findViewById(R.id.to_year_spinner);
        privacySpinner = dialog.findViewById(R.id.privacy_spinner);

        fabDone = dialog.findViewById(R.id.done);
        institutionNameRecyclerView = dialog.findViewById(R.id.institution_name_recycler_view);
        institutionNameRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchPlaceRecyclerView = dialog.findViewById(R.id.search_place_recycler_view);
        searchPlaceRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        SuggestionClickListener instituteNameSuggestionClickListener = new SuggestionClickListener() {
            @Override
            public void onSuggestionClick(String suggestion) {

            }

            @Override
            public void onSuggestionClick(AdvanceSuggestion experienceSuggestion) {
                etInstituteName.setText(experienceSuggestion.getInstituteName());
                etSiteAddress.setText(experienceSuggestion.getWebsiteUrl());
                etSearchPlace.setText(experienceSuggestion.getLocationName());
                advanceSuggestions.clear();
                Objects.requireNonNull(institutionNameRecyclerView.getAdapter()).notifyDataSetChanged();
            }
        };

        SuggestionClickListener searchLocationSuggestionClickListener = new SuggestionClickListener() {
            @Override
            public void onSuggestionClick(String suggestion) {
                etSearchPlace.setText(suggestion);
                searchLocations.clear();
                Objects.requireNonNull(searchPlaceRecyclerView.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onSuggestionClick(AdvanceSuggestion experienceSuggestion) {

            }
        };

        ArrayAdapter<String> privacyAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, privacyList);
        privacyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        privacySpinner.setAdapter(privacyAdapter);

        ArrayAdapter<String> instituteAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, instituteList);
        instituteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        instituteSpinner.setAdapter(instituteAdapter);

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromYearSpinner.setAdapter(yearAdapter);
        toYearSpinner.setAdapter(yearAdapter);

        ExperienceSuggestionAdapter instituteSuggestionAdapter = new ExperienceSuggestionAdapter(getActivity(), advanceSuggestions, instituteNameSuggestionClickListener);
        SuggestionAdapter placeSuggestionAdapter = new SuggestionAdapter(getActivity(), searchLocations, searchLocationSuggestionClickListener);
        institutionNameRecyclerView.setAdapter(instituteSuggestionAdapter);
        searchPlaceRecyclerView.setAdapter(placeSuggestionAdapter);

        if (isUpdate) {
            btnRemove.setVisibility(View.VISIBLE);
            instituteType = education.getInstituteType();
            startYear = education.getStartYear();
            endYear = education.getEndYear();
            permissionType = education.getPermissionType();
            instituteSpinner.setSelection(Integer.valueOf(education.getInstituteType()));
            etInstituteName.setText(education.getInstituteName());
            etSearchPlace.setText(education.getLocationName());
            etSiteAddress.setText(education.getWebsiteUrl());
            etDegree.setText(education.getDegreeName());
            etGrade.setText(education.getGrade());
            for (int i = 0; i < years.size(); i++) {
                if (education.getStartYear().equals(String.valueOf(years.get(i)))) {
                    fromYearSpinner.setSelection(i);
                }
            }
            for (int i = 0; i < years.size(); i++) {
                if (education.getEndYear().equals(String.valueOf(years.get(i)))) {
                    toYearSpinner.setSelection(i);
                }
            }
            etSummary.setText(education.getDescription());
            privacySpinner.setSelection(Integer.valueOf(education.getPermissionType()));
        } else {
            btnRemove.setVisibility(View.GONE);
        }

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = getString(R.string.are_you_sure) + " " + "education";
                Call<String> call = profileService.removeEducation(deviceId, token, userId, userId, educationId);
                showAlert(message, call, dialog);
            }
        });

        fabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                user_id: 26444
//                institute_name: Dhaka Collage
//                institute_type: 1
//                degree_name: Hsc
//                field_study_name: Science
//                start_year: 2009
//                website_url: google.com
//                location_name: PA-59, Pennsylvania, USA
//                permission_type: 0
//                grade: A+
//                        end_year: 2011
//                description: Hello
//                location_actual_name:
//                location_country_name:
//                location_latitude:
//                location_longitude:
                instituteName = etInstituteName.getText().toString();
                degreeName = etDegree.getText().toString();
                fieldStudyName = etStudyMajor.getText().toString();
                websiteUrl = etSiteAddress.getText().toString();
                locationName = etSearchPlace.getText().toString();
                grade = etGrade.getText().toString();
                description = etSummary.getText().toString();

                if (isUpdate) {
                    Call<String> call = profileService.updateEducation(deviceId, token, userId, userId, educationId, instituteName, instituteType, degreeName, fieldStudyName, websiteUrl, locationName, permissionType,
                            grade, startYear, endYear, description, locationActualName, locationCountryName, locationLatitude, locationLongitude);
                    addEducationRequest(call, dialog);
                } else {
                    Call<String> call = profileService.addEducation(deviceId, token, userId, userId, instituteName, instituteType, degreeName, fieldStudyName, websiteUrl, locationName, permissionType,
                            grade, startYear, endYear, description, locationActualName, locationCountryName, locationLatitude, locationLongitude);
                    addEducationRequest(call, dialog);
                }

            }
        });

        etInstituteName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etInstituteName.getText().toString().length() > 2) {
                    Call<String> call = profileService.getSuggestion(deviceId, token, userId, "institute", etInstituteName.getText().toString());
                    getAdvanceSuggestion(call, advanceSuggestions, institutionNameRecyclerView);
                } else {
                    advanceSuggestions.clear();
                    Objects.requireNonNull(institutionNameRecyclerView.getAdapter()).notifyDataSetChanged();
                }
            }
        });

        etInstituteName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    advanceSuggestions.clear();
                    Objects.requireNonNull(institutionNameRecyclerView.getAdapter()).notifyDataSetChanged();
                }
            }
        });

        instituteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                instituteType = (position == 0 ? "" : String.valueOf(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fromYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                startYear = years.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        toYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                endYear = years.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        privacySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                permissionType = String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dialog.show();
    }

    private void addExperience(boolean isUpdate, String experienceId, Experience experience) {
        Dialog dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        dialog.setContentView(R.layout.add_experience_layout);

        designationName = "";
        companyName = "";
        websiteUrl = "";
        fromDate = "";
        toDate = "";
        currentlyWorked = false;
        designationName = "";
        permissionType = "0";
        description = "";
        locationName = "";
        locationActualName = "";
        locationCountryName = "";
        locationLatitude = "";
        locationLongitude = "";

        final String[] fromYear = {""};
        final String[] toYear = {""};
        final String[] fromMonth = {""};
        final String[] toMonth = {""};
        List<String> months = Arrays.asList(getResources().getStringArray(R.array.month_list));
        ArrayList<String> designations = new ArrayList<>();
        ArrayList<AdvanceSuggestion> advanceSuggestions = new ArrayList<>();
        ArrayList<String> searchLocations = new ArrayList<>();
        ArrayList<String> years = new ArrayList<>();
        years.add(getString(R.string.select_year));
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 1930; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }

        Toolbar toolbar = dialog.findViewById(R.id.toolbar);
        Button btnRemove;
        EditText etDesignation, etCompanyName, etSiteAddress, etSearchPlace, etSummary;
        Spinner fromYearSpinner, fromMonthSpinner, toYearSpinner, toMonthSpinner, privacySpinner;
        FloatingActionButton fabDone;
        CheckBox currentlyWorkingCheck;
        RecyclerView designationRecyclerView, companyNameRecyclerView, searchPlaceRecyclerView;

        btnRemove = dialog.findViewById(R.id.remove);
        etDesignation = dialog.findViewById(R.id.designation);
        etCompanyName = dialog.findViewById(R.id.company_name);
        etSiteAddress = dialog.findViewById(R.id.site_address);
        etSearchPlace = dialog.findViewById(R.id.search_place);
        etSummary = dialog.findViewById(R.id.summary);

        fromYearSpinner = dialog.findViewById(R.id.from_year_spinner);
        fromMonthSpinner = dialog.findViewById(R.id.from_month_spinner);
        toYearSpinner = dialog.findViewById(R.id.to_year_spinner);
        toMonthSpinner = dialog.findViewById(R.id.to_month_spinner);
        privacySpinner = dialog.findViewById(R.id.privacy_spinner);

        fabDone = dialog.findViewById(R.id.done);
        currentlyWorkingCheck = dialog.findViewById(R.id.currently_working_check);

        designationRecyclerView = dialog.findViewById(R.id.designation_recycler_view);
        designationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        companyNameRecyclerView = dialog.findViewById(R.id.company_name_recycler_view);
        companyNameRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchPlaceRecyclerView = dialog.findViewById(R.id.search_place_recycler_view);
        searchPlaceRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        SuggestionClickListener designationSuggestionClickListener = new SuggestionClickListener() {
            @Override
            public void onSuggestionClick(String suggestion) {
                etDesignation.setText(suggestion);
                designations.clear();
                Objects.requireNonNull(designationRecyclerView.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onSuggestionClick(AdvanceSuggestion experienceSuggestion) {

            }
        };

        SuggestionClickListener searchPlaceSuggestionClickListener = new SuggestionClickListener() {
            @Override
            public void onSuggestionClick(String suggestion) {
                etSearchPlace.setText(suggestion);
                searchLocations.clear();
                Objects.requireNonNull(searchPlaceRecyclerView.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onSuggestionClick(AdvanceSuggestion experienceSuggestion) {

            }
        };

        SuggestionClickListener companyNameSuggestionClickListener = new SuggestionClickListener() {
            @Override
            public void onSuggestionClick(String suggestion) {

            }

            @Override
            public void onSuggestionClick(AdvanceSuggestion experienceSuggestion) {
                etCompanyName.setText(experienceSuggestion.getInstituteName());
                etSiteAddress.setText(experienceSuggestion.getWebsiteUrl());
                etSearchPlace.setText(experienceSuggestion.getLocationName());
                advanceSuggestions.clear();
                Objects.requireNonNull(companyNameRecyclerView.getAdapter()).notifyDataSetChanged();
            }
        };

        ArrayAdapter<String> privacyAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, privacyList);
        privacyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        privacySpinner.setAdapter(privacyAdapter);

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromYearSpinner.setAdapter(yearAdapter);
        toYearSpinner.setAdapter(yearAdapter);

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, months);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromMonthSpinner.setAdapter(monthAdapter);
        toMonthSpinner.setAdapter(monthAdapter);

        SuggestionAdapter designationSuggestionAdapter = new SuggestionAdapter(getActivity(), designations, designationSuggestionClickListener);
        SuggestionAdapter placeSuggestionAdapter = new SuggestionAdapter(getActivity(), searchLocations, searchPlaceSuggestionClickListener);
        ExperienceSuggestionAdapter companyNameSuggestionAdapter = new ExperienceSuggestionAdapter(getActivity(), advanceSuggestions, companyNameSuggestionClickListener);
        designationRecyclerView.setAdapter(designationSuggestionAdapter);
        searchPlaceRecyclerView.setAdapter(placeSuggestionAdapter);
        companyNameRecyclerView.setAdapter(companyNameSuggestionAdapter);

        if (isUpdate) {
            btnRemove.setVisibility(View.VISIBLE);
            permissionType = experience.getPermissionType();
            etDesignation.setText(experience.getDescription());
            etCompanyName.setText(experience.getCompanyName());
            etSiteAddress.setText(experience.getWebsiteUrl());
            etSearchPlace.setText(experience.getLocationName());
            etSummary.setText(experience.getDescription());
            currentlyWorkingCheck.setChecked(experience.getCurrentlyWorked());
            toYearSpinner.setEnabled(experience.getCurrentlyWorked());
            toMonthSpinner.setEnabled(experience.getCurrentlyWorked());
            privacySpinner.setSelection(Integer.valueOf(experience.getPermissionType()));
            for (int i = 0; i < years.size(); i++) {
                if (experience.getFromYear().equals(String.valueOf(years.get(i)))) {
                    fromYearSpinner.setSelection(i);
                }
            }
            for (int i = 0; i < years.size(); i++) {
                if (experience.getToYear().equals(String.valueOf(years.get(i)))) {
                    toYearSpinner.setSelection(i);
                }
            }
//            for (int i = 0; i < months.size(); i++) {
////                if (experience.getFromMonth().equals(String.valueOf(months.get(i)))) {
////                    fromMonthSpinner.setSelection(i);
////                }
////            }
////            for (int i = 0; i < months.size(); i++) {
////                if (experience.getToMonth().equals(String.valueOf(months.get(i)))) {
////                    toMonthSpinner.setSelection(i);
////                }
////            }

            try {
                if (Integer.valueOf(experience.getFromMonth()) <= months.size()) {
                    fromMonthSpinner.setSelection(Integer.valueOf(experience.getFromMonth()) - 1);
                }
                if (Integer.valueOf(experience.getToMonth()) <= months.size()) {
                    toMonthSpinner.setSelection(Integer.valueOf(experience.getToMonth()) - 1);
                }
            } catch (NumberFormatException ignored) {
            }
        } else {
            btnRemove.setVisibility(View.GONE);
        }

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = getString(R.string.are_you_sure) + " " + "experience";
                Call<String> call = profileService.removeExperience(deviceId, token, userId, userId, experienceId);
                showAlert(message, call, dialog);
            }
        });

        fabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                designation_name: soft
//                company_name: doodlei
//                website_url: abc.com
//                from_date: 1930-01-01
//                to_date: 0-0-01
//                currently_worked: true
//                permission_type: 0
//                description:
//                location_name: Destin, FL 32541, USA
//                location_actual_name: Mira Road
//                location_country_name: India
//                location_latitude: 19.2871393
//                location_longitude: 72.8688419
                designationName = etDesignation.getText().toString();
                companyName = etCompanyName.getText().toString();
                websiteUrl = etSiteAddress.getText().toString();
                fromDate = fromYear[0] + "-" + fromMonth[0] + "-01";
                toDate = toYear[0] + "-" + toMonth[0] + "-01";
                description = etSummary.getText().toString();
                locationName = etSearchPlace.getText().toString();

                if (isUpdate) {
                    Call<String> call = profileService.updateExperience(deviceId, token, userId, userId, experienceId, designationName, companyName, websiteUrl, fromDate, toDate, currentlyWorked, permissionType, description,
                            locationName, locationActualName, locationCountryName, locationLatitude, locationLongitude);
                    addExperienceRequest(call, dialog);
                } else {
                    Call<String> call = profileService.addExperience(deviceId, token, userId, userId, designationName, companyName, websiteUrl, fromDate, toDate, currentlyWorked, permissionType, description,
                            locationName, locationActualName, locationCountryName, locationLatitude, locationLongitude);
                    addExperienceRequest(call, dialog);
                }

            }
        });

        currentlyWorkingCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    toYearSpinner.setBackgroundResource(R.drawable.rectangle_corner_round_nine);
                    toMonthSpinner.setBackgroundResource(R.drawable.rectangle_corner_round_nine);
                    toYearSpinner.setClickable(false);
                    toYearSpinner.setEnabled(false);
                    toMonthSpinner.setClickable(false);
                    toMonthSpinner.setEnabled(false);
                } else {
                    toYearSpinner.setBackgroundResource(R.drawable.rectangle_corner_round_five);
                    toMonthSpinner.setBackgroundResource(R.drawable.rectangle_corner_round_five);
                    toYearSpinner.setClickable(true);
                    toYearSpinner.setEnabled(true);
                    toMonthSpinner.setClickable(true);
                    toMonthSpinner.setEnabled(true);
                }
                currentlyWorked = b;
            }
        });

        etDesignation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etDesignation.getText().toString().length() > 2) {
                    Call<String> call = profileService.getExperienceSuggestion(deviceId, token, userId, "designation", etDesignation.getText().toString());
                    getSuggestion(call, designations, designationRecyclerView);
                } else {
                    designations.clear();
                    Objects.requireNonNull(designationRecyclerView.getAdapter()).notifyDataSetChanged();
                }
            }
        });

        etDesignation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    designations.clear();
                    Objects.requireNonNull(designationRecyclerView.getAdapter()).notifyDataSetChanged();
                }
            }
        });

        etCompanyName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etCompanyName.getText().toString().length() > 2) {
                    Call<String> call = profileService.getExperienceSuggestion(deviceId, token, userId, "institute", etCompanyName.getText().toString());
                    getAdvanceSuggestion(call, advanceSuggestions, companyNameRecyclerView);
                } else {
                    advanceSuggestions.clear();
                    companyNameRecyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        });

        etCompanyName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    advanceSuggestions.clear();
                    companyNameRecyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        });

        fromYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                fromYear[0] = years.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fromMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                fromMonth[0] = months.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        toYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                toYear[0] = years.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        toMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                toMonth[0] = months.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        privacySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                permissionType = String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dialog.show();
    }

    private void addAwards(boolean isUpdate, String awardsId, Awards awards) {
        Dialog dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        dialog.setContentView(R.layout.add_awards_layout);

        instituteName = "";
        instituteType = "";
        websiteUrl = "";
        awardsName = "";
        date = "";
        permissionType = "0";
        description = "";
        locationName = "";
        locationActualName = "";
        locationCountryName = "";
        locationLatitude = "";
        locationLongitude = "";

        final String[] year = {""};
        final String[] month = {""};
        ArrayList<AdvanceSuggestion> advanceSuggestions = new ArrayList<>();
        ArrayList<String> awardsNames = new ArrayList<>();
        ArrayList<String> searchLocations = new ArrayList<>();
        List<String> months = Arrays.asList(getResources().getStringArray(R.array.month_list));
        ArrayList<String> years = new ArrayList<>();
        years.add(getString(R.string.select_year));
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 1930; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }

        Toolbar toolbar = dialog.findViewById(R.id.toolbar);
        Button btnRemove = dialog.findViewById(R.id.remove);
        EditText etAwardsName, etInstituteName, etSiteAddress, etSearchPlace, etSummary;
        Spinner yearSpinner, monthSpinner, privacySpinner;
        FloatingActionButton fabDone;
        RecyclerView awardsNameRecyclerView, instituteNameRecyclerView, searchPlaceRecyclerView;

        etAwardsName = dialog.findViewById(R.id.awards_name);
        etInstituteName = dialog.findViewById(R.id.institute_name);
        etSiteAddress = dialog.findViewById(R.id.site_address);
        etSearchPlace = dialog.findViewById(R.id.search_place);
        etSummary = dialog.findViewById(R.id.summary);

        yearSpinner = dialog.findViewById(R.id.year_spinner);
        monthSpinner = dialog.findViewById(R.id.month_spinner);
        privacySpinner = dialog.findViewById(R.id.privacy_spinner);

        fabDone = dialog.findViewById(R.id.done);

        awardsNameRecyclerView = dialog.findViewById(R.id.awards_name_recycler_view);
        awardsNameRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        instituteNameRecyclerView = dialog.findViewById(R.id.institute_name_recycler_view);
        instituteNameRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchPlaceRecyclerView = dialog.findViewById(R.id.search_place_recycler_view);
        searchPlaceRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        SuggestionClickListener awardsNameSuggestionClickListener = new SuggestionClickListener() {
            @Override
            public void onSuggestionClick(String suggestion) {
                etAwardsName.setText(suggestion);
                awardsNames.clear();
                Objects.requireNonNull(awardsNameRecyclerView.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onSuggestionClick(AdvanceSuggestion experienceSuggestion) {

            }
        };

        SuggestionClickListener instituteNameSuggestionClickListener = new SuggestionClickListener() {
            @Override
            public void onSuggestionClick(String suggestion) {

            }

            @Override
            public void onSuggestionClick(AdvanceSuggestion experienceSuggestion) {
                etInstituteName.setText(experienceSuggestion.getInstituteName());
                etSiteAddress.setText(experienceSuggestion.getWebsiteUrl());
                etSearchPlace.setText(experienceSuggestion.getLocationName());
                advanceSuggestions.clear();
                Objects.requireNonNull(instituteNameRecyclerView.getAdapter()).notifyDataSetChanged();
            }
        };

        SuggestionClickListener searchPlaceSuggestionClickListener = new SuggestionClickListener() {
            @Override
            public void onSuggestionClick(String suggestion) {
                etSearchPlace.setText(suggestion);
                searchLocations.clear();
                Objects.requireNonNull(searchPlaceRecyclerView.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onSuggestionClick(AdvanceSuggestion experienceSuggestion) {

            }
        };

        ArrayAdapter<String> privacyAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, privacyList);
        privacyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        privacySpinner.setAdapter(privacyAdapter);

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, months);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

        SuggestionAdapter awardsNameSuggestionAdapter = new SuggestionAdapter(getActivity(), awardsNames, awardsNameSuggestionClickListener);
        SuggestionAdapter placeSuggestionAdapter = new SuggestionAdapter(getActivity(), searchLocations, searchPlaceSuggestionClickListener);
        ExperienceSuggestionAdapter instituteNameSuggestionAdapter = new ExperienceSuggestionAdapter(getActivity(), advanceSuggestions, instituteNameSuggestionClickListener);
        awardsNameRecyclerView.setAdapter(awardsNameSuggestionAdapter);
        searchPlaceRecyclerView.setAdapter(placeSuggestionAdapter);
        instituteNameRecyclerView.setAdapter(instituteNameSuggestionAdapter);

        if (isUpdate) {
            btnRemove.setVisibility(View.VISIBLE);
            permissionType = awards.getPermissionType();
            etAwardsName.setText(awards.getAwardName());
            etInstituteName.setText(awards.getInstituteName());
            etSiteAddress.setText(awards.getWebsiteUrl());
            etSearchPlace.setText(awards.getLocationName());
            etSummary.setText(awards.getDescription());
            privacySpinner.setSelection(Integer.valueOf(awards.getPermissionType()));
            //Need to work
            for (int i = 0; i < years.size(); i++) {
                if (awards.getYear().equals(String.valueOf(years.get(i)))) {
                    yearSpinner.setSelection(i);
                }
            }
//            for (int i = 1; i < months.size(); i++) {
//                if (awards.getMonth().equals(String.valueOf(months.get(i)))) {
//                    monthSpinner.setSelection(i);
//                }
//            }

            try {
                if (Integer.valueOf(awards.getMonth()) <= months.size()) {
                    monthSpinner.setSelection(Integer.valueOf(awards.getMonth()) - 1);
                }
            } catch (NumberFormatException ignored) {
            }

        } else {
            btnRemove.setVisibility(View.GONE);
        }

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = getString(R.string.are_you_sure) + " " + "awards";
                Call<String> call = profileService.removeAwards(deviceId, token, userId, userId, awardsId);
                showAlert(message, call, dialog);
            }
        });

        fabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                institute_name: adafafaf
//                institute_type: 1
//                website_url: fafafa.com
//                awards_name: Design
//                date: 0-0-01
//                permission_type: 0
//                description:
//                location_name:
//                location_actual_name:
//                location_country_name:
//                location_latitude:
//                location_longitude:

                instituteName = etInstituteName.getText().toString();
                instituteType = "1";
                websiteUrl = etSiteAddress.getText().toString();
                awardsName = etAwardsName.getText().toString();
                date = year[0] + "-" + month[0] + "-01";
                description = etSummary.getText().toString();
                locationName = etSearchPlace.getText().toString();

                if (isUpdate) {
                    Call<String> call = profileService.updateAwards(deviceId, token, userId, userId, awardsId, instituteName, instituteType, websiteUrl, awardsName, date, permissionType, description,
                            locationName, locationActualName, locationCountryName, locationLatitude, locationLongitude);
                    addAwardsRequest(call, dialog);
                } else {
                    Call<String> call = profileService.addAwards(deviceId, token, userId, userId, instituteName, instituteType, websiteUrl, awardsName, date, permissionType, description,
                            locationName, locationActualName, locationCountryName, locationLatitude, locationLongitude);
                    addAwardsRequest(call, dialog);
                }

            }
        });

        etAwardsName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etAwardsName.getText().toString().length() > 2) {
                    Call<String> call = profileService.getAwardsSuggestion(deviceId, token, userId, "award", etAwardsName.getText().toString());
                    getSuggestion(call, awardsNames, awardsNameRecyclerView);
                } else {
                    awardsNames.clear();
                    Objects.requireNonNull(awardsNameRecyclerView.getAdapter()).notifyDataSetChanged();
                }
            }
        });

        etAwardsName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    awardsNames.clear();
                    Objects.requireNonNull(awardsNameRecyclerView.getAdapter()).notifyDataSetChanged();
                }
            }
        });

        etInstituteName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etInstituteName.getText().toString().length() > 2) {
                    Call<String> call = profileService.getExperienceSuggestion(deviceId, token, userId, "institute", etInstituteName.getText().toString());
                    getAdvanceSuggestion(call, advanceSuggestions, instituteNameRecyclerView);
                } else {
                    advanceSuggestions.clear();
                    instituteNameRecyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        });

        etInstituteName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    advanceSuggestions.clear();
                    Objects.requireNonNull(instituteNameRecyclerView.getAdapter()).notifyDataSetChanged();
                }
            }
        });

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                year[0] = years.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                month[0] = months.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        privacySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                permissionType = String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dialog.show();
    }

    private void addCertification(boolean isUpdate, String certificateId, Certification certification) {
        Dialog dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        dialog.setContentView(R.layout.add_certifications_layout);

        instituteName = "";
        instituteType = "";
        websiteUrl = "";
        certificationName = "";
        licenseNumber = "";
        certificationUrl = "";
        fromDate = "";
        isExpired = false;
        toDate = "";
        permissionType = "0";
        media = "";
        locationName = "";

        final String[] fromYear = {""};
        final String[] toYear = {""};
        ArrayList<String> certificateNames = new ArrayList<>();
        ArrayList<String> searchLocations = new ArrayList<>();
        ArrayList<AdvanceSuggestion> advanceSuggestions = new ArrayList<>();
        ArrayList<String> years = new ArrayList<>();
        years.add(getString(R.string.select_year));
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 1930; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }

        Toolbar toolbar = dialog.findViewById(R.id.toolbar);
        Button btnRemove;
        TextView tvFileName;
        EditText etCertificateName, etInstituteName, etSiteAddress, etSearchPlace, etCertificateNumber, etCertificateUrl;
        Spinner fromYearSpinner, toYearSpinner, privacySpinner;
        FloatingActionButton fabDone;
        CheckBox notExpireCheck;
        Button upload;
        RecyclerView certificateNameRecyclerView, instituteNameRecyclerView, searchPlaceRecyclerView;

        btnRemove = dialog.findViewById(R.id.remove);
        tvFileName = dialog.findViewById(R.id.file_name);
        etCertificateName = dialog.findViewById(R.id.certificate_name);
        etInstituteName = dialog.findViewById(R.id.institute_name);
        etSiteAddress = dialog.findViewById(R.id.site_address);
        etSearchPlace = dialog.findViewById(R.id.search_place);
        etCertificateNumber = dialog.findViewById(R.id.certificate_number);
        etCertificateUrl = dialog.findViewById(R.id.certificate_url);

        fromYearSpinner = dialog.findViewById(R.id.from_year_spinner);
        toYearSpinner = dialog.findViewById(R.id.to_year_spinner);
        privacySpinner = dialog.findViewById(R.id.privacy_spinner);

        fabDone = dialog.findViewById(R.id.done);

        notExpireCheck = dialog.findViewById(R.id.not_expire_check);

        upload = dialog.findViewById(R.id.upload);

        certificateNameRecyclerView = dialog.findViewById(R.id.certificate_name_recycler_view);
        certificateNameRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        instituteNameRecyclerView = dialog.findViewById(R.id.institute_name_recycler_name);
        instituteNameRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchPlaceRecyclerView = dialog.findViewById(R.id.search_place_recycler_name);
        searchPlaceRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        SuggestionClickListener awardsNameSuggestionClickListener = new SuggestionClickListener() {
            @Override
            public void onSuggestionClick(String suggestion) {
                etCertificateName.setText(suggestion);
                certificateNames.clear();
                Objects.requireNonNull(certificateNameRecyclerView.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onSuggestionClick(AdvanceSuggestion experienceSuggestion) {

            }
        };

        SuggestionClickListener instituteNameSuggestionClickListener = new SuggestionClickListener() {
            @Override
            public void onSuggestionClick(String suggestion) {

            }

            @Override
            public void onSuggestionClick(AdvanceSuggestion experienceSuggestion) {
                etInstituteName.setText(experienceSuggestion.getInstituteName());
                etSiteAddress.setText(experienceSuggestion.getWebsiteUrl());
                etSearchPlace.setText(experienceSuggestion.getLocationName());
                advanceSuggestions.clear();
                Objects.requireNonNull(instituteNameRecyclerView.getAdapter()).notifyDataSetChanged();
            }
        };

        SuggestionClickListener searchPlaceSuggestionClickListener = new SuggestionClickListener() {
            @Override
            public void onSuggestionClick(String suggestion) {
                etSearchPlace.setText(suggestion);
                searchLocations.clear();
                Objects.requireNonNull(searchPlaceRecyclerView.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onSuggestionClick(AdvanceSuggestion experienceSuggestion) {

            }
        };

        ArrayAdapter<String> privacyAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, privacyList);
        privacyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        privacySpinner.setAdapter(privacyAdapter);

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromYearSpinner.setAdapter(yearAdapter);
        toYearSpinner.setAdapter(yearAdapter);

        SuggestionAdapter certificateNameSuggestionAdapter = new SuggestionAdapter(getActivity(), certificateNames, awardsNameSuggestionClickListener);
        SuggestionAdapter placeSuggestionAdapter = new SuggestionAdapter(getActivity(), searchLocations, searchPlaceSuggestionClickListener);
        ExperienceSuggestionAdapter instituteNameSuggestionAdapter = new ExperienceSuggestionAdapter(getActivity(), advanceSuggestions, instituteNameSuggestionClickListener);
        certificateNameRecyclerView.setAdapter(certificateNameSuggestionAdapter);
        searchPlaceRecyclerView.setAdapter(placeSuggestionAdapter);
        instituteNameRecyclerView.setAdapter(instituteNameSuggestionAdapter);

        if (isUpdate) {
            btnRemove.setVisibility(View.VISIBLE);
            permissionType = certification.getPermissionType();

            etCertificateName.setText(certification.getCertificationName());
            etInstituteName.setText(certification.getInstituteName());
            etSiteAddress.setText(certification.getWebsiteUrl());
            etSearchPlace.setText(certification.getLocationName());
            etCertificateNumber.setText(certification.getLicenseNumber());
            etCertificateUrl.setText(certification.getCertificationUrl());
            notExpireCheck.setChecked(certification.getIsExpired());
            toYearSpinner.setEnabled(certification.getIsExpired());
            privacySpinner.setSelection(Integer.valueOf(certification.getPermissionType()));
            //Need to work
            for (int i = 0; i < years.size(); i++) {
                if (certification.getFromYear().equals(String.valueOf(years.get(i)))) {
                    fromYearSpinner.setSelection(i);
                }
            }
            for (int i = 0; i < years.size(); i++) {
                if (certification.getToYear().equals(String.valueOf(years.get(i)))) {
                    toYearSpinner.setSelection(i);
                }
            }
        } else {
            btnRemove.setVisibility(View.GONE);
        }

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = getString(R.string.are_you_sure) + " " + "certification";
                Call<String> call = profileService.removeCertification(deviceId, token, userId, userId, certificateId);
                showAlert(message, call, dialog);
            }
        });

        fabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                institute_name:
//                institute_type: 0
//                website_url:
//                certification_name: Web
//                license_number: 324131
//                certification_url:
//                from_date: 1932-01-01
//                is_expired: true
//                to_date: 0-01-01
//                permission_type: 0
//                media:
//                location_name:
//                location_actual_name:
//                location_country_name:
//                location_latitude:
//                location_longitude:

                instituteName = etInstituteName.getText().toString();
                instituteType = "1";
                websiteUrl = etSiteAddress.getText().toString();
                certificationName = etCertificateName.getText().toString();
                licenseNumber = etCertificateNumber.getText().toString();
                certificationUrl = etCertificateUrl.getText().toString();
                fromDate = fromYear[0] + "-01-01";
                toDate = toYear[0] + "-01-01";

                if (isUpdate) {
                    Call<String> call = profileService.updateCertificate(deviceId, token, userId, userId, certificateId, instituteName, instituteType, websiteUrl, certificationName, licenseNumber, certificationUrl,
                            fromDate, isExpired, toDate, permissionType, media, locationName, locationActualName, locationCountryName, locationLatitude, locationLongitude);
                    addAwardsRequest(call, dialog);
                } else {
                    Call<String> call = profileService.addCertificate(deviceId, token, userId, userId, instituteName, instituteType, websiteUrl, certificationName, licenseNumber, certificationUrl,
                            fromDate, isExpired, toDate, permissionType, media, locationName, locationActualName, locationCountryName, locationLatitude, locationLongitude);
                    addAwardsRequest(call, dialog);
                }

            }
        });

        etCertificateName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etCertificateName.getText().toString().length() > 2) {
                    Call<String> call = profileService.getCertificateSuggestion(deviceId, token, userId, "certification", etCertificateName.getText().toString());
                    getSuggestion(call, certificateNames, certificationRecyclerView);
                } else {
                    certificateNames.clear();
                    Objects.requireNonNull(certificateNameRecyclerView.getAdapter()).notifyDataSetChanged();
                }
            }
        });

        etCertificateName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    certificateNames.clear();
                    Objects.requireNonNull(certificateNameRecyclerView.getAdapter()).notifyDataSetChanged();
                }
            }
        });

        etInstituteName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etInstituteName.getText().toString().length() > 2) {
                    Call<String> call = profileService.getCertificateSuggestion(deviceId, token, userId, "institute", etInstituteName.getText().toString());
                    getAdvanceSuggestion(call, advanceSuggestions, instituteNameRecyclerView);
                } else {
                    advanceSuggestions.clear();
                    instituteNameRecyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        });

        etCertificateName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    advanceSuggestions.clear();
                    instituteNameRecyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        });

        notExpireCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    toYearSpinner.setBackgroundResource(R.drawable.rectangle_corner_round_nine);
                    toYearSpinner.setClickable(false);
                    toYearSpinner.setEnabled(false);
                } else {
                    toYearSpinner.setBackgroundResource(R.drawable.rectangle_corner_round_five);
                    toYearSpinner.setClickable(true);
                    toYearSpinner.setEnabled(true);
                }
                isExpired = b;
            }
        });

        fromYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                fromYear[0] = years.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        toYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                toYear[0] = years.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        privacySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                permissionType = String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dialog.show();
    }

    private void addSocialSites(boolean isUpdate, Links links) {
        Dialog dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        dialog.setContentView(R.layout.add_social_links_layout);

        oldLink = "";
        link = "";
        type = "";
        permissionType = "0";

        List<String> urlTypes = Arrays.asList(getResources().getStringArray(R.array.url_type_list));

        Toolbar toolbar = dialog.findViewById(R.id.toolbar);
        Button btnRemove;
        EditText etSiteAddress;
        Spinner urlTypeSpinner, privacySpinner;
        FloatingActionButton fabDone;

        etSiteAddress = dialog.findViewById(R.id.site_address);
        btnRemove = dialog.findViewById(R.id.remove);
        urlTypeSpinner = dialog.findViewById(R.id.url_type_spinner);
        privacySpinner = dialog.findViewById(R.id.privacy_spinner);

        fabDone = dialog.findViewById(R.id.done);

        ArrayAdapter<String> privacyAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, privacyList);
        privacyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        privacySpinner.setAdapter(privacyAdapter);

        ArrayAdapter<String> urlTypeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, urlTypes);
        urlTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        urlTypeSpinner.setAdapter(urlTypeAdapter);

        if (isUpdate) {
            btnRemove.setVisibility(View.VISIBLE);
            oldLink = links.getLink();
            permissionType = links.getPermissionType();
            etSiteAddress.setText(links.getLink());
            urlTypeSpinner.setSelection(Integer.valueOf(links.getType()));
            privacySpinner.setSelection(Integer.valueOf(links.getPermissionType()));
        } else {
            btnRemove.setVisibility(View.GONE);
        }

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = getString(R.string.are_you_sure) + " " + "link";
                Call<String> call = profileService.removeSocialLinks(deviceId, token, userId, userId, etSiteAddress.getText().toString());
                showAlert(message, call, dialog);
            }
        });

        fabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                link = etSiteAddress.getText().toString();

                if (isUpdate) {
                    Call<String> call = profileService.updateLink(deviceId, token, userId, userId, oldLink, link, type, permissionType);
                    addAwardsRequest(call, dialog);
                } else {
                    Call<String> call = profileService.addLink(deviceId, token, userId, userId, link, type, permissionType);
                    addAwardsRequest(call, dialog);
                }
            }
        });

        urlTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                type = (position == 0 ? "" : String.valueOf(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        privacySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                permissionType = String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dialog.show();
    }

    private void getSuggestion(Call<String> call, ArrayList<String> arrayList, RecyclerView recyclerView) {
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    JSONArray array = obj.getJSONArray("results");
                    arrayList.clear();
                    for (int i = 0; i < array.length(); i++) {
                        arrayList.add(array.getString(i));
                    }
                    Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void getAdvanceSuggestion(Call<String> call, ArrayList<AdvanceSuggestion> arrayList, RecyclerView recyclerView) {
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    JSONArray array = obj.getJSONArray("results");
                    arrayList.clear();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String instituteId, instituteName, locationId, websiteUrl, type, locationName;
                        instituteId = object.getString("institute_id");
                        instituteName = object.getString("institute_name");
                        locationId = object.getString("location_id");
                        websiteUrl = object.getString("website_url");
                        type = object.getString("type");
                        locationName = object.getString("location_name");
                        arrayList.add(new AdvanceSuggestion(instituteId, instituteName, locationId, websiteUrl, type, locationName));
                    }
                    Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void addEducationRequest(Call<String> call, Dialog dialog) {
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean status = obj.getBoolean("status");
                    if (status) {
                        getData();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addExperienceRequest(Call<String> call, Dialog dialog) {
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean status = obj.getBoolean("status");
                    if (status) {
                        getData();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addAwardsRequest(Call<String> call, Dialog dialog) {
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                getData();
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addEmailRequest(String emailAddress, String emailPrivacyType, AddEmailAdapter addEmailAdapter, EditText etNewEmail, LinearLayout addEmailLayout) {
        Call<String> call = profileService.addEmail(deviceId, token, userId, userId, emailAddress, emailPrivacyType);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                profileInfo.getEmails().add(new Email(emailAddress, "2", emailPrivacyType, "0"));
                emailAdapter.notifyDataSetChanged();
                addEmailAdapter.notifyDataSetChanged();
                etNewEmail.setText("");
                addEmailLayout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addPhoneRequest(String phoneNumber, String numberType, String phoneNumberPrivacyType, Phone phone, AddPhoneAdapter addPhoneAdapter, EditText etNewPhone, Spinner phoneCountry, LinearLayout addPhoneLayout) {
        Call<String> call = profileService.addPhone(deviceId, token, userId, userId, phoneNumber, numberType, phoneNumberPrivacyType, phone.getCountryId());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                profileInfo.getPhones().add(new Phone(phoneNumber, phone.getCountryId(), numberType, phoneNumberPrivacyType, "1", phone.getCountryName(), phone.getCountryIsoCode2(), phone.getCountryPhoneCode()));
                phoneAdapter.notifyDataSetChanged();
                addPhoneAdapter.notifyDataSetChanged();
                etNewPhone.setText("");
                phoneCountry.setSelection(0);
                addPhoneLayout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setStoryRequest(String storyType, String storyPrivacyType, String description, AddStoryAdapter addStoryAdapter, EditText etNewStory, LinearLayout addStoryLayout) {
        Call<String> call = profileService.setStory(deviceId, token, userId, userId, storyType, storyPrivacyType, description);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                profileInfo.getStories().add(new Story(description, storyType, storyPrivacyType));
                storyAdapter.notifyDataSetChanged();
                addStoryAdapter.notifyDataSetChanged();
                etNewStory.setText("");
                addStoryLayout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setLivePlaceRequest(String type, String privacyType, String countryId, String countryName, String cityId, String cityName, LinearLayout addLivesInLayout, RelativeLayout livesInAddressLayout,
                                     TextView tvLivesIn) {
        progressDialog.show();
        Call<String> call = profileService.setLivePlace(deviceId, token, userId, userId, countryId, cityId, type, privacyType);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                profileInfo.setCurrentCityCity(cityName);
                profileInfo.setCurrentCityCityId(cityId);
                profileInfo.setCurrentCityCountry(countryName);
                profileInfo.setCurrentCityCountryId(countryId);
                profileInfo.setCurrentCityId("");
                progressDialog.hide();
                tvLivesIn.setText(cityName + ", " + countryName);
                addLivesInLayout.setVisibility(View.GONE);
                livesInAddressLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.hide();
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void removeRequest(Call<String> call, Dialog dialog) {
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//                String jsonResponse = response.body();
//                try {
//                    JSONObject obj = new JSONObject(jsonResponse);
//                    boolean status = obj.getBoolean("status");
//                    if (status) {
//                        getData();
//                        dialog.dismiss();
//                    } else {
//                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                getData();
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendSetIntroRequest(Call<String> call, Dialog dialog) {
        progressDialog.show();
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressDialog.hide();
                dialog.dismiss();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .detach(AboutFragment.this)
                        .attach(AboutFragment.this)
                        .commit();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                progressDialog.hide();
            }
        });
    }

    private void sendProfileInfoRequest(Call<String> call) {
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                clearData();
                getDataFromJson(jsonResponse);
                sendCountryListRequest();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                progressDialog.hide();
            }
        });
    }

    private void removeEmail(Call<String> call, int position, RecyclerView recyclerView) {
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean status = obj.getBoolean("status");
                    if (status) {
                        profileInfo.getEmails().remove(position);
                        emailAdapter.notifyDataSetChanged();
                        recyclerView.getAdapter().notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                progressDialog.hide();
            }
        });
    }

    private void emailVerificationSend(Call<String> call, Email email, TextView tvEmailVerificationMessage) {
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
                        Tools.toast(getContext(), getString(R.string.a_verification_email_has_been_resent_to_your_email), R.drawable.ic_check_black_24dp);
                    } else {
                        Tools.toast(getContext(), getString(R.string.something_went_wrong), R.drawable.ic_info_outline_blue_24dp);
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

    private void removePhone(Call<String> call, int position, RecyclerView recyclerView) {
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean status = obj.getBoolean("status");
                    if (status) {
                        profileInfo.getPhones().remove(position);
                        phoneAdapter.notifyDataSetChanged();
                        recyclerView.getAdapter().notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                progressDialog.hide();
            }
        });
    }

    private void sendCountryListRequest() {
        progressDialog.show();
        CountryInfo countryInfo = new CountryInfo();
        countryInfo.setCountryId("");
        countryInfo.setCountryName("Select Country");
        countryInfo.setCountryIsoCode2("");
        countryInfo.setCountryIsoCode3("");
        countryInfo.setCountryPhoneCode("");
        countryInfo.setCountryAddressFormat("");
        countryInfo.setCountryPostcodeRequired("");
        countryInfo.setCountryStatus("");
        countries.add(countryInfo);
        Call<Country> call = profileService.getCountryList(deviceId, token, userId, userId);
        call.enqueue(new Callback<Country>() {

            @Override
            public void onResponse(Call<Country> call, Response<Country> response) {
                Country country = response.body();
                if (country != null) {
                    countries.addAll(country.getCountry());
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<Country> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                progressDialog.hide();
            }
        });
    }

    private void sendCityListRequest(String countryId, ArrayList<City> cityList, ArrayList<String> cityNameList, Spinner spinner) {
        cityNameList.clear();
        cityList.clear();
        City city = new City();
        city.setCountryId("");
        city.setId("");
        city.setName("Select City");
        city.setInactive("");
        cityList.add(city);
        Call<Cities> call = profileService.getCityList(deviceId, token, userId, userId, countryId);
        call.enqueue(new Callback<Cities>() {

            @Override
            public void onResponse(Call<Cities> call, Response<Cities> response) {
                Cities cities = response.body();
                if (cities.getData() != null) {
                    cityList.addAll(cities.getData());
                }
                for (City c : cityList) {
                    cityNameList.add(c.getName());
                }
                ArrayAdapter<String> cityListAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, cityNameList);
                cityListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(cityListAdapter);
            }

            @Override
            public void onFailure(Call<Cities> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
            }
        });
    }


    private void sendPhoneCountryListRequest() {
        progressDialog.show();
        PhoneCountry phoneCountry = new PhoneCountry();
        phoneCountry.setCountryId("");
        phoneCountry.setCountryName("Select");
        phoneCountry.setCountryIsoCode2("");
        phoneCountry.setCountryPhoneCode("");
        phoneCountries.add(phoneCountry);
        Call<ArrayList<PhoneCountry>> call = profileService.getCountryPhoneCodes(deviceId, token, userId, userId);
        call.enqueue(new Callback<ArrayList<PhoneCountry>>() {

            @Override
            public void onResponse(Call<ArrayList<PhoneCountry>> call, Response<ArrayList<PhoneCountry>> response) {
                if (response.body() != null) {
                    phoneCountries.addAll(response.body());
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<ArrayList<PhoneCountry>> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                progressDialog.hide();
            }
        });
    }

    private void getDataFromJson(String jsonResponse) {
        try {
            JSONObject obj = new JSONObject(jsonResponse);
            String userName, firstName, lastName, headLine, sex, birthYear, birthMonth, birthDate, yearPermission, dayMonthPermission, address, currentCityId, currentCityLocationName, currentCityCountryId,
                    currentCityCityId, currentCityCity, currentCityCountry, intro;

            userName = obj.getJSONObject("intro").getJSONObject("user_header").getString("user_name");
            firstName = obj.getJSONObject("intro").getJSONObject("user_header").getString("first_name");
            lastName = obj.getJSONObject("intro").getJSONObject("user_header").getString("last_name");
            headLine = obj.getJSONObject("intro").getJSONObject("user_header").getString("headline");
            sex = obj.getJSONObject("intro").getJSONObject("user_header").getString("sex");
            birthYear = obj.getJSONObject("intro").getString("birth_year");
            birthMonth = obj.getJSONObject("intro").getString("birth_month");
            birthDate = obj.getJSONObject("intro").getString("birth_date");
            yearPermission = obj.getJSONObject("intro").getJSONObject("user_header").getString("year_permission");
            dayMonthPermission = obj.getJSONObject("intro").getJSONObject("user_header").getString("day_month_permission");
            address = obj.getJSONObject("intro").getJSONObject("user_header").getString("address");
            currentCityId = obj.getJSONObject("intro").getJSONObject("live_places").getJSONObject("current_city").getString("id");
            currentCityLocationName = obj.getJSONObject("intro").getJSONObject("live_places").getJSONObject("current_city").getString("location_name");
            currentCityCountryId = obj.getJSONObject("intro").getJSONObject("live_places").getJSONObject("current_city").getString("country_id");
            currentCityCityId = obj.getJSONObject("intro").getJSONObject("live_places").getJSONObject("current_city").getString("city_id");
            currentCityCity = obj.getJSONObject("intro").getJSONObject("live_places").getJSONObject("current_city").getString("city");
            currentCityCountry = obj.getJSONObject("intro").getJSONObject("live_places").getJSONObject("current_city").getString("country");
            intro = obj.getJSONObject("intro").getJSONObject("story").getString("description");

            JSONArray emailArray = obj.getJSONObject("intro").getJSONArray("emails");
            for (int i = 0; i < emailArray.length(); i++) {
                JSONObject object = emailArray.getJSONObject(i);
                String email, type, permissionType, isVerified;
                email = object.getString("email");
                type = object.getString("type");
                permissionType = object.getString("permission_type");
                isVerified = object.getString("is_verified");
                emails.add(new Email(email, type, permissionType, isVerified));
            }

            JSONArray phoneArray = obj.getJSONObject("intro").getJSONArray("phone_numbers");
            for (int i = 0; i < phoneArray.length(); i++) {
                JSONObject object = phoneArray.getJSONObject(i);
                String phoneNumber, countryId, type, permissionType, isVerified, countryName, countryIsoCode2, countryPhoneCode;
                phoneNumber = object.getString("phone_number");
                countryId = object.getString("country_id");
                type = object.getString("type");
                permissionType = object.getString("permission_type");
                isVerified = object.getString("is_verified");
                countryName = object.getString("country_name");
                countryIsoCode2 = object.getString("country_iso_code_2");
                countryPhoneCode = object.getString("country_phone_code");
                phones.add(new Phone(phoneNumber, countryId, type, permissionType, isVerified, countryName, countryIsoCode2, countryPhoneCode));
            }

            JSONArray storiesArray = obj.getJSONObject("intro").getJSONArray("stories");
            for (int i = 0; i < storiesArray.length(); i++) {
                JSONObject object = storiesArray.getJSONObject(i);
                String description, type, permissionType;
                description = object.getString("description");
                type = object.getString("type");
                permissionType = object.getString("permission_type");
                stories.add(new Story(description, type, permissionType));
            }

            JSONArray educationArray = obj.getJSONArray("education");
            for (int i = 0; i < educationArray.length(); i++) {
                JSONObject object = educationArray.getJSONObject(i);
                String id, userId, instituteId, degreeId, fieldStudy, grade, permissionType, introStatus, startYear, endYear, description, entryDate, modifyDate, instituteName, locationId, instituteType,
                        locationName, websiteUrl, degreeName, fieldStudyName;
                id = object.getString("id");
                userId = object.getString("user_id");
                instituteId = object.getString("institute_id");
                degreeId = object.getString("degree_id");
                fieldStudy = object.getString("field_study");
                grade = object.getString("grade");
                permissionType = object.getString("permission_type");
                introStatus = object.getString("intro_status");
                startYear = object.getString("start_year");
                endYear = object.getString("end_year");
                description = object.getString("description");
                entryDate = object.getString("entry_date");
                modifyDate = object.getString("modify_date");
                instituteName = object.getString("institute_name");
                locationId = object.getString("location_id");
                instituteType = object.getString("institute_type");
                locationName = object.getString("location_name");
                websiteUrl = object.getString("website_url");
                degreeName = object.getString("degree_name");
                fieldStudyName = object.getString("field_study_name");
                educations.add(new Education(id, userId, instituteId, degreeId, fieldStudy, grade, permissionType, introStatus, startYear, endYear, description, entryDate, modifyDate, instituteName, locationId, instituteType,
                        locationName, websiteUrl, degreeName, fieldStudyName));
            }

            JSONArray experienceArray = obj.getJSONArray("experience");
            for (int i = 0; i < experienceArray.length(); i++) {
                JSONObject object = experienceArray.getJSONObject(i);
                String id, userId, designationId, instituteId, locationId, permissionType, introStatus, description, entryDate, modifyDate, designationName, companyName, websiteUrl, locationName, fromYear,
                        fromMonth, toYear, toMonth;
                boolean currentlyWorked;
                id = object.getString("id");
                userId = object.getString("user_id");
                designationId = object.getString("designation_id");
                instituteId = object.getString("institute_id");
                locationId = object.getString("location_id");
                permissionType = object.getString("permission_type");
                introStatus = object.getString("intro_status");
                description = object.getString("description");
                entryDate = object.getString("entry_date");
                modifyDate = object.getString("modify_date");
                designationName = object.getString("designation_name");
                companyName = object.getString("company_name");
                websiteUrl = object.getString("website_url");
                locationName = object.getString("location_name");
                fromYear = object.getString("from_year");
                fromMonth = object.getString("from_month");
                currentlyWorked = object.getBoolean("currently_worked");
                toYear = object.getString("to_year");
                toMonth = object.getString("to_month");
                experiences.add(new Experience(id, userId, designationId, instituteId, locationId, permissionType, introStatus, description, entryDate, modifyDate, designationName, companyName, websiteUrl,
                        locationName, fromYear, fromMonth, toYear, toMonth, currentlyWorked));
            }

            JSONArray awardsArray = obj.getJSONArray("award");
            for (int i = 0; i < awardsArray.length(); i++) {
                JSONObject object = awardsArray.getJSONObject(i);
                String id, userId, awardsId, instituteId, permissionType, description, entryDate, modifyDate, instituteName, locationId, locationName, websiteUrl, awardName, year, month;
                id = object.getString("id");
                userId = object.getString("user_id");
                awardsId = object.getString("awards_id");
                instituteId = object.getString("institute_id");
                permissionType = object.getString("permission_type");
                description = object.getString("description");
                entryDate = object.getString("entry_date");
                modifyDate = object.getString("modify_date");
                instituteName = object.getString("institute_name");
                locationId = object.getString("location_id");
                locationName = object.getString("location_name");
                websiteUrl = object.getString("website_url");
                awardName = object.getString("award_name");
                year = object.getString("year");
                month = object.getString("month");
                awards.add(new Awards(id, userId, awardsId, instituteId, permissionType, description, entryDate, modifyDate, instituteName, locationId, locationName, websiteUrl, awardName, year, month));
            }

            JSONArray certificationArray = obj.getJSONArray("certification");
            for (int i = 0; i < certificationArray.length(); i++) {
                JSONObject object = certificationArray.getJSONObject(i);
                String id, userId, certificationId, instituteId, licenseNumber, permissionType, certificationUrl, mediaId, entryDate, modifyDate, instituteName, locationId, locationName,
                        websiteUrl, instituteType, certificationName, imageName, fromYear, fromMonth, toYear, toMonth, mediaType;
                boolean isExpired;
                id = object.getString("id");
                userId = object.getString("user_id");
                certificationId = object.getString("certification_id");
                instituteId = object.getString("institute_id");
                licenseNumber = object.getString("license_number");
                permissionType = object.getString("permission_type");
                isExpired = object.getBoolean("is_expired");
                certificationUrl = object.getString("certification_url");
                mediaId = object.getString("media_id");
                entryDate = object.getString("entry_date");
                modifyDate = object.getString("modify_date");
                instituteName = object.getString("institute_name");
                locationId = object.getString("location_id");
                locationName = object.getString("location_name");
                websiteUrl = object.getString("website_url");
                instituteType = object.getString("institute_type");
                certificationName = object.getString("certification_name");
                imageName = object.getString("image_name");
                fromYear = object.getString("from_year");
                fromMonth = object.getString("from_month");
                toYear = object.getString("to_year");
                toMonth = object.getString("to_month");
                mediaType = object.getString("media_type");
                certifications.add(new Certification(id, userId, certificationId, instituteId, licenseNumber, permissionType, isExpired, certificationUrl, mediaId, entryDate, modifyDate,
                        instituteName, locationId, locationName, websiteUrl, instituteType, certificationName, imageName, fromYear, fromMonth, toYear, toMonth, mediaType));
            }

            JSONArray linksArray = obj.getJSONArray("links");
            for (int i = 0; i < linksArray.length(); i++) {
                JSONObject object = linksArray.getJSONObject(i);
                String link, type, permissionType;
                link = object.getString("link");
                type = object.getString("type");
                permissionType = object.getString("permission_type");
                links.add(new Links(link, type, permissionType));
            }

            profileInfo = new ProfileInfo(userName, firstName, lastName, headLine, sex, birthYear, birthMonth, birthDate, yearPermission, dayMonthPermission, address, currentCityId, currentCityLocationName,
                    currentCityCountryId, currentCityCityId, currentCityCity, currentCityCountry, intro, emails, phones, stories, educations, experiences, awards, certifications, links);

            setData();
            notifyAllAdapter();
            ownProfileComponentVisibility();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message, Call<String> call, Dialog dialog) {
        alertDialog.setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface d, int which) {
                        removeRequest(call, dialog);
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

}
