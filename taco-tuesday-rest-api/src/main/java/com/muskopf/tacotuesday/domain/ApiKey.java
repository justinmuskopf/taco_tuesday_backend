package com.muskopf.tacotuesday.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@EqualsAndHashCode(exclude = "employee")
public class ApiKey {
    @Id
    @GeneratedValue
    private Integer id;

    @OneToOne(mappedBy = "apiKey")
    @JsonIgnore
    private Employee employee;

    @Column(name = "api_key")
    private String key;
}
