package com.example.test.Service;

import com.example.test.enetiy.Account;
import com.example.test.enetiy.Users;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface AccountService {
    void createAccount(String username,String password,String email);
    Account findUser(HttpSession session);
    void chPws(String email,String newPws);
    List<Account> findUsers();
    Users findUserById(int id);

}
