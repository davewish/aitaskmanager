package com.dvwish.aitaskmanager.taskmanager.repository;

import com.dvwish.aitaskmanager.taskmanager.model.Task;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task,Long> {
  List<Task> findByAssigneeId(Long userId);

}
