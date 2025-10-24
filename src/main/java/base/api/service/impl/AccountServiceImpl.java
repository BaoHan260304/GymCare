package base.api.service.impl;

import base.api.dto.request.RegistrationRequest;
import base.api.model.Account;
import base.api.model.Customer;
import base.api.model.Role;
import base.api.model.User;
import base.api.repository.AccountRepository;
import base.api.repository.CustomerRepository;
import base.api.repository.RoleRepository;
import base.api.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<Account> findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public void registerCustomer(RegistrationRequest request) {
        if (accountRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("Email đã được sử dụng!");
        }

        Role customerRole = roleRepository.findByRoleName("Customer")
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy vai trò 'Customer'. Vui lòng tạo trong CSDL."));

        // 1. Tạo Account
        Account account = new Account();
        account.setEmail(request.getEmail());
        account.setHashedPassword(passwordEncoder.encode(request.getPassword()));
        account.setRole(customerRole);
        account.setIsActive(true);

        // 2. Tạo User
        User user = new User();
        user.setAccount(account);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());

        // 3. Tạo Customer
        Customer customer = new Customer();
        customer.setUser(user);
        customer.setJoinDate(LocalDate.now());

        // 4. Lưu Customer (sẽ tự động lưu User và Account nhờ CascadeType.ALL)
        customerRepository.save(customer);
    }
}