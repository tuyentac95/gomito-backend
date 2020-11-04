package com.gomito.Gomitobackend.service;

import com.gomito.Gomitobackend.Exception.SpringGomitoException;
import com.gomito.Gomitobackend.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {
    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;

    void setMail(NotificationEmail notificationEmail) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("langquang1995@gmail.com");
            messageHelper.setTo(notificationEmail.getRecipient());
          messageHelper.setSubject(notificationEmail.getSubject());
          messageHelper.setText(mailContentBuilder.build(
                  notificationEmail.getBody()
          ));
        };
        try {
            mailSender.send(messagePreparator);
            log.info("Đã gửi email kích hoạt");
        } catch (MailException ex) {
            throw new SpringGomitoException("Đã xảy ra lỗi trong quá trình gửi thư"
            + notificationEmail.getRecipient());
        }
    }
}
