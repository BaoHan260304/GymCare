package base.api.controller;


import base.api.base.BaseAPIController;
import base.api.dto.request.ProductDto;
import base.api.dto.response.TFUResponse;
import base.api.model.ProductModel;
import base.api.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController extends BaseAPIController {

    @Autowired
    private IProductService productService;

    @PostMapping("create-product")
    public ResponseEntity<TFUResponse<ProductModel>> createProduct(@RequestBody ProductDto dto) {
        ProductModel productModel = productService.createProduct(dto);
        if (productModel == null) {
            return badRequest("Can't create product");
        }
        return success(productModel);
    }

    @PutMapping("update-product")
    public ResponseEntity<TFUResponse<ProductModel>> updateProduct(@RequestBody ProductDto dto){
        ProductModel productModel = productService.updateProduct(dto);
        if(productModel == null){
            return badRequest("Can't update product");
        }
        return success(productModel);
    }

    @GetMapping("get-list-product")
    public ResponseEntity<TFUResponse<List<ProductModel>>> getListProduct(){
        List<ProductModel> listProduct = productService.getListProduct();
        return success(listProduct);
    }

    @DeleteMapping("delete-product")
    public ResponseEntity<TFUResponse<String>> deleteProduct(@RequestParam Long id){
        String result = productService.deleteProduct(id);
        return success(result);
    }

}