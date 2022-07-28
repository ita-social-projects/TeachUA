package com.softserve.teachua.repository.test;

import com.softserve.teachua.model.test.Answer;
import com.softserve.teachua.model.test.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findAllByQuestion(Question question);
    List<Answer> findAllByQuestionId(Long id);
}
