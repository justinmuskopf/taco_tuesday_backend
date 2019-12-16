package com.muskopf.tacotuesday.api;

import com.muskopf.tacotuesday.api.validator.ApiKey;
import com.muskopf.tacotuesday.api.validator.SlackId;
import com.muskopf.tacotuesday.bl.EmployeeDAO;
import com.muskopf.tacotuesday.bl.proc.TacoEmailer;
import com.muskopf.tacotuesday.bl.proc.TacoTuesdayResourceMapper;
import com.muskopf.tacotuesday.domain.Employee;
import com.muskopf.tacotuesday.resource.EmployeeResource;
import com.muskopf.tacotuesday.resource.NewEmployeeResource;
import com.muskopf.tacotuesday.resource.UpdateEmployeeBatchResource;
import com.muskopf.tacotuesday.resource.UpdateEmployeeResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(value = "/taco-tuesday/v1/employees")
public class TacoTuesdayApiEmployeeRestController {
    private EmployeeDAO employeeDAO;
    private TacoTuesdayResourceMapper mapper;
    private TacoEmailer emailer;

    @Autowired
    public TacoTuesdayApiEmployeeRestController(EmployeeDAO employeeDAO,
                                                TacoTuesdayResourceMapper mapper,
                                                TacoEmailer emailer)
    {
        this.employeeDAO = employeeDAO;
        this.mapper = mapper;
        this.emailer = emailer;
    }

    @ApiOperation(value = "Create a new Employee")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Employee Created", response = EmployeeResource.class),
            @ApiResponse(code = 400, message = "Request Body is Invalid", response = TacoTuesdayExceptionResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = TacoTuesdayExceptionResponse.class)
    })
    @PostMapping
    public ResponseEntity<EmployeeResource> createEmployee(@RequestParam(name = "apiKey") @ApiKey String apiKey,
                                                           @RequestBody @Valid NewEmployeeResource employeeResource)
    {
        log.info("POST /employees");

        Employee employee = mapper.mapNewEmployeeResourceToEmployee(employeeResource);
        employee = employeeDAO.createEmployee(employee);

        return new ResponseEntity<>(mapper.mapEmployeeToEmployeeResource(employee), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Retrieve all Existing Employees")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retrieved Successfully", response = EmployeeResource[].class),
            @ApiResponse(code = 401, message = "Invalid API Key", response = TacoTuesdayExceptionResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = TacoTuesdayExceptionResponse.class)
    })
    @GetMapping
    public ResponseEntity<List<EmployeeResource>> getAllEmployees(@RequestParam(name = "apiKey") @ApiKey String apiKey) {
        log.info("GET /employees");

        List<Employee> employees = employeeDAO.getAllEmployees();

        return new ResponseEntity<>(mapper.mapEmployeesToEmployeeResources(employees), HttpStatus.OK);
    }

    @ApiOperation(value = "Retrieve an Employee by Slack ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retrieved Successfully", response = EmployeeResource.class),
            @ApiResponse(code = 401, message = "Invalid API Key", response = TacoTuesdayExceptionResponse.class),
            @ApiResponse(code = 404, message = "No Such Employee Exists", response = TacoTuesdayExceptionResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = TacoTuesdayExceptionResponse.class)
    })
    @GetMapping("/{slackId}")
    public ResponseEntity<EmployeeResource> getEmployeeBySlackId(@RequestParam(name = "apiKey") @ApiKey String apiKey,
                                                                 @PathVariable(name = "slackId") @SlackId String slackId)
    {
        log.info("GET /employees/{slackId}, Slack ID: " + slackId);

        Employee employee = employeeDAO.getEmployeeBySlackId(slackId);

        return new ResponseEntity<>(mapper.mapEmployeeToEmployeeResource(employee), HttpStatus.OK);
    }

    @ApiOperation(value = "Update an Existing Employee")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Updated Successfully", response = EmployeeResource.class),
            @ApiResponse(code = 400, message = "Invalid Slack ID", response = TacoTuesdayExceptionResponse.class),
            @ApiResponse(code = 401, message = "Invalid API Key", response = TacoTuesdayExceptionResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = TacoTuesdayExceptionResponse.class)
    })
    @PatchMapping("/{slackId}")
    public ResponseEntity<EmployeeResource> updateEmployee(@RequestParam(name = "apiKey") @ApiKey String apiKey,
                                                           @PathVariable(name = "slackId") @SlackId String slackId,
                                                           @RequestBody @Valid UpdateEmployeeResource employeeResource)
    {
        log.info("PATCH /employees/{slackId}, Slack ID: " + slackId);

        Employee employee = mapper.mapUpdateEmployeeResourceToEmployee(employeeResource);
        employee.setSlackId(slackId);

        employee = employeeDAO.updateEmployee(employee);

        return new ResponseEntity<>(mapper.mapEmployeeToEmployeeResource(employee), HttpStatus.OK);
    }

    @ApiOperation(value = "Update a Batch of Existing Employees")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Updated Successfully", response = EmployeeResource.class),
            @ApiResponse(code = 400, message = "Invalid Request Body", response = TacoTuesdayExceptionResponse.class),
            @ApiResponse(code = 401, message = "Invalid API Key", response = TacoTuesdayExceptionResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = TacoTuesdayExceptionResponse.class)
    })
    @PatchMapping
    public ResponseEntity<List<EmployeeResource>> updateEmployees(@RequestParam(name = "apiKey") @ApiKey String apiKey,
                                                                  @RequestBody @Valid List<UpdateEmployeeBatchResource> employeeResources)
    {
        log.info("PATCH /employees");

        List<Employee> employees = mapper.mapUpdateEmployeeBatchResourcesToEmployees(employeeResources);
        employees = employeeDAO.updateEmployees(employees);

        return new ResponseEntity<>(mapper.mapEmployeesToEmployeeResources(employees), HttpStatus.OK);
    }
}
