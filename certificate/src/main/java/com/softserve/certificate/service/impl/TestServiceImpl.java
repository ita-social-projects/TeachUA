package com.softserve.certificate.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.amqp.message_producer.impl.ArchiveMQMessageProducer;
import com.softserve.certificate.model.TestModel;
import com.softserve.certificate.repository.TestRepository;
import com.softserve.certificate.service.TestService;
import com.softserve.commons.client.ArchiveClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TestServiceImpl implements TestService {
    private final TestRepository testRepository;
    private final ArchiveMQMessageProducer<TestModel> archiveMQMessageProducer;
    private final ArchiveClient archiveClient;
    private final ObjectMapper objectMapper;

    public TestServiceImpl(TestRepository testRepository, ArchiveMQMessageProducer<TestModel> archiveMQMessageProducer,
                           ArchiveClient archiveClient, ObjectMapper objectMapper) {
        this.testRepository = testRepository;
        this.archiveMQMessageProducer = archiveMQMessageProducer;
        this.archiveClient = archiveClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public void save(TestModel data) {
        log.info("save {}", testRepository.save(data));
    }

    @Override
    public void delete(Long id) {
        log.info("delete {}", id);
        var model = testRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        testRepository.deleteById(id);
        archiveMQMessageProducer.publish(model);
    }

    @Override
    public void restore(Long id) {
        var target = objectMapper.convertValue(
                archiveClient.restoreModel(TestModel.class.getName(), id),
                TestModel.class);
        log.info("restore {}", target);

        testRepository.save(target);
    }
}
