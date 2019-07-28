package com.doodle.Home.model;

import java.util.ArrayList;

public class PostFilterSubCategory {

    private String catId, subCatId, subCatName;
    private boolean isSelectedAll;
    private ArrayList<PostFilterItem> postFilterItems;

    public PostFilterSubCategory() {}

    public PostFilterSubCategory(String catId, String subCatId, String subCatName, boolean isSelectedAll, ArrayList<PostFilterItem> postFilterItems) {
        this.catId = catId;
        this.subCatId = subCatId;
        this.subCatName = subCatName;
        this.isSelectedAll = isSelectedAll;
        this.postFilterItems = postFilterItems;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(String subCatId) {
        this.subCatId = subCatId;
    }

    public String getSubCatName() {
        return subCatName;
    }

    public void setSubCatName(String subCatName) {
        this.subCatName = subCatName;
    }

    public boolean isSelectedAll() {
        return isSelectedAll;
    }

    public void setSelectedAll(boolean selectedAll) {
        isSelectedAll = selectedAll;
    }

    public ArrayList<PostFilterItem> getPostFilterItems() {
        return postFilterItems;
    }

    public void setPostFilterItems(ArrayList<PostFilterItem> postFilterItems) {
        this.postFilterItems = postFilterItems;
    }
}
