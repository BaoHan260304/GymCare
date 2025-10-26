package base.api.service;

import base.api.model.Customer;

import java.util.Optional;

public interface AuthService {

    Customer registerCustomer(String name, String email, String password, String mobile, String birthday, String identityCard, String licenceNumber, String licenceDate);

    Optional<String> login(String email, String password);
}