package com.example.test.Service.impl;

import com.example.test.Service.UserService;
import com.example.test.enetiy.Group;
import com.example.test.enetiy.Record;
import com.example.test.enetiy.Users;
import com.example.test.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Resource
    GroupRepository groupRepository;

    @Resource
    UserRepository userRepository;

    @Resource
    RecordRepository recordRepository;


    @Override
    public Set<Group> getmyGroups(int uid) {
        Users users=userRepository.findById(uid).get();
        Set<Group> groups =users.getGroup();
        return groups;
    }

    @Override
    public List<Record> findAllRecordByGid(int gid) {
        Group group = groupRepository.findById(gid).get();
        return recordRepository.findAllByGroup(group);
    }

    @Override
    public Set<Group> getGroupsbyme(int uid) {
        //前提：数据库子新增创建者（uid)与小组(gid)关系表
        //根据uid搜索数据库找到对应小组，显示所有小组信息（组id、组名、组介绍）
//        userRepository.findById(uid).ifPresent(user -> {
//            Set<Group> group = user.getGroup();
//        });
//        return group;
        Users users=userRepository.findById(uid).get();
        Set<Group> groups =users.getGroup();
        return groups;
    }

    @Override
    public void deleteGroupbyme(int gid) {
        groupRepository.deleteById(gid);
    }

    @Override
    public void addGroup(String name, String details) {
        Group group=new Group(name,details);
        groupRepository.save(group);
    }

    @Override
    public int MygroupCount(int uid) {
        Users users=userRepository.findById(uid).get();
        if(users.getGroup()==null) return 0;
        Set<Group> groups =  users.getGroup();
        return groups.size();
    }

    @Override
    public int RecordofmygroupCount(int uid) {
        Users users=userRepository.findById(uid).get();
        if(users.getGroup()==null) return 0;
        Set<Group> groups =  users.getGroup();
        AtomicInteger a = new AtomicInteger();
        groups.forEach(group -> {
            a.addAndGet(recordRepository.findAllByGroup(group).size());
        });
        return a.get();
    }
}
