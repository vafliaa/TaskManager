package com.example.TaskManager;

import com.example.TaskManager.service.SqlUserServiceTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import static org.junit.Assert.*;

public class UserServiceTest2HW {

    private static SqlUserServiceTest sqlUserServiceTest;
    private static JdbcTemplate jdbcTemplate;

    @BeforeClass
    public static void setUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/TaskManagement");
        dataSource.setUsername("postgres");
        dataSource.setPassword("1793");
        jdbcTemplate = new JdbcTemplate(dataSource);
        sqlUserServiceTest = new SqlUserServiceTest();
        sqlUserServiceTest.setJdbcTemplate(jdbcTemplate);
    }

    @AfterClass
    public static void tearDown() {
        sqlUserServiceTest = null;
    }

    @Test
    public void testValidateLogin_Positive() {
        assertTrue(sqlUserServiceTest.validateLogin("username"));
    }

    @Test
    public void testValidatePassword_Positive() {
        assertTrue(sqlUserServiceTest.validatePassword("password"));
    }

    @Test
    public void testValidateLogin_Negative() {
        assertFalse(sqlUserServiceTest.validateLogin(""));
    }

    @Test
    public void testValidatePassword_Negative() {
        assertFalse(sqlUserServiceTest.validatePassword(""));
    }

    @Test
    public void testCheckUserExists_Positive() {
        assertTrue(sqlUserServiceTest.checkUserExists("Vaflia"));
    }

    @Test
    public void testCheckUserExists_Negative() {
        assertFalse(sqlUserServiceTest.checkUserExists("nonExistingUser"));
    }

    @Test
    public void testConnectToDatabase_Positive() {
        assertTrue(sqlUserServiceTest.connectToDatabase());
    }

    @Test
    public void testAddUserToDatabase_Positive() {
        assertTrue(sqlUserServiceTest.addUserToDatabase("newUser", "password"));
    }

    @Test
    public void testLogout_Positive() {
        assertTrue(sqlUserServiceTest.logout());
    }

    @Test
    public void testLogout_Negative() {
        assertTrue(sqlUserServiceTest.logout());
    }
}

