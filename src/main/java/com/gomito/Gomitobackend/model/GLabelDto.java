package com.gomito.Gomitobackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GLabelDto {
    private Long labelId;
    private String labelName;
    private Long listId;
    private Integer labelIndex;
}