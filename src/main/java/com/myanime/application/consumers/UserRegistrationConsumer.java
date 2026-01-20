package com.myanime.application.consumers;

import com.myanime.common.exceptions.BadRequestException;
import com.myanime.domain.inputs.consumers.ConsumeInput;
import com.myanime.domain.port.input.consumers.ConsumerUC;
import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRegistrationConsumer {

    private final ConsumerUC consumerUC;

    @KafkaListener(
            topics = "kafka.topic.test.registration",
            groupId = "kafka.group-id.test",
            containerFactory = "genericKafkaListenerContainerFactory",
            concurrency = "3"
    )
    public void consume(List<ConsumerRecord<String, String>> records, Acknowledgment ack) throws BadRequestException {
        try {
            for (ConsumerRecord<String, String> recordItem : records) {
                ConsumeInput consumeInput = new ConsumeInput();
                consumeInput.setRecordItem(recordItem);

                consumerUC.consume(consumeInput);
            }

            ack.acknowledge();
            log.info("Commit {} records from userRegistrationConsumer", records.size());
        } catch (Exception e) {
            Sentry.setExtra("userRegistrationRecords", records.toString());
            Sentry.captureException(e);
            log.error(">>> {}", e.getMessage());

        }
    }

}

