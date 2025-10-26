package base.api.service;

import base.api.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAllUsers();

    List<User> findAllByRole(String role);

    Optional<User> findUserById(Long id);

    User updateUser(Long id, User userDetails);

    void inActiveUser(Long id);

}