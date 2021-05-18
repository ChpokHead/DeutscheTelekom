package com.chpok.logiweb.service.impl;

import com.chpok.logiweb.dao.EmployeeDao;
import com.chpok.logiweb.dto.EmployeeDto;
import com.chpok.logiweb.service.EmployeeService;
import com.chpok.logiweb.mapper.impl.EmployeeMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
    private static final Logger LOGGER = LogManager.getLogger(EmployeeServiceImpl.class);

    private final EmployeeDao employeeDao;
    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeDao employeeDao, EmployeeMapper employeeMapper) {
        this.employeeDao = employeeDao;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public EmployeeDto getEmployeeById(Long id) {
        try {
            return employeeMapper.mapEntityToDto(employeeDao.findById(id).get());
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("getting employee by id exception");

            throw new EntityNotFoundException();
        }
    }
}
