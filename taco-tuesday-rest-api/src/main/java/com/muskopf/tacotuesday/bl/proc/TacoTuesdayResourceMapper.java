package com.muskopf.tacotuesday.bl.proc;

import com.muskopf.tacotuesday.domain.Employee;
import com.muskopf.tacotuesday.domain.FullOrder;
import com.muskopf.tacotuesday.domain.IndividualOrder;
import com.muskopf.tacotuesday.domain.Taco;
import com.muskopf.tacotuesday.resource.EmployeeResource;
import com.muskopf.tacotuesday.resource.FullOrderResource;
import com.muskopf.tacotuesday.resource.IndividualOrderResource;
import com.muskopf.tacotuesday.resource.TacoResource;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.aspectj.lang.annotation.After;
import org.mapstruct.*;

import java.util.List;

import static java.util.Objects.isNull;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface TacoTuesdayResourceMapper {
    Taco map(TacoResource resource);
    TacoResource map(Taco taco);

    List<TacoResource> mapToTacoResources(List<Taco> tacos);

    @Mappings({
        @Mapping(target = "createdAt", ignore = true),
        @Mapping(target = "updatedAt", ignore = true),
        @Mapping(target = "apiKey", ignore = true),
        @Mapping(target = "orders", ignore = true)
    })
    Employee map(EmployeeResource resource);
    EmployeeResource map(Employee employee);

    List<EmployeeResource> mapToEmployeeResources(List<Employee> employees);

    @Mapping(target = "fullOrder", ignore = true)
    IndividualOrder map(IndividualOrderResource resource);
    @Mapping(target = "fullOrderId", source = "order.fullOrder.id")
    IndividualOrderResource map(IndividualOrder order);

    List<IndividualOrderResource> mapToIndividualOrderResources(List<IndividualOrder> orders);

    FullOrder map(FullOrderResource resource);
    FullOrderResource map(FullOrder order);
    @AfterMapping
    default void afterMapping(@MappingTarget FullOrderResource resource, FullOrder order) {
        if (isNull(order.getId())) {
            return;
        }

        resource.getIndividualOrders().forEach(o -> o.setFullOrderId(order.getId()));
    }

    List<FullOrderResource> mapToFullOrderResources(List<FullOrder> orders);
}

