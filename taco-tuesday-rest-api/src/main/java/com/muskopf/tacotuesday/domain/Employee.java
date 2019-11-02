package com.muskopf.tacotuesday.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.muskopf.tacotuesday.bl.proc.ApiKeyGenerator;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

import static java.util.Objects.isNull;

@Entity
@Table(name = "employee")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Employee {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(updatable = false)
    @CreationTimestamp
    private Instant createdAt;
    
    @Column
    @UpdateTimestamp
    private Instant updatedAt;
    
    @Column(nullable = false)
    private String fullName;
    
    @Column(unique = true, nullable = false)
    private String slackId;
    
    @Column
    private String nickName;
    
    @Column(nullable = false)
    private boolean admin = false;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "api_key_id", referencedColumnName = "id")
    @JsonIgnore
    private ApiKey apiKey;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<IndividualOrder> orders;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public Employee fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getSlackId() { return slackId; }
    public void setSlackId() { this.slackId = slackId; }
    public Employee slackId(String slackId) {
        this.slackId = slackId;
        return this;
    }

    public String getNickName() { return nickName; }
    public void setNickName(String nickName) { this.nickName = nickName; }
    public Employee nickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public boolean isAdmin() { return admin; }
    public void setAdmin(boolean admin) { this.admin = admin; }
    public Employee admin(boolean admin) {
        this.admin = admin;
        return this;
    }

    public ApiKey getApiKey() { return apiKey; }
    public void setApiKey(ApiKey apiKey) { this.apiKey = apiKey; }

    public List<IndividualOrder> getOrders() { return orders; }
    public void setOrders(List<IndividualOrder> orders) { this.orders = orders; }
    public Employee orders(List<IndividualOrder> orders) {
        this.orders = orders;
        return this;
    }
}
