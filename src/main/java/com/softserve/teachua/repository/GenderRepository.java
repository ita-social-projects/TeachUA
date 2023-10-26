package com.softserve.teachua.repository;

import com.softserve.teachua.model.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GenderRepository extends JpaRepository<Gender, Long> {

    Gender findGenderByValue(String value);

}
