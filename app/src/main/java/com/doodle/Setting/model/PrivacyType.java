package com.doodle.Setting.model;

public class PrivacyType {

    private String privacyType;
    private int privacyTypeImage;

    public PrivacyType(String privacyType, int privacyTypeImage) {
        this.privacyType = privacyType;
        this.privacyTypeImage = privacyTypeImage;
    }

    public String getPrivacyType() {
        return privacyType;
    }

    public void setPrivacyType(String privacyType) {
        this.privacyType = privacyType;
    }

    public int getPrivacyTypeImage() {
        return privacyTypeImage;
    }

    public void setPrivacyTypeImage(int privacyTypeImage) {
        this.privacyTypeImage = privacyTypeImage;
    }
}
