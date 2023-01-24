package com.softserve.teachua.repository;

import com.softserve.teachua.model.CertificateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateTypeRepository extends JpaRepository<CertificateType, Integer> {
    boolean existsByCodeNumber(Integer codeNumber);

    boolean existsByNameIgnoreCase(String name);

    CertificateType findCertificateTypeByCodeNumber(Integer codeNumber);
}
