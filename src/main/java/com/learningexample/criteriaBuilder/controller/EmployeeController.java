package com.learningexample.criteriaBuilder.controller;

import com.learningexample.criteriaBuilder.dto.EmployeePageDto;
import com.learningexample.criteriaBuilder.dto.EmployeeSearchCriteria;
import com.learningexample.criteriaBuilder.entity.Employee;
import com.learningexample.criteriaBuilder.service.EmployeeService;
import com.learningexample.criteriaBuilder.util.AppConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<Page<Employee>> getEmployees(
            @RequestParam(value = "pageNo",defaultValue = AppConstants.STRING_DEFAULT_PAGE_NUMBER, required = false) Integer pageNo,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.STRING_DEFAULT_PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(value = "sortBy",defaultValue = AppConstants.DEFAULT_SORT_BY,required = false)String sortBy,
            @RequestParam(value = "sortOrder",defaultValue = AppConstants.DEAFULT_SORT_ORDER,required = false)String sortOrder,
            @RequestParam(value = "searchByFirstname", required = false)String searchByFirstname,
            @RequestParam(value = "searchByLastname", required = false)String searchByLastname
    ){
        EmployeePageDto employeePageDto = new EmployeePageDto();
        employeePageDto.setPageNumber(pageNo);
        employeePageDto.setPageSize(pageSize);
        employeePageDto.setSortBy(sortBy);
        if(sortOrder=="asc"){
            employeePageDto.setSortOrder(Sort.Direction.ASC);
        }
        else if(sortOrder=="desc"){
            employeePageDto.setSortOrder(Sort.Direction.DESC);
        }
        EmployeeSearchCriteria employeeSearchCriteria = new EmployeeSearchCriteria();
        employeeSearchCriteria.setFirstname(searchByFirstname);
        employeeSearchCriteria.setLastname(searchByLastname);
        return new ResponseEntity<>(employeeService.getEmployeesWithFilters(employeePageDto,employeeSearchCriteria), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee){
        return new ResponseEntity<>(employeeService.addEmployee(employee), HttpStatus.OK);
    }
}
