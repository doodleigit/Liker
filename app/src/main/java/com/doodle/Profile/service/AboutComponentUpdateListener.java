package com.doodle.Profile.service;

import com.doodle.Profile.model.Awards;
import com.doodle.Profile.model.Certification;
import com.doodle.Profile.model.Education;
import com.doodle.Profile.model.Experience;
import com.doodle.Profile.model.Links;

public interface AboutComponentUpdateListener {

    void onEducationUpdate(Education education);
    void onExperienceUpdate(Experience experience);
    void onAwardsUpdate(Awards awards);
    void onCertificationUpdate(Certification certification);
    void onLinkUpdate(Links links);

}
