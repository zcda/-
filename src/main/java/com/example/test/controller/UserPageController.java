package com.example.test.controller;


import com.example.test.Service.AccountService;
import com.example.test.Service.AdminService;
import com.example.test.Service.UserService;
import com.example.test.enetiy.Users;
import com.example.test.repo.AccountRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/page/user")
public class UserPageController {

    @Resource
    AccountRepository repository;

    @Resource
    AccountService accountService;

    @Resource
    AdminService adminService;

    //@Resource
    //UserService userService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(HttpSession session, Model model) {
        model.addAttribute("authUser",accountService.findUser(session));
        model.addAttribute("meetingRoomsCount",adminService.getAllMeetingRoomCounts());
        model.addAttribute("UserCount",adminService.UserCount());
        model.addAttribute("MeetingRoomCount",adminService.MeetingRoomCount());
        //model.addAttribute("MygroupCount",userSrevice.MygroupCount());
        //model.addAttribute("RecordCount",userSrevice.RecordofmygroupCount());
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
        //model.addAttribute("groups",userService.getmyGroups(uid));

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

    @RequestMapping("/records_of_group/{gid}")
    public String records_of_group(HttpSession session, Model model, @PathVariable int gid){
        model.addAttribute("authUser",accountService.findUser(session));
        //model.addAttribute("records",userService.findAllRecordByGid(gid));
        session.setAttribute("gid",gid);
        return "user/record_of_group";
    }

    @RequestMapping("/groups_by_me")
    public String group_by_me(HttpSession session, Model model){
        model.addAttribute("authUser",accountService.findUser(session));
        //model.addAttribute("groups",userService.getGroupsbyme(uid));
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




}
