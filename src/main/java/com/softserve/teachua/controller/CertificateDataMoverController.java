package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.model.CertificateTemplate;
import com.softserve.teachua.service.CertificateDateSqlService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@RestController
@Slf4j
public class CertificateDataMoverController implements Api {

    private final CertificateDateSqlService dateSqlService;

    @Autowired
    public CertificateDataMoverController(CertificateDateSqlService dateSqlService) {
        this.dateSqlService = dateSqlService;
    }

    @AllowedRoles(RoleData.ADMIN)
    @PutMapping("/certificate/move-data")
    public void moveData() throws SQLException {
        dateSqlService.moveData();
    }

    @AllowedRoles(RoleData.ADMIN)
    @PutMapping("/certificate/drop-columns")
    public void dropColumns() throws SQLException {
        dateSqlService.dropUnusedColumns(Arrays
                .asList("course_description", "picture_path", "project_description"));
    }

}
