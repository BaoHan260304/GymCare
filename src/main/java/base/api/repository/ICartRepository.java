package base.api.repository;

import base.api.model.CartModel;
import base.api.model.user.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICartRepository extends JpaRepository<CartModel, Long>, JpaSpecificationExecutor<CartModel> {
    Optional<CartModel> findByUserId(Long userId);

}
