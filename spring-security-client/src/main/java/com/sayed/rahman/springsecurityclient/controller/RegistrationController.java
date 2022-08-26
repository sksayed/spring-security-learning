package com.sayed.rahman.springsecurityclient.controller;


import com.sayed.rahman.springsecurityclient.entity.User;
import com.sayed.rahman.springsecurityclient.event.RegistrationCompleteEvent;
import com.sayed.rahman.springsecurityclient.model.UserModel;
import com.sayed.rahman.springsecurityclient.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController()
@RequestMapping("/api")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @GetMapping
    public String hello() {
        return "Welcome to Spring Security learning ";
    }

    @PostMapping(value = "/register" , consumes = {"application/json"})
    public String registeringUser(@RequestBody UserModel userModel , final HttpServletRequest servletRequest) {
        User user = userService.registerUser(userModel);
        eventPublisher.publishEvent(new RegistrationCompleteEvent(
                user, applicationUrl(servletRequest)));
        return " ";
    }

    private String applicationUrl(HttpServletRequest servletRequest) {
        return "http://"
                +servletRequest.getServerName()
                +":"
                +servletRequest.getServerPort()
                +servletRequest.getContextPath();
    }
}
