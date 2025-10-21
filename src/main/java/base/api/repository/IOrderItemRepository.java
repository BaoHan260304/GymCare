package base.api.repository;

import base.api.model.OrderItemModel;
import base.api.model.OrderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderItemRepository extends JpaRepository<OrderItemModel, Long>, JpaSpecificationExecutor<OrderItemModel> {
}
