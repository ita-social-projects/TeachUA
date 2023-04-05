package com.softserve.teachua.model.test;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.User;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"id"})
@Entity
@Table(name = "results")
public class Result implements Convertible {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private int grade;

    @ManyToOne(optional = false)
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    private Test test;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "test_finish_time")
    private LocalDateTime testFinishTime;

    @Column(name = "test_start_time")
    private LocalDateTime testStartTime;

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "result",
               cascade = CascadeType.PERSIST)
    private Set<QuestionHistory> questionHistories = new HashSet<>();

    public void addQuestionHistory(QuestionHistory questionHistory) {
        questionHistories.add(questionHistory);
        questionHistory.setResult(this);
    }

    public Set<QuestionHistory> getQuestionHistories() {
        return Collections.unmodifiableSet(questionHistories);
    }
}
