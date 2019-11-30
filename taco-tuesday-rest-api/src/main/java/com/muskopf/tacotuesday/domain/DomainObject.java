package com.muskopf.tacotuesday.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
public class DomainObject {
    @Id
    @GeneratedValue
    protected Integer id;

    @Column(updatable = false)
    @CreationTimestamp
    protected Instant createdAt;

    @Column
    @UpdateTimestamp
    protected Instant updatedAt;

//    @Override
//    public boolean equals(Object o) {
//        if (o == this ) {
//            return true;
//        }
//
//        if (!(o instanceof DomainObject)) {
//            return false;
//        }
//
//        DomainObject other = (DomainObject) o;
//        return id.equals(other.getId()) &&
//                createdAt.equals(other.getCreatedAt()) &&
//                updatedAt.equals(other.getUpdatedAt());
//    }
//
//    @Override
//    public
}
