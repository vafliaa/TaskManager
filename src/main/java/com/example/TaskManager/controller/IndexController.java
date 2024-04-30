package com.example.TaskManager.controller;

import com.example.TaskManager.Repository.GroupRepository;
import com.example.TaskManager.Repository.UserRepository;
import com.example.TaskManager.model.Group;
import com.example.TaskManager.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String showIndexPage(Model model, HttpSession session) {
        String loggedInUsername = (String) session.getAttribute("loggedInUser");
        if (loggedInUsername != null) {
            System.out.println("Session ID: " + session.getId()); // Выведем ID сессии
            User loggedInUser = userRepository.findByUsername(loggedInUsername);
            if (loggedInUser != null) {
                model.addAttribute("loggedInUsername", loggedInUsername);
                /*// Проверяем наличие групп у пользователя
                List<Group> userGroups = loggedInUser.getGroups();
                if (userGroups != null && !userGroups.isEmpty()) {
                    model.addAttribute("groupList", userGroups);
                } else {
                    model.addAttribute("groupList", null); // Если групп нет, устанавливаем список групп как null
                }*/
            }
        }
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Удаление сессии
        return "redirect:/";
    }

    /*@PostMapping("/createGroup")
    public String createGroup(@RequestParam(value = "groupName", required = false, defaultValue = "NoNameGroup") String groupName, HttpSession session) {
        String loggedInUsername = (String) session.getAttribute("loggedInUser");
        if (loggedInUsername != null) {
            User owner = userRepository.findByUsername(loggedInUsername);
            if (owner != null) {
                String groupCode = UUID.randomUUID().toString(); // Генерируем уникальный код для группы
                Group group = new Group(groupName, owner, groupCode);

                // Добавляем созданную группу в список групп пользователя
                owner.getGroups().add(group);

                // Добавляем владельца в участники группы
                group.getMembers().add(owner);

                // Сохраняем созданную группу
                groupRepository.save(group);

                // Сохраняем обновленного пользователя
                userRepository.save(owner);
            }
        }
        return "redirect:/";
    }

    @PostMapping("/joinGroup")
    public String joinGroup(@RequestParam("groupCode") String groupCode, HttpSession session) {
        String loggedInUsername = (String) session.getAttribute("loggedInUser");
        if (loggedInUsername != null) {
            User user = userRepository.findByUsername(loggedInUsername);
            Group group = groupRepository.findByCode(groupCode);
            if (group != null && user != null) {
                // Добавляем пользователя в группу
                group.getMembers().add(user);
                // Сначала сохраняем пользователя
                userRepository.save(user);
                // Затем сохраняем группу
                groupRepository.save(group);
            }
        }
        return "redirect:/";
    }*/

}
