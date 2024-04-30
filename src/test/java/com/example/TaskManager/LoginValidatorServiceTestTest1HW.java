package com.example.TaskManager;

import com.example.TaskManager.service.LoginValidatorServiceTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginValidatorServiceTestTest1HW {

    @Test
    public void testValidLoginAndPassword() {
        LoginValidatorServiceTest validator = new LoginValidatorServiceTest();
        assertTrue(validator.validate("username", "password"));
    }

    @Test
    public void testEmptyLogin() {
        LoginValidatorServiceTest validator = new LoginValidatorServiceTest();
        assertFalse(validator.validate("", "password"));
    }

    @Test
    public void testEmptyPassword() {
        LoginValidatorServiceTest validator = new LoginValidatorServiceTest();
        assertFalse(validator.validate("username", ""));
    }

    @Test
    public void testEmptyLoginAndPassword() {
        LoginValidatorServiceTest validator = new LoginValidatorServiceTest();
        assertFalse(validator.validate("", ""));
    }
}
