package com.chpok.logiweb.service;

import com.chpok.logiweb.dto.DriverDto;
import com.chpok.logiweb.dto.EmployeeDto;
import com.chpok.logiweb.model.User;

public interface UserService {
    User getUserById(Long id);
    DriverDto getDriverByUserId(Long id);
    EmployeeDto getEmployeeByUserId(Long id);
}
