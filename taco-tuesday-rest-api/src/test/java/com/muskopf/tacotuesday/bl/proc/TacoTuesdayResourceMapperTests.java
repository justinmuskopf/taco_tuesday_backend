package com.muskopf.tacotuesday.bl.proc;

import com.muskopf.tacotuesday.TacoTuesdayTestHelper;
import com.muskopf.tacotuesday.config.TacoTuesdayApiConfiguration;
import com.sun.media.sound.SoftTuning;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        TacoTuesdayResourceMapperImpl.class,
        TacoTuesdayApiConfiguration.class,
        TacoTuesdayTestHelper.class
})
public class TacoTuesdayResourceMapperTests {
    @Autowired
    private TacoTuesdayResourceMapper mapper;

    @Autowired
    private TacoTuesdayTestHelper testHelper;

    @Before
    public void setup() {
        testHelper.initializeDatabase();
    }

    @Test
    public void test() {
        System.out.println(testHelper.createFullOrder());
    }

}
