package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.ClubDto;
import com.softserve.teachua.dto.PageableAdvancedDto;
import com.softserve.teachua.entity.Club;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.service.ClubService;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClubServiceIml implements ClubService {

    private ClubRepository clubRepository;
    private ModelMapper modelMapper;

    @Autowired
    public ClubServiceIml(ClubRepository clubRepository, ModelMapper modelMapper) {
        this.clubRepository = clubRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PageableAdvancedDto<ClubDto> getByCityIdAndSearchParam(Long id, String search, Pageable pageable) {
        Page<Club> pages = clubRepository.getClubByCityIdAndClubNamePageble(pageable,id,search);
        return buildPageableAdvancedDto(pages);
    }

    @Override
    public PageableAdvancedDto<ClubDto> getByCityId(Pageable pageable, Long id) {
        Page<Club> pages = clubRepository.getClubByCityIdPageble(pageable,id);
        return buildPageableAdvancedDto(pages);
    }

    private PageableAdvancedDto<ClubDto> buildPageableAdvancedDto(Page<Club> clubPage){
        List<ClubDto> clubDtos = clubPage.stream()
            .map(club -> modelMapper.map(club,ClubDto.class))
            .collect(Collectors.toList());
        return new PageableAdvancedDto<>(
            clubDtos,
            clubPage.getTotalElements(),
            clubPage.getPageable().getPageNumber(),
            clubPage.getTotalPages(),
            clubPage.getNumber(),
            clubPage.hasPrevious(),
            clubPage.hasNext(),
            clubPage.isFirst(),
            clubPage.isLast()
        );
    }
}
