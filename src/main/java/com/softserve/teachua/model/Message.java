package com.softserve.teachua.model;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

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
}
