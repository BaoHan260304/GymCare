package base.api.dao.impl;

import base.api.dao.IUserDAO;
import base.api.model.user.UserModel;
import base.api.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAOImpl implements IUserDAO {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public UserModel findByUserName(String userName) {
        return userRepository.findByUserName(userName).orElse(null);
    }

    @Override
    public List<UserModel> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void save(UserModel user) {
        userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
