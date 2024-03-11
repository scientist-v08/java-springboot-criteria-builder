package com.learningexample.criteriaBuilder.service;

import com.learningexample.criteriaBuilder.dto.EmployeePageDto;
import com.learningexample.criteriaBuilder.dto.EmployeeSearchCriteria;
import com.learningexample.criteriaBuilder.entity.Employee;
import org.springframework.data.domain.Page;

public interface EmployeeService {
    public Page<Employee> getEmployeesWithFilters(EmployeePageDto employeePageDto, EmployeeSearchCriteria employeeSearchCriteria);
    public Employee addEmployee(Employee employee);
}
