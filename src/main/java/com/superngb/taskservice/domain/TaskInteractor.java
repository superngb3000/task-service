package com.superngb.taskservice.domain;

import com.superngb.taskservice.entity.Task;
import com.superngb.taskservice.model.TaskDtoModel;
import com.superngb.taskservice.model.TaskPostModel;
import com.superngb.taskservice.model.TaskUpdateModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
public class TaskInteractor implements TaskInputBoundary{

    private final TaskDataAccess taskDataAccess;
    private final TaskOutputBoundary taskOutputBoundary;

    public TaskInteractor(TaskDataAccess taskDataAccess, TaskOutputBoundary taskOutputBoundary) {
        this.taskDataAccess = taskDataAccess;
        this.taskOutputBoundary = taskOutputBoundary;
    }

    //TODO проверка на существование card (запрос в card-service)
    //TODO проверка на существование creator и users (запрос в user-service)
    @Override
    public TaskDtoModel createTask(TaskPostModel taskPostModel) {
        if (taskPostModel == null ||
                taskPostModel.getName() == null ||
                taskPostModel.getDescription() == null ||
                taskPostModel.getDeadline() == null ||
                taskPostModel.getCardId() == null ||
                taskPostModel.getCreatorId() == null ||
                taskPostModel.getUsersId() == null) {
            return taskOutputBoundary.prepareFailPostTaskView();
        }
        if (!taskPostModel.getUsersId().contains(taskPostModel.getCreatorId())){
            taskPostModel.setUsersId(new ArrayList<>(List.of(taskPostModel.getCreatorId())));
        }
        return taskOutputBoundary.prepareSuccessPostTaskView(TaskDtoModel.mapper(
                taskDataAccess.save(Task.builder()
                        .name(taskPostModel.getName())
                        .description(taskPostModel.getDescription())
                        .deadline(taskPostModel.getDeadline())
                        .cardId(taskPostModel.getCardId())
                        .creatorId(taskPostModel.getCreatorId())
                        .usersId(taskPostModel.getUsersId())
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

    //TODO проверка на существование card (запрос в card-service)
    //TODO проверка на существование creator и users (запрос в user-service)
    @Override
    public TaskDtoModel updateTask(TaskUpdateModel taskUpdateModel) {
        Task taskById = taskDataAccess.findById(taskUpdateModel.getId());
        if (taskById == null) {
            return taskOutputBoundary.prepareFailUpdateTaskView();
        }
        updateFieldIfNotNull(taskUpdateModel.getName(), taskById::getName, taskById::setName);
        updateFieldIfNotNull(taskUpdateModel.getDescription(), taskById::getDescription, taskById::setDescription);
        updateFieldIfNotNull(taskUpdateModel.getDeadline(), taskById::getDeadline, taskById::setDeadline);
        updateFieldIfNotNull(taskUpdateModel.getCardId(), taskById::getCardId, taskById::setCardId);
        List<Long> usersId = new ArrayList<>(taskUpdateModel.getUsersId().stream()
                .filter(Objects::nonNull)
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
}
