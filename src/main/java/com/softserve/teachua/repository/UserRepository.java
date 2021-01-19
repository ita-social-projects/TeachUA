package com.softserve.teachua.repository;

import com.softserve.teachua.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    User findById(Long id);

    List<User> findAll();

    User deleteById(Long id);
}
