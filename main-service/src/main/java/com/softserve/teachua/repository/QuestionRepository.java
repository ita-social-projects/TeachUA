package com.softserve.teachua.repository;

import com.softserve.teachua.model.Question;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findById(Long id);

    boolean existsByTitle(String title);
}
