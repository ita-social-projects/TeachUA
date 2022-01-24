package com.softserve.teachua.tools.dao.jdbc;

import com.softserve.teachua.tools.dao.TaskDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TaskDaoImpl implements TaskDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TaskDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void dropRedundantChallenges() {
        jdbcTemplate.execute("DROP TABLE сhallenges");
    }

    @Override
    public void alterTaskConstrain() {
        jdbcTemplate.execute("ALTER TABLE tasks ADD CONSTRAINT task_challenge FOREIGN KEY (challenge_id) references challenges(id)");
    }

    @Override
    public void dropTaskConstrain() {
        jdbcTemplate.execute("ALTER TABLE tasks DROP CONSTRAINT fkq61bhoppvdsk4csfl8450fpek");
    }
}
