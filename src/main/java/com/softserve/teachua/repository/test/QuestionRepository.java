package com.softserve.teachua.repository.test;

import com.softserve.teachua.model.User;
import com.softserve.teachua.model.test.Question;
import com.softserve.teachua.model.test.QuestionCategory;
import com.softserve.teachua.model.test.QuestionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("testQuestionRepository")
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findByTitle(String title);

    List<Question> findQuestionsByCreator(User creator);

    List<Question> findQuestionsByQuestionType(QuestionType questionType);

    List<Question> findQuestionsByQuestionCategory(QuestionCategory questionCategory);

    @Query("SELECT qt.question FROM QuestionTest qt WHERE qt.test.id = :id")
    List<Question> findQuestionsByTestId(@Param("id") Long testId);
}
