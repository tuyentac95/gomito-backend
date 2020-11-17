package com.gomito.Gomitobackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentDto {
    private Long attachmentId;
    private String attachmentName;
    private String attachmentUrl;
    private Long cardId;
}
