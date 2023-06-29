package com.softserve.amqp.message_producer.impl;

import com.softserve.amqp.marker.Archivable;
import com.softserve.amqp.message_producer.RabbitMQMessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;

@Slf4j
public class ArchiveMQMessageProducer implements RabbitMQMessageProducer<Archivable> {
    private final AmqpTemplate amqpTemplate;
    private final String topicExchangeName;
    private final String archiveMQRoutingKey;

    public ArchiveMQMessageProducer(AmqpTemplate amqpTemplate, String topicExchangeName, String archiveMQRoutingKey) {
        this.amqpTemplate = amqpTemplate;
        this.topicExchangeName = topicExchangeName;
        this.archiveMQRoutingKey = archiveMQRoutingKey;
    }

    @Override
    public void publish(Archivable payload) {
        log.info("Publishing to {} using routingKey {}. Payload: {}", topicExchangeName, archiveMQRoutingKey, payload);
        amqpTemplate.convertAndSend(topicExchangeName, archiveMQRoutingKey, payload);
    }
}
