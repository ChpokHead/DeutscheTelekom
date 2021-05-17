package com.chpok.logiweb.service.impl;

import com.chpok.logiweb.dao.UserDao;
import com.chpok.logiweb.dto.DriverDto;
import com.chpok.logiweb.dto.EmployeeDto;
import com.chpok.logiweb.model.User;
import com.chpok.logiweb.service.UserService;
import com.chpok.logiweb.mapper.impl.DriverMapper;
import com.chpok.logiweb.mapper.impl.EmployeeMapper;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final DriverMapper driverMapper;
    private final EmployeeMapper employeeMapper;

    public UserServiceImpl(UserDao userDao, DriverMapper driverMapper, EmployeeMapper employeeMapper) {
        this.userDao = userDao;
        this.driverMapper = driverMapper;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public User getUserById(Long id) {
        return userDao.findById(id).get();
    }

    @Override
    public DriverDto getDriverByUserId(Long id) {
        return driverMapper.mapEntityToDto(userDao.findById(id).get().getDriver());
    }

    @Override
    public EmployeeDto getEmployeeByUserId(Long id) {
        return employeeMapper.mapEntityToDto(userDao.findById(id).get().getEmployee());
    }
}
