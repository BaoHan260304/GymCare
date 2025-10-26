package base.api.service;

import base.api.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<Customer> findAllCustomers();

    Optional<Customer> findCustomerById(Long id);

    Customer updateCustomer(Long id, Customer customerDetails);

    void inActiveCustomer(Long id);
}