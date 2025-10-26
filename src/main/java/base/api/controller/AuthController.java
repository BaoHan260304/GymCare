package base.api.controller;

import base.api.dto.request.LoginRequest;
import base.api.dto.request.CustomerRegistrationRequest;
import base.api.model.User;
import base.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

// Hidden Lines
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController { // Renamed from AuthController to AuthController

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@RequestBody CustomerRegistrationRequest request) {
        try {
            // Call the refactored service method with the correct DTO
            User registeredUser = authService.registerUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("Customer registered successfully with ID: " + registeredUser.getId());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<String> token = authService.login(request.getEmail(), request.getPassword());

        if (token.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("token", token.get());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }
}