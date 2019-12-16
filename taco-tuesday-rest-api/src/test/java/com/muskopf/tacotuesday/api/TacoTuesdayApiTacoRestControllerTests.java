package com.muskopf.tacotuesday.api;

import com.muskopf.tacotuesday.TacoTuesdayApiHelper;
import com.muskopf.tacotuesday.domain.Taco;
import com.muskopf.tacotuesday.resource.TacoResource;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.muskopf.tacotuesday.TacoTuesdayApiHelper.ResponseStatus.OK;
import static org.assertj.core.api.Assertions.assertThat;

public class TacoTuesdayApiTacoRestControllerTests extends TacoTuesdayBaseRestControllerTester {
    /**
     * Test happy path of GET /tacos/
     */
    @Test
    public void test_getAllTacos() {
        // Get tacos
        List<Taco> tacos = persistenceHelper.loadObjects(Taco.class);
        // Map to resources
        List<TacoResource> expectedResources = mapper.mapTacosToTacoResources(tacos);

        // Perform GET /tacos
        List<TacoResource> responseObject = Arrays.asList(apiHelper.GET("/tacos", OK,
                TacoTuesdayApiHelper.ApiKeyStatus.EMPTY, TacoResource[].class));

        expectedResources.forEach(r -> assertThat(responseObject).contains(r));
    }
}
