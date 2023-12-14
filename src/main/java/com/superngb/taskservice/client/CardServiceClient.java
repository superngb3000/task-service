package com.superngb.taskservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "card-service", url = "${feign_client.card_service.url}")
public interface CardServiceClient {
    @GetMapping("/{id}")
    ResponseEntity<?> getCard(@PathVariable Long id);
}