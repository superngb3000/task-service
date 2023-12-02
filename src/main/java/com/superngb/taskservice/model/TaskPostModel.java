package com.superngb.taskservice.model;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TaskPostModel {
    private String name;
    private String description;
    private Date deadline;
    private Long cardId;
    private Long creatorId;
    private List<Long> usersId;
}
