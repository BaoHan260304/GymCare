package base.api.security;

import base.api.model.Account;
import base.api.model.Customer;
import base.api.repository.AccountRepository;
import base.api.repository.CustomerRepository;
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
    private final CustomerRepository customerRepository; // To get the password

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByAccountName(email)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found with email: " + email));

        // We need the password from the Customer entity linked to this account
        // Assuming AccountName is the email for Customer accounts
        Optional<Customer> customerOptional = customerRepository.findByEmail(email);

        if (customerOptional.isEmpty()) {
            throw new UsernameNotFoundException("Customer details not found for account: " + email);
        }

        Customer customer = customerOptional.get();

        // KIỂM TRA TRẠNG THÁI KHÔNG HOẠT ĐỘNG
        if (customer.isInactive()) {
            throw new DisabledException("User account has been disabled.");
        }

        return new CustomUserDetails(account, customer.getPassword());
    }
}