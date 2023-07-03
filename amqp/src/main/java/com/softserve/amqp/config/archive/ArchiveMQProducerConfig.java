package com.softserve.amqp.config.archive;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.amqp.config.RabbitMQConfig;
import static com.softserve.amqp.config.archive.ArchiveMQConfig.ARCHIVE_MQ_ROUTING_KEY;
import com.softserve.amqp.marker.Archivable;
import com.softserve.amqp.message_producer.impl.ArchiveMQMessageProducer;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArchiveMQProducerConfig extends RabbitMQConfig {
    public ArchiveMQProducerConfig(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Bean
    public <T extends Archivable> ArchiveMQMessageProducer<T> archiveMQMessageProducer(AmqpTemplate amqpTemplate,
                                                                                   ObjectMapper objectMapper) {
        return new ArchiveMQMessageProducer<>(amqpTemplate, objectMapper, TOPIC_EXCHANGE_NAME, ARCHIVE_MQ_ROUTING_KEY);
    }
}
