package com.backend.users.dto;

import com.backend.users.entity.Rol;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private boolean enabled;
    private boolean admin;
    private List<Rol> roles;
}
