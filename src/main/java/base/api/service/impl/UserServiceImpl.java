package base.api.service.impl;

import base.api.model.Account;
import base.api.model.User;
import base.api.repository.AccountRepository;
import base.api.repository.UserRepository;
import base.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository; // Inject AccountRepository

    @Override
    public List<User> findAllUsers() {
        // Find all accounts that are active, then get the associated users.
        return accountRepository.findByIsActive(true)
                .stream()
                .map(Account::getUser)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    @Override
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Update only the allowed fields from the request
        user.setUserName(userDetails.getUserName());
        user.setMobile(userDetails.getMobile());
        user.setBirthday(userDetails.getBirthday());
        user.setIdentityCard(userDetails.getIdentityCard());

        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void inActiveUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Get the associated account and set it to inactive
        Account account = user.getAccount();
        if (account != null) {
            account.setActive(false);
            accountRepository.save(account);
        }
    }

    @Override
    public List<User> findAllByRole(String role) {
        return accountRepository.findByRole(role)
                .stream()
                .map(Account::getUser)
                .collect(Collectors.toList());
    }
}
