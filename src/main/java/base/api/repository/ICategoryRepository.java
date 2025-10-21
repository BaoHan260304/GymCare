package base.api.repository;

import base.api.model.CategoryModel;
import base.api.model.ProductModel;
import base.api.model.user.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends JpaRepository<CategoryModel, Long>, JpaSpecificationExecutor<CategoryModel> {
}
