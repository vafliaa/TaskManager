package com.example.TaskManager.service;

import com.example.TaskManager.Repository.GroupRepository;
import com.example.TaskManager.model.Group;
import com.example.TaskManager.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    public List<User> getGroupMembers(Long groupId) {
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        if (optionalGroup.isPresent()) {
            Group group = optionalGroup.get();
            return group.getMembers();
        } else {
            return Collections.emptyList(); // Вернуть пустой список
        }
    }

    // Метод для проверки является ли пользователь владельцем группы
    public boolean isGroupOwner(String loggedInUsername, Long groupId) {
        Group group = groupRepository.findById(groupId).orElse(null);
        return group != null && group.getOwner().getUsername().equals(loggedInUsername);
    }

    // Метод для получения имени группы по ее ID
    public String getGroupNameById(Long groupId) {
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        return groupOptional.map(Group::getName).orElse(null);
    }

    public String getGroupCodeById(Long groupId) {
        return groupRepository.findGroupCodeById(groupId);
    }
}
