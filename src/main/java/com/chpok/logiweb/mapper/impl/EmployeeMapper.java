package com.chpok.logiweb.mapper.impl;

import com.chpok.logiweb.dto.EmployeeDto;
import com.chpok.logiweb.mapper.Mapper;
import com.chpok.logiweb.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper implements Mapper<Employee, EmployeeDto> {
    public EmployeeDto mapEntityToDto(Employee employee) {
        final EmployeeDto employeeDto = new EmployeeDto();

        employeeDto.setId(employee.getId());

        employeeDto.setFirstName(employee.getFirstName());

        employeeDto.setLastName(employee.getLastName());

        employeeDto.setCurrentPosition(employee.getCurrentPosition());

        return employeeDto;
    }

    public Employee mapDtoToEntity(EmployeeDto employeeDto) {
        return Employee.builder()
                .withId(employeeDto.getId())
                .withFirstName(employeeDto.getFirstName())
                .withLastName(employeeDto.getLastName())
                .withCurrentPosition(employeeDto.getCurrentPosition())
                .build();
    }
}
