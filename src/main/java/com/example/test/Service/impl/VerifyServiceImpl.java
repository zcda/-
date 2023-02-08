package com.example.test.Service.impl;


import com.example.test.Service.VerifyService;
import com.example.test.enetiy.Account;
import com.example.test.enetiy.Group;
import com.example.test.enetiy.MeetingRoom;
import com.example.test.repo.AccountRepository;
import com.example.test.repo.GroupRepository;
import com.example.test.repo.MeetingRoomRepository;
import org.apache.commons.net.smtp.SMTPClient;
import org.apache.commons.net.smtp.SMTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

import javax.annotation.Resource;
import java.io.IOException;
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
    public boolean sendMail(String name, int rid, int gid, String startTime, String endTime) {
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
                message.setText("  您将在 "+meetingRoom.getName()+" ,具体地点为 "+meetingRoom.getAddress()+" ,有一场名为 "+name+" 的会议！"+"\n" +
                        "  "+"时间为"+startTime+"到"+endTime);
                //设置邮件发送给谁，可以多个，这里就发给你的QQ邮箱
                message.setTo(account.getEmail());
                //邮件发送者，这里要与配置文件中的保持一致
                message.setFrom(from);
                //OK，万事俱备只欠发送
                sender.send(message);
        }
        return true;
    }
    @Override
    public boolean checkEmail(String email) {
        if (!email.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")) {
            return false;
        }
        String log = "";
        String host = "";
        String hostName = email.split("@")[1];// 去掉@后面的
        //输出邮箱域名
        System.out.println("hostName:" + hostName);
        Record[] result = null;
        SMTPClient client = new SMTPClient();
        //设置超时时间
        client.setConnectTimeout(8000);
        try {
            // 查找DN的SMX记录
            org.xbill.DNS.Lookup lookup = new org.xbill.DNS.Lookup(hostName, Type.MX);
            lookup.run();
            if (lookup.getResult() != org.xbill.DNS.Lookup.SUCCESSFUL) {
                System.out.println("找不到MX记录");
                return false;
            } else {
                result = lookup.getAnswers();
                //循环打印DNS服务器
                for (int i = 0; i < result.length; i++) {
                    System.out.println(result[i].getAdditionalName().toString());
                }
            }
            // 循环连接到邮箱DNS服务器
            for (int i = 0; i < result.length; i++) {
                host = result[i].getAdditionalName().toString();
                //IP地址
                //System.out.println("ip:"+InetAddress.getByName(host).getHostAddress());
                int count=0;
                try {
                    client.connect(host);//捕获超时异常
                } catch (Exception e) {
                    count++;
                    //
                    if(count>=result.length){
                        return false;
                    }
                }
                if (!SMTPReply.isPositiveCompletion(client.getReplyCode())) {
                    client.disconnect();
                    continue;
                } else {
                    log += "邮箱mx记录" + hostName + "存在";
                    log += "成功连接到" + host;
                    break;
                }
            }
            client.login("163.com");
            client.setSender("www.linuxidc.com@linuxidc.com");// 发件人
            log += "=" + client.getReplyString();
            client.addRecipient(email);//发送邮件测试邮箱地址是否存在
            log += "\n";
            log += "=" + client.getReplyString();
            System.out.println(log+"  code"+client.getReplyCode());
            if (250 == client.getReplyCode()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.disconnect();
            } catch (IOException e) {
            }

        }
        return false;

    }
}
