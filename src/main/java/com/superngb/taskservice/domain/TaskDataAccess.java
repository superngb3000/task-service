package com.superngb.taskservice.domain;

import com.superngb.taskservice.entity.Task;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TaskDataAccess {
    Task save(Task task);

    Task findById(Long id);

    List<Task> findByUserId(Long id);

    List<Task> findByCardId(Long id);

    List<Task> getTasks();

    Task deleteById(Long id);
}
