package com.doodle.Home.model;

public class PostFilterItem {

    private String catId, subCatId, itemId, itemName;
    private boolean isSelected;

    public PostFilterItem() {}

    public PostFilterItem(String catId, String subCatId, String itemId, String itemName, boolean isSelected) {
        this.catId = catId;
        this.subCatId = subCatId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.isSelected = isSelected;
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

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
