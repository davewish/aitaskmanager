package com.dvwish.aitaskmanager.taskmanager.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT"  ,nullable = false)
    private String content;
    @ManyToOne
    @JoinColumn( name="task_id",nullable = false)
    private Task task;
    @ManyToOne
    @JoinColumn(name ="author_id", nullable = false)
    private User author;

    @Column
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate(){
        createdAt=LocalDateTime.now();
    }

}
