package com.muskopf.tacotuesday.bl.proc;

import com.muskopf.tacotuesday.domain.*;
import com.muskopf.tacotuesday.resource.*;
import io.swagger.models.auth.In;
import org.hibernate.sql.Update;
import org.mapstruct.*;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

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

    List<IndividualOrderResource> mapIndividualOrdersToIndividualOrderResources(List<IndividualOrder> orders);
    List<IndividualOrderResource> mapIndividualOrdersToIndividualOrderResources(Set<IndividualOrder> orders);
    Set<IndividualOrder> mapIndividualOrderResourcesToIndividualOrders(List<IndividualOrderResource> resources);

    List<FullOrderResource> mapFullOrdersToFullOrderResources(List<FullOrder> orders);

    default IndividualOrder mapIndividualOrderResourceToIndividualOrder(IndividualOrderResource resource) {
        IndividualOrder order = new IndividualOrder();
        mapOrderResourceOntoOrder(resource, order);

        order.setEmployee(mapEmployeeResourceToEmployee(resource.getEmployee()));

        return order;
    }

    default IndividualOrderResource mapIndividualOrderToIndividualOrderResource(IndividualOrder order) {
        IndividualOrderResource resource = new IndividualOrderResource();
        mapOrderOntoOrderResource(order, resource);

        resource.setEmployee(mapEmployeeToEmployeeResource(order.getEmployee()));

        return resource;
    }

    default FullOrder mapFullOrderResourceToFullOrder(FullOrderResource resource) {
        FullOrder fullOrder = new FullOrder();
        mapOrderResourceOntoOrder(resource, fullOrder);

        fullOrder.setIndividualOrders(mapIndividualOrderResourcesToIndividualOrders(resource.getIndividualOrders()));

        return fullOrder;
    }

    default FullOrderResource mapFullOrderToFullOrderResource(FullOrder order) {
        FullOrderResource resource = new FullOrderResource();
        mapOrderOntoOrderResource(order, resource);

        resource.setIndividualOrders(mapIndividualOrdersToIndividualOrderResources(order.getIndividualOrders()));
        resource.getIndividualOrders().forEach(o -> o.setFullOrderId(order.getId()));

        return resource;
    }

    default void mapOrderOntoOrderResource(Order order, OrderResource resource) {
        Map<TacoType, Integer> tacoMap = new HashMap<>();
        for (TacoType tacoType : TacoType.values()) {
            tacoMap.put(tacoType, order.getTacoCount(tacoType));
        }

        resource.setId(order.getId());
        resource.setTacos(tacoMap);
        resource.setTotal(order.getTotal());
        resource.setCreatedAt(order.getCreatedAt());//mapInstantToOffsetDateTime(order.getCreatedAt()));
    }

    default void mapOrderResourceOntoOrder(OrderResource resource, Order order) {
        for (Map.Entry<TacoType, Integer> tacoCount : resource.getTacos().entrySet()) {
            order.setTacoCount(tacoCount.getKey(), tacoCount.getValue());
        }

        if (resource.getId() != null)
            order.setId(resource.getId());
        if (resource.getCreatedAt() != null)
            order.setCreatedAt(resource.getCreatedAt());//mapOffsetDateTimeToInstant(resource.getCreatedAt()));

        order.setTotal(resource.getTotal());
    }

    default OffsetDateTime mapInstantToOffsetDateTime(Instant instant) {
        return (isNull(instant)) ? null : OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
    default Instant mapOffsetDateTimeToInstant(OffsetDateTime offsetDateTime) {
        return (isNull(offsetDateTime)) ? null : Instant.from(offsetDateTime);
    }
}

