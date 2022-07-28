package com.softserve.teachua.repository.test;

import com.softserve.teachua.model.test.QuestionTest;
import com.softserve.teachua.model.test.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionTestRepository extends JpaRepository<QuestionTest, Long> {
}
