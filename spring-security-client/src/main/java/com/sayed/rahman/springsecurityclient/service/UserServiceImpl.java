package com.sayed.rahman.springsecurityclient.service;

import com.sayed.rahman.springsecurityclient.entity.User;
import com.sayed.rahman.springsecurityclient.entity.VerificationToken;
import com.sayed.rahman.springsecurityclient.model.UserModel;
import com.sayed.rahman.springsecurityclient.repository.UserRepository;
import com.sayed.rahman.springsecurityclient.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    public static final String INVALID_TOKEN = "invalid_token";
    public static final String VALID_TOKEN = "valid_token";
    public static final String EXPIRED_TOKEN = "expired_token";
    public static final String USER_VERIFIED = "user_verified";
    @Autowired
    private VerificationTokenRepository tokenRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerUser(UserModel userModel) {
        User user = new User();
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        user.setEmail(userModel.getEmail());
        user.setRole("USER");
        userRepository.save(user);
        return user;
    }

    @Override
    public void saveVerifactionToken(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(token, user);
        tokenRepository.save(verificationToken);
    }

    @Override
    public String validateVerficationToken(String token) {
        VerificationToken verificationToken =
                this.tokenRepository.findByToken(token);
        if (verificationToken == null) {
            return INVALID_TOKEN;
        }
        User user = verificationToken.getUser();
        LocalDateTime timeNow = LocalDateTime.now();
        if (verificationToken.getExpirationTime().isBefore(timeNow)) {
            user.setEnabled(true);
            tokenRepository.delete(verificationToken);
            userRepository.save(user);
            return USER_VERIFIED;
        }//else time has expired
        return EXPIRED_TOKEN;
    }

    @Override
    public VerificationToken generateNewVerficationToken(String oldToken) {
        VerificationToken newToken = this.tokenRepository.findByToken(oldToken);
        if (newToken == null) {
            return null ;
        }
        newToken.setToken(UUID.randomUUID().toString());
        //time has been extended
        newToken.setExpirationTime( LocalDateTime.now().plusMinutes(10));
        tokenRepository.save(newToken);
        return newToken;
    }

    @Override
    public User getUserByEmail(String email) {
        User user = this.userRepository.findByEmail(email);
        return user ;
    }

    @Override
    public void createPasswordResetToken(User user, String token) {

    }
}
