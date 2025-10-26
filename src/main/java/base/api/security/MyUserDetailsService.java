package base.api.security;

import base.api.model.Account;
import base.api.model.User;
import base.api.repository.AccountRepository;
import base.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository; // To get the password

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByAccountName(email)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found with email: " + email));

        // We need the password from the Customer entity linked to this account
        // Assuming AccountName is the email for Customer accounts
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Customer details not found for account: " + email);
        }

        User user = userOptional.get();

        // KIỂM TRA TRẠNG THÁI KHÔNG HOẠT ĐỘNG
        if (user.isInactive()) {
            throw new DisabledException("User account has been disabled.");
        }

        return new CustomUserDetails(account, user.getPassword());
    }
}