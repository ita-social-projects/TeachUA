package com.softserve.amqp.config.archive;

import com.softserve.amqp.config.RabbitMQConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArchiveMQConfig extends RabbitMQConfig {
    public static final String ARCHIVE_MQ_NAME = "archive.queue";
    protected static final String ARCHIVE_MQ_ROUTING_KEY = "archive.routing-key";

    public ArchiveMQConfig(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Bean
    public TopicExchange internalTopicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE_NAME);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(ARCHIVE_MQ_NAME);
    }

    @Bean
    public Binding internalToNotificationBinding() {
        return BindingBuilder
                .bind(notificationQueue())
                .to(internalTopicExchange())
                .with(ARCHIVE_MQ_ROUTING_KEY);
    }
}
