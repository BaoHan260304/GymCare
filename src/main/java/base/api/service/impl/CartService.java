package base.api.service.impl;

import base.api.dto.request.CartDto;
import base.api.model.CartItemModel;
import base.api.model.CartModel;
import base.api.model.ProductModel;
import base.api.model.user.UserModel;
import base.api.repository.ICartItemRepository;
import base.api.repository.ICartRepository;
import base.api.repository.IProductRepository;
import base.api.repository.IUserRepository;
import base.api.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class CartService implements ICartService {

    @Autowired
    private ICartRepository cartRepository;

    @Autowired
    private ICartItemRepository cartItemRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IProductRepository productRepository;

    @Override
    public CartModel addItemToCart(CartDto dto) {

        UserModel existingUser = userRepository.findById(dto.getUserId()).orElse(null);
        if (existingUser == null) {
            return null; // phát triển thêm cart cho Guest mua ko cần đăng nhập
        }

        ProductModel existingProduct = productRepository.findById(dto.getProductId()).orElse(null);
        if (existingProduct == null) {
            throw new RuntimeException("Sản phẩm không tồn tại");
        }

        CartModel existingCart = cartRepository.findByUserId(dto.getUserId()).orElse(null);

        if (existingCart == null) {

//            B1: Tao gio hang moi
            CartModel newCart = new CartModel();
            newCart.setTotalPrice(existingProduct.getPrice());
            newCart.setUserId(existingUser.getId());
            cartRepository.save(newCart);

//            B2: Them san pham vao gio hang
            CartItemModel cartItem = new CartItemModel();
            cartItem.setProductId(existingProduct.getId());
            cartItem.setProductName(existingProduct.getName());
            cartItem.setProductPrice(existingProduct.getPrice());
            cartItem.setCartModel(newCart);
            cartItemRepository.save(cartItem);

            return newCart;
        } else {
            if (dto.isAdd()) {
                double updatedTotalPrice = existingCart.getTotalPrice() + existingProduct.getPrice();
                existingCart.setTotalPrice(updatedTotalPrice);
                cartRepository.save(existingCart);
                CartItemModel cartItem = new CartItemModel();
                cartItem.setProductId(existingProduct.getId());
                cartItem.setProductName(existingProduct.getName());
                cartItem.setProductPrice(existingProduct.getPrice());
                cartItem.setCartModel(existingCart);
                cartItemRepository.save(cartItem);
                return existingCart;
            }
        }

        return null;
    }

    @Override
    public CartModel getCartByUserId(Long userId) {
        UserModel existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));

        CartModel existingCart = cartRepository.findByUserId(userId).orElse(null);

        if (existingCart == null) {
            CartModel newCart = new CartModel();
            newCart.setUserId(existingUser.getId());
            newCart.setTotalPrice(0.0);
            cartRepository.save(newCart);
            return newCart;
        }

        existingCart.setCartItems(cartItemRepository.findByCartModel_Id(existingCart.getId()));

        double total = existingCart.getCartItems().stream()
                .mapToDouble(CartItemModel::getProductPrice)
                .sum();
        existingCart.setTotalPrice(total);
        cartRepository.save(existingCart);

        return existingCart;
    }

//    @Override
//    public CartModel addItemToCartForGuest(CartDto dto) {
//        ProductModel existingProduct = productRepository.findById(dto.getProductId()).orElse(null);
//        if (existingProduct == null) {
//            throw new RuntimeException("Sản phẩm không tồn tại");
//        }
//
//        // For guest, we create a new cart every time or use a session-based cart
//        // For simplicity, let's create a new cart for each guest request
//        CartModel newCart = new CartModel();
//        newCart.setTotalPrice(existingProduct.getPrice());
//        // No userId for guest cart
//        cartRepository.save(newCart);
//
//        CartItemModel cartItem = new CartItemModel();
//        cartItem.setProductId(existingProduct.getId());
//        cartItem.setProductName(existingProduct.getName());
//        cartItem.setProductPrice(existingProduct.getPrice());
//        cartItem.setCartModel(newCart);
//        cartItemRepository.save(cartItem);
//
//        return newCart;
//    }

//        get() ở đây bị cảnh báo vì usermodel sử dụng findbyid của jparepo.
//        nó trả về một optional<model> chứ ko phải model trực tiếp của mình nên bị cảnh báo ko an toàn nếu optional rỗng

//        cách 1: dùng orElse(null) thay vì get()
//         ProductModel existingProduct = productRepository.findById(dto.getProductId()).orElse(null);
//        if (existingProduct == null) {
//            return null;
//        }

//        cách 2: dùng ifPresent() hoặc isPresent()
//        Optional<ProductModel> optionalProduct = productRepository.findById(dto.getProductId());
//        if (optionalProduct.isPresent()) {
//            ProductModel existingProduct = optionalProduct.get();
//            // xử lý tiếp
//        } else {
//            return null;
//        }

//        cách 3: dùng orElseThrow() để báo lỗi rõ ràng hơn
//        ProductModel existingProduct = productRepository.findById(dto.getProductId())
//                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy sản phẩm với ID: " + dto.getProductId()));
}
