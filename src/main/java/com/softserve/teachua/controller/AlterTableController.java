package com.softserve.teachua.controller;

import com.softserve.teachua.service.AlterTableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AlterTableController {

    private final AlterTableService alterTableService;

    @Autowired
    public AlterTableController(AlterTableService alterTableService) {
        this.alterTableService = alterTableService;
    }

    @GetMapping("/alert/addFeedbackCountColumnToClubsTable")
    public void addFeedbackCountColumnToClubsTable(){
        alterTableService.addFeedbackCountColumnToClubsTable();
    }

    @GetMapping("/alert/addRatingColumnToCentersTable")
    public void addRatingColumnToCentersTable(){
        alterTableService.addRatingColumnToCentersTable();
    }

    @GetMapping("/alert/addClubCountToCentersTable")
    public void addClubCountToCentersTable(){
        alterTableService.addClubCountToCentersTable();
    }

}
