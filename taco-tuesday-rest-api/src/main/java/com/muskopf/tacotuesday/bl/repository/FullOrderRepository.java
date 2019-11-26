package com.muskopf.tacotuesday.bl.repository;

import com.muskopf.tacotuesday.domain.FullOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface FullOrderRepository extends JpaRepository<FullOrder, Integer> {
}
