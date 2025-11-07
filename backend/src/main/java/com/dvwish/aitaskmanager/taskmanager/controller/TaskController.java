package com.dvwish.aitaskmanager.taskmanager.controller;

import com.dvwish.aitaskmanager.taskmanager.dto.TaskCreateDTO;
import com.dvwish.aitaskmanager.taskmanager.dto.TaskDTO;
import com.dvwish.aitaskmanager.taskmanager.dto.TaskUpdateDTO;
import com.dvwish.aitaskmanager.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

  private final TaskService taskService;

  @PostMapping
  @PreAuthorize("hasAnyAuthority('User','Admin')")
  public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskCreateDTO request) {
    TaskDTO task = taskService.createTask(request);
    return ResponseEntity.ok(task);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAnyAuthority('User','Admin')")
  public ResponseEntity<TaskDTO> updateTask(@PathVariable long id,
      @Valid @RequestBody TaskUpdateDTO taskUpdateDTO) {
    TaskDTO task = taskService.updateTask(id, taskUpdateDTO);
    return ResponseEntity.ok(task);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyAuthority('User','Admin')")
  public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
    taskService.deleteTask(id);
    return ResponseEntity.noContent().build();

  }

  @GetMapping
  @PreAuthorize("hasAuthority('Admin')")
  public ResponseEntity<List<TaskDTO>> getAllTasks() {
    List<TaskDTO> tasks = taskService.getAllTasks();
    return ResponseEntity.ok(tasks);

  }

  @GetMapping("/my-tasks")
  @PreAuthorize("hasAnyAuthority('User','Admin')")
  public ResponseEntity<List<TaskDTO>> getMyTasks() {
    List<TaskDTO> tasks = taskService.getMyTasks();
    return ResponseEntity.ok(tasks);
  }

}
