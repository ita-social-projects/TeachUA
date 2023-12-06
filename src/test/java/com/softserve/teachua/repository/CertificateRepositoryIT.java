package com.softserve.teachua.repository;

import com.softserve.teachua.model.Certificate;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class CertificateRepositoryIT {
    private static final String USER_EMAIL = "admin@gmail.com";
    private static final String USER_NAME = "Власник Сертифікату";
    private static final LocalDate UPDATE_DATE = LocalDate.now();
    private final Certificate successfullySentCertificate = Certificate.builder()
            .userName(USER_NAME).sendToEmail(USER_EMAIL).serialNumber(3010000001L)
            .sendStatus(true).updateStatus(UPDATE_DATE).build();
    private final Certificate failedSentCertificate = Certificate.builder()
            .userName(USER_NAME).sendToEmail(USER_EMAIL).serialNumber(3010000002L)
            .sendStatus(false).updateStatus(UPDATE_DATE).build();
    private final Certificate failedSentCertificateUpdateStatusNull = Certificate.builder()
            .userName(USER_NAME).sendToEmail(USER_EMAIL).serialNumber(3010000003L)
            .sendStatus(false).updateStatus(null).build();

    private final Certificate untouchedCertificate = Certificate.builder()
            .userName(USER_NAME).sendToEmail(USER_EMAIL).serialNumber(null)
            .sendStatus(null).updateStatus(null).build();
    private final Certificate notDownloadableCertificate = Certificate.builder()
            .userName(USER_NAME).sendToEmail(USER_EMAIL).serialNumber(null)
            .sendStatus(true).updateStatus(UPDATE_DATE).build();
    private final Certificate foreignCertificate = Certificate.builder()
            .userName("Інший Користувач").sendToEmail("user@gmail.com").serialNumber(3020000010L)
            .sendStatus(true).updateStatus(UPDATE_DATE).build();
    @Autowired
    private CertificateRepository certificateRepository;
    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void setUp() {
        entityManager.persist(successfullySentCertificate);
        entityManager.persist(failedSentCertificate);
        entityManager.persist(failedSentCertificateUpdateStatusNull);
        entityManager.persist(untouchedCertificate);
        entityManager.persist(notDownloadableCertificate);
        entityManager.persist(foreignCertificate);
    }

    @Test
    void findAllForDownloadShouldNotReturnSentCertificateWithSerialNumberNull() {
        List<Certificate> expected = Arrays.asList(
                successfullySentCertificate,
                failedSentCertificate,
                failedSentCertificateUpdateStatusNull,
                untouchedCertificate
        );

        List<Certificate> actual = certificateRepository.findAllForDownload(USER_EMAIL);

        assertThat(notDownloadableCertificate.getId()).isNotNull();
        assertThat(actual)
                .isNotEmpty()
                .doesNotContain(foreignCertificate)
                .doesNotContain(notDownloadableCertificate)
                .isEqualTo(expected);
    }
}
