package com.superngb.taskservice.model;

import com.superngb.taskservice.entity.Task;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TaskDtoModel {
    private Long id;
    private String name;
    private String description;
    private Date deadline;
    private Long cardId;
    private Long creatorId;
    private List<Long> usersId;

    public static TaskDtoModel mapper(Task task) {
        return new TaskDtoModel(
                task.getId(),
                task.getName(),
                task.getDescription(),
                task.getDeadline(),
                task.getCardId(),
                task.getCreatorId(),
                task.getUsersId());
    }

    public static List<TaskDtoModel> mapper(List<Task> taskList) {
        return taskList.stream()
                .map(TaskDtoModel::mapper)
                .toList();
    }
}
