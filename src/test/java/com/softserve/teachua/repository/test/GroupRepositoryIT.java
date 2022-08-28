package com.softserve.teachua.repository.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@DataJpaTest
public class GroupRepositoryIT {

    @Autowired
    private GroupRepository groupRepository;

    @Test
    public void existsByTitleShouldReturnTrue() {
        //assertTrue(groupRepository.existsByTitle("first_group"));
    }
}
