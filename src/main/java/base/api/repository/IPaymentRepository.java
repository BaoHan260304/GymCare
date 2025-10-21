package base.api.repository;

import base.api.model.CategoryModel;
import base.api.model.PaymentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IPaymentRepository extends JpaRepository<PaymentModel, Long>, JpaSpecificationExecutor<PaymentModel> {
}
