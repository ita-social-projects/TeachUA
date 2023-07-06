package com.softserve.archive.rabbitmq;

import com.softserve.archive.model.Archive;
import com.softserve.archive.service.ArchiveService;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import static com.softserve.amqp.config.archive.ArchiveMQConfig.ARCHIVE_MQ_NAME;

@Component
@Slf4j
public class ArchiveMQConsumer {
    private final ArchiveService archiveService;

    public ArchiveMQConsumer(ArchiveService archiveService) {
        this.archiveService = archiveService;
    }

    @RabbitListener(queues = ARCHIVE_MQ_NAME)
    public void consumer(Map<String, String> notificationRequest) {
        archiveService.save(
                Archive.builder()
                        .className(notificationRequest.remove("class_name"))
                        .data(notificationRequest)
                        .build());

        log.info("Consumed {} from queue {}", notificationRequest, ARCHIVE_MQ_NAME);
    }
}
