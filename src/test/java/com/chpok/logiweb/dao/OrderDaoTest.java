package com.chpok.logiweb.dao;

import com.chpok.logiweb.config.KafkaProducerConfig;
import com.chpok.logiweb.config.TestConfig;
import com.chpok.logiweb.config.kafka.KafkaTopicConfig;
import com.chpok.logiweb.model.Order;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class, KafkaProducerConfig.class, KafkaTopicConfig.class})
@AutoConfigureEmbeddedDatabase(beanName = "dataSource")
@Sql(scripts = {"/dao/startup.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/dao/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class OrderDaoTest {
    @Autowired
    private OrderDao orderDao;

    @Test
    void saveShouldCorrectlySaveOrderEntity() {
        final Order expected = Order.builder()
                .withIsCompleted(false)
                .withCreationDate(LocalDate.now())
                .withDrivers(new ArrayList<>())
                .withWaypoints(new ArrayList<>())
                .build();

        orderDao.save(expected);

        final Order actual = orderDao.findById(expected.getId()).orElse(null);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findByIdShouldReturnCorrectOrderEntity() {
        final Order expected = Order.builder()
                .withIsCompleted(false)
                .withCreationDate(LocalDate.now())
                .build();

        orderDao.save(expected);

        final Order actual = orderDao.findById(expected.getId()).orElse(null);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findAllShouldReturnCorrectOrderList() {
        final Order firstSavingOrder = Order.builder()
                .withIsCompleted(false)
                .withCreationDate(LocalDate.now())
                .build();

        final Order secondSavingOrder = Order.builder()
                .withIsCompleted(true)
                .withCreationDate(LocalDate.of(2020, 5, 12))
                .build();

        orderDao.save(firstSavingOrder);

        orderDao.save(secondSavingOrder);

        final List<Order> expected = Arrays.asList(firstSavingOrder, secondSavingOrder);

        final List<Order> actual = orderDao.findAll();

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void updateShouldCorrectlyUpdateOrderEntity() {
        final Order savingOrder = Order.builder()
                .withIsCompleted(true)
                .withCreationDate(LocalDate.of(2010, 8, 4))
                .build();

        orderDao.save(savingOrder);

        final Order expected = Order.builder()
                .withId(savingOrder.getId())
                .withIsCompleted(false)
                .withCreationDate(LocalDate.of(2015, 1, 4))
                .build();


        orderDao.update(expected);

        final Order actual = orderDao.findById(savingOrder.getId()).orElse(null);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void deleteByIdShouldCorrectlyDeleteOrderEntity() {
        final Order savingOrder = Order.builder()
                .withIsCompleted(true)
                .withCreationDate(LocalDate.of(2010, 8, 4))
                .build();

        orderDao.save(savingOrder);

        orderDao.deleteById(savingOrder.getId());

        final Optional<Order> actual = orderDao.findById(savingOrder.getId());

        assertThat(actual).isEmpty();
    }

}
