package com.superngb.taskservice.domain;

import com.superngb.taskservice.model.ResponseModel;
import com.superngb.taskservice.model.TaskPostModel;
import com.superngb.taskservice.model.TaskUpdateModel;
import org.springframework.stereotype.Component;

@Component
public interface TaskInputBoundary {
    ResponseModel<?> createTask(TaskPostModel taskPostModel);

    ResponseModel<?> getTask(Long id);

    ResponseModel<?> getTasks();

    ResponseModel<?> getTasksByUserId(Long id);

    ResponseModel<?> getTasksByCardId(Long id);

    ResponseModel<?> updateTask(TaskUpdateModel taskUpdateModel);

    ResponseModel<?> deleteTask(Long id);

    ResponseModel<?> removeUserFromTasks(Long id);

    ResponseModel<?> deleteTasksByCard(Long id);
}
