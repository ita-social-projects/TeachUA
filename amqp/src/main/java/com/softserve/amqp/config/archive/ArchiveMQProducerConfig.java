package com.softserve.amqp.config.archive;

import com.softserve.amqp.config.RabbitMQConfig;
import com.softserve.amqp.message_producer.impl.ArchiveMQMessageProducer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static com.softserve.amqp.config.archive.ArchiveMQConfig.ARCHIVE_MQ_ROUTING_KEY;

@Configuration
public class ArchiveMQProducerConfig extends RabbitMQConfig {
    public ArchiveMQProducerConfig(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Bean
    public ArchiveMQMessageProducer archiveMQMessageProducer() {
        return new ArchiveMQMessageProducer(amqpTemplate(), TOPIC_EXCHANGE_NAME, ARCHIVE_MQ_ROUTING_KEY);
    }
}
