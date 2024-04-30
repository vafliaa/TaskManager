package com.example.TaskManager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SqlUserServiceTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean validateLogin(String username) {
        return !username.isEmpty();
    }

    public boolean validatePassword(String password) {
        return !password.isEmpty();
    }

    // Метод для проверки существования пользователя в БД
    public boolean checkUserExists(String username) {
        String query = "SELECT COUNT(*) FROM app_user WHERE username = ?";
        int count = jdbcTemplate.queryForObject(query, Integer.class, username);
        return count > 0;
    }

    // Метод для добавления пользователя в БД
    public boolean addUserToDatabase(String username, String password) {
        String query = "INSERT INTO app_user (username, password) VALUES (?, ?)";
        int rowsAffected = jdbcTemplate.update(query, username, password);
        return rowsAffected > 0;
    }

    // Метод для проверки подключения к БД
    public boolean connectToDatabase() {
        try {
            jdbcTemplate.getDataSource().getConnection().close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Метод для выхода с учетной записи
    public boolean logout() {
        // Здесь может быть логика выхода с учетной записи, но для примера вернем всегда true
        return true;
    }
}
