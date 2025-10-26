package base.api.service;

import base.api.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAllUsers();

    Optional<User> findUserById(Long id);

    User updateUser(Long id, User customerDetails);

    void inActiveUser(Long id);
}