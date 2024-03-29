package com.softserve.teachua.model;

import com.softserve.teachua.dto.marker.Convertible;
import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;
import org.hibernate.annotations.ColumnDefault;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@With
@Builder
@Entity
@Table(name = "complaints")
public class Complaint implements Convertible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "club_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private Club club;

    @ManyToOne
    @JoinColumn(name = "recipient_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private User recipient;

    @Column(name = "is_active", nullable = false)
    @ColumnDefault(value = "true")
    private Boolean isActive;

    @Column(name = "has_answer", nullable = false)
    @ColumnDefault(value = "false")
    private Boolean hasAnswer;

    @Column(nullable = false)
    private String text;

    @Column(name = "answer_text")
    private String answerText;
}
