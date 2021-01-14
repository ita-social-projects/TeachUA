package com.softserve.teachua.service;

import com.softserve.teachua.dto.ClubDto;
import java.util.List;

public interface ClubService {
    List<ClubDto> getAll();
    List<ClubDto> getByCityId(Long id);
    List<ClubDto> getByCityIdAndSearchParam(Long id, String search);
}
