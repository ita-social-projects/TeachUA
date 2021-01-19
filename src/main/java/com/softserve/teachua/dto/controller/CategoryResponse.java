package com.softserve.teachua.dto.controller;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CategoryResponse {
    private Long id;
    private String name;
}
