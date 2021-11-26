package com.softserve.teachua.repository;

import com.softserve.teachua.model.BannerItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerItemRepository extends JpaRepository<BannerItem, Long> {
    List<BannerItem> findAllByOrderBySequenceNumberAsc();
}
