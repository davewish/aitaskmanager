package com.dvwish.aitaskmanager.taskmanager.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false ,unique = true)
    private String name;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String cognitoId;

    @Column
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "assignee" , cascade = CascadeType.ALL ,orphanRemoval = true)
    private List<Task> assignedTasks= new ArrayList<>();

    @OneToMany(mappedBy = "assignee" ,cascade=CascadeType.ALL,orphanRemoval = true)
    private List<Comment> comments= new ArrayList<>();

    @PrePersist
    protected  void onCreate(){
        createdAt=LocalDateTime.now();
    }

}
