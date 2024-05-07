package com.example.TaskManager.service;

import com.example.TaskManager.Repository.UserRepository;
import com.example.TaskManager.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void save(User user) {
        userRepository.save(user);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public String getUsernameById(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return (user != null) ? user.getUsername() : null;
    }

    public Long findUserIdByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user.getId();
        }
        return null;
    }

    public boolean checkPassword(String username, String password) {
        // Проверяем существование пользователя по имени
        if (existsByUsername(username)) {
            // Если пользователь существует, получаем его из базы данных
            User existingUser = userRepository.findByUsername(username);
            // Сверяем введенный пароль с хранимым в базе данных
            return existingUser.getPassword().equals(password);
        } else {
            // Возвращаем false, если пользователь с таким именем не найден
            return false;
        }
    }
}
