package com.example.test.Service.impl;

import com.example.test.enetiy.Account;
import com.example.test.repo.AccountRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AuthService implements UserDetailsService {

    @Resource
    AccountRepository repository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account=repository.findAccountByEmail(username);
        if(account==null) account=repository.findAccountByUsername(username);//记住我返回的是存进去的username 不是你想要的哪个username
        if(account==null) throw new UsernameNotFoundException("");
        return User
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }

}
