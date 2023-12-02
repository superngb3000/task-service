package com.superngb.taskservice.controller;

import com.superngb.taskservice.domain.TaskInputBoundary;
import com.superngb.taskservice.model.TaskDtoModel;
import com.superngb.taskservice.model.TaskPostModel;
import com.superngb.taskservice.model.TaskUpdateModel;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class TaskController {

    private final TaskInputBoundary taskInputBoundary;

    public TaskController(TaskInputBoundary taskInputBoundary) {
        this.taskInputBoundary = taskInputBoundary;
    }

    @PostMapping
    public TaskDtoModel postTask(@RequestBody @Valid TaskPostModel model) {
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

    @GetMapping("/user/{id}")
    public List<TaskDtoModel> getTasksByUserId(@PathVariable Long id) {
        return taskInputBoundary.getTasksByUserId(id);
    }

    @GetMapping("/card/{id}")
    public List<TaskDtoModel> getTasksByCardId(@PathVariable Long id) {
        return taskInputBoundary.getTasksByCardId(id);
    }

    @PutMapping
    public TaskDtoModel updateTask(@RequestBody @Valid TaskUpdateModel model) {
        return taskInputBoundary.updateTask(model);
    }

    @DeleteMapping("/{id}")
    public TaskDtoModel deleteTask(@PathVariable Long id) {
        return taskInputBoundary.deleteTask(id);
    }

    @PatchMapping("/removeUser/{id}")
    void removeUserFromTasks(@PathVariable Long id){
        taskInputBoundary.removeUserFromTasks(id);
    }

    @DeleteMapping("/deleteByCard/{id}")
    void deleteTasksByCard(@PathVariable Long id){
        taskInputBoundary.deleteTasksByCard(id);
    }
}
