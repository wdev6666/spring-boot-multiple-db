package com.wdev.db.postgres.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "postgresEntityManagerFactoryBean",
        basePackages = {"com.wdev.db.postgres.repositories"},
        transactionManagerRef = "postgresTransactionManager"
)
public class PostgresConfig {

    @Autowired
    private Environment environment;

    //DataSource
    @Bean(name="postgresDataSource")
    @Primary
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty("postgres.datasource.driver-class-name"));
        dataSource.setUrl(environment.getProperty("postgres.datasource.url"));
        dataSource.setUsername(environment.getProperty("postgres.datasource.username"));
        dataSource.setPassword(environment.getProperty("postgres.datasource.password"));
        return dataSource;
    }

    //EntityManagerFactory
    @Bean(name = "postgresEntityManagerFactoryBean")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(){
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(dataSource());
        Map<String, String> props = new HashMap<>();
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.put("hibernate.show_ddl", "true");
        props.put("hibernate.hbm2ddl.auto", "update");
        JpaVendorAdapter vendor = new HibernateJpaVendorAdapter();
        bean.setPackagesToScan("com.wdev.db.postgres.entities");
        bean.setJpaPropertyMap(props);
        bean.setJpaVendorAdapter(vendor);
        return bean;
    }

    //platformTransactionManagerRef

    @Bean(name = "postgresTransactionManager")
    @Primary
    public PlatformTransactionManager transactionManager(){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactoryBean().getObject());
        return transactionManager;
    }

}
