package com.withNoa.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DatabaseConfigTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void whenContextLoads_thenDataSourceBeanIsAvailable() {
        DataSource dataSource = context.getBean(DataSource.class);
        assertNotNull(dataSource, "DataSource bean should be present in the context");
    }
}
