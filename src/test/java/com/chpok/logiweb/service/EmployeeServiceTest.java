package com.chpok.logiweb.service;

import com.chpok.logiweb.config.TestConfig;
import com.chpok.logiweb.dao.EmployeeDao;
import com.chpok.logiweb.dto.EmployeeDto;
import com.chpok.logiweb.mapper.impl.EmployeeMapper;
import com.chpok.logiweb.model.Employee;

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
@ContextConfiguration(classes = {TestConfig.class})
@AutoConfigureEmbeddedDatabase(beanName = "dataSource")
class EmployeeServiceTest {
    @Mock
    private EmployeeDao employeeDao;
    @Mock
    private EmployeeMapper employeeMapper;
    @Autowired
    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void getEmployeeByIdShouldReturnCorrectEmployeeIfIdIsPresent() {
        final Long findingEmployeeId = 1L;

        final Employee foundEmployee = Employee.builder()
                .withId(2L)
                .withFirstName("Андрей")
                .withLastName("Прокофьев")
                .build();

        EmployeeDto foundEmployeeDto = new EmployeeDto();
        foundEmployeeDto.setId(2L);
        foundEmployeeDto.setFirstName("Андрей");
        foundEmployeeDto.setLastName("Прокофьев");

        when(employeeDao.findById(findingEmployeeId)).thenReturn(Optional.of(foundEmployee));

        when(employeeMapper.mapEntityToDto(foundEmployee)).thenReturn(foundEmployeeDto);

        final EmployeeDto actual = employeeService.getEmployeeById(findingEmployeeId);

        assertThat(actual).isEqualTo(foundEmployeeDto);
    }

    @Test
    void getEmployeeByIdShouldThrowEntityNotFoundExceptionIfIdIsNotPresent() {
        final Long findingEmployeeId = null;

        when(employeeDao.findById(findingEmployeeId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> employeeService.getEmployeeById(findingEmployeeId));
    }
}
