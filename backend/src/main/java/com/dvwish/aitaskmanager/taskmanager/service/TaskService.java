package com.dvwish.aitaskmanager.taskmanager.service;

import com.dvwish.aitaskmanager.taskmanager.dto.CommentDTO;
import com.dvwish.aitaskmanager.taskmanager.dto.TaskCreateDTO;
import com.dvwish.aitaskmanager.taskmanager.dto.TaskDTO;
import com.dvwish.aitaskmanager.taskmanager.dto.TaskUpdateDTO;
import com.dvwish.aitaskmanager.taskmanager.model.Task;
import com.dvwish.aitaskmanager.taskmanager.model.User;
import com.dvwish.aitaskmanager.taskmanager.repository.TaskRepository;
import com.dvwish.aitaskmanager.taskmanager.repository.UserRepository;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class TaskService {

  private final TaskRepository taskRepository;
  private final UserRepository userRepository;
  private final S3Client s3Client;

  @Value("${openai.api.key}")
  private String openAiApiKey;

  @Value("${aws.s3.bucket}")
  private String s3Bucket;


  public TaskService(TaskRepository taskRepository, UserRepository userRepository,
      S3Client s3Client) {
    this.taskRepository = taskRepository;
    this.userRepository = userRepository;
    this.s3Client = s3Client;
  }

  public TaskDTO createTask(TaskCreateDTO request) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    User currentUser = userRepository.findByCognitoId(auth.getName())
        .orElseThrow(() -> new RuntimeException("User not found"));

    Task newTask = new Task();
    OpenAiService openAiService = new OpenAiService(openAiApiKey, Duration.ofSeconds(30));
    CompletionRequest completionRequest = CompletionRequest.builder()
        .prompt("Parse this task description into structured fields: " + request.getInput()
            + ". Fields: title, description, dueDate, tags (comma-separated), priority (1-5).")
        .model("gpt-3.5-turbo-instruct")
        .maxTokens(150)
        .build();

    newTask.setTitle("Parsed Title"); // Replace with actual parsing logic
    newTask.setDescription(request.getInput());
    newTask.setPriority(3); // AI-suggested
    newTask.setSummary("AI summary"); // AI-generated

    if (request.getAttachmentUrl() != null) {
      // Upload to S3 (assume attachmentUrl is local path for upload)
      PutObjectRequest putObjectRequest = PutObjectRequest.builder()
          .bucket(s3Bucket)
          .key(Paths.get(request.getAttachmentUrl()).getFileName().toString())
          .build();
      s3Client.putObject(putObjectRequest, Paths.get(request.getAttachmentUrl()));
      newTask.setAttachmentUrl(
          "https://" + s3Bucket + ".s3.amazonaws.com/" + Paths.get(request.getAttachmentUrl()).getFileName());
    }

    newTask.setAssignee(currentUser);
    taskRepository.save(newTask);

    return mapToDTO(newTask);

  }

  public TaskDTO updateTask(Long taskId, TaskUpdateDTO request) {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    User currentUser = userRepository.findByCognitoId(auth.getName())
        .orElseThrow(() -> new RuntimeException("User not found"));

    Task task = taskRepository.findById(taskId)
        .orElseThrow(() -> new RuntimeException("Task not found"));

    if (!task.getAssignee().getId().equals(currentUser.getId()) && !currentUser.getRoles()
        .contains("Admin")) {
      throw new RuntimeException("Unauthorized");
    }
    if (request.getTitle() != null) {
      task.setTitle(request.getTitle());
    }
    if (request.getDescription() != null) {
      task.setDescription(request.getDescription());
    }
    if (request.getDueDate() != null) {
      task.setDueDate(request.getDueDate());
    }
    if (request.getTags() != null) {
      task.setTags(request.getTags());
    }
    if (request.getPriority() != null) {
      task.setPriority(request.getPriority());
    }
    if (request.getSummary() != null) {
      task.setSummary(request.getSummary());
    }
    if (request.getAttachmentUrl() != null) {
      // Upload new attachment to S3
      PutObjectRequest putObjectRequest = PutObjectRequest.builder()
          .bucket(s3Bucket)
          .key(Paths.get(request.getAttachmentUrl()).getFileName().toString())
          .build();
      s3Client.putObject(putObjectRequest, Paths.get(request.getAttachmentUrl()));
      task.setAttachmentUrl(
          "https://" + s3Bucket + ".s3.amazonaws.com/" + Paths.get(request.getAttachmentUrl())
              .getFileName());
    }
    if (request.getAssigneeId() != null && currentUser.getRoles().contains("Admin")) {
      User assignee = userRepository.findById(request.getAssigneeId())
          .orElseThrow(() -> new RuntimeException("Assignee not found"));
      task.setAssignee(assignee);
    }

    taskRepository.save(task);
    return mapToDTO(task);


  }

  public void deleteTask(Long taskId) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    User currentUser = userRepository.findByCognitoId(auth.getName())
        .orElseThrow(() -> new RuntimeException("User not found"));

    Task task = taskRepository.findById(taskId)
        .orElseThrow(() -> new RuntimeException("Task not found"));

    // Role check: Admin or owner
    if (!task.getAssignee().getId().equals(currentUser.getId()) && !currentUser.getRoles().contains("Admin")) {
      throw new RuntimeException("Unauthorized");
    }

    taskRepository.delete(task);
  }
  public List<TaskDTO> getAllTasks() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    User currentUser = userRepository.findByCognitoId(auth.getName())
        .orElseThrow(() -> new RuntimeException("User not found"));

    if (!currentUser.getRoles().contains("Admin")) {
      throw new RuntimeException("Unauthorized");
    }

    return taskRepository.findAll().stream()
        .map(this::mapToDTO)
        .collect(Collectors.toList());
  }

  public List<TaskDTO> getMyTasks() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    User currentUser = userRepository.findByCognitoId(auth.getName())
        .orElseThrow(() -> new RuntimeException("User not found"));

    return taskRepository.findByAssigneeId(currentUser.getId()).stream()
        .map(this::mapToDTO)
        .collect(Collectors.toList());
  }
  private TaskDTO mapToDTO(Task task) {
    TaskDTO dto = new TaskDTO();
    dto.setId(task.getId());
    dto.setTitle(task.getTitle());
    dto.setDescription(task.getDescription());
    dto.setDueDate(task.getDueDate());
    dto.setTags(task.getTags());
    dto.setPriority(task.getPriority());
    dto.setSummary(task.getSummary());
    dto.setAttachmentUrl(task.getAttachmentUrl());
    dto.setAssigneeId(task.getAssignee() != null ? task.getAssignee().getId() : null);
    dto.setComments(task.getComments().stream().map(comment -> {
      CommentDTO commentDTO = new CommentDTO();
      commentDTO.setId(comment.getId());
      commentDTO.setContent(comment.getContent());
      commentDTO.setTaskId(comment.getTask().getId());
      commentDTO.setAuthorId(comment.getAuthor().getId());
      commentDTO.setAuthorUsername(comment.getAuthor().getUsername());
      commentDTO.setCreatedAt(comment.getCreatedAt());
      return commentDTO;
    }).collect(Collectors.toList()));
    dto.setCreatedAt(task.getCreatedAt());
    dto.setUpdatedAt(task.getUpdatedAt());
    return dto;
  }
}
