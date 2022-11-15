package com.example.test.Service;

import com.example.test.enetiy.*;

import java.util.Date;
import java.util.List;

public interface AdminService {
    List<MeetingRoom> getAllMeetingRooms();
    String[] getAllMeetingRoomsName();
    int[] getAllMeetingRoomCounts();
    List<Group> getAllGroups();
    List<Device> getDevicesByRid(int rid);
    List<Users> getAllUser();
    List<Users> getAllUserByGid(int gid);
    List<Record> getAllRecord();
    Group findGroupByGid(int gid);
    MeetingRoom findRoomByRid(int rid);
    Device findDeviceByDid(int did);
    Users findUsersByUid(int uid);
    void modifyMeetingRoom(int rid,String name,String address,int max_capacity);
    void modifyAccount(int id,String name,String password,String email);
    void modifyAccountDetail(int uid,String name,String phoneNumber,String sex);
    void modifyDevice(int did,String name,String details,int count);
    void modifyGroup(int gid,String name,String details);
    void deleteMeetingRoomByRid(int rid);
    void deleteDevice(int did);
    void deleteGroup(int gid);
    void addMeetingRoom(String name,String address,int max_capacity);
    void addDevice(int rid,String name,String detail_describe,int count);
    void addAccountDetail(int id,String name,String phone_number,String sex);
    void addGroup(String name,String details);
    void addUserInGroup(int gid,int uid);
    boolean addBorrow(int rid,int gid, String name, Date startTIme,Date endTime);
    void deleteAccount(int id);
    void deleteUser(int uid,int id);
    void deleteUserInGroup(int gid,int uid);
    void deleteRecordById(int recid);

    int UserCount();
    int MeetingRoomCount();
    int RecordCount();
    int DeviceCount();

}
