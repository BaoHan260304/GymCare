package base.api.repository;

import base.api.model.CartItemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICartItemRepository extends JpaRepository<CartItemModel, Long>, JpaSpecificationExecutor<CartItemModel> {

    // select * from cart_item where cart_id = ?
    List<CartItemModel> findByCartModel_Id(Long cartId);
}
