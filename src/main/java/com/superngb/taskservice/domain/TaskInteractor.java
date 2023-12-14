package com.superngb.taskservice.domain;

import com.superngb.taskservice.client.CardServiceClient;
import com.superngb.taskservice.client.UserServiceClient;
import com.superngb.taskservice.entity.Task;
import com.superngb.taskservice.model.ResponseModel;
import com.superngb.taskservice.model.TaskDtoModel;
import com.superngb.taskservice.model.TaskPostModel;
import com.superngb.taskservice.model.TaskUpdateModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
public class TaskInteractor implements TaskInputBoundary {

    private final TaskDataAccess taskDataAccess;
    private final UserServiceClient userServiceClient;
    private final CardServiceClient cardServiceClient;

    public TaskInteractor(TaskDataAccess taskDataAccess,
                          UserServiceClient userServiceClient,
                          CardServiceClient cardServiceClient) {
        this.taskDataAccess = taskDataAccess;
        this.userServiceClient = userServiceClient;
        this.cardServiceClient = cardServiceClient;
    }

    @Override
    public ResponseModel<?> createTask(TaskPostModel taskPostModel) {
        ResponseEntity<?> getUserResponse = userServiceClient.getUser(taskPostModel.getCreatorId());
        if (!getUserResponse.getStatusCode().equals(HttpStatus.valueOf(200))) {
            return ResponseModel.builder().code(403).body("User with userId = " + taskPostModel.getCreatorId().toString() + " does not exist").build();
        }
        ResponseEntity<?> getCardResponse = cardServiceClient.getCard(taskPostModel.getCardId());
        if (!getCardResponse.getStatusCode().equals(HttpStatus.valueOf(200))) {
            return ResponseModel.builder().code(403).body("Card with cardId = " + taskPostModel.getCardId().toString() + " does not exist").build();
        }
        List<Long> usersId;
        if (taskPostModel.getUsersId() != null) {
            usersId = taskPostModel.getUsersId();
            usersId.add(taskPostModel.getCreatorId());
            usersId = usersId.stream()
                    .filter(u -> u != null
                            && userServiceClient.getUser(u).getStatusCode().equals(HttpStatus.valueOf(200)))
                    .sorted()
                    .distinct()
                    .toList();
        } else {
            usersId = new ArrayList<>(List.of(taskPostModel.getCreatorId()));
        }
        return ResponseModel.builder().code(200).body(
                TaskDtoModel.mapper(
                        taskDataAccess.save(Task.builder()
                                .name(taskPostModel.getName())
                                .description(taskPostModel.getDescription())
                                .deadline(taskPostModel.getDeadline())
                                .cardId(taskPostModel.getCardId())
                                .creatorId(taskPostModel.getCreatorId())
                                .usersId(usersId)
                                .build())
                )
        ).build();
    }

    @Override
    public ResponseModel<?> getTask(Long id) {
        Task task = taskDataAccess.findById(id);
        return (task == null)
                ? ResponseModel.builder().code(404).body("Task with taskId = " + id.toString() + " not found").build()
                : ResponseModel.builder().code(200).body(TaskDtoModel.mapper(task)).build();
    }

    @Override
    public ResponseModel<?> getTasks() {
        return ResponseModel.builder().code(200).body(TaskDtoModel.mapper(taskDataAccess.getTasks())).build();
    }

    @Override
    public ResponseModel<?> getTasksByUserId(Long id) {
        List<Task> taskList = taskDataAccess.findByUserId(id);
        return (taskList == null)
                ? ResponseModel.builder().code(404).body("There are no tasks with user with userId = " + id.toString()).build()
                : ResponseModel.builder().code(200).body(TaskDtoModel.mapper(taskList)).build();
    }

