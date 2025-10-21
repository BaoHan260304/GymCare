package base.api.service;

import base.api.dto.request.RegisterDto;
import base.api.enums.UserRole;
import base.api.model.user.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserService {
    UserModel createUser(UserModel model);
    UserModel findByUserName(String userName);
    boolean existedByEmail(String email);
    UserModel findById(Long id);
    UserModel registerUser(RegisterDto dto);
    UserModel registerUser(String userName);



}
