package com.learningexample.criteriaBuilder.dto;

import com.learningexample.criteriaBuilder.util.AppConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class EmployeePageDto {
    private int pageNumber = AppConstants.DEFAULT_PAGE_NUMBER;
    private int pageSize = AppConstants.DEFAULT_PAGE_SIZE;
    private Sort.Direction sortOrder = Sort.Direction.ASC;
    private String sortBy = AppConstants.DEFAULT_SORT_BY;
}
