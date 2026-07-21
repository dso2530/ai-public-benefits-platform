package com.govtech.platform.database.config;

import jakarta.persistence.EntityManager;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@AutoConfiguration
@ConditionalOnClass({
        DataSource.class,
        EntityManager.class,
        Flyway.class
})
@ConditionalOnProperty(prefix = "spring.datasource", name = "url")
public class PlatformDatabaseAutoConfiguration {

}