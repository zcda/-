package com.example.test.controller.api;

import com.example.test.Service.AccountService;
import com.example.test.Service.AdminService;
import com.example.test.repo.AccountRepository;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@ResponseBody
@RequestMapping("/api/user")

public class UserApiController {
    @Resource
    AccountRepository repository;

    @Resource
    AdminService adminService;

    //@Resource
    //UserService userService;

    @Resource
    AccountService accountService;

    @RequestMapping("/delete_group/{gid}")
    public String delete_group(@PathVariable int gid, HttpSession session){
        //userService.deleteGroupbyme(gid);
        return "redirect:/page/user/groups_by_me";
    }

    @RequestMapping("/users_group_delete/{uid}")
    public String users_group_delete(@PathVariable int uid,HttpSession session){
        adminService.deleteUserInGroup((Integer) session.getAttribute("gid"),uid);
        return "redirect:/page/user/group_user_manage/"+ (Integer) session.getAttribute("gid");
    }

    @RequestMapping("/users_group_add/{uid}")
    public String users_group_add(@PathVariable int uid,HttpSession session){
        adminService.addUserInGroup((Integer) session.getAttribute("gid"),uid);
        return "redirect:/page/user/group_user_manage/"+ (Integer) session.getAttribute("gid");
    }

    @RequestMapping("/add-Group")
    public String addGroup(Model model, @Param("name")String name, @Param("details")String details, HttpSession session){
        if (name==""){
            model.addAttribute("authUser",accountService.findUser(session));
            model.addAttribute("fail",true);
            return "user/addgroup";
        }
        try{
            //userService.addGroup(name,details);
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

}
