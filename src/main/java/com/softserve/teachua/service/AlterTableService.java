package com.softserve.teachua.service;

import org.springframework.stereotype.Service;

public interface AlterTableService {

    void addFeedbackCountColumnToClubsTable();

    void addRatingColumnToCentersTable();

    void addClubCountToCentersTable();

}
