package com.muskopf.tacotuesday.api;

import com.muskopf.tacotuesday.bl.EmployeeDAO;
import com.muskopf.tacotuesday.domain.Employee;
import com.muskopf.tacotuesday.resource.EmployeeResource;
import com.muskopf.tacotuesday.TacoTuesdayApiHelper.ApiKeyStatus;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.muskopf.tacotuesday.TacoTuesdayApiHelper.ResponseStatus.*;
import static org.assertj.core.api.Assertions.assertThat;

public class TacoTuesdayApiEmployeeRestControllerTests extends TacoTuesdayBaseRestControllerTester {
    @Autowired
    private EmployeeDAO employeeDAO;

    private static final String EMPLOYEE_ENDPOINT = "/employees";

    /**
     * Test the happy path of the POST /employees endpoint
     */
    @Test
    public void test_POST$employees_happy() {
        // Create new employee to persist
        Employee employee = persistenceHelper.createRandomEmployee(); //persistenceHelper.loadObject("POST_Employees.json", Employee.class);

        // Map into resource
        EmployeeResource resource = mapper.mapToEmployeeResource(employee);

        // Perform POST /employees
        EmployeeResource responseObject = apiHelper.POST(EMPLOYEE_ENDPOINT, CREATED, resource, ApiKeyStatus.VALID,
                EmployeeResource.class);

        // ID was set
        assertThat(responseObject.getId()).isNotNull();
        // Assert Employee matches original
        assertThat(responseObject).usingRecursiveComparison().ignoringFields("id").isEqualTo(resource);

        // Get persisted Employee and assert it is correct
        Employee persistedEmployee = employeeDAO.getEmployeeBySlackId(employee.getSlackId());
        assertThat(employee).usingRecursiveComparison().ignoringActualNullFields().isEqualTo(persistedEmployee);
    }

    /**
     * Test a sad path of the POST /employees endpoint where the employee has no Slack ID
     */
    @Test
    public void test_POST$employees_sad_noSlackId() {
        // Create new employee to persist
        Employee employee = persistenceHelper.createRandomEmployee();//persistenceHelper.loadObject("POST_Employees.json", Employee.class);

        // Map into resource
        EmployeeResource resource = mapper.mapToEmployeeResource(employee);
        resource.setSlackId(null);

        // Perform POST /employees
        TacoTuesdayExceptionResponseResource responseObject = apiHelper.POST(EMPLOYEE_ENDPOINT, BAD_REQUEST, resource,
                ApiKeyStatus.VALID, TacoTuesdayExceptionResponseResource.class);

        apiHelper.assertExceptionResponseIsValid(responseObject, HttpStatus.BAD_REQUEST, false, "Employee must have a Slack ID!");
    }

    /**
     * Test a sad path of the POST /employees endpoint where the employee has no full name
     */
    @Test
    public void test_POST$employees_sad_noFullName() {
        // Create new employee to persist
        Employee employee = persistenceHelper.createRandomEmployee();//persistenceHelper.loadObject("POST_Employees.json", Employee.class);

        // Map into resource
        EmployeeResource resource = mapper.mapToEmployeeResource(employee);
        resource.setFullName(null);

        // Perform POST /employees
        TacoTuesdayExceptionResponseResource responseObject = apiHelper.POST(EMPLOYEE_ENDPOINT, BAD_REQUEST, resource,
                ApiKeyStatus.VALID, TacoTuesdayExceptionResponseResource.class);

        apiHelper.assertExceptionResponseIsValid(responseObject, HttpStatus.BAD_REQUEST, false, "Employee must have a full name!");
    }

    /**
     * Test the sad path of the POST /employees endpoint where the employee has no full name or Slack ID
     */
    @Test
    public void test_POST$employees_sad_noSlackIdNoFullName() {
        // Create new employee to persist
        Employee employee = persistenceHelper.loadObject("POST_Employees.json", Employee.class);

        // Map into resource
        EmployeeResource resource = mapper.mapToEmployeeResource(employee);
        resource.setSlackId(null);
        resource.setFullName(null);

        // Perform POST /employees
        TacoTuesdayExceptionResponseResource responseObject = apiHelper.POST(EMPLOYEE_ENDPOINT, BAD_REQUEST, resource,
                ApiKeyStatus.VALID, TacoTuesdayExceptionResponseResource.class);

        apiHelper.assertExceptionResponseIsValid(responseObject, HttpStatus.BAD_REQUEST, false,
                new String[]{"Employee must have a full name!", "Employee must have a Slack ID!"});
    }

    /**
     * Test the happy path of the GET /employees endpoint
     */
    @Test
    public void test_GET$employees_happy() {
        // Create employees in DB and map them into resources
        persistenceHelper.initializeDatabase();
        List<EmployeeResource> expectedResources = persistenceHelper.getPersistedEmployees()
                .stream()
                .map(e -> mapper.mapToEmployeeResource(e))
                .collect(Collectors.toList());

        // Get employees from API, read into list
        List<EmployeeResource> responseObject = Arrays.asList(apiHelper.GET(EMPLOYEE_ENDPOINT, OK, ApiKeyStatus.VALID,
                EmployeeResource[].class));

        // Assert that every employee persisted by testHelper was returned from API
        expectedResources.forEach(e -> assertThat(responseObject).contains(e));
    }

