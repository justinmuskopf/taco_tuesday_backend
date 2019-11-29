package com.muskopf.tacotuesday.domain;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

@Data
@MappedSuperclass
public class DomainObject {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column
    @UpdateTimestamp
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (o == this ) {
            return true;
        }

        if (!(o instanceof DomainObject)) {
            return false;
        }

        DomainObject other = (DomainObject) o;
        return id.equals(other.getId()) &&
                createdAt.equals(other.getCreatedAt()) &&
                updatedAt.equals(other.getUpdatedAt());
    }
}
