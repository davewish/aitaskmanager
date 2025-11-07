package com.dvwish.aitaskmanager.taskmanager.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;
@Getter
@Setter
public class TaskUpdateDTO {
  private Integer  priority;
  private LocalDateTime dueDate;
  private String title;
  private String description;
  private List<String> tags;
  private String summary;
  private String attachmentUrl;
  private Long assigneeId;
  private List<CommentDTO> comments;

}
