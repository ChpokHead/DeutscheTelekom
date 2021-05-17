package com.chpok.logiweb.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.chpok.logiweb.config.TestConfig;
import com.chpok.logiweb.model.Employee;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@AutoConfigureEmbeddedDatabase(beanName = "dataSource")
@Sql(scripts = {"/dao/startup.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/dao/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class EmployeeDaoTest {
    @Autowired
    private EmployeeDao employeeDao;

    @Test
    void saveShouldCorrectlySaveEmployeeEntity() {
        final Employee expected = Employee.builder()
                .withFirstName("Артем")
                .withLastName("Савельев")
                .withCurrentPosition("Логист")
                .build();

        employeeDao.save(expected);

        final Employee actual = employeeDao.findById(expected.getId()).orElse(null);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findByIdShouldReturnCorrectEmployeeEntity() {
        final Employee expected = Employee.builder()
                .withFirstName("Александр")
                .withLastName("Савельев")
                .withCurrentPosition("Младший инженер")
                .build();

        employeeDao.save(expected);

        final Employee actual = employeeDao.findById(expected.getId()).orElse(null);

        assertThat(actual).isEqualTo(expected);
    }
}
