package com.softserve.teachua.service;

import com.softserve.teachua.model.CertificateDates;
import java.util.Optional;

/**
 * This interface contains all needed methods to manage certificate dates.
 */

public interface CertificateDatesService {
    boolean exists(CertificateDates certificateDates);

    /**
     * The method returns entity of {@code CertificateDates} found by id.
     *
     * @param id put CertificateDates id
     * @return new {@code CertificateDates}
     */
    CertificateDates getCertificateDatesById(Integer id);

    /**
     * The method returns {@code CertificateDates} if dates successfully added.
     *
     * @param dates put body of {@code CertificateDates}
     * @return new {@code CertificateDates}
     */
    CertificateDates addCertificateDates(CertificateDates dates);

    /**
     * The method returns entity of {@code CertificateDates} found by all fields excluding the {@code id}.
     *
     * @param certificateDates put CertificateDates
     * @return new {@code Optional<CertificateDates>}
     */
    Optional<CertificateDates> findCertificateDates(CertificateDates certificateDates);

    /**
     * The method returns got or created entity of {@code CertificateDates} by all fields excluding the {@code id}.
     *
     * @param certificateDates put CertificateDates
     * @return new {@code CertificateDates}
     */
    CertificateDates getOrCreateCertificateDates(CertificateDates certificateDates);
}
