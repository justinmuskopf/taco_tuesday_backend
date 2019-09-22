package com.respec.tacotuesday.bl.repository;

import com.respec.tacotuesday.domain.FullOrder;
import com.respec.tacotuesday.domain.IndividualOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface IndividualOrderRepository extends JpaRepository<IndividualOrder, Integer> {
    List<IndividualOrder> findByEmployeeId(Integer employeeId);
}
