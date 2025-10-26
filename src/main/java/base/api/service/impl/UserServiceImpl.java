package base.api.service.impl;

import base.api.model.User;
import base.api.repository.UserRepository;
import base.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> findAllUsers() {
        return userRepository.findByInactiveFalse();
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User updateUser(Long id, User customerDetails) {
        User customer = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        customer.setUserName(customerDetails.getUserName());
        customer.setMobile(customerDetails.getMobile());
        // Update other fields as needed, but be careful with sensitive data like password or email

        return userRepository.save(customer);
    }

    @Override
    public void inActiveUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        // Đánh dấu khách hàng là không hoạt động (inactive)
        user.setInactive(true);
        userRepository.save(user);
    }
}
