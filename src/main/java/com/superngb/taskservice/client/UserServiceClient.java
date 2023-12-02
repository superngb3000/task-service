package com.superngb.taskservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${feign_client.user_service.url}")
public interface UserServiceClient {
    @GetMapping("/userExists/{id}")
    boolean userExists(@PathVariable Long id);
}