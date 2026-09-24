package com.backend.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String password;
    private List<RolResponse> roles;
    private boolean enabled;
    private boolean admin;
}
