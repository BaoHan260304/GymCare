package base.api.service;

import base.api.model.CartItemModel;

import java.util.List;

public interface ICartItemService {
    List<CartItemModel> getCartItemsByCartId(Long cartId);
}
