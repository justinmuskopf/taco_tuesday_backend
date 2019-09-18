package com.respec.tacotuesday.bl;

import com.respec.tacotuesday.domain.FullOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FullOrderRepository extends JpaRepository<FullOrder, Integer> {
}
