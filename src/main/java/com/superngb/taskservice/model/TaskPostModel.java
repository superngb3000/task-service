package com.superngb.taskservice.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TaskPostModel {
    @NotBlank
    private String name;
    private String description;
    private Date deadline;
    @NotNull
    private Long cardId;
    @NotNull
    private Long creatorId;
    private List<Long> usersId;
}
