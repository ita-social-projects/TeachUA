package com.softserve.teachua.repository;

import com.softserve.teachua.model.District;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {
    Optional<District> findById(Long id);

    Optional<District> findByName(String name);

    Optional<District> findFirstByName(String name);

    List<District> findAllByOrderByIdAsc();

    List<District> findAllByCityName(String name);

    boolean existsByName(String name);
}
