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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column
    private LocalDateTime dueDate;

    @ElementCollection
    @CollectionTable( name="task_tags" , joinColumns = @JoinColumn(name = "task_id"))
    @Column(name="tag")
    private List<String> tags= new ArrayList<>();

    @Column
    private Integer  priority;
    @Column
    private String summary;
    @Column
    private String attachmentUrl;

    @ManyToOne
    @JoinColumn(name="assignee_id")
    private User assignee;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Comment> comments= new ArrayList<>();
    @Column
    private LocalDateTime createdAt;
    @Column
    private LocalDateTime updatedAt;
    @PrePersist
    protected  void onCreate(){
         createdAt=LocalDateTime.now();
         updatedAt=LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        updatedAt=LocalDateTime.now();
    }

}
