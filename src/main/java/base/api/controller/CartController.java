package base.api.controller;

import base.api.base.BaseAPIController;
import base.api.dto.request.CartDto;
import base.api.dto.response.TFUResponse;
import base.api.model.CartItemModel;
import base.api.model.CartModel;
import base.api.service.ICartItemService;
import base.api.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController extends BaseAPIController {

    @Autowired
    private ICartService cartService;

    @Autowired
    private ICartItemService cartItemService;

    @PostMapping("/add-item-to-cart")
    public ResponseEntity<TFUResponse<CartModel>> addItemToCart(@RequestBody CartDto dto) {
        try {
            Long userId = getCurrentUserId();
            dto.setUserId(userId);
            CartModel cartModel = cartService.addItemToCart(dto);
            if (cartModel == null) {
                return badRequest("Không tìm thấy user hoặc sản phẩm");
            }
            return success(cartModel);
        } catch (Exception e) {
            return badRequest("Không thêm được sản phẩm vào giỏ hàng");
        }
    }

    @GetMapping("/get-cart-by-user-id")
    public ResponseEntity<TFUResponse<CartModel>> getCartByUserId() {
        try {
            Long userId = getCurrentUserId();
            CartModel cartModel = cartService.getCartByUserId(userId);
            if (cartModel == null) {
                return badRequest("Không tìm thấy giỏ hàng");
            }
            return success(cartModel);
        } catch (Exception e) {
            return badRequest("Không tìm thấy giỏ hàng");
        }
    }

    @GetMapping("/get-cart-items")
    public ResponseEntity<TFUResponse<List<CartItemModel>>> getListCartItemsByCartId(@RequestParam Long cartId) {
        try {
            List<CartItemModel> cartItems = cartItemService.getCartItemsByCartId(cartId);
            return success(cartItems);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return badRequest("Đã có lỗi xảy ra");
        }
    }

}
