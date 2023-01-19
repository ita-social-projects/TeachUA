package com.softserve.teachua.repository.test;

import com.softserve.teachua.model.UserFromGoogleForms;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFromGoogleFormsRepository extends JpaRepository<UserFromGoogleForms, Long> {
    Optional<UserFromGoogleForms> findByEmail(String email);
}
