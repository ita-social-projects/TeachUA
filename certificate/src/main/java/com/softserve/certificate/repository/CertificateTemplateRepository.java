package com.softserve.certificate.repository;

import com.softserve.certificate.model.CertificateTemplate;
import com.softserve.certificate.model.CertificateType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateTemplateRepository extends JpaRepository<CertificateTemplate, Integer> {
    Optional<CertificateTemplate> findById(Integer id);

    Optional<CertificateTemplate> findByName(String name);

    boolean existsByNameIgnoreCase(String name);

    List<CertificateTemplate> findAll();

    List<CertificateTemplate> findByIdGreaterThanOrderByIdDesc(Integer value);

    Optional<CertificateTemplate> findFirstByCertificateTypeId(Integer id);

    boolean existsBy();

    boolean existsCertificateTemplateByCertificateType(CertificateType certificateType);

    boolean existsByFilePath(String filePath);

    CertificateTemplate getCertificateTemplateByFilePath(String filePath);
}
