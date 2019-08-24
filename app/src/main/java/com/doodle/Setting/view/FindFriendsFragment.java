package com.doodle.Setting.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doodle.R;
import com.doodle.Setting.adapter.ViewPagerAdapter;
import com.doodle.Setting.model.SocialLink;
import com.doodle.Setting.service.SettingService;
import com.doodle.utils.PrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindFriendsFragment extends Fragment {

    View view;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private SettingService settingService;
    private PrefManager manager;
    private String deviceId, token, userIds;
    public static SocialLink socialLink;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.find_friends_fragment_layout, container, false);

        initialComponent();
        getSocialLinks();

        return view;
    }

    private void initialComponent() {
        settingService = SettingService.mRetrofit.create(SettingService.class);
        manager = new PrefManager(getContext());
        deviceId = manager.getDeviceId();
        token = manager.getToken();
        userIds = manager.getProfileId();

        toolbar = view.findViewById(R.id.toolbar);
        tabLayout = view.findViewById(R.id.tabs);
        viewPager = view.findViewById(R.id.viewpager);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new SuggestedFriendsFragment(), "Tab 1");
        adapter.addFrag(new FacebookFriendsFragment(), "Tab 2");
        adapter.addFrag(new TwitterFriendsFragment(), "Tab 3");
        adapter.addFrag(new LinkedinFriendsFragment(), "Tab 3");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setText("Suggested");
        tabLayout.getTabAt(1).setText("Facebook");
        tabLayout.getTabAt(2).setText("Twitter");
        tabLayout.getTabAt(3).setText("Linkedin");
    }

    private void getSocialLinks() {
        Call<SocialLink> call = settingService.getSocialLinks(deviceId, token, userIds, userIds);
        call.enqueue(new Callback<SocialLink>() {
            @Override
            public void onResponse(Call<SocialLink> call, Response<SocialLink> response) {
                socialLink = response.body();
            }

            @Override
            public void onFailure(Call<SocialLink> call, Throwable t) {

            }
        });
    }

}