    @Override
    public ResponseModel<?> getTasksByCardId(Long id) {
        List<Task> taskList = taskDataAccess.findByCardId(id);
        return (taskList == null)
                ? ResponseModel.builder().code(404).body("There are no tasks with card with cardId = " + id.toString()).build()
                : ResponseModel.builder().code(200).body(TaskDtoModel.mapper(taskList)).build();
    }

    @Override
    public ResponseModel<?> updateTask(TaskUpdateModel taskUpdateModel) {
        Task taskById = taskDataAccess.findById(taskUpdateModel.getId());
        if (taskById == null) {
            return ResponseModel.builder().code(404).body("Task with taskId = " + taskUpdateModel.getId().toString() + " not found").build();
        }
        updateFieldIfNotNull(taskUpdateModel.getName(), taskById::getName, taskById::setName);
        updateFieldIfNotNull(taskUpdateModel.getDescription(), taskById::getDescription, taskById::setDescription);
        updateFieldIfNotNull(taskUpdateModel.getDeadline(), taskById::getDeadline, taskById::setDeadline);
        Long cardId = taskUpdateModel.getCardId();
        ResponseEntity<?> getCardResponse = cardServiceClient.getCard(cardId);
        if (!getCardResponse.getStatusCode().equals(HttpStatus.valueOf(200))) {
            return ResponseModel.builder().code(404).body("There are no tasks with card with cardId = " + cardId.toString()).build();
        }
        if (taskById.getCardId() == null || !taskById.getCardId().equals(cardId)) {
            taskById.setCardId(cardId);
        }
        if (taskUpdateModel.getUsersId() != null) {
            List<Long> usersId = new ArrayList<>(taskUpdateModel.getUsersId().stream()
                    .filter(u -> u != null
                            && userServiceClient.getUser(u).getStatusCode().equals(HttpStatus.valueOf(200)))
                    .sorted()
                    .distinct()
                    .toList());
            updateFieldIfNotNull(usersId, taskById::getUsersId, taskById::setUsersId);
        }
        return ResponseModel.builder().code(200).body(TaskDtoModel.mapper(taskDataAccess.save(taskById))).build();
    }

    private <T> void updateFieldIfNotNull(T newValue, Supplier<T> currentValueSupplier, Consumer<T> updateFunction) {
        T currentValue = currentValueSupplier.get();
        if (newValue != null && (currentValue == null || !Objects.equals(currentValue, newValue))) {
            updateFunction.accept(newValue);
        }
    }

    @Override
    public ResponseModel<?> deleteTask(Long id) {
        Task task = taskDataAccess.deleteById(id);
        return (task == null)
                ? ResponseModel.builder().code(404).body("Task with taskId = " + id.toString() + " not found").build()
                : ResponseModel.builder().code(200).body(TaskDtoModel.mapper(task)).build();
    }

    @Override
    public ResponseModel<?> removeUserFromTasks(Long id) {
        ResponseEntity<?> responseEntity = userServiceClient.getUser(id);
        if (!responseEntity.getStatusCode().equals(HttpStatus.valueOf(200))) {
            return ResponseModel.builder().code(404).body("User with userId = " + id.toString() + " does not exist").build();
        }
        List<Task> taskList = taskDataAccess.findByUserId(id);
        taskList.forEach(task -> {
            List<Long> usersId = task.getUsersId();
            usersId.remove(id);
            task.setUsersId(usersId);
            taskDataAccess.save(task);
        });
        return ResponseModel.builder().code(200).body(TaskDtoModel.mapper(taskList)).build();
    }

    @Override
    public ResponseModel<?> deleteTasksByCard(Long id) {
        List<Task> taskList = taskDataAccess.findByCardId(id);
        if (taskList == null) {
            return ResponseModel.builder().code(403).body("There are no tasks with card with cardId = " + id.toString()).build();
        }
        taskList.forEach(task -> taskDataAccess.deleteById(task.getId()));
        return ResponseModel.builder().code(200).body(TaskDtoModel.mapper(taskList)).build();
    }
}