    /**
     * Test the sad path of the GET /employees endpoint where no API key is provided
     */
    @Test
    public void test_GET$employees_sad_noApiKey() {
        TacoTuesdayExceptionResponseResource responseObject = apiHelper.GET(EMPLOYEE_ENDPOINT, UNAUTHORIZED, ApiKeyStatus.EMPTY,
                TacoTuesdayExceptionResponseResource.class);

        apiHelper.assertExceptionResponseIsValid(responseObject, HttpStatus.UNAUTHORIZED, false,
                new String[]{"No API Key Provided!"});
    }

    /**
     * Test the sad path of the GET /employees endpoint where the API key is wrong
     */
    @Test
    public void test_GET$employees_sad_invalidApiKey() {
        TacoTuesdayExceptionResponseResource responseObject = apiHelper.GET(EMPLOYEE_ENDPOINT, UNAUTHORIZED, ApiKeyStatus.INVALID,
                TacoTuesdayExceptionResponseResource.class);

        apiHelper.assertExceptionResponseIsValid(responseObject, HttpStatus.UNAUTHORIZED, false,
                new String[]{"Unrecognized API Key: " + apiHelper.invalidApiKey()});
    }


    /**
     * Test the happy path of the GET /employees/{slackId} endpoint
     */
    @Test
    public void test_GET$employees$slackId_happy() {
        // Create employees in DB
        persistenceHelper.initializeDatabase();
        // Get a persisted employee and its resource equivalent
        Employee employee = persistenceHelper.createEmployee();
        EmployeeResource expectedResource = mapper.mapToEmployeeResource(employee);

        // GET the Employee from API by Slack ID
        EmployeeResource responseObject = apiHelper.GET(formEndpoint(employee.getSlackId()), OK,
                ApiKeyStatus.VALID, EmployeeResource.class);

        // Assert that the returned resource is what was expected
        assertThat(expectedResource).usingRecursiveComparison().isEqualTo(responseObject);
    }

    @Test
    public void test_GET$employees$slackId_sad_invalidSlackId() {
        // Create employees in DB
        persistenceHelper.initializeDatabase();
        // Get a persisted employee and its resource equivalent
        Employee employee = persistenceHelper.createEmployee();
        EmployeeResource expectedResource = mapper.mapToEmployeeResource(employee);

        Employee randomEmployee = persistenceHelper.createRandomEmployee();

        // GET the Employee from API by Slack ID
        TacoTuesdayExceptionResponseResource responseObject = apiHelper.GET(formEndpoint(randomEmployee.getSlackId()), NOT_FOUND,
                ApiKeyStatus.VALID, TacoTuesdayExceptionResponseResource.class);

        apiHelper.assertExceptionResponseIsValid(responseObject, HttpStatus.NOT_FOUND, false,
                new String[]{"Invalid Slack ID: " + randomEmployee.getSlackId()});
    }

    /**
     * Test the sad path of the GET /employees/{slackId} endpoint where no API key is provided
     */
    @Test
    public void test_GET$employees$slackId_sad_noApiKey() {
        // Create employees in DB
        persistenceHelper.initializeDatabase();
        // Get a persisted employee and its resource equivalent
        Employee employee = persistenceHelper.createEmployee();
        EmployeeResource expectedResource = mapper.mapToEmployeeResource(employee);

        TacoTuesdayExceptionResponseResource responseObject = apiHelper.GET(formEndpoint(employee.getSlackId()), UNAUTHORIZED, ApiKeyStatus.EMPTY,
                TacoTuesdayExceptionResponseResource.class);

        apiHelper.assertExceptionResponseIsValid(responseObject, HttpStatus.UNAUTHORIZED, false,
                new String[]{"No API Key Provided!"});
    }

    /**
     * Test the sad path of the GET /employees/{slackId} endpoint where the API key is wrong
     */
    @Test
    public void test_GET$employees$slackId_sad_invalidApiKey() {
        // Create employees in DB
        persistenceHelper.initializeDatabase();
        // Get a persisted employee and its resource equivalent
        Employee employee = persistenceHelper.createEmployee();
        EmployeeResource expectedResource = mapper.mapToEmployeeResource(employee);

        TacoTuesdayExceptionResponseResource responseObject = apiHelper.GET(formEndpoint(employee.getSlackId()), UNAUTHORIZED, ApiKeyStatus.INVALID,
                TacoTuesdayExceptionResponseResource.class);

        apiHelper.assertExceptionResponseIsValid(responseObject, HttpStatus.UNAUTHORIZED, false,
                new String[]{"Unrecognized API Key: " + apiHelper.invalidApiKey()});
    }

