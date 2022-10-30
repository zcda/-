package com.example.test.controller;


import com.example.test.Service.AccountService;
import com.example.test.Service.AdminService;
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
@RequestMapping("/page/admin")
public class AdminPageController {
    @Resource
    AccountRepository repository;

    @Resource
    AccountService accountService;

    @Resource
    AdminService adminService;


    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(HttpSession session, Model model) {
        model.addAttribute("authUser",accountService.findUser(session));
        model.addAttribute("meetingRooms",adminService.getAllMeetingRooms());
        model.addAttribute("UserCount",adminService.UserCount());
        model.addAttribute("MeetingRoomCount",adminService.MeetingRoomCount());
        model.addAttribute("RecordCount",adminService.RecordCount());
        model.addAttribute("DeviceCount",adminService.DeviceCount());
        return "admin/index";
    }

    @RequestMapping(value = "/users",method = RequestMethod.GET)
    public String users(HttpSession session, Model model){
        model.addAttribute("authUser",accountService.findUser(session));
        model.addAttribute("users",accountService.findUsers());

        return "admin/accounts";
    }

    @RequestMapping("/groups")
    public String groups(HttpSession session, Model model){
        model.addAttribute("authUser",accountService.findUser(session));
        model.addAttribute("groups",adminService.getAllGroups());
        return "admin/group";
    }

    @RequestMapping("/borrows")
    public String borrows(HttpSession session, Model model){
        model.addAttribute("authUser",accountService.findUser(session));
        model.addAttribute("records",adminService.getAllRecord());
        return "admin/borrows";
    }

    @RequestMapping(value = "/users_detail/{id}",method = RequestMethod.GET)
    public String users_detail(HttpSession session, Model model, @PathVariable int id){
        model.addAttribute("authUser",accountService.findUser(session));

        Users accountDetail =accountService.findUserById(id);
        session.setAttribute("id",id);
        if(accountDetail ==null) {
            model.addAttribute("fail",false);
            return "admin/add-account_detail";
        }
        model.addAttribute("user", accountDetail);
        return "admin/account_detail";

    }

    @RequestMapping("/showDevice/{rid}")
    public String showDevice(HttpSession session, Model model, @PathVariable int rid){
        model.addAttribute("authUser",accountService.findUser(session));
        model.addAttribute("devices",adminService.getDevicesByRid(rid));
        session.setAttribute("rid",rid);
        return "admin/device";
    }

    @RequestMapping("/addMeetingRoom")
    public String addMeetingRoom(HttpSession session, Model model){
        model.addAttribute("authUser",accountService.findUser(session));
        model.addAttribute("fail",false);
        return "admin/add-meetingRoom";
    }

    @RequestMapping("/addDevice")
    public String addDevice(HttpSession session, Model model){
        model.addAttribute("authUser",accountService.findUser(session));
        model.addAttribute("fail",false);
        return "admin/add-device";
    }

    @RequestMapping("/addAccount")
    public String addAccount(HttpSession session, Model model){
        model.addAttribute("authUser",accountService.findUser(session));
        model.addAttribute("fail",false);
        return "admin/add-account";
    }

    @RequestMapping("/addGroup")
    public String addGroup(HttpSession session, Model model){
        model.addAttribute("authUser",accountService.findUser(session));
        model.addAttribute("fail",false);
        return "admin/add-group";
    }

    @RequestMapping("/addBorrow")
    public String addBorrow(HttpSession session, Model model){
        model.addAttribute("authUser",accountService.findUser(session));
        model.addAttribute("groups",adminService.getAllGroups());
        model.addAttribute("meetingRooms",adminService.getAllMeetingRooms());
        model.addAttribute("fail",false);
        return "admin/add-borrow";
    }

    @RequestMapping("/group_user_manage/{gid}")
    public String group_user_manage(HttpSession session, Model model, @PathVariable int gid){
        model.addAttribute("authUser",accountService.findUser(session));
        model.addAttribute("users",adminService.getAllUser());
        model.addAttribute("group",adminService.findGroupByGid(gid));
        session.setAttribute("gid",gid);
        return "admin/group_user_manage";
    }

    @RequestMapping("/user_in_group/{gid}")
    public String user_in_group(HttpSession session, Model model, @PathVariable int gid){
        model.addAttribute("authUser",accountService.findUser(session));
        model.addAttribute("users",adminService.getAllUserByGid(gid));
        model.addAttribute("group",adminService.findGroupByGid(gid));
        session.setAttribute("gid",gid);
        return "admin/user_in_group";
    }

    @RequestMapping("/modify_group/{gid}")
    public String modify_groups(HttpSession session, Model model, @PathVariable int gid){
        model.addAttribute("authUser",accountService.findUser(session));
        model.addAttribute("group",adminService.findGroupByGid(gid));
        model.addAttribute("fail",false);
        session.setAttribute("gid",gid);
        return "admin/modify_group";
    }

    @RequestMapping("/modify_meetingRoom/{rid}")
    public String modify_meetingRoom(HttpSession session, Model model, @PathVariable int rid){
        model.addAttribute("authUser",accountService.findUser(session));
        model.addAttribute("room",adminService.findRoomByRid(rid));
        model.addAttribute("fail",false);
        session.setAttribute("rid",rid);
        return "admin/modify_meetingRoom";
    }
    @RequestMapping("/modify_device/{did}")
    public String modify_device(HttpSession session, Model model, @PathVariable int did){
        model.addAttribute("authUser",accountService.findUser(session));
        model.addAttribute("device",adminService.findDeviceByDid(did));
        model.addAttribute("fail",false);
        session.setAttribute("did",did);
        return "admin/modify_device";
    }

    @RequestMapping("/modify_account/{id}")
    public String modify_account(HttpSession session, Model model, @PathVariable int id){
        model.addAttribute("authUser",accountService.findUser(session));
        model.addAttribute("account",repository.findById(id).get());
        model.addAttribute("fail",false);
        session.setAttribute("id",id);
        return "admin/modify_account";
    }

    @RequestMapping("/modify_accountDetail/{uid}")
    public String modify_accountDetail(HttpSession session, Model model, @PathVariable int uid){
        model.addAttribute("authUser",accountService.findUser(session));
        model.addAttribute("accountDetail",adminService.findUsersByUid(uid));
        model.addAttribute("fail",false);
        session.setAttribute("uid",uid);
        return "admin/modify_account_detail";
    }
}
