package com.muskopf.tacotuesday.api;

import com.muskopf.tacotuesday.TacoTuesdayApiHelper;
import com.muskopf.tacotuesday.TacoTuesdayPersistenceInitializer;
import com.muskopf.tacotuesday.bl.proc.TacoEmailer;
import com.muskopf.tacotuesday.bl.proc.TacoTuesdayResourceMapper;
import com.muskopf.tacotuesday.config.TacoTuesdayApiConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = {TacoTuesdayApiConfiguration.class})
@ComponentScan(basePackages = "com.muskopf.tacotuesday")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class TacoTuesdayBaseRestControllerTester {
    @Autowired
    protected TacoTuesdayPersistenceInitializer persistenceHelper;
    @Autowired
    protected TacoTuesdayApiHelper apiHelper;

    @Autowired
    protected TacoTuesdayResourceMapper mapper;

    @Before
    public void setup() {
        persistenceHelper.initializeDatabase();
    }

    @Test
    public void contextLoads() {

    }
}
