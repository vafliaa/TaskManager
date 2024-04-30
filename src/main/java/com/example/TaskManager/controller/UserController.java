package com.example.TaskManager.controller;

import com.example.TaskManager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
@Controller
public class UserController {

    @Autowired
    private UserService userService;


}
