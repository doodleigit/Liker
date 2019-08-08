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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.doodle.Profile.model.CountryInfo;
import com.doodle.Profile.model.Education;
import com.doodle.Profile.model.Email;
import com.doodle.Profile.model.Experience;
import com.doodle.Profile.model.AdvanceSuggestion;
import com.doodle.Profile.model.Links;
import com.doodle.Profile.model.Phone;
import com.doodle.Profile.model.ProfileInfo;
import com.doodle.Profile.model.Story;
import com.doodle.Profile.service.ProfileService;
import com.doodle.Profile.service.SuggestionClickListener;
import com.doodle.R;
import com.doodle.utils.PrefManager;
import com.doodle.utils.Utils;

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
    private TextView tvFullName, tvGender, tvBirthDay, tvBirthYear, tvAddress, tvCity, tvCountry;
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

    private String deviceId, profileId, token, userIds;

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
        emails = new ArrayList<>();
        phones = new ArrayList<>();
        stories = new ArrayList<>();
        educations = new ArrayList<>();
        experiences = new ArrayList<>();
        awards = new ArrayList<>();
        certifications = new ArrayList<>();
        links = new ArrayList<>();

        deviceId = manager.getDeviceId();
        profileId = manager.getProfileId();
        token = manager.getToken();
        userIds = manager.getProfileId();

        emailAdapter = new EmailAdapter(getActivity(), emails);
        phoneAdapter = new PhoneAdapter(getActivity(), phones);
        storyAdapter = new StoryAdapter(getActivity(), stories);
        educationAdapter = new EducationAdapter(getActivity(), educations);
        experienceAdapter = new ExperienceAdapter(getActivity(), experiences);
        awardsAdapter = new AwardsAdapter(getActivity(), awards);
        certificationAdapter = new CertificationAdapter(getActivity(), certifications);
        socialAdapter = new SocialAdapter(getActivity(), links);

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
                addSocialSites(false, "", null);
            }
        });

    }

    private void setData() {
        tvFullName.setText(profileInfo.getFirstName() + " " + profileInfo.getLastName());
        tvGender.setText(profileInfo.getSex().equals("1") ? "Male" : "Female");
        tvBirthDay.setText(profileInfo.getBirthDate() + " " + Utils.getMonth(profileInfo.getBirthMonth()));
        tvBirthYear.setText(profileInfo.getBirthYear());
        tvAddress.setText(profileInfo.getAddress().isEmpty() ? "Not yet" : profileInfo.getAddress());
        tvCity.setText(profileInfo.getCurrentCityCity().isEmpty() ? "Not yet" : profileInfo.getCurrentCityCity());
        tvCountry.setText(profileInfo.getCurrentCityCountry().isEmpty() ? "Not yet" : profileInfo.getCurrentCityCountry());
    }

    private void getData() {
        Call<String> call = profileService.getProfileInfo(deviceId, token, userIds, profileId, userIds);
        sendProfileInfoRequest(call);
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

    String instituteName = "", instituteType = "", designationName = "", companyName = "", awardsName = "", certificationName = "", licenseNumber = "", certificationUrl = "", degreeName = "", link = "", type = "",
            fieldStudyName = "", websiteUrl = "", locationName = "", permissionType = "", grade = "", date = "", startYear = "", endYear = "", fromDate = "", toDate = "", description = "", media = "",
            locationActualName = "", locationCountryName = "", locationLatitude = "", locationLongitude = "";
    boolean currentlyWorked, isExpired;
    List<String> privacyList;

    private void editUserInfo() {
        Dialog dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        dialog.setContentView(R.layout.edit_user_info_layout);

        EditText etFirstName, etLastName, etHeadline, etAddress, etNewEmail, etNewPhone;
        Spinner genderSpinner, birthYearSpinner, birthMonthSpinner, birthDaySpinner, emailPrivacySpinner, phonePrivacySpinner, livesCountrySpinner, livesStateSpinner, homeCountrySpinner,
                homeStateSpinner;
        RecyclerView emailRecyclerView, phoneRecyclerView;
        Button btnCancel, btnEmailSave, btnPhoneCancel, btnPhoneSave, btnLivesCancel, btnLivesSave, btnHomeStateCancel, btnHomeStateSave;
        FloatingActionButton fabDone;

        etFirstName = dialog.findViewById(R.id.first_name);
        etLastName = dialog.findViewById(R.id.last_name);
        etHeadline = dialog.findViewById(R.id.headline);
        etAddress = dialog.findViewById(R.id.address);
        etNewEmail = dialog.findViewById(R.id.new_email);
        etNewPhone = dialog.findViewById(R.id.new_phone);

        genderSpinner = dialog.findViewById(R.id.gender_spinner);
        birthYearSpinner = dialog.findViewById(R.id.birth_year_spinner);
        birthMonthSpinner = dialog.findViewById(R.id.birth_month_spinner);
        birthDaySpinner = dialog.findViewById(R.id.birth_day_spinner);
        emailPrivacySpinner = dialog.findViewById(R.id.email_privacy_spinner);
        phonePrivacySpinner = dialog.findViewById(R.id.phone_privacy_spinner);
        livesCountrySpinner = dialog.findViewById(R.id.lives_country_spinner);
        livesStateSpinner = dialog.findViewById(R.id.lives_state_spinner);
        homeCountrySpinner = dialog.findViewById(R.id.home_country_spinner);
        homeStateSpinner = dialog.findViewById(R.id.home_state_spinner);

        emailRecyclerView = dialog.findViewById(R.id.email_recycler_view);
        emailRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        phoneRecyclerView = dialog.findViewById(R.id.phone_recycler_view);
        phoneRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        btnCancel = dialog.findViewById(R.id.cancel);
        btnEmailSave = dialog.findViewById(R.id.email_save);
        btnPhoneCancel = dialog.findViewById(R.id.phone_cancel);
        btnPhoneSave = dialog.findViewById(R.id.phone_save);
        btnLivesCancel = dialog.findViewById(R.id.lives_cancel);
        btnLivesSave = dialog.findViewById(R.id.lives_save);
        btnHomeStateCancel = dialog.findViewById(R.id.home_state_cancel);
        btnHomeStateSave = dialog.findViewById(R.id.home_state_save);
        fabDone = dialog.findViewById(R.id.done);

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
        ArrayList<String> years = new ArrayList<String>();
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
                Call<String> call = profileService.removeEducation(deviceId, token, userIds, userIds, educationId);
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

                Call<String> call = profileService.addEducation(deviceId, token, userIds, userIds, instituteName, instituteType, degreeName, fieldStudyName, websiteUrl, locationName, permissionType,
                        grade, startYear, endYear, description, locationActualName, locationCountryName, locationLatitude, locationLongitude);
                addEducationRequest(call, dialog);
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
                    Call<String> call = profileService.getSuggestion(deviceId, token, userIds, "institute", etInstituteName.getText().toString());
                    getAdvanceSuggestion(call, advanceSuggestions, institutionNameRecyclerView);
                } else {
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
            for (int i = 0; i < years.size(); i++) {
                if (experience.getFromMonth().equals(String.valueOf(months.get(i)))) {
                    fromMonthSpinner.setSelection(i);
                }
            }
            for (int i = 0; i < years.size(); i++) {
                if (experience.getToMonth().equals(String.valueOf(months.get(i)))) {
                    toMonthSpinner.setSelection(i);
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
                String message = getString(R.string.are_you_sure) + " " + "experience";
                Call<String> call = profileService.removeExperience(deviceId, token, userIds, userIds, experienceId);
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

                Call<String> call = profileService.addExperience(deviceId, token, userIds, userIds, designationName, companyName, websiteUrl, fromDate, toDate, currentlyWorked, permissionType, description,
                        locationName, locationActualName, locationCountryName, locationLatitude, locationLongitude);
                addExperienceRequest(call, dialog);

            }
        });

        currentlyWorkingCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    toYearSpinner.setBackgroundResource(R.drawable.rectangle_corner_round_nine);
                    toMonthSpinner.setBackgroundResource(R.drawable.rectangle_corner_round_nine);
                    toYearSpinner.setClickable(false);
                    toMonthSpinner.setClickable(false);
                } else {
                    toYearSpinner.setBackgroundResource(R.drawable.rectangle_corner_round_five);
                    toMonthSpinner.setBackgroundResource(R.drawable.rectangle_corner_round_five);
                    toYearSpinner.setClickable(true);
                    toMonthSpinner.setClickable(true);
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
                    Call<String> call = profileService.getExperienceSuggestion(deviceId, token, userIds, "designation", etDesignation.getText().toString());
                    getSuggestion(call, designations, designationRecyclerView);
                } else {
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
                    Call<String> call = profileService.getExperienceSuggestion(deviceId, token, userIds, "institute", etCompanyName.getText().toString());
                    getAdvanceSuggestion(call, advanceSuggestions, companyNameRecyclerView);
                } else {
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
            for (int i = 0; i < years.size(); i++) {
                if (awards.getMonth().equals(String.valueOf(months.get(i)))) {
                    monthSpinner.setSelection(i);
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
                String message = getString(R.string.are_you_sure) + " " + "awards";
                Call<String> call = profileService.removeAwards(deviceId, token, userIds, userIds, awardsId);
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

                Call<String> call = profileService.addAwards(deviceId, token, userIds, userIds, instituteName, instituteType, websiteUrl, awardsName, date, permissionType, description,
                        locationName, locationActualName, locationCountryName, locationLatitude, locationLongitude);
                addAwardsRequest(call, dialog);
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
                    Call<String> call = profileService.getAwardsSuggestion(deviceId, token, userIds, "award", etAwardsName.getText().toString());
                    getSuggestion(call, awardsNames, awardsNameRecyclerView);
                } else {
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
                    Call<String> call = profileService.getExperienceSuggestion(deviceId, token, userIds, "institute", etInstituteName.getText().toString());
                    getAdvanceSuggestion(call, advanceSuggestions, instituteNameRecyclerView);
                } else {
                    advanceSuggestions.clear();
                    instituteNameRecyclerView.getAdapter().notifyDataSetChanged();
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
                Call<String> call = profileService.removeCertification(deviceId, token, userIds, userIds, certificateId);
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

                Call<String> call = profileService.addCertificate(deviceId, token, userIds, userIds, instituteName, instituteType, websiteUrl, certificationName, licenseNumber, certificationUrl,
                        fromDate, isExpired, toDate, permissionType, media, locationName, locationActualName, locationCountryName, locationLatitude, locationLongitude);
                addAwardsRequest(call, dialog);
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
                    Call<String> call = profileService.getCertificateSuggestion(deviceId, token, userIds, "certification", etCertificateName.getText().toString());
                    getSuggestion(call, certificateNames, certificationRecyclerView);
                } else {
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
                    Call<String> call = profileService.getCertificateSuggestion(deviceId, token, userIds, "institute", etInstituteName.getText().toString());
                    getAdvanceSuggestion(call, advanceSuggestions, instituteNameRecyclerView);
                } else {
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
                } else {
                    toYearSpinner.setBackgroundResource(R.drawable.rectangle_corner_round_five);
                    toYearSpinner.setClickable(true);
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

    private void addSocialSites(boolean isUpdate, String socialSiteId, Links links) {
        Dialog dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        dialog.setContentView(R.layout.add_social_links_layout);

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
                Call<String> call = profileService.removeSocialLinks(deviceId, token, userIds, userIds, socialSiteId);
                showAlert(message, call, dialog);
            }
        });

        fabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                link = etSiteAddress.getText().toString();

                Call<String> call = profileService.addLink(deviceId, token, userIds, userIds, link, type, permissionType);
                addAwardsRequest(call, dialog);
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

    private void removeRequest(Call<String> call, Dialog dialog) {
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

    private void sendProfileInfoRequest(Call<String> call) {
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                clearData();
                getDataFromJson(jsonResponse);
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
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
