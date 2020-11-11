package com.gomito.Gomitobackend.dto;

import com.gomito.Gomitobackend.model.GLabel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GCardDto {
    private Long cardId;
    private String cardName;
    private String description;
    private Long listId;
    private Integer cardIndex;
    private List<GLabel> labels;
}
