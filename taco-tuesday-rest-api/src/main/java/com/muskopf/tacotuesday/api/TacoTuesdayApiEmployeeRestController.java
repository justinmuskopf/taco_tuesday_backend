package com.muskopf.tacotuesday.api;

import com.muskopf.tacotuesday.bl.EmployeeDAO;
import com.muskopf.tacotuesday.bl.proc.ApiKeyValidator;
import com.muskopf.tacotuesday.bl.proc.TacoEmailer;
import com.muskopf.tacotuesday.bl.proc.TacoTuesdayResourceMapper;
import com.muskopf.tacotuesday.domain.Employee;
import com.muskopf.tacotuesday.resource.EmployeeResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.isEmpty;

@RestController
@RequestMapping(value = "/taco-tuesday/v1/employees")
public class TacoTuesdayApiEmployeeRestController {
    private Logger logger = LoggerFactory.getLogger(TacoTuesdayApiEmployeeRestController.class);
    private EmployeeDAO employeeDAO;
    private ApiKeyValidator apiKeyValidator;
    private TacoTuesdayResourceMapper mapper;
    private TacoEmailer emailer;

    @Autowired
    public TacoTuesdayApiEmployeeRestController(EmployeeDAO employeeDAO,
                                                ApiKeyValidator apiKeyValidator,
                                                TacoTuesdayResourceMapper mapper,
                                                TacoEmailer emailer)
    {
        this.employeeDAO = employeeDAO;
        this.apiKeyValidator = apiKeyValidator;
        this.mapper = mapper;
        this.emailer = emailer;
    }

    @PostMapping
    public ResponseEntity<EmployeeResource> createEmployee(@RequestParam(name = "apiKey") @NotEmpty String apiKey,
                                                           @RequestBody @NotEmpty EmployeeResource employeeResource)
    {
        validateApiKey(apiKey);

        Employee employee = mapper.map(employeeResource);
        employee = employeeDAO.createEmployee(employee);

        return new ResponseEntity<>(mapper.map(employee), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResource>> getAllEmployees(@RequestParam(name = "apiKey") @NotEmpty String apiKey) {
        validateApiKey(apiKey);

        List<Employee> employees = employeeDAO.getAllEmployees();

        return new ResponseEntity<>(mapper.mapToEmployeeResources(employees), HttpStatus.OK);
    }

    @GetMapping("/{slackId}")
    public ResponseEntity<EmployeeResource> getEmployeeBySlackId(@RequestParam(name = "apiKey") @NotEmpty String apiKey,
                                                                 @PathVariable(name = "slackId") @NotEmpty String slackId)
    {
        validateApiKey(apiKey);

        Employee employee = employeeDAO.getEmployeeBySlackId(slackId);

        return new ResponseEntity<>(mapper.map(employee), HttpStatus.OK);
    }

    @PatchMapping("/{slackId}")
    public ResponseEntity<EmployeeResource> updateEmployee(@RequestParam(name = "apiKey") @NotEmpty String apiKey,
                                                           @PathVariable(name = "slackId") @NotEmpty String slackId,
                                                           @RequestBody @NotEmpty EmployeeResource employeeResource)
    {
        validateApiKey(apiKey);

        if (isEmpty(employeeResource.getSlackId())) {
            employeeResource.setSlackId(slackId);
        }

        Employee employee = mapper.map(employeeResource);
        employee = employeeDAO.updateEmployee(employee);

        return new ResponseEntity<>(mapper.map(employee), HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<List<EmployeeResource>> updateEmployees(@RequestParam(name = "apiKey") @NotEmpty String apiKey,
                                                                  @RequestBody @NotEmpty List<EmployeeResource> employeeResources)
    {
        validateApiKey(apiKey);

        List<Employee> employees = employeeResources.stream().map(e -> mapper.map(e)).collect(Collectors.toList());
        employees = employeeDAO.updateEmployees(employees);

        return new ResponseEntity<>(mapper.mapToEmployeeResources(employees), HttpStatus.OK);
    }

    private void validateApiKey(String apiKey) {
        if (apiKeyValidator.isInvalidApiKey(apiKey)) {
            throw new UnrecognizedApiKeyException(apiKey);
        }
    }
}
