package com.softserve.teachua.tools.service.transfer_impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.challenge.SuccessCreatedChallenge;
import com.softserve.teachua.model.Challenge;
import com.softserve.teachua.repository.ChallengeRepository;
import com.softserve.teachua.tools.FileUtils;
import com.softserve.teachua.tools.dao.ChallengeDao;
import com.softserve.teachua.tools.repository.ChallengeInfoRepository;
import com.softserve.teachua.tools.service.ChallengeTransferService;
import com.softserve.teachua.tools.transmodel.ChallengeTransfer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChallengeTransferServiceImpl implements ChallengeTransferService {
    private final String ALTER_DESCRIPTION = "Challenges description was altered successfully," +
            " now description is varchar(30 000)";
    private final String RENAME_DESCRIPTION = "Table сhallenges was renamed to challenges " +
            "(before renaming 'c' - was ukraine letter)";
    private final FileUtils fileUtils;
    private final DtoConverter dtoConverter;
    private final ChallengeRepository challengeRepository;
    private final ChallengeDao challengeDao;

    public ChallengeTransferServiceImpl(
            FileUtils fileUtils,
            DtoConverter dtoConverter, ChallengeRepository challengeRepository, ChallengeDao challengeDao) {
        this.fileUtils = fileUtils;
        this.dtoConverter = dtoConverter;
        this.challengeRepository = challengeRepository;
        this.challengeDao = challengeDao;
    }

    @Override
    public List<SuccessCreatedChallenge> createChallengesFromFile(String filePath) {
        return createTasks(fileUtils.readFromFile(filePath, ChallengeTransfer.class));
    }

    @Override
    public List<SuccessCreatedChallenge> createChallengesFromRepository() {
        return createTasks(ChallengeInfoRepository.getChallengesList());
    }

    private List<SuccessCreatedChallenge> createTasks(List<ChallengeTransfer> challenges) {
        return challenges
                .stream()
                .map(createChallenge -> {
                    createChallenge.setPicture(fileUtils.moveImage(createChallenge.getPicture(), "challenges"));
                    return dtoConverter.convertToEntity(createChallenge, Challenge.builder().build()).withId(null);
                }).map(challengeRepository::save)
                .map(challenge -> (SuccessCreatedChallenge) dtoConverter.convertToDto(challenge, SuccessCreatedChallenge.class))
                .collect(Collectors.toList());
    }

    @Override
    public String alterDescriptionColumn() {
        challengeDao.alterChallengeDescription();
        return ALTER_DESCRIPTION;
    }

    @Override
    public String renameChallengeTable() {
        challengeDao.renameChallengeTable();
        return RENAME_DESCRIPTION;
    }
}
