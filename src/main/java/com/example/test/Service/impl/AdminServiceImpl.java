package com.example.test.Service.impl;


import com.example.test.Service.AccountService;
import com.example.test.Service.AdminService;
import com.example.test.enetiy.*;
import com.example.test.repo.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    @Resource
    MeetingRoomRepository meetingRoomRepository;

    @Resource
    DeviceRepository deviceRepository;

    @Resource
    AccountRepository accountRepository;

    @Resource
    UserRepository userRepository;

    @Resource
    GroupRepository groupRepository;

    @Resource
    RecordRepository recordRepository;

    @Resource
    AccountService accountService;

    @Override
    public List<MeetingRoom> getAllMeetingRooms() {
       return meetingRoomRepository.findAll();
    }

    @Override
    public String[] getAllMeetingRoomsName() {
        List<MeetingRoom> meetingRooms=meetingRoomRepository.findAll();
        String[] strings=new String[meetingRooms.size()];
        for (int i = 0; i < strings.length; i++) {
            strings[i]=meetingRooms.get(i).getName();
        }
        return strings;
    }

    @Override
    public int[] getAllMeetingRoomCounts() {
        List<MeetingRoom> meetingRooms=meetingRoomRepository.findAll();
        int[] res=new int[meetingRooms.size()];
        for (int i = 0; i < res.length; i++) {
            res[i]=meetingRooms.get(i).getCount();
        }
        return res;
    }

    @Override
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    @Override
    public List<Device> getDevicesByRid(int rid) {
        return meetingRoomRepository.findById(rid).get().getDevices();
    }

    @Override
    public List<Users> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public List<Users> getAllUserByGid(int gid) {
        List<Users> users=userRepository.findAll();
        List<Users> usersList=new LinkedList<>();
        Group group=groupRepository.findById(gid).get();
        users.forEach(users1 -> {
            if(users1.getGroup().contains(group)) usersList.add(users1);
        });
        return usersList;
    }

    @Override
    public List<Record> getAllRecord() {
        return recordRepository.findAll();
    }

    @Override
    public Group findGroupByGid(int gid) {
        return groupRepository.findById(gid).get();
    }

    @Override
    public MeetingRoom findRoomByRid(int rid) {
        return meetingRoomRepository.findById(rid).get();
    }

    @Override
    public Device findDeviceByDid(int did) {
        return deviceRepository.findById(did).get();
    }

    @Override
    public Users findUsersByUid(int uid) {
        return userRepository.findById(uid).get();
    }

    @Override
    public void modifyMeetingRoom(int rid, String name, String address, int max_capacity) {
        meetingRoomRepository.findById(rid).ifPresent(meetingRoom -> {
        meetingRoom.setName(name);
        meetingRoom.setAddress(address);
        meetingRoom.setMax_capacity(max_capacity);
    });

    }

    @Override
    public void modifyAccount(int id, String name, String password, String email) {
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        System.out.println(password);
        if(password==""){
            accountRepository.findById(id).ifPresent(account -> {
                account.setUsername(name);
                account.setEmail(email);
            });
        }else{
                accountRepository.findById(id).ifPresent(account -> {
                    account.setUsername(name);
                    account.setEmail(email);
                    account.setPassword(encoder.encode(password));
                });
            }


    }

    @Override
    public void modifyAccountDetail(int uid, String name, String phoneNumber, String sex) {
        userRepository.findById(uid).ifPresent(users -> {
            users.setName(name);
            users.setPhoneNumber(phoneNumber);
            users.setSex(sex);
        });
    }

    @Override
    public void modifyDevice(int did, String name, String details, int count) {
        deviceRepository.findById(did).ifPresent(device -> {
            device.setName(name);
            device.setDetail(details);
            device.setCount(count);
        });
    }

    @Override
    public void modifyGroup(int gid, String name, String details) {
        groupRepository.findById(gid).ifPresent(group -> {
                    group.setName(name);
                    group.setDetails(details);
                }
        );
    }

    @Override
    public void deleteMeetingRoomByRid(int rid) {
        recordRepository.findAllByMeetingRoom(meetingRoomRepository.getById(rid)).forEach(record -> {
            record.setMeetingRoom(null);
            recordRepository.deleteById(record.getRecID());
        });

        meetingRoomRepository.deleteById(rid);
    }

    @Override
    public void deleteDevice(int did) {
        deviceRepository.deleteById(did);
    }

    @Override
    public void deleteGroup(int gid) {
        List<Users> users=userRepository.findAll();
        Group group=groupRepository.findById(gid).get();
        users.forEach(user -> {
            if(user.getGroup().contains(group)){
                user.getGroup().remove(group);
            }
        });
        recordRepository.findAllByGroup(group).forEach(record -> {
            record.setGroup(null);
            recordRepository.deleteById(record.getRecID());
        });
       groupRepository.deleteById(gid);
    }

    @Override
    public void addMeetingRoom(String name, String address, int max_capacity) {
        MeetingRoom meetingRoom=new MeetingRoom(name,address,max_capacity);
        meetingRoomRepository.save(meetingRoom);
    }

    @Override
    public void addDevice(int rid, String name, String detail_describe, int count) {
        Device device=new Device(name,detail_describe,count,rid);
        deviceRepository.save(device);
    }

    @Override
    public void addAccountDetail(int id, String name, String phone_number, String sex) {
        Users accountDetail =userRepository.save(new Users(name,sex,phone_number));
        accountRepository.findById(id).ifPresent(account -> account.setAccountDetail(accountDetail));
    }

    @Override
    public void addGroup(String name, String details) {
        Group group=new Group(name,details);
        groupRepository.save(group);
    }

    @Override
    public void deleteAccount(int id) {
        if (accountRepository.findById(id).get().getAccountDetail()!=null){
        deleteUser(accountRepository.findById(id).get().getAccountDetail().getUid(),id);}
        accountRepository.deleteById(id);
    }

    @Override
    public void deleteUser(int uid,int id) {
        userRepository.findById(uid).ifPresent(users -> {
            users.setGroup(null);
        });

        accountRepository.findById(id).ifPresent(account -> account.setAccountDetail(null));
        userRepository.deleteById(uid);
    }

    @Override
    public void deleteUserInGroup(int gid, int uid) {
        Users user=userRepository.findById(uid).get();
        Group group=groupRepository.findById(gid).get();
        if(user.getGroup().contains(group)){
            user.getGroup().remove(group);
        }
    }

    @Override
    public void deleteRecordById(int recid) {
        recordRepository.findById(recid).ifPresent(record -> {
            record.setMeetingRoom(null);
            record.setGroup(null);
        });
        recordRepository.deleteById(recid);
    }

    @Override
    public int UserCount() {
        return accountService.findUsers().size();
    }

    @Override
    public int MeetingRoomCount() {
        return meetingRoomRepository.findAll().size();
    }

    @Override
    public int RecordCount() {
        return recordRepository.findAll().size();
    }

    @Override
    public int DeviceCount() {
        return deviceRepository.findAll().size();
    }

    @Override
    public void addUserInGroup(int gid, int uid) {
        Users user=userRepository.findById(uid).get();
        Group group=groupRepository.findById(gid).get();
        user.getGroup().add(group);
    }

    @Override
    public boolean addBorrow(int rid,int gid, String name, Date startTIme, Date endTime) {
        Group group=groupRepository.findById(gid).get();
        if (startTIme.after(endTime)) {
            System.out.println(5);
            return false;
        }
        AtomicBoolean flag= new AtomicBoolean(true);
        recordRepository.findAllByMeetingRoom(meetingRoomRepository.findById(rid).get()).forEach(record -> {
            if (record.getStartTime().before(startTIme)&&record.getEndTime().after(startTIme)){
                flag.set(false);
                System.out.println(1);
                return;
            }
            if (record.getStartTime().before(endTime)&&record.getEndTime().after(endTime)){
                flag.set(false);
                System.out.println(2);
                return;
            }
            if (record.getStartTime().after(startTIme)&&record.getEndTime().before(endTime)){
                flag.set(false);
                System.out.println(3);
                return;
            }
        });
        if (flag.get()){
            recordRepository.save(new Record().setName(name).setEndTime(endTime).setStartTime(startTIme)
                    .setMeetingRoom(meetingRoomRepository.findById(rid).get()).setGroup(groupRepository.findById(gid).get())
            );
            meetingRoomRepository.findById(rid).ifPresent(meetingRoom -> meetingRoom.setCount(meetingRoom.getCount()+1));
        }
        return flag.get();
    }

}
