package com.chpok.logiweb.service;

import com.chpok.logiweb.config.KafkaProducerConfig;
import com.chpok.logiweb.config.TestConfig;
import com.chpok.logiweb.dao.UserDao;
import com.chpok.logiweb.dto.DriverDto;
import com.chpok.logiweb.dto.EmployeeDto;
import com.chpok.logiweb.mapper.impl.DriverMapper;
import com.chpok.logiweb.mapper.impl.EmployeeMapper;
import com.chpok.logiweb.model.Driver;
import com.chpok.logiweb.model.Employee;
import com.chpok.logiweb.model.User;
import com.chpok.logiweb.model.enums.UserRole;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {TestConfig.class, KafkaProducerConfig.class})
@AutoConfigureEmbeddedDatabase(beanName = "dataSource")
class UserServiceTest {
    @Mock
    private UserDao userDao;
    @Mock
    private DriverMapper driverMapper;
    @Mock
    private EmployeeMapper employeeMapper;
    @Autowired
    @InjectMocks
    private UserService userService;

    @Test
    void getUserByIdShouldReturnCorrectUserIfIdIsPresent() {
        final Long findingUserId = 1L;

        final User foundUser = new User("test", "testp", UserRole.ROLE_EMPLOYEE);

        when(userDao.findById(findingUserId)).thenReturn(Optional.of(foundUser));

        final User actual = userService.getUserById(findingUserId);

        assertThat(actual).isEqualTo(foundUser);
    }

    @Test
    void getUserByIdShouldThrowEntityNotFoundExceptionIfIdIsNotPresent() {
        final Long findingUserId = null;

        when(userDao.findById(findingUserId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> userService.getUserById(findingUserId));
    }

    @Test
    void getDriverByUserIdShouldReturnCorrectDriverIfIdIsPresent() {
        final Long findingUserId = 1L;

        final Driver driver = Driver.builder()
                .withId(1L)
                .withFirstName("Альберт")
                .withLastName("Альин")
                .build();

        final DriverDto driverDto = new DriverDto();

        driverDto.setPersonalNumber(driver.getId());
        driverDto.setFirstName(driver.getFirstName());
        driverDto.setLastName(driver.getLastName());

        final User foundUser = new User("test", "test1", UserRole.ROLE_DRIVER);
        foundUser.setDriver(driver);

        when(userDao.findById(findingUserId)).thenReturn(Optional.of(foundUser));

        when(driverMapper.mapEntityToDto(driver)).thenReturn(driverDto);

        final DriverDto actual = userService.getDriverByUserId(findingUserId);

        assertThat(actual).isEqualTo(driverDto);
    }

    @Test
    void getDriverByUserIdShouldThrowEntityNotFoundExceptionIfIdIsNotPresent() {
        final Long findingUserId = null;

        when(userDao.findById(findingUserId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> userService.getDriverByUserId(findingUserId));
    }

    @Test
    void getEmployeeByUserIdShouldReturnCorrectEmployeeIfIdIsPresent() {
        final Long findingUserId = 1L;

        final Employee employee = Employee.builder()
                .withId(1L)
                .withFirstName("Альберт")
                .withLastName("Альин")
                .build();

        final EmployeeDto employeeDto = new EmployeeDto();

        employeeDto.setId(employee.getId());
        employeeDto.setFirstName(employee.getFirstName());
        employeeDto.setLastName(employee.getLastName());

        final User foundUser = new User("test", "test1", UserRole.ROLE_EMPLOYEE);
        foundUser.setEmployee(employee);

        when(userDao.findById(findingUserId)).thenReturn(Optional.of(foundUser));

        when(employeeMapper.mapEntityToDto(employee)).thenReturn(employeeDto);

        final EmployeeDto actual = userService.getEmployeeByUserId(findingUserId);

        assertThat(actual).isEqualTo(employeeDto);
    }

    @Test
    void getEmployeeByUserIdShouldThrowEntityNotFoundExceptionIfIdIsNotPresent() {
        final Long findingUserId = null;

        when(userDao.findById(findingUserId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> userService.getEmployeeByUserId(findingUserId));
    }
}
