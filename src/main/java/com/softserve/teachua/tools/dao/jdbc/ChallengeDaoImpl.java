package com.softserve.teachua.tools.dao.jdbc;

import com.softserve.teachua.tools.dao.ChallengeDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ChallengeDaoImpl implements ChallengeDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ChallengeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void alterChallengeDescription() {
        jdbcTemplate.execute("ALTER TABLE сhallenges ALTER COLUMN description TYPE varchar(30000)");
    }

    @Override
    public void renameChallengeTable() {
        jdbcTemplate.execute("ALTER TABLE сhallenges RENAME TO challenges");
    }
}
