package com.example.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthPageController {


    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/register")
    public String register(){
        return  "register";
    }
    @RequestMapping("/logout")
    public String logout(){
        return "login";
    }
    @RequestMapping("/forgot-pws")
    public String forgetPwd(){
        return "forgot-pws";
    }
}
