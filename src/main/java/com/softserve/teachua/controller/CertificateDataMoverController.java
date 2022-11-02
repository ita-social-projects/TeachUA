package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.model.CertificateTemplate;
import com.softserve.teachua.service.CertificateDataMoverService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class CertificateDataMoverController implements Api {

    private CertificateDataMoverService moverService;

    @Autowired
    public CertificateDataMoverController(CertificateDataMoverService moverService) {
        this.moverService = moverService;
    }

    @AllowedRoles(RoleData.ADMIN)
    @PutMapping("/certificate/move-data")
    public List<CertificateTemplate> moveData() {
        return moverService.moveData();
    }

}
