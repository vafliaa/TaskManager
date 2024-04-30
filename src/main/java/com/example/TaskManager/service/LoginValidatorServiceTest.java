package com.example.TaskManager.service;

import org.springframework.stereotype.Service;

@Service
public class LoginValidatorServiceTest {

    public boolean validate(String username, String password) {
        return isValidUsername(username) && isValidPassword(password);
    }

    private boolean isValidUsername(String username) {
        return !username.isEmpty();
    }

    private boolean isValidPassword(String password) {
        return !password.isEmpty();
    }
}

