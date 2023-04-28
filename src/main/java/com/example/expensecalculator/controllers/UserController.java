package com.example.expensecalculator.controllers;

import com.example.expensecalculator.entities.User;
import com.example.expensecalculator.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
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
            return "login";
        }
    }

    @GetMapping("/register")
    public String registerUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "registeruser";
    }

    @PostMapping("/register")
    public String registeredUser(@ModelAttribute("user") User user) {
        userService.registerUser(user);
        return "redirect:/login";
    }

    @GetMapping("/edit")
    public String editUser() {
        return "mailcheck";
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
        return "edituser";
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
        model.addAttribute("user", user);
        return "menu";
    }


}
