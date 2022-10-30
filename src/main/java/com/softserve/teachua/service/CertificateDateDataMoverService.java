package com.softserve.teachua.service;

import com.softserve.teachua.model.CertificateTemplate;

import java.util.List;

/**
 * This interface contains all needed methods to move data from certificate_dates to certificate_templates.
 */
public interface CertificateDateDataMoverService {

    /**
     * This method move misplaced data from certificate_dates table to certificate_templates table and returns a list of updated Certificate Templates
     * {@code List<CertificateTemplate>} of templates
     *
     * @return new {@code List<CertificateTemplate>}
     */
    List<CertificateTemplate> moveData();

}
