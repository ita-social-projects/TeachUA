package com.softserve.teachua.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softserve.teachua.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);
}
