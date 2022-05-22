package com.chpok.logiweb.service.impl;

import com.chpok.logiweb.dao.UserDao;
import com.chpok.logiweb.dto.DriverDto;
import com.chpok.logiweb.dto.EmployeeDto;
import com.chpok.logiweb.model.User;
import com.chpok.logiweb.service.UserService;
import com.chpok.logiweb.mapper.impl.DriverMapper;
import com.chpok.logiweb.mapper.impl.EmployeeMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

import java.util.NoSuchElementException;

@Component
@Transactional
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;
    @Autowired
    private DriverMapper driverMapper;
    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public User getUserById(Long id) {
        try {
            return userDao.findById(id).orElseThrow(NoSuchElementException::new);
        } catch (HibernateException | NoSuchElementException | IllegalArgumentException | NoResultException e) {
            LOGGER.error("getting user by id exception");

            throw new EntityNotFoundException();
        }
    }

    @Override
    public DriverDto getDriverByUserId(Long id) {
        try {
            return driverMapper.mapEntityToDto(userDao.findById(id)
                    .orElseThrow(NoSuchElementException::new).getDriver());
        } catch (HibernateException | NoSuchElementException | IllegalArgumentException | NoResultException e) {
            LOGGER.error("getting driver by user id exception");

            throw new EntityNotFoundException();
        }
    }

    @Override
    public EmployeeDto getEmployeeByUserId(Long id) {
        try {
            return employeeMapper.mapEntityToDto(userDao.findById(id)
                    .orElseThrow(NoSuchElementException::new).getEmployee());
        } catch (HibernateException | NoSuchElementException | IllegalArgumentException | NoResultException e) {
            LOGGER.error("getting employee by user id exception");

            throw new EntityNotFoundException();
        }
    }
}
