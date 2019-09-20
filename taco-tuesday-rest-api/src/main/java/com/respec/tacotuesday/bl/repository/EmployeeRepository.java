package com.respec.tacotuesday.bl.repository;

import com.respec.tacotuesday.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
