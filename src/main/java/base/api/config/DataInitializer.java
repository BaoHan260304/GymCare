package base.api.config;

import base.api.model.Account;
import base.api.model.Customer;
import base.api.repository.AccountRepository;
import base.api.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@Order(1)
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.default-password:123123}") // nếu không có thì mặc định l 123123
    private String defaultAdminPassword; // injected từ properties, fallback "123123"

    @Override
    public void run(String... args) {
        String adminEmail = "admin@gmail.com";
        Optional<Customer> existingAdminOpt = customerRepository.findByEmail(adminEmail);

        // Nếu chưa có admin thì tạo mới
        if (existingAdminOpt.isEmpty()) {
            // Tạo Account trước
            Account account = new Account();
            account.setAccountName(adminEmail);
            account.setRole("ADMIN");
            Account savedAccount = accountRepository.save(account);

            // Tạo Customer tương ứng
            Customer admin = new Customer();
            admin.setCustomerName("System Admin");
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(defaultAdminPassword));
            admin.setMobile("0123456789");
            admin.setBirthday(LocalDate.of(1990, 1, 1));
            admin.setIdentityCard("000000001");
            admin.setLicenceNumber("AD123456");
            admin.setLicenceDate(LocalDate.now());
            admin.setAccount(savedAccount);

            customerRepository.save(admin);

            System.out.println("Default admin created: " + adminEmail);
        } else {
            Customer existingAdmin = existingAdminOpt.get();
            // Kiểm tra bằng passwordEncoder.matches (so sánh mật khẩu plain với hash stored)
            if (!passwordEncoder.matches(defaultAdminPassword, existingAdmin.getPassword())) {
                existingAdmin.setPassword(passwordEncoder.encode(defaultAdminPassword));
                customerRepository.save(existingAdmin);
                System.out.println(" Admin password updated for: " + adminEmail);
            } else {
                System.out.println("Admin already exists with correct password, skip update.");
            }
        }
    }
}
