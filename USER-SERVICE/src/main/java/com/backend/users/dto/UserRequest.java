package com.backend.users.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserRequest {

    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 20, message = "Name must be between 2 and 20 characters")
    private String name;
    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 20, message = "Name must be between 2 and 20 characters")
    private String email;
    @NotBlank(message = "Name is mandatory")
    @Size(min = 6, max = 20, message = "Name must be between 6 and 20 characters")
    private String password;
    private boolean enabled;
    private boolean admin;

}
