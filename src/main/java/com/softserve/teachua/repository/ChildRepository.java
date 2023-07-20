package com.softserve.teachua.repository;

import com.softserve.teachua.model.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChildRepository extends JpaRepository<Child, Long> {
    List<Child> getAllByParentId(Long parentId);
}
