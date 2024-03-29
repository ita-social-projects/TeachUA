package com.softserve.teachua.model;

import com.softserve.teachua.dto.marker.Convertible;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;
import org.hibernate.annotations.CreationTimestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@With
@Builder
@Entity
@Table(name = "feedbacks")
public class Feedback implements Convertible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Float rate;

    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime date;

    @Column(length = 1500)
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "club_id", referencedColumnName = "id")
    @ToString.Exclude
    private Club club;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id", referencedColumnName = "id")
    private Feedback parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Feedback> replies = new ArrayList<>();

    public void addReply(Feedback reply) {
        if (replies == null) {
            replies = new ArrayList<>();
        }
        this.replies.add(reply);
        reply.setParentComment(this);
    }
}
