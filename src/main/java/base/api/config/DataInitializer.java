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

@Component
@Order(1)
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String adminEmail = "admin@gmail.com";

        // Nếu chưa có admin thì tạo mới
        if (customerRepository.findByEmail(adminEmail).isEmpty()) {
            // Tạo Account trước
            Account account = new Account();
            account.setAccountName(adminEmail);
            account.setRole("ADMIN");
            Account savedAccount = accountRepository.save(account);

            // Tạo Customer tương ứng
            Customer admin = new Customer();
            admin.setCustomerName("System Admin");
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("123123"));
            admin.setMobile("0123456789");
            admin.setBirthday(LocalDate.of(1990, 1, 1));
            admin.setIdentityCard("000000001");
            admin.setLicenceNumber("AD123456");
            admin.setLicenceDate(LocalDate.now());
            admin.setAccount(savedAccount);

            customerRepository.save(admin);

            System.out.println("Default admin created: " + adminEmail);
        } else {
            System.out.println("Admin already exists, skip seeding.");
        }
    }
}
