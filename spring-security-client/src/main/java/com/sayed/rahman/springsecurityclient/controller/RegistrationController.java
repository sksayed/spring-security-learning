package com.sayed.rahman.springsecurityclient.controller;


import com.sayed.rahman.springsecurityclient.entity.User;
import com.sayed.rahman.springsecurityclient.entity.VerificationToken;
import com.sayed.rahman.springsecurityclient.event.RegistrationCompleteEvent;
import com.sayed.rahman.springsecurityclient.model.PasswordModel;
import com.sayed.rahman.springsecurityclient.model.UserModel;
import com.sayed.rahman.springsecurityclient.service.UserService;
import com.sayed.rahman.springsecurityclient.service.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController()
@RequestMapping("/api")
@Slf4j
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @GetMapping
    public String hello() {
        return "Welcome to Spring Security learning ";
    }

    @PostMapping(value = "/register", consumes = {"application/json"})
    public User registeringUser(@RequestBody UserModel userModel, final HttpServletRequest servletRequest) {
        User user = userService.registerUser(userModel);
        eventPublisher.publishEvent(new RegistrationCompleteEvent(
                user, applicationUrl(servletRequest)));
        return user;
    }

    @GetMapping("/verifyRegistration")
    public String verifyUser(@RequestParam("token") String token) {
        String result = userService.validateVerficationToken(token);
        if (result.equalsIgnoreCase(UserServiceImpl.USER_VERIFIED))
            return "User has been Verified";
        return result;
    }

    @GetMapping("/resendVerificationToken")
    public String resendVerificationToken(@RequestParam("token") String oldToken, HttpServletRequest servletRequest) {
        VerificationToken token = userService.generateNewVerficationToken(oldToken);
        User user = token.getUser();
        resendVerificationTokenViaMail(user, applicationUrl(servletRequest), token);
        return "See in the logs ";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam PasswordModel passwordModel , HttpServletRequest request) {
        User user = userService.getUserByEmail(passwordModel.getEmail());
        String url = "";
        if (user != null ){
            //user is present by the email
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetToken(user,token);
            url = passwordResetTokenMail(user, applicationUrl(request) , token);
        }
        return url;
    }

    private String passwordResetTokenMail(User user, String applicationUrl, String token) {
        String url = applicationUrl
                + "/api/"
                + "savePassword?"
                + "token="
                + token;
        log.info("Click the link to Reset your Password: {}",
                url);
        return url;
    }


    private void resendVerificationTokenViaMail(User user, String applicationUrl, VerificationToken token) {
        String link = applicationUrl
                + "/api/verifyRegistration"
                + "?"
                + "token="
                + token.getToken();

        log.info("the resend verification mail is {}", link);

    }

    private String applicationUrl(HttpServletRequest servletRequest) {
        return "http://"
                + servletRequest.getServerName()
                + ":"
                + servletRequest.getServerPort()
                + servletRequest.getContextPath();
    }


}
