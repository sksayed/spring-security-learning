package com.sayed.rahman.springsecurityclient.service;

import com.sayed.rahman.springsecurityclient.entity.User;
import com.sayed.rahman.springsecurityclient.entity.VerificationToken;
import com.sayed.rahman.springsecurityclient.model.UserModel;

import java.util.Optional;


public interface UserService {
    User registerUser(UserModel userModel);

    void saveVerifactionToken(User user, String token);

    String validateVerficationToken(String token);

    VerificationToken generateNewVerficationToken(String oldToken);

    User getUserByEmail(String email);

    void savePasswordResetToken(User user, String token);

    String validatePasswordToken(String token);

    Optional<User> getUserByPasswordRestToken(String token);

    User changePassword(String password, Optional<User> user);
}
