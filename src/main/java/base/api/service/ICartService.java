package base.api.service;

import base.api.dto.request.CartDto;
import base.api.model.CartModel;

public interface ICartService {
    CartModel addItemToCart(CartDto dto);
    CartModel getCartByUserId(Long userId);
}
