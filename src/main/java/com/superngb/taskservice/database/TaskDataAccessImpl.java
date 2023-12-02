package com.superngb.taskservice.database;

import com.superngb.taskservice.domain.TaskDataAccess;
import com.superngb.taskservice.entity.Task;
import com.superngb.taskservice.repository.TaskRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TaskDataAccessImpl implements TaskDataAccess {

    private final TaskRepository taskRepository;

    public TaskDataAccessImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task findById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    @Override
    public List<Task> findByUserId(Long id) {
        return taskRepository.findAllByUsersId(id).orElse(null);
    }

    @Override
    public List<Task> findByCardId(Long id) {
        return taskRepository.findAllByCardId(id).orElse(null);
    }

    @Override
    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task deleteById(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            taskRepository.deleteById(id);
            return optionalTask.get();
        }
        return null;
    }
}
