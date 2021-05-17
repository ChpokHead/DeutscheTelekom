package com.chpok.logiweb.service;

import com.chpok.logiweb.dto.EmployeeDto;

public interface EmployeeService {
    EmployeeDto findById(Long id);
}
