package base.api.service.impl;

import base.api.dto.request.TrainerCreationRequest;
import base.api.model.Account;
import base.api.model.Staff;
import base.api.model.User;
import base.api.repository.AccountRepository;
import base.api.repository.StaffRepository;
import base.api.repository.UserRepository;
import base.api.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Staff createTrainer(TrainerCreationRequest request) {
        if (accountRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already in use");
        }

        // Generate a random password for the first login
        String randomPassword = UUID.randomUUID().toString().substring(0, 8);

        // 1. Create Account
        Account account = new Account();
        account.setAccountName(request.getEmail());
        account.setEmail(request.getEmail());
        account.setPassword(passwordEncoder.encode(randomPassword));
        account.setRole("TRAINER");

        // 2. Create User
        User user = new User();
        user.setUserName(request.getUserName());
        user.setMobile(request.getMobile());
        user.setBirthday(request.getBirthday());
        user.setIdentityCard(request.getIdentityCard());
        user.setAccount(account);

        // 3. Create Staff
        Staff staff = new Staff();
        staff.setUser(user); // Link to the user
        staff.setLicenceNumber(request.getLicenceNumber());
        staff.setLicenceDate(request.getLicenceDate());

        // Vì có CascadeType.ALL trên Account->User, việc lưu Staff sẽ không tự động lưu User/Account.
        // Cần lưu chúng một cách rõ ràng hoặc điều chỉnh cài đặt cascade.
        userRepository.save(user); // This will also save the associated Account.
        return staffRepository.save(staff);
    }
}