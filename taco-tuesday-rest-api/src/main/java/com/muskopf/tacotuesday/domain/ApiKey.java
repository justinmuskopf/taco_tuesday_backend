package com.muskopf.tacotuesday.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class ApiKey {
    @Id
    @GeneratedValue
    private Integer id;

    @OneToOne(mappedBy = "apiKey")
    @JsonIgnore
    private Employee employee;

    @Column(name = "api_key")
    private String key;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    public ApiKey key(String key) {
        this.key = key;
        return this;
    }
}
