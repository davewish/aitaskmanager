package com.dvwish.aitaskmanager.taskmanager.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class CommentDTO {
    private  Long id ;
    private String content;
    private Long taskId;
    private Long authorId;
    private String authorUsername;
    private LocalDateTime createdAt;
}
