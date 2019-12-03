package com.muskopf.tacotuesday.bl.proc;

import com.muskopf.tacotuesday.domain.Employee;
import com.muskopf.tacotuesday.domain.FullOrder;
import com.muskopf.tacotuesday.domain.IndividualOrder;
import com.muskopf.tacotuesday.domain.Taco;
import com.muskopf.tacotuesday.resource.*;
import org.mapstruct.*;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static java.util.Objects.isNull;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface TacoTuesdayResourceMapper {
    Taco mapToTaco(TacoResource resource);
    TacoResource mapToTacoResource(Taco taco);

    List<TacoResource> mapToTacoResources(List<Taco> tacos);

    @Mappings({
        @Mapping(target = "createdAt", ignore = true),
        @Mapping(target = "updatedAt", ignore = true),
        @Mapping(target = "apiKey", ignore = true),
        @Mapping(target = "orders", ignore = true)
    })
    Employee mapToEmployee(EmployeeResource resource);
    EmployeeResource mapToEmployeeResource(Employee employee);

    @Mappings({
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "apiKey", ignore = true),
            @Mapping(target = "orders", ignore = true)
    })
    Employee mapToNewEmployee(NewEmployeeResource resource);
    NewEmployeeResource mapToNewEmployeeResource(Employee employee);

    List<EmployeeResource> mapToEmployeeResources(List<Employee> employees);

    @Mapping(target = "fullOrder", ignore = true)
    IndividualOrder mapToIndividualOrder(IndividualOrderResource resource);
    @Mapping(target = "fullOrderId", source = "order.fullOrder.id")
    IndividualOrderResource mapToIndividualOrderResource(IndividualOrder order);

    List<IndividualOrderResource> mapToIndividualOrderResources(List<IndividualOrder> orders);

    FullOrder mapToFullOrder(FullOrderResource resource);
    FullOrderResource mapToFullOrderResource(FullOrder order);
    @AfterMapping
    default void afterMapping(@MappingTarget FullOrderResource resource, FullOrder order) {
        if (isNull(order.getId())) {
            return;
        }

        resource.getIndividualOrders().forEach(o -> o.setFullOrderId(order.getId()));
    }

    List<FullOrderResource> mapToFullOrderResources(List<FullOrder> orders);

    default OffsetDateTime mapToOffsetDateTime(Instant instant) {
        return (isNull(instant)) ? null : OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
    default Instant mapToInstant(OffsetDateTime offsetDateTime) {
        return (isNull(offsetDateTime)) ? null : Instant.from(offsetDateTime);
    }
}

