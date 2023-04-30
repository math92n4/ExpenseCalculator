package com.example.expensecalculator.controllers;

import com.example.expensecalculator.dtos.GroupDTO;
import com.example.expensecalculator.dtos.TransferDTO;
import com.example.expensecalculator.dtos.UserExpenseDTO;
import com.example.expensecalculator.entities.Expense;
import com.example.expensecalculator.entities.Group;
import com.example.expensecalculator.entities.User;
import com.example.expensecalculator.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("userName") String userName,
                           @RequestParam("password") String password,
                           HttpSession httpSession,
                           Model model) {

        User user = userService.getUserByCredentials(userName, password);
        if (user != null) {
            httpSession.setAttribute("user", user);
            return "redirect:/menu";
        } else {
            model.addAttribute("wrongInfo", true);
            return "login/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String registerUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "login/registeruser";
    }

    @PostMapping("/register")
    public String registeredUser(@ModelAttribute("user") User user) {
        userService.registerUser(user);
        return "redirect:/login";
    }

    @GetMapping("/edit")
    public String editUser() {
        return "login/mailcheck";
    }

    @PostMapping("/edit")
    public String editUser(@RequestParam String email,
                           HttpSession httpSession) {
        User user = userService.getUserByEmail(email);
        if (user != null) {
            httpSession.setAttribute("user", user);
            return "redirect:/edituser";
        } else
            return "redirect:/edit";
    }

    @GetMapping("/edituser")
    public String editedUser(HttpSession httpSession,
                             Model model) {
        User user = (User) httpSession.getAttribute("user");
        model.addAttribute("user", user);
        return "login/edituser";
    }

    @PostMapping("/edituser")
    public String editedUser(@ModelAttribute User editedUser,
                             HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        userService.editUser(user.getUserID(), editedUser);
        httpSession.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/menu")
    public String menu(HttpSession httpSession,
                       Model model) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        } else {
            model.addAttribute("user", user);
            return "menu/menu";
        }
    }

    @GetMapping("/create-group")
    public String createGroup(Model model) {
        GroupDTO groupDTO = new GroupDTO();
        model.addAttribute("group", groupDTO);
        return "menu/creategroup";
    }

    @PostMapping("/create-group")
    public String createGroup(@ModelAttribute("group") GroupDTO group,
                              HttpSession httpSession) {

        // input is taken separated by commas. therefore this string is split and a list is made
        // this should maybe be handled in service layer

        User user = (User) httpSession.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        String replaceSpace = group.getMembers().replace(" ", "");
        List<String> members = new ArrayList<>(Arrays.asList(replaceSpace.split(",")));
        members.add(user.getUserName());

        if (userService.doesExist(members)) {
            Group newGroup = new Group();
            newGroup.setName(group.getName());
            newGroup.setDesc(group.getDesc());
            newGroup.setMembers(members);
            userService.createGroup(newGroup);
        }

        return "redirect:/menu";
    }


    @GetMapping("/your-groups")
    public String getGroups(HttpSession httpSession,
                            Model model) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        List<Group> groups = userService.getGroupsByUserID(user.getUserID());
        model.addAttribute("groups", groups);

        return "menu/yourgroups";
    }

    @GetMapping("/group/{groupid}")
    public String getGroup(Model model,
                           HttpSession httpSession,
                           @PathVariable int groupid) {

        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Group group = userService.getGroupByGroupID(groupid);
        List<UserExpenseDTO> users = userService.getUsersAndExpensesByGroupID(groupid);
        Expense expense = new Expense();
        List<TransferDTO> transfers = userService.calculateExpenses(groupid);
        model.addAttribute("group",group);
        model.addAttribute("users", users);
        model.addAttribute("expense", expense);
        model.addAttribute("transfers", transfers);

        return "menu/groupmenu";
    }


    @PostMapping("/group/{groupid}")
    public String postExpense(@RequestParam double expense,
                              @PathVariable int groupid,
                              HttpSession httpSession) {

        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        userService.createExpense(expense, user.getUserID(), groupid);

        return "redirect:/group/{groupid}";
    }


}
