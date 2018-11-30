package com.ydx.datamigration;



import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableCaching
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class,MybatisPlusAutoConfiguration.class})
public class DatamigrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatamigrationApplication.class, args);
    }
}
