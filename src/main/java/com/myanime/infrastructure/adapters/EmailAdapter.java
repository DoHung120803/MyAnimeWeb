package com.myanime.infrastructure.adapters;

import com.myanime.common.utils.ModelMapperUtil;
import com.myanime.domain.dtos.notifies.EmailDTO;
import com.myanime.domain.port.output.EmailRepository;
import com.myanime.infrastructure.rest_client.notifies.email.EmailService;
import com.myanime.infrastructure.rest_client.notifies.email.SendEmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailAdapter implements EmailRepository {

    private final EmailService emailService;

    @Override
    public void sendEmail(EmailDTO event) {
        if (event == null) {
            return;
        }

        emailService.send(ModelMapperUtil.mapper(event, SendEmailRequest.class));
    }
}
