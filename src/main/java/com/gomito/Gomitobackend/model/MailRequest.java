package com.gomito.Gomitobackend.model;

import lombok.Data;

@Data
public class MailRequest {
    private String name;
    private String to;
    private String from;
    private String subject;
}
