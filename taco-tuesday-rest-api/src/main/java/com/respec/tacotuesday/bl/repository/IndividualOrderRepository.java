package com.respec.tacotuesday.bl.repository;

import com.respec.tacotuesday.domain.FullOrder;
import com.respec.tacotuesday.domain.IndividualOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IndividualOrderRepository extends JpaRepository<IndividualOrder, Integer> {
    List<IndividualOrder> findByEmployeeId(Integer employeeId);
}
