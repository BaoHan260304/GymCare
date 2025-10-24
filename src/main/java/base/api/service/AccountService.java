package base.api.service;

import base.api.dto.request.RegistrationRequest;
import base.api.model.Account;

import java.util.Optional;

public interface AccountService {
    Optional<Account> findByEmail(String email);

    void registerCustomer(RegistrationRequest request);
}