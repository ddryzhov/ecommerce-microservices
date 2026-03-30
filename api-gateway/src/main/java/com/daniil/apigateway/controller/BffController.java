package com.daniil.apigateway.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/bff")
@RequiredArgsConstructor
public class BffController {

    private final WebClient.Builder webClientBuilder;

    @GetMapping("/dashboard")
    public Mono<Map<String, Object>> getDashboard() {

        Mono<List<Object>> orders = webClientBuilder.build()
                .get()
                .uri("lb://order-service/api/orders")
                .retrieve()
                .bodyToFlux(Object.class)
                .collectList()
                .onErrorReturn(List.of());

        Mono<List<Object>> inventory = webClientBuilder.build()
                .get()
                .uri("lb://inventory-service/api/inventory")
                .retrieve()
                .bodyToFlux(Object.class)
                .collectList()
                .onErrorReturn(List.of());

        return Mono.zip(orders, inventory)
                .map(tuple -> Map.of(
                        "orders", tuple.getT1(),
                        "inventory", tuple.getT2()
                ));
    }
}
