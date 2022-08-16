package com.softserve.teachua.service;

/**
 * This interface contains method to manage scheduling for sending certificate via email.
 */

public interface ScheduleSendService {
    /**
     * The method schedule sending of certificate.
     */
    void sendCertificateWithScheduler();
}
