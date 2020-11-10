package com.gomito.Gomitobackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GListDto {
   private Long listId;
   private String listName;
   private Long boardId;
   private Integer listIndex;
}
