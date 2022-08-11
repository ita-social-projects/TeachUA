package com.softserve.teachua.service;

import com.softserve.teachua.model.CertificateDates;

public interface CertificateDatesService {
    //TODO

    CertificateDates getCertificateDatesById(Integer id);
    CertificateDates addCertificateDates(CertificateDates dates);
    CertificateDates getCertificateDatesByDuration(String duration);
    CertificateDates getCertificateDatesByDurationAndDate(String duration, String date);

}
