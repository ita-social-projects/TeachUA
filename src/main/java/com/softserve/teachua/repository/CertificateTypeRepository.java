package com.softserve.teachua.repository;

import com.softserve.teachua.model.CertificateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateTypeRepository extends JpaRepository<CertificateType, Integer> {

    Optional<CertificateType> findById(Integer id);

    Optional<CertificateType> findByName(String name);

    boolean existsByName(String name);

    List<CertificateType> findAll();
}
