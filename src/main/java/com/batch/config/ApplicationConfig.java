package com.batch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration //para habilitar la configuracion de spring
public class ApplicationConfig { //configura conexion BD
    @Bean //con ello inyectamos al contenedor de spring
    public DataSource dataSource(){

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/batch_tasklet");//puerto y nombre bd
        dataSource.setUsername("root");
        dataSource.setPassword("root");

        return dataSource;
    }
}
