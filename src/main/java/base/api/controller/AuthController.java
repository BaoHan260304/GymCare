package base.api.controller;

import base.api.dto.request.LoginRequest;
import base.api.dto.request.RegisterRequest;
import base.api.model.Customer;
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
    public ResponseEntity<?> registerCustomer(@RequestBody RegisterRequest request) {
        try {
            Customer registeredCustomer = authService.registerCustomer(request.getName(), request.getEmail(), request.getPassword(), request.getMobile(), request.getBirthday().toString(), // Convert LocalDate to String for service method
                    request.getIdentityCard(), request.getLicenceNumber(), request.getLicenceDate().toString() // Convert LocalDate to String for service method
            );
            return ResponseEntity.status(HttpStatus.CREATED).body("Customer registered successfully with ID: " + registeredCustomer.getId());
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