package com.chpok.logiweb.service.impl;

import com.chpok.logiweb.dao.EmployeeDao;
import com.chpok.logiweb.dto.EmployeeDto;
import com.chpok.logiweb.service.EmployeeService;
import com.chpok.logiweb.mapper.impl.EmployeeMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
    private static final Logger LOGGER = LogManager.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public EmployeeDto getEmployeeById(Long id) {
        try {
            return employeeMapper.mapEntityToDto(employeeDao.findById(id).orElseThrow(NoSuchElementException::new));
        } catch (HibernateException | NoSuchElementException e) {
            LOGGER.error("getting employee by id exception");

            throw new EntityNotFoundException();
        }
    }
}
