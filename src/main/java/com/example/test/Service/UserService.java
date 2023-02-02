package com.example.test.Service;

import com.example.test.enetiy.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface UserService {
    //根据普通用户uid搜索表user_group得到相关gid,再根据gid得到自己所在组信息以及根据gid再次检索表user_group得到所有组成员uid，进而得到成员信息
    Set<Group> getmyGroups(int uid);
    //根据gid搜索得到所有该组的预定信息
    List<Record> findAllRecordByGid(int gid);
    //前提：数据库子新增创建者（uid)与小组(gid)关系表
    //根据uid搜索数据库找到对应小组，显示所有小组信息（组id、组名、组介绍）
    Set<Group> getGroupsbyme(int uid);
    //
    void deleteGroupbyme(int gid);

    void addGroup(String name,String details);

    int MygroupCount(int uid);

    int RecordofmygroupCount(int uid);
}
