package base.api.service.impl;

import base.api.config.JwtUtil;
import base.api.model.Account;
import base.api.model.User;
import base.api.repository.AccountRepository;
import base.api.repository.UserRepository;
import base.api.security.CustomUserDetails;
import base.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public User registerUser(String name, String email, String password, String mobile, String birthday, String identityCard, String licenceNumber, String licenceDate) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("Email already in use");
        }

        // 1. Create and save Account
        Account account = new Account();
        account.setAccountName(email); // Use email as account name
        account.setRole("CUSTOMER");
        Account savedAccount = accountRepository.save(account);

        // 2. Create and save Customer
        User user = new User();
        user.setUserName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setMobile(mobile);
        user.setBirthday(LocalDate.parse(birthday));
        user.setIdentityCard(identityCard);
        user.setLicenceNumber(licenceNumber);
        user.setLicenceDate(LocalDate.parse(licenceDate));
        user.setAccount(savedAccount);

        return userRepository.save(user);
    }

    @Override
    public Optional<String> login(String email, String password) {
        try {
            // Spring Security will use UserDetailsService to check credentials
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            // If authentication is successful, generate a token
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);

            return Optional.of(token);

        } catch (Exception e) {
            // Authentication failed
            return Optional.empty();
        }
    }
}