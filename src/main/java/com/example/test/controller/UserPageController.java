package com.example.test.controller;


import com.example.test.Service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/page/user")
public class UserPageController {

    @Resource
    AccountService accountService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(HttpSession session, Model model) {
        model.addAttribute("authUser",accountService.findUser(session));
        return "user/index";
    }



}
