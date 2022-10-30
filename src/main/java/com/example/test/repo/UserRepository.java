package com.example.test.repo;

import com.example.test.enetiy.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users,Integer> {


//    @Transactional    //DML操作需要事务环境，可以不在这里声明，但是调用时一定要处于事务环境下
//    @Modifying     //表示这是一个DML操作
//    @Query(value = "update User set group = ?2 where uid = ?1") //这里操作的是一个实体类对应的表，参数使用?代表，后面接第n个参数
//    int updateUser();   不可用

}
