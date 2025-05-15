package com.csniico.userService.controller;

import com.csniico.userService.Services.UserCRUDService;
import com.csniico.userService.Services.UserServiceMessagePublisher;
import com.csniico.userService.dto.UserRequest;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Data
@RestController
@RequestMapping("/api/v1")
public class EventController {

    private final UserServiceMessagePublisher publisher;
    private  RestTemplate restTemplate;
    private final UserCRUDService userCRUDService;

    public EventController(
            UserServiceMessagePublisher publisher,
            UserCRUDService userCRUDService,
            RestTemplate restTemplate
    ) {
        this.publisher = publisher;
        this.userCRUDService = userCRUDService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/")
    public String home() {
        return "Hello World!";
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNewUser(@RequestBody UserRequest userRequest) {
        try {
            boolean created = userCRUDService.createUser(userRequest);
            if (created) {
                publisher.sendUserCreatedMessageToTopic(userRequest);
                return ResponseEntity.ok("User created successfully");
            } else {
                return ResponseEntity.badRequest().body("User already exists");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
