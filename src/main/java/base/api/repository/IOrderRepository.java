package base.api.repository;

import base.api.model.CategoryModel;
import base.api.model.OrderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderRepository extends JpaRepository<OrderModel, Long>, JpaSpecificationExecutor<OrderModel> {
}
