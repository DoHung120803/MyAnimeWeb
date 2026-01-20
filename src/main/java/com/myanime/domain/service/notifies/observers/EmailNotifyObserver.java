package com.myanime.domain.service.notifies.observers;

import com.myanime.common.utils.FileUtil;
import com.myanime.domain.dtos.notifies.EmailDTO;
import com.myanime.domain.dtos.notifies.NotificationDTO;
import com.myanime.domain.enums.NotifyType;
import com.myanime.domain.port.output.EmailRepository;
import com.myanime.domain.service.notifies.PostNotifyObserver;
import com.myanime.domain.service.templates.TemplateService;
import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailNotifyObserver implements PostNotifyObserver {
    private final EmailRepository emailRepository;
    private final TemplateService templateService;

    @Override
    public Short getSupportedType() {
        return NotifyType.EMAIL.getValue();
    }

    @Override
    public void sendNotification(NotificationDTO event) {
        if (event == null) {
            return;
        }

        EmailDTO emailDTO = buildEmail(event);
        emailRepository.sendEmail(emailDTO);
    }

    private EmailDTO buildEmail(NotificationDTO event) {
        EmailDTO emailDTO = new EmailDTO();

        emailDTO.setHtmlContent(buildContent(event));
        emailDTO.setSubject(event.getTitle());

        EmailDTO.Recipient recipient = new EmailDTO.Recipient();
        recipient.setEmail(event.getReceiver());
        recipient.setName(event.getMetaData().getOrDefault("fullName", "New Member").toString());
        emailDTO.setTo(List.of(recipient));

        EmailDTO.Sender sender = new EmailDTO.Sender();
        sender.setEmail("huna12082003@gmail.com");
        sender.setName("Admin");
        emailDTO.setSender(sender);

        return emailDTO;
    }

    private String buildContent(NotificationDTO event) {
        String content = FileUtil.readFromFile("templates/welcome-email-content.html");

        Map<String, String> variables = new TreeMap<>();
        variables.put("content", content);
        variables.put("fullName", event.getMetaData().getOrDefault("fullName", "New Member").toString());
        variables.put("template-title", "Welcome to MyAnime 🎉");

        return templateService.loadTemplate("templates/email-template.html", variables);
    }
}
