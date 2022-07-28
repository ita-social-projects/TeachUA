package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.model.test.Test;
import com.softserve.teachua.repository.test.TestRepository;
import com.softserve.teachua.service.test.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class TestServiceImpl implements TestService {
    private final TestRepository testRepository;

    public List<Test> findActiveTests(){
        return testRepository.findActiveTests();
    }

    public List<Test> findArchivedTests(){
        return testRepository.findArchivedTests();
    }

    public List<Test> findUnarchivedTests(){
        return testRepository.findUnarchivedTests();
    }

    public void archiveTestById(Long id){
        Test testToArchive = testRepository.findById(id).orElse(null);  // ask how to handle
        testToArchive.setArchived(true);

        testRepository.save(testToArchive);
    }
}
