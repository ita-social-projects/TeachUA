package com.softserve.teachua.service.impl;

import com.softserve.teachua.dao.AlterTableDao;
import com.softserve.teachua.service.AlterTableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class AlterTableServiceImpl implements AlterTableService {

    private final AlterTableDao alertTableDao;

    @Autowired
    public AlterTableServiceImpl(AlterTableDao alterTableDao) {
        this.alertTableDao = alterTableDao;
    }

    @Override
    public void addFeedbackCountColumnToClubsTable() {
        alertTableDao.addFeedbackCountColumnToClubsTable();
    }

    @Override
    public void addRatingColumnToCentersTable() {
        alertTableDao.addRatingColumnToCentersTable();
    }

    @Override
    public void addClubCountToCentersTable() {
        alertTableDao.addClubCountToCentersTable();
    }
}