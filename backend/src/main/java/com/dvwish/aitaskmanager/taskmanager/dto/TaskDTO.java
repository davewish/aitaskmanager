package com.dvwish.aitaskmanager.taskmanager.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private List<String> tags;
    private Integer priority;
    private String summary;
    private String attachmentUrl;
    private Long assigneeId;
    private List<CommentDTO> comments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
