package org.skypro.recommendation_service.configuration;

import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(
        basePackages = "org.skypro.recommendation_service.repository",
        entityManagerFactoryRef = "dynamicRulesEntityManagerFactory",
        transactionManagerRef = "dynamicRulesTransactionManager"
)
public class DynamicRulesDataSourceConfiguration {

    @Primary
    @Bean(name = "defaultDataSourceProperties")
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties defaultDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "defaultDataSource")
    public DataSource defaultDataSource() {
        return defaultDataSourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "dynamicRulesDataSourceProperties")
    @ConfigurationProperties("spring.datasource.dynamic-rules")
    public DataSourceProperties dynamicRulesDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "dynamicRulesDataSource")
    public DataSource dynamicRulesDataSource() {
        return dynamicRulesDataSourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "dynamicRulesEntityManagerFactory")
    @DependsOn("dynamicRulesLiquibase")
    public LocalContainerEntityManagerFactoryBean dynamicRulesEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dynamicRulesDataSource());
        em.setPackagesToScan("org.skypro.recommendation_service.model.entity");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "validate");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.setProperty("hibernate.jdbc.lob.non_contextual_creation", "true");
        em.setJpaProperties(properties);

        return em;
    }

    @Bean(name = "dynamicRulesTransactionManager")
    public PlatformTransactionManager dynamicRulesTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(dynamicRulesEntityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean
    public SpringLiquibase dynamicRulesLiquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dynamicRulesDataSource());
        liquibase.setChangeLog("classpath:db/changelog/db.changelog-master.xml");
        liquibase.setDefaultSchema("public");
        return liquibase;
    }
}
