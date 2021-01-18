package com.softserve.teachua.service;

import com.softserve.teachua.dto.ClubDto;
import com.softserve.teachua.dto.PageableAdvancedDto;
import org.springframework.data.domain.Pageable;

public interface ClubService {
    PageableAdvancedDto<ClubDto> getByCityIdAndSearchParam(Long id, String search, Pageable pageable);
    PageableAdvancedDto<ClubDto> getByCityId(Pageable pageable, Long id);
}
