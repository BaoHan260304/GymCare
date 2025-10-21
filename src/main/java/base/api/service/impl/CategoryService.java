package base.api.service.impl;

import base.api.dto.request.CreateCategoryDto;
import base.api.model.CategoryModel;
import base.api.model.ProductModel;
import base.api.model.user.UserModel;
import base.api.repository.ICategoryRepository;
import base.api.repository.IProductRepository;
import base.api.repository.IUserRepository;
import base.api.service.ICategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    private ICategoryRepository categoryRepository;

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private IUserRepository userRepository;

    @Override
    public CategoryModel getById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public CategoryModel create(CreateCategoryDto dto) {
        UserModel user = userRepository.findById(dto.getUserId()).orElse(null);
        if (user == null) {
            return null;
        }

        CategoryModel model = new CategoryModel();
        model.setName(dto.getName());
        model.setDescription(dto.getDescription());
        model.setUserModel(user);
        return categoryRepository.save(model);
    }


}
