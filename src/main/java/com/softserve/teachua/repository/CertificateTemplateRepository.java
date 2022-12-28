package com.softserve.teachua.repository;

import com.softserve.teachua.model.CertificateTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateTemplateRepository extends JpaRepository<CertificateTemplate, Integer> {

    Optional<CertificateTemplate> findById(Integer id);

    Optional<CertificateTemplate> findByName(String name);

    boolean existsByName(String name);

    List<CertificateTemplate> findAll();

    List<CertificateTemplate> findByIdGreaterThanOrderByIdDesc(Integer value);

    Optional<CertificateTemplate> findByCertificateType(Integer type);

    boolean existsBy();

    boolean existsCertificateTemplateByCertificateType(Integer certificateType);

    boolean existsByFilePath(String filePath);

    CertificateTemplate getCertificateTemplateByFilePath(String filePath);
}
