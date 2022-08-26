package com.sayed.rahman.springsecurityclient.event.listener;

import com.sayed.rahman.springsecurityclient.entity.User;
import com.sayed.rahman.springsecurityclient.event.RegistrationCompleteEvent;
import com.sayed.rahman.springsecurityclient.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerifactionToken(user, token);
        // send email to user using the token to click
        String url = event.getApplicationUrl()
                + "/api/verifyRegistration"
                + "?"
                + "token="
                + token;
        log.info("click the link to verify User :{}", url);
    }
}
