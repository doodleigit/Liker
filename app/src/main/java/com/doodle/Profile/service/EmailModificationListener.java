package com.doodle.Profile.service;

import com.doodle.Profile.model.Email;

public interface EmailModificationListener {

    void onEmailRemove(Email email, int position);

}
