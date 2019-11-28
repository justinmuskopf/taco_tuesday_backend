package com.muskopf.tacotuesday;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.muskopf.tacotuesday.bl.repository.EmployeeRepository;
import com.muskopf.tacotuesday.bl.repository.FullOrderRepository;
import com.muskopf.tacotuesday.bl.repository.IndividualOrderRepository;
import com.muskopf.tacotuesday.domain.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component
@ActiveProfiles("test")
@EnableAutoConfiguration
public class TacoTuesdayTestHelper {
    private ObjectMapper objectMapper;
    private TypeFactory typeFactory;
    private Random random = new Random();

    private FullOrderRepository fullOrderRepository;
    private IndividualOrderRepository individualOrderRepository;
    private EmployeeRepository employeeRepository;

    private List<Taco> tacos;
    private List<Employee> loadedEmployees;
    private List<IndividualOrder> loadedIndividualOrders;
    private List<FullOrder> loadedFullOrders;

    private boolean databaseInitialized = false;

    @Autowired
    public TacoTuesdayTestHelper(TacoPriceList priceList, ObjectMapper objectMapper, FullOrderRepository fullOrderRepository,
                                 IndividualOrderRepository individualOrderRepository, EmployeeRepository employeeRepository)
    {
        this.tacos = priceList.getPriceList();
        this.objectMapper = objectMapper;
        this.typeFactory = objectMapper.getTypeFactory();
        this.fullOrderRepository = fullOrderRepository;
        this.individualOrderRepository = individualOrderRepository;
        this.employeeRepository = employeeRepository;
    }

    public void initializeDatabase() {
        // Load all employees from JSON
        List<Employee> employees = Arrays.asList(loadObjects(Employee.class));
        loadedEmployees = employeeRepository.saveAll(employees);

        // Slack ID --> Employee
        Map<String, Employee> employeesBySlackId = new HashMap<>();
        loadedEmployees.forEach(e -> employeesBySlackId.put(e.getSlackId(), e));

        // Load all FullOrders from JSON and set their employees to persisted ones
        List<FullOrder> fullOrders = Arrays.asList(loadObjects(FullOrder.class));
        for (FullOrder fo : fullOrders) {
            for (IndividualOrder io : fo.getIndividualOrders()) {
                Employee employee = employeesBySlackId.get(io.getEmployee().getSlackId());
                if (employee == null) {
                    throw new RuntimeException("No valid employee is defined for Slack ID: " + io.getEmployee().getSlackId());
                }

                io.setEmployee(employee);
            }
        }

        loadedFullOrders = fullOrderRepository.saveAll(fullOrders);
        loadedIndividualOrders = individualOrderRepository.findAll();

        databaseInitialized = true;
    }

    public Employee createEmployee() {
        if (databaseInitialized)
            return loadedEmployees.get(randomIndex(loadedEmployees));

        return loadObject(Employee.class);
    }

    public IndividualOrder createIndividualOrder() {
        if (databaseInitialized)
            return loadedIndividualOrders.get(randomIndex(loadedIndividualOrders));

        return loadObject(IndividualOrder.class);
    }

    public FullOrder createFullOrder() {
        if (databaseInitialized)
            return loadedFullOrders.get(randomIndex(loadedFullOrders));

        return loadObject(FullOrder.class);
    }

    public List<Taco> createTacos() {
        return tacos;
    }

    /**
     * A wrapper of the {@link #load(String, JavaType)} method that returns a singular object
     *
     * @param filename The filename containing the JSON to load
     * @param objectClass The class of the object to be loaded
     *
     * @return The loaded object
     */
    private <T> T loadObject(String filename, Class<T> objectClass) {
        return load(filename, typeFactory.constructType(objectClass));
    }

    /**
     * A wrapper of the {@link #loadObject(String, Class)} method that assumes the filename is the
     * same as the {@code objectClass}'s {@code simpleName}
     *
     * @param objectClass The class of the object to be loaded
     *
     * @return The loaded object
     */
    private <T> T loadObject(Class<T> objectClass) {
        return loadObject(objectClass.getSimpleName() + ".json", objectClass);
    }

    /**
     * A wrapper of the {@link #loadObjects(String, Class)} method that converts the loaded objects into a list
     *
     * @param filename The filename containing the JSON to load
     * @param objectClass The class of the object to be loaded
     *
     * @return A list of the loaded objects
     */
    private <T> T[] loadObjects(String filename, Class<T> objectClass) {
        return load(filename, typeFactory.constructArrayType(objectClass));
    }

    /**
     * A wrapper of the {@link #loadObjects(Class)} method that assumes the filename is the plural of the
     * class name (e.g. FullOrder -> FullOrders.json)
     *
     * @param objectClass The class of the objects to be loaded
     *
     * @return A list of the loaded objects
     */
    private <T> T[] loadObjects(Class<T> objectClass) {
        return loadObjects(objectClass.getSimpleName() + "s.json", objectClass);
    }

    /**
     * Loads object(s) of type {@code objectClass} from JSON stored in the file referenced by {@code filename}
     *
     * @param filename The filename containing the JSON to load
     *
     * @return The loaded object(s)
     */
    private <T> T load(String filename, JavaType javaType) {
        ClassPathResource resource = new ClassPathResource("json/" + filename);
        if (!resource.exists()) {
            throw new RuntimeException("No class path resource found for " + filename);
        }

        try {
            InputStream inputStream = resource.getInputStream();
            return objectMapper.readValue(inputStream, javaType);
        } catch (IOException e) {
            throw new RuntimeException("InputStream error: " + resource.getFilename() + ", " + e.getMessage());
        }
    }

    private int randomIndex(List<? extends Object> list) {
        return random.nextInt(list.size());
    }
}
