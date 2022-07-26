package com.softserve.teachua.repository.test;

import com.softserve.teachua.model.test.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface TopicRepository extends JpaRepository<Topic, Long> {
}
