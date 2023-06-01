package com.softserve.teachua.tools.dao;

public interface TaskDao {
    void dropRedundantChallenges();

    void alterTaskConstrain();

    void dropTaskConstrain();
}
