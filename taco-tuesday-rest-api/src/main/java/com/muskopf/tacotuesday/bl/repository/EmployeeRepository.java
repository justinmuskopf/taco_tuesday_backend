package com.muskopf.tacotuesday.bl.repository;

import com.muskopf.tacotuesday.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    boolean existsEmployeeByFullName(String firstName);
    boolean existsEmployeeBySlackId(String slackId);

    Employee findBySlackId(String slackId);
}
