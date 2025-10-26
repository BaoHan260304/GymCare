package base.api.config;

import base.api.model.Account;
import base.api.model.Customer;
import base.api.repository.AccountRepository;
import base.api.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Component
@Order(1)
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppDefaultUserProperties appDefaultUserProperties;

    @Override
    public void run(String... args) {
        Map<String, AppDefaultUserProperties.DefaultUser> users = appDefaultUserProperties.getUsers();

        users.forEach((roleKey, user) -> {
            String role = roleKey.toUpperCase(); // admin → ADMIN
            createUserIfNotExists(role, user);
        });
    }

    private void createUserIfNotExists(String role, AppDefaultUserProperties.DefaultUser user) {
        Optional<Customer> existingOpt = customerRepository.findByEmail(user.getEmail());

        if (existingOpt.isEmpty()) {
            Account account = new Account();
            account.setAccountName(user.getEmail());
            account.setRole(role);
            Account savedAccount = accountRepository.save(account);

            Customer customer = new Customer();
            customer.setCustomerName(user.getName());
            customer.setEmail(user.getEmail());
            customer.setPassword(passwordEncoder.encode(user.getPassword()));
            customer.setMobile(user.getMobile() != null ? user.getMobile() : "0123456789");
            customer.setBirthday(parseDate(user.getBirthday(), LocalDate.of(1990, 1, 1)));
            customer.setIdentityCard(user.getIdentitycard() != null ? user.getIdentitycard() : "000000000");
            customer.setLicenceNumber(user.getLicencenumber() != null ? user.getLicencenumber() : "LC000000");
            customer.setLicenceDate(parseDate(user.getLicencedate(), LocalDate.now()));
            customer.setAccount(savedAccount);

            customerRepository.save(customer);
            System.out.println("Created " + role + " user: " + user.getEmail());
        } else {
            Customer existing = existingOpt.get();
            boolean updated = false;

            if (!passwordEncoder.matches(user.getPassword(), existing.getPassword())) {
                existing.setPassword(passwordEncoder.encode(user.getPassword()));
                updated = true;
            }

            if (!existing.getCustomerName().equals(user.getName())) {
                existing.setCustomerName(user.getName());
                updated = true;
            }

            if (user.getMobile() != null && !user.getMobile().equals(existing.getMobile())) {
                existing.setMobile(user.getMobile());
                updated = true;
            }

            if (user.getBirthday() != null && !user.getBirthday().equals(existing.getBirthday().toString())) {
                existing.setBirthday(LocalDate.parse(user.getBirthday()));
                updated = true;
            }

            if (updated) {
                customerRepository.save(existing);
                System.out.println("Updated info for: " + user.getEmail());
            } else {
                System.out.println(role + " already exists, no changes.");
            }
        }

    }

    private LocalDate parseDate(String dateStr, LocalDate defaultDate) {
        try {
            return (dateStr != null && !dateStr.isBlank()) ? LocalDate.parse(dateStr) : defaultDate;
        } catch (Exception e) {
            return defaultDate;
        }
    }
}
