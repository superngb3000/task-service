package com.superngb.taskservice.controller;

import com.superngb.taskservice.domain.TaskInputBoundary;
import com.superngb.taskservice.model.TaskDtoModel;
import com.superngb.taskservice.model.TaskPostModel;
import com.superngb.taskservice.model.TaskUpdateModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/tasks")
public class TaskController {

    private final TaskInputBoundary taskInputBoundary;

    public TaskController(TaskInputBoundary taskInputBoundary) {
        this.taskInputBoundary = taskInputBoundary;
    }

    @PostMapping
    public TaskDtoModel postTask(@RequestBody TaskPostModel model) {
        return taskInputBoundary.createTask(model);
    }

    @GetMapping("/{id}")
    public TaskDtoModel getTask(@PathVariable Long id) {
        return taskInputBoundary.getTask(id);
    }

    @GetMapping
    public List<TaskDtoModel> getTasks() {
        return taskInputBoundary.getTasks();
    }

    @GetMapping("user/{id}")
    public List<TaskDtoModel> getTasksByUserId(@PathVariable Long id) {
        return taskInputBoundary.getTasksByUserId(id);
    }

    @GetMapping("card/{id}")
    public List<TaskDtoModel> getTasksByCardId(@PathVariable Long id) {
        return taskInputBoundary.getTasksByCardId(id);
    }

    @PutMapping
    public TaskDtoModel updateTask(@RequestBody TaskUpdateModel model) {
        return taskInputBoundary.updateTask(model);
    }

    @DeleteMapping("/{id}")
    public TaskDtoModel deleteTask(@PathVariable Long id) {
        return taskInputBoundary.deleteTask(id);
    }
}
