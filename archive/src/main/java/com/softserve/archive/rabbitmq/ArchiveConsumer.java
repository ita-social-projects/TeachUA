package com.softserve.archive.rabbitmq;

import static com.softserve.amqp.config.archive.ArchiveMQConfig.ARCHIVE_MQ_NAME;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ArchiveConsumer {
    @RabbitListener(queues = ARCHIVE_MQ_NAME)
    public void consumer(Map<String, String> notificationRequest) {
        //todo
        log.info("Consumed {} from queue {}", notificationRequest, ARCHIVE_MQ_NAME);
    }
}
