package com.muskopf.tacotuesday.bl.proc;

import com.muskopf.tacotuesday.domain.Employee;
import com.muskopf.tacotuesday.domain.FullOrder;
import com.muskopf.tacotuesday.domain.IndividualOrder;
import com.muskopf.tacotuesday.domain.Taco;
import com.muskopf.tacotuesday.resource.EmployeeResource;
import com.muskopf.tacotuesday.resource.FullOrderResource;
import com.muskopf.tacotuesday.resource.IndividualOrderResource;
import com.muskopf.tacotuesday.resource.TacoResource;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface TacoTuesdayResourceMapper {
    Taco map(TacoResource resource);
    TacoResource map(Taco taco);

    List<TacoResource> mapToTacoResources(List<Taco> tacos);

    Employee map(EmployeeResource resource);
    EmployeeResource map(Employee employee);

    List<EmployeeResource> mapToEmployeeResources(List<Employee> employees);

    IndividualOrder map(IndividualOrderResource resource);
    IndividualOrderResource map(IndividualOrder order);

    List<IndividualOrderResource> mapToIndividualOrderResources(List<IndividualOrder> orders);

    FullOrder map(FullOrderResource resource);
    FullOrderResource map(FullOrder order);

    List<FullOrderResource> mapToFullOrderResources(List<FullOrder> orders);
}

