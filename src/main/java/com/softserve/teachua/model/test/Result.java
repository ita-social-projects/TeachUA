package com.softserve.teachua.model.test;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.UserFromGoogleForms;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "user_from_google_forms", referencedColumnName = "id")
    private UserFromGoogleForms userFromGoogleForms;

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
