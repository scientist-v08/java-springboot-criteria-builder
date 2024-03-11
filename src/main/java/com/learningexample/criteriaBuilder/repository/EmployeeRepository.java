package com.learningexample.criteriaBuilder.repository;

import com.learningexample.criteriaBuilder.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {
}
