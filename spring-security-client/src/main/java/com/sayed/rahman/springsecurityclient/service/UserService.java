package com.sayed.rahman.springsecurityclient.service;

import com.sayed.rahman.springsecurityclient.entity.User;
import com.sayed.rahman.springsecurityclient.entity.VerificationToken;
import com.sayed.rahman.springsecurityclient.model.UserModel;
import org.springframework.stereotype.Service;


public interface UserService {
    User registerUser(UserModel userModel);

    void saveVerifactionToken(User user, String token);

    String validateVerficationToken(String token);

    VerificationToken generateNewVerficationToken(String oldToken);

    User getUserByEmail(String email);

    void createPasswordResetToken(User user, String token);
}
