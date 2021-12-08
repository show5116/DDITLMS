package com.example.dditlms.domain.repository.sanctn;

import com.example.dditlms.domain.dto.EmployeeDTO;

import java.util.List;

public interface EmployeeRepositoryCustom {

    List<EmployeeDTO> viewDetails(Long userNumber);
}
