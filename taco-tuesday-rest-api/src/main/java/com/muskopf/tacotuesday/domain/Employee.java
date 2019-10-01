package com.muskopf.tacotuesday.domain;

import com.muskopf.tacotuesday.bl.proc.ApiKeyGenerator;

import javax.persistence.*;
import java.util.List;

import static java.util.Objects.isNull;

@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String nickName;
    @Column
    private String slackId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "api_key_id", referencedColumnName = "id")
    private ApiKey apiKey;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.PERSIST)
    private List<IndividualOrder> orders;

    @PrePersist
    public void ensureApiKeyIsPopulated() {
        if (!isNull(this.apiKey)) {
            return;
        }

        this.apiKey = ApiKeyGenerator.generateForEmployee(this);
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public Employee firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public Employee lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getFullName() {
        if (isNull(firstName) || isNull(lastName)) {
            return null;
        }

        return firstName + " " + lastName;
    }

    public String getNickName() { return nickName; }
    public void setNickName(String nickName) { this.nickName = nickName; }
    public Employee nickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public List<IndividualOrder> getOrders() { return orders; }
    public void setOrders(List<IndividualOrder> orders) { this.orders = orders; }
    public Employee orders(List<IndividualOrder> orders) {
        this.orders = orders;
        return this;
    }

    public String getSlackId() { return slackId; }
    public void setSlackId() { this.slackId = slackId; }
}
