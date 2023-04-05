package com.softserve.teachua.model;

import com.softserve.teachua.dto.marker.Convertible;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.hibernate.annotations.Type;

@AllArgsConstructor
@NoArgsConstructor
@Data
@With
@Entity
@Builder
@Table(name = "question")
public class Question implements Convertible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    @Type(type = "text")
    private String text;
}
