package base.api.service.impl;

import base.api.model.Customer;
import base.api.repository.CustomerRepository;
import base.api.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public List<Customer> findAllCustomers() {
        return customerRepository.findByInactiveFalse();
    }

    @Override
    public Optional<Customer> findCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        customer.setCustomerName(customerDetails.getCustomerName());
        customer.setMobile(customerDetails.getMobile());
        // Update other fields as needed, but be careful with sensitive data like password or email

        return customerRepository.save(customer);
    }

    @Override
    public void inActiveCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        // Đánh dấu khách hàng là không hoạt động (inactive)
        customer.setInactive(true);
        customerRepository.save(customer);
    }
}
