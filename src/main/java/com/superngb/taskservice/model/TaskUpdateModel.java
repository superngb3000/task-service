package com.superngb.taskservice.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TaskUpdateModel {
    @NotNull
    private Long id;
    private String name;
    private String description;
    private Date deadline;
    private Long cardId;
    private List<Long> usersId;
}
