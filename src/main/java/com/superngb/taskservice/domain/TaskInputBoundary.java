package com.superngb.taskservice.domain;

import com.superngb.taskservice.model.TaskDtoModel;
import com.superngb.taskservice.model.TaskPostModel;
import com.superngb.taskservice.model.TaskUpdateModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TaskInputBoundary {
    TaskDtoModel createTask(TaskPostModel taskPostModel);
    TaskDtoModel getTask(Long id);
    List<TaskDtoModel> getTasks();
    List<TaskDtoModel> getTasksByUserId(Long id);
    List<TaskDtoModel> getTasksByCardId(Long id);
    TaskDtoModel updateTask(TaskUpdateModel taskUpdateModel);
    TaskDtoModel deleteTask(Long id);
    void removeUserFromTasks(Long id);
    void deleteTasksByCard(Long id);
}
