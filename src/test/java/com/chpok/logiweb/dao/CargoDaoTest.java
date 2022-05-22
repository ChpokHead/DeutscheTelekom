package com.chpok.logiweb.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.chpok.logiweb.config.TestConfig;
import com.chpok.logiweb.model.Cargo;
import com.chpok.logiweb.model.enums.CargoStatus;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@AutoConfigureEmbeddedDatabase(beanName = "dataSource")
@Sql(scripts = {"/dao/startup.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/dao/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class CargoDaoTest {
    @Autowired
    private CargoDao cargoDao;

    @Test
    void saveShouldCorrectlySaveCargoEntity() {
        final Cargo savingCargo = Cargo.builder()
                .withName("Стол")
                .withWeight(400)
                .withStatus(CargoStatus.PREPARED)
                .build();

        cargoDao.save(savingCargo);

        assertThat(savingCargo).isEqualTo(cargoDao.findById(savingCargo.getId()).get());
    }

    @Test
    void findByNameShouldReturnCorrectCargoEntity() {
        final Cargo savingCargo = Cargo.builder()
                .withName("Шкаф")
                .withWeight(125)
                .withStatus(CargoStatus.PREPARED)
                .build();

        cargoDao.save(savingCargo);

        assertThat(savingCargo).isEqualTo(cargoDao.findByName(savingCargo.getName()).get(0));
    }

    @Test
    void findByIdShouldReturnCorrectCargoEntity() {
        final Cargo savingCargo = Cargo.builder()
                .withName("Холодильники")
                .withWeight(500)
                .withStatus(CargoStatus.DELIVERED)
                .build();

        cargoDao.save(savingCargo);

        assertThat(savingCargo).isEqualTo(cargoDao.findById(savingCargo.getId()).get());
    }

    @Test
    void findAllShouldReturnCorrectCargoEntities() {
        final Cargo firstSavingCargo = Cargo.builder()
                .withName("Стулья")
                .withWeight(126)
                .withStatus(CargoStatus.PREPARED)
                .build();

        final Cargo secondSavingCargo = Cargo.builder()
                .withName("Бочки")
                .withWeight(18)
                .withStatus(CargoStatus.DELIVERED)
                .build();

        final List<Cargo> savingCargosList = Arrays.asList(firstSavingCargo, secondSavingCargo);

        cargoDao.save(firstSavingCargo);
        cargoDao.save(secondSavingCargo);

        assertThat(savingCargosList).isEqualTo(cargoDao.findAll());
    }

    @Test
    void updateShouldCorrectlyUpdateCargoEntity() {
        final Cargo savingCargo = Cargo.builder()
                .withName("Стулья")
                .withWeight(126)
                .withStatus(CargoStatus.PREPARED)
                .build();

        cargoDao.save(savingCargo);

        final Cargo updatedCargo = Cargo.builder()
                .withId(savingCargo.getId())
                .withName("Стулья")
                .withWeight(190)
                .withStatus(CargoStatus.DELIVERED)
                .build();

        cargoDao.update(updatedCargo);

        assertThat(updatedCargo).isEqualTo(cargoDao.findById(savingCargo.getId()).get());
    }

}
