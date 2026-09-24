package com.backend.users.controller;


import com.backend.users.dto.UserRequest;
import com.backend.users.dto.UserResponse;
import com.backend.users.exception.ResourceNotFoundException;
import com.backend.users.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) throws ResourceNotFoundException {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest userRequest) throws ResourceNotFoundException {
        return new ResponseEntity<>(userService.createUser(userRequest), HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) throws ResourceNotFoundException {
        return new ResponseEntity<>(userService.updateUser(id, userRequest), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) throws ResourceNotFoundException {
        userService.deleteUser(id);
    }


}
