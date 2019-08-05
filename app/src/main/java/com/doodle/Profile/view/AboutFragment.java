package com.doodle.Profile.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doodle.Profile.model.CountryInfo;
import com.doodle.Profile.model.ProfileInfo;
import com.doodle.R;

import java.util.ArrayList;

public class AboutFragment extends Fragment {

    View view;

    private LinearLayout birthDayLayout, birthYearLayout, emailLayout, phoneLayout, addressLayout;
    private TextView tvFullName, tvGender, tvBirthDay, tvBirthYear, tvEmail, tvPhone, tvAddress, tvCity, tvCountry, tvIntro;
    private ImageView ivAddEducation, ivAddExperience, ivAddAwards, ivAddCertificate, ivAddSocialLinks;
    private RecyclerView educationRecyclerView, experienceRecyclerView, awardsRecyclerView, certificationRecyclerView, socialRecyclerView;

    private ProfileInfo profileInfo;
    private ArrayList<CountryInfo> countries;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.about_layout, container, false);

        initialComponent();

        return view;
    }

    private void initialComponent() {
        birthDayLayout = view.findViewById(R.id.birth_day_layout);
        birthYearLayout = view.findViewById(R.id.birth_year_layout);
        emailLayout = view.findViewById(R.id.email_layout);
        phoneLayout = view.findViewById(R.id.phone_layout);
        addressLayout = view.findViewById(R.id.address_layout);
        tvFullName = view.findViewById(R.id.user_name);
        tvGender = view.findViewById(R.id.gender);
        tvBirthDay = view.findViewById(R.id.birth_day);
        tvBirthYear = view.findViewById(R.id.birth_year);
        tvEmail = view.findViewById(R.id.email);
        tvPhone = view.findViewById(R.id.phone);
        tvAddress = view.findViewById(R.id.address);
        tvCity = view.findViewById(R.id.city);
        tvCountry = view.findViewById(R.id.country);
        tvIntro = view.findViewById(R.id.intro);
        ivAddEducation = view.findViewById(R.id.add_education);
        ivAddExperience = view.findViewById(R.id.add_experience);
        ivAddAwards = view.findViewById(R.id.add_awards);
        ivAddCertificate = view.findViewById(R.id.add_certificate);
        ivAddSocialLinks = view.findViewById(R.id.add_social_links);
        educationRecyclerView = view.findViewById(R.id.educationRecyclerView);
        educationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        experienceRecyclerView = view.findViewById(R.id.experienceRecyclerView);
        experienceRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        awardsRecyclerView = view.findViewById(R.id.awardsRecyclerView);
        awardsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        certificationRecyclerView = view.findViewById(R.id.certificationRecyclerView);
        certificationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        socialRecyclerView = view.findViewById(R.id.socialRecyclerView);
        socialRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setData() {
        tvFullName.setText(profileInfo.getFirstName() + " " + profileInfo.getLastName());
        tvGender.setText(profileInfo.getSex().equals("1") ? "Male" : "Female");
        tvBirthDay.setText(profileInfo.getBirthDate() + "-" + profileInfo.getBirthMonth());
        tvBirthYear.setText(profileInfo.getBirthYear());
//        tvEmail.setText(profileInfo.getEmail());
//        tvPhone.setText(profileInfo.getPhoneNumberCountryPhoneCode() + profileInfo.getPhoneNumber());
        tvAddress.setText(profileInfo.getAddress());
        tvCity.setText(profileInfo.getCurrentCityCity());
        tvCountry.setText(profileInfo.getCurrentCityCountry());
        tvIntro.setText(profileInfo.getIntro());
    }

}
