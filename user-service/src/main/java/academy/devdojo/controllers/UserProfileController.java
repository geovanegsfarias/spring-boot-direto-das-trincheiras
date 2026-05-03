package academy.devdojo.controllers;

import academy.devdojo.domain.UserProfile;
import academy.devdojo.mapper.UserMapper;
import academy.devdojo.request.UserPostRequest;
import academy.devdojo.request.UserPutRequest;
import academy.devdojo.response.UserGetResponse;
import academy.devdojo.response.UserPostResponse;
import academy.devdojo.service.UserProfileService;
import academy.devdojo.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/user-profiles")
@Slf4j
public class UserProfileController {
    private final UserProfileService service;

    @Autowired
    public UserProfileController(UserProfileService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<UserProfile>> findAll() {
        log.debug("Request received to list all user profiles");

        var userProfiles = service.findAll();

        return ResponseEntity.ok(userProfiles);
    }

}
