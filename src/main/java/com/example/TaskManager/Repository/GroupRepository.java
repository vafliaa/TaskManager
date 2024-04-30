package com.example.TaskManager.Repository;

import com.example.TaskManager.model.Group;
import com.example.TaskManager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByGroupCode(String code);

    List<Group> findAllByMembersId(Long userId);
    @Query("SELECT g FROM Group g WHERE g.groupCode = :code")
    Group findByCode(@Param("code") String code);

    @Query("SELECT g.groupCode FROM Group g WHERE g.id = :groupId")
    String findGroupCodeById(@Param("groupId") Long groupId);
}
