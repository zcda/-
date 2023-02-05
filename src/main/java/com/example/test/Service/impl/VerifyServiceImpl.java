package com.example.test.Service.impl;


import com.example.test.Service.VerifyService;
import com.example.test.enetiy.Account;
import com.example.test.enetiy.Group;
import com.example.test.enetiy.MeetingRoom;
import com.example.test.repo.AccountRepository;
import com.example.test.repo.GroupRepository;
import com.example.test.repo.MeetingRoomRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@Service
public class VerifyServiceImpl implements VerifyService {

    @Resource
    JavaMailSender sender;
    @Resource
    StringRedisTemplate template;
    @Resource
    AccountRepository repository;
    @Resource
    MeetingRoomRepository meetingRoomRepository;
    @Resource
    GroupRepository groupRepository;


    //按下ctrl不会跳转
    @Value("${spring.mail.username}")
    String from;

    @Override
    public boolean SentVerifyCode(String mail) {
        Account account=repository.findAccountByEmail(mail);
        if(account==null) {
            //SimpleMailMessage是一个比较简易的邮件封装，支持设置一些比较简单内容
            SimpleMailMessage message = new SimpleMailMessage();
            //设置邮件标题
            message.setSubject("验证码");
            Random random=new Random();
            int verifyCode= random.nextInt(89999)+10000;
            //设置邮件内容
            message.setText("验证码为： " +verifyCode+
                    "三分钟过期");

            template.opsForValue().set("verify:code:"+mail ,verifyCode+"",3, TimeUnit.MINUTES);
            //设置邮件发送给谁，可以多个，这里就发给你的QQ邮箱
            message.setTo(mail);
            //邮件发送者，这里要与配置文件中的保持一致
            message.setFrom(from);
            //OK，万事俱备只欠发送
            sender.send(message);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean SentVerify_forgotPws(String mail) {
        Account account=repository.findAccountByEmail(mail);
        if(account!=null) {
            //SimpleMailMessage是一个比较简易的邮件封装，支持设置一些比较简单内容
            SimpleMailMessage message = new SimpleMailMessage();
            //设置邮件标题
            message.setSubject("忘记密码的验证码");
            Random random=new Random();
            int verifyCode= random.nextInt(89999)+10000;
            //设置邮件内容
            message.setText("验证码为： " +verifyCode+
                    "三分钟过期");
            template.opsForValue().set("verify:code:"+mail ,verifyCode+"",3, TimeUnit.MINUTES);
            //设置邮件发送给谁，可以多个，这里就发给你的QQ邮箱
            message.setTo(mail);
            //邮件发送者，这里要与配置文件中的保持一致
            message.setFrom(from);
            //OK，万事俱备只欠发送
            sender.send(message);
            return true;
        }else{
            return false;
        }
    }





    @Override
    public boolean doVerify(String mail, String code) {
        String string=template.opsForValue().get("verify:code:"+mail);
        if (string==null||!string.equals(code)) return false;
        template.delete("verify:code:"+mail);
        return true;
    }

    @Override
    public boolean sendMail(String name, int rid, int gid, Date startTime, Date endTime) {
        Group group=groupRepository.findById(gid).get();
        MeetingRoom meetingRoom=meetingRoomRepository.findById(rid).get();
        List<Account> accounts=new LinkedList<>();
        //查找对应组的全部人员信息
        repository.findAll().forEach(account -> {
            if (account.getAccountDetail()!=null&&account.getAccountDetail().getGroup().contains(group))
                accounts.add(account);
        });

        if (accounts.isEmpty()) return false;
        for (Account account:accounts
             ) {
                //SimpleMailMessage是一个比较简易的邮件封装，支持设置一些比较简单内容
                SimpleMailMessage message = new SimpleMailMessage();
                //设置邮件标题
                message.setSubject("您有一个新会议需要参加！");
                //设置邮件内容
                message.setText("  您将在"+meetingRoom.getName()+",具体地点为"+meetingRoom.getAddress()+",有一场名为"+name+"的会议！"+"                                " +
                        ""+"时间为"+startTime+"到"+endTime);
                //设置邮件发送给谁，可以多个，这里就发给你的QQ邮箱
                message.setTo(account.getEmail());
                //邮件发送者，这里要与配置文件中的保持一致
                message.setFrom(from);
                //OK，万事俱备只欠发送
                sender.send(message);
        }
        return true;
    }
}
