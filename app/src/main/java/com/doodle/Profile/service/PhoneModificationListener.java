package com.doodle.Profile.service;

import com.doodle.Profile.model.Phone;

public interface PhoneModificationListener {

    void onPhoneEdit(Phone phone);
    void onPhoneRemove(Phone phone, int position);

}
