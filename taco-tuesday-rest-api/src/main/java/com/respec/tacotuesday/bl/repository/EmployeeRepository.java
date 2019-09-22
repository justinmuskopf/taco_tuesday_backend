package com.respec.tacotuesday.bl.repository;

import com.respec.tacotuesday.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    boolean existsEmployeeByFirstNameAndLastName(String firstName, String lastName);
}
