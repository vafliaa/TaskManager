package com.example.TaskManager;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class JdbcIntegrationTest3HWInter {

    private static JdbcTemplate jdbcTemplate;

    @BeforeClass
    public static void setUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/TaskManagement");
        dataSource.setUsername("postgres");
        dataSource.setPassword("1793");
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @After
    public void tearDown() {
        // Удаляем все записи из таблицы app_user после каждого теста
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "app_user");
    }

    @Test
    public void testInsertUser() {
        // Вставляем пользователя в таблицу
        String insertUserQuery = "INSERT INTO app_user (username, password) VALUES ('john_doe', 'password123')";
        int rowsAffected = jdbcTemplate.update(insertUserQuery);

        // Проверяем, что одна строка была вставлена
        assertThat(rowsAffected).isEqualTo(1);
    }

    @Test
    public void testCountUsers() {
        // Получаем количество пользователей
        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM app_user", Integer.class);

        // Проверяем, что количество пользователей равно ожидаемому значению (0, так как таблица должна быть пустой)
        assertThat(count).isEqualTo(0);
    }

    @Test
    public void testDeleteUser() {
        // Вставляем пользователя
        String insertUserQuery = "INSERT INTO app_user (username, password) VALUES ('jane_smith', 'password456')";
        jdbcTemplate.update(insertUserQuery);

        // Удаляем пользователя
        String deleteUserQuery = "DELETE FROM app_user WHERE username = 'jane_smith'";
        int rowsAffected = jdbcTemplate.update(deleteUserQuery);

        // Проверяем, что одна строка была удалена
        assertThat(rowsAffected).isEqualTo(1);
    }
}

