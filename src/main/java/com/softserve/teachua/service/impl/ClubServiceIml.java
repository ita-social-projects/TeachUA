package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.ClubDto;
import com.softserve.teachua.entity.Activities;
import com.softserve.teachua.entity.Club;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.service.ClubService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<ClubDto> getAll() {
        List<Club> clubs = clubRepository.findAll();
        List<ClubDto> clubDtos = new ArrayList<>();
        for (Club club:clubs){
            clubDtos.add(modelMapper.map(club, ClubDto.class));
        }
        return clubDtos;
    }

    @Override
    public List<ClubDto> getByCityId(Long id) {
        List<Club> clubs = clubRepository.getClubByCityId(id);
        List<ClubDto> clubDtos = new ArrayList<>();
        for (Club club: clubs) {
            clubDtos.add(modelMapper.map(club,ClubDto.class));
        }
        return clubDtos;
    }

    @Override
    public List<ClubDto> getByCityIdAndSearchParam(Long id, String search) {
        List<Club> clubs = clubRepository.getClubByCityId(id);
        Set<Club> clubSet = new HashSet<>();
        for (Club club:clubs){
            Set<Activities> activitiesSet = club.getActivities();
            for (Activities activity :activitiesSet){
                if (activity.getActivity().contains(search)){
                    clubSet.add(club);
                }
            }
            if (club.getClubName().contains(search)){
                clubSet.add(club);
            }
        }
        List<ClubDto> clubDtos = new ArrayList<>();
        for (Club club: clubSet) {
            clubDtos.add(modelMapper.map(club,ClubDto.class));
        }
        return clubDtos;
    }
}
