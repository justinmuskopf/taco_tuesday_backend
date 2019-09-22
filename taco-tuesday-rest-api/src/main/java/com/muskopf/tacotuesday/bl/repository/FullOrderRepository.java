package com.muskopf.tacotuesday.bl.repository;

import com.muskopf.tacotuesday.domain.FullOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface FullOrderRepository extends JpaRepository<FullOrder, Integer> {
}
