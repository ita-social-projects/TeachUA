package com.softserve.teachua.tools.repository;

import com.softserve.teachua.dto.task.TaskProfile;

import java.util.Arrays;
import java.util.List;

public class TaskInfoRepository {
    public static List<TaskProfile> getTasksList() {
        return Arrays.asList(
                TaskProfile.builder()
                        .name("MyRepoTask")
                        .headerText("It is the best way")
                        .description("My repo get it to me")
                        .picture("image_for_prod/NASE_Earth.jpg")
                        .challengeId(15L)
                        .build()
                , TaskProfile.builder()
                        .name("MyRepoTask_2")
                        .headerText("SECOND: It is the best way")
                        .description("My repo get it to me twice")
                        .picture("image_for_prod/ubuntu_dragon.jpg")
                        .challengeId(15L)
                        .build()
        );
    }
}
