package com.gomito.Gomitobackend.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@AllArgsConstructor
public class MailContentBuilder {

    private final TemplateEngine templateEngine;

    String build(String message) {
        Context context = new Context();
        context.setVariable("message", message);
        return templateEngine.process("email-template-signup.ftl", context);
    }

//    // Thông báo mail khi thêm 1 thành viên vào bảng
//    String notificationAddBoard(String message){
//        Context contextAddBoard = new Context();
//        contextAddBoard.setVariable("message", message);
//        return templateEngine.process("email-template-addBoard.ftl", contextAddBoard);
//    }
}
