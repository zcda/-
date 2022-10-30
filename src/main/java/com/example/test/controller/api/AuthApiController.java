package com.example.test.controller.api;


import com.example.test.Service.AccountService;
import com.example.test.Service.VerifyService;
import com.example.test.enetiy.resp.RestBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@ResponseBody
@RequestMapping("/api/auth")
public class AuthApiController {

    @Resource
    VerifyService verifyService;

    @Resource
    AccountService accountService;


    @GetMapping("/verify-code")
    public RestBean<Void> verifyCode(@RequestParam("mail")String mail){
        try {
            if (verifyService.SentVerifyCode(mail))
                return new RestBean<>(200,"邮件发送成功");
            else
            return new RestBean<>(400,"邮箱已存在");
        }catch (Exception e){
            return new RestBean<>(500,"邮件发送失败");
        }
    }

    @GetMapping("/forgotPws_verifyCode")
    public RestBean<Void> forgotPws_verifyCode(@RequestParam("mail")String mail){
        if (verifyService.SentVerify_forgotPws(mail))
                return new RestBean<>(200,"邮件发送成功");
        else
                return new RestBean<>(400,"改用户未注册，请注册");
    }

    @PostMapping("/register")
    public RestBean<Void> register(String username,
                             String password,
                             String email,
                             String verify ){
        if(verifyService.doVerify(email,verify)) {
            accountService.createAccount(username, password,email);
            return new RestBean<>(200,"注册成功");
        }
        else {
            return new RestBean<>(403,"注册失败");
        }
    }

    @PostMapping("/forgotPws")
    public RestBean<Void> forgotPws(String password,
                                   String email,
                                   String verify ){
        if(verifyService.doVerify(email,verify)) {
            accountService.chPws(email,password);
            return new RestBean<>(200,"修改密码成功");
        }
        else {
            return new RestBean<>(403,"修改密码失败");
        }
    }



}
