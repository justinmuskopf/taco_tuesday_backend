package com.muskopf.tacotuesday.bl.repository;

import com.muskopf.tacotuesday.domain.IndividualOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface IndividualOrderRepository extends JpaRepository<IndividualOrder, Integer> {
    List<IndividualOrder> findByEmployeeId(Integer employeeId);

    List<IndividualOrder> findByEmployeeSlackId(String slackId);
}
