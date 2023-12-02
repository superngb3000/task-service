package com.superngb.taskservice.domain;

import com.superngb.taskservice.model.TaskDtoModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TaskOutputBoundary {
    TaskDtoModel prepareSuccessPostTaskView(TaskDtoModel model);
    TaskDtoModel prepareFailPostTaskView();
    TaskDtoModel prepareSuccessGetTaskView(TaskDtoModel model);
    TaskDtoModel prepareFailGetTaskView();
    TaskDtoModel prepareSuccessUpdateTaskView(TaskDtoModel model);
    TaskDtoModel prepareFailUpdateTaskView();
    TaskDtoModel prepareSuccessDeleteTaskView(TaskDtoModel model);
    TaskDtoModel prepareFailDeleteTaskView();
    List<TaskDtoModel> convertUser(List<TaskDtoModel> modelList);
}
