package com.dvwish.aitaskmanager.taskmanager.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

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
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String cognitoId;

    @ElementCollection
    @CollectionTable(name="user_roles", joinColumns = @JoinColumn(name="user_id"))
    @Column(name="role")
    private List<String> roles= new ArrayList<>();

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
