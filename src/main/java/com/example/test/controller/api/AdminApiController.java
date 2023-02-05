package com.example.test.controller.api;

import com.example.test.Service.AccountService;
import com.example.test.Service.AdminService;
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
@RequestMapping("/api/admin")
public class AdminApiController {

    @Resource
    AccountRepository repository;

    @Resource
    AdminService service;

    @Resource
    AccountService accountService;

    @Resource
    VerifyService verifyService;


    @RequestMapping("/deleteMeetingRoom/{rid}")
    public String deleteMeetingRoom(@PathVariable int rid,Model model,HttpSession session){

        service.deleteMeetingRoomByRid(rid);
        return "redirect:/page/admin/index" ;

    }

    @RequestMapping("/deleteDevice/{did}")
    public String deleteDevice(@PathVariable int did,HttpSession session){
        service.deleteDevice(did);
        return "redirect:/page/admin/showDevice/"+(Integer) session.getAttribute("rid");
    }

    @RequestMapping("/deleteAccount/{id}")
    public String deleteAccount(@PathVariable int id,HttpSession session){
        service.deleteAccount(id);
        return "redirect:/page/admin/users";
    }

    @RequestMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable int id,HttpSession session){
        service.deleteUser(id,(Integer) session.getAttribute("id"));
        return "redirect:/page/admin/users";
    }

    @RequestMapping("/add-MeetingRoom")
    public String addMeetingRoom(@Param("name")String name,@Param(value = "address")String address,@Param(value = "max_capacity")String max_capacity,HttpSession session, Model model){
        if(name==""||address==""||max_capacity==""){
            model.addAttribute("authUser",accountService.findUser(session));
            model.addAttribute("fail",true);
            return "admin/add-meetingRoom" ;
        }
        System.out.println(name+address+max_capacity);
        try{
        service.addMeetingRoom(name, address, Integer.parseInt(max_capacity));
            return "redirect:/page/admin/meetingrooms" ;
        }catch (Exception e){
            model.addAttribute("authUser",accountService.findUser(session));
            model.addAttribute("fail",true);
            return "admin/add-meetingRoom" ;
        }
    }

    @RequestMapping("/add-Device")
    public String addDevice(@Param("name")String name, @Param("detail_describe")String detail_describe, @Param("count")String count, HttpSession session,Model model){
        if(name==""||detail_describe==""||count==""){
            model.addAttribute("authUser",accountService.findUser(session));
            model.addAttribute("fail",true);
            return "admin/add-device";
        }

        try{
            service.addDevice((Integer) session.getAttribute("rid"),name,detail_describe,Integer.parseInt(count));
            return "redirect:/page/admin/showDevice/"+(Integer) session.getAttribute("rid");
        }catch (Exception e){
            model.addAttribute("authUser",accountService.findUser(session));
            model.addAttribute("fail",true);
            return "admin/add-device";
        }
    }

    @RequestMapping("/add-Account")
    public String addAccount(@Param("name")String name, @Param("password")String password, @Param("email")String email, HttpSession session,Model model){


        if(name==""||password==""||email==""||verifyService.checkEmail(email)){
            model.addAttribute("authUser",accountService.findUser(session));
            model.addAttribute("fail",true);
            return "admin/add-account";
        }

        try{
            accountService.createAccount(name,password,email);
            return "redirect:/page/admin/users";
        }catch (Exception e){
            model.addAttribute("authUser",accountService.findUser(session));
            model.addAttribute("fail",true);
            return "admin/add-account";
        }
    }

    @RequestMapping("/add-Account_detail")
    public String addAccountDetail(Model model,@Param("name")String name, @Param("phone_number")String phone_number, @Param("sex")String sex, HttpSession session){


        if (name==""||phone_number==""||(!sex.equals("女")&&!sex.equals("男"))){
            model.addAttribute("authUser",accountService.findUser(session));
            model.addAttribute("fail",true);

            return "admin/add-account_detail";
        }
        try{
            service.addAccountDetail((Integer) session.getAttribute("id"),name,phone_number,sex);
            return "redirect:/page/admin/users";
        }catch (Exception e){
            model.addAttribute("authUser",accountService.findUser(session));
            model.addAttribute("fail",true);
            return "admin/add-account_detail";
        }
    }
    @RequestMapping("/add-Group")
    public String addGroup(Model model,@Param("name")String name,@Param("details")String details, HttpSession session){
        if (name==""){
            model.addAttribute("authUser",accountService.findUser(session));
            model.addAttribute("fail",true);
            return "admin/add-group";
        }
        try{
            service.addGroup(name,details);
            return "redirect:/page/admin/groups";
        }catch (Exception e){
            model.addAttribute("authUser",accountService.findUser(session));
            model.addAttribute("fail",true);
            return "admin/add-group";
        }
    }

    @SneakyThrows
    @RequestMapping("/add-Borrow")
    public String addBorrow(Model model,@Param("name")String name,@Param("rid")String rid,@Param("gid")String gid,@Param("startTime")String startTime,@Param("endTime")String endTime, HttpSession session){

        SimpleDateFormat ft= new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (name==""){
            model.addAttribute("authUser",accountService.findUser(session));
            model.addAttribute("groups",service.getAllGroups());
            model.addAttribute("meetingRooms",service.getAllMeetingRooms());
            model.addAttribute("fail",true);
            return "admin/add-borrow";
        }

        try{
            Date start=ft.parse(startTime.replace("T"," "));
            Date end=ft.parse(endTime.replace("T"," "));

            if (service.addBorrow(Integer.parseInt(rid),Integer.parseInt(gid),name,start,end)){
                verifyService.sendMail(name,Integer.parseInt(rid),Integer.parseInt(gid),startTime,endTime);
                return "redirect:/page/admin/borrows";
            }else{
                model.addAttribute("authUser",accountService.findUser(session));
                model.addAttribute("groups",service.getAllGroups());
                model.addAttribute("meetingRooms",service.getAllMeetingRooms());
                model.addAttribute("fail",true);
                return "admin/add-borrow";
            }


        }catch (Exception e){
            model.addAttribute("authUser",accountService.findUser(session));
            model.addAttribute("groups",service.getAllGroups());
            model.addAttribute("meetingRooms",service.getAllMeetingRooms());
            model.addAttribute("fail",true);
            return "admin/add-borrow";
        }
    }


    @RequestMapping("/users_group_delete/{uid}")
    public String users_group_delete(@PathVariable int uid,HttpSession session){
        service.deleteUserInGroup((Integer) session.getAttribute("gid"),uid);
        return "redirect:/page/admin/group_user_manage/"+ (Integer) session.getAttribute("gid");
    }
    @RequestMapping("/users_group_add/{uid}")
    public String users_group_add(@PathVariable int uid,HttpSession session){
        service.addUserInGroup((Integer) session.getAttribute("gid"),uid);
        return "redirect:/page/admin/group_user_manage/"+ (Integer) session.getAttribute("gid");
    }

    @RequestMapping("/delete_group/{gid}")
    public String delete_group(@PathVariable int gid,HttpSession session){
        service.deleteGroup(gid);
        return "redirect:/page/admin/groups";
    }

    @RequestMapping("/deleteRecord/{recid}")
    public String delete_record(@PathVariable int recid,HttpSession session){
        service.deleteRecordById(recid);
        return "redirect:/page/admin/borrows";
    }




    @RequestMapping("/modify_group")
    public String modify_group(Model model,HttpSession session,@Param("name")String name,@Param("details") String details){


        if (name==""){
            model.addAttribute("authUser",accountService.findUser(session));
            model.addAttribute("group",service.findGroupByGid((Integer)session.getAttribute("gid")));
            model.addAttribute("fail",true);

            return "admin/modify_group";
        }
        try{
            service.modifyGroup((Integer)session.getAttribute("gid"),name,details);
            return "redirect:/page/admin/groups";
        }
        catch (Exception e){
            model.addAttribute("authUser",accountService.findUser(session));
            model.addAttribute("group",service.findGroupByGid((Integer)session.getAttribute("gid")));
            model.addAttribute("fail",true);

            return "admin/modify_group";
        }

    }
    @RequestMapping("/modify_MeetingRoom")
    public String modify_MeetingRoom(HttpSession session, @Param("name")String name, @Param("address") String address, @Param("max_capacity")String max_capacity,Model model){
        if (name==""||address==""||max_capacity==""){

            model.addAttribute("authUser",accountService.findUser(session));
            model.addAttribute("room",service.findRoomByRid((Integer)session.getAttribute("rid")));
            model.addAttribute("fail",true);
            return "admin/modify_meetingRoom";
        }
        try{service.modifyMeetingRoom((Integer)session.getAttribute("rid"),name,address,Integer.parseInt(max_capacity));
            return "redirect:/page/admin/meetingrooms";}
        catch (Exception e){
            model.addAttribute("authUser",accountService.findUser(session));
            model.addAttribute("room",service.findRoomByRid((Integer)session.getAttribute("rid")));
            model.addAttribute("fail",true);
            return "admin/modify_meetingRoom";
        }

    }

    @RequestMapping("/modify_Device")
    public String modify_Device(HttpSession session,Model model, @Param("name")String name,@Param("detail_describe")String detail_describe, @Param("count")String count){

        if (name==""||detail_describe==""||count==""){
            model.addAttribute("authUser",accountService.findUser(session));
            model.addAttribute("device",service.findDeviceByDid((Integer)session.getAttribute("did")));
            model.addAttribute("fail",true);
            return "admin/modify_device";
        }
        try{
            service.modifyDevice((Integer)session.getAttribute("did"),name,detail_describe,Integer.parseInt(count));
            return "redirect:/page/admin/showDevice/"+(Integer) session.getAttribute("rid");
        }catch (Exception e){
            model.addAttribute("authUser",accountService.findUser(session));
            model.addAttribute("device",service.findDeviceByDid((Integer)session.getAttribute("did")));
            model.addAttribute("fail",true);
            return "admin/modify_device";
        }
    }

    @RequestMapping("/modify_Account")
    public String modify_Account(HttpSession session,Model model, @Param("name")String name, @Param("password") String password,@Param("email")String email){


        if (name==""||password==""||email==""||verifyService.checkEmail(email)){
            model.addAttribute("authUser",accountService.findUser(session));
            model.addAttribute("account",repository.findById((Integer)session.getAttribute("id")).get());
            model.addAttribute("fail",true);
            return "admin/modify_account";
        }
        try{
            service.modifyAccount((Integer)session.getAttribute("id"),name,password,email);
            return "redirect:/page/admin/users";
        }catch (Exception e){
            model.addAttribute("authUser",accountService.findUser(session));
            model.addAttribute("account",repository.findById((Integer)session.getAttribute("id")).get());
            model.addAttribute("fail",true);
            return "admin/modify_account";
        }
    }

    @RequestMapping("/modify_Account_detail")
    public String modify_Account_detail(Model model,HttpSession session, @Param("name")String name, @Param("phone_number") String phone_number,@Param("sex")String sex){


        if (name==""||phone_number==""||(!sex.equals("女")&&!sex.equals("男"))){
            model.addAttribute("authUser",accountService.findUser(session));
            model.addAttribute("accountDetail",service.findUsersByUid((Integer)session.getAttribute("uid")));
            model.addAttribute("fail",true);
            return "admin/modify_account_detail";
        }
        try{
            service.modifyAccountDetail((Integer)session.getAttribute("uid"),name,phone_number,sex);
            return "redirect:/page/admin/users_detail/"+ (Integer) session.getAttribute("id");
        }catch (Exception e){
            model.addAttribute("authUser",accountService.findUser(session));
            model.addAttribute("accountDetail",service.findUsersByUid((Integer)session.getAttribute("uid")));
            model.addAttribute("fail",true);
            return "admin/modify_account_detail";
        }
    }
}
