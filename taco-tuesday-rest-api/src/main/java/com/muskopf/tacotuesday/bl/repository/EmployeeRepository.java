package com.muskopf.tacotuesday.bl.repository;

import com.muskopf.tacotuesday.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    boolean existsEmployeeByFirstNameAndLastName(String firstName, String lastName);
    boolean existsEmployeeBySlackId(String slackId);

    Employee findBySlackId(String slackId);
}
