package com.example.test.repo;

import com.example.test.enetiy.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findAccountByEmail(String email);

    Account findAccountByUsername(String username);

    List<Account> findAccountsByRole(String role);



    @Transactional    //DML操作需要事务环境，可以不在这里声明，但是调用时一定要处于事务环境下
    @Modifying     //表示这是一个DML操作
    @Query("update Account set password = ?2 where email = ?1") //这里操作的是一个实体类对应的表，参数使用?代表，后面接第n个参数
    int updatePasswordByEmail(String email, String newPassword);




}