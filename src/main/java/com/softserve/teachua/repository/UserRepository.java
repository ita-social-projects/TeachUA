package com.softserve.teachua.repository;

import com.softserve.teachua.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByVerificationCode(String verificationCode);

    Optional<User> findById(Long id);

    List<User> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);

    boolean existsByEmail(String email);

    Optional<List<User>> findByRoleName(String roleName);


    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.children WHERE u.id = :id")
    Optional<User> findByIdFetchChildren(Long id);
}
