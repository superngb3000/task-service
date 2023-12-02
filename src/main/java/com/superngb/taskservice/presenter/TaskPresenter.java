package com.superngb.taskservice.presenter;

import com.superngb.taskservice.domain.TaskOutputBoundary;
import com.superngb.taskservice.model.TaskDtoModel;
import org.springframework.stereotype.Component;

import java.util.List;

//TODO переделать
@Component
public class TaskPresenter implements TaskOutputBoundary {
    @Override
    public TaskDtoModel prepareSuccessPostTaskView(TaskDtoModel model) {
        return model;
    }

    @Override
    public TaskDtoModel prepareFailPostTaskView() {
        return null;
    }

    @Override
    public TaskDtoModel prepareSuccessGetTaskView(TaskDtoModel model) {
        return model;
    }

    @Override
    public TaskDtoModel prepareFailGetTaskView() {
        return null;
    }

    @Override
    public TaskDtoModel prepareSuccessUpdateTaskView(TaskDtoModel model) {
        return model;
    }

    @Override
    public TaskDtoModel prepareFailUpdateTaskView() {
        return null;
    }

    @Override
    public TaskDtoModel prepareSuccessDeleteTaskView(TaskDtoModel model) {
        return model;
    }

    @Override
    public TaskDtoModel prepareFailDeleteTaskView() {
        return null;
    }

    @Override
    public List<TaskDtoModel> convertUser(List<TaskDtoModel> modelList) {
        return modelList;
    }
}
