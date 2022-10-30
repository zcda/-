package com.example.test.Service.impl;

import com.example.test.Service.AccountService;
import com.example.test.enetiy.Account;
import com.example.test.enetiy.Users;
import com.example.test.repo.AccountRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;


@Service
public class AccountServiceImpl implements AccountService {
    @Resource
    AccountRepository repository;


    @Override
    public void createAccount(String username, String password,String email) {
        Account account=new Account();
        account.setUsername(username);
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        account.setPassword(encoder.encode(password));
        account.setEmail(email);
        account.setRole("user");
        repository.save(account);
    }

    @Override
    public Account findUser(HttpSession session) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            user = repository.findAccountByUsername(authentication.getName());
            session.setAttribute("authUser", user);
        }
        return user;
    }

    @Override
    public void chPws(String email, String newPws) {
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();

        repository.updatePasswordByEmail(email, encoder.encode(newPws));
    }

    @Override
    public List<Account> findUsers() {
        return repository.findAccountsByRole("user");
    }

    @Override
    public Users findUserById(int id) {
        return  repository.findById(id).get().getAccountDetail();
    }


}
