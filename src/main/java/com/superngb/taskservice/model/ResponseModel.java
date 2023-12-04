package com.superngb.taskservice.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseModel<T> {
    private boolean status;
    private Integer code;
    private String message;
    private T body;
}
