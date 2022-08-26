package com.sayed.rahman.springsecurityclient.service;

import com.sayed.rahman.springsecurityclient.entity.User;
import com.sayed.rahman.springsecurityclient.model.UserModel;
import org.springframework.stereotype.Service;


public interface UserService {
    User registerUser(UserModel userModel);

    void saveVerifactionToken(User user, String token);
}
