package com.softserve.teachua.service;

import com.softserve.teachua.model.CertificateDates;
import com.softserve.teachua.model.CertificateTemplate;

/**
 * This interface contains all needed methods to manage certificate dates.
 */

public interface CertificateDatesService {

    /**
     * The method returns entity of {@code CertificateDates} found by id
     *
     * @param id - put CertificateDates id
     * @return new {@code CertificateDates}
     */
    CertificateDates getCertificateDatesById(Integer id);

    /**
     * The method returns entity of {@code CertificateDates} found by duration
     *
     * @param duration - put CertificateDates duration
     * @return new {@code CertificateDates}
     */
    CertificateDates getCertificateDatesByDuration(String duration);

    /**
     * The method returns entity of {@code CertificateDates} found by duration and date
     *
     * @param duration - put CertificateDates duration
     * @param date - put CertificateDates date
     * @return new {@code CertificateDates}
     */
    CertificateDates getCertificateDatesByDurationAndDate(String duration, String date);

    /**
     * The method returns {@code CertificateDates} if dates successfully added
     *
     * @param dates - put body of {@code CertificateDates}
     * @return new {@code CertificateDates}
     */
    CertificateDates addCertificateDates(CertificateDates dates);
}
