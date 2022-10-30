package com.example.test;

import com.example.test.Service.AdminService;
import com.example.test.repo.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import javax.annotation.Resource;
import javax.transaction.Transactional;

@SpringBootTest
class TestApplicationTests {

    @Resource
    AccountRepository repository;
    @Resource
    MeetingRoomRepository meetingRoomRepository;
    @Resource
    DeviceRepository deviceRepository;

    @Resource
    UserRepository userRepository;
    @Resource
    GroupRepository groupRepository;

    @Resource
    AdminService service;

    @Transactional
    @Commit
    @Test
    void contextLoads() {
//        User user=new User("王八","男","18182335100");
//        user=userRepository.save(user);
//        repository.addUser(2,user);
//        System.out.println(repository.findById(2));
//        Group group=new Group("08班","一段描述");
//        group=groupRepository.save(group);
//        List<Group> groupList=new LinkedList<>();
//        groupList.add(group);
//        userRepository.updateGroup(15,groupList);

//        userRepository.test(1,new LinkedList<Group>());
      //  repository.deleteById(5);
//        repository.findById(9).ifPresent(account -> account.setUser(userRepository.findById(15).get()));
//        System.out.println(repository.findById(5));
    }

    @Test
    @Transactional
    @Commit
    public void test(){

        System.out.println(meetingRoomRepository.findAll().size());
    }

}
