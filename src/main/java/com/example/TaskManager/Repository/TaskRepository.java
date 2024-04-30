package com.example.TaskManager.Repository;

import com.example.TaskManager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t WHERE t.idGroup = :groupId")
    List<Task> findByGroupId(@Param("groupId") Long groupId);

    List<Task> findByExecutorId(Long executorId);
}
