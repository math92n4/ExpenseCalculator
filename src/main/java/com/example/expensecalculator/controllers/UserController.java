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
import java.util.List;

@Controller
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String login() {
        return "login/login";
    }

    @PostMapping("/")
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
        return "redirect:/";
    }

    @GetMapping("/register")
    public String registerUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "login/registeruser";
    }

    @PostMapping("/register")
    public String registeredUser(@ModelAttribute("user") User user,
                                 Model model) {
        if (userService.doesUsernameExist(user.getUserName())) {
            model.addAttribute("notUnique", true);
            return "login/registeruser";
        } else {
            userService.registerUser(user);
            return "redirect:/";
        }
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
        return "redirect:/";
    }

    @GetMapping("/menu")
    public String menu(HttpSession httpSession,
                       Model model) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        } else {
            model.addAttribute("user", user);
            return "menu/menu";
        }
    }

    @GetMapping("/create-group")
    public String createGroup(Model model) {
        GroupDTO groupDTO = new GroupDTO();
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("group", groupDTO);
        return "menu/creategroup";
    }

    @PostMapping("/create-group")
    public String createGroup(@ModelAttribute("group") GroupDTO group,
                              HttpSession httpSession,
                              @RequestParam List<Integer> listOfUsers) {

        User user = (User) httpSession.getAttribute("user");

        if (user == null) {
            return "redirect:/";
        }

        group.setListOfUsers(listOfUsers);
        userService.createGroup(group);

        return "redirect:/menu";
    }


    @GetMapping("/your-groups")
    public String getGroups(HttpSession httpSession,
                            Model model) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            return "redirect:/";
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
            return "redirect:/";
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
            return "redirect:/";
        }

        userService.createExpense(expense, user.getUserID(), groupid);

        return "redirect:/group/{groupid}";
    }

    @PostMapping("/group/{groupid}/delete")
    public String deleteGroup(@PathVariable int groupid,
                              HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        userService.deleteGroup(groupid);

        return "redirect:/menu";
    }


}
