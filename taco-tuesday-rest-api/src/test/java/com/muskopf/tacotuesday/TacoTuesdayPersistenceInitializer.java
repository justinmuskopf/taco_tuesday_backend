package com.muskopf.tacotuesday;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.muskopf.tacotuesday.bl.repository.ApiKeyRepository;
import com.muskopf.tacotuesday.bl.repository.EmployeeRepository;
import com.muskopf.tacotuesday.bl.repository.FullOrderRepository;
import com.muskopf.tacotuesday.bl.repository.IndividualOrderRepository;
import com.muskopf.tacotuesday.domain.ApiKey;
import com.muskopf.tacotuesday.domain.Employee;
import com.muskopf.tacotuesday.domain.FullOrder;
import com.muskopf.tacotuesday.domain.IndividualOrder;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Used in test classes to easily initialize the in-memory TT DB
 * and to load objects from JSON / persist them to the DB
 */
@Component
@ActiveProfiles("test")
public class TacoTuesdayPersistenceInitializer {
    private ObjectMapper objectMapper;
    private TypeFactory typeFactory;
    private Random random = new Random();

    private FullOrderRepository fullOrderRepository;
    private IndividualOrderRepository individualOrderRepository;
    private EmployeeRepository employeeRepository;
    private ApiKeyRepository apiKeyRepository;

    private boolean databaseInitialized = false;

    @Autowired
    public TacoTuesdayPersistenceInitializer(ObjectMapper objectMapper, FullOrderRepository fullOrderRepository,
                                             IndividualOrderRepository individualOrderRepository, EmployeeRepository employeeRepository,
                                             ApiKeyRepository apiKeyRepository)
    {
        this.objectMapper = objectMapper;
        this.typeFactory = objectMapper.getTypeFactory();

        this.fullOrderRepository = fullOrderRepository;
        this.individualOrderRepository = individualOrderRepository;
        this.employeeRepository = employeeRepository;
        this.apiKeyRepository = apiKeyRepository;
    }

    /**
     * Initialize the database with the objects listed in {@see Employees.json} and {@see FullOrders.json}
     * This provides what can be considered a fully-qualified DB consisting of Employees and Full/Individual Orders
     */
    public void initializeDatabase() {
        if (databaseInitialized) {
            return;
        }

        // Load all employees from JSON and persist
        List<Employee> employees = loadObjects(Employee.class);
        List<Employee> persistedEmployees = employeeRepository.saveAll(employees);
        if (persistedEmployees.size() == 0) {
            throw new RuntimeException("No Employee objects persisted!");
        }

        Map<String, Employee> persistedEmployeesBySlackId = new HashMap<>();

        // Slack ID --> Employee
        persistedEmployees.forEach(e -> persistedEmployeesBySlackId.put(e.getSlackId(), e));

        // Initialize DB with Full/Individual Orders & Employees
        List<FullOrder> fullOrders = loadObjects(FullOrder.class);
        for (FullOrder fo : fullOrders) {
            for (IndividualOrder io : fo.getIndividualOrders()) {
                Employee employee = persistedEmployeesBySlackId.get(io.getEmployee().getSlackId());
                if (employee == null) {
                    throw new RuntimeException("No persisted employee is defined for Slack ID: " + io.getEmployee().getSlackId());
                }

                io.setEmployee(employee);
            }

            fullOrderRepository.save(fo);

            // Set full order on individual and save
            fo.getIndividualOrders().forEach(io -> {
                io = individualOrderRepository.save(io);
                io.setFullOrder(fo);

                individualOrderRepository.save(io);
            });
        }

        // Save all full orders
        if (fullOrderRepository.findAll().size() == 0) {
            throw new RuntimeException("No FullOrder objects persisted!");
        }

        // Get individual orders from DB
        if (individualOrderRepository.findAll().size() == 0) {
            throw new RuntimeException("No IndividualOrder objects persisted!");
        }

        databaseInitialized = true;
    }

    /**
     * Persist a mock API key for authenticated requests
     *
     * @param apiKeyString the API Key String to persist
     */
    public void persistMockApiKey(String apiKeyString) {
        ApiKey apiKey = new ApiKey();
        apiKey.setKey(apiKeyString);

        apiKeyRepository.save(apiKey);
    }

    /**
     * Create an employee based on state of DB
     * - If {@code databaseInitialized == true} return a random, persisted employee
     * - If {@code databaseInitialized == false} load the order from JSON
     *
     * @return The loaded/persisted employee
     */
    public Employee createEmployee() {
        if (databaseInitialized) {
            return randomItem(employeeRepository.findAll());
        }

        return loadObject(Employee.class);
    }

