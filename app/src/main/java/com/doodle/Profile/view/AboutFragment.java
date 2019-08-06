package com.doodle.Profile.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doodle.Profile.adapter.AwardsAdapter;
import com.doodle.Profile.adapter.CertificationAdapter;
import com.doodle.Profile.adapter.EducationAdapter;
import com.doodle.Profile.adapter.EmailAdapter;
import com.doodle.Profile.adapter.ExperienceAdapter;
import com.doodle.Profile.adapter.PhoneAdapter;
import com.doodle.Profile.adapter.SocialAdapter;
import com.doodle.Profile.adapter.StoryAdapter;
import com.doodle.Profile.model.Awards;
import com.doodle.Profile.model.Certification;
import com.doodle.Profile.model.CountryInfo;
import com.doodle.Profile.model.Education;
import com.doodle.Profile.model.Email;
import com.doodle.Profile.model.Experience;
import com.doodle.Profile.model.Links;
import com.doodle.Profile.model.Phone;
import com.doodle.Profile.model.ProfileInfo;
import com.doodle.Profile.model.Story;
import com.doodle.Profile.service.ProfileService;
import com.doodle.R;
import com.doodle.utils.PrefManager;
import com.doodle.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutFragment extends Fragment {

    View view;

    private LinearLayout birthDayLayout, birthYearLayout, emailLayout, phoneLayout, addressLayout;
    private TextView tvFullName, tvGender, tvBirthDay, tvBirthYear, tvAddress, tvCity, tvCountry;
    private ImageView ivAddEducation, ivAddExperience, ivAddAwards, ivAddCertificate, ivAddSocialLinks;
    private RecyclerView emailRecyclerView, phoneRecyclerView, storyRecyclerView, educationRecyclerView, experienceRecyclerView, awardsRecyclerView, certificationRecyclerView, socialRecyclerView;

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
        getData();

        return view;
    }

    private void initialComponent() {
        profileService = ProfileService.mRetrofit.create(ProfileService.class);
        manager = new PrefManager(getContext());

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
        ivAddEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ivAddExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ivAddAwards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ivAddCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ivAddSocialLinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

    private void updateEducation(boolean isUpdate, String educationId) {
        Dialog dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        dialog.setContentView(R.layout.add_education_layout);

        Toolbar toolbar = dialog.findViewById(R.id.toolbar);



        dialog.show();
    }

    private void updateExperience(boolean isUpdate, String experienceId) {
        Dialog dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        dialog.setContentView(R.layout.add_experience_layout);

        dialog.show();
    }

    private void updateAwards(boolean isUpdate, String awardsId) {
        Dialog dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        dialog.setContentView(R.layout.add_awards_layout);

        dialog.show();
    }

    private void updateCertification(boolean isUpdate, String certificateId) {
        Dialog dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        dialog.setContentView(R.layout.add_certifications_layout);

        dialog.show();
    }
    private void updateSocialSites(boolean isUpdate, String socialSiteId) {
        Dialog dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        dialog.setContentView(R.layout.add_social_links_layout);

        dialog.show();
    }


    private void sendProfileInfoRequest(Call<String> call) {
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                getDataFromJson(jsonResponse);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
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

}
