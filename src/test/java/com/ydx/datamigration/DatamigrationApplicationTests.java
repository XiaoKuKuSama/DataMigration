package com.ydx.datamigration;

import com.ydx.datamigration.properties.KYAgentSrvDataSourceConfigProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DatamigrationApplicationTests {

    @Autowired
    private KYAgentSrvDataSourceConfigProperties properties;

    @Test
    public void test() throws Exception {
        System.out.println(properties.toString());

    }

}
