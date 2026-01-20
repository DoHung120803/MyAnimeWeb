package com.myanime.domain.inputs.consumers;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.consumer.ConsumerRecord;

@Getter
@Setter
public class ConsumeInput {
    ConsumerRecord<String, String> recordItem;
}
