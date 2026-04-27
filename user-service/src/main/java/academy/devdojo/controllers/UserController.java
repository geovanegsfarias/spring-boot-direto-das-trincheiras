package academy.devdojo.controllers;

import academy.devdojo.mapper.UserMapper;
import academy.devdojo.request.UserPostRequest;
import academy.devdojo.request.UserPutRequest;
import academy.devdojo.response.UserGetResponse;
import academy.devdojo.response.UserPostResponse;
import academy.devdojo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/users")
@Slf4j
public class UserController {
    private final UserMapper mapper;
    private final UserService service;

    @Autowired
    public UserController(UserMapper mapper, UserService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<UserGetResponse>> findAll(@RequestParam(required = false) String name) {
        log.debug("Request received to list all users, params name {}", name);

        var users = service.findAll(name);

        var userGetResponseList = mapper.toUserGetResponseList(users);

        return ResponseEntity.ok(userGetResponseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGetResponse> findById(@PathVariable Long id) {
        log.debug("Request received to find user by id {}", id);

        var user = service.findByIdOrThrowException(id);

        var userGetResponse = mapper.toUserGetResponse(user);

        return ResponseEntity.ok(userGetResponse);
    }

    @PostMapping
    public ResponseEntity<UserPostResponse> save(@RequestBody UserPostRequest request) {
        log.debug("Request received to save user {}", request);

        var userToSave = mapper.toUser(request);

        var savedUser = service.save(userToSave);

        var userPostResponse = mapper.toUserPostResponse(savedUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(userPostResponse);
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody UserPutRequest request) {
        log.debug("Request received to update user {}", request);

        var userToUpdate = mapper.toUser(request);

        service.update(userToUpdate);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.debug("Request received to delete user by id {}", id);

        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}
