package com.softserve.amqp.message_producer.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.softserve.amqp.marker.Archivable;
import com.softserve.amqp.message_producer.RabbitMQMessageProducer;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;

@Slf4j
public class ArchiveMQMessageProducer<T extends Archivable> implements RabbitMQMessageProducer<T> {
    private final AmqpTemplate amqpTemplate;
    private final String topicExchangeName;
    private final String archiveMQRoutingKey;
    private final ObjectMapper objectMapper;
    private final MapType mapType;

    public ArchiveMQMessageProducer(AmqpTemplate amqpTemplate, ObjectMapper objectMapper, String topicExchangeName,
                                    String archiveMQRoutingKey) {
        this.amqpTemplate = amqpTemplate;
        this.objectMapper = objectMapper;
        this.topicExchangeName = topicExchangeName;
        this.archiveMQRoutingKey = archiveMQRoutingKey;
        this.mapType = objectMapper.getTypeFactory().constructMapType(LinkedHashMap.class, String.class, String.class);
    }

    @Override
    public void publish(T payload) {
        Map<String, String> message = objectMapper.convertValue(payload, mapType);
        message.put("class_name", payload.getClass().getName());

        log.info("Publishing to {} using routingKey {}. Payload: {}", topicExchangeName, archiveMQRoutingKey, message);
        amqpTemplate.convertAndSend(topicExchangeName, archiveMQRoutingKey, message);
    }
}
