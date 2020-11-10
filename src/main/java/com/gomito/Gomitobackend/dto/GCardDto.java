package com.gomito.Gomitobackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GCardDto {
    private Long cardId;
    private String cardName;
    private Long listId;
    private Integer cardIndex;
}
