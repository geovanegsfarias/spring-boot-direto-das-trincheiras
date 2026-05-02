package academy.devdojo.controllers;

import academy.devdojo.mapper.ProfileMapper;
import academy.devdojo.request.ProfilePostRequest;
import academy.devdojo.response.ProfileGetResponse;
import academy.devdojo.response.ProfilePostResponse;
import academy.devdojo.service.ProfileService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/profiles")
@Slf4j
public class ProfileController {
    private final ProfileMapper mapper;
    private final ProfileService service;

    @Autowired
    public ProfileController(ProfileMapper mapper, ProfileService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProfileGetResponse>> findAll() {
        log.debug("Request received to list all profiles");

        var profiles = service.findAll();

        var profileGetResponseList = mapper.toProfileGetResponseList(profiles);

        return ResponseEntity.ok(profileGetResponseList);
    }

    @PostMapping
    public ResponseEntity<ProfilePostResponse> save(@RequestBody @Valid ProfilePostRequest request) {
        log.debug("Request received to save profile {}", request);

        var profileToSave = mapper.toProfile(request);

        var savedProfile = service.save(profileToSave);

        var profilePostResponse = mapper.toProfilePostResponse(savedProfile);

        return ResponseEntity.status(HttpStatus.CREATED).body(profilePostResponse);
    }

}
