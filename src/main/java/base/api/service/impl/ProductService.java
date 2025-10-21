package base.api.service.impl;

import base.api.dto.request.ProductDto;
import base.api.model.CategoryModel;
import base.api.model.ProductModel;
import base.api.repository.ICategoryRepository;
import base.api.repository.IProductRepository;
import base.api.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IProductService {

    @Autowired
    private ICategoryRepository categoryRepository;

    @Autowired
    private IProductRepository productRepository;

    @Override
    public ProductModel createProduct(ProductDto dto) {
        // có tồn tại category hay ko
        CategoryModel categoryModel = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + dto.getCategoryId()));

        ProductModel newProduct = new ProductModel();
        newProduct.setName(dto.getName());
        newProduct.setDescription(dto.getDescription());
        newProduct.setPrice(dto.getPrice());
        newProduct.setSalePrice(dto.getSalePrice());
        newProduct.setQuantity(dto.getQuantity());
        newProduct.setImage(dto.getImage());
        newProduct.setAllowSale(dto.isAllowSale());
        newProduct.setCategoryModel(categoryModel);
        return productRepository.save(newProduct);

    }

    @Override
    public ProductModel updateProduct(ProductDto dto) {
        ProductModel existingProduct = productRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + dto.getId()));

        existingProduct.setName(dto.getName());
        existingProduct.setDescription(dto.getDescription());
        existingProduct.setPrice(dto.getPrice());
        existingProduct.setSalePrice(dto.getSalePrice());
        existingProduct.setImage(dto.getImage());
        existingProduct.setQuantity(dto.getQuantity());
        existingProduct.setAllowSale(dto.isAllowSale());

        return productRepository.save(existingProduct);
    }

    @Override
    public List<ProductModel> getListProduct() {
        return productRepository.findAll();
    }

    @Override
    public String deleteProduct(Long id) {
        // tim sp  có tồn tại hay không
        ProductModel existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

//        productRepository.deleteById(id);
        productRepository.delete(existingProduct);

        return "Delete successfully";
    }
}
