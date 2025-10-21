package base.api.service;


import base.api.dto.request.CreateCategoryDto;
import base.api.model.CategoryModel;
import base.api.model.ProductModel;

public interface ICategoryService {
    CategoryModel getById(Long id);
    CategoryModel create(CreateCategoryDto dto);
}
