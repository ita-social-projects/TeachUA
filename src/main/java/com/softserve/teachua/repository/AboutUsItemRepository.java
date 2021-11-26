package com.softserve.teachua.repository;

import com.softserve.teachua.model.AboutUsItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AboutUsItemRepository extends JpaRepository<AboutUsItem, Long> {
    List<AboutUsItem> findAllByOrderByNumberAsc();
}
