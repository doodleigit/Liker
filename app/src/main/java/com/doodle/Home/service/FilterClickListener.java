package com.doodle.Home.service;

import com.doodle.Home.model.PostFilterItem;
import com.doodle.Home.model.PostFilterSubCategory;

public interface FilterClickListener {

    void onSingleSubCategorySelect(PostFilterSubCategory postFilterSubCategory);
    void onSingleSubCategoryDeselect(PostFilterSubCategory postFilterSubCategory);

    void onSingleFilterItemSelect(PostFilterSubCategory postFilterSubCategory);
    void onSingleFilterItemDeselect(PostFilterSubCategory postFilterSubCategory);

}
