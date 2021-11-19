package com.softserve.teachua.dto.news;

import antlr.collections.impl.LList;
import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SuccessCreatedNews implements Convertible {

    private Long id;

    private String title;

    private String description;

    private String urlTitleLogo;

    private Boolean isActive;

    private LocalDate date;

    private Long userId;

}
