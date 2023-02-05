package com.example.test.Service;


public interface VerifyService {
     boolean SentVerifyCode(String mail);

     boolean SentVerify_forgotPws(String mail);

     boolean doVerify(String mail,String code);

     boolean sendMail(String name, int rid, int gid, String startTime,String endTime);

     boolean checkEmail(String email);
}
