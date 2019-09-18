package com.respec.tacotuesday.bl;

import com.respec.tacotuesday.domain.IndividualOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndividualOrderRepository extends JpaRepository<IndividualOrder, Integer> {
}
