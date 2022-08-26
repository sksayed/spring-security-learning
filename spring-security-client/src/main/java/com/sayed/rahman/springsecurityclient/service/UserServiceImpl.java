package com.sayed.rahman.springsecurityclient.service;

import com.sayed.rahman.springsecurityclient.entity.User;
import com.sayed.rahman.springsecurityclient.entity.VerificationToken;
import com.sayed.rahman.springsecurityclient.model.UserModel;
import com.sayed.rahman.springsecurityclient.repository.UserRepository;
import com.sayed.rahman.springsecurityclient.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private VerificationTokenRepository tokenRepository ;
    @Autowired
    PasswordEncoder passwordEncoder ;
    @Autowired
    private UserRepository userRepository ;
    @Override
    public User registerUser(UserModel userModel) {
        User user = new User() ;
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
        VerificationToken verificationToken = new VerificationToken(token,user);
        tokenRepository.save(verificationToken);
    }
}
