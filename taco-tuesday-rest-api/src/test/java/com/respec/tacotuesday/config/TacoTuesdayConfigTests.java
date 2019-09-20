package com.respec.tacotuesday.config;

import com.respec.tacotuesday.TacoTuesdayApiApplication;
import com.respec.tacotuesday.domain.TacoType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TacoTuesdayApiApplication.class)
public class TacoTuesdayConfigTests {
    @Autowired
    TacoTuesdayConfiguration configuration;
}
