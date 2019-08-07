package com.doodle.Profile.service;

import com.doodle.Profile.model.AdvanceSuggestion;

public interface SuggestionClickListener {

    void onSuggestionClick(String suggestion);
    void onSuggestionClick(AdvanceSuggestion advanceSuggestion);

}
