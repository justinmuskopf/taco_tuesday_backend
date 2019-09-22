package com.respec.tacotuesday.bl.repository;

import com.respec.tacotuesday.domain.FullOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface FullOrderRepository extends JpaRepository<FullOrder, Integer> {
}
