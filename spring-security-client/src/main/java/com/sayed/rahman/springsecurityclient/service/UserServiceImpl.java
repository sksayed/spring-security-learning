package com.sayed.rahman.springsecurityclient.service;

import com.sayed.rahman.springsecurityclient.entity.PasswordResetToken;
import com.sayed.rahman.springsecurityclient.entity.User;
import com.sayed.rahman.springsecurityclient.entity.VerificationToken;
import com.sayed.rahman.springsecurityclient.model.UserModel;
import com.sayed.rahman.springsecurityclient.repository.PasswordResetTokenRepository;
import com.sayed.rahman.springsecurityclient.repository.UserRepository;
import com.sayed.rahman.springsecurityclient.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
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
    private PasswordResetTokenRepository passwordResetTokenRepository ;
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
    public void savePasswordResetToken(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
        this.passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public String validatePasswordToken(String token) {
        PasswordResetToken verificationToken =
                this.passwordResetTokenRepository.findByToken(token);
        if (verificationToken == null) {
            return INVALID_TOKEN;
        }
        User user = verificationToken.getUser();
        LocalDateTime timeNow = LocalDateTime.now();
        if (verificationToken.getExpirationTime().isBefore(timeNow)) {
            user.setEnabled(true);
            passwordResetTokenRepository.delete(verificationToken);
            userRepository.save(user);
            return USER_VERIFIED;
        }//else time has expired
        return EXPIRED_TOKEN;
    }

    @Override
    public Optional<User> getUserByPasswordRestToken(String token) {
        return Optional.ofNullable(this.passwordResetTokenRepository
                .findByToken(token)
                .getUser());

    }

    @Override
    public User changePassword(String password, Optional<User> user) {
        User existingUser;
        existingUser = user.get();
        existingUser
                .setPassword(this.passwordEncoder.encode(password));
       return this.userRepository.save(existingUser);
    }
}
