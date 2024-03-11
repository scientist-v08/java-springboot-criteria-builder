package com.learningexample.criteriaBuilder.service.impl;

import com.learningexample.criteriaBuilder.dto.EmployeePageDto;
import com.learningexample.criteriaBuilder.dto.EmployeeSearchCriteria;
import com.learningexample.criteriaBuilder.entity.Employee;
import com.learningexample.criteriaBuilder.repository.EmployeeRepository;
import com.learningexample.criteriaBuilder.service.EmployeeService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(
            EntityManager entityManager,
            EmployeeRepository employeeRepository
    ) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Page<Employee> getEmployeesWithFilters(
            EmployeePageDto employeePageDto,
            EmployeeSearchCriteria employeeSearchCriteria
    ) {

        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class); // CriteriaQuery writes queries in a programmatic/dynamic way.
        Root<Employee> root = criteriaQuery.from(Employee.class); // Root tells us from which DB to take the data from.
        if(!(isEmptyInputString(employeeSearchCriteria.getFirstname())) || isEmptyInputString(employeeSearchCriteria.getLastname())){
            Predicate predicate = getPredicate(employeeSearchCriteria,root);
            criteriaQuery.where(predicate);
        }
        setOrder(employeePageDto, criteriaQuery, root);

        TypedQuery<Employee> typedQuery = this.entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(employeePageDto.getPageNumber() * employeePageDto.getPageSize());
        typedQuery.setMaxResults(employeePageDto.getPageSize());

        Pageable pageable = getPageable(employeePageDto);
        Predicate predicate = getPredicate(employeeSearchCriteria,root);
        Long employeesCount = getEmployeesCount(predicate);

        return new PageImpl<>(typedQuery.getResultList(), pageable, employeesCount);
    }

    private boolean isEmptyInputString(String inputString) {
        return inputString == null || inputString.isEmpty() || inputString.equals("null") || inputString.length() < 1;
    }

    @Override
    public Employee addEmployee(Employee employee){
        return employeeRepository.save(employee);
    }

    private Long getEmployeesCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Employee> countRoot = countQuery.from(Employee.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return this.entityManager.createQuery(countQuery).getSingleResult();
    }

    private Pageable getPageable(EmployeePageDto employeePageDto) {
        Sort sort = Sort.by(employeePageDto.getSortOrder(), employeePageDto.getSortBy());
        return PageRequest.of(employeePageDto.getPageNumber(), employeePageDto.getPageSize(), sort);
    }

    private void setOrder(
            EmployeePageDto employeePageDto,
            CriteriaQuery<Employee> criteriaQuery,
            Root<Employee> root
    ) {
        if(employeePageDto.getSortOrder().equals(Sort.Direction.ASC)){
            criteriaQuery.orderBy(this.criteriaBuilder.asc(root.get(employeePageDto.getSortBy())));
        }
        else {
            criteriaQuery.orderBy(this.criteriaBuilder.desc(root.get(employeePageDto.getSortBy())));
        }
    }

    private Predicate getPredicate(EmployeeSearchCriteria employeeSearchCriteria,
                                   Root<Employee> root) {
        List<Predicate> predicates = new ArrayList<>();
        if(Objects.nonNull(employeeSearchCriteria.getFirstname())){
            predicates.add(
                    this.criteriaBuilder.like(root.get("firstname"),"%"+employeeSearchCriteria.getFirstname()+"%")
            );
        }
        if(Objects.nonNull(employeeSearchCriteria.getLastname())){
            predicates.add(
                    this.criteriaBuilder.like(root.get("lastname"),"%"+employeeSearchCriteria.getLastname()+"%")
            );
        }
        return this.criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
