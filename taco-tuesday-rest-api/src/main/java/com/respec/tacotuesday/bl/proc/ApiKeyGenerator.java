package com.respec.tacotuesday.bl.proc;

import com.respec.tacotuesday.domain.ApiKey;
import com.respec.tacotuesday.domain.Employee;

import java.util.UUID;

import static java.util.Objects.isNull;

public class ApiKeyGenerator {
    private static String getRandomKeyString(Employee employee) {
        String employeeFullName = employee.getFullName();
        if (isNull(employeeFullName)) {
            throw new IllegalArgumentException("No API Key for Employee ID " + employee.getId() + " was generated! (No Full Name)");
        }

        UUID randomKey = UUID.nameUUIDFromBytes(employeeFullName.getBytes());

        return randomKey.toString();
    }

    public static ApiKey generateForEmployee(Employee employee) {
        String randomKey = getRandomKeyString(employee);

        ApiKey apiKey = new ApiKey();
        apiKey.setEmployee(employee);
        apiKey.setKey(randomKey);

        return apiKey;
    }
}
