package com.respec.tacotuesday.service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class TacoTuesdayApiRestClient {
    private WebClient webClient;

    @Autowired
    public TacoTuesdayApiRestClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public

}
