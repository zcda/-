package com.example.test.controller;


import com.example.test.Service.AccountService;
import com.example.test.Service.AdminService;
import com.example.test.Service.UserService;
import com.example.test.enetiy.Group;
import com.example.test.enetiy.Record;
import com.example.test.enetiy.Users;
import com.example.test.repo.AccountRepository;
import com.example.test.repo.RecordRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/page/user")
public class UserPageController {

    @Resource
    AccountRepository repository;

    @Resource
    AccountService accountService;

    @Resource
    AdminService adminService;

    @Resource
    UserService userService;

    @Resource
    RecordRepository recordRepository;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(HttpSession session, Model model) {
        model.addAttribute("authUser",accountService.findUser(session));
        model.addAttribute("meetingRoomsCount",adminService.getAllMeetingRoomCounts());
        model.addAttribute("UserCount",adminService.UserCount());
        model.addAttribute("MeetingRoomCount",adminService.MeetingRoomCount());
        model.addAttribute("MygroupCount",userService.MygroupCount(accountService.findUser(session).getAccountDetail().getUid()));
        model.addAttribute("RecordCount",userService.RecordofmygroupCount(accountService.findUser(session).getAccountDetail().getUid()));
        return "user/index";
    }

    @RequestMapping(value = "/meetingrooms" ,method = RequestMethod.GET)
    public String meetingrooms(HttpSession session, Model model) {
        model.addAttribute("authUser",accountService.findUser(session));
        model.addAttribute("meetingRooms",adminService.getAllMeetingRooms());
        model.addAttribute("meetingRoomsName",adminService.getAllMeetingRoomsName());
        model.addAttribute("meetingRoomsCount",adminService.getAllMeetingRoomCounts());
        return "user/meetingroom";
    }

    @RequestMapping("/showDevice/{rid}")
    public String showDevice(HttpSession session, Model model, @PathVariable int rid){
        model.addAttribute("authUser",accountService.findUser(session));
        model.addAttribute("devices",adminService.getDevicesByRid(rid));
        session.setAttribute("rid",rid);
        return "user/device";
    }

    @RequestMapping(value = "/users",method = RequestMethod.GET)
    public String users(HttpSession session, Model model){
        model.addAttribute("authUser",accountService.findUser(session));
        model.addAttribute("users",accountService.findUsers());

        return "user/users";
    }

    @RequestMapping(value = "/users_detail/{id}",method = RequestMethod.GET)
    public String users_detail(HttpSession session, Model model, @PathVariable int id){
        model.addAttribute("authUser",accountService.findUser(session));

        Users accountDetail =accountService.findUserById(id);
        session.setAttribute("id",id);
        if(accountDetail ==null) {
            model.addAttribute("fail",false);
            return "user/user_detail";
        }
        model.addAttribute("user", accountDetail);
        return "user/user_detail";

    }

    @RequestMapping("/mygroups")
    public String mygroups(HttpSession session, Model model){
        model.addAttribute("authUser",accountService.findUser(session));
        model.addAttribute("groups",userService.getmyGroups(accountService.findUser(session).getAccountDetail().getUid()));

        return "user/mygroup";
    }

    @RequestMapping("/user_in_group/{gid}")
    public String user_in_group(HttpSession session, Model model, @PathVariable int gid){
        model.addAttribute("authUser",accountService.findUser(session));
        model.addAttribute("users",adminService.getAllUserByGid(gid));
        model.addAttribute("group",adminService.findGroupByGid(gid));
        session.setAttribute("gid",gid);
        return "user/user_in_group";
    }

    @RequestMapping("/record_of_group/{gid}")
    public String records_of_group(HttpSession session, Model model, @PathVariable int gid){
        model.addAttribute("authUser",accountService.findUser(session));
        model.addAttribute("records",userService.findAllRecordByGid(gid));
        session.setAttribute("gid",gid);
        return "user/record_of_group";
    }

    @RequestMapping("/groups_by_me")
    public String group_by_me(HttpSession session, Model model){
        model.addAttribute("authUser",accountService.findUser(session));
        model.addAttribute("groups",adminService.getAllGroups());
        model.addAttribute("myGroups",userService.getGroupsbyme(accountService.findUser(session).getAccountDetail().getUid()));
        return "user/group_by_me";
    }

    @RequestMapping("/group_user_manage/{gid}")
    public String group_user_manage(HttpSession session, Model model, @PathVariable int gid){
        model.addAttribute("authUser",accountService.findUser(session));
        model.addAttribute("users",adminService.getAllUser());
        model.addAttribute("group",adminService.findGroupByGid(gid));
        session.setAttribute("gid",gid);
        return "user/group_user_manage";
    }

    @RequestMapping("/addgroup")
    public String addGroup(HttpSession session, Model model){
        model.addAttribute("authUser",accountService.findUser(session));
        model.addAttribute("fail",false);
        return "user/addgroup";
    }

    @RequestMapping("/modify_group/{gid}")
    public String modify_groups(HttpSession session, Model model, @PathVariable int gid){
        model.addAttribute("authUser",accountService.findUser(session));
        model.addAttribute("group",adminService.findGroupByGid(gid));
        model.addAttribute("fail",false);
        session.setAttribute("gid",gid);
        return "user/modify_group";
    }
    @RequestMapping("/borrows")
    public String borrows(HttpSession session, Model model){
        model.addAttribute("authUser",accountService.findUser(session));
        List<Record> records = new LinkedList<>();
        userService.getmyGroups(accountService.findUser(session).getAccountDetail().getUid()).forEach(group -> {
            records.addAll(recordRepository.findAllByGroup(group));
        });
        model.addAttribute("recordsOfMine",records);
        model.addAttribute("records",adminService.getAllRecord());
        model.addAttribute("groups",userService.getmyGroups(accountService.findUser(session).getAccountDetail().getUid()));
        return "user/borrows";
    }
    @RequestMapping("/addBorrow")
    public String addBorrow(HttpSession session, Model model){
        model.addAttribute("authUser",accountService.findUser(session));
        model.addAttribute("groups",userService.getmyGroups(accountService.findUser(session).getAccountDetail().getUid()));
        model.addAttribute("meetingRooms",adminService.getAllMeetingRooms());
        model.addAttribute("fail",false);
        return "user/add-borrow";
    }
    @RequestMapping("/myinfo")
    public String myinfo(HttpSession session, Model model){
        model.addAttribute("authUser",accountService.findUser(session));
        model.addAttribute("groups",userService.getmyGroups(accountService.findUser(session).getAccountDetail().getUid()));
        model.addAttribute("meetingRooms",adminService.getAllMeetingRooms());
        model.addAttribute("accountDetail",adminService.findUsersByUid(accountService.findUser(session).getAccountDetail().getUid()));
        model.addAttribute("fail",false);
        return "user/myinfo";
    }

}
