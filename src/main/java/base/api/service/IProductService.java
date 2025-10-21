package base.api.service;


import base.api.dto.request.ProductDto;
import base.api.model.ProductModel;

import java.util.List;

public interface IProductService {
    ProductModel createProduct(ProductDto dto);
    ProductModel updateProduct(ProductDto dto);
    List<ProductModel> getListProduct();
    String deleteProduct(Long id);
}
