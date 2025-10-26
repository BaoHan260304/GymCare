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

    // ----ADMIN DEFAULT ACCOUNT----
    @Value("${app.default-users.admin.name}")
    private String adminName;
    @Value("${app.default-users.admin.email}")
    private String adminEmail;
    @Value("${app.default-users.admin.password}")
    private String adminPassword;
    @Value("${app.default-users.admin.mobile}")
    private String adminMobile;
    @Value("${app.default-users.admin.birthday}")
    private String adminBirthday;
    @Value("${app.default-users.admin.identitycard}")
    private String adminIdentityCard;
    @Value("${app.default-users.admin.licencenumber}")
    private String adminLicenceNumber;
    @Value("${app.default-users.admin.licencedate}")
    private String adminLicenceDate;

    // ----MANAGER DEFAULT ACCOUNT----
    @Value("${app.default-users.manager.name}")
    private String managerName;
    @Value("${app.default-users.manager.email}")
    private String managerEmail;
    @Value("${app.default-users.manager.password}")
    private String managerPassword;
    @Value("${app.default-users.manager.mobile}")
    private String managerMobile;
    @Value("${app.default-users.manager.birthday}")
    private String managerBirthday;
    @Value("${app.default-users.manager.identitycard}")
    private String managerIdentityCard;
    @Value("${app.default-users.manager.licencenumber}")
    private String managerLicenceNumber;
    @Value("${app.default-users.manager.licencedate}")
    private String managerLicenceDate;

    // ----INVENTORY DEFAULT ACCOUNT----
    @Value("${app.default-users.inventory.name}")
    private String inventoryName;
    @Value("${app.default-users.inventory.email}")
    private String inventoryEmail;
    @Value("${app.default-users.inventory.password}")
    private String inventoryPassword;
    @Value("${app.default-users.inventory.mobile}")
    private String inventoryMobile;
    @Value("${app.default-users.inventory.birthday}")
    private String inventoryBirthday;
    @Value("${app.default-users.inventory.identitycard}")
    private String inventoryIdentityCard;
    @Value("${app.default-users.inventory.licencenumber}")
    private String inventoryLicenceNumber;
    @Value("${app.default-users.inventory.licencedate}")
    private String inventoryLicenceDate;


    @Override
    public void run(String... args) {
        createIfNotExists(adminName, adminEmail, adminPassword, adminMobile, adminBirthday, adminIdentityCard, adminLicenceNumber, adminLicenceDate, "ADMIN");
        createIfNotExists(managerName, managerEmail, managerPassword, managerMobile, managerBirthday, managerIdentityCard, managerLicenceNumber, managerLicenceDate,"MANAGER");
        createIfNotExists(inventoryName, inventoryEmail, inventoryPassword, inventoryMobile, inventoryBirthday, inventoryIdentityCard, inventoryLicenceNumber, inventoryLicenceDate, "INVENTORY");
    }

    private void createIfNotExists(
            String name,
            String email,
            String defaultPassword,
            String mobile,
            String birthday,
            String identityCard,
            String licenceNumber,
            String licenceDate,
            String role) {
        Optional<Customer> existingAccountOpt = customerRepository.findByEmail(email);

        // Nếu chưa có tài khoản thì tạo mới
        if (existingAccountOpt.isEmpty()) {
            // Tạo Account trước
            Account account = new Account();
            account.setAccountName(email);
            account.setRole(role);
            Account savedAccount = accountRepository.save(account);

            // Tạo Customer tương ứng
            Customer customer = new Customer();
            customer.setCustomerName(name);
            customer.setEmail(email);
            customer.setPassword(passwordEncoder.encode(defaultPassword));
            customer.setMobile(mobile);
            customer.setBirthday(LocalDate.parse(birthday));  //or customer.setBirthday(LocalDate.of(2004, 3, 26));
            customer.setIdentityCard(identityCard);
            customer.setLicenceNumber(licenceNumber);
            customer.setLicenceDate(LocalDate.parse(licenceDate));  //or customer.setLicenceDate(LocalDate.now());
            customer.setAccount(savedAccount);

            customerRepository.save(customer);
            System.out.println("Default " + role + " created: " + email);
        } else {
            Customer existingAccount = existingAccountOpt.get();

            // Kiểm tra bằng passwordEncoder.matches (so sánh mật khẩu plain với hash stored)
            if (!passwordEncoder.matches(defaultPassword, existingAccount.getPassword())) {
                existingAccount.setPassword(passwordEncoder.encode(defaultPassword));
                customerRepository.save(existingAccount);
                System.out.println(" Password updated for: " + email);
            } else {
                System.out.println("Default accounts already exists with correct password, skip update.");
            }
        }
    }


}
