package dev.altindal.graphql_client.controller;

import dev.altindal.graphql_client.dto.UserCreateRequest;
import dev.altindal.graphql_client.dto.UserResponse;
import dev.altindal.graphql_client.dto.UserUpdateRequest;
import dev.altindal.graphql_client.service.UserGraphqlRequester;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserGraphqlRequester service;

    public UserController(UserGraphqlRequester service) {
        this.service = service;
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return new ResponseEntity<>(service.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        return new ResponseEntity<>(service.getUserById(id), HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserCreateRequest request) {
        return new ResponseEntity<>(service.createUser(request), HttpStatus.CREATED);
    }

    @PutMapping("/user")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserUpdateRequest request) {
        return new ResponseEntity<>(service.updateUser(request), HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable UUID id) {
        return new ResponseEntity<>(service.deleteUser(id), HttpStatus.OK);
    }
}
