package com.backend.users.service.imp;

import com.backend.users.dto.UserRequest;
import com.backend.users.dto.UserResponse;
import com.backend.users.entity.Rol;
import com.backend.users.entity.User;
import com.backend.users.exception.DuplicatedUserException;
import com.backend.users.exception.ResourceNotFoundException;
import com.backend.users.repository.RolRepository;
import com.backend.users.repository.UserRepository;
import com.backend.users.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, RolRepository rolRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {

        List<UserResponse> userResponses = userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
                .toList();

        return userResponses;
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) throws ResourceNotFoundException {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        return modelMapper.map(user, UserResponse.class);

    }


    @Override
    @Transactional
    public UserResponse createUser(UserRequest userRequest) {

        userRepository.findByEmail(userRequest.getEmail())
                .ifPresent(user -> {
                    throw new DuplicatedUserException("User with email " + userRequest.getEmail()
                            + " already exists");
                });

        User user = modelMapper.map(userRequest, User.class);
        user.setRoles(getRoles(userRequest.isAdmin()));

        return modelMapper.map(userRepository.save(user), UserResponse.class);

    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UserRequest userRequest) throws ResourceNotFoundException {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setName(userRequest.getName());
                    existingUser.setEmail(userRequest.getEmail());
                    existingUser.setPassword(userRequest.getPassword());
                    existingUser.setRoles(getRoles(userRequest.isAdmin()));
                    existingUser.setEnabled(userRequest.isEnabled());
                    return modelMapper.map(userRepository.save(existingUser), UserResponse.class);
                })
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) throws ResourceNotFoundException {

        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
    }

    public List<Rol> getRoles(boolean isAdmin) {
        List<Rol> roles = new ArrayList<>();

        Optional<Rol> userRole = rolRepository.findByName("ROLE_USER");
        userRole.ifPresent(roles::add);

        if (isAdmin) {
            System.out.println("User is admin, adding ROLE_ADMIN");
            Optional<Rol> adminRole = rolRepository.findByName("ROLE_ADMIN");

            adminRole.ifPresent(roles::add);
        }

        return roles;
    }

}
