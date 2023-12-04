package com.superngb.taskservice.domain;

import com.superngb.taskservice.client.CardServiceClient;
import com.superngb.taskservice.client.UserServiceClient;
import com.superngb.taskservice.entity.Task;
import com.superngb.taskservice.model.TaskDtoModel;
import com.superngb.taskservice.model.TaskPostModel;
import com.superngb.taskservice.model.TaskUpdateModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
public class TaskInteractor implements TaskInputBoundary {

    private final TaskDataAccess taskDataAccess;
    private final TaskOutputBoundary taskOutputBoundary;
    private final UserServiceClient userServiceClient;
    private final CardServiceClient cardServiceClient;

    public TaskInteractor(TaskDataAccess taskDataAccess,
                          TaskOutputBoundary taskOutputBoundary,
                          UserServiceClient userServiceClient,
                          CardServiceClient cardServiceClient) {
        this.taskDataAccess = taskDataAccess;
        this.taskOutputBoundary = taskOutputBoundary;
        this.userServiceClient = userServiceClient;
        this.cardServiceClient = cardServiceClient;
    }

    @Override
    public TaskDtoModel createTask(TaskPostModel taskPostModel) {
        if (!userServiceClient.userExists(taskPostModel.getCreatorId())) {
            return taskOutputBoundary.prepareFailPostTaskView();
        }
        if (!userServiceClient.userExists(taskPostModel.getCardId())) {
            return taskOutputBoundary.prepareFailPostTaskView();
        }
        List<Long> usersId;
        if (taskPostModel.getUsersId() != null) {
            usersId = taskPostModel.getUsersId();
            usersId.add(taskPostModel.getCreatorId());
            usersId = usersId.stream()
                    .filter(u -> u != null && userServiceClient.userExists(u))
                    .sorted()
                    .distinct()
                    .toList();
        } else {
            usersId = new ArrayList<>(List.of(taskPostModel.getCreatorId()));
        }
        return taskOutputBoundary.prepareSuccessPostTaskView(TaskDtoModel.mapper(
                taskDataAccess.save(Task.builder()
                        .name(taskPostModel.getName())
                        .description(taskPostModel.getDescription())
                        .deadline(taskPostModel.getDeadline())
                        .cardId(taskPostModel.getCardId())
                        .creatorId(taskPostModel.getCreatorId())
                        .usersId(usersId)
                        .build())
        ));
    }

    @Override
    public TaskDtoModel getTask(Long id) {
        Task task = taskDataAccess.findById(id);
        return (task == null)
                ? taskOutputBoundary.prepareFailGetTaskView()
                : taskOutputBoundary.prepareSuccessGetTaskView(TaskDtoModel.mapper(task));
    }

    @Override
    public List<TaskDtoModel> getTasks() {
        return taskOutputBoundary.convertUser(TaskDtoModel.mapper(taskDataAccess.getTasks()));
    }

    @Override
    public List<TaskDtoModel> getTasksByUserId(Long id) {
        return taskOutputBoundary.convertUser(TaskDtoModel.mapper(taskDataAccess.findByUserId(id)));
    }

    @Override
    public List<TaskDtoModel> getTasksByCardId(Long id) {
        return taskOutputBoundary.convertUser(TaskDtoModel.mapper(taskDataAccess.findByCardId(id)));
    }

    @Override
    public TaskDtoModel updateTask(TaskUpdateModel taskUpdateModel) {
        Task taskById = taskDataAccess.findById(taskUpdateModel.getId());
        if (taskById == null) {
            return taskOutputBoundary.prepareFailUpdateTaskView();
        }
        updateFieldIfNotNull(taskUpdateModel.getName(), taskById::getName, taskById::setName);
        updateFieldIfNotNull(taskUpdateModel.getDescription(), taskById::getDescription, taskById::setDescription);
        updateFieldIfNotNull(taskUpdateModel.getDeadline(), taskById::getDeadline, taskById::setDeadline);
        Long cardId = taskUpdateModel.getCardId();
        if (cardId != null && cardServiceClient.cardExists(cardId)
                && (taskById.getCardId() == null || !taskById.getCardId().equals(cardId))) {
            taskById.setCardId(cardId);
        }
        List<Long> usersId = new ArrayList<>(taskUpdateModel.getUsersId().stream()
                .filter(u -> u != null && userServiceClient.userExists(u))
                .sorted()
                .distinct()
                .toList());
        updateFieldIfNotNull(usersId, taskById::getUsersId, taskById::setUsersId);
        return taskOutputBoundary.prepareSuccessUpdateTaskView(TaskDtoModel.mapper(taskDataAccess.save(taskById)));
    }

    private <T> void updateFieldIfNotNull(T newValue, Supplier<T> currentValueSupplier, Consumer<T> updateFunction) {
        T currentValue = currentValueSupplier.get();
        if (newValue != null && (currentValue == null || !Objects.equals(currentValue, newValue))) {
            updateFunction.accept(newValue);
        }
    }

    @Override
    public TaskDtoModel deleteTask(Long id) {
        Task task = taskDataAccess.deleteById(id);
        return (task == null)
                ? taskOutputBoundary.prepareFailDeleteTaskView()
                : taskOutputBoundary.prepareSuccessDeleteTaskView(TaskDtoModel.mapper(task));
    }

    @Override
    public void removeUserFromTasks(Long id) {
        List<Task> taskList = taskDataAccess.findByUserId(id);
        taskList.forEach(task -> {
            List<Long> usersId = task.getUsersId();
            usersId.remove(id);
            task.setUsersId(usersId);
            taskDataAccess.save(task);
        });
    }

    @Override
    public void deleteTasksByCard(Long id) {
        taskDataAccess.findByCardId(id).forEach(task -> taskDataAccess.deleteById(task.getId()));
    }
}
