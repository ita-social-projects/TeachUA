package com.softserve.teachua.service;

import com.softserve.teachua.model.CertificateDates;
import com.softserve.teachua.model.CertificateTemplate;

/**
 * This interface contains all needed methods to manage certificate dates.
 */

public interface CertificateDatesService {

    /**
     * The method returns {@link CertificateDates} by id
     *
     * @param id - put certificate dates id
     * @return new {@code CertificateDates}
     */
    CertificateDates getCertificateDatesById(Integer id);

    /**
     * The method returns {@link CertificateDates} by duration
     *
     * @param duration - put certificate dates duration
     * @return new {@code CertificateDates}
     */
    CertificateDates getCertificateDatesByDuration(String duration);

    /**
     * The method returns {@link CertificateDates} by duration and date
     *
     * @param duration - put certificate dates duration
     * @param date - put certificate dates date
     * @return new {@code CertificateDates}
     */
    CertificateDates getCertificateDatesByDurationAndDate(String duration, String date);

    /**
     * The method returns {@link CertificateDates} if dates successfully added
     *
     * @param dates - put body of {@code CertificateDates}
     * @return new {@code CertificateDates}
     */
    CertificateDates addCertificateDates(CertificateDates dates);
}
