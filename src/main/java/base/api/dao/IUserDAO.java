package base.api.dao;

import base.api.model.user.UserModel;
import java.util.List;

public interface IUserDAO {
    UserModel findByUserName(String userName);
    List<UserModel> findAll();
    void save(UserModel user);
    void deleteById(Long id);
}
