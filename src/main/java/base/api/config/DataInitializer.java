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
        // Step 1: Check for existence using the Account repository, as email is now there.
        Optional<Account> existingAccountOpt = accountRepository.findByEmail(user.getEmail());

        if (existingAccountOpt.isEmpty()) {
            // --- Create New User and Account ---
            Account account = new Account();
            account.setAccountName(user.getEmail());
            account.setEmail(user.getEmail());
            account.setPassword(passwordEncoder.encode(user.getPassword()));
            account.setRole(role);
            account.setActive(true);

            User newUser = new User();
            newUser.setUserName(user.getName());
            newUser.setMobile(user.getMobile() != null ? user.getMobile() : "0123456789");
            newUser.setBirthday(parseDate(user.getBirthday(), LocalDate.of(1990, 1, 1)));
            newUser.setIdentityCard(user.getIdentitycard() != null ? user.getIdentitycard() : "000000000");
            newUser.setAccount(account); // Link the user to the account

            userRepository.save(newUser);
            System.out.println("Created " + role + " user: " + user.getEmail());
        } else {
            // --- Update Existing User and Account ---
            Account existingAccount = existingAccountOpt.get();
            User existingUser = existingAccount.getUser(); // Get the associated user
            boolean updated = false;

            // Update password on the Account entity
            if (!passwordEncoder.matches(user.getPassword(), existingAccount.getPassword())) {
                existingAccount.setPassword(passwordEncoder.encode(user.getPassword()));
                updated = true;
            }

            // Update personal info on the User entity
            if (existingUser != null && !existingUser.getUserName().equals(user.getName())) {
                existingUser.setUserName(user.getName());
                updated = true;
            }

            if (updated) {
                // Saving the account will persist its changes. If user was changed, save it too.
                accountRepository.save(existingAccount);
                if (existingUser != null) userRepository.save(existingUser);
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
