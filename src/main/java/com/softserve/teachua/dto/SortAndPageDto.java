package com.softserve.teachua.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SortAndPageDto {
    private boolean asc = false;
    @Min(value = 0)
    private int page = 0;
    @Min(value = 0)
    private int size = 5;
    private int totalPages;
}
