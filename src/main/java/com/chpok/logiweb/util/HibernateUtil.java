package com.chpok.logiweb.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class HibernateUtil {
    private final HikariConfig config;
    private final DataSource dataSource;

    public HibernateUtil(@Value("/db.properties") String fileConfigName) {
        this.config = new HikariConfig(fileConfigName);
        this.dataSource = new HikariDataSource(config);
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("com.chpok.logiweb.model");
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }

    @Bean
    public DataSource dataSource() {
        return dataSource;
    }

    private Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty(
                "hibernate.hbm2ddl.auto", "validate");
        hibernateProperties.setProperty(
                "hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect");
        hibernateProperties.setProperty("hibernate.show_sql", "true");

        return hibernateProperties;
    }
}
