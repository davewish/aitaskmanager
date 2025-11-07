package com.dvwish.aitaskmanager.taskmanager.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

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
