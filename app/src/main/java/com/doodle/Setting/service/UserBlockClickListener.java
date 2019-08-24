package com.doodle.Setting.service;

import com.doodle.Setting.model.BlockUser;
import com.doodle.Setting.model.Friend;

public interface UserBlockClickListener {

    void onBlockClick(Friend friend, int position);
    void onUnBlockClick(BlockUser blockUser, int position);

}
