package com.example.TaskManager.service;

import com.example.TaskManager.Repository.TaskRepository;
import com.example.TaskManager.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getTasksByGroupId(Long groupId) {
        return taskRepository.findByGroupId(groupId);
    }

    public void createTask(Task task) {
        // Сохраняем задачу
        taskRepository.save(task);
    }

    // Метод для получения задачи по ее идентификатору
    public Task getTaskById(Long taskId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        return taskOptional.orElse(null);
    }

    public void updateTask(Long taskId, Task updatedTask) {
        // Проверяем, существует ли задача с указанным ID
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isPresent()) {
            // Получаем существующую задачу
            Task existingTask = optionalTask.get();
            // Обновляем существующую задачу
            existingTask.setDescription(updatedTask.getDescription());
            existingTask.setExecutorId(updatedTask.getExecutorId());
            existingTask.setDeadline(updatedTask.getDeadline());
            // Продолжайте для других полей, которые нужно обновить
            taskRepository.save(existingTask);
        } else {
            // Обработка случая, если задача не найдена
            throw new IllegalArgumentException("Task with ID " + taskId + " not found");
        }
    }

    // Метод для обновления статуса задачи
    public Task updateStatusTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTaskById(Long taskId) {
        // Проверяем, существует ли задача с указанным идентификатором
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            // Если задача найдена, удаляем ее
            taskRepository.deleteById(taskId);
        } else {
            // Если задача не найдена, вы можете выбрать, что делать дальше,
            // например, бросить исключение или просто проигнорировать это
            // в зависимости от ваших требований
            throw new IllegalArgumentException("Task with ID " + taskId + " not found");
        }
    }

}
