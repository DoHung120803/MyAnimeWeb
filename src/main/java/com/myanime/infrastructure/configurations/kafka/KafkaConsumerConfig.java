package com.myanime.infrastructure.configurations.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class KafkaConsumerConfig {
    @Value(value = "${kafka.brokers}")
    private String kafkaBootstrapAddress;
    @Value(value = "${kafka.username}")
    private String kafkaUsername;
    @Value(value = "${kafka.password}")
    private String kafkaPassword;
    @Value(value = "${kafka.consumer.batch-size}")
    private int kafkaConsumerBatchSize;
    @Value(value = "${kafka.consumer.concurrency}")
    private int kafkaConsumerConcurrency;

    @Value(value = "${kafka.auto-commit-enable}")
    private Boolean enableAutoCommit;
    private static final String SECURITY_PROTOCOL = "PLAINTEXT";

    public ConsumerFactory<String, String> consumerGenericAvroFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapAddress);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafkaConsumerBatchSize);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, SECURITY_PROTOCOL);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        return new DefaultKafkaConsumerFactory<>(props);
    }

    private CommonErrorHandler getErrorHandler() {
        FixedBackOff fixedBackOff = new FixedBackOff(1000L, 3);

        return new DefaultErrorHandler((message, exception) -> {
            log.error(">>> {}", exception.getMessage());
//            System.exit(0);
        }, fixedBackOff);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> genericKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerGenericAvroFactory());
        factory.setCommonErrorHandler(getErrorHandler());
        factory.getContainerProperties().setSyncCommits(true);
//        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setBatchListener(true);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.setConcurrency(kafkaConsumerConcurrency);
        return factory;
    }
}
