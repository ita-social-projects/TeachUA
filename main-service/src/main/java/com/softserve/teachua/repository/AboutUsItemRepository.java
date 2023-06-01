package com.softserve.teachua.repository;

import com.softserve.teachua.model.AboutUsItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AboutUsItemRepository extends JpaRepository<AboutUsItem, Long> {
    List<AboutUsItem> findAllByOrderByNumberAsc();
}