    /**
     * Test the happy path of the PATCH /employees/{slackId} endpoint
     */
    @Test
    public void test_PATCH$employees$slackId_happy() {
        // Create employees in DB
        persistenceHelper.initializeDatabase();
        // Get a persisted employee and set a random nickName
        Employee employee = persistenceHelper.createEmployee();
        employee.setNickName(UUID.randomUUID().toString());

        // Map into resource
        EmployeeResource expectedResource = mapper.mapToEmployeeResource(employee);

        // Perform PATH on /employees/{employee.slackId}
        EmployeeResource responseObject = apiHelper.PATCH(formEndpoint(employee.getSlackId()), OK,
                expectedResource, ApiKeyStatus.VALID, EmployeeResource.class);

        // Assert that the returned resource is what was expected
        assertThat(expectedResource).usingRecursiveComparison().isEqualTo(responseObject);
    }

    /**
     * Test the happy path of the PATCH /employees endpoint
     */
    @Test
    public void test_PATCH$employees_happy() {
        // Create employees in DB
        persistenceHelper.initializeDatabase();
        // Get persisted employees and set random nickNames
        List<Employee> employees = persistenceHelper.getPersistedEmployees();
        employees.forEach(e -> e.setNickName(UUID.randomUUID().toString()));

        // Convert employees to resources
        List<EmployeeResource> expectedResources = employees.stream().map(e -> mapper.mapToEmployeeResource(e)).collect(Collectors.toList());

        // Perform PATCH on /employees
        List<EmployeeResource> responseObject = Arrays.asList(apiHelper.PATCH(EMPLOYEE_ENDPOINT, OK,
                expectedResources, ApiKeyStatus.VALID, EmployeeResource[].class));

        // Assert that all expected resources came back
        expectedResources.forEach(r -> assertThat(responseObject).contains(r));

        // Assert that all updates were properly persisted in DB
        expectedResources.forEach(r ->
                assertThat(mapper.mapToEmployeeResource(employeeDAO.getEmployeeBySlackId(r.getSlackId())))
                        .usingRecursiveComparison()
                        .isEqualTo(r)
        );
    }

    @Test
    public void test_PATCH$employees_sad_invalidEmployeeInBatch() {
        // Create employees in DB
        persistenceHelper.initializeDatabase();
        // Get persisted employees and set random nickNames
        List<Employee> employees = persistenceHelper.getPersistedEmployees();
        employees.forEach(e -> e.setNickName(UUID.randomUUID().toString()));

        Employee randomEmployee = persistenceHelper.createRandomEmployee();

        // Put invalid employee into the mix!!!
        employees.set(employees.size() / 2, randomEmployee);

        List<EmployeeResource> resources = employees.stream().map(e -> mapper.mapToEmployeeResource(e)).collect(Collectors.toList());

        // Perform PATCH on /employees
        TacoTuesdayExceptionResponseResource responseObject = apiHelper.PATCH(EMPLOYEE_ENDPOINT, BAD_REQUEST,
                resources, ApiKeyStatus.VALID, TacoTuesdayExceptionResponseResource.class);

        apiHelper.assertExceptionResponseIsValid(responseObject, HttpStatus.BAD_REQUEST, false,
                new String[]{"Invalid Slack ID: " + randomEmployee.getSlackId()});

    }

    /**
     * Test the sad path of the GET /employees/{slackId} endpoint where no API key is provided
     */
    @Test
    public void test_PATCH$employees_sad_noApiKey() {
        // Create employees in DB
        persistenceHelper.initializeDatabase();
        // Get a persisted employee and its resource equivalent
        List<Employee> employees = persistenceHelper.getPersistedEmployees();
        List<EmployeeResource> resources = employees.stream().map(e -> mapper.mapToEmployeeResource(e)).collect(Collectors.toList());

        TacoTuesdayExceptionResponseResource responseObject = apiHelper.PATCH(EMPLOYEE_ENDPOINT, UNAUTHORIZED, resources, ApiKeyStatus.EMPTY,
                TacoTuesdayExceptionResponseResource.class);

        apiHelper.assertExceptionResponseIsValid(responseObject, HttpStatus.UNAUTHORIZED, false,
                new String[]{"No API Key Provided!"});
    }

    /**
     * Test the sad path of the GET /employees/{slackId} endpoint where the API key is wrong
     */
    @Test
    public void test_PATCH$employees_sad_invalidApiKey() {
        // Create employees in DB
        persistenceHelper.initializeDatabase();
        // Get a persisted employee and its resource equivalent
        Employee employee = persistenceHelper.createEmployee();
        EmployeeResource resource = mapper.mapToEmployeeResource(employee);

        TacoTuesdayExceptionResponseResource responseObject = apiHelper.PATCH(EMPLOYEE_ENDPOINT, UNAUTHORIZED, resource, ApiKeyStatus.EMPTY,
                TacoTuesdayExceptionResponseResource.class);

        apiHelper.assertExceptionResponseIsValid(responseObject, HttpStatus.UNAUTHORIZED, false,
                new String[]{"Unrecognized API Key: " + apiHelper.invalidApiKey()});
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
