package com.backend.order.client;

import com.backend.order.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(url = "${user-service.url}", path = "/api/users", name = "USER-SERVICE")
public interface UserFeignClient {


    @GetMapping
    List<UserResponse> getAllUsers();

    @GetMapping("/{id}")
    UserResponse getUserById(@PathVariable Long id);
}
