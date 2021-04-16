package com.chpok.logiweb.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DBConnector {
//    private static final Logger LOGGER = LoggerFactory.getLogger(DBConnector.class);

    private final HikariConfig config;
    private final DataSource dataSource;

    public DBConnector(@Value("/db.properties") String fileConfigName) {
        config = new HikariConfig(fileConfigName);
        dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
//            LOGGER.error("");
            throw new DataBaseRuntimeException(e);
        }
    }
}
