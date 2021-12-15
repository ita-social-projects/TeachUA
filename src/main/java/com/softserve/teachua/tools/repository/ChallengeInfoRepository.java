package com.softserve.teachua.tools.repository;

import com.softserve.teachua.tools.transmodel.ChallengeTransfer;

import java.util.Arrays;
import java.util.List;

public class ChallengeInfoRepository {
    public static List<ChallengeTransfer> getChallengesList() {
        return Arrays.asList(
                ChallengeTransfer.builder()
                        .name("MyRepoChallenge")
                        .title("Title for my challenge from repo")
                        .sortNumber(1L)
                        .description("My repo get it to me. My repo get it to me. My repo get it to me.")
                        .picture("image_for_prod/NASE_Earth.jpg")
                        .isActive(true)
                        .build()
                , ChallengeTransfer.builder()
                        .name("MyRepoTask_2")
                        .title("Title for my challenge from repo")
                        .sortNumber(2L)
                        .description("My repo get it to me. My repo get it to me. My repo get it to me.")
                        .description("My repo get it to me twice")
                        .picture("image_for_prod/ubuntu_dragon.jpg")
                        .isActive(true)
                        .build()
        );
    }
}
