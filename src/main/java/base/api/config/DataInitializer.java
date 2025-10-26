package base.api.config;

import base.api.model.Account;
import base.api.model.User;
import base.api.repository.AccountRepository;
import base.api.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppDefaultUserProperties appDefaultUserProperties;

    @Override
    public void run(String... args) {
        Map<String, AppDefaultUserProperties.DefaultUser> defaultUser = appDefaultUserProperties.getMap();

        defaultUser.forEach((roleKey, user) -> {
            String role = roleKey.toUpperCase(); // admin → ADMIN
            createUserIfNotExists(role, user);
        });
    }

    private void createUserIfNotExists(String role, AppDefaultUserProperties.DefaultUser user) {
        Optional<User> existingOpt = userRepository.findByEmail(user.getEmail());

        if (existingOpt.isEmpty()) {
            Account account = new Account();
            account.setAccountName(user.getEmail());
            account.setRole(role);
            Account savedAccount = accountRepository.save(account);

            User u = new User();
            u.setUserName(user.getName());
            u.setEmail(user.getEmail());
            u.setPassword(passwordEncoder.encode(user.getPassword()));
            u.setMobile(user.getMobile() != null ? user.getMobile() : "0123456789");
            u.setBirthday(parseDate(user.getBirthday(), LocalDate.of(1990, 1, 1)));
            u.setIdentityCard(user.getIdentitycard() != null ? user.getIdentitycard() : "000000000");
            u.setLicenceNumber(user.getLicencenumber() != null ? user.getLicencenumber() : "LC000000");
            u.setLicenceDate(parseDate(user.getLicencedate(), LocalDate.now()));
            u.setAccount(savedAccount);

            userRepository.save(u);
            System.out.println("Created " + role + " user: " + user.getEmail());
        } else {
            User existing = existingOpt.get();
            boolean updated = false;

            if (!passwordEncoder.matches(user.getPassword(), existing.getPassword())) {
                existing.setPassword(passwordEncoder.encode(user.getPassword()));
                updated = true;
            }

            if (!existing.getUserName().equals(user.getName())) {
                existing.setUserName(user.getName());
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
                userRepository.save(existing);
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
