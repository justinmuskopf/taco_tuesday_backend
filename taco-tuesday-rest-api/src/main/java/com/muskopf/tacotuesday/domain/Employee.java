package com.muskopf.tacotuesday.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

import static reactor.util.StringUtils.isEmpty;

@Data
@Entity
@Table(name = "employee")
@EqualsAndHashCode(callSuper = true)
public class Employee extends DomainObject {
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
    private ApiKey apiKey;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private List<IndividualOrder> orders;

    public void merge(Employee employee) {
        String fullName = employee.getFullName();
        if (!isEmpty(fullName) && !fullName.equals(this.fullName)) {
            this.fullName = fullName;
        }

        String nickName = employee.getNickName();
        if (!isEmpty(nickName) && !nickName.equals(this.nickName)) {
            this.nickName = nickName;
        }

        boolean isAdmin = employee.isAdmin();
        if (this.admin != isAdmin) {
            this.admin = isAdmin;
        }
    }

    @Override
    public String toString() {
        return "#" + id + ". " + fullName + " / " + slackId + "(nn: " + nickName + ", admin: " + admin + ")";
    }
}
