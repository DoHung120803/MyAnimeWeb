package com.myanime.domain.port.output;

import com.myanime.domain.dtos.notifies.EmailDTO;
import com.myanime.domain.dtos.notifies.NotificationDTO;

public interface EmailRepository {
    void sendEmail(EmailDTO email);
}
