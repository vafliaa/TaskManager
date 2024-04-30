package com.example.TaskManager.controller;

import com.example.TaskManager.Repository.GroupRepository;
import com.example.TaskManager.model.User;
import com.example.TaskManager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import javax.servlet.http.HttpSession;

@Controller
public class LoginRegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupRepository groupRepository;

    @GetMapping("/login")
    public String showLoginPage(Model model, HttpSession session) {
        model.addAttribute("user", new User());
        model.addAttribute("loginPage", true);
        model.addAttribute("registerPage", false);
        model.addAttribute("pageTitle", "Авторизация");

        return "LoginRegisterForm";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") User user, Model model, HttpSession session) {
        if (user.getUsername().isEmpty() || user.getPassword().isEmpty()) {
            if (user.getUsername().isEmpty() && user.getPassword().isEmpty()) {
                model.addAttribute("errorPass", "Введите имя пользователя и пароль.");
            } else if (user.getUsername().isEmpty()) {
                model.addAttribute("errorName", "Введите имя пользователя.");
            } else {
                model.addAttribute("errorPass", "Введите пароль.");
            }
        } else if(userService.checkPassword(user.getUsername(), user.getPassword())) {
            System.out.println(userService.checkPassword(user.getUsername(), user.getPassword()));
            session.setAttribute("loggedInUser", user.getUsername()); // Сохраняем имя пользователя в сессии
            System.out.println(user.getUsername());
            System.out.println("User logged in: " + user.getUsername()); // Добавим вывод на консоль
            System.out.println("Session ID: " + session.getId()); // Выведем ID сессии
            return "redirect:/"; // Перенаправляем на главную страницу
        } else model.addAttribute("errorPass", "Неверное имя пользователя или пароль");

        model.addAttribute("loginPage", true);
        model.addAttribute("registerPage", false);
        model.addAttribute("pageTitle", "Авторизация");
        return "LoginRegisterForm";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("loginPage", false);
        model.addAttribute("registerPage", true);
        model.addAttribute("pageTitle", "Регистрация");
        return "LoginRegisterForm";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user, Model model) {
        if (userService.existsByUsername(user.getUsername())) {
            model.addAttribute("errorName", "Пользователь уже существует");
        }else if (user.getUsername().isEmpty() || user.getPassword().isEmpty()) {
            if (user.getUsername().isEmpty() && user.getPassword().isEmpty()) {
                model.addAttribute("errorPass", "Введите имя пользователя и пароль.");
            } else if (user.getUsername().isEmpty()) {
                model.addAttribute("errorName", "Введите имя пользователя.");
            } else {
                model.addAttribute("errorPass", "Введите пароль.");
            }
        } else {
            // Регистрация нового пользователя
            userService.save(user);
            return "redirect:/login?registered"; // Перенаправление на страницу входа с сообщением о успешной регистрации
        }
        model.addAttribute("loginPage", false);
        model.addAttribute("registerPage", true);
        model.addAttribute("pageTitle", "Регистрация");
        return "LoginRegisterForm";
    }

}

