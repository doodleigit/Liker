package com.doodle.Setting.service;

import com.doodle.Setting.model.Email;

public interface EmailModificationListener {

    void onEmailRemove(Email email, int position);
    void onEmailResendVerification(Email email);

}
