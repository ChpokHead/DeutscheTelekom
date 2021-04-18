package com.chpok.logiweb.dao.impl;

import com.chpok.logiweb.dao.DBConnector;
import com.chpok.logiweb.dao.TruckDao;
import com.chpok.logiweb.model.Truck;
import com.chpok.logiweb.model.TruckStatus;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Optional;

@Component
public class TruckDaoImpl extends AbstractCrudDao<Truck> implements TruckDao {
    private static final String SAVE_QUERY = "INSERT INTO truck (reg_number, drivers_shift, capacity, status, location) values(?, ?, ?, ?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM truck WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM truck";
    private static final String UPDATE_QUERY = "UPDATE truck SET reg_number = ?, drivers_shift = ?, capacity = ?, status = CAST(? AS truck_status), location = ? WHERE id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM truck WHERE id = ?";
    private static final String FIND_BY_REG_NUMBER_QUERY = "SELECT * FROM truck WHERE reg_number = ?";

    public TruckDaoImpl(DBConnector connector) {
        super(connector, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, UPDATE_QUERY, DELETE_BY_ID_QUERY);
    }

    @Override
    protected Truck mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return Truck.builder()
                .withId(resultSet.getLong("id"))
                .withRegNumber(resultSet.getString("reg_number"))
                .withDriversShift(resultSet.getShort("drivers_shift"))
                .withCapacity(resultSet.getShort("capacity"))
                .withStatus(TruckStatus.valueOf(resultSet.getString("status")))
                .withLocation(resultSet.getString("location"))
                .build();
    }

    @Override
    protected void insert(PreparedStatement preparedStatement, Truck entity) throws SQLException {
        preparedStatement.setLong(1, entity.getId());
        preparedStatement.setString(2, entity.getRegNumber());
        preparedStatement.setShort(3, entity.getDriversShift());
        preparedStatement.setShort(4, entity.getCapacity());
        preparedStatement.setString(5, entity.getStatus().toString());
        preparedStatement.setString(6, entity.getLocation());
    }

    @Override
    protected void updateValues(PreparedStatement preparedStatement, Truck entity) throws SQLException {
        preparedStatement.setString(1, entity.getRegNumber());
        preparedStatement.setShort(2, entity.getDriversShift());
        preparedStatement.setShort(3, entity.getCapacity());
        preparedStatement.setString(4, entity.getStatus().toString());
        preparedStatement.setString(5, entity.getLocation());
        preparedStatement.setLong(6, entity.getId());
    }

    @Override
    public Optional<Truck> findByRegNumber(String regNumber) {
        return findByStringParam(regNumber, FIND_BY_REG_NUMBER_QUERY);
    }
}
