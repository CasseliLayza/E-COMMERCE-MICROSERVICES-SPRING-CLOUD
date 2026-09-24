package com.backend.users.service;

import com.backend.users.dto.UserRequest;
import com.backend.users.dto.UserResponse;
import com.backend.users.exception.DuplicatedUserException;
import com.backend.users.exception.ResourceNotFoundException;

import java.util.List;

public interface UserService {

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id) throws ResourceNotFoundException;

    UserResponse createUser(UserRequest userRequest) throws ResourceNotFoundException, DuplicatedUserException;

    UserResponse updateUser(Long id, UserRequest userRequest) throws ResourceNotFoundException;

    void deleteUser(Long id) throws ResourceNotFoundException;

}
