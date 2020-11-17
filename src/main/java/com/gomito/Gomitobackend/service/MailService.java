package com.gomito.Gomitobackend.service;

import com.gomito.Gomitobackend.Exception.SpringGomitoException;
import com.gomito.Gomitobackend.model.MailRequest;
import com.gomito.Gomitobackend.model.NotificationEmail;
import freemarker.template.*;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {
    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;
    private final Configuration configuration;

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

    public void sendMail(MailRequest request, Map<String, Object> model, String mailTemplate){
        System.out.println("Sending email....");
        MimeMessage message = mailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(message,MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            Template template = configuration.getTemplate(mailTemplate);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template,model);

            helper.setTo(request.getTo());
            helper.setText(html,true);
            helper.setSubject(request.getSubject());
            helper.setFrom(request.getFrom());
            mailSender.send(message);
            System.out.println("Sent");
        } catch (MessagingException | TemplateException | IOException e) {
            e.printStackTrace();
        }
    }
}
