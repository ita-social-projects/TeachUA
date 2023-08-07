package com.softserve.teachua.model;

import com.softserve.teachua.dto.marker.Convertible;
import java.time.LocalDateTime;
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
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;
import org.hibernate.annotations.CreationTimestamp;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
@Data
@Entity
@Table(name = "messages")
public class Message implements Convertible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "club_id", referencedColumnName = "id")
    @ToString.Exclude
    private Club club;

    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    @ToString.Exclude
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", referencedColumnName = "id")
    @ToString.Exclude
    private User recipient;

    @Column(length = 1500)
    private String text;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_answered")
    private Boolean isAnswered;
}
