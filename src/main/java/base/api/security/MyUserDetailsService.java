package base.api.security;

import base.api.model.Account;
import base.api.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Step 1: Find the account directly by email. This is the only lookup needed.
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found with email: " + email));

        // Step 2: Check if the account is active.
        if (!account.isActive()) {
            throw new DisabledException("User account has been disabled.");
        }

        // Step 3: Return a new CustomUserDetails object using only the account.
        // The password is now directly available from the account entity.
        return new CustomUserDetails(account);
    }
}