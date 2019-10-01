package com.muskopf.tacotuesday.api;

import com.muskopf.tacotuesday.bl.EmployeeDAO;
import com.muskopf.tacotuesday.bl.proc.ApiKeyValidator;
import com.muskopf.tacotuesday.bl.proc.TacoEmailer;
import com.muskopf.tacotuesday.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/taco-tuesday/v1/employees")
public class TacoTuesdayApiEmployeeRestController {
    private Logger logger = LoggerFactory.getLogger(TacoTuesdayApiEmployeeRestController.class);
    private EmployeeDAO employeeDAO;
    private ApiKeyValidator apiKeyValidator;
    private TacoEmailer emailer;

    @Autowired
    public TacoTuesdayApiEmployeeRestController(EmployeeDAO employeeDAO,
                                                ApiKeyValidator apiKeyValidator,
                                                TacoEmailer emailer)
    {
        this.employeeDAO = employeeDAO;
        this.apiKeyValidator = apiKeyValidator;
        this.emailer = emailer;
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestParam(name = "apiKey") String apiKey, @RequestBody Employee employee) {
        if (apiKeyValidator.isInvalidApiKey(apiKey)) {
            throw new UnrecognizedApiKeyException(apiKey);
        }

        return new ResponseEntity<>(employeeDAO.createEmployee(employee), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees(@RequestParam(name = "apiKey") String apiKey) {
        if (apiKeyValidator.isInvalidApiKey(apiKey)) {
            throw new UnrecognizedApiKeyException(apiKey);
        }

        return new ResponseEntity<>(employeeDAO.getAllEmployees(), HttpStatus.OK);
    }
}
