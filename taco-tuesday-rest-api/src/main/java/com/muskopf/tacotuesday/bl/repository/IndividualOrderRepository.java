package com.muskopf.tacotuesday.bl.repository;

import com.muskopf.tacotuesday.domain.IndividualOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface IndividualOrderRepository extends JpaRepository<IndividualOrder, Integer> {
    List<IndividualOrder> findByEmployeeId(Integer employeeId);
    List<IndividualOrder> findByEmployeeSlackId(String slackId);
}
