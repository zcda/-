package com.example.test.Service;


import java.util.Date;

public interface VerifyService {
     boolean SentVerifyCode(String mail);

     boolean SentVerify_forgotPws(String mail);

     boolean doVerify(String mail,String code);

     boolean sendMail(String name, int rid, int gid, Date startTime,Date endTime);
}
