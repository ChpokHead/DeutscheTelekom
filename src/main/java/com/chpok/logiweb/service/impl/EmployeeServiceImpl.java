package com.chpok.logiweb.service.impl;

import com.chpok.logiweb.dao.EmployeeDao;
import com.chpok.logiweb.dto.EmployeeDto;
import com.chpok.logiweb.service.EmployeeService;
import com.chpok.logiweb.mapper.impl.EmployeeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeDao employeeDao;
    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeDao employeeDao, EmployeeMapper employeeMapper) {
        this.employeeDao = employeeDao;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public EmployeeDto findById(Long id) {
        return employeeDao.findById(id).map(employeeMapper::mapEntityToDto).get();
    }
}