    /**
     * Get all employees persisted on database initialization
     *
     * @return The list of persisted employees
     */
    public List<Employee> getPersistedEmployees() {
        if (!databaseInitialized) {
            throw new RuntimeException("Database is not initialized!");
        }

        return employeeRepository.findAll();
    }

    /**
     * Get all individual orders persisted on database initialization
     *
     * @return The list of persisted individual orders
     */
    public List<IndividualOrder> getPersistedIndividualOrders() {
        if (!databaseInitialized) {
            throw new RuntimeException("Database is not initialized!");
        }

        return individualOrderRepository.findAll();
    }

    /**
     * Get all full orders persisted on database initialization
     *
     * @return The list of persisted full orders
     */
    public List<FullOrder> getPersistedFullOrders() {
        if (!databaseInitialized) {
            throw new RuntimeException("Database is not initialized!");
        }

        return fullOrderRepository.findAll();
    }

    /**
     * Get a persisted employee by its Slack ID
     *
     * @param slackId The Slack ID of the employee to retrieve
     * @return The persisted employee
     */
    public Employee getPersistedEmployeeBySlackId(String slackId) {
        return employeeRepository.findBySlackId(slackId);
    }

    /**
     * Create an individual order based on state of DB
     * - If {@code databaseInitialized == true} return a random, persisted individual order
     * - If {@code databaseInitialized == false} load the order from JSON
     *
     * @return The loaded/persisted order
     */
    public IndividualOrder createIndividualOrder() {
        if (databaseInitialized) {
            return randomItem(individualOrderRepository.findAll());
        }

        return loadObject(IndividualOrder.class);
    }

    /**
     * Create a full order based on state of DB
     * - If {@code databaseInitialized == true} return a random, persisted full order
     * - If {@code databaseInitialized == false} load the order from JSON
     *
     * @return The loaded/persisted order
     */
    public FullOrder createFullOrder() {
        if (databaseInitialized) {
            return randomItem(fullOrderRepository.findAll());
        }

        return loadObject(FullOrder.class);
    }

    /**
     * A wrapper of the {@link #load(String, JavaType)} method that returns a singular object
     *
     * @param filename    The filename containing the JSON to load
     * @param objectClass The class of the object to be loaded
     * @return The loaded object
     */
    public <T> T loadObject(String filename, Class<T> objectClass) {
        return load(filename, typeFactory.constructType(objectClass));
    }

    /**
     * A wrapper of the {@link #loadObject(String, Class)} method that assumes the filename is the
     * same as the {@code objectClass}'s {@code simpleName}
     *
     * @param objectClass The class of the object to be loaded
     * @return The loaded object
     */
    public <T> T loadObject(Class<T> objectClass) {
        return loadObject(objectClass.getSimpleName() + ".json", objectClass);
    }

    /**
     * A wrapper of the {@link #load(String, JavaType)} (String, Class)} method that converts the loaded objects into a list
     *
     * @param filename    The filename containing the JSON to load
     * @param objectClass The class of the object to be loaded
     * @return A list of the loaded objects
     */
    public <T> T[] loadObjects(String filename, Class<T> objectClass) {
        return load(filename, typeFactory.constructArrayType(objectClass));
    }

    /**
     * A wrapper of the {@link #loadObjects(String, Class)} method that assumes the filename is the plural of the
     * class name (e.g. FullOrder -> FullOrders.json)
     *
     * @param objectClass The class of the objects to be loaded
     * @return A list of the loaded objects
     */
    public <T> List<T> loadObjects(Class<T> objectClass) {
        return Arrays.asList(loadObjects(objectClass.getSimpleName() + "s.json", objectClass));
    }

    /**
     * Loads object(s) of type {@code objectClass} from JSON stored in the file referenced by {@code filename}
     *
     * @param filename The filename containing the JSON to load
     * @return The loaded object(s)
     */
    public <T> T load(String filename, JavaType javaType) {
        ClassPathResource resource = new ClassPathResource("json/" + filename);
        if (!resource.exists()) {
            throw new RuntimeException("No class path resource found for " + filename);
        }

        try {
            InputStream inputStream = resource.getInputStream();
            return objectMapper.readValue(inputStream, javaType);
        } catch (IOException e) {
            throw new RuntimeException("InputStream error: " + resource.getFilename(), e);
        }
    }

    /**
     * Returns a random item from a list
     *
     * @param list The list to get the item from
     * @return The random item from the list
     */
    private <T> T randomItem(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }

    /**
     * Creates a random employee
     */
    public Employee createRandomEmployee() {
        Employee employee = new Employee();
        employee.setFullName(RandomString.make());
        employee.setNickName(RandomString.make());

        do {
            employee.setSlackId("U" + RandomString.make(8));
        } while (employeeRepository.findBySlackId(employee.getSlackId()) != null);

        return employee;
    }
}

