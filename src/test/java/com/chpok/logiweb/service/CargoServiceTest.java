package com.chpok.logiweb.service;

import com.chpok.logiweb.config.TestConfig;
import com.chpok.logiweb.dao.CargoDao;
import com.chpok.logiweb.dto.CargoDto;
import com.chpok.logiweb.mapper.impl.CargoMapper;
import com.chpok.logiweb.model.Cargo;
import com.chpok.logiweb.model.enums.CargoStatus;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@AutoConfigureEmbeddedDatabase(beanName = "dataSource")
@Sql(scripts = {"/dao/startup.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/dao/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class CargoServiceTest {
    @Mock
    private CargoDao cargoDao;

    @Autowired
    @InjectMocks
    private CargoService cargoService;

    @Autowired
    private CargoMapper cargoMapper;

    @Test
    void getAllCargosShouldReturnCorrectCargoDtos() {
        final Cargo firstCargo = Cargo.builder()
                .withId(1L)
                .withName("Столы")
                .withWeight(126)
                .withStatus(CargoStatus.PREPARED).build();
        final Cargo secondCargo = Cargo.builder()
                .withId(2L)
                .withName("Стулья")
                .withWeight(243).withStatus(CargoStatus.PREPARED)
                .build();
        final List<CargoDto> expected =
                Arrays.asList(cargoMapper.mapEntityToDto(firstCargo), cargoMapper.mapEntityToDto(secondCargo));

        when(cargoDao.findAll()).thenReturn(Arrays.asList(firstCargo, secondCargo));

        final List<CargoDto> actual = cargoService.getAllCargos();

        assertThat(actual).isEqualTo(expected);
    }

}
