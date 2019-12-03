package com.muskopf.tacotuesday.api;

import com.muskopf.tacotuesday.api.validator.ApiKey;
import com.muskopf.tacotuesday.api.validator.SlackId;
import com.muskopf.tacotuesday.bl.EmployeeDAO;
import com.muskopf.tacotuesday.bl.proc.TacoEmailer;
import com.muskopf.tacotuesday.bl.proc.TacoTuesdayResourceMapper;
import com.muskopf.tacotuesday.domain.Employee;
import com.muskopf.tacotuesday.resource.EmployeeResource;
import com.muskopf.tacotuesday.resource.NewEmployeeResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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

    @PostMapping
    public ResponseEntity<EmployeeResource> createEmployee(@RequestParam(name = "apiKey") @ApiKey String apiKey,
                                                           @RequestBody @Valid NewEmployeeResource employeeResource)
    {
        log.info("POST /employees");

        Employee employee = mapper.mapToNewEmployee(employeeResource);
        employee = employeeDAO.createEmployee(employee);

        return new ResponseEntity<>(mapper.mapToEmployeeResource(employee), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResource>> getAllEmployees(@RequestParam(name = "apiKey") @ApiKey String apiKey) {
        log.info("GET /employees");

        List<Employee> employees = employeeDAO.getAllEmployees();

        return new ResponseEntity<>(mapper.mapToEmployeeResources(employees), HttpStatus.OK);
    }

    @GetMapping("/{slackId}")
    public ResponseEntity<EmployeeResource> getEmployeeBySlackId(@RequestParam(name = "apiKey") @ApiKey String apiKey,
                                                                 @PathVariable(name = "slackId") @SlackId String slackId)
    {
        log.info("GET /employees/{slackId}, Slack ID: " + slackId);

        Employee employee = employeeDAO.getEmployeeBySlackId(slackId);
        if (employee == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(mapper.mapToEmployeeResource(employee), HttpStatus.OK);
    }

    @PatchMapping("/{slackId}")
    public ResponseEntity<EmployeeResource> updateEmployee(@RequestParam(name = "apiKey") @ApiKey String apiKey,
                                                           @PathVariable(name = "slackId") @SlackId String slackId,
                                                           @RequestBody @Valid EmployeeResource employeeResource)
    {
        log.info("PATCH /employees/{slackId}, Slack ID: " + slackId);

        employeeResource.setSlackId(slackId);

        Employee employee = mapper.mapToEmployee(employeeResource);
        employee = employeeDAO.updateEmployee(employee);

        return new ResponseEntity<>(mapper.mapToEmployeeResource(employee), HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<List<EmployeeResource>> updateEmployees(@RequestParam(name = "apiKey") @ApiKey String apiKey,
                                                                  @RequestBody @Valid List<EmployeeResource> employeeResources)
    {
        log.info("PATCH /employees");

        List<Employee> employees = employeeResources.stream().map(e -> mapper.mapToEmployee(e)).collect(Collectors.toList());
        employees = employeeDAO.updateEmployees(employees);

        return new ResponseEntity<>(mapper.mapToEmployeeResources(employees), HttpStatus.OK);
    }

    boolean employeeDoesNotExist(EmployeeResource resource) {
        return !employeeDAO.employeeExistsBySlackId(resource.getSlackId());
    }
}
