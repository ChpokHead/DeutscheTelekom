package com.chpok.logiweb.dao;

import com.chpok.logiweb.config.KafkaProducerConfig;
import com.chpok.logiweb.config.TestConfig;
import com.chpok.logiweb.model.User;
import com.chpok.logiweb.model.enums.UserRole;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class, KafkaProducerConfig.class})
@AutoConfigureEmbeddedDatabase(beanName = "dataSource")
@Sql(scripts = {"/dao/startup.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/dao/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserDaoTest {
    @Autowired
    private UserDao userDao;

    @Test
    void saveShouldCorrectlySaveUserEntity() {
        final User expected = new User("username", "password", UserRole.ROLE_EMPLOYEE);

        userDao.save(expected);

        final User actual = userDao.findById(expected.getId()).orElse(null);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findByIdShouldReturnCorrectUserEntity() {
        final User expected = new User("username", "password", UserRole.ROLE_DRIVER);

        userDao.save(expected);

        final User actual = userDao.findById(expected.getId()).orElse(null);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findByUserByUsernameShouldReturnCorrectUserEntity() {
        final User expected = new User("qwerty", "password", UserRole.ROLE_DRIVER);

        userDao.save(expected);

        final User actual = userDao.findUserByUsername(expected.getUsername()).orElse(null);

        assertThat(expected).isEqualTo(actual);
    }
}
