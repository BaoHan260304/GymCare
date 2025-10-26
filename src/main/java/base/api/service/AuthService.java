package base.api.service;

import base.api.dto.request.CustomerRegistrationRequest;
import base.api.model.User;

import java.util.Optional;

public interface AuthService {

    User registerUser(CustomerRegistrationRequest request);

    Optional<String> login(String email, String password);
}