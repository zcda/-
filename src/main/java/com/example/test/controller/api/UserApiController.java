package com.example.test.controller.api;

import com.example.test.Service.AccountService;
import com.example.test.Service.AdminService;
import com.example.test.Service.UserService;
import com.example.test.Service.VerifyService;
import com.example.test.repo.AccountRepository;
import lombok.SneakyThrows;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/api/user")

public class UserApiController {
    @Resource
    AccountRepository repository;

    @Resource
    AdminService adminService;

    @Resource
    UserService userService;

    @Resource
    AccountService accountService;

    @Resource
    VerifyService verifyService;

    @RequestMapping("/delete_group/{gid}")
    public String delete_group(@PathVariable int gid, HttpSession session){
        userService.deleteGroupbyme(gid);
        return "redirect:/page/user/groups_by_me";
    }

    @RequestMapping("/users_group_delete/{uid}")
    public String users_group_delete(@PathVariable int uid,HttpSession session){
        adminService.deleteUserInGroup((Integer) session.getAttribute("gid"),uid);
        return "redirect:/page/user/group_user_manage/"+ (Integer) session.getAttribute("gid");
    }

    @RequestMapping("/userOutGroup/{gid}")
    public String userOutGroup(@PathVariable int gid,HttpSession session){
        adminService.deleteUserInGroup(gid,accountService.findUser(session).getAccountDetail().getUid());
        return "redirect:/page/user/groups_by_me";
    }

    @RequestMapping("/users_group_add/{uid}")
    public String users_group_add(@PathVariable int uid,HttpSession session){
        adminService.addUserInGroup((Integer) session.getAttribute("gid"),uid);
        return "redirect:/page/user/group_user_manage/"+ (Integer) session.getAttribute("gid");
    }

    @RequestMapping("/userInGroup/{gid}")
    public String userInGroup(@PathVariable int gid,HttpSession session){
        adminService.addUserInGroup(gid,accountService.findUser(session).getAccountDetail().getUid());
        return "redirect:/page/user/groups_by_me";
    }

    @RequestMapping("/add-Group")
    public String addGroup(Model model, @Param("name")String name, @Param("details")String details, HttpSession session){
        if (name==""){
            model.addAttribute("authUser",accountService.findUser(session));
            model.addAttribute("fail",true);
            return "user/addgroup";
        }
        try{
            userService.addGroup(name,details);
            return "redirect:/page/user/groups_by_me";
        }catch (Exception e){
            model.addAttribute("authUser",accountService.findUser(session));
            model.addAttribute("fail",true);
            return "user/addgroup";
        }
    }

    @RequestMapping("/modify_group")
    public String modify_group(Model model,HttpSession session,@Param("name")String name,@Param("details") String details){


        if (name==""){
            model.addAttribute("authUser",accountService.findUser(session));
            model.addAttribute("group",adminService.findGroupByGid((Integer)session.getAttribute("gid")));
            model.addAttribute("fail",true);

            return "user/modify_group";
        }
        try{
            adminService.modifyGroup((Integer)session.getAttribute("gid"),name,details);
            return "redirect:/page/user/groups_by_me";
        }
        catch (Exception e){
            model.addAttribute("authUser",accountService.findUser(session));
            model.addAttribute("group",adminService.findGroupByGid((Integer)session.getAttribute("gid")));
            model.addAttribute("fail",true);

            return "user/modify_group";
        }

    }


    @SneakyThrows
    @RequestMapping("/add-Borrow")
    public String addBorrow(Model model,@Param("name")String name,@Param("rid")String rid,@Param("gid")String gid,@Param("startTime")String startTime,@Param("endTime")String endTime, HttpSession session){

        SimpleDateFormat ft= new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (name==""){
            model.addAttribute("authUser",accountService.findUser(session));
            model.addAttribute("groups",userService.getmyGroups(accountService.findUser(session).getAccountDetail().getUid()));
            model.addAttribute("meetingRooms",adminService.getAllMeetingRooms());
            model.addAttribute("fail",true);
            return "user/add-borrow";
        }

        try{
            Date start=ft.parse(startTime.replace("T"," "));
            Date end=ft.parse(endTime.replace("T"," "));

            if (adminService.addBorrow(Integer.parseInt(rid),Integer.parseInt(gid),name,start,end)){
//                verifyService.sendMail(name,Integer.parseInt(rid),Integer.parseInt(gid),start,end);
                verifyService.sendMail(name,Integer.parseInt(rid),Integer.parseInt(gid),startTime.replace("T"," "),endTime.replace("T"," "));
                return "redirect:/page/user/borrows";
            }else{
                model.addAttribute("authUser",accountService.findUser(session));
                model.addAttribute("groups",userService.getmyGroups(accountService.findUser(session).getAccountDetail().getUid()));
                model.addAttribute("meetingRooms",adminService.getAllMeetingRooms());
                model.addAttribute("fail",true);
                return "user/add-borrow";
            }


        }catch (Exception e){
            model.addAttribute("authUser",accountService.findUser(session));
            model.addAttribute("groups",userService.getmyGroups(accountService.findUser(session).getAccountDetail().getUid()));
            model.addAttribute("meetingRooms",adminService.getAllMeetingRooms());
            model.addAttribute("fail",true);
            return "user/add-borrow";
        }
    }

    @RequestMapping("/deleteRecord/{recid}")
    public String delete_record(@PathVariable int recid,HttpSession session){
        adminService.deleteRecordById(recid);
        return "redirect:/page/user/borrows";
    }

    @RequestMapping("/modify_Account_detail")
    public String modify_Account_detail(Model model,HttpSession session, @Param("name")String name, @Param("phone_number") String phone_number,@Param("sex")String sex){


        if (name==""||phone_number==""||(!sex.equals("女")&&!sex.equals("男"))){
            model.addAttribute("authUser",accountService.findUser(session));
//            model.addAttribute("groups",userService.getmyGroups(accountService.findUser(session).getAccountDetail().getUid()));
//            model.addAttribute("meetingRooms",adminService.getAllMeetingRooms());
            model.addAttribute("accountDetail",adminService.findUsersByUid(accountService.findUser(session).getAccountDetail().getUid()));
            model.addAttribute("fail",false);
            return "user/myinfo";
        }
        try{
            adminService.modifyAccountDetail(accountService.findUser(session).getAccountDetail().getUid(),name,phone_number,sex);
            return "redirect:/page/user/myinfo";
        }catch (Exception e){
            model.addAttribute("authUser",accountService.findUser(session));
//            model.addAttribute("groups",userService.getmyGroups(accountService.findUser(session).getAccountDetail().getUid()));
//            model.addAttribute("meetingRooms",adminService.getAllMeetingRooms());
            model.addAttribute("accountDetail",adminService.findUsersByUid(accountService.findUser(session).getAccountDetail().getUid()));
            model.addAttribute("fail",false);
            return "user/myinfo";
        }
    }
    @RequestMapping("/add-Account_detail")
    public String addAccountDetail(Model model,@Param("name")String name, @Param("phone_number")String phone_number, @Param("sex")String sex, HttpSession session){


        if (name==""||phone_number==""||(!sex.equals("女")&&!sex.equals("男"))){
            model.addAttribute("authUser",accountService.findUser(session));
            model.addAttribute("fail",true);
            return "user/addaccount_detail";
        }
        try{
            adminService.addAccountDetail(accountService.findUser(session).getID(),name,phone_number,sex);
            return "redirect:/login";
        }catch (Exception e){
            model.addAttribute("authUser",accountService.findUser(session));
            model.addAttribute("fail",true);
            return "user/addaccount_detail";
        }
    }
}
