package com.softserve.teachua.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class AlterTableDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AlterTableDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addFeedbackCountColumnToClubsTable(){
        jdbcTemplate.execute("ALTER TABLE clubs ADD COLUMN feedback_count BIGINT NOT NULL default 0");
    }

    public void addRatingColumnToCentersTable(){
        jdbcTemplate.execute("ALTER TABLE centers ADD COLUMN rating BIGINT NOT NULL default 0");
    }

    public void addClubCountToCentersTable(){
        jdbcTemplate.execute("ALTER TABLE centers ADD COLUMN club_count BIGINT NOT NULL default 0");
    }

}
