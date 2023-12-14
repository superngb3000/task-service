package com.superngb.taskservice.controller;

import com.superngb.taskservice.domain.TaskInputBoundary;
import com.superngb.taskservice.model.ResponseModel;
import com.superngb.taskservice.model.TaskPostModel;
import com.superngb.taskservice.model.TaskUpdateModel;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/")
public class TaskController {

    private final TaskInputBoundary taskInputBoundary;

    public TaskController(TaskInputBoundary taskInputBoundary) {
        this.taskInputBoundary = taskInputBoundary;
    }

    @PostMapping
    public ResponseEntity<?> postTask(@RequestBody @Valid TaskPostModel model) {
        ResponseModel<?> response = taskInputBoundary.createTask(model);
        return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getCode()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTask(@PathVariable Long id) {
        ResponseModel<?> response = taskInputBoundary.getTask(id);
        return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getCode()));
    }

    @GetMapping
    public ResponseEntity<?> getTasks() {
        ResponseModel<?> response = taskInputBoundary.getTasks();
        return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getCode()));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getTasksByUserId(@PathVariable Long id) {
        ResponseModel<?> response = taskInputBoundary.getTasksByUserId(id);
        return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getCode()));
    }

    @GetMapping("/card/{id}")
    public ResponseEntity<?> getTasksByCardId(@PathVariable Long id) {
        ResponseModel<?> response = taskInputBoundary.getTasksByCardId(id);
        return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getCode()));
    }

    @PutMapping
    public ResponseEntity<?> updateTask(@RequestBody @Valid TaskUpdateModel model) {
        ResponseModel<?> response = taskInputBoundary.updateTask(model);
        return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getCode()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        ResponseModel<?> response = taskInputBoundary.deleteTask(id);
        return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getCode()));
    }

    @PatchMapping("/removeUser/{id}")
    public ResponseEntity<?> removeUserFromTasks(@PathVariable Long id) {
        ResponseModel<?> response = taskInputBoundary.removeUserFromTasks(id);
        return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getCode()));
    }

    @DeleteMapping("/deleteByCard/{id}")
    public ResponseEntity<?> deleteTasksByCard(@PathVariable Long id) {
        ResponseModel<?> response = taskInputBoundary.deleteTasksByCard(id);
        return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getCode()));
    }
}
