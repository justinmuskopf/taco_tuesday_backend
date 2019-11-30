package com.muskopf.tacotuesday.api;

import com.muskopf.tacotuesday.bl.EmployeeDAO;
import com.muskopf.tacotuesday.domain.Employee;
import com.muskopf.tacotuesday.resource.EmployeeResource;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.muskopf.tacotuesday.TacoTuesdayApiHelper.ResponseStatus.CREATED;
import static com.muskopf.tacotuesday.TacoTuesdayApiHelper.ResponseStatus.OK;
import static org.assertj.core.api.Assertions.assertThat;

public class TacoTuesdayApiEmployeeRestControllerTests extends TacoTuesdayBaseRestControllerTester {
    @Autowired
    private EmployeeDAO employeeDAO;

    private static final String EMPLOYEE_ENDPOINT = "/employees";

    /**
     * Test the happy path of the POST /employees endpoint
     */
    @Test
    public void test_createEmployee() {
        // Create new employee to persist
        Employee employee = persistenceHelper.loadObject("POST_Employees.json", Employee.class);

        // Map into resource
        EmployeeResource resource = mapper.map(employee);

        // Perform POST /employees
        EmployeeResource responseObject = apiHelper.POST(EMPLOYEE_ENDPOINT, CREATED, resource, EmployeeResource.class);

        // ID was set
        assertThat(responseObject.getId()).isNotNull();
        // Assert Employee matches original
        assertThat(responseObject).usingRecursiveComparison().ignoringFields("id").isEqualTo(resource);

        // Get persisted Employee and assert it is correct
        Employee persistedEmployee = employeeDAO.getEmployeeBySlackId(employee.getSlackId());
        assertThat(employee).usingRecursiveComparison().ignoringActualNullFields().isEqualTo(persistedEmployee);
    }

    /**
     * Test the happy path of the GET /employees endpoint
     */
    @Test
    public void test_getAllEmployees() {
        // Create employees in DB and map them into resources
        persistenceHelper.initializeDatabase();
        List<EmployeeResource> expectedResources = persistenceHelper.getPersistedEmployees()
                .stream()
                .map(e -> mapper.map(e))
                .collect(Collectors.toList());

        // Get employees from API, read into list
        List<EmployeeResource> responseObject = Arrays.asList(apiHelper.GET(EMPLOYEE_ENDPOINT, OK, true,
                EmployeeResource[].class));

        // Assert that every employee persisted by testHelper was returned from API
        expectedResources.forEach(e -> assertThat(responseObject).contains(e));
    }

    /**
     * Test the happy path of the GET /employees/{slackId} endpoint
     */
    @Test
    public void test_getEmployeeBySlackId() {
        // Create employees in DB
        persistenceHelper.initializeDatabase();
        // Get a persisted employee and its resource equivalent
        Employee employee = persistenceHelper.createEmployee();
        EmployeeResource expectedResource = mapper.map(employee);

        // GET the Employee from API by Slack ID
        EmployeeResource responseObject = apiHelper.GET(formEndpoint(employee.getSlackId()), OK,
                true, EmployeeResource.class);

        // Assert that the returned resource is what was expected
        assertThat(expectedResource).usingRecursiveComparison().isEqualTo(responseObject);
    }

    /**
     * Test the happy path of the PATCH /employees/{slackId} endpoint
     */
    @Test
    public void test_updateEmployee() {
        // Create employees in DB
        persistenceHelper.initializeDatabase();
        // Get a persisted employee and set a random nickName
        Employee employee = persistenceHelper.createEmployee();
        employee.setNickName(UUID.randomUUID().toString());

        // Map into resource
        EmployeeResource expectedResource = mapper.map(employee);

        // Perform PATH on /employees/{employee.slackId}
        EmployeeResource responseObject = apiHelper.PATCH(formEndpoint(employee.getSlackId()), OK,
                expectedResource, EmployeeResource.class);

        // Assert that the returned resource is what was expected
        assertThat(expectedResource).usingRecursiveComparison().isEqualTo(responseObject);
    }

    /**
     * Test the happy path of the PATCH /employees endpoint
     */
    @Test
    public void test_updateEmployees() {
        // Create employees in DB
        persistenceHelper.initializeDatabase();
        // Get persisted employees and set random nickNames
        List<Employee> employees = persistenceHelper.getPersistedEmployees();
        employees.forEach(e -> e.setNickName(UUID.randomUUID().toString()));

        // Convert employees to resources
        List<EmployeeResource> expectedResources = employees.stream().map(e -> mapper.map(e)).collect(Collectors.toList());

        // Perform PATCH on /employees
        List<EmployeeResource> responseObject = Arrays.asList(apiHelper.PATCH(EMPLOYEE_ENDPOINT, OK,
                expectedResources, EmployeeResource[].class));

        // Assert that all expected resources came back
        expectedResources.forEach(r -> assertThat(responseObject).contains(r));

        // Assert that all updates were properly persisted in DB
        expectedResources.forEach(r ->
                assertThat(mapper.map(employeeDAO.getEmployeeBySlackId(r.getSlackId())))
                        .usingRecursiveComparison()
                        .isEqualTo(r)
        );
    }

    /**
     * Form an endpoint URI based off of the {@code EMPLOYEE_ENDPOINT}
     *
     * @param path The path to append to the base endpoint
     * @return The formed URI
     */
    private String formEndpoint(String path) {
        return EMPLOYEE_ENDPOINT + "/" + path;
    }
}
