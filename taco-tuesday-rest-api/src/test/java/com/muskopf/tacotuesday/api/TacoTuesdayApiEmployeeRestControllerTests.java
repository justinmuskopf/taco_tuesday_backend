package com.muskopf.tacotuesday.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.muskopf.tacotuesday.TacoTuesdayTestHelper;
import com.muskopf.tacotuesday.bl.proc.TacoEmailer;
import com.muskopf.tacotuesday.bl.proc.TacoTuesdayResourceMapper;
import com.muskopf.tacotuesday.config.TacoTuesdayApiConfiguration;
import com.muskopf.tacotuesday.domain.Employee;
import com.muskopf.tacotuesday.resource.EmployeeResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = {
        TacoTuesdayApiEmployeeRestController.class,
        TacoTuesdayApiConfiguration.class
})
//@WebMvcTest(controllers = TacoTuesdayApiEmployeeRestController.class)
public class TacoTuesdayApiEmployeeRestControllerTests {
    @MockBean
    private TacoEmailer tacoEmailer;

    @Autowired
    private TacoTuesdayTestHelper testHelper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TacoTuesdayResourceMapper mapper;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void test_createEmployee() throws Exception {
        Employee employee = testHelper.createEmployee();
        EmployeeResource resource = mapper.map(employee);

        String resourceJson = objectMapper.writerFor(EmployeeResource.class).writeValueAsString(resource);

        MvcResult result = mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(resourceJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        EmployeeResource responseObject = objectMapper.readerFor(EmployeeResource.class).readValue(result.getResponse().getContentAsString());
        Employee returnedEmployee = mapper.map(responseObject);

        assertThat(returnedEmployee).usingRecursiveComparison().isEqualTo(employee);
    }

    @Test
    public void test_getAllEmployees() {

    }

    @Test
    public void test_getEmployeeBySlackId() {

    }

    @Test
    public void test_updateEmployee() {

    }

    @Test
    public void test_updateEmployees() {

    }
}
