package com.muskopf.tacotuesday.bl.proc;

import com.muskopf.tacotuesday.domain.Employee;
import com.muskopf.tacotuesday.domain.FullOrder;
import com.muskopf.tacotuesday.domain.IndividualOrder;
import com.muskopf.tacotuesday.domain.Taco;
import com.muskopf.tacotuesday.resource.*;
import org.hibernate.sql.Update;
import org.mapstruct.*;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static java.util.Objects.isNull;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface TacoTuesdayResourceMapper {
    Taco mapTacoResourceToTaco(TacoResource resource);
    TacoResource mapTacoToTacoResource(Taco taco);

    List<TacoResource> mapTacosToTacoResources(List<Taco> tacos);

    Employee mapEmployeeResourceToEmployee(EmployeeResource resource);
    EmployeeResource mapEmployeeToEmployeeResource(Employee employee);

    List<EmployeeResource> mapEmployeesToEmployeeResources(List<Employee> employees);

    Employee mapNewEmployeeResourceToEmployee(NewEmployeeResource resource);
    NewEmployeeResource mapEmployeeToNewEmployeeResource(Employee employee);

    Employee mapUpdateEmployeeResourceToEmployee(UpdateEmployeeResource resource);
    UpdateEmployeeResource mapEmployeeToUpdateEmployeeResource(Employee employee);

    Employee mapUpdateEmployeeBatchResourceToEmployee(UpdateEmployeeBatchResource resources);
    UpdateEmployeeBatchResource mapEmployeeToUpdateEmployeeBatchResource(Employee employees);

    List<Employee> mapUpdateEmployeeBatchResourcesToEmployees(List<UpdateEmployeeBatchResource> resources);
    List<UpdateEmployeeBatchResource> mapEmployeesToUpdateEmployeeBatchResources(List<Employee> employees);

    @Mapping(target = "fullOrder", ignore = true)
    IndividualOrder mapIndividualOrderResourceToIndividualOrder(IndividualOrderResource resource);
    @Mapping(target = "fullOrderId", source = "order.fullOrder.id")
    IndividualOrderResource mapIndividualOrderToIndividualOrderResource(IndividualOrder order);

    List<IndividualOrderResource> mapIndividualOrdersToIndividualOrderResources(List<IndividualOrder> orders);

    FullOrder mapFullOrderResourcesToFullOrder(FullOrderResource resource);
    FullOrderResource mapFullOrderToFullOrderResource(FullOrder order);
    @AfterMapping
    default void afterMapping(@MappingTarget FullOrderResource resource, FullOrder order) {
        if (isNull(order.getId())) { return; }
        resource.getIndividualOrders().forEach(o -> o.setFullOrderId(order.getId()));
    }

    List<FullOrderResource> mapFullOrdersToFullOrderResources(List<FullOrder> orders);

    default OffsetDateTime mapInstantToOffsetDateTime(Instant instant) {
        return (isNull(instant)) ? null : OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
    default Instant mapOffsetDateTimeToInstant(OffsetDateTime offsetDateTime) {
        return (isNull(offsetDateTime)) ? null : Instant.from(offsetDateTime);
    }
}

