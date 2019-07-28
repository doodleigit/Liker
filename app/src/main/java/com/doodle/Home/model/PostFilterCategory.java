package com.doodle.Home.model;

import java.util.ArrayList;

public class PostFilterCategory {

    private String catId, catName;
    private ArrayList<PostFilterSubCategory> postFilterSubCategories;

    public PostFilterCategory(String catId, String catName, ArrayList<PostFilterSubCategory> postFilterSubCategories) {
        this.catId = catId;
        this.catName = catName;
        this.postFilterSubCategories = postFilterSubCategories;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public ArrayList<PostFilterSubCategory> getPostFilterSubCategories() {
        return postFilterSubCategories;
    }

    public void setPostFilterSubCategories(ArrayList<PostFilterSubCategory> postFilterSubCategories) {
        this.postFilterSubCategories = postFilterSubCategories;
    }
}
