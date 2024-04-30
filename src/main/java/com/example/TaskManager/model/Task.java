package com.example.TaskManager.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_task")
    private Long id;

    @Column(name = "id_executor")
    private Long executorId;

    @Column(name = "id_guarantor")
    private Long guarantorId;

    @Column(name = "task_description")
    private String description;

    @Column(name = "created_at")
    private Date createdDate;

    @Column(name = "finish_date")
    private Date finishDate;

    @Column(name = "due_date")
    private Date deadline;

    @Column(name = "id_group")
    private Long idGroup;

    @Column(name = "status_id")
    private Long statusId;

}
